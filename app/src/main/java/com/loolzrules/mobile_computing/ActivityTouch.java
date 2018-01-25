package com.loolzrules.mobile_computing;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class ActivityTouch extends AppCompatActivity implements View.OnTouchListener {

    private ViewDragger mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        mView = findViewById(R.id.dv_main);
        mView.setOnTouchListener(this);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mView.redrawCrosshair(motionEvent.getX(), motionEvent.getY());
        return true;
    }
}
