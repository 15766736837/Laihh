package com.example.androiddemo.bean;

public class UserDataBean {

    /**
     * user : {"id":3,"username":"szt1","password":"szt1","nickname":"szt1","sex":"男","phone":"1300","job":"207201751"}
     */
    private UserBean user;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public class UserBean {
        /**
         * id : 3
         * username : szt1
         * password : szt1
         * nickname : szt1
         * sex : 男
         * phone : 1300
         * job : 207201751
         */

        private int id;
        private String username;
        private String password;
        private String nickname;
        private String sex;
        private String phone;
        private String job;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }
    }
}
