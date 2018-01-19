package com.source.yin.yinlayoutsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import com.source.yin.yinlayout.layoutadapter.BaseLayoutAdapter;
import com.source.yin.yinlayout.checkable.CommonCheckableGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yin on 2017/12/12.
 */

public class CheckableGroupSampleActivity extends AppCompatActivity {


    private List<String> stringList;

    private CommonCheckableGroup commonCheckableGroup;
    private TextView tvChecked;
    private TextView tvCheckedUseAdapter;

    private CommonCheckableGroup commonCheckableGroupUseAdapter;
    private CommonCheckableGroup commonCheckableGroupUseAdapterHorizontal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkable_group_sample);
        commonCheckableGroup = findViewById(R.id.common_checkable_group);
        tvChecked = findViewById(R.id.tv_checked);

        commonCheckableGroupUseAdapter = findViewById(R.id.common_checkable_group_use_adapter);
        commonCheckableGroupUseAdapterHorizontal = findViewById(R.id.common_checkable_group_use_adapter_horizontal);
        tvCheckedUseAdapter = findViewById(R.id.tv_checked_use_adapter);
        stringList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            stringList.add("" + i * 13);
        }
        initCommonCheckableGroup();

        initCommonCheckableGroupUseAdapter();
    }

    private void initCommonCheckableGroupUseAdapter() {
        commonCheckableGroupUseAdapter.setLayoutAdapter(new BaseLayoutAdapter<String>(getApplicationContext(), stringList, R.layout.checkable_group_layout_item) {
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

        commonCheckableGroupUseAdapter.checkItem(2);
        commonCheckableGroupUseAdapterHorizontal.setLayoutAdapter(new BaseLayoutAdapter<String>(getApplicationContext(), stringList, R.layout.checkable_group_layout_item_horizontal) {
            @Override
            public void dataBind(View itemView, int position, String data) {
                TextView textView = (TextView) itemView.findViewById(R.id.tv_item);
                textView.setText(data);
            }
        });
        commonCheckableGroupUseAdapterHorizontal.checkItem(1);
    }

    private void initCommonCheckableGroup() {
        commonCheckableGroup.setCheckedListener(new CommonCheckableGroup.CheckedListener() {
            @Override
            public void onCheckChange(Checkable checkable) {
                showChecked();
            }
        });

        commonCheckableGroup.checkItem(0);
    }

    private void showChecked() {
        List<Checkable> checkedItemList = commonCheckableGroup.getCheckedItemList();
        StringBuilder text = new StringBuilder();
        if (checkedItemList != null) {
            for (Checkable checkable : checkedItemList) {
                ViewGroup viewGroup = (ViewGroup) checkable;
                TextView textView = (TextView) ((ViewGroup) viewGroup.getChildAt(0)).getChildAt(0);
                text.append(textView.getText().toString());
            }
        }
        tvChecked.setText(text);
    }

    private void showCheckedUseAdapter() {
        List<Checkable> checkedItemList = commonCheckableGroupUseAdapter.getCheckedItemList();
        StringBuilder text = new StringBuilder();
        if (checkedItemList != null) {
            for (Checkable checkable : checkedItemList) {
                ViewGroup viewGroup = (ViewGroup) checkable;
                TextView textView = (TextView) (viewGroup.getChildAt(0));
                text.append(textView.getText().toString());
            }
        }
        tvCheckedUseAdapter.setText(text);
    }
}
