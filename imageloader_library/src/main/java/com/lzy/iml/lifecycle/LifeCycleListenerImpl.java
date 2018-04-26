package com.lzy.iml.lifecycle;

import android.content.Context;

/**
 * Created by lizhiyun on 2018/4/26.
 */

public abstract class LifeCycleListenerImpl implements LifeCycleListener {
    int code;

    public LifeCycleListenerImpl(int code) {
        this.code = code;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestory() {

    }
}
