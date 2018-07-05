package com.source.yin.yinlayout.layoutadapter;

import android.view.View;

public interface DataType<T> {

    /**
     * @return 不同数据需要显示的样式布局可能不同，在此返回它们对应的itemView的layoutId
     */
    int getLayoutId();

    /**
     * @param data
     * @param position
     * @return 最重要的，用于分辨某一数据是否匹配当前{@link DataType}实现类。
     * 若此data是当前实现类的目标数据类型，返回true
     */
    boolean isMatching(T data, int position);

    /**
     * 实现数据与viewHolder的绑定
     *
     * @param itemView
     * @param data
     */
    void dataBind(View itemView, T data, int position);


}
