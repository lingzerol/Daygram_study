package com.example.zero.daygram;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    public int status;//标志现在处于什么状态,0为初始状态，1为进入RecyclerView状态，2为进入setting状态

    public  DAIRY Dairy;

    public final static String weeks[]={"SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THUESDAY","FRIDAY","SATURDAY"};
    public final static String months[]={"JANUARY",
            "FEBRUARY",
            "MARCH",
            "APRIL",
            "MAY",
            "JUNE",
            "JULY",
            "AUGEST",
            "SEPTEMBER",
            "OCTORBER",
            "NOVEMBER",
            "DECEMBER"};
    private static ArrayList<String> year_list=new ArrayList<String>();
    private static ArrayList<String> full_month_list=new ArrayList<String>();
    private static ArrayList<String> month_list=new ArrayList<String>();
    private boolean last_term=false;
    private boolean refresh_again=false;

    private final static int begin_year=1970;
    public daygram_memory_operator memory_operator;
    public static int present_year,present_month;
    public static int content_year,content_month;

    private ListView dairy_list;
    private Calendar calendar=null;
    public dairyListAdapter adapter,adapter01,adapter02;
    private LinearLayout bottom;
    private RecyclerView month_recycler,year_recycler;
    private LinearLayout optionview;
    private RegularTextView month_option,year_option;
    private LinearLayout plus_option,view_option;
    public static int today;

    private LinearLayout up_top;
    private TextView up_time_week,up_time_md,up_time_hms;
    private float firstY=0,lastY=0;
    private int height=0;
    private float scale;
    private final int HEIGHT=200;
    private dateThread mthread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        scale=this.getResources().getDisplayMetrics().density;
        bottom=(LinearLayout)findViewById(R.id.daygram_bottom);
        adapter01=new dairyListAdapter(MainActivity.this,R.layout.dairy_option_content01,Dairy.dairy_view);
        adapter02=new dairyListAdapter(MainActivity.this,R.layout.dairy_option_content02,Dairy.dairy_view2);
        adapter=adapter01;
        dairy_list=(ListView)findViewById(R.id.dairy_list);
        dairy_list.setAdapter(adapter);
        dairy_list.setOnItemClickListener(new myOnItemClickListener());

        optionview=(LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.daygram_option,null);

        month_recycler=(RecyclerView)LayoutInflater.from(getApplicationContext()).inflate(R.layout.time_recycle,null);
        year_recycler=(RecyclerView)LayoutInflater.from(getApplicationContext()).inflate(R.layout.time_recycle,null);

        up_top=(LinearLayout) findViewById(R.id.daygram_top);
        up_time_week=(TextView) findViewById(R.id.daygram_time_week);
        up_time_md=(TextView) findViewById(R.id.daygram_time_md);
        up_time_hms=(TextView) findViewById(R.id.daygram_time_hms);


        bottom.addView(optionview);
        get_option_view();
        get_recycler_view(0);
        get_recycler_view(1);

        mthread=new dateThread();
        mthread.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                lastY = event.getY();
                float dis= (int) (((lastY - firstY) / 5) * scale + 0.5f);
                Log.d("dis: ",dis+"");
                if(dis<0&&height>0){
                    if(height+dis>=0){
                        height+=dis;
                        Log.d("height and dis: ",height+" "+dis);
                    }else{
                        height=0;
                    }
                }else if(dis>0&&height<HEIGHT&&dairy_list.getFirstVisiblePosition()==0){
                    if(height+dis<=HEIGHT){
                        height+=dis;
                    }else{
                        height=HEIGHT;
                    }
                }
                Log.d("height: ",height+"");
                setHeight((int)height);
                break;
            case MotionEvent.ACTION_UP:
                firstY=event.getY();
                Log.d("ACTION_UP: ",firstY+"");
                break;

        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        switch (status){
            case 1:
                bottom.removeAllViews();
                bottom.addView(optionview);
                get_option_view();
                break;
            case 2:
                break;
            default:
                super.onBackPressed();
        }
        status=0;
    }

    @Override
    protected void onDestroy() {
        save_item();
        super.onDestroy();
    }


    void Init(){
        status=0;
        calendar=Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        memory_operator=new daygram_memory_operator(getApplicationContext());;
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);
        present_month=month;
        present_year=year;
        content_month=month;
        content_year=year;
        int days=calendar.get(Calendar.DATE);
        today=days;
        for(int i=0;i<=month;++i){
            month_list.add(months[i].substring(0,3));
        }
        for(int i=0;i<12;++i){
            full_month_list.add(months[i].substring(0,3));
        }
        for(int i=begin_year;i<=year;++i){
            year_list.add(i+"");
        }
        Dairy=read_item(present_year,present_month,false);

       /* for(int i=begin_day;i<days;++i){
            Dairy.add(getApplicationContext(),new dairy_option("2018",months[10],weeks[week],i+1+""));
            week=(week+1)%7;
        }
        Dairy.set(getApplicationContext(),1,new dairy_option("2018",months[10],"FRIDAY","2","haha"));*/
    }

    private class dairyType{
        private int type,position;
        public dairyType(int type,int position) {
            this.type=type;
            this.position=position;
        }

        public dairyType(int type) {
            this.type = type;
            this.position=-1;
        }

        public int getType() {
            return type;
        }

        public int getPosition() {
            return position;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    private class myOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           goto_edit_page(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0&&resultCode==RESULT_OK){
            Bundle bundle=data.getExtras();
            boolean situation=bundle.getBoolean("situation");
            if(situation) {
                int position = bundle.getInt("position");
                dairy_option content = (dairy_option) bundle.getSerializable("item");
                if (Dairy.get(position).getContent() != content.getContent()) {
                    Dairy.set(getApplicationContext(), position, content);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
    private void goto_edit_page(int position){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,dairyEditActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("position",position);
        bundle.putSerializable("item",Dairy.get(position));
        intent.putExtras(bundle);
        startActivityForResult(intent,0);
    }
    private void get_option_view(){
        month_option=(RegularTextView)findViewById(R.id.daygram_option_month);
        year_option=(RegularTextView)findViewById(R.id.daygram_option_year);
        plus_option=(LinearLayout) findViewById(R.id.daygram_option_plus);
        view_option=(LinearLayout)findViewById(R.id.daygram_option_change);

        month_option.setOnClickListener(new optionOnClickListener(0));
        year_option.setOnClickListener(new optionOnClickListener(1));
        plus_option.setOnClickListener(new optionOnClickListener(2));
        view_option.setOnClickListener(new optionOnClickListener(3));

        month_option.setText(months[content_month]);
        year_option.setText(content_year+"");

        status=0;
    }

    private void get_recycler_view(int type){
        //type=0为月份recycler，=1为年份recycler
        RecyclerView temp=month_recycler;
        if(type==0){
            temp=month_recycler;
        }else {
            temp=year_recycler;
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        temp.setLayoutManager(linearLayoutManager);
        if(type==0) {
            if(content_year!=present_year)
                temp.setAdapter(new TimeAdapter(full_month_list,type,MainActivity.this));
            else
                temp.setAdapter(new TimeAdapter(month_list,type,MainActivity.this));
            month_recycler=temp;
        }else {
            temp.setAdapter(new TimeAdapter(year_list,type,MainActivity.this));
            year_recycler=temp;
        }
    }
    private class optionOnClickListener implements View.OnClickListener {
        private int type;
        public optionOnClickListener(int type) {
            this.type=type;
        }

        @Override
        public void onClick(View v) {
            switch (type){
                case 0:
                    status=1;
                    bottom.removeAllViews();
                    bottom.addView(month_recycler);
                    break;
                case 1:
                    status=1;
                    bottom.removeAllViews();
                    bottom.addView(year_recycler);
                    break;
                case 2:
                    refresh(read_item(present_year,present_month,true),present_year,present_month);
                    goto_edit_page(today-1);
                    break;
                case 3:
                    if(adapter==adapter01)
                        adapter=adapter02;
                    else adapter=adapter01;
                    dairy_list.setAdapter(adapter);
                    break;
            }
        }
    }

    public void save_item(){
        memory_operator.save(Dairy,content_year+"-"+content_month+"");
    }
    private DAIRY read_item(int year,int month,boolean flag){
        try{
            if(flag)
                save_item();
            content_month=year;
            content_month=month;
            return memory_operator.read_item_with_exception(year,month);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void refresh_bottom(){
        bottom.removeAllViews();
        bottom.addView(optionview);
        if(!last_term&&content_year!=present_year){
            last_term=true;
            refresh_again=true;
        }
        if(last_term&&content_year==present_year){
            last_term=false;
            refresh_again=true;
        }
        if(refresh_again){
            get_recycler_view(0);
            refresh_again=false;
        }
        get_option_view();
    }
    public  void refresh(DAIRY t,int y,int m){
        memory_operator.save(Dairy,MainActivity.content_year+"-"+MainActivity.content_month);
        Dairy.change(t);
        adapter.notifyDataSetChanged();
        content_month=m;
        content_year=y;
        refresh_bottom();
    }
    private void setAnimation(float fromX,float toX,float fromY,float toY,long time,View view){
        TranslateAnimation translate=new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,fromX,
                Animation.RELATIVE_TO_SELF,toX,
                Animation.RELATIVE_TO_SELF,fromY,
                Animation.RELATIVE_TO_SELF,toY
        );
        translate.setDuration(time);
        translate.setFillAfter(true);
        view.setAnimation(translate);
    }
    public void setHeight(int height){
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        up_top.setLayoutParams(layoutParams);
        up_top.requestLayout();
    }

    public class dateThread extends Thread {
        @Override
        public void run() {
            do{
                try {
                    Thread.sleep(1000);
                    Message msg=new Message();
                    msg.what=1;
                    mHandler.sendMessage(msg);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }while(true);
        }
        private Handler mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        Calendar calendar=Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                        String str="Today is ";
                        str+=weeks[(calendar.get(Calendar.DAY_OF_WEEK)-1)];
                        up_time_week.setText(str);
                        str=months[calendar.get(Calendar.MONTH)];
                        str+=" "+calendar.get(Calendar.DAY_OF_MONTH);
                        up_time_md.setText(str);
                        String temp;
                        temp=calendar.get(Calendar.HOUR_OF_DAY)+"";
                        if(temp.length()==1)
                            temp="0"+temp+":";
                        else temp+=":";
                        str=temp;
                        temp=calendar.get(Calendar.MINUTE)+"";
                        if(temp.length()==1)
                            temp="0"+temp+":";
                        else temp+=":";
                        str+=temp;
                        temp=calendar.get(Calendar.SECOND)+"";
                        if(temp.length()==1)
                            temp="0"+temp;
                        str+=temp;
                        up_time_hms.setText(str);
                        break;
                    default:
                        break;
                }
            }
        };
    }
}
