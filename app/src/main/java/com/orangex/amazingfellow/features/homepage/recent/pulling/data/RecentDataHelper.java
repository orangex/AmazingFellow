package com.orangex.amazingfellow.features.homepage.recent.pulling.data;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.features.homepage.recent.notification.NotificationsManager;
import com.orangex.amazingfellow.features.homepage.recent.pulling.PullingJobService;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota.RecentDotaMatchHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
/**
 * Created by chengyuan.wang on 2017/10/31.
 */

public class RecentDataHelper {// TODO: 2017/11/1 可配置次数的重试。因为并不是所有的网络请求都需要频繁地重试，比如说一个重要的表单提交，它应该尽可能多失败重连，相反地，埋点上报等统计功能，它可能最多只需要重试一次就足够了。因此针对不同的场景，我们需要不同的重试次数。退避策略。
    public static final String TAG = RecentDataHelper.class.getSimpleName();

    public static final int TYPE_REFRESH = 0;
    public static final int TYPE_LOADMORE = 1;
    
    private static List<List<MatchModel>> sCachedBrokenTimeline = new ArrayList<>();
    private static boolean isRefreshing = false;
    private static boolean isLoadingMore = false;
    
    private static DisposableObserver<MatchModel> sMatchModelObserver ;
    private static List<MatchModel> sBufferedDelta = new ArrayList<>();
    
    public static void getRecentMVPMoments(Observer<List<MatchModel>> observer, final int type) {
        doPullingJob(type);
        
        Observable.timer(4, TimeUnit.SECONDS)
                .flatMap(new Function<Long, ObservableSource<List<MatchModel>>>() {
                    @Override
                    public ObservableSource<List<MatchModel>> apply(Long aLong) throws Exception {
                        Log.i(TAG, "got timeline with size " + sCachedBrokenTimeline.get(0).size());
                        return Observable.just(sCachedBrokenTimeline.get(0));
                    }
                })
                .map(new Function<List<MatchModel>, List<MatchModel>>() {
                    @Override
                    public List<MatchModel> apply(List<MatchModel> matchModels) throws Exception {
                        Collections.sort(matchModels, new Comparator<MatchModel>() {
                            @Override
                            public int compare(MatchModel matchModel, MatchModel compareTo) {
                                return Long.valueOf(compareTo.getId()).compareTo(Long.valueOf(matchModel.getId()));
                            }
                        });
                        return matchModels;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    
    public static void doPullingJob(final int type) {
        sBufferedDelta.clear();
        Log.d(TAG, "getRecentMVPMoments: start with type " + type);
        if (type == TYPE_REFRESH) {
            if (isRefreshing) {
                Log.i(TAG, "doPullingJob: already freshing");
                return;
            }
            if (isLoadingMore) {
                Log.i(TAG, "getRecentMVPMoments: 加载更多时发生刷新操作");
                sMatchModelObserver.dispose();
            }
            isRefreshing = true;
            sCachedBrokenTimeline.add(0, new ArrayList<MatchModel>());
        }
        if (type == TYPE_LOADMORE) {
            if (isLoadingMore) {
                Log.i(TAG, "doPullingJob: already loadingmore");
                return;
            }
            if (isRefreshing) {
                Log.i(TAG, "getRecentMVPMoments: 刷新时发生加载更多操作");
                sMatchModelObserver.dispose();
            }
            isLoadingMore = true;
        }
    
        sMatchModelObserver = new DisposableObserver<MatchModel>() {
            @Override
            public void onNext(MatchModel matchModel) {
                if (!sBufferedDelta.contains(matchModel)) {
                    sBufferedDelta.add(matchModel);
                }
                Log.d(TAG, "onNext: " + matchModel.toString());
            }
        
            @Override
            public void onError(Throwable e) {
                Toast.makeText(AFApplication.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                updateState(type);
                sCachedBrokenTimeline.remove(0);
            }
        
            @Override
            public void onComplete() {
                if (isReachedLastTimeline()) {
                    Log.i(TAG, "reached last timeline");
                    sCachedBrokenTimeline.get(0).addAll(sCachedBrokenTimeline.get(1));
                    sCachedBrokenTimeline.remove(1);
                }
                Log.e(TAG, "onComplete: at " + System.currentTimeMillis());
                updateState(type);
            }
        };
        RecentDotaMatchHelper.getDotaMoments(type)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        updateState(type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sMatchModelObserver);
    }
    
    private static boolean isReachedLastTimeline() {
        return RecentDotaMatchHelper.isReachedLastTimeline();
    }
    
    /**
     * 根据 timeline 和 type 维护自己的状态，并告知各个游戏的 data helper
     */
    private static void updateState(int type) {
        NotificationsManager.updateNotification(sBufferedDelta);
        if (type == TYPE_REFRESH) {
            isRefreshing = false;
        } else if (type == TYPE_LOADMORE) {
            isLoadingMore = false;
        }
        RecentDotaMatchHelper.updateState(type,sCachedBrokenTimeline);
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
    
    public static List<List<MatchModel>> getCachedBrokenTimeline() {
        return sCachedBrokenTimeline;
    }
    
    public static void startPulling() {
        JobScheduler scheduler = (JobScheduler) AFApplication.getAppContext().getSystemService( Context.JOB_SCHEDULER_SERVICE );
    
        JobInfo.Builder builder = new JobInfo.Builder( 1, new ComponentName( AFApplication.getAppContext().getPackageName(),PullingJobService.class.getName()));
        builder.setPeriodic(1000 * 60 * 30);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        builder.setPersisted(false);
        int result = scheduler.schedule(builder.build());
        Log.i(TAG, "job scheduled " + result);
    }
}
