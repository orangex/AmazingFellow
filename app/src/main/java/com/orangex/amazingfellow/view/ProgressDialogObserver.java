package com.orangex.amazingfellow.view;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orangex.amazingfellow.R;
import com.orangex.amazingfellow.rx.ResponseException;
import com.orangex.amazingfellow.rx.ResponseObserver;

import io.reactivex.disposables.Disposable;

/**
 * Created by chengyuan.wang on 2017/10/31.
 * require call from main thread cause the onSubscribe() methed run on the thread where observable is create
 * or you can use doOnSubscribe() operator which can be assigned with a specific method instead
 */

public abstract class ProgressDialogObserver<T> extends ResponseObserver {
    private static final String TAG = ProgressDialogObserver.class.getSimpleName();
    MaterialDialog dialog;
    protected Context context;

    public ProgressDialogObserver(Context context) {
        this.context = context;
    }


    @Override
    public void onSubscribe(Disposable d) {
        boolean showMinMax = true;
        dialog= new MaterialDialog.Builder(context)
                .content(R.string.content_progress_dialog)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();

    }


    @Override
    public void onComplete() {
        dialog.dismiss();
    }

    @Override
    public void onError(ResponseException e) {
        Log.w(TAG, "onError: " + e.getMessage(), e);
        dialog.dismiss();
        Toast.makeText(context, e.getDisplayMessage(), Toast.LENGTH_SHORT).show();
    }
}
