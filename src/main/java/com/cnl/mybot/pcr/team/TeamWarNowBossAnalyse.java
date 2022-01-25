package com.cnl.mybot.pcr.team;

import com.alibaba.fastjson.JSONObject;
import com.cnl.mybot.pcr.team.entity.bossnow.JsonRootBean;

public class TeamWarNowBossAnalyse {
    private JsonRootBean root;

    private TeamWarNowBossAnalyse() {
    }

    public static TeamWarNowBossAnalyse analyse(String data) {
        var info = new TeamWarNowBossAnalyse();
        info.root = JSONObject.parseObject(data, JsonRootBean.class);
        return info;
    }

    public JsonRootBean getRoot() {
        return root;
    }
}
