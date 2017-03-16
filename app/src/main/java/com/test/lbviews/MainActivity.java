package com.test.lbviews;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.test.lbviews.model.PieData;
import com.test.lbviews.views.ImageStepViews;
import com.test.lbviews.views.MovePath;
import com.test.lbviews.views.StepViews;

import java.util.ArrayList;
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

        Display dl = getWindowManager().getDefaultDisplay();

        ArrayList<PieData> list1 = new ArrayList<>();
        PieData pd = new PieData();
        pd.setColor(Color.YELLOW);
        pd.setPercentage(0.3f);
        list1.add(pd);

        PieData pieData = new PieData();
        pieData.setPercentage(0.1f);
        pieData.setColor(Color.RED);
        list1.add(pieData);

        PieData pd2 = new PieData();
        pd2.setPercentage(0.3f);
        pd2.setColor(Color.BLACK);
        list1.add(pd2);

        PieData pd3 = new PieData();
        pd3.setPercentage(0.1f);
        pd3.setColor(Color.BLUE);
        list1.add(pd3);

        /*PieView v = (PieView) findViewById(R.id.pie);
        v.setmViewData(list1);*/


    }

}
