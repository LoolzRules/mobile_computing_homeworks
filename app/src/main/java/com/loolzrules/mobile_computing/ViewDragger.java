package com.loolzrules.mobile_computing;

import android.content.Context;
import org.jetbrains.annotations.Nullable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;


public class ViewDragger extends View {

    private float x;
    private float y;
    private Bitmap crosshair;

    public ViewDragger(Context context) {
        super(context);
        initialize(context);
    }

    public ViewDragger(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ViewDragger(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }


    private void initialize(Context context) {
        crosshair = BitmapFactory.decodeResource(context.getResources(), R.drawable.crosshair);
        this.x = 12;
        this.y = 12;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(crosshair, x-12, y-12, null);
    }

    public void redrawCrosshair(float x, float y) {
        this.x = x;
        this.y = y;
        invalidate();
    }

}

