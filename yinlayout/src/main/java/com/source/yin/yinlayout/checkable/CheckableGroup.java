package com.source.yin.yinlayout.checkable;

import android.widget.Checkable;

import java.util.List;

/**
 * 子 view 可被选中的 viewGroup 的统一接口
 * Created by yin on 2018/1/18.
 */

public interface CheckableGroup {

    /**
     * 返回处于被选中状态的对象的列表
     *
     * @return 处于被选中状态的对象的列表
     */
    List<Checkable> getCheckedItemList();

    /**
     * 返回此父布局直接包含的 Checkable 对象的列表
     *
     * @return
     */
    List<Checkable> getCheckableList();


    /**
     * 设置指定项为选中状态
     *
     * @param position 子 view 列表中的第几项，注意，从0开始
     */
    void checkItem(int position);

    /**
     * 设置选项被选中状态改变监听器
     */
    void setOnItemCheckListener(OnItemCheckListener onItemCheckListener);

    /**
     * 返回被选中项在列表中的位置的集合
     */
    List<Integer> getCheckedItemPositionList();


    /**
     * 返回处于被选中状态的对象（只适用于单选情况）
     *
     * @return 处于被选中状态的对象
     */
    Checkable getCheckedItem();

    /**
     * 返回被选中项在列表中的位置（只适用于单选情况）
     */
    Integer getCheckedItemPosition();


}
