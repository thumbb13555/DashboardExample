package com.jetec.speeddashboardexample;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;

public class DashBoardView extends View {
    private static final String TAG = DashBoardView.class.getSimpleName();
    private int totleHeight, totleWeight;
    private float percentage;
    private String originValue;

    private int mColor;
    private boolean setColor = false;


    public DashBoardView(Context context) {
        super(context);
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        totleHeight = MeasureSpec.getSize(heightMeasureSpec);
        totleWeight = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(totleWeight, totleHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        int bottomColor = Color.parseColor("#C9C9C9");
        paint.setColor(bottomColor);//設置底部顏色
        paint.setStrokeJoin(Paint.Join.ROUND);//設置畫筆畫出的形狀
        paint.setStrokeCap(Paint.Cap.ROUND);//使線的尾端具有圓角
        paint.setStyle(Paint.Style.STROKE);//使圓為空心
        paint.setStrokeWidth(dp2px(40));//設置外圍線的粗度
        RectF mRecF = new RectF(10 + dp2px(20), 10 + dp2px(20), getWidth() - 10 - dp2px(20), getHeight() - 10);
        canvas.drawArc(mRecF, 180, 180, false, paint);


        if (!setColor) {
            mColor = Color.parseColor("#1C98EB");
        }
        paint.setColor(mColor);
        canvas.drawArc(mRecF, 180, percentage, false, paint);//畫進度

        Paint point = new Paint();
        point.setAntiAlias(true);
        point.setColor(Color.parseColor("#096148"));
        double x, y, Rx1, Ry1, Rx2, Ry2;
        float ovalbottom = getHeight() + 4 * 16;
        int xPos = (getWidth() / 2);
        int yPos = (getHeight() / 2);
        float px = 50;
        x = xPos + Math.cos(Math.toRadians((percentage - 180))) * (yPos * 2 / 3);   //180:startAngle 60:sweepAngle
        y = ovalbottom - yPos + Math.sin(Math.toRadians((percentage - 180))) * (yPos * 2 / 3);   //width:(yPos * 2 / 3)
        Rx1 = xPos + Math.cos(Math.toRadians(((percentage - 180) + 90))) * px;
        Rx2 = xPos + Math.cos(Math.toRadians(((percentage - 180) - 90))) * px;
        Ry1 = ovalbottom - yPos + Math.sin(Math.toRadians(((percentage - 180) + 90))) * px;//底邊寬度
        Ry2 = ovalbottom - yPos + Math.sin(Math.toRadians(((percentage - 180) - 90))) * px;//底邊寬度
        canvas.drawCircle(getWidth() / 2, getHeight() / 2 + 64+0.8f , px - 0.5f, point);
        Path path = new Path();
        path.moveTo((int) x, (int) y);
        path.lineTo((int) Rx1, (int) Ry1);
        path.lineTo((int) Rx2, (int) Ry2);
        path.close();
        canvas.drawPath(path, point);
        paint.reset();
        paint.setColor(Color.BLACK);

        paint.setTextSize(80);
        if (originValue == null){
            originValue = "0";
        }
        canvas.drawText("值:"+originValue,getHeight()/2-50,getWidth()/2+200,paint);

    }

    private float dp2px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }


    public void setPercentage(float value) {
        if (value >= 0) {
            DecimalFormat df = new DecimalFormat("#####0.#");
            originValue = df.format(value);
            value = value * 180 / 100;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(percentage, value);
            percentage = value;
            valueAnimator.addUpdateListener((vA -> {
                percentage = (float) vA.getAnimatedValue();
                postInvalidate();
            }));
            valueAnimator.setDuration(500);
            valueAnimator.start();
        }

    }

    public void setPercentage(float mValue, float max, float min) {
        if (max > min) {
            DecimalFormat df = new DecimalFormat("#####0.#");
            float f1;
            if (mValue <= min) {
                f1 = (min - min) / (max - min);
                originValue = df.format(min);
            } else if (mValue >= max) {
                f1 = (max - min) / (max - min);
                originValue = df.format(max);
            } else {
                f1 = (mValue - min) / (max - min);
                originValue = df.format(mValue);
            }
            Log.d(TAG, "setPercentage: " + f1);
            mValue = f1 * 180;

            ValueAnimator vA = ValueAnimator.ofFloat(percentage, mValue);
            percentage = mValue;
            Log.d(TAG, "setPercentage: " + percentage);
            vA.addUpdateListener((v -> {
                percentage = (float) vA.getAnimatedValue();
                postInvalidate();
            }));
            vA.setDuration(500);
            vA.start();
        }

    }

    public void setColor(int color) {
        setColor = true;
        mColor = color;
    }
}
