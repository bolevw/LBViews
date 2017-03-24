package com.test.lbviews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.test.lbviews.R;
import com.test.lbviews.views.ImageStepViews;

public class ImageStepsActivity extends AppCompatActivity {
    private ImageStepViews mImageStepViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_steps);
        mImageStepViews = (ImageStepViews) findViewById(R.id.step);
        mImageStepViews.setOnStepClickListener(new ImageStepViews.OnStepClickListener() {
            @Override
            public void stepClick(int clickStep) {
                Toast.makeText(ImageStepsActivity.this, "click step is" + clickStep, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
