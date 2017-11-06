package com.orangex.amazingfellow.features.homepage.recent.dota;

import android.util.Log;
import android.util.Pair;

import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.constant.Config;
import com.orangex.amazingfellow.constant.PrefKeys;
import com.orangex.amazingfellow.features.homepage.recent.MatchModel;
import com.orangex.amazingfellow.network.RetrofitHelper;
import com.orangex.amazingfellow.network.steam.ISteamApiService;
import com.orangex.amazingfellow.network.steam.dota.DotaMatchDetailResultBean;
import com.orangex.amazingfellow.network.steam.dota.DotaMatchDetailResultBean.ResultBean.PlayersBean;
import com.orangex.amazingfellow.rx.ApiException;
import com.orangex.amazingfellow.rx.ResponseException;
import com.orangex.amazingfellow.utils.SteamUtil;
import com.white.easysp.EasySP;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chengyuan.wang on 2017/11/6.
 */

public class RecentDotaMatchHelper {
    private static final String TAG = RecentDotaMatchHelper.class.getSimpleName();

    public static Observable<MatchModel> getDotaMoments() {

        Set<Observable<DotaMatchModel>> friendMVPMatchObservables = new HashSet<>();
        for (String friendId : EasySP.init(AFApplication.getAppContext()).getStringSet(PrefKeys.DOTA_FRIEND)
                ) {

            Observable<DotaMatchModel> observableFriend = Observable.just(friendId)
                    .flatMap(new Function<String, ObservableSource<DotaMatchModel>>() {
                        @Override
                        public ObservableSource<DotaMatchModel> apply(final String friendID64) throws Exception {
                            return Observable.just(friendID64)
                                    .map(new Function<String, Document>() {
                                        int page = 0;
                                        @Override
                                        public Document apply(String id) throws Exception {
                                            page++;
                                            Document document = Jsoup.connect(String.format("http://dotamore.com/match/matchlist/%s.html?p=%d", SteamUtil.getSteamID32By64(id), page)).get();
                                            if (document != null) {
                                                Log.d(TAG, id+" 访问页数 "+page);
                                                return document;
                                            } else {
                                                throw new ResponseException("jsoup document 没有拿到", "访问数据源异常，请检查网络");
                                            }
                                        }
                                    })
                                    .repeat()
                                    .takeWhile(new Predicate<Document>() {
                                        @Override
                                        public boolean test(Document document) throws Exception {
                                            boolean found404 = document.select("div[class=page404]").size() != 0;
                                            if (found404) {

                                                Log.d(TAG, document.baseUri() + " 404 found");
                                            } else {
                                                Log.d(TAG, document.baseUri()+"找到一名 dota 玩家！！");
                                            }
                                            return  !found404;
                                        }
                                    })
                                    .takeUntil(new Predicate<Document>() {
                                        @Override
                                        public boolean test(Document document) throws Exception {
                                            int count = 0;
                                            Matcher matcherForMatchTableSplit = Pattern.compile("<!-- 头像 -->").matcher(document.text());
                                            while (matcherForMatchTableSplit.find()) {
                                                count++;
                                            }
                                            return (count < 10);
//                                            Matcher matcherForPageNow = Pattern.compile(".*\\?p=(\\d+)").matcher(document.baseUri());
//                                            if (!matcherForPageNow.matches()) {
//                                                return false;
//                                            }
//                                            int pageNow = Integer.parseInt(matcherForPageNow.group(1));
//                                            Elements elements = document.select("div[class=match_pages] > a[title=尾页]");
//                                            if (elements.size() == 0) {
//
//                                                Log.d(TAG, "尾页页码未找到 " + document.baseUri() + document.select("div[class=match_pages]").toString());
//                                                return true;
//                                            }
//                                            Matcher matcherForPageMax = Pattern.compile(".*\\?p=(\\d+)").matcher(elements.get(0).attr("href"));
//                                            if (!matcherForPageMax.matches()) {
//                                                return false;
//                                            }
//                                            int maxPage = Integer.parseInt(matcherForPageMax.group(1));
//                                            Log.d(TAG, "当前页 " + pageNow + " 最大页 " + maxPage);
//                                            return pageNow >= maxPage;
                                        }
                                    })
                                    .concatMap(new Function<Document, ObservableSource<Pair<String, DotaMatchModel>>>() {
                                        @Override
                                        public ObservableSource<Pair<String, DotaMatchModel>> apply(Document document) throws Exception {
                                            List<Pair<String, DotaMatchModel>> matchModels = new ArrayList<>();
                                            Elements elements = document.select("div[class=match_index_lately_mvp_ico]");
                                            if (elements.size() > 0) {
                                                Log.d(TAG, "找到 mvp 的比赛"+elements.size()+" 场");
                                            }
                                            for (Element element : elements
                                                    ) {
                                                DotaMatchModel model = new DotaMatchModel(MatchModel.TYPE_DOTA);
                                                List<Integer> glorys;
                                                String time = null;
                                                try {
                                                    glorys = new ArrayList<>();
                                                    for (Element glory :
                                                            element.parent().nextElementSibling().children()) {
                                                        glorys.add(getGloryIDByClassName(glory.className()));
                                                    }
                                                    model.setGlorys(glorys);
                                                    model.setId(element.parent().parent().parent().attr("url"));
                                                    time = element.parent().parent().nextElementSibling().nextElementSibling().getElementsByClass("match_table_start_time_color").get(0).text();
                                                } catch (NullPointerException | IndexOutOfBoundsException e) {
                                                    Log.w(TAG, "抓取比赛 table split 出错", e);
                                                }
                                                model.setSteamID64(friendID64);

                                                matchModels.add(new Pair<String, DotaMatchModel>(time, model));
                                            }
                                            return Observable.fromIterable(matchModels);
                                        }
                                    })
                                    .takeWhile(new Predicate<Pair<String, DotaMatchModel>>() {
                                        @Override
                                        public boolean test(Pair<String, DotaMatchModel> stringMatchModelPair) throws Exception {

                                            if (stringMatchModelPair.first == null || stringMatchModelPair.first.contains("天")) {
                                                Log.d(TAG, "一天前的数据，不再 take ");
                                                return true;
                                            }
                                            Log.d(TAG, "一天内的数据 " + stringMatchModelPair.first);
                                            return true;
                                        }
                                    })
                                    .map(new Function<Pair<String, DotaMatchModel>, DotaMatchModel>() {
                                        @Override
                                        public DotaMatchModel apply(Pair<String, DotaMatchModel> stringMatchModelPair) throws Exception {

                                            return stringMatchModelPair.second;
                                        }
                                    })
                                    .subscribeOn(Schedulers.io());
                        }
                    });

            friendMVPMatchObservables.add(observableFriend);
        }

        return Observable.merge(friendMVPMatchObservables)
                .flatMap(new Function<MatchModel, ObservableSource<MatchModel>>() {
                    @Override
                    public ObservableSource<MatchModel> apply(final MatchModel matchModel) throws Exception {
                        return getMatchDetail(matchModel.getId())
                                .map(new Function<DotaMatchDetailResultBean, MatchModel>() {
                                    @Override
                                    public MatchModel apply(DotaMatchDetailResultBean resultBean) throws Exception {
                                        DotaMatchModel dotaMatchModel = (DotaMatchModel) matchModel;
                                        dotaMatchModel.setDuration(resultBean.getResult().getDuration());
                                        int steam32 = Integer.parseInt(SteamUtil.getSteamID32By64(dotaMatchModel.getSteamID64()));
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
                                        Log.d(TAG, "mvp match model " + dotaMatchModel.toString());
                                        return dotaMatchModel;
                                    }
                                });
                    }
                });

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
//                .repeat()
//                .takeUntil(new Predicate<DotaMatchHistoryResultBean>() {
//                    @Override
//                    public boolean test(DotaMatchHistoryResultBean resultBean) throws Exception {
//                        Log.d(TAG, "remaining " + resultBean.getResult().getResults_remaining());
//                        List<MatchesBean> matchesBeans = resultBean.getResult().getMatches();
//                        if (resultBean.getResult().getResults_remaining() <= 0) {
//                            return true;
//                        } else {
//                            latestQueryingMatchID[0] = matchesBeans.get(matchesBeans.size() - 1).getMatch_id();
//                            return false;
//                        }
//                    }
//                })
//                .concatMap(new Function<DotaMatchHistoryResultBean, ObservableSource<MatchesBean>>() {
//                    @Override
//                    public ObservableSource<MatchesBean> apply(DotaMatchHistoryResultBean resultBean) throws Exception {
//                        Log.d(TAG, "apply: flat to " + resultBean.getResult().getMatches().size());
//                        return Observable.fromIterable(resultBean.getResult().getMatches());
//                    }
//                })
//                .distinct(new Function<MatchesBean, Object>() {
//                    @Override
//                    public Object apply(MatchesBean matchesBean) throws Exception {
//                        return matchesBean.getMatch_id();
//                    }
//                })
//                .filter(new Predicate<MatchesBean>() {
//                    @Override
//                    public boolean test(MatchesBean matchesBean) throws Exception {
//                        String steamId32 = EasySP.init(AFApplication.getAppContext()).getString(PrefKeys.STEAM_ID_32);
//                        Document document = Jsoup.connect(Config.URL_DOTA_MATCH_DETAIL_PREFIX + matchesBean.getMatch_id()).get();
//                        Elements elements = document.select(String.format("tr[url=%s] div[data-tip=%s]", steamId32, "match_mvp_tip"));
//                        if (elements != null && elements.size() == 1) {
//                            return true;
//                        }
//                        return false;
//                    }
//                })
//                .flatMap(new Function<MatchesBean, ObservableSource<MatchModel>>() {
//                    @Override
//                    public ObservableSource<MatchModel> apply(MatchesBean matchesBean) throws Exception {
//                        final DotaMatchModel dotaMatchModel = new DotaMatchModel(MatchModel.TYPE_DOTA, matchesBean.getStart_time());
//                        return RetrofitHelper.getService(ISteamApiService.class).getMatchDetail(Config.STEAM_API_KEY, matchesBean.getMatch_id())
//                                .map(new Function<DotaMatchDetailResultBean, MatchModel>() {
//                                    @Override
//                                    public MatchModel apply(DotaMatchDetailResultBean resultBean) throws Exception {
//                                        dotaMatchModel.setDuration(resultBean.getResult().getDuration());
//                                        int steam32 = Integer.parseInt(EasySP.init(AFApplication.getAppContext()).getString(PrefKeys.STEAM_ID_32));
//                                        PlayersBean myPlayerBean = null;
//                                        for (PlayersBean playersBean : resultBean.getResult().getPlayers()
//                                                ) {
//                                            if (playersBean.getAccount_id() == steam32) {
//                                                myPlayerBean = playersBean;
//                                                break;
//                                            }
//                                        }
//                                        if (myPlayerBean == null) {
//                                            throw new ApiException("找不到比赛该玩家的数据");
//                                        }
//
//                                        dotaMatchModel.setKills(myPlayerBean.getKills());
//                                        dotaMatchModel.setDeaths(myPlayerBean.getDeaths());
//                                        dotaMatchModel.setAssits(myPlayerBean.getAssists());
//                                        dotaMatchModel.setEpm(myPlayerBean.getXp_per_min());
//                                        dotaMatchModel.setGold(myPlayerBean.getGold());
//                                        dotaMatchModel.setGpm(myPlayerBean.getGold_per_min());
//                                        dotaMatchModel.setScaledDamage(myPlayerBean.getScaled_hero_damage());
//                                        return dotaMatchModel;
//                                    }
//                                });
//                    }
//                });
    }

    private static Observable<DotaMatchDetailResultBean> getMatchDetail(String id) {
        return RetrofitHelper.getService(ISteamApiService.class).getMatchDetail(Config.STEAM_API_KEY, Long.parseLong(id));
    }

    private static Integer getGloryIDByClassName(String className) {
        if (className.equals("glory_kill")) {
            return 1;
        } else if (className.equals("glory_nai")) {
            return 2;
        } else if (className.equals("glory_assists")) {
            return 3;
        } else if (className.equals("glory_destroy")) {
            return 4;
        } else if (className.equals("glory_gold")) {
            return 5;
        }
        return 0;
    }
}
