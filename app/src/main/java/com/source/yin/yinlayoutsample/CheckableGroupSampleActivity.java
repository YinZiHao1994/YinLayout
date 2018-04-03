package com.source.yin.yinlayoutsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.TextView;

import com.source.yin.yinlayout.checkable.CheckableGroup;
import com.source.yin.yinlayout.checkable.CommonCheckableGroup;
import com.source.yin.yinlayout.checkable.OnItemCheckListener;
import com.source.yin.yinlayout.layoutadapter.BaseLayoutAdapter;

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
    private TextView tvUseAdapterHorizontal;

    private CommonCheckableGroup commonCheckableGroupUseAdapter;
    private CommonCheckableGroup commonCheckableGroupUseAdapterHorizontal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkable_group_sample);
        commonCheckableGroup = findViewById(R.id.common_checkable_group);
        tvChecked = findViewById(R.id.tv_checked);
        tvUseAdapterHorizontal = findViewById(R.id.tv_checked_use_adapter_horizontal);

        commonCheckableGroupUseAdapter = findViewById(R.id.common_checkable_group_use_adapter);
        commonCheckableGroupUseAdapter.setMultiple(false);
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

        commonCheckableGroupUseAdapter.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public void onCheckedStateChange(CheckableGroup checkableGroup) {
                showCheckedUseAdapter();
            }
        });

        commonCheckableGroupUseAdapter.checkItem(2);
        commonCheckableGroupUseAdapterHorizontal.setLayoutAdapter(new BaseLayoutAdapter<String>(getApplicationContext(), stringList, R.layout.checkable_group_layout_item_horizontal) {
            @Override
            public void dataBind(View itemView, int position, String data) {
                TextView textView = (TextView) itemView;
                textView.setText(data);
            }
        });
        commonCheckableGroupUseAdapterHorizontal.checkItem(1);
        commonCheckableGroupUseAdapterHorizontal.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public void onCheckedStateChange(CheckableGroup checkableGroup) {
                Checkable checkedItem = commonCheckableGroupUseAdapterHorizontal.getCheckedItem();
                if (checkedItem instanceof TextView) {
                    String s = ((TextView) checkedItem).getText().toString();
                    Log.d("yzh", "s = " + s);
                    tvUseAdapterHorizontal.setText(s);
                }
            }
        });

    }

    private void initCommonCheckableGroup() {
        commonCheckableGroup.setOnItemCheckListener(new OnItemCheckListener() {
            @Override
            public void onCheckedStateChange(CheckableGroup checkableGroup) {
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
                if (checkable instanceof TextView) {
                    String s = ((TextView) checkable).getText().toString();
                    text.append(s);
                } else if (checkable instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) checkable;
                    View view = viewGroup.getChildAt(0);
                    if (view instanceof TextView) {
                        String s = ((TextView) view).getText().toString();
                        text.append(s);
                    }
                }
            }
        }
        tvChecked.setText(text);
    }

    private void showCheckedUseAdapter() {
        List<Integer> checkedItemPositionList = commonCheckableGroupUseAdapter.getCheckedItemPositionList();
        StringBuilder text = new StringBuilder();
        if (checkedItemPositionList != null) {
            for (Integer position : checkedItemPositionList) {
//                Checkable checkable = checkableItemWrapper.getCheckable();
//                int positionInList = checkableItemWrapper.getPositionInList();
//                ViewGroup viewGroup = (ViewGroup) checkable;
//                TextView textView = (TextView) (viewGroup.getChildAt(0));
                text.append("第 " + position + " 项 ");
            }
        }
        tvCheckedUseAdapter.setText(text);
    }
}
