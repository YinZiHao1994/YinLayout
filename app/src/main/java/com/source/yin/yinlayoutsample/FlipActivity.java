package com.source.yin.yinlayoutsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.source.yin.yinlayout.flipview.FlipView;
import com.source.yin.yinlayout.layoutadapter.BaseLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

public class FlipActivity extends AppCompatActivity {


    private FlipView flipViewText;
    private FlipView flipViewImage;

    private SeekBar seekBar;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip);

        flipViewText = findViewById(R.id.flip_view_text);
        flipViewImage = findViewById(R.id.flip_view_image);
        seekBar = findViewById(R.id.seek_bar);
        radioGroup = findViewById(R.id.radio_group);

        initFlipText();
        initFlipImage();

        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        changeAnimDirection(checkedRadioButtonId);

        seekBar.setProgress(2);
        changeSetInterval(seekBar.getProgress());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                changeSetInterval(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeAnimDirection(checkedId);
            }
        });
    }

    private void changeSetInterval(int progress) {
        if (progress < 1) {
            progress = 1;
        }
        int second = progress * 1000;
        flipViewText.setInterval(second);
        flipViewImage.setInterval(second);
    }

    private void changeAnimDirection(int checkedRadioButtonId) {
        switch (checkedRadioButtonId) {
            case R.id.radio_horizontal:
                flipViewImage.setAnimDirection(FlipView.AnimDirection.HORIZONTAL);
                flipViewText.setAnimDirection(FlipView.AnimDirection.HORIZONTAL);
                break;
            case R.id.radio_vertical:
                flipViewImage.setAnimDirection(FlipView.AnimDirection.VERTICAL);
                flipViewText.setAnimDirection(FlipView.AnimDirection.VERTICAL);
                break;
        }
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
