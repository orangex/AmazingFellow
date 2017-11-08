package com.orangex.amazingfellow.features.homepage.recent.dota;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.constant.Config;
import com.orangex.amazingfellow.constant.PrefKeys;
import com.orangex.amazingfellow.features.homepage.recent.MatchModel;
import com.orangex.amazingfellow.network.RetrofitHelper;
import com.orangex.amazingfellow.network.steam.ISteamApiService;
import com.orangex.amazingfellow.network.steam.dota.DotaMatchDetailResultBean;
import com.orangex.amazingfellow.rx.ResponseException;
import com.orangex.amazingfellow.utils.DotaUtil;
import com.orangex.amazingfellow.utils.SteamUtil;
import com.white.easysp.EasySP;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chengyuan.wang on 2017/11/6.
 */

public class RecentDotaMatchHelper {// TODO: 2017/11/7  rxLifeCircle
    private static final String TAG = RecentDotaMatchHelper.class.getSimpleName();
    private static List<MatchModel> sCachedTimeLine = new ArrayList<>();
    private static HashMap<String, Integer> sOldestMatchPageMap = new HashMap<>();
    private static List<MatchModel> sLastTimeLine = new ArrayList<>();
    private static long sOldestMatchID = Long.MAX_VALUE;
    private static long sLastLatestMatchID = -1;
    
