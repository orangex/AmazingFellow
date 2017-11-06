package com.orangex.amazingfellow.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

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
    }
    
    public static Context getAppContext() {
        return sContext;
    }
}
