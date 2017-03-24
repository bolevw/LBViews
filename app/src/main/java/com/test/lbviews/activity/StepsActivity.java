package com.test.lbviews.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.test.lbviews.R;
import com.test.lbviews.views.StepViews;

import java.util.Arrays;
import java.util.List;

public class StepsActivity extends AppCompatActivity {
    private StepViews mStepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        mStepView = (StepViews) findViewById(R.id.stepView);

        String[] strings = getResources().getStringArray(R.array.default_steps);

        List<String> list = Arrays.asList(strings);
        mStepView.setCurrentSteps(2).setSteps(list).build();
    }
}
