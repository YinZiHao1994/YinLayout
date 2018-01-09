package com.source.yin.yinlayout.layoutadapter;

/**
 * Created by yin on 2018/1/8.
 */

public interface LayoutByAdapterAble<T extends BaseLayoutAdapter> {

    void setLayoutAdapter(T adapter);

    T getLayoutAdapter();

}
