package com.example.androiddemo.bean;

import java.io.Serializable;
import java.util.List;

public class CommentDataBean implements Serializable {
    private List<CommentBean> list;

    public List<CommentBean> getList() {
        return list;
    }

    public void setList(List<CommentBean> list) {
        this.list = list;
    }

    public class CommentBean implements Serializable {
        private String content;
        private String username;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
