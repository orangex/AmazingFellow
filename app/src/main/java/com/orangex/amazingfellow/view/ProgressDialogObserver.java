package com.orangex.amazingfellow.view;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orangex.amazingfellow.base.ResponseException;
import com.orangex.amazingfellow.base.ResponseObserver;

/**
 * Created by chengyuan.wang on 2017/10/31.
 * require call from main thread cause the onSubscribe() methed run on the thread where observable is create
 * or you can use doOnSubscribe() operator which can be assigned with a specific method instead
 */

public abstract class ProgressDialogObserver<T> extends ResponseObserver<T> {
    private static final String TAG ="datui "+ ProgressDialogObserver.class.getSimpleName();
    protected MaterialDialog dialog;
    protected Context context;

    public ProgressDialogObserver(Context context) {
        this.context = context;
    }
    
    
    @Override
    public void onComplete() {
        dialog.dismiss();
    }

    @Override
    public void onError(ResponseException e) {
        Log.w(TAG, "onError: " + e.getMessage(), e);
        e.printStackTrace();
        dialog.dismiss();
        Toast.makeText(context, e.getDisplayMessage(), Toast.LENGTH_SHORT).show();
    }
}
