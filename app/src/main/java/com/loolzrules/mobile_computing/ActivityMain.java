package com.loolzrules.mobile_computing;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ActivityMain extends AppCompatActivity {

    TextView serviceProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
            }
        }

        serviceProgress = findViewById(R.id.tv_service_progress);

        DownloadStateReceiver mDownloadStateReceiver = new DownloadStateReceiver(this);
        IntentFilter statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mDownloadStateReceiver, statusIntentFilter);

    }

    public void launchTouch(View button) {
        startActivity(new Intent(this, ActivityTouch.class));
    }

    public void launchChuck(View button) {
        startActivity(new Intent(this, ActivityChuckNorris.class));
    }

    public void launchLeveler(View button) {
        startActivity(new Intent(this, ActivityLeveler.class));
    }

    public void launchCamera(View button) {
        startActivity(new Intent(this, ActivityPhoto.class));
    }

    public void launchService(View button) {
        startService(new Intent(this, MyService.class));
    }

    public void setProgressFromService(int progress) {
        serviceProgress.setText(String.valueOf(progress));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
