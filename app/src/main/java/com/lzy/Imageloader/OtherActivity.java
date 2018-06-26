package com.lzy.Imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.lzy.iml.cache.ImageCache;
import com.lzy.iml.core.ImageLoader;
import com.lzy.iml.request.BitmapRequestBuilder;
import com.lzy.iml.util.ImageSizeUtil;
import com.lzy.iml.util.Util;

import java.io.InputStream;


public class OtherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_other);

//        ImageView imageView1 = findViewById(R.id.iv1);
//        ImageLoader.with(this).load("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=823222274,759908896&fm=27&gp=0.jpg")
//                .diskCacheStrategy(BitmapRequestBuilder.DiskCacheStrategy.NONE)
//                .face()
//                .blur(3)
//                .into(imageView1);
        final ImageView imageView2 = findViewById(R.id.iv2);
        ImageLoader.with(this).loadAsserts("python.png").into(imageView2);
//        ImageLoader.with(this).load(R.drawable.python).into(imageView2);
//        Glide.with(this).load("file:///android_asset/python.png").into(imageView2);
//        ImageView imageView3 = findViewById(R.id.iv3);
//        ImageLoader.with(this).errorDrawable(null).load(R.drawable.xiaosong).into(imageView3);
//        ImageView imageView4 = findViewById(R.id.iv4);
//        ImageLoader.with(this).errorDrawable(null).load(R.drawable.g).into(imageView4);


//        imageView2.post(new Runnable() {
//            @Override
//            public void run() {
//                InputStream is = null;
//                try {
//                    is = ImageCache.getInstance().context.getAssets().open("python.png");
//                    if (is != null) {
//                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inJustDecodeBounds = true;
//                        BitmapFactory.decodeStream(is, null, options);
//                        options.inSampleSize = ImageSizeUtil.caculateInSampleSize(options, "python.png",
//                                imageView2.getMeasuredWidth(), imageView2.getMeasuredHeight());
//                        options.inJustDecodeBounds = false;
//                        options.inSampleSize = 2;
//                        try {
//                            Bitmap bitmap = BitmapFactory.decodeStream(is,null,options);
//                            imageView2.setImageBitmap(bitmap);
//                        }catch (Exception e){
//                            Log.e("test","e="+e.getMessage());
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    Util.close(is);
//                }
//
//            }
//        });

    }

}
