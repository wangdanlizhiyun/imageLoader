package com.lzy.iml.gif;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.lzy.iml.request.BitmapRequest;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lizhiyun on 2018/4/26.
 */

public class GifDraw {
    private Movie movie;
//    private Bitmap bitmap;
    private Canvas canvas;
    private Handler handler = new Handler(Looper.getMainLooper());
    private final long delayMills = 100;
    BitmapRequest bitmapRequest;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (bitmapRequest.view == null || bitmapRequest.view.get() == null || bitmapRequest.view.get().getParent() == null || !bitmapRequest.checkIfCanDisplay()) {
                onStop();
                handler.removeCallbacksAndMessages(null);
                return;
            }
            draw();
            handler.postDelayed(runnable, delayMills);
        }
    };

    public GifDraw(Movie movie, BitmapRequest bitmapRequest) {
        this.movie = movie;
        this.bitmapRequest = bitmapRequest;
    }

    private void draw() {
        if (mIsStop.get()) return;
        if (bitmapRequest.view == null || bitmapRequest.view.get() == null || bitmapRequest.view.get().getParent() == null || !bitmapRequest.checkIfCanDisplay()) return;
        canvas.save();
        movie.setTime((int) (System.currentTimeMillis() % movie.duration()));
        movie.draw(canvas, 0, 0);
        bitmapRequest.display();
//        if (bitmapRequest.view.get() instanceof ImageView){
//            ((ImageView) bitmapRequest.view.get()).setImageBitmap(bitmap);
//        }
        canvas.restore();
    }


    public void into(BitmapRequest bitmapRequest) {
        this.bitmapRequest = bitmapRequest;
        if (bitmapRequest.view == null || bitmapRequest.view.get() == null || bitmapRequest.view.get().getParent() == null || !bitmapRequest.checkIfCanDisplay()) return;
        if (movie == null) return;
        if (movie.width() <= 0 || movie.height() <= 0) {
            return;
        }
        bitmapRequest.bitmap = Bitmap.createBitmap(movie.width(), movie.height(), bitmapRequest.inPreferredConfig);
        canvas = new Canvas(bitmapRequest.bitmap);
        handler.postDelayed(runnable, delayMills);
    }

    AtomicBoolean mIsStop = new AtomicBoolean(false);

    void onStop() {
        handler.removeCallbacksAndMessages(null);
        mIsStop.compareAndSet(false, true);
    }

    void onStart() {
        handler.postDelayed(runnable, delayMills);
        mIsStop.compareAndSet(true, false);
    }
}
