package com.lzy.iml.lifecycle;

/**
 * Created by lizhiyun on 2018/2/14.
 */

public interface LifeCycleListener {
    void onStart();
    void onResume();
    void onPause();
    void onStop();
    void onDestory();
}
