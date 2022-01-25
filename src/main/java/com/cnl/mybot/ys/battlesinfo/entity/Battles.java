/**
 * Copyright 2021 json.cn
 */
package com.cnl.mybot.ys.battlesinfo.entity;

import java.util.List;

/**
 * Auto-generated: 2021-04-12 16:26:45
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Battles {

    private int index;
    private String timestamp;
    private List<Avatars> avatars;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setAvatars(List<Avatars> avatars) {
        this.avatars = avatars;
    }

    public List<Avatars> getAvatars() {
        return avatars;
    }

}