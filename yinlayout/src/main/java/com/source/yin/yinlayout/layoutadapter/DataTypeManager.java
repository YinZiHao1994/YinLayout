package com.source.yin.yinlayout.layoutadapter;

import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DataTypeManager<T> {

    private SparseArray<DataType<T>> dataTypeList = new SparseArray<>();
    private int viewTypeKey;


    int getLayoutId(T data, int position) {
        DataType<T> dataType = getMatchedDataType(data, position);
        if (dataType == null) {
            throw new RuntimeException("not find matched dataType");
        }
        return dataType.getLayoutId();
    }


    int getLayoutId(int viewType) {
        DataType<T> dataType = dataTypeList.get(viewType);
        if (dataType == null) {
            throw new RuntimeException("not find matched dataType");
        }
        return dataType.getLayoutId();
    }

    void dataBind(View itemView, T data, int position) {
        DataType<T> matchedDataType = getMatchedDataType(data, position);
        matchedDataType.dataBind(itemView, data, position);
    }

    /**
     * 根据data获取它所匹配的Type类型，检查是否存在多匹配和无匹配的情况
     *
     * @param data
     * @return
     */
    private DataType<T> getMatchedDataType(T data, int position) {
        int size = dataTypeList.size();
        List<DataType<T>> matchedDataTypeList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            DataType<T> dataType = dataTypeList.get(i);
            boolean matching = dataType.isMatching(data, position);
            if (matching) {
                matchedDataTypeList.add(dataType);
            }
        }
        int matchedDataTypeSize = matchedDataTypeList.size();
        if (matchedDataTypeSize == 0) {
            throw new IllegalArgumentException("No dataType matched the data :" + data.toString() + " in position :" + position);
        } else if (matchedDataTypeSize > 1) {
            throw new IllegalArgumentException(matchedDataTypeSize + " DataType.Class matched the data :" + data.toString() + " in position :" + position);
        } else {
            return matchedDataTypeList.get(0);
        }
    }


    void addViewType(DataType<T> viewType) {
        dataTypeList.append(viewTypeKey, viewType);
        viewTypeKey++;
    }

    DataType<T> getDateType(int viewType) {
        return dataTypeList.get(viewType);
    }

    public SparseArray<DataType<T>> getDataTypeList() {
        return dataTypeList;
    }

}
