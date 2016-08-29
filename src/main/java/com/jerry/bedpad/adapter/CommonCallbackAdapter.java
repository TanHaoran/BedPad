package com.jerry.bedpad.adapter;

import org.xutils.common.Callback;

/**
 * Created by Jerry on 2016/8/4.
 */
public abstract class CommonCallbackAdapter<String> implements Callback.CommonCallback<String> {
    @Override
    public void onSuccess(String result) {
        success(result);
    }

    protected abstract void success(String result);

    @Override
    public void onError(Throwable throwable, boolean b) {

    }

    @Override
    public void onCancelled(CancelledException e) {

    }

    @Override
    public void onFinished() {

    }
}
