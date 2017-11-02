package com.orangex.amazingfellow.features.homepage.recent;

import android.text.TextUtils;

import com.orangex.amazingfellow.AFApplication;
import com.orangex.amazingfellow.constant.PrefKeys;
import com.orangex.amazingfellow.network.RetrofitHelper;
import com.orangex.amazingfellow.network.steam.ISteamApiService;
import com.orangex.amazingfellow.network.steam.MatchHistoryResultData;
import com.orangex.amazingfellow.network.steam.MatchHistoryResultData.ResultBean.MatchesBean;
import com.white.easysp.EasySP;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by chengyuan.wang on 2017/10/31.
 */

public class RecentDataHelper {// TODO: 2017/11/1 可配置次数的重试。因为并不是所有的网络请求都需要频繁地重试，比如说一个重要的表单提交，它应该尽可能多失败重连，相反地，埋点上报等统计功能，它可能最多只需要重试一次就足够了。因此针对不同的场景，我们需要不同的重试次数。退避策略。
    public void getRecentData(Observer observer) {
        final long[] latestQueryingMatchID = {getMostRecentMatchID()};
        Observable.defer(new Callable<ObservableSource<MatchHistoryResultData>>() {// TODO: 2017/11/2 rxLifeCircle
            @Override
            public ObservableSource<MatchHistoryResultData> call() throws Exception {
                String steamId = EasySP.init(AFApplication.getAppContext()).getString(PrefKeys.KEY_STEAM_ID);
                if (TextUtils.isEmpty(steamId)) {
                    throw new Exception("找不到 Steam ID，请重新绑定");
                }
                return RetrofitHelper.getService(ISteamApiService.class).getMatchHistory(steamId, latestQueryingMatchID[0]);
            }
        })
                .repeat()
                .takeUntil(new Predicate<MatchHistoryResultData>() {
                    @Override
                    public boolean test(MatchHistoryResultData matchHistoryResultData) throws Exception {
                        List<MatchesBean> matchesBeans = matchHistoryResultData.getResult().getMatches();
                        if (matchesBeans.get(matchesBeans.size()).getMatch_id() >= getMostRecentMatchID() || matchHistoryResultData.getResult().getResults_remaining() <= 0) {
                            return true;
                        } else {
                            latestQueryingMatchID[0] = matchesBeans.get(matchesBeans.size()).getMatch_id();
                            return false;
                        }
                    }
                })
                .concatMap(new Function<MatchHistoryResultData, ObservableSource<MatchesBean>>() {
                    @Override
                    public ObservableSource<MatchesBean> apply(MatchHistoryResultData matchHistoryResultData) throws Exception {
                        return Observable.fromIterable(matchHistoryResultData.getResult().getMatches());
                    }
                })
                .takeWhile(new Predicate<MatchesBean>() {
                    @Override
                    public boolean test(MatchesBean matchesBean) throws Exception {
                        return matchesBean.getMatch_id() > getMostRecentMatchID();
                    }
                })
                .distinct(new Function<MatchesBean, Object>() {
                    @Override
                    public Object apply(MatchesBean matchesBean) throws Exception {
                        return matchesBean.getMatch_id();
                    }
                })
                .filter(new Predicate<MatchesBean>() {
                    @Override
                    public boolean test(MatchesBean matchesBean) throws Exception {
                        
                        
                        
                        
                        
                        return false;
                    }
                })
                .collect(new Callable<List<MatchesBean>>() {
                    @Override
                    public List<MatchesBean> call() throws Exception {
                        return null;
                    }
                }, new BiConsumer<List<MatchesBean>, MatchHistoryResultData>() {
                    @Override
                    public void accept(List<MatchesBean> matchesBeans, MatchHistoryResultData matchHistoryResultData) throws Exception {
                        long mostRecentID = getMostRecentMatchID();
                        List<MatchesBean> pendingMatchBeans = matchHistoryResultData.getResult().getMatches();
                        MatchesBean endOfAddedMatchBeans;
                        if (matchesBeans.size() > 0) {
                            endOfAddedMatchBeans = matchesBeans.get(matchesBeans.size());
                            if (endOfAddedMatchBeans.getMatch_id() == pendingMatchBeans.get(0).getMatch_id()) {
                                matchesBeans.remove(endOfAddedMatchBeans);
                            }
                        }
                        int loc = pendingMatchBeans.size();
                        for (int i=pendingMatchBeans.size()-1; i>=0;i--) {
                            if (pendingMatchBeans.get(i).getMatch_id() > mostRecentID) {
                                loc = i+1;
                                break;
                            }
                        }
                        pendingMatchBeans = pendingMatchBeans.subList(0, loc);
                        matchesBeans.addAll(pendingMatchBeans);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<MatchesBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
        
                    }
    
                    @Override
                    public void onSuccess(List<MatchesBean> matchesBeans) {
        
                    }
    
                    @Override
                    public void onError(Throwable e) {
        
                    }
                })
        String steamId = ;
        if (TextUtils.isEmpty(steamId)) {

        }
    }

    private long getMostRecentMatchID() {
        return ;
    }
}
