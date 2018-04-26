package com.lzy.Imageloader;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by lizhiyun on 2018/4/19.
 */

public class LoadLocalImagesUtil {


    static ExecutorService executors;
    static LoadCompeleteListener loadCompeleteListener;

    public static void setLoadCompeleteListener(LoadCompeleteListener loadCompeleteListener) {
        LoadLocalImagesUtil.loadCompeleteListener = loadCompeleteListener;
    }

    public static void load(final Context context) {
        if (TDSystemGallery.sList == null || TDSystemGallery.sList.size() == 0) {
            if (executors == null) {
                executors = Executors.newSingleThreadExecutor();
            }

            executors.execute(new Runnable() {
                @Override
                public void run() {

                    TDSystemGallery.asyncFindGallery(context);
                    sHandler.sendEmptyMessage(0);
                }
            });

        } else {
            sHandler.sendEmptyMessage(0);
        }


    }

    static Handler sHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (loadCompeleteListener != null) {
                loadCompeleteListener.compelete();
            }
        }
    };


    public static interface LoadCompeleteListener {
        void compelete();
    }
}
