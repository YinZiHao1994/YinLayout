package com.source.yin.yinlayoutsample.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder> {

    private Context context;
    private List<T> dataList;
    @LayoutRes
    private int layoutRes;

    public BaseAdapter(Context context, List<T> dataList, @LayoutRes int layoutRes) {
        this.context = context;
        this.dataList = dataList;
        this.layoutRes = layoutRes;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return BaseViewHolder.createViewHolder(context, parent, layoutRes);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T data = dataList.get(position);
        onBindView(holder, data, position);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        public static BaseViewHolder createViewHolder(Context context, ViewGroup parent, @LayoutRes int layoutRes) {
            View inflate = LayoutInflater.from(context).inflate(layoutRes, parent,false);
            return new BaseViewHolder(inflate);
        }

        public BaseViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public View getView() {
            return itemView;
        }
    }

    public abstract void onBindView(BaseViewHolder holder, T data, int position);
}
