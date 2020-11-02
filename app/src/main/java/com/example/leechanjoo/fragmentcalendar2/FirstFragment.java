package com.example.leechanjoo.fragmentcalendar2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.leechanjoo.fragmentcalendar2.OnSwipeTouchListener;

/**
 * Created by LEECHANJOO on 2018-04-15.
 */

public class FirstFragment extends Fragment
{

    public FirstFragment()
    {
    }

    /**
     * 연/월 텍스트뷰
     */
    private TextView tvDate;
    /**
     * 그리드뷰 어댑터
     */
    private GridAdapter gridAdapter;

    /**
     * 일 저장 할 리스트
     */
    private ArrayList<String> dayList;

    /**
     * 그리드뷰
     */
    private GridView gridView;

    /**
     * 캘린더 변수
     */
    private Calendar mCal;

    int LRCount = 0;

    String dateString;
    String tempTextString;

    int gridViewWidth;
    int gridViewHeight;
    int gridViewTop;

    private TextView tempText;



    private DataInterface dataInterface;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       // LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_first, container, false);
        View view = inflater.inflate(R.layout.fragment_first,
                container, false);

        tvDate = (TextView)view.findViewById(R.id.tv_date);
        gridView = (GridView)view.findViewById(R.id.gridview);
        Button leftBtn = (Button)view.findViewById(R.id.leftBtn);
        Button rightBtn = (Button)view.findViewById(R.id.rightBtn);
        tempText = (TextView)view.findViewById(R.id.dateViewer);

        // 오늘에 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);
        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("YYYY", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("DD", Locale.KOREA);

        dayList = new ArrayList<String>();


        mCal = Calendar.getInstance();

        // 현재 날짜를 텍스트뷰에 표시
        dateString = mCal.get(Calendar.YEAR)+"년 "+(mCal.get(Calendar.MONTH) +1)+"월";
        tvDate.setText(dateString);



        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), 1);
        int firstDayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < firstDayNum; i++) {
            dayList.add("");
        }

        setCalendarDate(mCal.get(Calendar.MONTH));

        // 밑쪽 공백 채우기 위해 마지막날로 설정 -> 마지막 주 구함
        mCal.set(mCal.get(Calendar.YEAR),mCal.get(Calendar.MONTH),mCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastDayNum =  (mCal.get(Calendar.WEEK_OF_MONTH)) * 7 - dayList.size();
        for (int i = 0 ;i < lastDayNum ; i++) {
            dayList.add("");
        }


        //임시 TextView
        tempTextString = lastDayNum+"임";

        tempText.setText(tempTextString);

        gridAdapter = new GridAdapter(getActivity().getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthSwitch(-1);
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthSwitch(1);
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getActivity().getApplicationContext(), "position: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        gridView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {

            public void onSwipeRight() {
                monthSwitch(-1);
            }
            public void onSwipeLeft() {
                monthSwitch(1);
            }

        });



//
        tempTextString = gridViewWidth+"W, "+gridViewHeight+"H ";
        tempText.setText(tempTextString);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //뷰의 생성된 후 크기와 위치 구하기
                        gridViewWidth = gridView.getWidth();
                        gridViewHeight = gridView.getHeight();
                        gridViewTop = gridView.getTop();

                        dataInterface = (DataInterface) getActivity();
                        dataInterface.getCalendarHeight(getGridViewHeight());
                        dataInterface.getCalendarTop(getGridViewTop());
                        dataInterface.getCalendarBottom(gridView.getBottom());

                        //
                        tempTextString = getGridViewWidth()+"W, "+getGridViewHeight()+"H "+gridView.getTop()+"top"+gridView.getBottom()+"bottom";
                        tempText.setText(tempTextString);

                        //리스너 해제
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }


    public int getGridViewHeight() {
        return gridViewHeight;
    }

    public int getGridViewWidth() {
        return gridViewWidth;
    }

    public int getGridViewTop() {
        return gridViewTop;
    }

    public void monthSwitch(int flag) {
        // 목록 초기화
        dayList.clear();

        // 이전달? 다음달? -> 조정
        LRCount += flag;
        mCal.add(mCal.MONTH,LRCount);

        // 앞쪽공백 추가
        mCal.set(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), 1);
        int firstDayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < firstDayNum; i++) {
            dayList.add("");
        }

        // 날짜 숫자 추가
        setCalendarDate(mCal.get(Calendar.MONTH));

        // 밑쪽 공백 채우기 위해 마지막날로 설정 -> 마지막 주 구함
        mCal.set(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH), mCal.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastDayNum =  (mCal.get(Calendar.WEEK_OF_MONTH)) * 7 - dayList.size();
        for (int i = 0 ;i < lastDayNum ; i++) {
            dayList.add("");
        }

        //
        tempTextString = gridViewWidth+"W, "+gridViewHeight+"H ";
        tempText.setText(tempTextString);

        //화면 재구성 요청
        gridAdapter.notifyDataSetChanged();

        //상단바 밑 날짜 수정
        dateString = mCal.get(Calendar.YEAR)+"년 "+(mCal.get(Calendar.MONTH) +1)+"월";
        tvDate.setText(dateString);

    }
    /**
     * 해당 월에 표시할 일 수 구함
     *
     * @param month
     */
    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month);

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }

    }

    /**
     * 그리드뷰 어댑터
     *
     */
    private class GridAdapter extends BaseAdapter {

        private final List<String> list;

        private final LayoutInflater inflater;

        /**
         * 생성자
         *
         * @param context
         * @param list
         */
        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.tv_item_gridview);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvItemGridView.setText("" + getItem(position));


            //해당 날짜 텍스트 컬러,배경 변경
            mCal = Calendar.getInstance();

            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);

            holder.tvItemGridView.setTextColor(getResources().getColor(R.color.color_000000));
            if (sToday.equals(getItem(position)) && LRCount == 0) { //오늘 day 텍스트 컬러 변경
                holder.tvItemGridView.setTextColor(getResources().getColor(R.color.color_21a4ff));
            }



            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvItemGridView;
    }

    public interface DataInterface {
        void getCalendarHeight(int height);
        void getCalendarTop(int top);
        void getCalendarBottom(int bottom);
    }
}

