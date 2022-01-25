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
public class Levels {

    private int index;
    private int star;
    private int max_star;
    private List<Battles> battles;

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
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

    public void setBattles(List<Battles> battles) {
        this.battles = battles;
    }

    public List<Battles> getBattles() {
        return battles;
    }

}