package com.ninepmonline.ninepmdriver.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

class TouchDisableView extends ViewGroup {
    private View mContent;
    private boolean mTouchDisabled;

    public TouchDisableView(Context context) {
        this(context, null);
    }

    public TouchDisableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTouchDisabled = false;
    }

    public void setContent(View v) {
        if (this.mContent != null) {
            removeView(this.mContent);
        }
        this.mContent = v;
        addView(this.mContent);
    }

    public View getContent() {
        return this.mContent;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
        this.mContent.measure(getChildMeasureSpec(widthMeasureSpec, 0, width), getChildMeasureSpec(heightMeasureSpec, 0, height));
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mContent.layout(0, 0, r - l, b - t);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.mTouchDisabled;
    }

    void setTouchDisable(boolean disableTouch) {
        this.mTouchDisabled = disableTouch;
    }

    boolean isTouchDisabled() {
        return this.mTouchDisabled;
    }
}
