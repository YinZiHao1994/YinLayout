package com.source.yin.yinlayoutsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.source.yin.yinlayout.pulltorefreshlayout.PullToRefreshLayout;
import com.source.yin.yinlayoutsample.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class PullToRefreshLayoutActivity extends AppCompatActivity {

    private PullToRefreshLayout pullToRefreshLayout;
    private RecyclerView recyclerView;
    private List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh);


        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        pullToRefreshLayout.setRefreshListener(new PullToRefreshLayout.RefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshLayout.finishRefresh();
                    }
                }, 1000);
            }
        });

        dataList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            dataList.add(String.valueOf(i));
        }

        BaseAdapter<String> adapter = new BaseAdapter<String>(getApplicationContext(), dataList, R.layout.text_list_item) {
            @Override
            public void onBindView(BaseViewHolder holder, String data, int position) {
                TextView textView = (TextView) holder.getView().findViewById(R.id.tv);
                textView.setText(data);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}
