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
import io.reactivex.functions.Predicate;


/**
 * Created by chengyuan.wang on 2017/10/31.
 */

public class RecentDataHelper {// TODO: 2017/11/1 可配置次数的重试。因为并不是所有的网络请求都需要频繁地重试，比如说一个重要的表单提交，它应该尽可能多失败重连，相反地，埋点上报等统计功能，它可能最多只需要重试一次就足够了。因此针对不同的场景，我们需要不同的重试次数。退避策略。
    public void getRecentData() {
        final String latestQueryingMatchID = getMostRecentMatchID();
        Observable.defer(new Callable<ObservableSource<MatchHistoryResultData>>() {// TODO: 2017/11/2 rxLifeCircle
            @Override
            public ObservableSource<MatchHistoryResultData> call() throws Exception {
                String steamId = EasySP.init(AFApplication.getAppContext()).getString(PrefKeys.KEY_STEAM_ID);
                if (TextUtils.isEmpty(steamId)) {
                    throw new Exception("找不到 Steam ID，请重新绑定");
                }
                return RetrofitHelper.getService(ISteamApiService.class).getMatchHistory(steamId, latestQueryingMatchID);
            }
        })
                .repeat()
                .takeUntil(new Predicate<MatchHistoryResultData>() {
                    @Override
                    public boolean test(MatchHistoryResultData matchHistoryResultData) throws Exception {
                        List<MatchesBean> matchesBeans = matchHistoryResultData.getResult().getMatches();
                        return matchesBeans.get(0).getMatch_id()>getMostRecentMatchID()
                        return matchHistoryResultData.getResult().getMatches().
                    }
                })
                .filter()

        String steamId = ;
        if (TextUtils.isEmpty(steamId)) {

        }
    }

    private long getMostRecentMatchID() {
        return ;
    }
}
