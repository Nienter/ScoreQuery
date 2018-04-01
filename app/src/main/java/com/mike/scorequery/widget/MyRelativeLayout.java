package com.mike.scorequery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MyRelativeLayout extends RelativeLayout {

    public boolean isIntercept() {
        return isIntercept;
    }

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    private  boolean isIntercept=true;//是否拦截
    public MyRelativeLayout(Context context) {
        super(context);
    }
 
    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    /**
     * 重写这个方法，返回true就行了
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isIntercept;
    }

 
}