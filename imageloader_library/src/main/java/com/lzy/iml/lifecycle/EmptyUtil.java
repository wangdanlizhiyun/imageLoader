package com.lzy.iml.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

import com.lzy.iml.gif.GifUtil;
import com.lzy.iml.util.ImageLoaderExecutor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lizhiyun on 2018/4/26.
 */

public class EmptyUtil {
    static ConcurrentHashMap<Integer,LifeCycleListener> concurrentHashMap = new ConcurrentHashMap<Integer,LifeCycleListener>();
    public static void setLifeCycle(Activity activity){
        EmptyFragment emptyFragment = getFragment(activity);
        if (emptyFragment == null) return;
        emptyFragment.setLifeCycleListener(getLifeCycleListener(activity.hashCode()));
    }
    public static void setLifeCycle(Fragment fragment){
        EmptyFragment emptyFragment = getFragment(fragment);
        if (emptyFragment == null) return;
        emptyFragment.setLifeCycleListener(getLifeCycleListener(fragment.hashCode()));
    }
    public static void setLifeCycle(android.support.v4.app.Fragment fragment){
        EmptySupportFragment emptyFragment = getFragment(fragment);
        if (emptyFragment == null) return;
        emptyFragment.setLifeCycleListener(getLifeCycleListener(fragment.hashCode()));
    }

    private static LifeCycleListener getLifeCycleListener(int code) {
        LifeCycleListener lifeCycleListener = concurrentHashMap.get(code);
        if (lifeCycleListener == null){
            lifeCycleListener = new LifeCycleListenerImpl(code) {
                @Override
                public void onDestory() {
                    super.onDestory();
                    ImageLoaderExecutor.getInstance().cancelTask(code);
                    concurrentHashMap.remove(code);
                    GifUtil.getInstance().removeGif(code);
                }

                @Override
                public void onStop() {
                    super.onStop();
                    GifUtil.getInstance().stopGif(code);
                }

                @Override
                public void onStart() {
                    super.onStart();
                    GifUtil.getInstance().startGif(code);
                }

            };
            concurrentHashMap.put(code,lifeCycleListener);
        }
        return lifeCycleListener;
    }


    public static EmptyFragment getFragment(Activity activity) {
        EmptyFragment emptyFragment = findFragment(activity);
        try{
            if (emptyFragment == null) {
                emptyFragment = new EmptyFragment();
                FragmentManager fragmentManager = activity.getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .add(emptyFragment, TAG)
                        .commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return emptyFragment;
    }


    public static EmptyFragment getFragment(Fragment fragment) {
        EmptyFragment emptyFragment = findFragment(fragment);
        try{
            if (emptyFragment == null) {
                emptyFragment = new EmptyFragment();
                FragmentManager fragmentManager = fragment.getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .add(emptyFragment, TAG)
                        .commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return emptyFragment;
    }
    public static EmptySupportFragment getFragment(android.support.v4.app.Fragment fragment) {
        EmptySupportFragment emptyFragment = findFragment(fragment);
        try{
            if (emptyFragment == null) {
                emptyFragment = new EmptySupportFragment();
                android.support.v4.app.FragmentManager fragmentManager = fragment.getChildFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .add(emptyFragment, TAG)
                        .commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return emptyFragment;
    }
    private static final String TAG = "EmptyFragment_Tag_ImageLoaderLifeCycle";
    private static EmptyFragment findFragment(Activity activity) {
        return (EmptyFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }
    private static EmptyFragment findFragment(Fragment fragment) {
        return (EmptyFragment) fragment.getFragmentManager().findFragmentByTag(TAG);
    }
    private static EmptySupportFragment findFragment(android.support.v4.app.Fragment fragment) {
        return (EmptySupportFragment) fragment.getFragmentManager().findFragmentByTag(TAG);
    }
}
