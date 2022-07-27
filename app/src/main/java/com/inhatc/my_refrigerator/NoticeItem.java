package com.inhatc.my_refrigerator;

public class NoticeItem {
    private String name;
    private long days;

    NoticeItem(String name, long days){
        this.name = name;
        this.days = days;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long date) {
        this.days = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
