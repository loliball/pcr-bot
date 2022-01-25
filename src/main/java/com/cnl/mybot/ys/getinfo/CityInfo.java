package com.cnl.mybot.ys.getinfo;


public class CityInfo {
    private String name;
    private int level;
    private int exploration_percentage;

    @Override
    public String toString() {
        return String.format("%s探索度%.1f，声望%d级",
                name, exploration_percentage / 10.0f, level);
    }
}
