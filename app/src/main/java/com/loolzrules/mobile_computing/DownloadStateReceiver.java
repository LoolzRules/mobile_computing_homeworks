package com.loolzrules.mobile_computing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DownloadStateReceiver extends BroadcastReceiver
{
    ActivityMain main;
    public DownloadStateReceiver(ActivityMain main) {
        this.main = main;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        main.setProgressFromService(intent.getIntExtra(Constants.EXTENDED_DATA_STATUS, 0));
    }
}
