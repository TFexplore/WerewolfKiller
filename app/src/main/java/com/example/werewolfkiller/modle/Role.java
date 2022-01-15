package com.example.werewolfkiller.modle;

public class Role {
    private Integer img;
    private String ability;
    private String name;
    private String color;
    private int num;

    public Role(Integer img, String ability, String name, String color) {
        this.img = img;
        this.ability = ability;
        this.name = name;
        this.color = color;
    }

    public Integer getImg() {
        return img;
    }

    public void setImg(Integer img) {
        this.img = img;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
