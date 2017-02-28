package com.test.lbviews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.lbviews.views.ImageStepViews;
import com.test.lbviews.views.MovePath;
import com.test.lbviews.views.StepViews;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private StepViews mStepView;
    private ImageStepViews mImageStepViews;
    private MovePath mMovePath;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mStepView = (StepViews) findViewById(R.id.stepView);

        String[] strings = getResources().getStringArray(R.array.default_steps);

        List<String> list = Arrays.asList(strings);
        mStepView.setCurrentSteps(2).setSteps(list).build();

        mImageStepViews = (ImageStepViews) findViewById(R.id.step);
        mImageStepViews.setOnStepClickListener(new ImageStepViews.OnStepClickListener() {
            @Override
            public void stepClick(int clickStep) {
                Toast.makeText(MainActivity.this, "click step is" + clickStep, Toast.LENGTH_SHORT).show();
            }
        });

        mMovePath = (MovePath) findViewById(R.id.move);
        mMovePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMovePath.start();
            }
        });

    }

}
