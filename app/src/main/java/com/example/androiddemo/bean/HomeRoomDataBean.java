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
        private String name;
        private String location;
        private int count;

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
