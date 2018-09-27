package com.source.yin.yinlayout.checkable;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import com.source.yin.yinlayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理实现了 {@link CheckableGroup} 接口的 ViewGroup 的相关事件的操作
 */
public class CheckableGroupManager implements View.OnClickListener {

    //是否能多选
    private boolean isMultiple;
    //单选情况下，再次点击选中项能否取消选中
    private boolean childCheckStateCancelable;
    private List<Checkable> checkableList = new ArrayList<>();
    private OnItemCheckListener onItemCheckListener;

    private Context context;
    private CheckableGroup checkableGroup;
    private ItemClickInterceptor itemClickInterceptor;

    public CheckableGroupManager(Context context, CheckableGroup checkableGroup) {
        this.context = context;
        this.checkableGroup = checkableGroup;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setMultiple(boolean multiple) {
        isMultiple = multiple;
    }

    public boolean isChildCheckStateCancelable() {
        return childCheckStateCancelable;
    }

    public void setChildCheckStateCancelable(boolean childCheckStateCancelable) {
        this.childCheckStateCancelable = childCheckStateCancelable;
    }

    public List<Checkable> getCheckableList() {
        return checkableList;
    }

    public void setCheckableList(List<Checkable> checkableList) {
        this.checkableList = checkableList;
    }

    public OnItemCheckListener getOnItemCheckListener() {
        return onItemCheckListener;
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.onItemCheckListener = onItemCheckListener;
    }


    public void init(List<View> childViewList) {
        checkableList.clear();
        if (childViewList != null) {
            for (View view : childViewList) {
                if (view instanceof Checkable) {
                    checkableList.add((Checkable) view);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Checkable) {
            Checkable checkableView = (Checkable) v;
            if (itemClickInterceptor != null && itemClickInterceptor.onInterceptorItemClick(checkableView, checkableList.indexOf(checkableView))) {
                return;
            }
            dealWithCheckEvent(checkableView, !checkableView.isChecked(), true);
        }
    }


    private void dealWithCheckEvent(Checkable targetCheckable, boolean isCheck, boolean triggerChangeEvent) {
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
        if (triggerChangeEvent && onItemCheckListener != null) {
            onItemCheckListener.onCheckedStateChange(checkableGroup);
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

    public Checkable getCheckedItem() {
        if (isMultiple) {
            throw new RuntimeException(context.getResources().getString(R.string.multiple_error));
        }
        List<Checkable> checkedItemList = getCheckedItemList();
        if (checkedItemList == null || checkedItemList.size() <= 0) {
            return null;
        }
        if (checkedItemList.size() > 1) {
            throw new RuntimeException(context.getResources().getString(R.string.too_many_data_error, checkedItemList.size(), checkedItemList));
        }
        return checkedItemList.get(0);
    }


    public Integer getCheckedItemPosition() {
        if (isMultiple) {
            throw new RuntimeException(context.getResources().getString(R.string.multiple_error));
        }
        List<Integer> checkedItemPositionList = getCheckedItemPositionList();
        if (checkedItemPositionList == null || checkedItemPositionList.size() <= 0) {
            return null;
        }
        if (checkedItemPositionList.size() > 1) {
            throw new RuntimeException(context.getResources().getString(R.string.too_many_data_error, checkedItemPositionList.size(), checkedItemPositionList));
        }
        return checkedItemPositionList.get(0);
    }

    public void checkItem(int position, View view, boolean triggerChangeEvent) {
        // todo 在 activity 的 onCreate 和 onResume 中调用此方法时，如果通过如下实现， checkableList 总是为空列表。待解决
        /*if (checkableList != null) {
            if (checkableList.size() > position) {
                Checkable checkable = checkableList.get(position);
                dealWithCheckEvent(checkable, true);
            } else {
                throw new RuntimeException("传入的位置 : " + position + " 超出可选中的总数量 :" + checkableList.size());
            }
        }*/

        if (view instanceof Checkable) {
            dealWithCheckEvent((Checkable) view, true, triggerChangeEvent);
        } else {
            Log.e(getClass().getName(), "position " + position + " 位置的 view 不是 Checkable 的");
        }
    }


    /**
     * 点击事件拦截器，可实现自己的点击事件并决定是否拦截后续的框架中的选中事件触发
     */
    public interface ItemClickInterceptor {
        /**
         * @param checkable
         * @param index     被点击的 item 在 checkable 列表中的坐标
         * @return isInterceptorItemClickEvent 是否拦截后续事件
         */
        boolean onInterceptorItemClick(Checkable checkable, int index);
    }

    public ItemClickInterceptor getItemClickInterceptor() {
        return itemClickInterceptor;
    }

    public void setItemClickInterceptor(ItemClickInterceptor itemClickInterceptor) {
        this.itemClickInterceptor = itemClickInterceptor;
    }
}
