package com.lzy.iml.loader;

import android.graphics.Movie;

import com.lzy.iml.cache.ImageCache;
import com.lzy.iml.cache.disk.DiskLruCache;
import com.lzy.iml.core.ImageLoader;
import com.lzy.iml.request.BitmapRequest;
import com.lzy.iml.request.BitmapRequestBuilder;
import com.lzy.iml.util.Util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by lizhiyun on 16/6/2.
 */
public class HttpLoader implements Load {
    int mIntDownloadTime = 0;
    int mIntRetryTime = 2;


    @Override
    public void loadBitmap(BitmapRequest request) {
        request.isFirstDown = true;

        if (request.diskCacheStrategy != BitmapRequestBuilder.DiskCacheStrategy.ALL){

            request.bitmap = ImageCache.getInstance().getBitmapFromDisk(request);
            if (request.bitmap == null) {
                if (ImageCache.getInstance().hasDiskLruCache()) {
                    downloadBitmapToDisk(request, ImageCache.getInstance().getDiskLruCache());
                    request.bitmap = ImageCache.getInstance().getBitmapFromDisk(request);
                } else {
                    downloadImgByUrl(request);
                }
            }
        }else {
            downloadImgByUrl(request);
        }
    }
    @Override
    public Movie loadMovie(BitmapRequest request) {
        return ImageCache.getInstance().getMovieFromDisk(request);
    }

    public void downloadBitmapToDisk(BitmapRequest request, DiskLruCache diskLruCache) {
        long total = 0;
        synchronized (ImageCache.mDiskCacheLock) {
            if (diskLruCache == null) {
                return;
            }
            mIntDownloadTime++;
            if (mIntDownloadTime > mIntRetryTime) return;

            DiskLruCache.Editor editor;
            try {
                editor = diskLruCache.edit(request.getDiskKey());
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    BufferedOutputStream out = null;
                    BufferedInputStream in = null;
                    HttpURLConnection conn = null;
                    try {
                        URL url = new URL(request.path);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(ImageLoader.ConnectTimeout);
                        request.totalSize = conn.getContentLength();
                        in = new BufferedInputStream(conn.getInputStream(), 8 * 1024);
                        out = new BufferedOutputStream(outputStream, 8 * 1024);
                        int b = 0;
                        while ((b = in.read()) != -1) {
                            total++;
                            out.write(b);
                        }
                        if (request.totalSize <= 0 || total == request.totalSize) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        editor.abort();
                    } finally {
                        Util.close(conn, out, in);
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                diskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (total < request.totalSize) {
            downloadBitmapToDisk(request, diskLruCache);
        }

    }


    public void downloadImgByUrl(BitmapRequest request) {
        request.isFirstDown = true;
        FileOutputStream fos = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(request.path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(ImageLoader.ConnectTimeout);
            request.loadBitmapFromIs(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.close(conn, fos, is);
        }
    }


}
