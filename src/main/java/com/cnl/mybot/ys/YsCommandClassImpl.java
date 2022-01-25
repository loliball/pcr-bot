package com.cnl.mybot.ys;

import com.cnl.mybot.annotation.CommandClassAnnotation;
import com.cnl.mybot.annotation.CommandMethod;
import com.cnl.mybot.system.CommandClass;
import com.cnl.mybot.system.ConfigLoader;
import com.cnl.mybot.system.MessageOutput;
import com.cnl.mybot.system.Permission;
import com.cnl.mybot.ys.battlesinfo.BattleInfo;
import com.cnl.mybot.ys.getinfo.SummaryInfo;
import com.cnl.mybot.ys.getinfo.UserInfoAPI;
import com.cnl.mybot.ys.signin.SignInLooper;

@CommandClassAnnotation("ys")
public class YsCommandClassImpl implements CommandClass {

    private final MessageOutput sent;
    private final ConfigLoader config;
    private final SignInLooper loop;

    public YsCommandClassImpl(MessageOutput mo, SignInLooper sign) {
        sent = mo;
        config = ConfigLoader.getInstance();
        loop = sign;
    }

    @CommandMethod(Permission.GROUP_USER)
    public void yshelp() {
        sent.sendMessage("yshelp 显示本帮助信息\n" +
                "ysinfo <id> 查询玩家信息\n" +
                "abyss1 <id> 查询本月深渊信息\n" +
                "abyss2 <id> 查询上月深渊信息");
    }

    @CommandMethod(Permission.GROUP_USER)
    public void ysinfo(String id) {
        String userInfo = UserInfoAPI.getUserInfo(Long.parseLong(id),
                config.getProp("cookie0"));
        System.out.println(userInfo);
        sent.sendMessage(SummaryInfo.analyse(userInfo).toString());
    }

    @CommandMethod(Permission.GROUP_USER)
    public void abyss1(String id) {
        String abyssInfo = UserInfoAPI.getAbyssInfo(Long.parseLong(id),
                config.getProp("cookie0"), UserInfoAPI.ABYSS_THIS_MONTH);
        System.out.println(abyssInfo);
        sent.sendMessage(BattleInfo.analyse(abyssInfo).toString());
    }

    @CommandMethod(Permission.GROUP_USER)
    public void abyss2(String id) {
        String abyssInfo = UserInfoAPI.getAbyssInfo(Long.parseLong(id),
                config.getProp("cookie0"), UserInfoAPI.ABYSS_LAST_MONTH);
        System.out.println(abyssInfo);
        sent.sendMessage(BattleInfo.analyse(abyssInfo).toString());
    }

    @CommandMethod(Permission.DEVELOPER)
    public void checkin() {
        if (loop == null) return;
        loop.signIn();
    }

    @CommandMethod(Permission.DEVELOPER)
    public void update() {
        if (loop == null) return;
        loop.update();
    }

}