    public static void getDotaMoments(boolean isLoadingMore) {
        
        Set<Observable<DotaMatchModel>> friendMVPMatchObservables = new HashSet<>();
        for (String friendId : EasySP.init(AFApplication.getAppContext()).getStringSet(PrefKeys.DOTA_FRIEND)
                ) {
            if (!isLoadingMore) {
                sOldestMatchPageMap.put(friendId, 0);
                sOldestMatchID = Long.MAX_VALUE;
                sLastTimeLine = sCachedTimeLine;
            }
            if (sLastTimeLine.size() > 0) {
                sLastLatestMatchID = Long.parseLong(sLastTimeLine.get(0).getId());
            }
            Observable<DotaMatchModel> observableFriend = Observable.just(friendId)
                    .flatMap(new Function<String, ObservableSource<DotaMatchModel>>() {
                        @Override
                        public ObservableSource<DotaMatchModel> apply(final String friendID64) throws Exception {
                            return Observable.just(friendID64)
                                    .map(new Function<String, Document>() {
                                        int page = sOldestMatchPageMap.get(friendID64);
                                        @Override
                                        public Document apply(String id) throws Exception {
                                            sOldestMatchPageMap.put(friendID64, page);
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
                                        String lastpageFirstMatchID = "xowentwt";
                                        @Override
                                        public boolean test(Document document) throws Exception {
    
                                            try {
                                                String current = document.select("tbody[class=cursor_pointer]").get(0).child(0).attr("url");
                                                if (current.equals(lastpageFirstMatchID)) {
                                                    return true;
                                                } else {
                                                    lastpageFirstMatchID = current;
                                                    return false;
                                                }
                                            } catch (NullPointerException | IndexOutOfBoundsException e) {
                                                Log.w(TAG, "拿不到当前页第一个比赛的 url", e);
                                                return true;
                                            }
//                                            int count = 0;
//                                            Matcher matcherForMatchTableSplit = Pattern.compile("<!-- 头像 -->").matcher(document.text());
//                                            while (matcherForMatchTableSplit.find()) {
//                                                count++;
//                                            }
//                                            return (count < 10);
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
                                            String playerName = document.select("div[class=match_common_player] > span[title]").get(0).text().trim();
                                            if (elements.size() > 0) {
                                                Log.d(TAG, "找到 mvp 的比赛"+elements.size()+" 场");
                                            }
                                            for (Element element : elements
                                                    ) {
                                                Element mvpTable = element.parent().parent();
                                                String id;
                                                try {
                                                    id= mvpTable.parent().attr("url");
                                                } catch (NullPointerException e) {
                                                    Log.w(TAG, "比赛 id 抓取错误", e);
                                                    continue;
                                                }
                                                if (TextUtils.isEmpty(id)) {
                                                    Log.w(TAG, "比赛 id 抓取为空");
                                                    continue;
                                                }
    
                                                if (Long.parseLong(id) >= sOldestMatchID) {
                                                    break;
                                                }
                                                DotaMatchModel model = new DotaMatchModel(MatchModel.TYPE_DOTA);
                                                model.setId(id);
                                                List<Integer> glorys;
                                                String time = null;
                                                try {
                                                    glorys = new ArrayList<>();
                                                    for (Element glory :
                                                            element.parent().nextElementSibling().children()) {
                                                        glorys.add(getGloryIDByClassName(glory.className()));
                                                        glorys.add(0);
                                                    }
                                                    model.setGlorys(glorys);
                                                    
                                                    time = mvpTable.nextElementSibling().nextElementSibling().getElementsByClass("match_table_start_time_color").get(0).text().trim();
                                                    
                                                    String locHeroName = mvpTable.previousElementSibling().select("div[class] > img[src]").get(0).attr("title").trim();
                                                    model.setHero(DotaUtil.getHeroIdByLocname(locHeroName));
    
                                                    String last = mvpTable.nextElementSibling().nextElementSibling().getElementsByTag("span").get(0).text().trim();
                                                    model.setLastTime(Integer.parseInt(last.replace("分钟", "")));
    
                                                    String efficiency = mvpTable.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().select("div[class=match_index_lately_table_damage]").get(0).text().trim();
                                                    model.setEfficiency(efficiency);
    
                                                    String kda = mvpTable.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().text().trim();
                                                    Matcher matcherkda = Pattern.compile("(\\d+)/(\\d+)/(\\d+)").matcher(kda);
                                                    if (matcherkda.find()) {
                                                        model.setKills(Integer.parseInt(matcherkda.group(1)));
                                                        model.setDeaths(Integer.parseInt(matcherkda.group(2)));
                                                        model.setAssits(Integer.parseInt(matcherkda.group(3)));
                                                    }
    
                                                    String gpm = mvpTable.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().select("div[class=match_index_lately_table_damage]").get(0).text().trim();
                                                    model.setGpm(Integer.parseInt(gpm));
    
                                                    String dhb = mvpTable.nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling().nextElementSibling()
                                                            .getElementsByClass("match_index_lately_table_damage").get(0).text().trim();
                                                    model.setDhb(Integer.parseInt(dhb));
                                                    model.setPlayerName(playerName);
                                                    model.setTimeOffsetDesc(time);
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
    
                                            if (Long.parseLong(stringMatchModelPair.second.getId()) < sLastLatestMatchID) {
                                                return false;
                                            }
                                            String time = stringMatchModelPair.first;
                                            if (TextUtils.isEmpty(time)) {
                                                Log.d(TAG, "时间抓取为空 ");
                                                return true;
                                            } else {
                                                if (time.contains("天前") && Integer.parseInt(time.substring(0, time.indexOf("天前"))) >= 30) {
                                                    Log.d(TAG, "七天前的数据 ");
                                                    return false;
                                                }
                                            }
                                            Log.d(TAG, "七天内的数据 " + stringMatchModelPair.first);
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
        Observable.mergeDelayError(friendMVPMatchObservables)
                .doOnNext(new Consumer<DotaMatchModel>() {
                    @Override
                    public void accept(DotaMatchModel dotaMatchModel) throws Exception {
                        if (dotaMatchModel.getId().equals(String.valueOf(sLastLatestMatchID))) {
                            sCachedTimeLine.addAll(sLastTimeLine);
                        } else {
                            sCachedTimeLine.add(dotaMatchModel);
                        }
    
//                        Collections.sort(sCachedTimeLine, new Comparator<MatchModel>() {
//                            @Override
//                            public int compare(MatchModel matchModel, MatchModel compareTo) {
//                                return Long.valueOf(matchModel.getId()).compareTo(Long.valueOf(compareTo.getId()));
//                            }
//                        });
                    }
                })
                .subscribe(new Observer<DotaMatchModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
        
                    }
    
                    @Override
                    public void onNext(DotaMatchModel dotaMatchModel) {
                        Log.i(TAG, "onNext: " + sCachedTimeLine.size() + "  " + dotaMatchModel.toString());
                    }
    
                    @Override
                    public void onError(Throwable e) {
        
                    }
    
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: at " + System.currentTimeMillis());
                    }
                });
                
        
//                .flatMap(new Function<MatchModel, ObservableSource<MatchModel>>() {
//                    @Override
//                    public ObservableSource<MatchModel> apply(final MatchModel matchModel) throws Exception {
//                        return getMatchDetail(matchModel.getId())
//                                .map(new Function<DotaMatchDetailResultBean, MatchModel>() {
//                                    @Override
//                                    public MatchModel apply(DotaMatchDetailResultBean resultBean) throws Exception {
//                                        DotaMatchModel dotaMatchModel = (DotaMatchModel) matchModel;
//                                        dotaMatchModel.setDuration(resultBean.getResult().getDuration());
//                                        int steam32 = Integer.parseInt(SteamUtil.getSteamID32By64(dotaMatchModel.getSteamID64()));
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
//                                        dotaMatchModel.setKills(myPlayerBean.getKills());
//                                        dotaMatchModel.setDeaths(myPlayerBean.getDeaths());
//                                        dotaMatchModel.setAssits(myPlayerBean.getAssists());
//                                        dotaMatchModel.setEpm(myPlayerBean.getXp_per_min());
//                                        dotaMatchModel.setGold(myPlayerBean.getGold());
//                                        dotaMatchModel.setGpm(myPlayerBean.getGold_per_min());
//                                        dotaMatchModel.setScaledDamage(myPlayerBean.getScaled_hero_damage());
//                                        Log.d(TAG, "mvp match model " + dotaMatchModel.toString());
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
            return DotaMatchModel.GLORY_KILL;
        }else if (className.equals("glory_destroy")) {
            return DotaMatchModel.GLORY_DESTROY;
        } else if (className.equals("glory_gold")) {
            return DotaMatchModel.GLORY_GOLD;
        } else if (className.equals("glory_nai")) {
            return DotaMatchModel.GLORY_HEALTH;
        } else if (className.equals("glory_assists")) {
            return DotaMatchModel.GLORY_ASSIST;
        }
        return 0;
    }
    
  
    
    
    public static List<MatchModel> getCachedTimeLine() {
        return sCachedTimeLine;
    }
    
}
