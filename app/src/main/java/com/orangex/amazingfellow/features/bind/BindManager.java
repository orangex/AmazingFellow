package com.orangex.amazingfellow.features.bind;

import android.app.Activity;
import android.view.View;

import com.orangex.amazingfellow.AFApplication;
import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.constant.Config;
import com.orangex.amazingfellow.constant.PrefKeys;
import com.orangex.amazingfellow.rx.ApiException;
import com.orangex.amazingfellow.rx.ResponseException;
import com.orangex.amazingfellow.network.RetrofitHelper;
import com.orangex.amazingfellow.network.steam.ISteamApiService;
import com.orangex.amazingfellow.network.steam.ResolveVanityURLResultData;
import com.white.easysp.EasySP;

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
    public static void showBindDialog(View fromClick, Activity activity) {

    }

    public static void bindSteamId(String input,Observer observer) {
        Observable.just(input)
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<String> apply(String input) throws Exception {
                        Matcher matcherProfiles = Pattern.compile(".*steamcommunity\\.com/profiles/(\\d+)").matcher(input);
                        Matcher matcherID = Pattern.compile(".*steamcommunity\\.com/id/(.+)").matcher(input);
                        if (matcherProfiles.matches()) {
                            return Observable.just(matcherProfiles.group(1));
                        } else if (matcherID.matches()) {
                            ISteamApiService steamApiService = RetrofitHelper.getService(ISteamApiService.class);
                            return steamApiService.getSteamIdByVanityUrl(Config.STEAM_API_KEY, matcherID.group(1))
                                    .map(new Function<ResolveVanityURLResultData, String>() {
                                        @Override
                                        public String apply(ResolveVanityURLResultData data) throws Exception {
                                            if (data.getSuccess() == ResolveVanityURLResultData.CODE_SUCCESSFUL) {
                                                return data.getSteamid();
                                            } else if (data.getSuccess() == ResolveVanityURLResultData.CODE_NO_MATCH) {
                                                throw new ApiException(ResolveVanityURLResultData.CODE_NO_MATCH, AFApplication.getAppContext().getString(R.string.error_steam_vanityurl_api_match_failed))
                                            } else {
                                                throw new ApiException(data.getSuccess(), AFApplication.getAppContext().getString(R.string.error_steam_vanityurl_api_failed_unkown));
                                            }
                                        }
                                    });
                        } else {
                            throw new Exception(AFApplication.getAppContext().getString(R.string.error_steam_vanityurl_pattern_match_failed));
                        }
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String steamId) throws Exception {
                        EasySP.init(AFApplication.getAppContext()).putString(PrefKeys.KEY_STEAM_ID, steamId);
                    }
                })
                .onErrorReturn(new Function<Exception, Exception>() {
                    @Override
                    public Exception apply(Exception e) throws Exception {
                        throw ResponseException.generateResponseException(e);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((observer);
    }
}
