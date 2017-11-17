package yin.source.com.yinlayout;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yin on 2017/11/17.
 */

public abstract class FlowLayoutAdapter<T> {

    private Context context;
    private List<T> dataList;
    private List<DataChangeListener> dataChangeListenerList;

    @LayoutRes
    private int layoutRes;

    public FlowLayoutAdapter(Context context, List<T> dataList, int layoutRes) {
        this.context = context;
        this.dataList = dataList;
        this.layoutRes = layoutRes;
    }

    public void notidifyDataChanged() {
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

    public int getLayoutRes() {
        return layoutRes;
    }

    public void onDataBind(View view, int position) {
        T data = dataList.get(position);
        dataBind(view, position, data);
    }

    public abstract void dataBind(View itemView, int position, T data);

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
