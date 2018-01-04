package com.source.yin.yinlayout.flowLayout;

import android.content.Context;

import java.util.List;

/**
 * Created by yin on 2017/11/17.
 */

public abstract class FlowLayoutAdapter<T> extends BaseLayoutAdapter<T> {


    public FlowLayoutAdapter(Context context, List<T> dataList, int layoutRes) {
        super(context, dataList, layoutRes);
    }
}
