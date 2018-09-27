package com.source.yin.yinlayout.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.source.yin.yinlayout.R;
import com.source.yin.yinlayout.checkable.CheckableGroup;
import com.source.yin.yinlayout.checkable.CheckableGroupManager;
import com.source.yin.yinlayout.checkable.CheckableTag;
import com.source.yin.yinlayout.checkable.OnItemCheckListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于选择标签的流式布局，要求直接的子 View 实现{@link Checkable}接口或者利用框架中的{@link CheckableTag}包裹
 * Created by yin on 2017/11/3.
 */

public class CheckableGroupFlowLayout extends FlowLayout implements CheckableGroup {

    private CheckableGroupManager checkableGroupManager;

    public CheckableGroupFlowLayout(Context context) {
        this(context, null);
    }

    public CheckableGroupFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckableGroupFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        checkableGroupManager = new CheckableGroupManager(context, this);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CheckableGroupFlowLayout,
                defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CheckableGroupFlowLayout_multiple) {
                boolean isMultiple = a.getBoolean(attr, false);
                setMultiple(isMultiple);
            } else if (attr == R.styleable.CommonCheckableGroup_child_check_state_cancelable) {
                boolean childCheckStateCancelable = a.getBoolean(attr, false);
                setChildCheckStateCancelable(childCheckStateCancelable);
            }
        }
        a.recycle();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initGroupManager();
    }

    private void initGroupManager() {
        List<View> childViewList = new ArrayList<>();
        int childCount = getChildCount();
        childViewList.clear();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            childViewList.add(childAt);
        }
        checkableGroupManager.init(childViewList);
    }

    @Override
    public void onDataChange() {
        super.onDataChange();
        initGroupManager();
    }

    public boolean isMultiple() {
        return checkableGroupManager.isMultiple();
    }

    public void setMultiple(boolean multiple) {
        checkableGroupManager.setMultiple(multiple);
    }

    @Override
    public void setItemClickInterceptor(CheckableGroupManager.ItemClickInterceptor itemClickInterceptor) {
        checkableGroupManager.setItemClickInterceptor(itemClickInterceptor);
    }


    @Override
    public List<Checkable> getCheckedItemList() {
        return checkableGroupManager.getCheckedItemList();
    }

    @Override
    public List<Integer> getCheckedItemPositionList() {
        return checkableGroupManager.getCheckedItemPositionList();
    }

    @Override
    public Checkable getCheckedItem() {
        return checkableGroupManager.getCheckedItem();
    }

    @Override
    public Integer getCheckedItemPosition() {
        return checkableGroupManager.getCheckedItemPosition();
    }

    @Override
    public List<Checkable> getCheckableList() {
        return checkableGroupManager.getCheckableList();
    }

    @Override
    public void checkItem(int position) {
        checkItem(position, true);
    }

    @Override
    public void checkItem(int position, boolean triggerChangeEvent) {

        int childCount = getChildCount();
        if (position >= childCount) {
            throw new RuntimeException("传入的位置 : " + position + " 超出子 view 的总数量 :" + childCount);
        }
        View childView = getChildAt(position);
        checkableGroupManager.checkItem(position, childView, triggerChangeEvent);
    }

    @Override
    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        checkableGroupManager.setOnItemCheckListener(onItemCheckListener);
    }

    @Override
    public void setChildCheckStateCancelable(boolean childCheckStateCancelable) {
        checkableGroupManager.setChildCheckStateCancelable(childCheckStateCancelable);
    }


}
