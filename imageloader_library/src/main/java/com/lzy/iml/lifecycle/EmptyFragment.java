package com.lzy.iml.lifecycle;

import android.app.Fragment;

import java.lang.reflect.Field;

/**
 * Created by jack on 2017/12/27.
 */

public class EmptyFragment extends Fragment {
    LifeCycleListener mLifeCycleListener;

    public EmptyFragment() {
    }


    public LifeCycleListener getLifeCycleListener() {
        return mLifeCycleListener;
    }

    public void setLifeCycleListener(LifeCycleListener lifeCycleListener) {
        this.mLifeCycleListener = lifeCycleListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mLifeCycleListener != null){
            mLifeCycleListener.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLifeCycleListener != null){
            mLifeCycleListener.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLifeCycleListener != null){
            mLifeCycleListener.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLifeCycleListener != null){
            mLifeCycleListener.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLifeCycleListener != null){
            mLifeCycleListener.onDestory();
        }
    }

}
