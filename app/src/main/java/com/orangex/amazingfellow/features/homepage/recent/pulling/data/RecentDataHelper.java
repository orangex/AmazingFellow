package com.orangex.amazingfellow.features.homepage.recent.pulling.data;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.orangex.amazingfellow.base.AFApplication;
import com.orangex.amazingfellow.db.DBHelper;
import com.orangex.amazingfellow.features.homepage.recent.notification.NotificationsManager;
import com.orangex.amazingfellow.features.homepage.recent.pulling.PullingJobService;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota.DotaMatchModel;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota.DotaMatchModelDao;
import com.orangex.amazingfellow.features.homepage.recent.pulling.data.dota.RecentDotaMatchHelper;
import com.orangex.amazingfellow.rx.ResponseException;
import com.orangex.amazingfellow.utils.AccountUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
/**
 * Created by chengyuan.wang on 2017/10/31.
 */

public class RecentDataHelper {
    public static final String TAG ="datui "+ RecentDataHelper.class.getSimpleName();

    public static final int TYPE_REFRESH = 0;
    public static final int TYPE_LOADMORE = 1;
    private static final int RETRY_COUNT = 5;
    
    private static List<MatchModel> sCachedTimeline = new ArrayList<>();
    private static boolean isRefreshing = false;
    private static boolean isLoadingMore = false;
    
    private static DisposableObserver<MatchModel> sMatchModelObserver ;
    private static List<MatchModel> sBufferedIncrement = new ArrayList<>();
    
