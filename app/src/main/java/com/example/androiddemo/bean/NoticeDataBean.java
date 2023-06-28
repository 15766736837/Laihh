package com.example.androiddemo.bean;

import java.io.Serializable;
import java.util.List;

public class NoticeDataBean implements Serializable {
    private List<NoticeBean> list;

    public List<NoticeBean> getList() {
        return list;
    }

    public void setList(List<NoticeBean> list) {
        this.list = list;
    }

    public class NoticeBean implements Serializable {
        private int id;
        private String title;
        private String content;
        private int uid;
        private String username;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
