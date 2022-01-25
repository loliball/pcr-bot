package com.cnl.mybot.ys.signin.entity;

public class Reward {
    private String name;
    private String icon;
    private Integer cnt;

    public String getName() {
        return name;
    }

    public Reward setName(String name) {
        this.name = name;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public Reward setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public Integer getCnt() {
        return cnt;
    }

    public Reward setCnt(Integer cnt) {
        this.cnt = cnt;
        return this;
    }
}
