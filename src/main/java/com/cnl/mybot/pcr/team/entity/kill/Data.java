/**
 * Copyright 2021 json.cn
 */
package com.cnl.mybot.pcr.team.entity.kill;

import java.util.List;

/**
 * Auto-generated: 2021-04-13 18:57:2
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Data {

    private int has_next_page;
    private List<Bean> list;
    private int member_count;
    private int total_damage_num;

    public void setHas_next_page(int has_next_page) {
        this.has_next_page = has_next_page;
    }

    public int getHas_next_page() {
        return has_next_page;
    }

    public void setList(List<Bean> list) {
        this.list = list;
    }

    public List<Bean> getList() {
        return list;
    }

    public void setMember_count(int member_count) {
        this.member_count = member_count;
    }

    public int getMember_count() {
        return member_count;
    }

    public void setTotal_damage_num(int total_damage_num) {
        this.total_damage_num = total_damage_num;
    }

    public int getTotal_damage_num() {
        return total_damage_num;
    }

}