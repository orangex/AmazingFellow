package com.orangex.amazingfellow.features.bind;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.constant.Config;
import com.orangex.amazingfellow.constant.PrefKeys;
import com.orangex.amazingfellow.network.RetrofitHelper;
import com.orangex.amazingfellow.network.steam.FriendListResultBean;
import com.orangex.amazingfellow.network.steam.FriendListResultBean.FriendslistBean.FriendsBean;
import com.orangex.amazingfellow.network.steam.ISteamApiService;
import com.orangex.amazingfellow.network.steam.ResolveVanityURLResultBean;
import com.orangex.amazingfellow.rx.ApiException;
import com.orangex.amazingfellow.rx.ResponseException;
import com.white.easysp.EasySP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chengyuan.wang on 2017/10/26.
 */

public class BindManager {
    public static final String TAG = BindManager.class.getSimpleName();
    public static void showBindDialog(View fromClick, Activity activity) {

    }

    public static void bindSteamId(String input,Observer<List<String>> observer) {
        Observable.just(input)
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String input) throws Exception {
                        Matcher matcherProfiles = Pattern.compile(".*steamcommunity\\.com/profiles/(\\d+)").matcher(input);
                        Matcher matcherID = Pattern.compile(".*steamcommunity\\.com/id/(.+?)/?").matcher(input);
                        if (matcherProfiles.matches()) {
                            return Observable.just(matcherProfiles.group(1));
                        } else if (matcherID.matches()) {
                            return getSteamIdByVanityUrl(matcherID.group(1))
                                    .map(new Function<ResolveVanityURLResultBean, String>() {
                                        @Override
                                        public String apply(ResolveVanityURLResultBean data) throws Exception {
                                            if (data.getResponse().getSuccess() == ResolveVanityURLResultBean.CODE_SUCCESSFUL) {
                                                Log.d(TAG, "1,resovle vanity seccess ");
                                                return data.getResponse().getSteamid();
                                            } else if (data.getResponse().getSuccess() == ResolveVanityURLResultBean.CODE_NO_MATCH) {
                                                throw new ApiException("找不到匹配", ResolveVanityURLResultBean.CODE_NO_MATCH, AFApplication.getAppContext().getString(R.string.error_steam_vanityurl_api_match_failed));
                                            } else {
                                                throw new ApiException("未知的 success code ",data.getResponse().getSuccess(), AFApplication.getAppContext().getString(R.string.error_steam_vanityurl_api_failed_unkown));
                                            }
                                        }
                                    });
                        } else {
                            throw new ResponseException("非法链接",AFApplication.getAppContext().getString(R.string.error_steam_vanityurl_pattern_match_failed));
                        }
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String steamId) throws Exception {
                        long steamID32 = Long.valueOf(steamId) - Config.STEAM_ID_64_TO_32;
                        EasySP.init(AFApplication.getAppContext()).putString(PrefKeys.STEAM_ID, steamId);
                        EasySP.init(AFApplication.getAppContext()).putString(PrefKeys.STEAM_ID_32, Long.toString(steamID32));
                        Log.d(TAG, "2,steam 32 build");
                    }
                })
                .flatMap(new Function<String, ObservableSource<FriendListResultBean>>() {
                    @Override
                    public ObservableSource<FriendListResultBean> apply(String steamId) throws Exception {
                        return getFriendList(steamId);
                    }
                })
                .map(new Function<FriendListResultBean, List<String>>() {
                    @Override
                    public List<String> apply(FriendListResultBean friendListResultBean) throws Exception {
                        List<String> friendList = new ArrayList<>();
                        for (FriendsBean friendsBean : friendListResultBean.getFriendslist().getFriends()
                                ) {
                            friendList.add(friendsBean.getSteamid());
                        }
                        return friendList;
                    }
                })
                .doOnNext(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> friendList) throws Exception {
                        EasySP.init(AFApplication.getAppContext()).putStringSet(PrefKeys.DOTA_FRIEND, new HashSet<String>(friendList));
                    }
                })
                .onErrorReturn(new Function<Throwable, List<String>>() {
                    @Override
                    public List<String> apply(Throwable throwable) throws Exception {
                        Log.d(TAG, "3,throwable found");
                        throw ResponseException.generateResponseException((Exception) throwable);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    
    private static Observable<ResolveVanityURLResultBean> getSteamIdByVanityUrl(String nickname) {
        return RetrofitHelper.getService(ISteamApiService.class).getSteamIdByVanityUrl(Config.STEAM_API_KEY, nickname);
    }
    
    private static ObservableSource<FriendListResultBean> getFriendList(String steamId) {
        return RetrofitHelper.getService(ISteamApiService.class).getFriendList(Config.STEAM_API_KEY, steamId, "friend");
    }
}
