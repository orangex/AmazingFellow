package com.orangex.amazingfellow.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by orangex on 2017/10/28.
 */

public class AFApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Stetho.initializeWithDefaults(this);
    }
    
    public static Context getAppContext() {
        return sContext;
    }
//    static {
//        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
//            @NonNull
//            @Override
//            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                //开始设置全局的基本参数
//                layout.setReboundDuration(1000);
//                layout.setReboundInterpolator(new DropBounceInterpolator());
//                layout.setFooterHeight(100);
//                layout.setDisableContentWhenLoading(true);
//                //更多的 set 方法
//                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
//            }
//        });
//        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
//            @NonNull
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                //开始设置全局的基本参数
//                layout.setReboundDuration(1000);
//                layout.setReboundInterpolator(new DropBounceInterpolator());
//                layout.setFooterHeight(100);
//                layout.setDisableContentWhenRefresh(true);
//                //更多的 set 方法
//                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
//            }
//        });
//        ClassicsHeader.REFRESH_HEADER_PULLDOWN = "这特么是我队友??";
//        ClassicsHeader.REFRESH_HEADER_REFRESHING = "不敢相信";
//        ClassicsHeader.REFRESH_HEADER_LOADING = "不敢相信";
//        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即刷新";
//        ClassicsHeader.REFRESH_HEADER_FINISH = "刷新完成";
//        ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败";
//        ClassicsHeader.REFRESH_HEADER_LASTTIME = "上次更新 M-d HH:mm";
//
//        ClassicsFooter.REFRESH_FOOTER_PULLUP = "上拉加载更多";
//        ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载";
//        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在刷新...";
//        ClassicsFooter.REFRESH_FOOTER_LOADING = "正在加载...";
//        ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成";
//        ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败";
//        ClassicsFooter.REFRESH_FOOTER_ALLLOADED = "全部加载完成";
//    }
}
