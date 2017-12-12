package yin.source.com.yinlayoutsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Checkable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yin.source.com.yinlayout.CheckableTag;
import yin.source.com.yinlayout.flowLayout.CheckableGroupFlowLayout;
import yin.source.com.yinlayout.flowLayout.FlowLayout;
import yin.source.com.yinlayout.flowLayout.FlowLayoutAdapter;

/**
 * Created by yin on 2017/12/12.
 */

public class FlowLayoutSampleActivity extends AppCompatActivity {

    private FlowLayout flowLayout;
    private List<String> stringList;
    private CheckableGroupFlowLayout checkableGroupFlowLayout;
    private CheckableGroupFlowLayout checkableGroupFlowLayoutMultiple;
    private TextView tvCheckedNum;
    private TextView tvChecked;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout_sample);

        flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        checkableGroupFlowLayout = (CheckableGroupFlowLayout) findViewById(R.id.checkable_flow_layout);
        checkableGroupFlowLayoutMultiple = (CheckableGroupFlowLayout) findViewById(R.id.checkable_flow_layout_multiple);
        tvCheckedNum = (TextView) findViewById(R.id.tv_checked_num);
        tvChecked = (TextView) findViewById(R.id.tv_checked);

        stringList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stringList.add("" + i * 13);
        }

        initFlowLayout();
        initCheckableFlowLayout();

    }


    private void initCheckableFlowLayout() {
        checkableGroupFlowLayout.setAdapter(new FlowLayoutAdapter<String>(this, stringList, R.layout.checkable_flow_layout_item) {
            @Override
            public void dataBind(View itemView, int position, String data) {
                TextView textView = (TextView) itemView.findViewById(R.id.tv_item);
                textView.setText(data);
            }
        });
        checkableGroupFlowLayout.setOnChildViewCheckListener(new CheckableGroupFlowLayout.OnChildViewCheckListener() {
            @Override
            public void onChildViewCheckedStateChanged(Checkable checkable) {
                if (checkable instanceof CheckableTag) {
                    CheckableTag checkableTag = (CheckableTag) checkable;
                    View child = checkableTag.getChildAt(0);
                    if (child instanceof TextView) {
                        TextView textView = (TextView) child;
                        tvChecked.setText(textView.getText());
                    }
                }
            }
        });


        checkableGroupFlowLayoutMultiple.setAdapter(new FlowLayoutAdapter<String>(this, stringList, R.layout.checkable_flow_layout_item2) {
            @Override
            public void dataBind(View itemView, int position, String data) {
                TextView textView = (TextView) itemView.findViewById(R.id.tv_item);
                textView.setText(data);
            }
        });
        checkableGroupFlowLayoutMultiple.setOnChildViewCheckListener(new CheckableGroupFlowLayout.OnChildViewCheckListener() {
            @Override
            public void onChildViewCheckedStateChanged(Checkable checkable) {
                List<Checkable> haveCheckedList = checkableGroupFlowLayoutMultiple.getHaveCheckedList();
                tvCheckedNum.setText(String.valueOf(haveCheckedList.size()));
            }
        });
    }

    private void initFlowLayout() {
        flowLayout.setAdapter(new FlowLayoutAdapter<String>(this, stringList, R.layout.default_flow_layout_item) {
            @Override
            public void dataBind(View itemView, int position, String data) {
                TextView textView = (TextView) itemView.findViewById(R.id.tv_item);
                textView.setText(data);
            }
        });
    }

}
