package com.boger.pictureutils.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.boger.pictureutils.R;

import java.io.File;
import java.io.IOException;

/**
 * Created by liubo on 2017/3/27.
 */

public class SimpleActivity extends AppCompatActivity {
    public static final int REQUEST_CAMERA = 1;
    private static final int REQEUST_PERMISSION_CAMERA = 2;
    private static final String TAG = "SimpleActivity";
    private String mCameraImagePath;
    private ImageView imageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        Button viewById = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.image);

        viewById.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SimpleActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(SimpleActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    takeCamera();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQEUST_PERMISSION_CAMERA);
                }
            }
        });
    }

    private void addGallery() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCameraImagePath);
        Uri uri = Uri.fromFile(f);
        intent.setData(uri);
        this.sendBroadcast(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQEUST_PERMISSION_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takeCamera();
            }
        }
    }

    private void takeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            File file = null;
            try {
                file = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (file != null) {
                Uri uri = null;
                if (Build.VERSION.SDK_INT >= 25) {

                    uri = FileProvider.getUriForFile(this, "com.boger.pictureutils.MyFileProvider", file);
                } else {
                    uri = Uri.fromFile(file);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + mCameraImagePath + "]");
            addGallery();
            readPictureDegree();
            showImage();
        }
    }

    int degree = 0;

    private void readPictureDegree() {
        try {
            ExifInterface exifInterface = new ExifInterface(mCameraImagePath);
            int oriention = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (oriention) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            degree = 0;
            e.printStackTrace();
        }

    }

    private void showImage() {
        int width = imageView.getWidth();
        int height = imageView.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        int ow = options.outWidth;
        int oh = options.outHeight;
        int scale = Math.min(ow / width, oh / height);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;

        Bitmap bitmap = BitmapFactory.decodeFile(mCameraImagePath, options);
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        imageView.setImageBitmap(bitmap);
    }

    private File createImageFile() throws IOException {
        String imageName = System.currentTimeMillis() + "";
        File file = File.createTempFile(imageName, ".jpeg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        mCameraImagePath = file.getAbsolutePath();
        return file;
    }
}
