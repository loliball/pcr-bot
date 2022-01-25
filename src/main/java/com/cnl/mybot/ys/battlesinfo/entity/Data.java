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
public class Data {

    private int schedule_id;
    private String start_time;
    private String end_time;
    private int total_battle_times;
    private int total_win_times;
    private String max_floor;
    private List<Reveal_rank> reveal_rank;
    private List<Defeat_rank> defeat_rank;
    private List<Damage_rank> damage_rank;
    private List<Take_damage_rank> take_damage_rank;
    private List<Normal_skill_rank> normal_skill_rank;
    private List<Energy_skill_rank> energy_skill_rank;
    private List<Floors> floors;
    private int total_star;
    private boolean is_unlock;

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setTotal_battle_times(int total_battle_times) {
        this.total_battle_times = total_battle_times;
    }

    public int getTotal_battle_times() {
        return total_battle_times;
    }

    public void setTotal_win_times(int total_win_times) {
        this.total_win_times = total_win_times;
    }

    public int getTotal_win_times() {
        return total_win_times;
    }

    public void setMax_floor(String max_floor) {
        this.max_floor = max_floor;
    }

    public String getMax_floor() {
        return max_floor;
    }

    public void setReveal_rank(List<Reveal_rank> reveal_rank) {
        this.reveal_rank = reveal_rank;
    }

    public List<Reveal_rank> getReveal_rank() {
        return reveal_rank;
    }

    public void setDefeat_rank(List<Defeat_rank> defeat_rank) {
        this.defeat_rank = defeat_rank;
    }

    public List<Defeat_rank> getDefeat_rank() {
        return defeat_rank;
    }

    public void setDamage_rank(List<Damage_rank> damage_rank) {
        this.damage_rank = damage_rank;
    }

    public List<Damage_rank> getDamage_rank() {
        return damage_rank;
    }

    public void setTake_damage_rank(List<Take_damage_rank> take_damage_rank) {
        this.take_damage_rank = take_damage_rank;
    }

    public List<Take_damage_rank> getTake_damage_rank() {
        return take_damage_rank;
    }

    public void setNormal_skill_rank(List<Normal_skill_rank> normal_skill_rank) {
        this.normal_skill_rank = normal_skill_rank;
    }

    public List<Normal_skill_rank> getNormal_skill_rank() {
        return normal_skill_rank;
    }

    public void setEnergy_skill_rank(List<Energy_skill_rank> energy_skill_rank) {
        this.energy_skill_rank = energy_skill_rank;
    }

    public List<Energy_skill_rank> getEnergy_skill_rank() {
        return energy_skill_rank;
    }

    public void setFloors(List<Floors> floors) {
        this.floors = floors;
    }

    public List<Floors> getFloors() {
        return floors;
    }

    public void setTotal_star(int total_star) {
        this.total_star = total_star;
    }

    public int getTotal_star() {
        return total_star;
    }

    public void setIs_unlock(boolean is_unlock) {
        this.is_unlock = is_unlock;
    }

    public boolean getIs_unlock() {
        return is_unlock;
    }

}