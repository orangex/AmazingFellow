package com.orangex.amazingfellow.features.homepage.recent;

import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.constant.PrefKeys;
import com.orangex.amazingfellow.features.homepage.recent.dota.RecentDotaMatchHelper;
import com.white.easysp.EasySP;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;


/**
 * Created by chengyuan.wang on 2017/10/31.
 */

public class RecentDataHelper {// TODO: 2017/11/1 可配置次数的重试。因为并不是所有的网络请求都需要频繁地重试，比如说一个重要的表单提交，它应该尽可能多失败重连，相反地，埋点上报等统计功能，它可能最多只需要重试一次就足够了。因此针对不同的场景，我们需要不同的重试次数。退避策略。
    public static final String TAG = RecentDataHelper.class.getSimpleName();
    private static List<MatchModel> sLastTimeLine = null;
    


    public static void getRecentMVPMoments(Observer<List<MatchModel>> observer) {
        //Observable.mergeDelayError(RecentDotaMatchHelper.getDotaMoments(), getOWMoments(), getPUBGMoments(), getCSGOMoments())
        RecentDotaMatchHelper.getDotaMoments(false);
        
                //                .take(Config.MAX_MATCH_COUNT_LOAD_ONCE)
                //                .take(5, TimeUnit.SECONDS)
                //                .takeWhile(new Predicate<MatchModel>() {
                //                    @Override
                //                    public boolean test(MatchModel matchModel) throws Exception {
                //                        return matchModel.getStartAt() > getLoacalLatestMatchTime();
                //                    }
                //                })
        
        refreshUI(observer);
        
        
        
//                .toSortedList(new Comparator<MatchModel>() {
//                    @Override
//                    public int compare(MatchModel matchModel, MatchModel compareTo) {
//                        Log.d(TAG, "compare: " + matchModel.getStartAt() + " " + compareTo.getStartAt());
//                        return (int) (matchModel.getStartAt() - compareTo.getStartAt());
//                    }
//                })
//                .map(new Function<List<MatchModel>, List<MatchModel>>() {
//                    @Override
//                    public List<MatchModel> apply(List<MatchModel> matchModels) throws Exception {
//                        if (sLastTimeLine == null) {
//                            return matchModels;
//                        }
//                        Pair<Boolean, List<MatchModel>> pair = hasReachedLastTimeline(matchModels);
//                        if (pair.first) {
//                            matchModels.addAll(sLastTimeLine);
//                        }
//                        return matchModels;
//                    }
//                })
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
    }
    
    private static void refreshUI(Observer<List<MatchModel>> observer) {
        Observable.timer(8,TimeUnit.SECONDS)
                .flatMap(new Function<Long, ObservableSource<List<MatchModel>>>() {
                    @Override
                    public ObservableSource<List<MatchModel>> apply(Long aLong) throws Exception {
                        return Observable.just(getCachedOverviews());
                    }
                })
                .map(new Function<List<MatchModel>, List<MatchModel>>() {
                    @Override
                    public List<MatchModel> apply(List<MatchModel> matchModels) throws Exception {
                        Collections.sort(matchModels, new Comparator<MatchModel>() {
                            @Override
                            public int compare(MatchModel matchModel, MatchModel compareTo) {
                                return Long.valueOf(matchModel.getId()).compareTo(Long.valueOf(compareTo.getId()));
                            }
                        });
                        return matchModels;
                    }
                })
                .subscribe(observer);
    }
    
    private static List<MatchModel> getCachedOverviews() {
        return RecentDotaMatchHelper.getCachedTimeLine();
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

    public static void getRecentMVPMoments(List<String> strings, Observer<List<MatchModel>> getRecentDataObserver) {
        getRecentMVPMoments(getRecentDataObserver);
    }
    
    public static void onRecentDataChanged(List<MatchModel> dataList) {
//        sLastTimeLine = dataList;
//        for (int i=0; i<dataList.size();i++) {
//            MatchModel model = dataList.get(i);
//            if (model.getType() == MatchModel.TYPE_DOTA) {
//                RecentDotaMatchHelper.setLastLatestMatchID(model.getId());
//                break;
//            }
//        }
    }
    
//    private static Pair<Boolean,List<MatchModel>> hasReachedLastTimeline(List<MatchModel> matchModels) {
//
//        int indexForDota = -1;
//        boolean existDotaMatch = false;
//        int indexForOw = -1;
//        for (int i = matchModels.size() - 1; i >= 0; i--) {
//            MatchModel model = matchModels.get(i);
//            if (indexForDota == -1 && model.getType() == MatchModel.TYPE_DOTA) {
//                existDotaMatch = true;
//                if (model.getId().equals(RecentDotaMatchHelper.getLastLatestMatchID())) {
//                    indexForDota = i;
//                }
//            }
//            //                if (indexForOw == -1 && model.getType() == MatchModel.TYPE_OW) {
//            //                    if (model.getId().equals(RecentOWMatchHelper.getLastLatestMatchID())) {
//            //                        indexForOw = i;
//            //                    }
//            //                }
//        }
//
//        if (!existDotaMatch) {
//            return new Pair<>(Boolean.TRUE, matchModels);
//        }
//        if (indexForDota != -1) {
//            matchModels.remove(indexForDota);
//        }
//        return new Pair<>(Boolean.TRUE, matchModels);
//
//    }
}
