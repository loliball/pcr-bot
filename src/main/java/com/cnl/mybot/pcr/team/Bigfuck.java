package com.cnl.mybot.pcr.team;

import com.cnl.mybot.system.ConfigLoader;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Bigfuck {

    //bigfun(cn.bigfun) var3.7.1(10048)
    private final static String access_token = "93178af2a9c68c6c6945eaff98487f91";
    //static value, you can get it use HttpCanary or read it in
    // /data/data/cn.bigfun/shared_prefs/BF_DATE.xml
    private final static String device_number = "07ad5bda108ebef67fa65c21d55accfd20210406101847b7c7b85dddab1b763b";

    public static String getBF_Client_Data() {
        return ConfigLoader.getInstance().getProp("BF_Client_Data");
    }

    //cn.bigfun.utils.r/d()Long
    private static long rid() {
        return ((int) (Math.random() * 900000.0d)) + 100000;
    }

    //cn.bigfun.utils.BigfunMake/b(String)String
    @NotNull
    private static String sign(String str) {
        if (str == null || "".equals(str)) {
            return "";
        }
        try {
            StringBuilder str2 = new StringBuilder();
            //libbigfunmake.so native makeCode()String
            //this method always return WKO-2k_03jisxgH6
            for (byte b : MessageDigest.getInstance("MD5").digest((str + "WKO-2k_03jisxgH6").getBytes())) {
                String hexString = Integer.toHexString(b & 255);
                if (hexString.length() == 1) {
                    hexString = "0" + hexString;
                }
                str2.append(hexString);
            }
            return str2.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    //cn.bigfun.utils.r/a(List<String>, long, long)String
    @NotNull
    private static String sign(@NotNull List<String> list, long ts, long rid) {
        list.add("access_token=" + access_token);
        list.add("ts=" + ts);
        list.add("rid=" + rid);
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append("&");
            }
        }
        return sign(sb.toString());
    }

    private static void addGlobalArgs(@NotNull List<String> list) {
        long time = System.currentTimeMillis();
        long ts = time / 1000;
        long rid = ts + rid();
        list.add("device_number=" + device_number);
        list.add("sign=" + sign(list, ts, rid));
    }

    @NotNull
    private static String getArgsAppendUrl(@NotNull List<String> list) {
        StringBuilder sb = new StringBuilder("https://api.bigfun.cn/webview/android?");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    @NotNull
    public static String getTimelineUrl(int page) {
        List<String> list = new ArrayList<>();
        list.add("target=gzlj-clan-day-timeline-report/a");
        list.add("page=" + page);
        list.add("size=30");
        addGlobalArgs(list);
        return getArgsAppendUrl(list);
    }

    @NotNull
    public static String getDayUrl() {
        var format = new SimpleDateFormat("yyyy-MM-dd");
        var now = Calendar.getInstance();
        if (now.get(Calendar.HOUR_OF_DAY) < 5)
            now.add(Calendar.DATE, -1);
        return getDayUrl(format.format(new Date(now.getTimeInMillis())));
    }

    @NotNull
    public static String getDayUrl(String day) {
        List<String> list = new ArrayList<>();
        list.add("target=gzlj-clan-day-report/a");
        list.add("date=" + day);
        list.add("size=30");
        addGlobalArgs(list);
        return getArgsAppendUrl(list);
    }

    @NotNull
    public static String getNowBOSSInfoUrl() {
        List<String> list = new ArrayList<>();
        list.add("target=gzlj-clan-day-report-collect/a");
        addGlobalArgs(list);
        return getArgsAppendUrl(list);
    }

    @NotNull
    public static String getBOSSListUrl() {
        List<String> list = new ArrayList<>();
        list.add("target=gzlj-clan-boss-report-collect/a");
        addGlobalArgs(list);
        return getArgsAppendUrl(list);
    }

    @NotNull
    public static String getSearchByNameUrl(String name) {
        List<String> list = new ArrayList<>();
        list.add("target=gzlj-search-clan/b");
        list.add("name=" + name);
        addGlobalArgs(list);
        return getArgsAppendUrl(list);
    }

    @NotNull
    public static String getSearchByRankUrl(String rank) {
        List<String> list = new ArrayList<>();
        list.add("target=gzlj-search-clan/b");
        list.add("rank=" + rank);
        addGlobalArgs(list);
        return getArgsAppendUrl(list);
    }

    @NotNull
    public static String getClanBattleListUrl() {
        List<String> list = new ArrayList<>();
        list.add("target=gzlj-clan-battle-list/a");
        addGlobalArgs(list);
        return getArgsAppendUrl(list);
    }

    @NotNull
    public static String getClanBattleRankingTimeRangeUrl(int battle) {
        List<String> list = new ArrayList<>();
        list.add("target=gzlj-clan-battle-ranking-time-range/a");
        list.add("battle_id=" + battle);
        addGlobalArgs(list);
        return getArgsAppendUrl(list);
    }

    @NotNull
    public static String getMyClanRankingUrl(int year, int month, int day, int hour, int minute, int battle) {
        List<String> list = new ArrayList<>();
        list.add("target=gzlj-my-clan-ranking/a");
        list.add("year=" + year);
        list.add("month=" + month);
        list.add("day=" + day);
        list.add("hour=" + hour);
        list.add("minute=" + minute);
        list.add("battle_id=" + battle);
        addGlobalArgs(list);
        return getArgsAppendUrl(list);
    }

}
