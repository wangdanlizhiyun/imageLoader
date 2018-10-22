package com.lzy.Imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.iml.core.ImageLoader;
import com.lzy.iml.loader.CustomLoader;
import com.lzy.iml.request.BitmapRequest;
import com.lzy.iml.request.BitmapRequestBuilder;
import com.lzy.iml.request.CustomDisplayMethod;


public class OtherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_other);

        ImageView imageView1 = findViewById(R.id.iv1);
        ImageLoader.with(imageView1.getContext()).load("http://img.soogif.com/aKaxb4oh3iwU0nFamlzjfAeHPNMUYFTq.gif_s400x0")
//                .diskCacheStrategy(BitmapRequestBuilder.DiskCacheStrategy.NONE)
//                .face()
//                .blur(3)
                .into(imageView1);
        final ImageView imageView2 = findViewById(R.id.iv2);
        ImageLoader.with(this).loadAsserts("640.gif").into(imageView2);
        ImageView imageView3 = findViewById(R.id.iv3);
        ImageLoader.with(this).errorDrawable(null).load(R.drawable.xiaosong).into(imageView3);
        ImageView imageView4 = findViewById(R.id.iv4);
        ImageLoader.with(this).errorDrawable(null).load(R.drawable.g).into(imageView4);

        //自定义生成二维码图片
        ImageView imageView5 = findViewById(R.id.iv5);

        ImageLoader.with(this).customLoader(new CustomLoader() {
            @Override
            public void loadBitmap(BitmapRequest request) {
                super.loadBitmap(request);
                request.bitmap = QrUtil.getQrCodeBitmap(MyApp.application.getApplicationContext(),request.path,null);
            }
        }).load("http://dev-newwap.yixinfa.cn/#/add_dev?uuid=118061031592").into(imageView5);


        final TextView tv  = findViewById(R.id.tv6);
        tv.setText("textview左边有个leftdrawable");
        tv.setTextSize(10);
        ImageLoader.with(this).customDisplayMethod(new CustomDisplayMethod() {
            @Override
            public void display(BitmapRequest request) {
                Drawable drawable = new BitmapDrawable(request.bitmap);
                drawable.setBounds(0,0,(int) tv.getTextSize()*4,(int) tv.getTextSize()*4);
                tv.setCompoundDrawables(drawable,null,null,null);
            }
        }).size(100,100).load(R.drawable.g)
                .into(tv);

    }

}
