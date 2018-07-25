package com.source.yin.yinlayoutsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.Toast;

import com.source.yin.yinlayout.checkable.CheckableGroupManager;
import com.source.yin.yinlayout.checkable.CommonCheckableGroup;

public class UseItemClickInterceptorActivity extends AppCompatActivity {
    private CommonCheckableGroup commonCheckableGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_item_click_interceptor);

        commonCheckableGroup = (CommonCheckableGroup) findViewById(R.id.common_checkable_group);
        commonCheckableGroup.setItemClickInterceptor(new CheckableGroupManager.ItemClickInterceptor() {
            @Override
            public boolean onInterceptorItemClick(Checkable checkable) {
                if (checkable instanceof CheckedTextView) {
                    CheckedTextView checkedTextView = (CheckedTextView) checkable;
                    String s = checkedTextView.getText().toString();
                    if (s.equals("拦截")) {
                        showToast("被拦截");
                        return true;
                    } else {
                        showToast("没有被拦截");
                        return false;
                    }
                }
                return false;
            }
        });
    }


    public void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
