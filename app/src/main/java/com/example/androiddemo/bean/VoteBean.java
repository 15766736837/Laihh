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
    private long start_time;                //创建的时间
    private String msg_contain_me = "";     //有可参与的投票通知
    private String msg_dying_period = "";   //临期通知
    private String msg_expire = "";         //到期通知
    private long msg_time;
    private long msg_type;

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

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public String getMsg_contain_me() {
        return msg_contain_me;
    }

    public void setMsg_contain_me(String msg_contain_me) {
        this.msg_contain_me = msg_contain_me;
    }

    public String getMsg_dying_period() {
        return msg_dying_period;
    }

    public void setMsg_dying_period(String msg_dying_period) {
        this.msg_dying_period = msg_dying_period;
    }

    public String getMsg_expire() {
        return msg_expire;
    }

    public void setMsg_expire(String msg_expire) {
        this.msg_expire = msg_expire;
    }

    public long getMsg_time() {
        return msg_time;
    }

    public void setMsg_time(long msg_time) {
        this.msg_time = msg_time;
    }

    public long getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(long msg_type) {
        this.msg_type = msg_type;
    }
}
