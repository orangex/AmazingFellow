package com.orangex.amazingfellow.features.homepage.recent;

import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.constant.Config;
import com.orangex.amazingfellow.constant.PrefKeys;
import com.orangex.amazingfellow.features.homepage.recent.dota.RecentDotaMatchHelper;
import com.white.easysp.EasySP;

import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by chengyuan.wang on 2017/10/31.
 */

public class RecentDataHelper {// TODO: 2017/11/1 可配置次数的重试。因为并不是所有的网络请求都需要频繁地重试，比如说一个重要的表单提交，它应该尽可能多失败重连，相反地，埋点上报等统计功能，它可能最多只需要重试一次就足够了。因此针对不同的场景，我们需要不同的重试次数。退避策略。
    public static final String TAG = RecentDataHelper.class.getSimpleName();



    public static void getRecentMVPMoments(SingleObserver<List<MatchModel>> observer) {
        Observable.merge(RecentDotaMatchHelper.getDotaMoments(), getOWMoments(), getPUBGMoments(), getCSGOMoments())
                .take(Config.MAX_MATCH_COUNT_LOAD_ONCE)
                .takeWhile(new Predicate<MatchModel>() {
                    @Override
                    public boolean test(MatchModel matchModel) throws Exception {
                        return matchModel.getStartAt() > getLoacalLatestMatchTime();
                    }
                })
                .toSortedList(new Comparator<MatchModel>() {
                    @Override
                    public int compare(MatchModel matchModel, MatchModel compareTo) {
                        return (int) (matchModel.getStartAt() - compareTo.getStartAt());
                    }
                })
                //                    @Override
                //                    public void accept(List<MatchesBean> matchesBeans, DotaMatchHistoryResultBean matchHistoryResultData) throws Exception {
                //                        long mostRecentID = getMostRecentMatchID();
                //                        List<MatchesBean> pendingMatchBeans = matchHistoryResultData.getResult().getMatches();
                //                        MatchesBean endOfAddedMatchBeans;
                //                        if (matchesBeans.size() > 0) {
                //                            endOfAddedMatchBeans = matchesBeans.get(matchesBeans.size());
                //                            if (endOfAddedMatchBeans.getMatch_id() == pendingMatchBeans.get(0).getMatch_id()) {
                //                                matchesBeans.remove(endOfAddedMatchBeans);
                //                            }
                //                        }
                //                        int loc = pendingMatchBeans.size();
                //                        for (int i=pendingMatchBeans.size()-1; i>=0;i--) {
                //                            if (pendingMatchBeans.get(i).getMatch_id() > mostRecentID) {
                //                                loc = i+1;
                //                                break;
                //                            }
                //                        }
                //                        pendingMatchBeans = pendingMatchBeans.subList(0, loc);
                //                        matchesBeans.addAll(pendingMatchBeans);
                //                    }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private static ObservableSource<MatchModel> getCSGOMoments() {
        return Observable.just(new MatchModel(MatchModel.TYPE_CSGO, System.currentTimeMillis()));
    }

    private static ObservableSource<MatchModel> getPUBGMoments() {
        return Observable.just(new MatchModel(MatchModel.TYPE_PUBG, System.currentTimeMillis()));
    }

    private static ObservableSource<MatchModel> getOWMoments() {
        return Observable.just(new MatchModel(MatchModel.TYPE_OW, System.currentTimeMillis()));
    }

    private static long getLoacalLatestMatchTime() {
        return EasySP.init(AFApplication.getAppContext()).getLong(PrefKeys.LATEST_MATCH_TIME);
    }

    public static void getRecentMVPMoments(List<String> strings, SingleObserver<List<MatchModel>> getRecentDataObserver) {
        getRecentMVPMoments(getRecentDataObserver);
    }

}
