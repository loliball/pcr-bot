package com.cnl.mybot.ys.getinfo;

import java.util.HashMap;
import java.util.Map;

public class Element {

    private static Map<String, String> map = new HashMap<>();

    static {
        map.put("None", "无");
        map.put("Anemo", "风");
        map.put("Pyro", "火");
        map.put("Geo", "岩");
        map.put("Electro", "雷");
        map.put("Cryo", "冰");
        map.put("Hydro", "水");
    }

    public static String getElementName(String name) {
        return map.get(name);
    }
}
