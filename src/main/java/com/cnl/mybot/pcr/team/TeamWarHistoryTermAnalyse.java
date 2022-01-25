package com.cnl.mybot.pcr.team;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamWarHistoryTermAnalyse {

    private JSONObject root;

    private List<BOSSTerm> data;

    private TeamWarHistoryTermAnalyse() {
    }

    public static TeamWarHistoryTermAnalyse analyse(String data) {
        var info = new TeamWarHistoryTermAnalyse();
        info.root = JSONObject.parseObject(data);
        if (info.root.getInteger("code") != 0) {
            return null;
        }
        JSONArray ja = info.root.getJSONArray("data");
        if (ja == null || ja.size() == 0) {
            return null;
        }
        info.data = new ArrayList<>(ja.size());
        for (Object obj : ja) {
            JSONObject obj1 = (JSONObject) obj;
            info.data.add(new BOSSTerm(Integer.parseInt(obj1.getString("id")), obj1.getString("name")));
        }
        return info;
    }

    public List<BOSSTerm> getData() {
        return data;
    }

    public int getNowId() {
        return data.get(0).id;
    }

    public static class BOSSTerm {
        public BOSSTerm(int id, String name) {
            this.id = id;
            this.name = name;
        }

        int id;
        String name;

        @Override
        public String toString() {
            return "BOSSTerm{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TeamWarHistoryTermAnalyse{" +
                "data=" + data +
                '}';
    }
}
