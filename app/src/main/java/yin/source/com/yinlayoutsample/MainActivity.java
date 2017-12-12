package yin.source.com.yinlayoutsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnFlowLayout;
    private Button btnCheckableGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnFlowLayout = findViewById(R.id.btn_flow_layout);
        btnCheckableGroup = findViewById(R.id.btn_checkable_group);

        btnFlowLayout.setOnClickListener(this);
        btnCheckableGroup.setOnClickListener(this);
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
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
