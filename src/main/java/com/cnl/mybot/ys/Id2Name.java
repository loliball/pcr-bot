package com.cnl.mybot.ys;

import java.util.HashMap;
import java.util.Map;

public class Id2Name {

    static Map<Long, String> map = new HashMap<>();

    static {
        map.put(10000003L, "琴");

        map.put(10000005L, "旅行者"); //哥哥
        map.put(10000006L, "丽莎");
        map.put(10000007L, "旅行者"); //妹妹

        map.put(10000014L, "芭芭拉");
        map.put(10000015L, "凯亚");
        map.put(10000016L, "迪卢克");

        map.put(10000020L, "雷泽");
        map.put(10000021L, "安柏");
        map.put(10000022L, "温迪");
        map.put(10000023L, "香菱");
        map.put(10000024L, "北斗");
        map.put(10000025L, "行秋");
        map.put(10000026L, "魈");
        map.put(10000027L, "凝光");

        map.put(10000029L, "可莉");
        map.put(10000030L, "钟离");
        map.put(10000031L, "菲谢尔");
        map.put(10000032L, "班尼特");
        map.put(10000033L, "达达利亚");
        map.put(10000034L, "诺艾尔");
        map.put(10000035L, "七七");
        map.put(10000036L, "重云");
        map.put(10000037L, "甘雨");
        map.put(10000038L, "阿贝多");
        map.put(10000039L, "迪奥娜");

        map.put(10000041L, "莫娜");
        map.put(10000042L, "刻晴");
        map.put(10000043L, "砂糖");
        map.put(10000044L, "辛焱");
        map.put(10000045L, "罗莎莉亚");
        map.put(10000046L, "胡桃");

        map.put(10000048L, "烟绯");
    }

//    static {
//        String s = "{\"10000003\": \"琴\",\n" +
//                "\"10000005\": \"旅行者\",\n" +
//                "\"10000006\": \"丽莎\",\n" +
//                "\"10000007\": \"旅行者\",\n" +
//                "\"10000014\": \"芭芭拉\",\n" +
//                "\"10000015\": \"凯亚\",\n" +
//                "\"10000016\": \"迪卢克\",\n" +
//                "\"10000020\": \"雷泽\",\n" +
//                "\"10000021\": \"安柏\",\n" +
//                "\"10000022\": \"温迪\",\n" +
//                "\"10000023\": \"香菱\",\n" +
//                "\"10000024\": \"北斗\",\n" +
//                "\"10000025\": \"行秋\",\n" +
//                "\"10000027\": \"凝光\",\n" +
//                "\"10000029\": \"可莉\",\n" +
//                "\"10000031\": \"菲谢尔\",\n" +
//                "\"10000032\": \"班尼特\",\n" +
//                "\"10000033\": \"达达利亚\",\n" +
//                "\"10000034\": \"诺艾尔\",\n" +
//                "\"10000036\": \"重云\",\n" +
//                "\"10000039\": \"迪奥娜\",\n" +
//                "\"10000041\": \"莫娜\",\n" +
//                "\"10000042\": \"刻晴\",\n" +
//                "\"10000043\": \"砂糖\"}";
//        JSONObject jobj = JSONObject.parseObject(s);
//        for (String key : jobj.keySet()) {
//            map.put(Long.parseLong(key), jobj.getString(key));
//        }
//        for(Long key: map.keySet()){
//            System.out.println(String.format("map.put(%sL, \"%s\");", key, map.get(key)));
//        }
//    }

    public static String getNameById(long id) {
        if (map.containsKey(id))
            return map.get(id);
        return String.valueOf(id);
    }
}
