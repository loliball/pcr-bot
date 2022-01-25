package com.cnl.mybot.pcr.team;

import com.alibaba.fastjson.JSONObject;
import com.cnl.mybot.pcr.team.entity.search.SearchData;

public class TeamWarHistoryRankAnalyse {

    private JSONObject root;

    private TeamWarHistoryRankAnalyse() {
    }

    public static TeamWarHistoryRankAnalyse analyse(String data) {
        var info = new TeamWarHistoryRankAnalyse();
        info.root = JSONObject.parseObject(data);
        return info;
    }

    public JSONObject getRoot() {
        return root;
    }

    public SearchData getData() {
        String json = root.getString("data");
        if (json == null || !json.startsWith("{")) return null;
        return JSONObject.parseObject(json, SearchData.class);
    }

}
