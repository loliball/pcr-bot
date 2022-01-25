package com.cnl.mybot.ys.getinfo;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class SummaryInfo {

    private List<CharacterInfo> character;
    private OpenWorldInfo world;
    private List<CityInfo> city;

    private SummaryInfo() {
    }

    public static SummaryInfo analyse(String rawJson) {
        SummaryInfo info = new SummaryInfo();
        info.character = JSONObject.parseObject(rawJson)
                .getJSONObject("data")
                .getJSONArray("avatars")
                .toJavaList(CharacterInfo.class);
        info.world = JSONObject.parseObject(rawJson)
                .getJSONObject("data")
                .getJSONObject("stats")
                .toJavaObject(OpenWorldInfo.class);
        info.city = JSONObject.parseObject(rawJson)
                .getJSONObject("data")
                .getJSONArray("world_explorations")
                .toJavaList(CityInfo.class);
        return info;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("角色信息：\n");
        character.forEach(userInfo -> {
            sb.append(userInfo.toString()).append('\n');
        });
        sb.append("世界信息：\n");
        sb.append(world).append('\n');
        city.forEach(cityInfo -> {
            sb.append(cityInfo.toString()).append('\n');
        });
        if (sb.charAt(sb.length() - 1) == '\n')
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
