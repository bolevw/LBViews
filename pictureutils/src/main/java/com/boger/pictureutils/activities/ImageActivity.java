package com.boger.pictureutils.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boger.pictureutils.R;
import com.boger.pictureutils.adapter.ImageAdapter;

import java.util.List;

public class ImageActivity extends AppCompatActivity {
    private RecyclerView imageRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageRv = (RecyclerView) findViewById(R.id.imageRv);
        imageRv.setLayoutManager(new GridLayoutManager(this, 2));

        List<String> data = getIntent().getStringArrayListExtra("data");
        imageRv.setAdapter(new ImageAdapter(data));
    }
}
