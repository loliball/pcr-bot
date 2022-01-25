package com.cnl.mybot.ys.signin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cnl.mybot.system.Utils;
import com.cnl.mybot.ys.getinfo.UserInfoAPI;
import com.cnl.mybot.ys.signin.entity.GameRole;
import com.cnl.mybot.ys.signin.entity.Result;
import com.cnl.mybot.ys.signin.entity.Reward;
import com.cnl.mybot.ys.signin.entity.User;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ApiTakumi {
    static Logger logger = Logger.getLogger(ApiTakumi.class);
    static OkHttpClient client = new OkHttpClient();

    //获取签到奖励列表
    public static void initSignAwards(String cookie) {
        Request request = new Request.Builder()
                .url(StaticAPI.signInfo)
                .headers(getHeaders(cookie))
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                if ("OK".equals(response.message())) {
                    JSONObject jo = JSONObject.parseObject(response.body().string());
                    JSONArray jsonArray = jo.getJSONObject("data").getJSONArray("awards");
                    jsonArray.forEach(object -> {
                        JSONObject award = JSONObject.parseObject(object.toString());
                        Reward reward = new Reward()
                                .setName(award.getString("name"))
                                .setIcon(award.getString("icon"))
                                .setCnt(award.getInteger("cnt"));
                        StaticAPI.REWARD_LIST.add(reward);
                    });
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int updateSignDay(List<User> users) {
        AtomicInteger count = new AtomicInteger();
        users.forEach(user -> {
            user.getRole().forEach(gameRole -> {
                gameRole.setSignDay(getSignDay(gameRole));
                count.incrementAndGet();
            });
        });
        return count.intValue();
    }

    public static int getSignDay(GameRole role) {
        String url = StaticAPI.signDay;
        url = url.replace("{region}", role.getRegion())
                .replace("{uid}", role.getGameUid().toString())
                .replace("{act_id}", StaticAPI.act_id);
        Request request = new Request.Builder()
                .url(url)
                .headers(getHeaders(role.getCookie()))
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                if ("OK".equals(response.message())) {
                    String body = response.body().string();
                    System.out.println(body);
                    JSONObject jo = JSONObject.parseObject(body).getJSONObject("data");
                    return jo.getInteger("total_sign_day");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void signIn(User user, SignInCallback cb) {
        List<Result> list = new ArrayList<>();
        user.getRole().forEach(role -> {
            JSONObject data = new JSONObject();
            data.put("act_id", StaticAPI.act_id);
            data.put("region", role.getRegion());
            data.put("uid", role.getGameUid());
            Request request = new Request.Builder()
                    .url(StaticAPI.signUrl)
                    .post(RequestBody.create(data.toJSONString(), StaticAPI.JSON_TYPE))
                    .headers(getHeadersDS221(role.getCookie()))
                    .build();
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                int signDay = getSignDay(role);
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    //debug
                    System.out.println(body);
                    JSONObject jo = JSONObject.parseObject(body);
                    Result result = new Result();
                    boolean success = "OK".equals(jo.getString("message"));
                    result.setNickname(role.getNickname())
                            .setAward(StaticAPI.REWARD_LIST.get(signDay == 0 ? 0 : signDay - 1))
                            .setSignDay(signDay)
                            .setSignResult(success ? "签到成功" : jo.getString("message"));
                    list.add(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        cb.onSingIn(list);
    }

    //加载游戏角色
    public static void getRoles(List<User> users) {
        users.forEach(item -> {
            List<GameRole> roles = new ArrayList<>();
            Request request = new Request.Builder()
                    .url(StaticAPI.gameRole)
                    .headers(getHeaders(item.getCookie()))
                    .build();
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    JSONObject ret = JSONObject.parseObject(response.body().string());
                    if ("OK".equals(ret.getString("message"))) {
                        JSONArray jo = ret.getJSONObject("data").getJSONArray("list");
                        jo.forEach(roleStr -> {
                            try {
                                JSONObject roleJson = (JSONObject) roleStr;
                                GameRole role = new GameRole();
                                role.setCookie(item.getCookie()).setNickname(roleJson.getString("nickname"))
                                        .setGame_biz(roleJson.getString("game_biz"))
                                        .setLevel(roleJson.getInteger("level"))
                                        .setGameUid(roleJson.getLong("game_uid"))
                                        .setRegion_name(roleJson.getString("region_name"))
                                        .setRegion(roleJson.getString("region"));
                                logger.info("加载到角色:'" + roleJson.getString("nickname") + "',UID:" + roleJson.getLong("game_uid"));
                                role.setSignDay(getSignDay(role));
                                roles.add(role);
                            } catch (Exception e) {
                                logger.info(Utils.getStackTrace(e));
                            }
                        });
                    } else {
                        logger.info("游戏角色加载失败");
                    }
                }
            } catch (IOException e) {
                logger.info("游戏角色加载错误!");
            }
            item.setRole(roles);
        });
    }

    //包装请求头
    public static Headers getHeaders(String cookie) {
        return new Headers.Builder()
                .add("Host", "api-takumi.mihoyo.com")
                .add("Accept", "application/json, text/plain, */*")
                .add("Origin", "https://webstatic.mihoyo.com")
                .add("User-Agent", "Mozilla/5.0 (Linux; Android 10; Redmi K30 Pro Build/QKQ1.200419.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/81.0.4044.138 Mobile Safari/537.36 miHoYoBBS/" + StaticAPI.app_version)
                .add("x-rpc-device_id", "e7425860-4908-3e49-84e3-464bc8ce92a9")
                .add("X-Requested-With", "com.mihoyo.hyperion")
                .add("Sec-Fetch-Site", "same-site")
                .add("Sec-Fetch-Mode", "cors")
                .add("Sec-Fetch-Dest", "empty")
                .add("Referer", "https://webstatic.mihoyo.com/bbs/event/signin-ys/index.html?bbs_auth_required=true&act_id=e202009291139501&utm_source=bbs&utm_medium=mys&utm_campaign=icon")
                .add("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                .add("Cookie", cookie)
                .build();
    }

    public static Headers getHeadersDS221(String cookie) {
        String ds = getDS221();
        return new Headers.Builder()
                .add("Host", "api-takumi.mihoyo.com")
                .add("Connection", "keep-alive")
                .add("Content-Length", "66")
                .add("DS", ds)
                .add("Origin", "https://webstatic.mihoyo.com")
                .add("x-rpc-app_version", "2.2.1")
                .add("User-Agent", "Mozilla/5.0 (Linux; Android 10; Redmi K30 Pro Build/QKQ1.200419.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/81.0.4044.138 Mobile Safari/537.36 miHoYoBBS/" + StaticAPI.app_version)
                .add("x-rpc-device_id", "e7425860-4908-3e49-84e3-464bc8ce92a9")
                .add("Accept", "application/json, text/plain, */*")
                .add("Content-Type", "application/json;charset=UTF-8")
                .add("x-rpc-client_type", "5")
                .add("X-Requested-With", "com.mihoyo.hyperion")
                .add("Sec-Fetch-Site", "same-site")
                .add("Sec-Fetch-Mode", "cors")
                .add("Sec-Fetch-Dest", "empty")
                .add("Referer", "https://webstatic.mihoyo.com/bbs/event/signin-ys/index.html?bbs_auth_required=true&act_id=e202009291139501&utm_source=bbs&utm_medium=mys&utm_campaign=icon")
                .add("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7")
                .add("Cookie", cookie)
                .build();
    }

    //生成DS
    public static String getDS221() {
        Random random = new Random();
        int nextInt = random.nextInt(15);
        //2.2.1 cx2y9z9a29tfqvr1qsq6c7yz99b5jsqt
        //2.3.0 h8w582wxwgqvahcdkpvdhbh2w9casgfl
        //2.7.0 fd3ykrh7o1j54g581upo1tvpam0dsgtf
        String a = "cx2y9z9a29tfqvr1qsq6c7yz99b5jsqt";
        String b = new Date().getTime() + "";
        b = b.substring(0, 10);
        String c = UUID.randomUUID().toString().replace("-", "").substring(nextInt, nextInt + 6);
        String d = UserInfoAPI.md5Hex("salt=" + a + "&t=" + b + "&r=" + c);
        return b + "," + c + "," + d + ",";
    }
}
