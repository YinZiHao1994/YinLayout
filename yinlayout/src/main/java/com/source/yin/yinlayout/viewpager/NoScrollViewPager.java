package com.source.yin.yinlayout.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止滑动切换的 ViewPager
 * Created by yin on 2017/12/7.
 */

public class NoScrollViewPager extends WrapContentHeightViewPager {


    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * dispatchTouchEvent一般情况不做处理
     * 如果修改了默认的返回值,子View都无法收到事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否拦截
     * 拦截:会走到自己的onTouchEvent方法里面来
     * 不拦截:事件传递给子孩子
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //return super.onInterceptTouchEvent(ev);//不行,会有细微移动
        return false;
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //return false;// 可行,不消费,传给父控件
        //return true;// 可行,消费,拦截事件
        //虽然onInterceptTouchEvent中拦截了,
        //但是如果viewPage里面子控件不是viewGroup,还是会调用这个方法.
//            return super.onTouchEvent(ev);
        return true;//消费,拦截事件
    }

}
