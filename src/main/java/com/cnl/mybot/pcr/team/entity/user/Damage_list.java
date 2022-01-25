/**
 * Copyright 2021 json.cn
 */
package com.cnl.mybot.pcr.team.entity.user;

/**
 * Auto-generated: 2021-04-14 16:49:57
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Damage_list {

    private long datetime;
    private String boss_name;
    private int lap_num;
    private long damage;
    private int kill;
    private int reimburse;

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setBoss_name(String boss_name) {
        this.boss_name = boss_name;
    }

    public String getBoss_name() {
        return boss_name;
    }

    public void setLap_num(int lap_num) {
        this.lap_num = lap_num;
    }

    public int getLap_num() {
        return lap_num;
    }

    public void setDamage(long damage) {
        this.damage = damage;
    }

    public long getDamage() {
        return damage;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public int getKill() {
        return kill;
    }

    public void setReimburse(int reimburse) {
        this.reimburse = reimburse;
    }

    public int getReimburse() {
        return reimburse;
    }

}