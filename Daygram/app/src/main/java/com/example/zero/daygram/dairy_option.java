package com.example.zero.daygram;

import java.io.Serializable;

public class dairy_option implements Serializable {
    private static final long serialVersionUID = 2333L;
    private String year,month,week,day,content;
    public boolean flag;

    public dairy_option() {
        this.flag=false;
    }

    public dairy_option(String year, String month, String week, String day) {
        this.year = year;
        this.month = month;
        this.week = week;
        this.day = day;
        this.content="";
        this.flag=false;
    }

    public dairy_option(String year, String month, String week, String day, String content) {
        this.year = year;
        this.month = month;
        this.week = week;
        this.day = day;
        this.content = content;
        this.flag=true;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;flag=true;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

}
