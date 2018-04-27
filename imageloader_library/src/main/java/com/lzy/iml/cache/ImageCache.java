package com.lzy.iml.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.LruCache;

import com.lzy.iml.BuildConfig;
import com.lzy.iml.cache.disk.DiskLruCache;
import com.lzy.iml.gif.GifDraw;
import com.lzy.iml.gif.GifUtil;
import com.lzy.iml.request.BitmapRequest;
import com.lzy.iml.util.Util;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.lzy.iml.util.Util.toHexString;


public class ImageCache {

    private static ImageCache instance;
    public Context context;
    private LruCache<String, Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;
    Set<WeakReference<Bitmap>> reusablePool;
    public static final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;

    public static ImageCache getInstance() {
        if (null == instance) {
            synchronized (ImageCache.class) {
                if (null == instance) {
                    instance = new ImageCache();
                }
            }
        }
        return instance;
    }

    //引用队列
    ReferenceQueue referenceQueue;
    Thread clearReferenceQueue;
    boolean shutDown;

    private ReferenceQueue<Bitmap> getReferenceQueue() {
        if (null == referenceQueue) {
            //引用队列 当弱引用需要被回收 会放入队列
            referenceQueue = new ReferenceQueue<Bitmap>();
            clearReferenceQueue = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!shutDown) {
                        try {
                            Reference<Bitmap> reference = referenceQueue.remove();
                            Bitmap bitmap = reference.get();
                            if (null != bitmap && !bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            clearReferenceQueue.start();
        }
        return referenceQueue;
    }

    String dir;

    /**
     * 初始化
     * disk在sd卡的话需要读写权限
     *
     * @param context
     * @param dir
     */
    public void init(Context context, String dir) {
        this.dir = dir;
        this.context = context.getApplicationContext();
        reusablePool = Collections.synchronizedSet(new HashSet<WeakReference<Bitmap>>());
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();
        mMemoryCache = new LruCache<String, Bitmap>(memoryClass / 10 * 1024 * 1024) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                return Util.getByteCount(value);
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                removEntry(oldValue);
            }
        };
        initDiskCache();
    }

