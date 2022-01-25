package com.cnl.mybot.pcr.team;

import com.alibaba.fastjson.JSONObject;
import com.cnl.mybot.pcr.team.entity.user.JsonRootBean;

public class TeamWarUserAnalyse {

    private JsonRootBean root;

    private TeamWarUserAnalyse() {
    }

    public static TeamWarUserAnalyse analyse(String data) {
        var info = new TeamWarUserAnalyse();
        info.root = JSONObject.parseObject(data, JsonRootBean.class);
        return info;
    }

    public JsonRootBean getRoot() {
        return root;
    }
}
