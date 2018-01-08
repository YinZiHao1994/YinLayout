package com.source.yin.yinlayout.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import com.source.yin.yinlayout.checkable.CheckableTag;
import com.source.yin.yinlayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于选择标签的流式布局，要求直接的子 View 实现{@link Checkable}接口或者利用框架中的{@link CheckableTag}包裹
 * Created by yin on 2017/11/3.
 */

public class CheckableGroupFlowLayout extends FlowLayout implements View.OnClickListener {

    private List<Checkable> checkableList = new ArrayList<>();
    private OnChildViewCheckListener onChildViewCheckListener;
    //是否能多选
    private boolean isMultiple;

    public CheckableGroupFlowLayout(Context context) {
        this(context, null);
    }

    public CheckableGroupFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableGroupFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CheckableGroupFlowLayout,
                defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CheckableGroupFlowLayout_multiple) {
                this.isMultiple = a.getBoolean(attr, false);
            }
        }
        a.recycle();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        checkableList.clear();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof Checkable) {
                childView.setOnClickListener(this);
                checkableList.add((Checkable) childView);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v instanceof Checkable) {
            Checkable chooseCheckable = (Checkable) v;
            if (!isMultiple) {
                for (Checkable checkable : checkableList) {
                    if (checkable != chooseCheckable) {
                        if (checkable.isChecked()) {
                            changeCheckedState(checkable, false);
                        }
                    }
                }
            }
            changeCheckedState(chooseCheckable, !chooseCheckable.isChecked());
        }
    }


    private void changeCheckedState(Checkable checkable, boolean isChecked) {
        if (checkable != null) {
            checkable.setChecked(isChecked);
            if (onChildViewCheckListener != null) {
                onChildViewCheckListener.onChildViewCheckedStateChanged(checkable);
            }
            if (checkable instanceof ViewGroup) {
                changeChildCheckState((ViewGroup) checkable, isChecked);
            }
        }
    }

    //修改所有 checkable 的子 view 的 checked 属性
    private void changeChildCheckState(ViewGroup viewGroup, boolean checked) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof Checkable) {
                ((Checkable) childView).setChecked(checked);
            }
            if (childView instanceof ViewGroup) {
                changeChildCheckState((ViewGroup) childView, checked);
            }
        }
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }


    public List<Checkable> getHaveCheckedList() {
        List<Checkable> haveCheckedList = new ArrayList<>();
        for (Checkable checkable : checkableList) {
            if (checkable.isChecked()) {
                haveCheckedList.add(checkable);
            }
        }
        return haveCheckedList;
    }


    public interface OnChildViewCheckListener {
        void onChildViewCheckedStateChanged(Checkable checkable);
    }

    public OnChildViewCheckListener getOnChildViewCheckListener() {
        return onChildViewCheckListener;
    }

    public void setOnChildViewCheckListener(OnChildViewCheckListener onChildViewCheckListener) {
        this.onChildViewCheckListener = onChildViewCheckListener;
    }
}
