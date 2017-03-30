package com.boger.imagepicker.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.boger.imagepicker.R;
import com.boger.imagepicker.adapter.ImageAdapter;
import com.boger.imagepicker.model.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ImageSelectActivity extends AppCompatActivity {
    private static final int SCAN_IMAGE = 1;
    private static final int SCAN_SUCCESS = 2;
    private static final int SCAN_FAIL = 3;

    private String albumName;
    private Handler handler;
    private Thread thread;
    private ContentObserver observer;
    private String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
    private List<Image> data = new ArrayList<>();

    private RecyclerView imageRv;

    public static void enter(Activity activity, String albumName) {
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        intent.putExtra("albumName", albumName);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        imageRv = (RecyclerView) findViewById(R.id.imageRv);
        imageRv.setLayoutManager(new GridLayoutManager(this, 3));
        imageRv.setAdapter(new ImageAdapter(data));
        imageRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = getResources().getDimensionPixelSize(R.dimen.offset);
                outRect.right = getResources().getDimensionPixelSize(R.dimen.offset);
            }
        });

        Intent intent = getIntent();
        albumName = intent.getStringExtra("albumName");
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SCAN_IMAGE:
                        loadImages();
                        break;
                    case SCAN_SUCCESS:
                        imageRv.getAdapter().notifyDataSetChanged();
                        break;
                    case SCAN_FAIL:
                        break;
                }
            }
        };

        observer = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                loadImages();
            }
        };

        getApplicationContext().getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, observer);
        handler.sendEmptyMessage(SCAN_IMAGE);
    }

    private void loadImages() {
        stopThread();
        thread = new Thread(new ImageRunnable());
        thread.start();
    }

    private void stopThread() {
        if (thread == null || !thread.isAlive()) {
            return;
        }

        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class ImageRunnable implements Runnable {

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            /*
            记录image是否select
             */
            HashSet<Long> set = new HashSet<>();
            File file;
            for (int i = 0; i < data.size(); i++) {
                Image image = data.get(i);
                file = new File(image.path);
                if (file.exists() && image.isSelect) {
                    set.add(image.id);
                }
            }

            Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "= ?", new String[]{albumName}, MediaStore.Images.Media.DATE_MODIFIED);
            if (cursor == null) {
                handler.sendEmptyMessage(SCAN_FAIL);
                return;
            }

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(projection[0]));
                String name = cursor.getString(cursor.getColumnIndex(projection[1]));
                String path = cursor.getString(cursor.getColumnIndex(projection[2]));
                boolean select = set.contains(id);

                File f = new File(path);
                if (f.exists()) {
                    data.add(new Image(id, path, name, select));
                }
            }

            handler.sendEmptyMessage(SCAN_SUCCESS);
            cursor.close();
        }
    }
}
