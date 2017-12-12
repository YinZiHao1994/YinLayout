package yin.source.com.yinlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import yin.source.com.yinlayout.flowLayout.BaseLayoutAdapter;

/**
 * 多选或单选项的父布局，类似于{@link android.widget.RadioGroup},
 * 直接子 View 需要实现{@link Checkable}接口或利用框架中的{@link CheckableTag}包裹
 * Created by yin on 2017/12/12.
 */

public class CommonCheckableGroup extends LinearLayout implements View.OnClickListener, BaseLayoutAdapter.DataChangeListener {

    //是否能多选
    private boolean isMultiple;
    private List<Checkable> checkableList = new ArrayList<>();
    private CheckedListener checkedListener;

    private BaseLayoutAdapter adapter;
    private Context context;

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
                this.isMultiple = a.getBoolean(attr, false);
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
                childView.setOnClickListener(this);
                checkableList.add((Checkable) childView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Checkable checkableView = (Checkable) v;
        if (!isMultiple) {
            for (Checkable checkable : checkableList) {
                if (checkable != checkableView) {
                    changeCheckedState(checkable, false);
                }
            }
            changeCheckedState(checkableView, true);
        } else {
            changeCheckedState(checkableView, !checkableView.isChecked());
        }
    }

    private void changeCheckedState(Checkable checkable, boolean isChecked) {
        if (checkable != null) {
            checkable.setChecked(isChecked);
            if (checkable instanceof ViewGroup) {
                changeChildCheckState((ViewGroup) checkable, isChecked);
            }
            if (checkedListener != null) {
                checkedListener.onCheckChange(checkable);
            }
        }
    }


    //修改所有 checkable 的子 view 的 checked 属性
    private void changeChildCheckState(ViewGroup viewGroup, boolean checked) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0;i<childCount;i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof Checkable) {
                ((Checkable) childView).setChecked(checked);
            }
            if (childView instanceof ViewGroup) {
                changeChildCheckState((ViewGroup) childView, checked);
            }
        }
    }

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


    public BaseLayoutAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BaseLayoutAdapter adapter) {
        this.adapter = adapter;
        adapter.addDataChangeListener(this);
        onDataChange();

    }

    public CheckedListener getCheckedListener() {
        return checkedListener;
    }

    public void setCheckedListener(CheckedListener checkedListener) {
        this.checkedListener = checkedListener;
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

    public interface CheckedListener {

        void onCheckChange(Checkable checkable);
//        void itemCheck(Checkable view);

//        void itemUnCheck(Checkable view);
    }
}
