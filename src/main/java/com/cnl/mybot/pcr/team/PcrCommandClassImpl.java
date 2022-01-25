package com.cnl.mybot.pcr.team;

import com.cnl.mybot.annotation.CommandClassAnnotation;
import com.cnl.mybot.annotation.CommandMethod;
import com.cnl.mybot.annotation.CommandMethodEnv;
import com.cnl.mybot.annotation.CommandMethodEnvRequest;
import com.cnl.mybot.annotation.GlobalNickName;
import com.cnl.mybot.annotation.NickName;
import com.cnl.mybot.system.CommandClass;
import com.cnl.mybot.system.MessageOutput;
import com.cnl.mybot.system.Permission;
import net.mamoe.mirai.contact.Member;

@CommandClassAnnotation("pcrteamwar")
public class PcrCommandClassImpl implements CommandClass {

    private final MessageOutput sent;
    private final TeamWarLooper loop;

    public PcrCommandClassImpl(MessageOutput messageOutput, TeamWarLooper teamWarLooper) {
        sent = messageOutput;
        loop = teamWarLooper;
    }

    @NickName("pcr")
    @NickName("帮助")
    @GlobalNickName("帮助")
    @CommandMethod(Permission.GROUP_USER)
    public void pcrhelp() {
        sent.sendMessage("任何命令都以!!开头\n" +
                "部分中文命令支持无前缀调用\n" +
                "pcrhelp(帮助) 显示本信息\n" +
                "check(查刀) 查询剩余刀数\n" +
                "info(状态) 查询BOSS信息\n" +
                "list(查成员) 查询公会成员\n" +
                "remain(剩余刀) 查询所有剩余刀\n" +
                "tail(尾刀) 查询所有剩余刀\n" +
                "summ [name] (报告 [名称])\n" +
                "查询个人出刀(当日)\n" +
                "book [id] (预约 [序号])\n" +
                "订阅boss信息(上一个尾刀时at)\n" +
                "search1 [name] (查分 [名称])\n" +
                "search2 [rank] (查线 [分数])\n" +
                "yesterday(昨日排名)\n" +
                "rank(本期排名)\n" +
                "ranks [id] (历史排名[boss序号])\n" +
                "history (历史会战) 含boss序号\n" +
                "sync 立即刷新\n" +
                "wake 立即唤醒\n" +
                "cancel 终止主循环队列\n" +
                "sleep 立即休眠至五点\n" +
                "debug 打印变量到控制台");
    }

    @NickName("查刀")
    @GlobalNickName("查刀")
    @CommandMethod(Permission.GROUP_USER)
    public void check() {
        loop.getSummary(sent::sendMessage);
    }

    @NickName("剩余刀")
    @GlobalNickName("剩余刀")
    @CommandMethod(Permission.GROUP_USER)
    public void remain() {
        loop.getRemain(sent::sendMessage);
    }

    @NickName("尾刀")
    @GlobalNickName("尾刀")
    @CommandMethod(Permission.GROUP_USER)
    public void tail() {
        loop.getRemain(sent::sendMessage);
    }

    @NickName("now")
    @NickName("状态")
    @GlobalNickName("状态")
    @CommandMethod(Permission.GROUP_USER)
    public void info() {
        loop.getBossInfo(sent::sendMessage);
    }

    @NickName("查成员")
    @GlobalNickName("查成员")
    @CommandMethod(Permission.GROUP_USER)
    public void list() {
        loop.getAllUsers(sent::sendMessage);
    }

    @NickName("报告")
    @GlobalNickName("报告")
    @CommandMethod(Permission.GROUP_USER)
    public void summ(String name) {
        loop.getPersonalSummary(name, sent::sendMessage);
    }

    @NickName("预约")
    @GlobalNickName("预约")
    @CommandMethodEnvRequest(CommandMethodEnv.SEND)
    @CommandMethod(Permission.GROUP_USER)
    public void book(String id, Member send) {
        loop.bookBoss(Integer.parseInt(id), send.getId(), sent::sendMessage);
    }

    @NickName("历史会战")
    @GlobalNickName("历史会战")
    @CommandMethod(Permission.GROUP_USER)
    public void history() {
        loop.historyTerm(sent::sendMessage);
    }

    @NickName("昨日排名")
    @GlobalNickName("昨日排名")
    @CommandMethod(Permission.GROUP_USER)
    public void yesterday() {
        loop.yesterdayRank(sent::sendMessage);
    }

    @NickName("本期排名")
    @GlobalNickName("本期排名")
    @CommandMethod(Permission.GROUP_USER)
    public void rank() {
        loop.historyRankSync(sent::sendMessage);
    }

    @NickName("历史排名")
    @GlobalNickName("历史排名")
    @CommandMethod(Permission.GROUP_USER)
    public void ranks(String id) {
        loop.historyRankSync(Integer.parseInt(id), sent::sendMessage);
    }

    @NickName("查分")
    @GlobalNickName("查分")
    @CommandMethod(Permission.GROUP_USER)
    public void search1(String name) {
        loop.searchByName(name, sent::sendMessage);
    }

    @NickName("查线")
    @GlobalNickName("查线")
    @CommandMethod(Permission.GROUP_USER)
    public void search2(String name) {
        loop.searchByRank(name, sent::sendMessage);
    }

    @CommandMethod(Permission.DEVELOPER)
    public void sync() {
        sent.sendMessage(loop.sync());
    }

    @CommandMethod(Permission.DEVELOPER)
    public void wake() {
        sent.sendMessage(loop.wake());
    }

    @CommandMethod(Permission.DEVELOPER)
    public void cancel() {
        sent.sendMessage(loop.cancel());
    }

    @CommandMethod(Permission.DEVELOPER)
    public void sleep() {
        sent.sendMessage(loop.sleep());
    }

    @CommandMethod(Permission.DEVELOPER)
    public void debug() {
        sent.sendMessage(loop.debug());
    }

    @CommandMethod(Permission.DEVELOPER)
    public void dailyReport() {
        loop.testDailyReport();
    }

}
