package com.loolzrules.mobile_computing;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

public class MyService extends IntentService {

    int counter;

    public MyService() {
        super("MyService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        counter = 1;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            while (counter<99) {
                Thread.sleep(1000);
                counter = counter % 100 + 1;
                Intent localIntent = new Intent(Constants.BROADCAST_ACTION)
                        .putExtra(Constants.EXTENDED_DATA_STATUS, counter);
                LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}