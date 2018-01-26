package com.loolzrules.mobile_computing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityPhoto extends AppCompatActivity {

    private String mCurrentPhotoPath;
    private ImageView mImage;
    private TextView mText;
    private Button mRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mImage = findViewById(R.id.iv_photo);
        mText = findViewById(R.id.tv_process);
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

    public void makeAPhoto(View button) {
        File externalFilesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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
        if (requestCode == 2) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(mCurrentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

                Matrix matrix = new Matrix();
                matrix.postRotate(90);

                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                mImage.setImageBitmap(rotatedBitmap);

                mRed.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                Toast.makeText(this, "Error occurred while loading image", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private class ProcessImageTask extends AsyncTask<Bitmap, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            mText.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
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
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mText.setVisibility(View.GONE);
            mImage.setImageBitmap(bitmap);
        }
    }

}
