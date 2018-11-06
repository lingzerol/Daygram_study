package com.example.zero.daygram;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.TimeZone;

public class dairyEditActivity extends AppCompatActivity {
    private LinearLayout bottomlayout;
    View turn_back,done,root,dairy_edit,dairy_show;
    private EditText edit;
    private TextView date,show;
    private Button done_btn,back_btn;
    private LinearLayout time_btn;
    private dairy_option content;
    private int position;
    private ScrollView scrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_dairy);
        root=getWindow().getDecorView();
        Bundle bundle=this.getIntent().getExtras();
        content=(dairy_option)bundle.getSerializable("item");
        position=bundle.getInt("position");

        turn_back=LayoutInflater.from(getApplicationContext()).inflate(R.layout.edit_turn_back,null);
        done=LayoutInflater.from(getApplicationContext()).inflate(R.layout.edit_done,null);
        dairy_edit=LayoutInflater.from(getApplicationContext()).inflate(R.layout.edit_dairy_edit,null);
        dairy_show=LayoutInflater.from(getApplicationContext()).inflate(R.layout.edit_dairy_show,null);

        bottomlayout=(LinearLayout)findViewById(R.id.dairy_edit_bottom);

        scrollView=(ScrollView)findViewById(R.id.dairy_edit_scrollview);
        scrollView.addView(dairy_show);

        show=(TextView)findViewById(R.id.dairy_show_content);
        show.setOnClickListener(new showOnClickListener());
        show.setText(content.getContent());

        date=(TextView)findViewById(R.id.dairy_edit_date);
        bottomlayout.addView(turn_back);
        back_btn=(Button)findViewById(R.id.dairy_btn_turn_back);
        back_btn.setOnClickListener(new buttonOnClickListener(2));

        String d;
        if(content.getWeek().equals("SUNDAY"))
            d="<font color='#ff0000'>"+content.getWeek()+"</font>/"+content.getMonth().substring(0,3)+" "+content.getDay()+"/"+content.getYear();
        else d=content.getWeek()+"/"+content.getMonth().substring(0,3)+" "+content.getDay()+"/"+content.getYear();
        date.setText(Html.fromHtml(d,Html.FROM_HTML_MODE_LEGACY));
        root.addOnLayoutChangeListener(new mOnLayoutChangeListener());
        //bottomlayout.getViewTreeObserver().addOnGlobalLayoutListener(new mOnGlobalLayoutListener());
    }


    private class mOnLayoutChangeListener implements View.OnLayoutChangeListener {
        boolean flag=true;
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            int heightDiff =bottomlayout.getTop();
            if(heightDiff<1000 ){
                if(!flag)
                    return;
                bottomlayout.removeAllViews();
                bottomlayout.addView(done);
                flag=false;
                time_btn=(LinearLayout) findViewById(R.id.dairy_btn_time);
                done_btn=(Button)findViewById(R.id.dairy_btn_done);
                time_btn.setOnClickListener(new buttonOnClickListener(0));
                done_btn.setOnClickListener(new buttonOnClickListener(1));
            }else{
                if(flag)
                    return;
                bottomlayout.removeAllViews();
                bottomlayout.addView(turn_back);
                flag=true;
                back_btn=(Button)findViewById(R.id.dairy_btn_turn_back);
                back_btn.setOnClickListener(new buttonOnClickListener(2));

                String temp=edit.getText().toString();
                scrollView.removeAllViews();
                scrollView.addView(dairy_show);
                show=(TextView) findViewById(R.id.dairy_show_content);
                show.setText(temp);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putBoolean("situation",false);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }

    private class buttonOnClickListener implements View.OnClickListener {
       private final int type;
       public buttonOnClickListener(int type) {
            this.type=type;
        }

        @Override
        public void onClick(View v) {
            if (type == 0) {
                int p=edit.getSelectionStart();
                StringBuffer temp_content=new StringBuffer(edit.getText().toString());
                Calendar calendar=Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                int hour,minute;
                String present_time;
                hour=calendar.get(Calendar.HOUR_OF_DAY);
                minute=calendar.get(Calendar.MINUTE);
                if(hour>12){
                    present_time=" "+(hour-12)+":"+minute+" pm";
                }else {
                    present_time=" "+hour+":"+minute+" am";
                }
                temp_content.insert(p,present_time);
                edit.setText(temp_content);
                edit.setSelection(p+present_time.length());
            }else if(type==1){
                save(0);
                dairyEditActivity.this.finish();
            }else if(type==2){
                save(1);
                dairyEditActivity.this.finish();
            }
        }
        private void save(int type){
            Intent intent=new Intent();
            Bundle bundle=new Bundle();
            String temp;
            if(type==0){
                temp=edit.getText().toString();
            }else{
                temp=show.getText().toString();
            }
           if(!content.getContent().equals(temp)){
               content.setContent(temp);
               bundle.putSerializable("item",content);
               bundle.putInt("position",position);
               bundle.putBoolean("situation",true);
           }else {
               bundle.putBoolean("situation",false);
           }
            intent.putExtras(bundle);
            setResult(RESULT_OK,intent);
        }
    }

    private class showOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String temp=show.getText().toString();
            scrollView.removeAllViews();
            scrollView.addView(dairy_edit);
            edit=(EditText)findViewById(R.id.dairy_edit_content);
            edit.setText(temp);
            showSoftKeyboard(edit);
        }
    }
    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
