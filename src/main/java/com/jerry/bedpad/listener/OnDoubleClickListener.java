package com.jerry.bedpad.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * 双击监听
 */
public abstract class OnDoubleClickListener implements View.OnTouchListener {

    private long first;
    private long second;
    private int count;

    private static final long DEFAULT_TIME = 800;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            count++;
            if (count == 1) {
                first = System.currentTimeMillis();

            } else if (count == 2) {
                second = System.currentTimeMillis();
                if (second - first < DEFAULT_TIME) {
                    //双击事件
                    onDoubleClick();
                }
                count = 0;
                first = 0;
                second = 0;
            }
        }
        return true;
    }

    protected abstract void onDoubleClick();


}   