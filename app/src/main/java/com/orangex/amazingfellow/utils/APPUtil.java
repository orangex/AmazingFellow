package com.orangex.amazingfellow.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.orangex.amazingfellow.base.AFApplication;

import java.util.List;

/**
 * Created by orangex on 2017/11/11.
 */

public class APPUtil {
    public static boolean isAppForeground(){
        ActivityManager manager = (ActivityManager) AFApplication.getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = manager.getRunningAppProcesses();
        if(appProcessInfos == null || appProcessInfos.isEmpty()){
            return false;
        }
        for(ActivityManager.RunningAppProcessInfo info : appProcessInfos){
            //当前应用处于运行中，并且在前台
            if(info.processName.equals(AFApplication.getAppContext().getPackageName()) && info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                return true;
            }
        }
        return false;
    }
}
