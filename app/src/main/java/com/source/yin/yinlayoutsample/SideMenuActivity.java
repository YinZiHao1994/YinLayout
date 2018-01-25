package com.source.yin.yinlayoutsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.source.yin.yinlayout.sidemenulayout.SideMenuLayout;

import java.util.ArrayList;
import java.util.List;

public class SideMenuActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_menu);

        recyclerView = findViewById(R.id.recycler_view);
        initAdapter();
    }

    private void initAdapter() {
        List<String> dataList = new ArrayList<>();
        for (int i = 100000; i < 100100; i++) {
            dataList.add(String.valueOf(i * i));

        }
        SideMenuAdapter<String> stringSideMenuAdapter = new SideMenuAdapter<>(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(stringSideMenuAdapter);
        stringSideMenuAdapter.notifyDataSetChanged();
    }


    class SideMenuAdapter<T> extends RecyclerView.Adapter<SideMenuAdapter.MyViewHolder> {

        private List<T> dataList;

        public SideMenuAdapter(List<T> dataList) {
            this.dataList = dataList;
        }

        @Override
        public SideMenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.side_menu_item, parent, false);
            return new MyViewHolder(inflate);
        }

        @Override
        public void onBindViewHolder(SideMenuAdapter.MyViewHolder holder, int position) {
            TextView tvContent = holder.getTvContent();
            tvContent.setText((String) dataList.get(position));
            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "clickContent", Toast.LENGTH_SHORT).show();
                }
            });
            SideMenuLayout sideMenuLayout = holder.getSideMenuLayout();
            sideMenuLayout.setOnMenuClickListener(new SideMenuLayout.OnMenuClickListener() {
                @Override
                public void onMenuClickListener(View view) {
                    if (view instanceof TextView) {
                        String s = ((TextView) view).getText().toString();
                        Toast.makeText(getApplicationContext(), "click menu : " + s, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "click : " + view.getId(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            int count = 0;
            if (dataList != null) {
                count = dataList.size();
            }
            return count;
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvContent;
            private SideMenuLayout sideMenuLayout;

            public MyViewHolder(View itemView) {
                super(itemView);
                sideMenuLayout = (SideMenuLayout) itemView;
                tvContent = itemView.findViewById(R.id.tv_content);
            }

            public TextView getTvContent() {
                return tvContent;
            }

            public SideMenuLayout getSideMenuLayout() {
                return sideMenuLayout;
            }
        }
    }


}
