package com.example.zero.daygram;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class daygram_memory_operator {
    private Context context;

    public daygram_memory_operator(Context context) {
        this.context = context;
    }

    public void save(DAIRY Dairy,String title){
        try{
            SharedPreferences.Editor editor=context.getSharedPreferences(title,Context.MODE_PRIVATE).edit();
            String content=serializable(Dairy);
            Log.d("save String: ",content);
            editor.putString("content",content);
            editor.apply();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public DAIRY read(String title){
        try{
            SharedPreferences pref=context.getSharedPreferences(title,Context.MODE_PRIVATE);
            String content=pref.getString("content",null);
            if(content==null){
                return null;
            }
            return de_serializabale(content);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private String serializable(DAIRY item) throws IOException
    {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(baos);
        oos.writeObject(item.dairy);
        String result=baos.toString("ISO-8859-1");
        result=java.net.URLEncoder.encode(result,"utf-8");
        oos.close();
        baos.close();
        return result;
    }
    private DAIRY de_serializabale(String item)throws IOException,ClassNotFoundException{
        item=java.net.URLDecoder.decode(item,"utf-8");
        ByteArrayInputStream bais=new ByteArrayInputStream(item.getBytes("ISO_8859-1"));
        ObjectInputStream ois=new ObjectInputStream(bais);
        DairyArrayList<dairy_option> temp=(DairyArrayList<dairy_option>) ois.readObject();
        ois.close();
        bais.close();
        return new DAIRY(context,temp);
    }
    public DAIRY read_item_with_exception(int year,int month)throws ParseException {
        DAIRY temp=read(year+"-"+month+"");
        if(temp==null){
            temp=new DAIRY();
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();
        cal.setTime(sdf.parse(year+"-"+(month+1)+"-1"));
        int week=cal.get(Calendar.DAY_OF_WEEK)-1;
        int day=temp.get_count();
        int days=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(week<0)
            week=0;
        if(month==MainActivity.present_month&&year==MainActivity.present_year){
            days=MainActivity.today;
        }

        if(day<days){

            for(int i=day+1;i<=days;++i){
                dairy_option d=new dairy_option(year+"",MainActivity.months[month],MainActivity.weeks[week],i+"");
                temp.add(context,d);
                week=(week+1)%7;
            }
        }
        return temp;
    }
}
