package com.lzy.Imageloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import com.lzy.iml.core.Image;


public class OtherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_other);

        ImageView imageView1 = findViewById(R.id.iv1);
        Image.with(this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524719691407&di=6b60fa1d61c82f19fa3c2f511a98bc43&imgtype=0&src=http%3A%2F%2Fwww.21998.cn%2Fuploadfile%2Fnews%2Fimage%2F20161107%2F20161107171401_12213.gif").into(imageView1);
        ImageView imageView2 = findViewById(R.id.iv2);
        Image.with(this).loadAsserts("mp.gif").into(imageView2);
        ImageView imageView3 = findViewById(R.id.iv3);
        Image.with(this).errorDrawable(null).load(R.drawable.xiaosong).into(imageView3);
        ImageView imageView4 = findViewById(R.id.iv4);
        Image.with(this).errorDrawable(null).load(R.drawable.g).into(imageView4);

    }

}
