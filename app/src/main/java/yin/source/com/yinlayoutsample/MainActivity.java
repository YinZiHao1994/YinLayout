package yin.source.com.yinlayoutsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yin.source.com.yinlayout.CheckableGroupFlowLayout;
import yin.source.com.yinlayout.FlowLayout;
import yin.source.com.yinlayout.FlowLayoutAdapter;

public class MainActivity extends AppCompatActivity {

    private FlowLayout flowLayout;
    private List<String> stringList;
    private CheckableGroupFlowLayout checkableGroupFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        checkableGroupFlowLayout = (CheckableGroupFlowLayout) findViewById(R.id.checkable_flow_layout);

        stringList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stringList.add("" + i * 13);
        }
        flowLayout.setAdapter(new FlowLayoutAdapter<String>(this, stringList, R.layout.default_flow_layout_item) {
            @Override
            public void dataBind(View itemView, int position, String data) {
                TextView textView = (TextView) itemView.findViewById(R.id.tv_item);
                textView.setText(data);
            }
        });

        checkableGroupFlowLayout.setAdapter(new FlowLayoutAdapter<String>(this, stringList, R.layout.default_flow_layout_item) {
            @Override
            public void dataBind(View itemView, int position, String data) {
                TextView textView = (TextView) itemView.findViewById(R.id.tv_item);
                textView.setText(data);
            }
        });
    }
}
