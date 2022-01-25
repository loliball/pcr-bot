package com.cnl.mybot.ys.getinfo;

public class OpenWorldInfo {
    private int active_day_number;
    private int achievement_number;
    private int anemoculus_number;
    private int geoculus_number;
    private int avatar_number;
    private int way_point_number;
    private int domain_number;
    private String spiral_abyss;
    private int common_chest_number;
    private int exquisite_chest_number;
    private int precious_chest_number;
    private int luxurious_chest_number;

    @Override
    public String toString() {
        return String.format("活跃天数%d，达成成就%d，风神瞳%d，岩神瞳%d，" +
                        "获得角色%d个，解锁传送点%d个和%d个秘境，深境螺旋本期%s，" +
                        "开启箱子：普通%d个，精致%d个，珍贵%d个，华丽%d个",
                active_day_number, achievement_number, anemoculus_number, geoculus_number,
                avatar_number, way_point_number, domain_number, spiral_abyss.equals("-") ? "没打" : spiral_abyss,
                common_chest_number, exquisite_chest_number, precious_chest_number, luxurious_chest_number);
    }
}
