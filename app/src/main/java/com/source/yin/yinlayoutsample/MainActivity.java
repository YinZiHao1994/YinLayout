package com.source.yin.yinlayoutsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnFlowLayout;
    private Button btnCheckableGroup;
    private Button btnMoonMenu;
    private Button btnSideMenu;
    private Button btnFlipView;
    private Button btnPullToRefresh;
    private Button btnDifferentTypeCheckableItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnFlowLayout = (Button) findViewById(R.id.btn_flow_layout);
        btnCheckableGroup = (Button)findViewById(R.id.btn_checkable_group);
        btnMoonMenu = (Button)findViewById(R.id.btn_moon_menu);
        btnSideMenu = (Button)findViewById(R.id.btn_side_menu);
        btnFlipView = (Button)findViewById(R.id.btn_flip_view);
        btnPullToRefresh = (Button)findViewById(R.id.btn_pull_to_refresh);
        btnDifferentTypeCheckableItem = (Button)findViewById(R.id.btn_different_type_checkable_item);

        btnFlowLayout.setOnClickListener(this);
        btnCheckableGroup.setOnClickListener(this);
        btnMoonMenu.setOnClickListener(this);
        btnSideMenu.setOnClickListener(this);
        btnFlipView.setOnClickListener(this);
        btnPullToRefresh.setOnClickListener(this);
        btnDifferentTypeCheckableItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_flow_layout:
                intent = new Intent(getApplicationContext(), FlowLayoutSampleActivity.class);
                break;
            case R.id.btn_checkable_group:
                intent = new Intent(getApplicationContext(), CheckableGroupSampleActivity.class);
                break;
            case R.id.btn_different_type_checkable_item:
                intent = new Intent(getApplicationContext(), DifferentTypeCheckableItemActivity.class);
                break;
            case R.id.btn_moon_menu:
                intent = new Intent(getApplicationContext(), MoonMenuActivity.class);
                break;
            case R.id.btn_side_menu:
                intent = new Intent(getApplicationContext(), SideMenuActivity.class);
                break;
            case R.id.btn_flip_view:
                intent = new Intent(getApplicationContext(), FlipActivity.class);
                break;
            case R.id.btn_pull_to_refresh:
                intent = new Intent(getApplicationContext(), PullToRefreshLayoutActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
