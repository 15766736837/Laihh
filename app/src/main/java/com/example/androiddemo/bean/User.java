package com.example.androiddemo.bean;

public class User {
    private long _id;
    private String user_name;
    private String password;
    private int is_login;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIs_login() {
        return is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }
}
