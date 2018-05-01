package com.lzy.Imageloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.lzy.iml.core.Image;
import com.lzy.iml.request.BitmapRequestBuilder;


public class OtherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_other);

        ImageView imageView1 = findViewById(R.id.iv1);
        Image.with(this).load("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=823222274,759908896&fm=27&gp=0.jpg")
                .diskCacheStrategy(BitmapRequestBuilder.DiskCacheStrategy.NONE)
                .face()
                .blur(3)
                .into(imageView1);
        ImageView imageView2 = findViewById(R.id.iv2);
        Image.with(this).loadAsserts("mp.gif").into(imageView2);
        ImageView imageView3 = findViewById(R.id.iv3);
        Image.with(this).errorDrawable(null).load(R.drawable.xiaosong).into(imageView3);
        ImageView imageView4 = findViewById(R.id.iv4);
        Image.with(this).errorDrawable(null).load(R.drawable.g).into(imageView4);

    }

}
