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
public class Floors {

    private int index;
    private String icon;
    private boolean is_unlock;
    private String settle_time;
    private int star;
    private int max_star;
    private List<Levels> levels;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIs_unlock(boolean is_unlock) {
        this.is_unlock = is_unlock;
    }

    public boolean getIs_unlock() {
        return is_unlock;
    }

    public void setSettle_time(String settle_time) {
        this.settle_time = settle_time;
    }

    public String getSettle_time() {
        return settle_time;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getStar() {
        return star;
    }

    public void setMax_star(int max_star) {
        this.max_star = max_star;
    }

    public int getMax_star() {
        return max_star;
    }

    public void setLevels(List<Levels> levels) {
        this.levels = levels;
    }

    public List<Levels> getLevels() {
        return levels;
    }

}