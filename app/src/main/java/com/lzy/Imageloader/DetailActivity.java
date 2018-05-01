package com.lzy.Imageloader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

public class DetailActivity extends AppCompatActivity {
    ViewPager mPager;
    int position = 0;
    private ImagePagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
        position = getIntent().getIntExtra("position",0);
        mPager = findViewById(R.id.vp);


        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), TDSystemGallery.sList.size());
        mPager.setAdapter(mAdapter);
        mPager.setOffscreenPageLimit(2);
        mPager.setCurrentItem(position);


    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final int mSize;

        public ImagePagerAdapter(FragmentManager fm, int size) {
            super(fm);
            mSize = size;
        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageDetailFragment.newInstance(TDSystemGallery.sList.get(position).getPath());
        }
    }

}
