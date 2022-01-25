package com.cnl.mybot.ys.signin;

import com.cnl.mybot.ys.signin.entity.Reward;
import okhttp3.MediaType;

import java.util.ArrayList;
import java.util.List;

public class StaticAPI {
    //签到
    public final static String signUrl = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/sign";
    //签到信息
    public final static String signInfo = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/home?act_id=e202009291139501";
    //签到天数
    public final static String signDay = "https://api-takumi.mihoyo.com/event/bbs_sign_reward/info?region={region}&act_id={act_id}&uid={uid}";
    //游戏信息
    public final static String gameRole = "https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=hk4e_cn";

    //设备id
    public final static String device_id = "94588081EDD446EFAA3A45B8CC436CCF";
    //软件版本
    public final static String app_version = "2.7.0";
    //盐值
    public final static String salt = "fd3ykrh7o1j54g581upo1tvpam0dsgtf";
    //服务器
    public final static String region = "cn_gf01";
    //原神签到act_id
    public final static String act_id = "e202009291139501";
    //mamitype
    public final static MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    //签到奖励
    public final static List<Reward> REWARD_LIST = new ArrayList<>();
}
