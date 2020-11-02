package com.example.leechanjoo.fragmentcalendar2;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by LEECHANJOO on 2018-04-18.
 */
public class CustomViewPager extends ViewPager {
    private boolean swipeEnabled;
    private boolean nowCalendar;

    int calendarHeight;
    int calendarTop;
    int calendarBottom;

    public CustomViewPager(Context context) {
        super(context);
        swipeEnabled = true;
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        swipeEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.swipeEnabled){
            return super.onTouchEvent(ev);
        }
        return false;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.swipeEnabled) {
            final int action = event.getAction() & MotionEventCompat.ACTION_MASK;

            switch(action){
                case MotionEvent.ACTION_DOWN:
                    // 안쪽이면, false 넘겨준다 => 달력의 움직임
                    if( calendarTop < (int) event.getY() && calendarBottom > (int)event.getY() && nowCalendar ){
                        return false;
                    }
            }

            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setCalendarHeight(int height) {
        this.calendarHeight = height;
    }

    public void setCalendarTop(int top) {
        this.calendarTop = top;
    }

    public void setCalendarBottom(int bottom) {
        this.calendarBottom = bottom;
    }

    public void isCalendar(boolean enabled) {
        this.nowCalendar = enabled;
    }
    public void setSwipeEnabled(boolean enabled) {
        this.swipeEnabled = enabled;
    }
}
