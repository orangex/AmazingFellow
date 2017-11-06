package com.orangex.amazingfellow.features.homepage.recent;

import android.util.Log;

import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.constant.Config;
import com.orangex.amazingfellow.constant.PrefKeys;
import com.orangex.amazingfellow.features.homepage.recent.dota.DotaMatchModel;
import com.orangex.amazingfellow.network.RetrofitHelper;
import com.orangex.amazingfellow.network.steam.ISteamApiService;
import com.orangex.amazingfellow.network.steam.dota.DotaMatchDetailResultBean;
import com.orangex.amazingfellow.network.steam.dota.DotaMatchDetailResultBean.ResultBean.PlayersBean;
import com.orangex.amazingfellow.network.steam.dota.DotaMatchHistoryResultBean;
import com.orangex.amazingfellow.network.steam.dota.DotaMatchHistoryResultBean.ResultBean.MatchesBean;
import com.orangex.amazingfellow.rx.ApiException;
import com.orangex.amazingfellow.utils.SteamUtil;
import com.white.easysp.EasySP;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by chengyuan.wang on 2017/10/31.
 */

public class RecentDataHelper {// TODO: 2017/11/1 可配置次数的重试。因为并不是所有的网络请求都需要频繁地重试，比如说一个重要的表单提交，它应该尽可能多失败重连，相反地，埋点上报等统计功能，它可能最多只需要重试一次就足够了。因此针对不同的场景，我们需要不同的重试次数。退避策略。
    public static final String TAG = RecentDataHelper.class.getSimpleName();
    
