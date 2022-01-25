package com.cnl.mybot.pcr.team;

import com.alibaba.fastjson.JSONObject;
import com.cnl.mybot.pcr.team.entity.search.JsonRootBean;

public class SearchResultAnalyse {
    private JsonRootBean root;

    private SearchResultAnalyse() {
    }

    public static SearchResultAnalyse analyse(String data) {
        var info = new SearchResultAnalyse();
        info.root = JSONObject.parseObject(data, JsonRootBean.class);
        return info;
    }

    public JsonRootBean getRoot() {
        return root;
    }
}
