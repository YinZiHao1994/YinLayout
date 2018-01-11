package com.source.yin.yinlayoutsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.source.yin.yinlayout.flipview.FlipView;
import com.source.yin.yinlayout.layoutadapter.BaseLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

public class FlipActivity extends AppCompatActivity {


    private FlipView flipViewText;
    private FlipView flipViewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip);

        flipViewText = findViewById(R.id.flip_view_text);
        flipViewImage = findViewById(R.id.flip_view_image);


        initFlipText();
        initFlipImage();
    }

    private void initFlipImage() {
        List<Integer> drawableResList = new ArrayList<>();
        drawableResList.add(R.mipmap.ic_launcher);
        drawableResList.add(R.mipmap.ic_launcher_round);
        flipViewImage.setLayoutAdapter(new BaseLayoutAdapter<Integer>(getApplicationContext(), drawableResList, R.layout.item_image_flip) {
            @Override
            public void dataBind(View itemView, int position, Integer data) {
                if (itemView instanceof ImageView) {
                    ((ImageView) itemView).setImageResource(data);
                }
            }
        });
        flipViewImage.setOnItemClick(new FlipView.OnItemClick() {
            @Override
            public void onItemClick(int itemPosition, View itemView) {
                Toast.makeText(getApplicationContext(), "image item : " + itemPosition, Toast.LENGTH_SHORT).show();
            }
        });
        flipViewImage.setAnimDirection(FlipView.AnimDirection.HORIZONTAL);
    }

    private void initFlipText() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            dataList.add(String.valueOf(i));
        }
        flipViewText.setLayoutAdapter(new BaseLayoutAdapter<String>(getApplicationContext(), dataList, R.layout.item_text_flip) {
            @Override
            public void dataBind(View itemView, int position, String data) {
                if (itemView instanceof TextView) {
                    ((TextView) itemView).setText(data);
                }
            }
        });
        flipViewText.setOnItemClick(new FlipView.OnItemClick() {
            @Override
            public void onItemClick(int itemPosition, View itemView) {
                Toast.makeText(getApplicationContext(), "text item : " + itemPosition, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
