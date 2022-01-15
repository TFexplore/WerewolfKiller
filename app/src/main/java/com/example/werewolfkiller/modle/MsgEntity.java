package com.example.werewolfkiller.modle;


public class MsgEntity {
            private String code;
            private String msg;
            private User user;
            private String token;

    public MsgEntity(User user, String code, String msg, String token) {
        this.user = user;
        this.code = code;
        this.msg = msg;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
