package com.source.yin.yinlayout.checkable;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import com.source.yin.yinlayout.R;
import com.source.yin.yinlayout.layoutadapter.BaseLayoutAdapter;
import com.source.yin.yinlayout.layoutadapter.LayoutByAdapterAble;

import java.util.ArrayList;
import java.util.List;

/**
 * 多选或单选项的父布局，类似于{@link android.widget.RadioGroup},
 * 直接子 View 需要实现{@link Checkable}接口或利用框架中的{@link CheckableTag}包裹才能被监听选中状态
 * Created by yin on 2017/12/12.
 */

public class CommonCheckableGroup extends LinearLayout implements View.OnClickListener, BaseLayoutAdapter.DataChangeListener, LayoutByAdapterAble, CheckableGroup {

    //是否能多选
    private boolean isMultiple;
    //单选情况下，再次点击选中项能否取消选中
    private boolean childCheckStateCancelable;
    private List<Checkable> checkableList = new ArrayList<>();
    private OnItemCheckListener onItemCheckListener;

    private BaseLayoutAdapter adapter;
    private Context context;
    //子项是否能更改被选中状态（用于某些情况下只用来展示不能编辑的被选中状态）
    private boolean childCheckable = true;

    public CommonCheckableGroup(Context context) {
        this(context, null);
    }

    public CommonCheckableGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonCheckableGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
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
                setChildCheckable(childCheckable);
            } else if (attr == R.styleable.CommonCheckableGroup_child_check_state_cancelable) {
                boolean childCheckStateCancelable = a.getBoolean(attr, false);
                setChildCheckStateCancelable(childCheckStateCancelable);
            }
        }
        a.recycle();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        checkableList.clear();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof Checkable) {
                if (childCheckable) {
                    childView.setOnClickListener(this);
                }
                checkableList.add((Checkable) childView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Checkable) {
            Checkable checkableView = (Checkable) v;
            dealWithCheckEvent(checkableView, !checkableView.isChecked());
        }
    }

    private void dealWithCheckEvent(Checkable targetCheckable, boolean isCheck) {
        if (!isMultiple) {
            for (Checkable checkable : checkableList) {
                if (checkable != targetCheckable) {
                    changeCheckedState(checkable, false);
                }
            }
            if (childCheckStateCancelable) {
                changeCheckedState(targetCheckable, !targetCheckable.isChecked());
            } else {
                changeCheckedState(targetCheckable, true);
            }
        } else {
            changeCheckedState(targetCheckable, isCheck);
        }
        if (onItemCheckListener != null) {
            onItemCheckListener.onCheckedStateChange(this);
        }
    }

    private void changeCheckedState(Checkable checkable, boolean isChecked) {
        if (checkable != null) {
            checkable.setChecked(isChecked);
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

    @Override
    public List<Checkable> getCheckedItemList() {
        List<Checkable> checkedItemList = null;
        if (checkableList != null && checkableList.size() > 0) {
            checkedItemList = new ArrayList<>();
            for (Checkable checkable : checkableList) {
                if (checkable.isChecked()) {
                    checkedItemList.add(checkable);
                }
            }
        }
        return checkedItemList;
    }


    @Override
    public List<Integer> getCheckedItemPositionList() {
        List<Integer> checkableItemWrapperList = null;
        if (checkableList != null && checkableList.size() > 0) {
            checkableItemWrapperList = new ArrayList<>();
            for (int i = 0; i < checkableList.size(); i++) {
                Checkable checkable = checkableList.get(i);
                if (checkable.isChecked()) {
                    checkableItemWrapperList.add(i);
                }
            }
        }
        return checkableItemWrapperList;
    }

    @Override
    public Checkable getCheckedItem() {
        if (isMultiple) {
            throw new RuntimeException(getResources().getString(R.string.multiple_error));
        }
        List<Checkable> checkedItemList = getCheckedItemList();
        if (checkedItemList == null || checkedItemList.size() <= 0) {
            return null;
        }
        if (checkedItemList.size() > 1) {
            throw new RuntimeException(getResources().getString(R.string.too_many_data_error, checkedItemList.size(), checkedItemList));
        }
        return checkedItemList.get(0);
    }

    @Override
    public Integer getCheckedItemPosition() {
        if (isMultiple) {
            throw new RuntimeException(getResources().getString(R.string.multiple_error));
        }
        List<Integer> checkedItemPositionList = getCheckedItemPositionList();
        if (checkedItemPositionList == null || checkedItemPositionList.size() <= 0) {
            return null;
        }
        if (checkedItemPositionList.size() > 1) {
            throw new RuntimeException(getResources().getString(R.string.too_many_data_error, checkedItemPositionList.size(), checkedItemPositionList));
        }
        return checkedItemPositionList.get(0);
    }

    @Override
    public void setChildCheckable(boolean childCheckable) {
        this.childCheckable = childCheckable;
    }

    @Override
    public void setChildCheckStateCancelable(boolean childCheckStateCancelable) {
        this.childCheckStateCancelable = childCheckStateCancelable;
    }


    @Override
    public List<Checkable> getCheckableList() {
        return checkableList;
    }

    @Override
    public void checkItem(int position) {
        // todo 在 activity 的 onCreate 和 onResume 中调用此方法时，如果通过如下实现， checkableList 总是为空列表。待解决
        /*if (checkableList != null) {
            if (checkableList.size() > position) {
                Checkable checkable = checkableList.get(position);
                dealWithCheckEvent(checkable, true);
            } else {
                throw new RuntimeException("传入的位置 : " + position + " 超出可选中的总数量 :" + checkableList.size());
            }
        }*/

        int childCount = getChildCount();
        if (position >= childCount) {
            throw new RuntimeException("传入的位置 : " + position + " 超出子 view 的总数量 :" + childCount);
        }
        View childView = getChildAt(position);
        if (childView instanceof Checkable) {
            dealWithCheckEvent((Checkable) childView, true);
        } else {
            Log.e(getClass().getName(), "position " + position + " 位置的 view 不是 Checkable 的");
        }

    }

    @Override
    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }

    @Override
    public void onDataChange() {
        removeAllViews();
        int itemCount = adapter.getItemCount();
        for (int i = 0; i < itemCount; i++) {
//            View itemView = flowLayoutAdapter.getItemView(this, i);
            int layoutRes = adapter.getLayoutRes();
            if (layoutRes != 0) {
                View view = LayoutInflater.from(context).inflate(layoutRes, this, false);
                addView(view);
                adapter.onDataBind(view, i);
            }
        }
    }

    @Override
    public void setLayoutAdapter(BaseLayoutAdapter adapter) {
        this.adapter = adapter;
        adapter.addDataChangeListener(this);
        onDataChange();

    }

    @Override
    public BaseLayoutAdapter getLayoutAdapter() {
        return adapter;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }
}
