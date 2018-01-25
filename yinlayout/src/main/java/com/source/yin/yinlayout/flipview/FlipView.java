package com.source.yin.yinlayout.flipview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ViewFlipper;

import com.source.yin.yinlayout.R;
import com.source.yin.yinlayout.layoutadapter.BaseLayoutAdapter;
import com.source.yin.yinlayout.layoutadapter.LayoutByAdapterAble;

import java.util.List;

/**
 * 轮播控件，类似{@link android.support.v4.view.ViewPager}，但实现更简单、并且可控制动画效果、动画时长。
 * 适用于简单的文字、图片等的轮播
 * Created by yin on 2018/1/11.
 */

public class FlipView extends ViewFlipper implements LayoutByAdapterAble, BaseLayoutAdapter.DataChangeListener {


    private BaseLayoutAdapter baseLayoutAdapter;
    //动画时长
    private int interval = 3000;
    //动画切换方向
    private AnimDirection animDirection = AnimDirection.VERTICAL;
    private Context context;
    private OnItemClick onItemClick;


    public enum AnimDirection {
        VERTICAL, HORIZONTAL
    }

    public FlipView(Context context) {
        this(context, null);
    }

    public FlipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        //设置开始和结束动画
        setAnim(AnimDirection.VERTICAL);
        //设置间隔时间
        setFlipInterval(interval);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    int displayedChild = getDisplayedChild();
                    View child = getChildAt(displayedChild);
                    onItemClick.onItemClick(displayedChild, child);
                }
            }
        });
    }


    @Override
    public void setLayoutAdapter(BaseLayoutAdapter adapter) {
        baseLayoutAdapter = adapter;
        baseLayoutAdapter.addDataChangeListener(this);
        baseLayoutAdapter.notidifyDataChanged();
    }

    @Override
    public BaseLayoutAdapter getLayoutAdapter() {
        return baseLayoutAdapter;
    }

    @Override
    public void onDataChange() {
        List dataList = baseLayoutAdapter.getDataList();
        int layoutRes = baseLayoutAdapter.getLayoutRes();
        removeAllViews();
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                View view = LayoutInflater.from(context).inflate(layoutRes, this, false);
                addView(view);
                baseLayoutAdapter.dataBind(view, i, dataList.get(i));
            }
        }
        //开始播放动画
        startFlipping();
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
        setFlipInterval(interval);
    }

    public AnimDirection getAnimDirection() {
        return animDirection;
    }

    public void setAnimDirection(AnimDirection animDirection) {
        if (animDirection != null && this.animDirection != animDirection) {
            this.animDirection = animDirection;
            setAnim(animDirection);
        }
    }

    private void setAnim(AnimDirection animDirection) {
        if (animDirection == AnimDirection.HORIZONTAL) {
            setInAnimation(context, R.anim.flip_horizontal_in_anim);
            setOutAnimation(context, R.anim.flip_horizontal_out_anim);
        } else if (animDirection == AnimDirection.VERTICAL) {
            setInAnimation(context, R.anim.flip_vertical_in_anim);
            setOutAnimation(context, R.anim.flip_vertiacl_out_anim);
        }
    }

    public interface OnItemClick {
        void onItemClick(int itemPosition, View itemView);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
