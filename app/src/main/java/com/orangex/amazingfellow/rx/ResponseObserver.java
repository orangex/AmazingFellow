package com.orangex.amazingfellow.rx;

import io.reactivex.Observer;

/**
 * Created by chengyuan.wang on 2017/10/31.
 */

public abstract class ResponseObserver<T> implements Observer<T> {


    @Override
    public void onError(Throwable e) {
        if (e instanceof ResponseException) {
            onError((ResponseException) e);
        } else {
            onError(new ResponseException("not responseexception", e));
        }
    }

    public abstract void onError(ResponseException exception);

}
