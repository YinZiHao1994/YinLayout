package com.source.yin.yinlayoutsample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        viewPager = findViewById(R.id.view_pager);
        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {

            View e = new View(getApplicationContext());
            Button button = new Button(getApplicationContext());
            button.setHeight(50);
            button.setText("button");
//            e.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40));
            ViewPager.LayoutParams params = new ViewPager.LayoutParams();
            params.height = 70;
            e.setLayoutParams(params);
            e.setBackgroundResource(R.color.colorPrimary);
            viewList.add(button);
        }
        viewPager.setPageMargin(60);
        viewPager.setAdapter(new CommonViewPagerAdapter<View>(viewList));

    }


    public static class CommonViewPagerAdapter<T extends View> extends PagerAdapter {

        private List<T> viewList;

        public CommonViewPagerAdapter(List<T> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View child = viewList.get(position);
            container.addView(child);
            return child;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {//必须实现，销毁
            container.removeView(viewList.get(position));
        }

    }

}
