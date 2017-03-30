package com.test.lbviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.boger.imagepicker.activities.AlbumSelectActivity;
import com.boger.pictureutils.activities.AlbumActivity;
import com.boger.pictureutils.activities.SimpleActivity;
import com.test.lbviews.activity.BersaierCircleActivity;
import com.test.lbviews.activity.DragViewActivity;
import com.test.lbviews.activity.ImageStepsActivity;
import com.test.lbviews.activity.LeafsLoadingActivity;
import com.test.lbviews.activity.MoveActivity;
import com.test.lbviews.activity.SimpleBerSaierActivity;
import com.test.lbviews.activity.SpiderActivity;
import com.test.lbviews.activity.StepsActivity;
import com.test.lbviews.views.ImageStepViews;
import com.test.lbviews.views.MovePath;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private ImageStepViews mImageStepViews;
    private MovePath mMovePath;
    private MainActivity mainActivity;

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
        mainActivity = this;
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        jump(R.id.bersaierCircle, BersaierCircleActivity.class);
        jump(R.id.drag, DragViewActivity.class);
        jump(R.id.imagestepView, ImageStepsActivity.class);
        jump(R.id.leafs, LeafsLoadingActivity.class);
        jump(R.id.move, MoveActivity.class);
        jump(R.id.simpleberser, SimpleBerSaierActivity.class);
        jump(R.id.spider, SpiderActivity.class);
        jump(R.id.steps, StepsActivity.class);
        jump(R.id.camera, SimpleActivity.class);
        jump(R.id.album, AlbumActivity.class);
        jump(R.id.albumSelect, AlbumSelectActivity.class);
    }


    public void jump(int resId, final Class clazz) {
        findViewById(resId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainActivity, clazz));
            }
        });
    }

}
