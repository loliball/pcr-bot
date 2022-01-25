/**
 * Copyright 2021 json.cn
 */
package com.cnl.mybot.pcr.team.entity.timerange;

/**
 * Auto-generated: 2021-05-11 20:33:34
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class TimeRangeData {

    private int battle_id;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public TimeRangeData() {
    }

    public TimeRangeData(int battle_id, int year, int month, int day, int hour, int minute) {
        this.battle_id = battle_id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public void setBattle_id(int battle_id) {
        this.battle_id = battle_id;
    }

    public int getBattle_id() {
        return battle_id;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getMonth() {
        return month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getHour() {
        return hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getMinute() {
        return minute;
    }

}