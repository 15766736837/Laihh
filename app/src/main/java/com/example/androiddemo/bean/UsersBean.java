package com.example.androiddemo.bean;

import java.util.List;

public class UsersBean {
    private List<UserDataBean.UserBean> list;

    public List<UserDataBean.UserBean> getList() {
        return list;
    }

    public void setList(List<UserDataBean.UserBean> list) {
        this.list = list;
    }
}
