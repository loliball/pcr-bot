/**
 * Copyright 2021 json.cn
 */
package com.cnl.mybot.pcr.team.entity.user;

import java.util.List;

/**
 * Auto-generated: 2021-04-14 16:49:57
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Data2 {

    private String name;
    private float number;
    private long damage;
    private long score;
    private List<Damage_list> damage_list;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    public float getNumber() {
        return number;
    }

    public void setDamage(long damage) {
        this.damage = damage;
    }

    public long getDamage() {
        return damage;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getScore() {
        return score;
    }

    public void setDamage_list(List<Damage_list> damage_list) {
        this.damage_list = damage_list;
    }

    public List<Damage_list> getDamage_list() {
        return damage_list;
    }

}