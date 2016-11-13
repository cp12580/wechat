package com.aaron.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.aaron.wechat.R;

/**
 * Created by Administrator on 2016/10/3.
 */
public class ChangeColorWithText extends View {
    private int mColor = 0xff45c01a;
    private Bitmap mIconBitmap;
    private String mText = "weChat";
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,12,getResources().getDisplayMetrics());

    private Canvas mCanvas;
    private Paint mPaint;
    private Bitmap mBitmap;

    private float mAlpha = 1.0f;

    private Rect mIconRect;
    private Rect mTextBound;
    private Paint mTextPaint;

    public ChangeColorWithText(Context context) {
        this(context,null);
    }
    public ChangeColorWithText(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }
    public ChangeColorWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ChangeColorWithText);
        for (int i = 0; i <array.getIndexCount() ; i++) {
            int index = array.getIndex(i);
            switch (index){
                case R.styleable.ChangeColorWithText_icon:
                    BitmapDrawable drawable = (BitmapDrawable) array.getDrawable(index);
                    mIconBitmap = drawable.getBitmap();
                    break;
                case R.styleable.ChangeColorWithText_color:
                    mColor = array.getColor(index,0xff45c01a);
                    break;
                case R.styleable.ChangeColorWithText_text:
                    mText = array.getString(index);
                    Log.d("Aaron",mText);
                    break;
                case R.styleable.ChangeColorWithText_text_size:
                    mTextSize = (int) array.getDimension(index,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,12,getResources().getDisplayMetrics()));
                    break;

            }
        }
        array.recycle();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff555555);
        mTextPaint.getTextBounds(mText,0,mText.length(),mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth  = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                getMeasuredHeight() - getPaddingBottom() - getPaddingTop() - mTextBound.height());
        int left = (getMeasuredWidth()-iconWidth)/2;
        int top =( getMeasuredHeight()-iconWidth - mTextBound.height())/2;
        mIconRect = new Rect(left,top,left + iconWidth,top + iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mIconBitmap,null,mIconRect,null);
        int alpha = (int) Math.ceil(255 * mAlpha);
        //在内存中绘制bitmap
        setTargetBitmap(alpha);
        drawSourceText(canvas,alpha);
        drawTargetText(canvas,alpha);
        canvas.drawBitmap(mBitmap,0,0,null);
    }

    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        int x = (getMeasuredWidth() - mTextBound.width())/2;
        int y = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText,x,y,mTextPaint);
    }

    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(0xff3333);
        mTextPaint.setAlpha(255 - alpha);
        int x = (getMeasuredWidth() - mTextBound.width())/2;
        int y = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText,x,y,mTextPaint);
    }

    private void setTargetBitmap(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(alpha);
        mPaint.setDither(true);

        mCanvas.drawRect(mIconRect,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap,null,mIconRect,mPaint);

    }

    public void setAlpha(float alpha){
        this.mAlpha = alpha;
        invalidateView();
    }

    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()){
            invalidate();
        }else {
            postInvalidate();
        }
    }

}
