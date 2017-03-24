package com.test.lbviews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.test.lbviews.R;
import com.test.lbviews.views.path.DragCircleView;

/**
 * Created by liubo on 2017/3/23.
 */

public class DragViewActivity extends AppCompatActivity {
    private CheckBox checkBox;
    private DragCircleView dragCircleView;
    private Button reset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_circle_view);
        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dragCircleView.reset();
            }
        });
        dragCircleView = (DragCircleView) findViewById(R.id.drag);
        checkBox = (CheckBox) findViewById(R.id.check);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dragCircleView.setPaintStyle(checkBox.isChecked());
            }
        });
    }
}
