package yin.source.com.yinlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择标签 Layout
 * Created by yin on 2017/11/3.
 */

public class CheckableGroupFlowLayout extends FlowLayout implements View.OnClickListener {

    private List<Checkable> checkableList = new ArrayList<>();
    private ChildViewCheckListener childViewCheckListener;
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
                            if (childViewCheckListener != null) {
                                childViewCheckListener.onChildViewCheckedStateChanged(checkable);
                            }
                        }
                    }
                }
            }

            chooseCheckable.setChecked(!chooseCheckable.isChecked());

            if (childViewCheckListener != null) {
                childViewCheckListener.onChildViewCheckedStateChanged(chooseCheckable);
            }
        }
    }


    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public interface ChildViewCheckListener {

        void onChildViewCheckedStateChanged(Checkable checkable);

    }

    public ChildViewCheckListener getChildViewCheckListener() {
        return childViewCheckListener;
    }

    public void setChildViewCheckListener(ChildViewCheckListener childViewCheckListener) {
        this.childViewCheckListener = childViewCheckListener;
    }
}
