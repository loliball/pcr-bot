package com.cnl.mybot.ys.battlesinfo;

import com.alibaba.fastjson.JSONObject;
import com.cnl.mybot.ys.Id2Name;
import com.cnl.mybot.ys.battlesinfo.entity.Avatars;
import com.cnl.mybot.ys.battlesinfo.entity.Battles;
import com.cnl.mybot.ys.battlesinfo.entity.Data;
import com.cnl.mybot.ys.battlesinfo.entity.Floors;
import com.cnl.mybot.ys.battlesinfo.entity.JsonRootBean;
import com.cnl.mybot.ys.battlesinfo.entity.Levels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BattleInfo {

    private JsonRootBean root;

    private BattleInfo() {
    }

    public static BattleInfo analyse(String rawJson) {
        BattleInfo info = new BattleInfo();
        info.root = JSONObject.parseObject(rawJson, JsonRootBean.class);
        return info;
    }

    @Override
    public String toString() {
        if (root.getRetcode() != 0) {
            return "未公开或id错误";
        }
        StringBuilder sb = new StringBuilder();
        Data dd = root.getData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append(String.format("深渊信息(%s至%s):\n到达%s，渊星%d颗，凹了%d次，成功%d次\n",
                sdf.format(new Date(Integer.parseInt(dd.getStart_time()) * 1000L)),
                sdf.format(new Date(Integer.parseInt(dd.getEnd_time()) * 1000L)),
                dd.getMax_floor(), dd.getTotal_star(), dd.getTotal_battle_times(),
                dd.getTotal_win_times()
        ));
        for (Floors floor : dd.getFloors()) {
            sb.append(String.format("第%d层：%d/%d\n",
                    floor.getIndex(), floor.getStar(), floor.getMax_star()
            ));
            for (Levels level : floor.getLevels()) {
                sb.append(String.format("第%d间(%s)：%d/%d 阵容：\n",
                        level.getIndex(),
                        sdf2.format(new Date(Integer.parseInt(level.getBattles().get(0).getTimestamp()) * 1000L)),
                        level.getStar(), level.getMax_star()
                ));
                for (Battles battle : level.getBattles()) {
                    sb.append(String.format("%s半：",
                            battle.getIndex() == 1 ? "上" : "下"
                    ));
                    for (Avatars ava : battle.getAvatars()) {
                        sb.append(Id2Name.getNameById(ava.getId())).append(' ');
                    }
                    sb.append('\n');
                }
            }
        }
        return sb.toString();
    }
}