package com.source.yin.yinlayout.layoutadapter;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 为实现了{@link LayoutByAdapterAble}接口的 viewGroup 提供 adapter 方式的 layout
 *
 * 可以根据不同的{@link DataType}给子项设置不同的 UI ,如果只需要一种样式的子项，请使用{@link BaseLayoutAdapter}
 * Created by yin on 2017/12/12.
 */

public abstract class CommonLayoutAdapter<T> {

    protected List<T> dataList;
    protected Context context;
    private DataTypeManager<T> dataTypeManager;
    private List<DataChangeListener> dataChangeListenerList;


    public CommonLayoutAdapter(Context context, List<T> dataList) {
        if (dataList == null) {
            throw new RuntimeException("dataList can not be null");
        }
        this.context = context;
        this.dataList = dataList;

        dataTypeManager = new DataTypeManager<>();
        addDataTypes(getDataTypes());
    }

    /**
     * @return 子类需要返回实现了{@link DataType}接口的List
     */
    public abstract List<DataType<T>> getDataTypes();


    public void addDataTypes(List<DataType<T>> viewTypes) {
        if (viewTypes != null) {
            for (DataType<T> viewType : viewTypes) {
                dataTypeManager.addViewType(viewType);
            }
        }
    }

    public int getLayoutRes(int position) {
        T data = dataList.get(position);
        return dataTypeManager.getLayoutId(data, position);
    }

    public void addDataType(DataType<T> viewType) {
        if (viewType != null) {
            dataTypeManager.addViewType(viewType);
        }
    }


    public void notifyDataChanged() {
        if (dataChangeListenerList != null) {
            for (DataChangeListener dataChangeListener : dataChangeListenerList) {
                dataChangeListener.onDataChange();
            }
        }
    }

    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public T getData(int position) {
        return dataList.get(position);
    }


    public void onDataBind(View view, int position) {
        T data = dataList.get(position);
        dataTypeManager.dataBind(view, data, position);
    }

    public interface DataChangeListener {
        void onDataChange();
    }

    public void addDataChangeListener(DataChangeListener dataChangeListener) {
        if (dataChangeListenerList == null) {
            dataChangeListenerList = new ArrayList<>();
        }
        dataChangeListenerList.add(dataChangeListener);
    }
}
