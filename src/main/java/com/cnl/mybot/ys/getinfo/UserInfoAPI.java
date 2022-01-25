package com.cnl.mybot.ys.getinfo;

import com.cnl.mybot.ys.signin.StaticAPI;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class UserInfoAPI {

    public static final int ABYSS_THIS_MONTH = 1;
    public static final int ABYSS_LAST_MONTH = 2;

    static OkHttpClient client = new OkHttpClient();

    public static String getSummary(long uid, String cookie) {
        return SummaryInfo.analyse(getUserInfo(uid, cookie)).toString();
    }

    //@time 1为本期深渊，2为上期深渊
    public static String getAbyssInfo(long uid, String cookie, int time) {
        String sid = getSid(uid);
        if (sid == null) return null;
        String url = "https://api-takumi.mihoyo.com/game_record/genshin/api/spiralAbyss?schedule_type={time}&server={sid}&role_id={uid}";
        url = url.replace("{time}", String.valueOf(time))
                .replace("{sid}", sid)
                .replace("{uid}", String.valueOf(uid));
        Request request = new Request.Builder()
                .url(url)
                .headers(getHeaders(cookie))
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
//            logger.info("获取深渊信息失败");
        }
        return null;
    }

    public static String getUserInfo(long uid, String cookie) {
        String sid = getSid(uid);
        if (sid == null) return null;
        String url = "https://api-takumi.mihoyo.com/game_record/genshin/api/index?server={sid}&role_id={uid}";
        url = url.replace("{sid}", sid)
                .replace("{uid}", String.valueOf(uid));
        Request request = new Request.Builder()
                .url(url)
                .headers(getHeaders(cookie))
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
//            logger.info("获取基本信息失败");
        }
        return null;
    }

    private static String getSid(long uid) {
        String str = String.valueOf(uid);
        if (str.startsWith("1")) {
            return "cn_gf01";
        } else if (str.startsWith("5")) {
            return "cn_qd01";
        } else return null;
    }

    //包装请求头
    public static Headers getHeaders(String cookie) {
        String ds = getDS();
        return new Headers.Builder()
                //.add("x-rpc-device_id", StaticAPI.device_id)
                .add("Host", "api-takumi.mihoyo.com")
                .add("Origin", "https://webstatic.mihoyo.com")
                .add("Content-type", "application/json;charset=utf-8")
                .add("Accept", "application/json, text/plain, */*")
                .add("x-rpc-client_type", "2")
                .add("X-Requested-With", "com.mihoyo.hyperion")
                .add("Sec-Fetch-Site", "same-site")
                .add("Sec-Fetch-Mode", "cors")
                .add("Sec-Fetch-Dest", "empty")
                .add("x-rpc-app_version", StaticAPI.app_version)
                .add("Cookie", cookie)
                .add("DS", ds)
                .add("User-Agent", "Mozilla/5.0 (Linux; Android 10; Redmi K30 Pro Build/QKQ1.200419.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/81.0.4044.138 Mobile Safari/537.36 miHoYoBBS/" + StaticAPI.app_version)
                .add("Referer", "https://webstatic.mihoyo.com/app/community-game-records/index.html?v=6")
                .build();
    }

    //生成DS
    public static String getDS() {
        Random random = new Random();
        int nextInt = random.nextInt(15);
        String a = StaticAPI.salt;
        String b = new Date().getTime() + "";
        b = b.substring(0, 10);
        String c = UUID.randomUUID().toString().replace("-", "").substring(nextInt, nextInt + 6);
        String d = md5Hex("salt=" + a + "&t=" + b + "&r=" + c);
        return b + "," + c + "," + d + ",";
    }

    public static String md5Hex(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
