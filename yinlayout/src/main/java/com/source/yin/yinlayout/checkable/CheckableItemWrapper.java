package com.source.yin.yinlayout.checkable;

import android.widget.Checkable;

/**
 * 带列表位置的 Checkable 包装对象，用来在之后根据列表位置值获取数据源对象
 * Created by yin on 2018/3/14.
 */

public class CheckableItemWrapper {

    private int positionInList;
    private Checkable checkable;

    public CheckableItemWrapper(int positionInList, Checkable checkable) {
        this.positionInList = positionInList;
        this.checkable = checkable;
    }

    public int getPositionInList() {
        return positionInList;
    }

    public Checkable getCheckable() {
        return checkable;
    }
}
