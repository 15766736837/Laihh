package com.example.androiddemo.bean;

import java.io.Serializable;

public class RoomBean implements Serializable {
    private long id;
    private String room_name;   //自习室名字
    private String region;      //区域
    private String seat;        //座位
    private String describe;    //描述
    private int status;         //状态 0=未被约满 1=被约满了

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
