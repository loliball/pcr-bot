/**
 * Copyright 2021 json.cn
 */
package com.cnl.mybot.pcr.team.entity.bossnow;

import java.util.List;

/**
 * Auto-generated: 2021-04-14 23:35:17
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class CurrentData {

    private long server_time;
    private Clan_info clan_info;
    private Boss_info boss_info;
    private Battle_info battle_info;
    private List<String> day_list;

    public void setServer_time(long server_time) {
        this.server_time = server_time;
    }

    public long getServer_time() {
        return server_time;
    }

    public void setClan_info(Clan_info clan_info) {
        this.clan_info = clan_info;
    }

    public Clan_info getClan_info() {
        return clan_info;
    }

    public void setBoss_info(Boss_info boss_info) {
        this.boss_info = boss_info;
    }

    public Boss_info getBoss_info() {
        return boss_info;
    }

    public void setBattle_info(Battle_info battle_info) {
        this.battle_info = battle_info;
    }

    public Battle_info getBattle_info() {
        return battle_info;
    }

    public void setDay_list(List<String> day_list) {
        this.day_list = day_list;
    }

    public List<String> getDay_list() {
        return day_list;
    }

}