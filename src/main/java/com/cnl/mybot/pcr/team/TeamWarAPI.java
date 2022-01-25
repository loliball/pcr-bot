package com.cnl.mybot.pcr.team;

import com.cnl.mybot.pcr.team.entity.kill.JsonRootBean;
import com.cnl.mybot.system.Utils;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeamWarAPI {

    public static String searchByName(String name) {
        return networkRequest(Bigfuck.getSearchByNameUrl(name));
    }

    public static String searchByRank(String rank) {
        return networkRequest(Bigfuck.getSearchByRankUrl(rank));
    }

    public static String getClanBattleList() {
        return networkRequest(Bigfuck.getClanBattleListUrl());
    }

    public static String getTimeRange(int battle) {
        return networkRequest(Bigfuck.getClanBattleRankingTimeRangeUrl(battle));
    }

    public static String getMyClanRanking(int year, int month, int day, int hour, int minute, int battle) {
        return networkRequest(Bigfuck.getMyClanRankingUrl(year, month, day, hour, minute, battle));
    }

    public static String getBOSSList() {
        return networkRequest(Bigfuck.getBOSSListUrl());
    }

    public static String getNowBossInfo() {
        return networkRequest(Bigfuck.getNowBOSSInfoUrl());
    }

    public static String getTeamWarAllUsers() {
        return networkRequest(Bigfuck.getDayUrl());
    }

    public static String getTeamWarInfo(int page) {
        String json = networkRequest(Bigfuck.getTimelineUrl(page));
        System.out.println("getTeamWarInfo(" + page + ")" + json);
        return json;
    }

    @NotNull
    public static List<JsonRootBean> getTeamWarInfoAll() throws Exception {
        List<JsonRootBean> data = new ArrayList<>();
        TeamWarAnalyse last = null;
        int page = 1;
        do {
            last = TeamWarAnalyse.analyse(getTeamWarInfo(page));
            data.add(last.getRoot());
            page++;
            Thread.sleep(Utils.random(1000, 2000));
        } while (last.hasNext());
        return data;
    }

    @Nullable
    public static String networkRequest(String dayUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(dayUrl)
                .headers(getHeaders())
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                if ("OK".equals(response.message())) {
                    return response.body().string();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    private static Headers getHeaders() {
        return new Headers.Builder()
                .add("Content-Type", "application/vnd.api+json")
                .add("Accept", "application/vnd.api+json")
                .add("BF-Json-Api-Version", "v1.0")
                .add("BF-Client-Type", "BF-ANDROID")
                .add("BF-Client-Version", "3.7.1")
                .add("BF-Client-Data", Bigfuck.getBF_Client_Data())
                .add("Host", "api.bigfun.cn")
                .add("Connection", "Keep-Alive")
                .add("User-Agent", "okhttp/3.12.12")
                .build();
    }

}
