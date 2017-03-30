package com.boger.imagepicker.activities;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boger.imagepicker.R;
import com.boger.imagepicker.adapter.AlbumAdapter;
import com.boger.imagepicker.model.Album;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by liubo on 2017/3/29.
 */

public class AlbumSelectActivity extends AppCompatActivity {

    private static final int SCAN_ALBUM = 1;
    private static final int SCAN_SUCCESS = 2;
    private static final int SCAN_FAIL = 3;

    private Thread albumScanThread;
    private Handler handler;
    private ContentObserver observer;
    private String[] projection = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA};
    private ArrayList<Album> data = new ArrayList<>();

    private RecyclerView albumRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_album);
        albumRv = (RecyclerView) findViewById(R.id.albumRv);
        albumRv.setLayoutManager(new GridLayoutManager(this, 2));
        albumRv.setAdapter(new AlbumAdapter(data, AlbumSelectActivity.this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SCAN_ALBUM:
                        loadAlbum();
                        break;
                    case SCAN_SUCCESS:
                        albumRv.getAdapter().notifyDataSetChanged();
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
                loadAlbum();
            }
        };

        getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                false,
                observer);

        handler.sendEmptyMessage(SCAN_ALBUM);
    }

    private void loadAlbum() {
        stopThread();
        albumScanThread = new Thread(new ScanRunnable());
        albumScanThread.start();
    }

    class ScanRunnable implements Runnable {

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Media.DATE_MODIFIED);
            ArrayList<Album> temp = new ArrayList<>(cursor.getCount());
            Set<Long> set = new HashSet<>();
            File file;

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(projection[0]));
                String name = cursor.getString(cursor.getColumnIndex(projection[1]));
                String image = cursor.getString(cursor.getColumnIndex(projection[2]));

                if (!set.contains(id)) {
                    file = new File(image);
                    if (file.exists()) {
                        temp.add(new Album(id, name, image));
                        set.add(id);
                    }
                }
            }
            cursor.close();
            data.clear();
            data.addAll(temp);
            file = null;
            set.clear();
            set = null;
            handler.sendEmptyMessage(SCAN_SUCCESS);
        }
    }

    public void stopThread() {
        if (albumScanThread != null && albumScanThread.isAlive()) {
            albumScanThread.interrupt();
            try {
                albumScanThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopThread();

        getApplicationContext().getContentResolver().unregisterContentObserver(observer);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
