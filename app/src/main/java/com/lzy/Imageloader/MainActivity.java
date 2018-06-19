package com.lzy.Imageloader;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.Imageloader.view.FailedDrawable;
import com.lzy.iml.core.ImageLoader;
import com.lzy.iml.util.Util;
import com.zhy.adapter.recyclerview.CommenAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Activity that;
    CommenAdapter<ImageBean> mAdapter;
    private Drawable loadingDrawable;
    Drawable errorDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        that = this;
        recyclerView = findViewById(R.id.recycleView);

        errorDrawable = new FailedDrawable(Color.RED);

        loadingDrawable = new ColorDrawable(Color.WHITE);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter = new CommenAdapter<>(that, TDSystemGallery.sList).addItemViewDelegate(new ItemViewDelegate<ImageBean>(R.layout.item_main) {
            @Override
            public void convert(final ViewHolder holder, ImageBean imageBean, final int position) {

                ImageView imageView = (ImageView) holder.getView(R.id.iv);
                ImageLoader.with(that).load(TDSystemGallery.sList.get(position).getPath()).placeHolder(loadingDrawable)
                        .errorDrawable(errorDrawable)
                        .size(Util.getScreenWidth(that) / 4, Util.getScreenWidth(that) / 4)
                        .into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(that, DetailActivity.class);
                        intent.putExtra("position", position);
                        startActivity(intent);

                    }
                });
            }
        });
        recyclerView.setAdapter(mAdapter);

        LoadLocalImagesUtil.setLoadCompeleteListener(new LoadLocalImagesUtil.LoadCompeleteListener() {
            @Override
            public void compelete() {
                if (isDestoryed) return;
                mAdapter.notifyDataSetChanged();
                recyclerView.requestLayout();
            }
        });
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } else {
            LoadLocalImagesUtil.load(that);
        }


        findViewById(R.id.other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(that, OtherActivity.class));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoadLocalImagesUtil.load(that);
            } else {
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    boolean isDestoryed = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestoryed = true;
    }
}
