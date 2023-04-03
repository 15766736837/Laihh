package com.example.androiddemo.bean;

import java.io.Serializable;

public class VoteBean implements Serializable {
    private long _id;
    private long create_id;
    private String title;
    private String describe;
    private String vote_url;
    private int type = 1;   //1 = 文本 2 = 图片
    private long end_time;
    private int single = 1;
    private int min = 1;
    private int max = 1;
    private String users = "";

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getCreate_id() {
        return create_id;
    }

    public void setCreate_id(long create_id) {
        this.create_id = create_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getVote_url() {
        return vote_url;
    }

    public void setVote_url(String vote_url) {
        this.vote_url = vote_url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public int getSingle() {
        return single;
    }

    public void setSingle(int single) {
        this.single = single;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}
