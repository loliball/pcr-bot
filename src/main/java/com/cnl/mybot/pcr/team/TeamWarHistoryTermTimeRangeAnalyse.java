package com.cnl.mybot.pcr.team;

import com.alibaba.fastjson.JSONObject;
import com.cnl.mybot.pcr.team.entity.timerange.JsonRootBean;

public class TeamWarHistoryTermTimeRangeAnalyse {
    private JsonRootBean root;

    private TeamWarHistoryTermTimeRangeAnalyse() {
    }

    public static TeamWarHistoryTermTimeRangeAnalyse analyse(String data) {
        var info = new TeamWarHistoryTermTimeRangeAnalyse();
        info.root = JSONObject.parseObject(data, JsonRootBean.class);
        return info;
    }

    public JsonRootBean getRoot() {
        return root;
    }
}
