package com.source.yin.yinlayoutsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.source.yin.yinlayout.checkable.CommonCheckableGroup;
import com.source.yin.yinlayout.layoutadapter.CommonLayoutAdapter;
import com.source.yin.yinlayout.layoutadapter.DataType;

import java.util.ArrayList;
import java.util.List;

public class DifferentTypeCheckableItemActivity extends AppCompatActivity {

    private CommonCheckableGroup commonCheckableGroup;

    private List<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_different_type_checkable_item);
        stringList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            stringList.add("" + i * 13);
        }

        commonCheckableGroup = (CommonCheckableGroup) findViewById(R.id.common_checkable_group_with_different_data_type);
        commonCheckableGroup.setLayoutAdapter(new CommonLayoutAdapter<String>(getApplicationContext(), stringList) {
            @Override
            public List<DataType<String>> getDataTypes() {
                List<DataType<String>> dataTypeList = new ArrayList<>();
                DataType<String> dataType1 = new DataType<String>() {
                    @Override
                    public int getLayoutId() {
                        return R.layout.checkable_group_layout_item;
                    }

                    @Override
                    public boolean isMatching(String data, int position) {
                        return position % 2 == 0;
                    }

                    @Override
                    public void dataBind(View itemView, String data, int position) {
                        TextView textView = (TextView) itemView.findViewById(R.id.tv_item);
                        textView.setText(data);
                    }
                };
                DataType<String> dataType2 = new DataType<String>() {
                    @Override
                    public int getLayoutId() {
                        return R.layout.checkable_group_layout_item_horizontal;
                    }

                    @Override
                    public boolean isMatching(String data, int position) {
                        return position % 2 != 0;
                    }

                    @Override
                    public void dataBind(View itemView, String data, int position) {
                        TextView textView = (TextView) itemView;
                        textView.setText(data);
                    }
                };
                dataTypeList.add(dataType1);
                dataTypeList.add(dataType2);
                return dataTypeList;
            }
        });
    }
}
