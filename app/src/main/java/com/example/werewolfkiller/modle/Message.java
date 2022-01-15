package com.example.werewolfkiller.modle;

public class Message {
    private String name;
    private String id;
    private String msg;

    public Message(String name, String id, String msg) {
        this.name = name;
        this.id = id;
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
