package com.source.yin.yinlayout.flowlayout;

import android.content.Context;

import com.source.yin.yinlayout.BaseLayoutAdapter;

import java.util.List;

/**
 * Created by yin on 2017/11/17.
 */

public abstract class FlowLayoutAdapter<T> extends BaseLayoutAdapter<T> {

    public FlowLayoutAdapter(Context context, List<T> dataList, int layoutRes) {
        super(context, dataList, layoutRes);
    }
}
