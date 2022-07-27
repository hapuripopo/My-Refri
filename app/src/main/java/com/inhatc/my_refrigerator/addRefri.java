package com.inhatc.my_refrigerator;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class addRefri {

    @Exclude
    String key;

    String name; //식재료명
    String category; //카테고리
    String storage; //보관방법
    String memo; //메모

    long buyDate; //구매일
    long lastDate; //유통기한
//    Date buyDate; //구매일
//    Date expiDate; //유통기한

    public addRefri() {
    } // 생성자메서드

    @Exclude
    public String getKey() {
        return key;
    }

    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(long buyDate) {
        this.buyDate = buyDate;
    }

    public long getLastDate() {
        return lastDate;
    }

    public void setLastDate(long lastDate) {
        this.lastDate = lastDate;
    }

    public addRefri(String name, String category, String storage, long buyDate, long lastDate, String memo) {
        this.name = name;
        this.category = category;
        this.storage = storage;
        this.buyDate = buyDate;
        this.lastDate = lastDate;
        this.memo = memo;
    }
}
