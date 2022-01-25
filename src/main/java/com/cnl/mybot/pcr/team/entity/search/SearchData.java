/**
 * Copyright 2021 json.cn
 */
package com.cnl.mybot.pcr.team.entity.search;

/**
 * Auto-generated: 2021-05-11 17:17:58
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class SearchData {

    private String clan_name;
    private String leader_name;
    private long damage;
    private int rank;
    private long delta_damage;

    public void setClan_name(String clan_name) {
        this.clan_name = clan_name;
    }

    public String getClan_name() {
        return clan_name;
    }

    public void setLeader_name(String leader_name) {
        this.leader_name = leader_name;
    }

    public String getLeader_name() {
        return leader_name;
    }

    public void setDamage(long damage) {
        this.damage = damage;
    }

    public long getDamage() {
        return damage;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setDelta_damage(long delta_damage) {
        this.delta_damage = delta_damage;
    }

    public long getDelta_damage() {
        return delta_damage;
    }

}