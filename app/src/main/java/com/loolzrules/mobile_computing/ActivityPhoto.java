package com.loolzrules.mobile_computing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityPhoto extends AppCompatActivity {

    private String mCurrentPhotoPath;
    private ImageView mImage;
    private Button mRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mImage = findViewById(R.id.iv_photo);
        mRed = findViewById(R.id.bt_red);

        // Hacky way to disable exception
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void chooseAPhoto(View button) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public void makeAPhoto(View button) {
        File externalFilesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        File file = new File(externalFilesDir, timeStamp + "_makar.jpg");
        mCurrentPhotoPath = file.getAbsolutePath();
        Uri uri = Uri.fromFile(file);
        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(photoIntent, 2);
    }

    public void redPhoto(View button) {
        new ProcessImageTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri contentUri = data.getData();
            loadImage(contentUri);
        }

        if (requestCode == 2) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);
            loadImage(contentUri);
        }
    }

    private void loadImage(Uri uri) {
        try {
            final int SCALING_FACTOR = 4;
            Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rtdBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            rtdBmp = Bitmap.createScaledBitmap(rtdBmp, rtdBmp.getWidth() / SCALING_FACTOR, rtdBmp.getHeight() / SCALING_FACTOR, true);
            mImage.setImageBitmap(rtdBmp);
            if (mRed.getVisibility() != View.VISIBLE) {
                mRed.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error occurred while loading image", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private class ProcessImageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap bitmap = ((BitmapDrawable) mImage.getDrawable()).getBitmap();
            int pixel;
            for (int x = 0; x < bitmap.getWidth(); x++) {
                for (int y = 0; y < bitmap.getHeight(); y++) {
                    pixel = bitmap.getPixel(x, y);
                    bitmap.setPixel(x, y, Color.argb(
                            Color.alpha(pixel),
                            Color.red(pixel),
                            Color.green(0),
                            Color.blue(0)
                    ));
                }
                if (x % 16 == 0) {
                    mImage.postInvalidate();
                }
            }
            return null;
        }
    }

}
