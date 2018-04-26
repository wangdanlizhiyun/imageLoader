package com.lzy.iml.util;

import com.lzy.iml.core.LoadTask;
import com.lzy.iml.go.OverloadPolicy;
import com.lzy.iml.go.SchedulePolicy;
import com.lzy.iml.go.SmartExecutor;

import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lizhiyun on 2018/4/26.
 */

public class ImageLoaderExecutor {
    private static class InstanceHoler {
        private static final ImageLoaderExecutor instance = new ImageLoaderExecutor();
    }
    public static ImageLoaderExecutor getInstance() {
        return InstanceHoler.instance;
    }
    private SmartExecutor executor;
    public ImageLoaderExecutor(){
        executor = new SmartExecutor(5, 400);
        executor.setSchedulePolicy(SchedulePolicy.FirstInFistRun);
        executor.setOverloadPolicy(OverloadPolicy.DiscardOldTaskInQueue);
    }

    public void cancelTask(int code){
        executor.cancelCode(code);
    }

    public void execute(LoadTask task) {
        executor.execute(task);
    }
}
