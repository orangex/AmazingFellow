package com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota;

import android.text.TextUtils;
import android.util.Log;

import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.constant.Config;
import com.orangex.amazingfellow.constant.PrefKeys;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.MatchModel;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.RecentDataHelper;
import com.orangex.amazingfellow.network.RetrofitHelper;
import com.orangex.amazingfellow.network.steam.ISteamApiService;
import com.orangex.amazingfellow.network.steam.dota.DotaMatchDetailResultBean;
import com.orangex.amazingfellow.rx.ResponseException;
import com.orangex.amazingfellow.utils.AccountUtil;
import com.orangex.amazingfellow.utils.DotaUtil;
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
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chengyuan.wang on 2017/11/6.
 * 各个游戏的 data helper 应该维护各自刷新和加载更多相关的状态，上层的总 data helper 不需要知道具体的逻辑和实现
 */

public class RecentDotaMatchHelper {// TODO: 2017/11/7  rxLifeCircle
    private static final String TAG ="datui "+ RecentDotaMatchHelper.class.getSimpleName();
    private static HashMap<String, Integer> sOldestMatchPageMap = new HashMap<>();
    private static boolean sReachedLastTimeline = false;

    private static long sOldestMatchID = Long.MAX_VALUE;
    private static long sLatestMatchID = -1;
    
    public static Observable<MatchModel> getDotaMoments(final int type) {
        sReachedLastTimeline = false;
        Set<Observable<MatchModel>> friendMVPMatchObservables = new HashSet<>();
        for (String friendId : EasySP.init(AFApplication.getAppContext()).getStringSet(PrefKeys.DOTA_FRIEND)
                ) {
            Observable<MatchModel> observableFriend = Observable.just(friendId)
                    .flatMap(new Function<String, ObservableSource<MatchModel>>() {
                        @Override
                        public ObservableSource<MatchModel> apply(final String friendID64) throws Exception {
                            return Observable.just(friendID64)
                                    .map(new Function<String, Document>() {
                                        Integer page = type == RecentDataHelper.TYPE_LOADMORE ? sOldestMatchPageMap.get(friendID64) : 0;
                                        @Override
                                        public Document apply(String id) throws Exception {
                                            if (page == null) {
                                                page = 0;
                                            }
                                            page++;
                                            Document document = Jsoup.connect(String.format("http://dotamore.com/match/matchlist/%s.html?p=%d", AccountUtil.getSteamID32By64(id), page)).get();
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
                                        }
                                    })
                                    .concatMap(new Function<Document, ObservableSource<MatchModel>>() {
                                        @Override
                                        public ObservableSource<MatchModel> apply(Document document) throws Exception {
                                            List<MatchModel> matchModels = new ArrayList<>();
                                            Elements elements = document.select("div[class=match_index_lately_mvp_ico], div[class=match_index_lately_rong_ico]");
                                            String playerName = document.select("div[class=match_common_player] > span[title]").get(0).text().trim();
                                            int page = 0;
                                            Matcher matcherForPageNow = Pattern.compile(".*\\?p=(\\d+)").matcher(document.baseUri());
                                            if (matcherForPageNow.find()) {
                                                page = Integer.parseInt(matcherForPageNow.group(1));
                                            }
                                            
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
                                                if (type == RecentDataHelper.TYPE_LOADMORE && Long.parseLong(id) >= sOldestMatchID) {
                                                    break;
                                                }
                                                DotaMatchModel model = new DotaMatchModel(MatchModel.TYPE_DOTA);
                                                model.setId(id);
    
                                                if (element.parent().select("div[class=match_index_lately_rong_ico]").size() > 0) {
                                                    model.setMvpType(DotaMatchModel.MVP_TYPE_GLORIOUS);
                                                } else {
                                                    model.setMvpType(DotaMatchModel.MVP_TYPE_MVP);
                                                }
                                                
                                                List<Integer> glorys;
                                                String time = null;
                                                try {
    
                                                    if (mvpTable.nextElementSibling().select("span[class=match_index_lately_type match_type_high]").size() > 0) {
                                                        model.setGameLevel(DotaMatchModel.GAME_LEVEL_HIGH);
                                                    } else if (mvpTable.nextElementSibling().select("span[class=match_index_lately_type match_type_veryHigh]").size() > 0) {
                                                        model.setGameLevel(DotaMatchModel.GAME_LEVEL_VERYHIGN);
                                                    } else {
                                                        model.setGameLevel(DotaMatchModel.GAME_LEVEL_NORMAL);
                                                    }
                                                
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
                                                model.setPageInDotaMore(page);
                                                matchModels.add(model);
                                            }
                                            return Observable.fromIterable(matchModels);
                                        }
                                    })
                                    .takeWhile(new Predicate<MatchModel>() {
                                        @Override
                                        public boolean test(MatchModel matchModel) throws Exception {
                                            DotaMatchModel dotaMatchModel = (DotaMatchModel) matchModel;
    
                                            if (type == RecentDataHelper.TYPE_REFRESH ) {
                                                if (sLatestMatchID > 0) {
                                                    if (Long.parseLong(dotaMatchModel.getId()) <= sLatestMatchID) {
                                                        sReachedLastTimeline = true;
                                                        return false;
                                                    }
                                                } else {
                                                    sReachedLastTimeline = true;
                                                }
                                            }
                                            String timeOffset = dotaMatchModel.getTimeOffsetDesc();
                                            if (type == RecentDataHelper.TYPE_LOADMORE || (type == RecentDataHelper.TYPE_REFRESH && sLatestMatchID == -1)) {
                                                
                                                if (TextUtils.isEmpty(timeOffset)) {
                                                    Log.d(TAG, "时间抓取为空 ");
                                                    return true;
                                                } else {
                                                    if (timeOffset.contains("天前") && Integer.parseInt(timeOffset.substring(0, timeOffset.indexOf("天前"))) >= 30) {
                                                        Log.d(TAG, " 超出最远时间的数据 ");
                                                        return false;
                                                    }
                                                }
                                            }
                                            Log.d(TAG, "最远时间内的数据" + timeOffset);
                                            return true;
                                        }
                                    })
                                    .subscribeOn(Schedulers.io());
                        }
                    });
            friendMVPMatchObservables.add(observableFriend);
        }
        return  Observable.mergeDelayError(friendMVPMatchObservables);
        
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
    
    
    public static void updateState(int type, List<MatchModel> cachedTimeline) {
        if (type == RecentDataHelper.TYPE_LOADMORE) {
            sOldestMatchPageMap.clear();
            boolean foundOldestID = false;
            for (int i = cachedTimeline.size() - 1; i >= 0; i--) {
                MatchModel model = cachedTimeline.get(i);
                if (model != null && model instanceof DotaMatchModel) {
                    if (!foundOldestID) {
                        foundOldestID = true;
                        sOldestMatchID = Long.parseLong(model.getId());
                    }
                    String steamId64 = ((DotaMatchModel) model).getSteamID64();
                    if (sOldestMatchPageMap.get(steamId64) != null) {
                        sOldestMatchPageMap.put(steamId64, ((DotaMatchModel) model).getPageInDotaMore());
                    }
                }
        
            }
        } else if (type == RecentDataHelper.TYPE_REFRESH) {
            for (int i = 0; i < cachedTimeline.size(); i++) {
                MatchModel model = cachedTimeline.get(i);
                if (model != null && model instanceof DotaMatchModel) {
                    sLatestMatchID = Long.parseLong(model.getId());
                    break;
                }
            }
        }
        
    }
    
    public static boolean isReachedLastTimeline() {
        return sReachedLastTimeline;
    }
}
