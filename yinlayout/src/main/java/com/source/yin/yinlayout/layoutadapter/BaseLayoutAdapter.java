package com.source.yin.yinlayout.layoutadapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 为实现了{@link LayoutByAdapterAble}接口的 viewGroup 提供 adapter 方式的 layout
 * 当只需要一种样式的子项时方便使用，如果子项需要不同的 UI 显示方式，请使用{@link CommonLayoutAdapter}
 * Created by yin on 2017/12/12.
 */

public abstract class BaseLayoutAdapter<T> extends CommonLayoutAdapter<T> {

    @LayoutRes
    private int layoutRes;

    public BaseLayoutAdapter(Context context, List<T> dataList, int layoutRes) {
        super(context, dataList);
        this.layoutRes = layoutRes;
    }

    @Override
    public List<DataType<T>> getDataTypes() {
        List<DataType<T>> dataTypeList = new ArrayList<>();
        DataType<T> dataType = new DataType<T>() {
            @Override
            public int getLayoutId() {
                return layoutRes;
            }

            @Override
            public boolean isMatching(T data, int position) {
                return true;
            }

            @Override
            public void dataBind(View itemView, T data, int position) {
                onDataBind(itemView, data, position);
            }
        };
        dataTypeList.add(dataType);
        return dataTypeList;
    }

    //子类只需要实现此方法进行数据的绑定显示
    public abstract void onDataBind(View itemView, T data, int position);

    public int getLayoutRes() {
        return layoutRes;
    }
}
