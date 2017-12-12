package yin.source.com.yinlayoutsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yin.source.com.yinlayout.CommonCheckableGroup;
import yin.source.com.yinlayout.flowLayout.BaseLayoutAdapter;

/**
 * Created by yin on 2017/12/12.
 */

public class CheckableGroupSampleActivity extends AppCompatActivity {


    private List<String> stringList;

    private CommonCheckableGroup commonCheckableGroup;
    private TextView tvChecked;
    private TextView tvCheckedUseAdapter;

    private CommonCheckableGroup commonCheckableGroupUseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkable_group_sample);
        commonCheckableGroup = findViewById(R.id.common_checkable_group);
        tvChecked = findViewById(R.id.tv_checked);

        commonCheckableGroupUseAdapter = findViewById(R.id.common_checkable_group_use_adapter);
        tvCheckedUseAdapter = findViewById(R.id.tv_checked_use_adapter);
        stringList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            stringList.add("" + i * 13);
        }
        initCommonCheckableGroup();

        initCommonCheckableGroupUseAdapter();
    }

    private void initCommonCheckableGroupUseAdapter() {
        commonCheckableGroupUseAdapter.setAdapter(new BaseLayoutAdapter<String>(getApplicationContext(), stringList, R.layout.checkable_group_layout_item) {
            @Override
            public void dataBind(View itemView, int position, String data) {
                TextView textView = (TextView) itemView.findViewById(R.id.tv_item);
                textView.setText(data);
            }
        });

        commonCheckableGroupUseAdapter.setCheckedListener(new CommonCheckableGroup.CheckedListener() {
            @Override
            public void onCheckChange(Checkable checkable) {
                showCheckedUseAdapter();

            }
        });
    }

    private void initCommonCheckableGroup() {
        commonCheckableGroup.setCheckedListener(new CommonCheckableGroup.CheckedListener() {
            @Override
            public void onCheckChange(Checkable checkable) {
                showChecked();
            }
        });
    }

    private void showChecked() {
        List<Checkable> checkedItemList = commonCheckableGroup.getCheckedItemList();
        StringBuilder text = new StringBuilder();
        for (Checkable checkable : checkedItemList) {
            ViewGroup viewGroup = (ViewGroup) checkable;
            TextView textView = (TextView) ((ViewGroup) viewGroup.getChildAt(0)).getChildAt(0);
            text.append(textView.getText().toString());
        }
        tvChecked.setText(text);
    }

    private void showCheckedUseAdapter() {
        List<Checkable> checkedItemList = commonCheckableGroupUseAdapter.getCheckedItemList();
        StringBuilder text = new StringBuilder();
        for (Checkable checkable : checkedItemList) {
            ViewGroup viewGroup = (ViewGroup) checkable;
            TextView textView = (TextView) (viewGroup.getChildAt(0));
            text.append(textView.getText().toString());
        }
        tvCheckedUseAdapter.setText(text);
    }
}