    public static Observable<MatchModel> getDotaMoments() {
    
        Set<Observable<String>> friendMVPMatchObservables = new HashSet<>();
        for (String friendId :EasySP.init(AFApplication.getAppContext()).getStringSet(PrefKeys.DOTA_FRIEND)
                ) {
             Observable.just(friendId)
                     .map(new Function<String, Document>() {
                         int page = 0;
                         int maxpage = 0;
                         @Override
                         public Document apply(String id) throws Exception {
                             page++;
                             Document document = Jsoup.connect(String.format("http://dotamore.com/match/matchlist/%s.html?p=%d", SteamUtil.getSteamID32By64(id), page)).get();
                             if (document != null) {
                                 try {
                                     maxpage = Integer.parseInt(document.select("div[class=match_pages] > a[title=下一页]").get(0).previousElementSibling().text());
                                 } catch (NullPointerException | IndexOutOfBoundsException e) {
                                     maxpage = Config.MAX_PAGE_COUNT_TO_SPIDE;
                                     throw e;
                                 }
                                 if (page > maxpage) {
                                     document = null;
                                 }
                                 
                             }
                             return document;
                         }
                     })
                     .repeat()
                     .takeWhile(new Predicate<Document>() {
                         @Override
                         public boolean test(Document document) throws Exception {
                             return document != null;
                         }
                     })
                     .concatMap(new Function<Document, ObservableSource<MatchModel>>() {
                         @Override
                         public ObservableSource<MatchModel> apply(Document document) throws Exception {
                             List<MatchModel> matchModels = new ArrayList<>();
                             Elements elements = document.select("td[style] > div[class=match_index_lately_mvp]");
                             for (Element element:elements
                                  ) {
                                 DotaMatchModel model = new DotaMatchModel(MatchModel.TYPE_DOTA);
                                 model.set
                                 
                             }
                             
                             
                             return null;
                         }
                     })
                     
                    
              
              
            
            friendMVPMatchObservables.add(Observable.just(friendId));
        }
        
        final long[] latestQueryingMatchID = {-1L};
        Observable.merge()
        return Observable.fromIterable()
               .me
               
               
               
               
//                .defer(new Callable<ObservableSource<DotaMatchHistoryResultBean>>() {// TODO: 2017/11/2 rxLifeCircle
//            @Override
//            public ObservableSource<DotaMatchHistoryResultBean> call() throws Exception {
//                String steamId = EasySP.init(AFApplication.getAppContext()).getString(PrefKeys.STEAM_ID);
//                if (TextUtils.isEmpty(steamId)) {
//                    throw new ResponseException("找不到 Steam ID，请重新绑定");
//                }
//                if (latestQueryingMatchID[0] == -1L) {
//                    return RetrofitHelper.getService(ISteamApiService.class).getMatchHistory(Config.STEAM_API_KEY, steamId);
//                } else {
//                    return RetrofitHelper.getService(ISteamApiService.class).getMatchHistory(Config.STEAM_API_KEY, steamId, latestQueryingMatchID[0]);
//                }
//            }
//        })
                .repeat()
                .takeUntil(new Predicate<DotaMatchHistoryResultBean>() {
                    @Override
                    public boolean test(DotaMatchHistoryResultBean resultBean) throws Exception {
                        Log.d(TAG, "remaining " + resultBean.getResult().getResults_remaining());
                        List<MatchesBean> matchesBeans = resultBean.getResult().getMatches();
                        if (resultBean.getResult().getResults_remaining() <= 0) {
                            return true;
                        } else {
                            latestQueryingMatchID[0] = matchesBeans.get(matchesBeans.size() - 1).getMatch_id();
                            return false;
                        }
                    }
                })
                .concatMap(new Function<DotaMatchHistoryResultBean, ObservableSource<MatchesBean>>() {
                    @Override
                    public ObservableSource<MatchesBean> apply(DotaMatchHistoryResultBean resultBean) throws Exception {
                        Log.d(TAG, "apply: flat to " + resultBean.getResult().getMatches().size());
                        return Observable.fromIterable(resultBean.getResult().getMatches());
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
                        String steamId32 = EasySP.init(AFApplication.getAppContext()).getString(PrefKeys.STEAM_ID_32);
                        Document document = Jsoup.connect(Config.URL_DOTA_MATCH_DETAIL_PREFIX + matchesBean.getMatch_id() ).get();
                        Elements elements = document.select(String.format("tr[url=%s] div[data-tip=%s]", steamId32, "match_mvp_tip"));
                        if (elements != null && elements.size() == 1) {
                            return true;
                        }
                        return false;
                    }
                })
                .flatMap(new Function<MatchesBean, ObservableSource<MatchModel>>() {
                    @Override
                    public ObservableSource<MatchModel> apply(MatchesBean matchesBean) throws Exception {
                        final DotaMatchModel dotaMatchModel = new DotaMatchModel(MatchModel.TYPE_DOTA, matchesBean.getStart_time());
                        return RetrofitHelper.getService(ISteamApiService.class).getMatchDetail(Config.STEAM_API_KEY, matchesBean.getMatch_id())
                                .map(new Function<DotaMatchDetailResultBean, MatchModel>() {
                                    @Override
                                    public MatchModel apply(DotaMatchDetailResultBean resultBean) throws Exception {
                                        dotaMatchModel.setDuration(resultBean.getResult().getDuration());
                                        int steam32 = Integer.parseInt(EasySP.init(AFApplication.getAppContext()).getString(PrefKeys.STEAM_ID_32));
                                        PlayersBean myPlayerBean = null;
                                        for (PlayersBean playersBean : resultBean.getResult().getPlayers()
                                                ) {
                                            if (playersBean.getAccount_id() == steam32) {
                                                myPlayerBean = playersBean;
                                                break;
                                            }
                                        }
                                        if (myPlayerBean == null) {
                                            throw new ApiException("找不到比赛该玩家的数据");
                                        }
                                    
                                        dotaMatchModel.setKills(myPlayerBean.getKills());
                                        dotaMatchModel.setDeaths(myPlayerBean.getDeaths());
                                        dotaMatchModel.setAssits(myPlayerBean.getAssists());
                                        dotaMatchModel.setEpm(myPlayerBean.getXp_per_min());
                                        dotaMatchModel.setGold(myPlayerBean.getGold());
                                        dotaMatchModel.setGpm(myPlayerBean.getGold_per_min());
                                        dotaMatchModel.setScaledDamage(myPlayerBean.getScaled_hero_damage());
                                        return dotaMatchModel;
                                    }
                                });
                    }
                });
    }
    public static void getRecentMVPMoments(SingleObserver<List<MatchModel>> observer) {
        Observable.merge(getDotaMoments(), getOWMoments(), getPUBGMoments(), getCSGOMoments())
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
                        return (int) (matchModel.getStartAt()-compareTo.getStartAt());
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
