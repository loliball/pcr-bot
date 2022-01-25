/**
 * Copyright 2021 json.cn
 */
package com.cnl.mybot.pcr.team.entity.bossnow;

/**
 * Auto-generated: 2021-04-14 23:35:17
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Boss_info {

    private String name;
    private long total_life;
    private long current_life;
    private int lap_num;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTotal_life(long total_life) {
        this.total_life = total_life;
    }

    public long getTotal_life() {
        return total_life;
    }

    public void setCurrent_life(long current_life) {
        this.current_life = current_life;
    }

    public long getCurrent_life() {
        return current_life;
    }

    public void setLap_num(int lap_num) {
        this.lap_num = lap_num;
    }

    public int getLap_num() {
        return lap_num;
    }

}