    public static void getRecentMVPMoments(Observer<List<MatchModel>> fillMissingDataObserver, Observer<List<MatchModel>> observer, final int type) {
        
        Observable.just(sCachedTimeline)
                .filter(new Predicate<List<MatchModel>>() {
                    @Override
                    public boolean test(List<MatchModel> matchModels) throws Exception {
                        return sCachedTimeline.size() != 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fillMissingDataObserver);
        
        doPullingJob(type);
        Observable.timer(3, TimeUnit.SECONDS)
                .flatMap(new Function<Long, ObservableSource<List<MatchModel>>>() {
                    @Override
                    public ObservableSource<List<MatchModel>> apply(Long aLong) throws Exception {
                        Log.i(TAG, "got timeline with size " + sBufferedIncrement.size());
                        return Observable.just(sBufferedIncrement);
                    }
                })
                .map(new Function<List<MatchModel>, List<MatchModel>>() {
                    @Override
                    public List<MatchModel> apply(List<MatchModel> matchModels) throws Exception {
                        Collections.sort(matchModels, new Comparator<MatchModel>() {
                            @Override
                            public int compare(MatchModel matchModel, MatchModel compareTo) {
                                return compareTo.getEndAt().compareTo(matchModel.getEndAt());
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
        if (!AccountUtil.hasBindSteam()) {
            Log.i(TAG, "doPullingJob: has not bind steam return");
            return;
        }
        Log.i(TAG, "getRecentMVPMoments: start with type " + type);
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
        sBufferedIncrement.clear();
        sMatchModelObserver = new DisposableObserver<MatchModel>() {
            @Override
            public void onNext(MatchModel matchModel) {
                if (!sBufferedIncrement.contains(matchModel)) {
                    sBufferedIncrement.add(matchModel);
                }
                Log.d(TAG, "onNext: " + matchModel.toString());
            }
        
            @Override
            public void onError(Throwable e) {//已经重试过依然 error，则视为成功
                Log.w(TAG, "onError: 重试过依然 error");
                Toast.makeText(AFApplication.getAppContext(), ((ResponseException)e).getDisplayMessage(), Toast.LENGTH_SHORT).show();
                updateState(type);
            }
        
            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: at " + System.currentTimeMillis());
                updateState(type);
            }
        };
        RecentDotaMatchHelper.getDotaMoments(type)
                .take(30, TimeUnit.SECONDS)
                .doOnComplete(new Action() {// TODO: 2017/11/12  放到各个 data helper 里去处理
                    @Override
                    public void run() throws Exception {
                        if (!RecentDotaMatchHelper.isReachedLastTimeline()) {
                            throw new ResponseException("has not reached the last timeline", "数据可能会有些缺失orz,后续会自动重试");
                        }
                    }
                })
                .onErrorReturn(new Function<Throwable, MatchModel>() {
                    @Override
                    public MatchModel apply(Throwable throwable) throws Exception {
                        Log.w(TAG, throwable.getMessage() + throwable);
                        sBufferedIncrement.clear();
                        throw ResponseException.generateResponseException((Exception) throwable);
                    }
                })
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {//重试机制
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        return throwableObservable.zipWith(Observable.range(1, RETRY_COUNT), new BiFunction<Throwable, Integer, Integer>() {
                            @Override
                            public Integer apply(Throwable throwable, Integer integer) throws Exception {
                                return integer;
                            }
                        })
                                .flatMap(new Function<Integer, ObservableSource<?>>() {
                                    @Override
                                    public ObservableSource<?> apply(Integer integer) throws Exception {
                                        return Observable.timer(integer * 5, TimeUnit.SECONDS);
                                    }
                                });
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "disapose ");
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
        Log.i(TAG, "updateState: " + type + " " + sBufferedIncrement.size() + " " + sCachedTimeline.size());
        Collections.sort(sBufferedIncrement, new Comparator<MatchModel>() {
            @Override
            public int compare(MatchModel matchModel, MatchModel compareTo) {
                return compareTo.getEndAt().compareTo(matchModel.getEndAt());
            }
        });
        if (type == TYPE_REFRESH) {
            sCachedTimeline.addAll(0, sBufferedIncrement);
            NotificationsManager.updateNotification(sBufferedIncrement);
        } else if (type == TYPE_LOADMORE) {
            sCachedTimeline.addAll(sBufferedIncrement);
        }
        List<MatchModel> templistForSave = new ArrayList<>();
        templistForSave.addAll(sBufferedIncrement);
        sBufferedIncrement.clear();
        saveToDB(templistForSave);
        
        if (type == TYPE_REFRESH) {
            isRefreshing = false;
        } else if (type == TYPE_LOADMORE) {
            isLoadingMore = false;
        }
        RecentDotaMatchHelper.updateState(type,sCachedTimeline);
    }
    
    private static void saveToDB(List<MatchModel> bufferedIncrement) {
       
        List<DotaMatchModel> dotaMatchModels = new ArrayList<>();
        for (MatchModel model : bufferedIncrement
                ) {
            if (model instanceof DotaMatchModel) {
                dotaMatchModels.add((DotaMatchModel) model);
            }
        }
        Log.i(TAG, "saveToDB: buffered size=" + bufferedIncrement.size() + " and save size " + dotaMatchModels.size());
        
        Observable.just(dotaMatchModels)
                .doOnNext(new Consumer<List<DotaMatchModel>>() {
                    @Override
                    public void accept(List<DotaMatchModel> dotaMatchModels) throws Exception {
                        DBHelper.getDaoSession().getDotaMatchModelDao().insertOrReplaceInTx(dotaMatchModels);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
    
//    private static ObservableSource<MatchModel> getCSGOMoments() {
//        return Observable.just(new MatchModel(MatchModel.TYPE_CSGO, System.currentTimeMillis()));
//    }
//
//    private static ObservableSource<MatchModel> getPUBGMoments() {
//        return Observable.just(new MatchModel(MatchModel.TYPE_PUBG, System.currentTimeMillis()));
//    }
//
//    private static ObservableSource<MatchModel> getOWMoments() {
//        return Observable.just(new MatchModel(MatchModel.TYPE_OW, System.currentTimeMillis()));
//    }
    
    
    public static void startPulling() {
        JobScheduler scheduler = (JobScheduler) AFApplication.getAppContext().getSystemService( Context.JOB_SCHEDULER_SERVICE );
        JobInfo.Builder builder = new JobInfo.Builder( 1, new ComponentName( AFApplication.getAppContext().getPackageName(),PullingJobService.class.getName()));
        builder.setPeriodic(1000 * 60 * 30);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        builder.setPersisted(false);
        int result = scheduler.schedule(builder.build());
        Log.i(TAG, "job scheduled " + result);
    }
    
    public static void loadfromDB() {
        isRefreshing = true;
        Observable.create(new ObservableOnSubscribe<List<DotaMatchModel>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DotaMatchModel>> e) throws Exception {
                DotaMatchModelDao dotaMatchModelDao = DBHelper.getDaoSession().getDotaMatchModelDao();
                e.onNext(dotaMatchModelDao.queryBuilder()
                        .orderDesc(DotaMatchModelDao.Properties.EndAt)
                        .list());
                e.onComplete();
            
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<DotaMatchModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
        
                    }
    
                    @Override
                    public void onNext(List<DotaMatchModel> dotaMatchModels) {
                        sCachedTimeline.clear();
                        sCachedTimeline.addAll(dotaMatchModels);
                    }
    
                    @Override
                    public void onError(Throwable e) {
        
                    }
    
                    @Override
                    public void onComplete() {
                        isRefreshing = false;
                    }
                });
       
                
        
    }
}
