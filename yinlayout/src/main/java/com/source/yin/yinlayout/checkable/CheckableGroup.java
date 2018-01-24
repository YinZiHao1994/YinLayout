package com.source.yin.yinlayout.checkable;

import android.widget.Checkable;

import java.util.List;

/**
 * Created by yin on 2018/1/18.
 */

public interface CheckableGroup {

    /**
     * 返回处于被选中状态的对象的列表
     *
     * @return
     */
    List<Checkable> getCheckedItemList();

    /**
     * 返回 Checkable 对象的列表
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
}
