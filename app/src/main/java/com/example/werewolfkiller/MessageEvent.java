package com.example.werewolfkiller;

public class MessageEvent {
    Integer code;
    String res;

    public MessageEvent(Integer code, String res) {
        this.code = code;
        this.res = res;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }
}
