package com.example.zero.daygram;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public  class DAIRY {
    private static final long serialVersionUID =346631263871623612L;
    public   DairyArrayList<dairy_option> dairy=new DairyArrayList<dairy_option>();
    public  DairyArrayList<View> dairy_view=new DairyArrayList<View>();
    public  DairyArrayList<View> dairy_view2=new DairyArrayList<View>();
    public DAIRY(Context context,DairyArrayList<dairy_option> dairy) {
        this.dairy = dairy;
        this.dairy_view=new DairyArrayList<View>();
        this.dairy_view2=new DairyArrayList<View>();
        for(int i=0;i<dairy.size();++i){
            this.dairy_view.add(create_view(context,dairy.get(i),0));
            if(dairy.get(i).getContent().length()>0)
                this.dairy_view2.add(create_view(context,dairy.get(i),1));
        }
    }

    public DAIRY(DairyArrayList<dairy_option> dairy, DairyArrayList<View> dairy_view,DairyArrayList<View> dairy_view2) {
        this.dairy = dairy;
        this.dairy_view = dairy_view;
        this.dairy_view2 = dairy_view2;
    }

    public DAIRY() {
        this.dairy = new DairyArrayList<dairy_option>();
        this.dairy_view =new DairyArrayList<View>();
        this.dairy_view2 =new DairyArrayList<View>();
    }
    public void change(DAIRY item){
        if(item==null)
            return;
        dairy=item.dairy;
        dairy_view.clear();
        dairy_view2.clear();
        for(int i=0;i<item.get_count();++i){
            dairy_view.add(item.get_view(i));
        }
        for(int i=0;i<item.get_view2_count();++i){
            dairy_view2.add(item.get_view2(i));
        }
    }
    public  void add(Context context, dairy_option item){
        dairy.add(item);
        dairy_view.add(create_view(context,item,0));
        if(item.getContent().length()>0)
            dairy_view2.add(create_view(context,item,1));
    }
    public  dairy_option get(int position){
        return (dairy_option) dairy.get(position);
    }
    public View get_view(int position){
        return dairy_view.get(position);
    }
    public View get_view2(int position){
        return dairy_view2.get(position);
    }
    public   void set(Context context,int position,dairy_option item){
        dairy.set(position,item);
        dairy_view.set(position,create_view(context,item,0));
        dairy_view2.clear();
        for(int i=0;i<dairy.size();++i){
            dairy_option temp=dairy.get(i);
            if(temp.getContent().length()>0){
                dairy_view2.add(create_view(context,temp,1));
            }
        }
    }
    public  View create_view(Context context,dairy_option item,int type){
        String week = item.getWeek(), day = item.getDay();
        if(type==0) {
            View view;
            if (item.flag == false) {
                view = LayoutInflater.from(context).inflate(R.layout.dairy_option_circle, null);
                if (week.equals("SUNDAY")) {
                    LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.option_circle);
                    linearLayout.setBackgroundResource(R.drawable.red_circle);
                }
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.dairy_option_content01, null);
                TextView option_week = (TextView) view.findViewById(R.id.dairy_option_week);
                TextView option_day = (TextView) view.findViewById(R.id.dairy_option_day);
                TextView option_content = (TextView) view.findViewById(R.id.dairy_option_content);
                option_week.setText(item.getWeek().substring(0, 3));
                option_day.setText(item.getDay());
                String temp;
                if (item.getContent().length() > 70) {
                    temp = item.getContent().substring(0, 70);
                    temp += "...";
                } else temp = item.getContent();
                option_content.setText(temp);
                if (week.equals("SUNDAY")) {
                    option_day.setTextColor(ContextCompat.getColor(context,android.R.color.holo_red_light));
                }
            }
            return view;
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.dairy_option_content02, null);
            TextView content02=(TextView)view.findViewById(R.id.dairy_option_content02);
            String str=null;
            if(week.equals("SUNDAY"))
                str="<b><tt><font color='#ff0000'>"+item.getDay()+" "+item.getWeek()+"/ </font></tt></b>"+item.getContent();
            else  str="<b><tt>"+item.getDay()+" "+item.getWeek()+"/ </tt></b>"+item.getContent();
            content02.setText(Html.fromHtml(str,Html.FROM_HTML_MODE_LEGACY));
            return view;
        }
    }
    public int get_count(){return dairy.size();}
    public int get_view2_count(){return dairy_view2.size();}
}
