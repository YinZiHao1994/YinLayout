package yin.source.com.yinlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于选择标签的流式布局，要求直接的子 View 实现{@link Checkable}接口或者利用框架中的实现了接口的{@link CheckableTag}包裹
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
                            checkable.setChecked(false);
                            if (onChildViewCheckListener != null) {
                                onChildViewCheckListener.onChildViewCheckedStateChanged(checkable);
                            }
                        }
                    }
                }
            }

            chooseCheckable.toggle();

            if (onChildViewCheckListener != null) {
                onChildViewCheckListener.onChildViewCheckedStateChanged(chooseCheckable);
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
