package com.boger.pictureutils.activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boger.pictureutils.R;
import com.boger.pictureutils.adapter.AlbumAdapter;
import com.boger.pictureutils.model.Album;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AlbumActivity extends AppCompatActivity {
    private static final int TYPE_START = 1;
    private static final int TYPE_FINISH = 2;
    private RecyclerView albumRv;
    private Thread scanImageThread;
    private HashMap<String, ArrayList<String>> albumGroup = new HashMap<>();

    public Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_START:
                    break;
                case TYPE_FINISH:
                    notifyDataHasChange();
                    break;
            }
        }
    };

    private void notifyDataHasChange() {
        albumRv.setAdapter(new AlbumAdapter(fixData(), this));
        albumRv.getAdapter().notifyDataSetChanged();
    }

    private List<Album> fixData() {
        List<Album> result = new ArrayList<>();
        Iterator<Map.Entry<String, ArrayList<String>>> iterator = albumGroup.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<String>> entry = iterator.next();
            Album album = new Album(entry.getValue().get(0), entry.getKey(), entry.getValue().size(), entry.getValue());
            result.add(album);
        }
        return result;
    }


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_album);

        albumRv = (RecyclerView) findViewById(R.id.albumRv);
        albumRv.setLayoutManager(new GridLayoutManager(this, 2));

        getImages();
    }

    public void getImages() {
        scanImageThread = new Thread(new ScanRunnable(AlbumActivity.this));
        scanImageThread.start();
    }


    class ScanRunnable implements Runnable {
        private WeakReference<Activity> weakReference;

        public ScanRunnable(Activity activity) {
            this.weakReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void run() {
            Uri albumUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver resolver = weakReference.get().getContentResolver();

            Cursor cursor = resolver.query(
                    albumUri,
                    null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png"},
                    MediaStore.Images.Media.DATE_MODIFIED
            );
            if (cursor == null) {
                return;
            }
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String parentName = new File(path).getParentFile().getName();

                if (albumGroup.containsKey(parentName)) {
                    albumGroup.get(parentName).add(path);
                } else {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(path);
                    albumGroup.put(parentName, list);
                }
            }
            handler.sendEmptyMessage(TYPE_FINISH);
            cursor.close();
        }
    }
}
