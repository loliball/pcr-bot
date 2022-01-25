package com.cnl.mybot.pcr.team;

import com.alibaba.fastjson.JSONObject;
import com.cnl.mybot.pcr.team.entity.bosslist.Boss_list;
import com.cnl.mybot.pcr.team.entity.bosslist.JsonRootBean;

import java.util.List;

public class TeamWarBossListAnalyse {
    private JsonRootBean root;

    private TeamWarBossListAnalyse() {
    }

    public static TeamWarBossListAnalyse analyse(String data) {
        var info = new TeamWarBossListAnalyse();
        info.root = JSONObject.parseObject(data, JsonRootBean.class);
        return info;
    }

    private List<Boss_list> getBossList() {
        return root.getData().getBoss_list();
    }

    public static int preBoss(int id) {
        if (id < 1 || id > 5) return 0;
        if (id == 1) return 5;
        return id - 1;
    }

    public static int nextBoss(int id) {
        if (id < 1 || id > 5) return 0;
        if (id == 5) return 1;
        return id + 1;
    }

    public int name2id(String name) {
        var list = getBossList();
        for (int i = 0; i < list.size(); i++) {
            if (name.equals(list.get(i).getBoss_name()))
                return i + 1;
        }
        return 0;
    }

    public String id2name(int id) {
        if (id < 1 || id > 5) return "没有这个BOSS";
        return getBossList().get(id - 1).getBoss_name();
    }
}
