package com.loolzrules.mobile_computing;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ViewLeveler extends View {

    private float x;
    private float y;
    private int center_x;
    private int center_y;
    private int size_x;
    private int size_y;
    private Paint paint_indicator;
    private Paint paint_ruler;
    private final int max_level = 16;
    private final float alpha = 0.3f;

    public ViewLeveler(Context context) {
        super(context);
        init();
    }

    public ViewLeveler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewLeveler(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // status bar height
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        // action bar height
        int actionBarHeight = 0;
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize }
        );
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        size_x = this.getResources().getDisplayMetrics().widthPixels;
        size_y = this.getResources().getDisplayMetrics().heightPixels - statusBarHeight - actionBarHeight;
        center_x = size_x/2;
        center_y = size_y/2;
        x = center_x;
        y = center_y;

        paint_indicator = new Paint();
        paint_indicator.setStyle(Paint.Style.FILL);
        paint_indicator.setColor(Color.BLUE);
        paint_ruler = new Paint();
        paint_ruler.setStyle(Paint.Style.FILL);
        paint_ruler.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // MAIN AXES
        canvas.drawLine(center_x, 0, center_x, center_y * 2, paint_ruler);
        canvas.drawLine(0, center_y, center_x * 2, center_y, paint_ruler);

        // HITCHES
        int coeff_x = center_x / max_level;
        int coeff_y = center_y / max_level;
        for (int i = 1; i < max_level; i++) {
            canvas.drawLine(center_x + 25, center_y - i * coeff_y, center_x - 25, center_y - i * coeff_y, paint_ruler);
            canvas.drawLine(center_x + 25, center_y + i * coeff_y, center_x - 25, center_y + i * coeff_y, paint_ruler);
            canvas.drawLine(center_x - i * coeff_x, center_y - 25, center_x - i * coeff_x, center_y + 25, paint_ruler);
            canvas.drawLine(center_x + i * coeff_x, center_y - 25, center_x + i * coeff_x, center_y + 25, paint_ruler);
        }

        // INDICATOR
        canvas.drawLine(this.x, 0, this.x, size_y, paint_ruler);
        canvas.drawLine(0, this.y, size_x, this.y, paint_ruler);
        canvas.drawCircle(this.x, this.y, 9.5f, paint_indicator);
    }

    public void reset(float x, float y) {
        this.x = (1 - alpha) * this.x + alpha * (center_x + center_x * x / max_level);
        this.y = (1 - alpha) * this.y + alpha * (center_y - center_y * y / max_level);
        invalidate();
    }
}
