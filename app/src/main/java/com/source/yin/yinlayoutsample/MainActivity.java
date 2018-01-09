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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnFlowLayout = findViewById(R.id.btn_flow_layout);
        btnCheckableGroup = findViewById(R.id.btn_checkable_group);
        btnMoonMenu = findViewById(R.id.btn_moon_menu);
        btnSideMenu = findViewById(R.id.btn_side_menu);

        btnFlowLayout.setOnClickListener(this);
        btnCheckableGroup.setOnClickListener(this);
        btnMoonMenu.setOnClickListener(this);
        btnSideMenu.setOnClickListener(this);
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
            case R.id.btn_moon_menu:
                intent = new Intent(getApplicationContext(), MoonMenuActivity.class);
                break;
            case R.id.btn_side_menu:
                intent = new Intent(getApplicationContext(), SideMenuActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
