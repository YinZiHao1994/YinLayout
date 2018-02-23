package com.source.yin.yinlayout.layoutadapter;

/**
 * 可使用 adapter 方式实现数据管理的布局统一实现的接口
 * Created by yin on 2018/1/8.
 */

public interface LayoutByAdapterAble<T extends BaseLayoutAdapter> {

    void setLayoutAdapter(T adapter);

    T getLayoutAdapter();

}
