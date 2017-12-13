package com.ue.colorful.feature.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ue.colorful.R;
import com.ue.colorful.feature.main.MainActivity;

public class ColorVisionTestActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_vision_test);
        mViewPager = (ViewPager) findViewById(R.id.ViewPager);
        //绑定自定义适配器
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 24;
            }

            @Override
            public Fragment getItem(int position) {
                return ColorVisionTestFragment.newInstance(position);
            }
        });

        TextView tags = (TextView) findViewById(R.id.tags);
        tags.setText((mViewPager.getCurrentItem() + 1) + "/24");

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TextView tags = (TextView) findViewById(R.id.tags);
                tags.setText((mViewPager.getCurrentItem() + 1) + "/24");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void onBackPressed() {
        startActivity(MainActivity.class);
    }

    private void startActivity(Class targetClass) {
        Intent intent = new Intent(ColorVisionTestActivity.this, targetClass);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
        finish();
    }
}