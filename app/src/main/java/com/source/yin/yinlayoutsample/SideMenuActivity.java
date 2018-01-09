package com.source.yin.yinlayoutsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

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

    }
}
