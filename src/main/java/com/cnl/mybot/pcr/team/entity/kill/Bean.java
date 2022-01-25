/**
 * Copyright 2021 json.cn
 */
package com.cnl.mybot.pcr.team.entity.kill;


/**
 * Auto-generated: 2021-04-13 18:57:2
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */

public class Bean {

    private long datetime;
    private String name;
    private String boss_name;
    private int lap_num; //周目
    private long damage;
    private int kill; //尾刀
    private int reimburse; //补偿刀
    private long score;

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public void setScore(long score) {
        this.score = score;
    }

    public long getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "datetime=" + datetime +
                ", name='" + name + '\'' +
                ", boss_name='" + boss_name + '\'' +
                ", lap_num=" + lap_num +
                ", damage=" + damage +
                ", kill=" + kill +
                ", reimburse=" + reimburse +
                ", score=" + score +
                '}';
    }
}