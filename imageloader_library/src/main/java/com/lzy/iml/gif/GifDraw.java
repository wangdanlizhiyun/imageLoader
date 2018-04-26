package com.lzy.iml.gif;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.lzy.iml.request.BitmapRequest;

/**
 * Created by lizhiyun on 2018/4/26.
 */

public class GifDraw {
    private ImageView imageView;
    private Movie movie;
    private Bitmap bitmap;
    private Canvas canvas;
    private Handler handler = new Handler(Looper.getMainLooper());
    private final long delayMills = 16;
    BitmapRequest bitmapRequest;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (imageView.getParent() == null){
                return;
            }

            if (!bitmapRequest.checkEffective()) return;
            draw();
            handler.postDelayed(runnable, delayMills);
        }
    };

    public GifDraw(Movie movie, BitmapRequest bitmapRequest) {
        this.movie = movie;
        this.bitmapRequest = bitmapRequest;
    }

    private void draw() {
        canvas.save();
        movie.setTime((int) (System.currentTimeMillis() % movie.duration()));
        movie.draw(canvas, 0, 0);
        imageView.setImageBitmap(bitmap);
        canvas.restore();
    }


    public void into(ImageView imageView) {
        this.imageView = imageView;
        if (imageView == null) return;
        if (movie == null) return;
        if (movie.width() <= 0 || movie.height() <= 0) {
            return;
        }
        bitmap = Bitmap.createBitmap(movie.width(), movie.height(), bitmapRequest.inPreferredConfig);
        canvas = new Canvas(bitmap);
        handler.post(runnable);
    }

}
