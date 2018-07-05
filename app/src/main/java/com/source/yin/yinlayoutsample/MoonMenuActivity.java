package com.source.yin.yinlayoutsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.source.yin.yinlayout.moonmenu.MoonMenu;

public class MoonMenuActivity extends AppCompatActivity {


    private MoonMenu moonMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moon_menu);

        moonMenu = (MoonMenu) findViewById(R.id.moon_menu);

        moonMenu.setOnMenuItemClickListener(new MoonMenu.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(View view) {
                Toast.makeText(getApplicationContext(), "点击了 ：" + view.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
