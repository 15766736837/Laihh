package com.example.androiddemo.bean;

public class VoteBean {
    private String content;
    private String img;
    private int index;

    public VoteBean(int index) {
        this.index = index;
    }

    public VoteBean() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
