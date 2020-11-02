package com.example.leechanjoo.fragmentcalendar2;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// 첫화면 & 달력이 늘어났을때, 높이 계산이 안된다 -> 갱신과 연관이 있을 듯.
// rl

public class FragmentCalendarActivity2 extends AppCompatActivity implements FirstFragment.DataInterface
{
    CustomViewPager vp;
    //private TextView tvDate;

    int calendarHeight;
    int calendarTop;
    int calendarBottom;

    FirstFragment firstFragment;

    TextView testText;
    String testString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_calendar2);

        //tvDate = (TextView)findViewById(R.id.tv_date);
        vp = (CustomViewPager)findViewById(R.id.vp);
        Button btn_first = (Button)findViewById(R.id.btn_first);
        Button btn_second = (Button)findViewById(R.id.btn_second);
        Button btn_third = (Button)findViewById(R.id.btn_third);

        // firstFragment = (FirstFragment)getSupportFragmentManager().findFragmentById(R.id.firstFragment);

        testText = (TextView)findViewById(R.id.testText);

        testString = calendarTop+"top"+calendarBottom+"bottom";
        testText.setText(testString);



        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        btn_first.setOnClickListener(movePageListener);
        btn_first.setTag(0);
        btn_second.setOnClickListener(movePageListener);
        btn_second.setTag(1);
        btn_third.setOnClickListener(movePageListener);
        btn_third.setTag(2);


        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vp.setCalendarHeight(calendarHeight);
                    vp.setCalendarTop(calendarTop);
                    vp.setCalendarBottom(calendarBottom);

                    testString = calendarTop+"top"+calendarBottom+"bottom";
                    testText.setText(testString);

                    String text = "You click at x = " + event.getX() + " and y = " + event.getY();
                    Toast.makeText(FragmentCalendarActivity2.this, text, Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //아이템이 선택이 되었으면
            @Override public void onPageSelected(int position) {
                if (position == 0) {
                    vp.isCalendar(true);
                }
                else {
                    vp.isCalendar(false);
                }
            }
            @Override public void onPageScrolled(int position, float positionOffest, int positionOffsetPixels) {}
            @Override public void onPageScrollStateChanged(int state) {}
        });

    }

    public void onResume() {
        super.onResume();

    }

    View.OnClickListener movePageListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
        }

    };

    @Override
    public void getCalendarHeight(int height) {
        calendarHeight = height;
    }

    @Override
    public void getCalendarTop(int top) {
        calendarTop = top;
    }

    @Override
    public void getCalendarBottom(int bottom) {
        calendarBottom = bottom;
    }


    private class pagerAdapter extends FragmentStatePagerAdapter
    {
        public pagerAdapter(android.support.v4.app.FragmentManager fm)
        {
            super(fm);
        }
        public void onStarted() {
            vp.setCalendarHeight(calendarHeight);
            vp.setCalendarTop(calendarTop);
            vp.setCalendarBottom(calendarBottom);
        }
        @Override
        public android.support.v4.app.Fragment getItem(int position)
        {
            switch(position)
            {
                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            return 3;
        }
    }


}