    private void initDiskCache() {
        synchronized (mDiskCacheLock) {

            try {
                mDiskLruCache = DiskLruCache.open(new File(dir),
                        BuildConfig.VERSION_CODE, 1, 10 * 1024 * 1024);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mDiskCacheStarting = false;
            mDiskCacheLock.notifyAll();
        }
    }

    private synchronized void removEntry(Bitmap oldValue) {
        if (oldValue.isMutable()) {
            reusablePool.add(new WeakReference<Bitmap>(oldValue, getReferenceQueue()));
        } else {
            oldValue.recycle();
        }
    }


    public DiskLruCache getDiskLruCache() {
        synchronized (mDiskCacheLock){
            return mDiskLruCache;
        }
    }
    public Boolean hasDiskLruCache(){
        synchronized (mDiskCacheLock){
            return mDiskLruCache != null;
        }
    }

    public synchronized void putBitmap2Memory(String key, Bitmap bitmap) {
        if (!TextUtils.isEmpty(key) && bitmap != null && !bitmap.isRecycled()) {
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * 加入磁盘缓存
     *
     * @param key
     * @param bitmap
     */
    public synchronized void putBitMap2Disk(String key, Bitmap bitmap) {
        synchronized (mDiskCacheLock) {

            if (mDiskLruCache == null) return;
            DiskLruCache.Snapshot snapshot = null;
            OutputStream os = null;
            try {
                snapshot = mDiskLruCache.get(key);
                // 如果缓存有对应key的文件 那么不管 （也可以替换）
                if (null == snapshot) {
                    DiskLruCache.Editor edit = mDiskLruCache.edit(key);
                    if (null != edit) {
                        os = edit.newOutputStream(0);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                        edit.commit();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != snapshot) {
                    snapshot.close();
                }
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public synchronized Bitmap getBitmapFromMemory(BitmapRequest bitmapRequest) {
        return mMemoryCache.get(bitmapRequest.getMemoryKey());
    }

    public synchronized Bitmap getBitmapFromDisk(BitmapRequest bitmapRequest) {
        synchronized (mDiskCacheLock) {
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                }
            }
            if (mDiskLruCache == null) return null;
            DiskLruCache.Snapshot snapshot = null;
            try {
                snapshot = mDiskLruCache.get(bitmapRequest.getDiskKey());
                if (null == snapshot) {
                    return null;
                }
                InputStream is = snapshot.getInputStream(0);
                if (is != null) {
                    FileDescriptor fd = ((FileInputStream) is).getFD();
                    bitmapRequest.loadBitmapFromDescriptor(fd);
                    if (null != bitmapRequest.bitmap) {
                        mMemoryCache.put(bitmapRequest.getMemoryKey(), bitmapRequest.bitmap);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != snapshot) {
                    snapshot.close();
                }
            }
            return bitmapRequest.bitmap;
        }
    }

    public synchronized void getMovieFromDisk(BitmapRequest bitmapRequest) {
        synchronized (mDiskCacheLock) {
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                }
            }
            if (mDiskLruCache == null) return;
            DiskLruCache.Snapshot snapshot = null;
            try {
                snapshot = mDiskLruCache.get(bitmapRequest.getDiskKey());
                if (null == snapshot) {
                    return;
                }
                InputStream is = snapshot.getInputStream(0);
                if (is != null) {
                    Movie movie = Movie.decodeStream(is);
                    GifUtil.getInstance().getGifDraw(movie,bitmapRequest);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != snapshot) {
                    snapshot.close();
                }
            }
        }
    }


    public void clearCache() {
        if (mMemoryCache != null) {
            mMemoryCache.evictAll();
        }

        synchronized (mDiskCacheLock) {
            mDiskCacheStarting = true;
            if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
                try {
                    mDiskLruCache.delete();
                } catch (IOException e) {
                }
                mDiskLruCache = null;
                initDiskCache();
            }
        }
    }

    /**
     * 可被复用的Bitmap必须设置inMutable为true；
     * Android4.4(API 19)之前只有格式为jpg、png，同等宽高（要求苛刻），
     * inSampleSize为1的Bitmap才可以复用；
     * Android4.4(API 19)之前被复用的Bitmap的inPreferredConfig
     * 会覆盖待分配内存的Bitmap设置的inPreferredConfig；
     * Android4.4(API 19)之后被复用的Bitmap的内存
     * 必须大于等于需要申请内存的Bitmap的内存；
     */
    public synchronized Bitmap getReusable(BitmapFactory.Options options) {
        if (options.outWidth == -1 || options.outHeight == -1) {
            return null;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return null;
        }
        synchronized (reusablePool) {
            Bitmap reusable = null;
            Iterator<WeakReference<Bitmap>> iterator = reusablePool.iterator();
            //迭代查找符合复用条件的Bitmap
            while (iterator.hasNext()) {
                Bitmap bitmap = iterator.next().get();
                if (null != bitmap) {
                    //可以被复用
                    if (checkInBitmap(bitmap, options.outWidth, options.outHeight, options.inSampleSize)) {
                        reusable = bitmap;
                        //移出复用池
                        iterator.remove();
                        break;
                    }
                } else {
                    iterator.remove();
                }
            }
            return reusable;
        }
    }

    boolean checkInBitmap(Bitmap bitmap, int w, int h, int inSampleSize) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return bitmap.getWidth() == w && bitmap.getHeight() == h
                    && inSampleSize == 1;
        }
        //如果缩放系数大于1 获得缩放后的宽与高
        if (inSampleSize > 1) {
            w /= inSampleSize;
            h /= inSampleSize;
        }
        int byteCout = w * h * Util.getPixelsCout(bitmap.getConfig());
        return byteCout <= bitmap.getAllocationByteCount();
    }
}
