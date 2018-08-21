package com.source.yin.yinlayout.checkable;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.source.yin.yinlayout.R;
import com.source.yin.yinlayout.layoutadapter.BaseLayoutAdapter;
import com.source.yin.yinlayout.layoutadapter.CommonLayoutAdapter;
import com.source.yin.yinlayout.layoutadapter.LayoutByAdapterAble;

import java.util.ArrayList;
import java.util.List;

/**
 * 多选或单选项的父布局，类似于{@link android.widget.RadioGroup},
 * 直接子 View 需要实现{@link Checkable}接口或利用框架中的{@link CheckableTag}包裹才能被监听选中状态
 * Created by yin on 2017/12/12.
 */

public class CommonCheckableGroup extends LinearLayout implements BaseLayoutAdapter.DataChangeListener, LayoutByAdapterAble, CheckableGroup {

    private CommonLayoutAdapter adapter;
    private Context context;

    private CheckableGroupManager checkableGroupManager;

    public CommonCheckableGroup(Context context) {
        this(context, null);
    }

    public CommonCheckableGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonCheckableGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        checkableGroupManager = new CheckableGroupManager(context, this);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CommonCheckableGroup,
                defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CommonCheckableGroup_multiple) {
                boolean isMultiple = a.getBoolean(attr, false);
                setMultiple(isMultiple);
            } else if (attr == R.styleable.CommonCheckableGroup_child_checkable) {
                boolean childCheckable = a.getBoolean(attr, true);
                setCanChildCheckStateChange(childCheckable);
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
        View childAt = getChildAt(position);
        checkableGroupManager.checkItem(position, childAt, triggerChangeEvent);
    }

    @Override
    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        checkableGroupManager.setOnItemCheckListener(onItemCheckListener);
    }

    @Override
    public void onDataChange() {
        removeAllViews();
        int itemCount = adapter.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            int layoutRes = adapter.getLayoutRes(i);
            if (layoutRes != 0) {
                View view = LayoutInflater.from(context).inflate(layoutRes, this, false);
                addView(view);
                adapter.onDataBind(view, i);
            }
        }
        //子 view 被 remove 重新添加之后需要手动重新调用 CheckableGroupManager 的相关初始化方法
        initGroupManager();
    }

    @Override
    public void setLayoutAdapter(CommonLayoutAdapter adapter) {
        this.adapter = adapter;
        adapter.addDataChangeListener(this);
        onDataChange();
    }

    @Override
    public CommonLayoutAdapter getLayoutAdapter() {
        return adapter;
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
    public void setCanChildCheckStateChange(boolean canChildCheckStateChange) {
        checkableGroupManager.setChildCheckable(canChildCheckStateChange);
    }

    @Override
    public void setChildCheckStateCancelable(boolean childCheckStateCancelable) {
        checkableGroupManager.setChildCheckStateCancelable(childCheckStateCancelable);
    }
}
