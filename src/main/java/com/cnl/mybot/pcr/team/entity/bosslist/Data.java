/**
 * Copyright 2021 json.cn
 */
package com.cnl.mybot.pcr.team.entity.bosslist;

import java.util.List;

/**
 * Auto-generated: 2021-04-14 23:33:11
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Data {

    private String name;
    private List<Boss_list> boss_list;
    private List<First_page> first_page;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBoss_list(List<Boss_list> boss_list) {
        this.boss_list = boss_list;
    }

    public List<Boss_list> getBoss_list() {
        return boss_list;
    }

    public void setFirst_page(List<First_page> first_page) {
        this.first_page = first_page;
    }

    public List<First_page> getFirst_page() {
        return first_page;
    }

}