package com.example.androiddemo.bean;

import java.io.Serializable;
import java.util.List;

public class HomeRoomDataBean implements Serializable {
    private List<HomeRoomBean> list;

    public List<HomeRoomBean> getList() {
        return list;
    }

    public void setList(List<HomeRoomBean> list) {
        this.list = list;
    }

    public class HomeRoomBean implements Serializable {
        private int id;
        private int mid;
        private int uid;
        private int rid;
        private String name;
        private String location;
        private String title;
        private String topic;
        private int count;
        private int status;
        private long appointTime;
        private long unAppointTime;
        private long endTime;

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getRid() {
            return rid;
        }

        public void setRid(int rid) {
            this.rid = rid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getAppointTime() {
            return appointTime;
        }

        public void setAppointTime(long appointTime) {
            this.appointTime = appointTime;
        }

        public long getUnAppointTime() {
            return unAppointTime;
        }

        public void setUnAppointTime(long unAppointTime) {
            this.unAppointTime = unAppointTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
