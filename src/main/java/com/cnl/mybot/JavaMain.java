package com.cnl.mybot;

import com.cnl.mybot.pcr.team.PcrCommandClassImpl;
import com.cnl.mybot.pcr.team.TeamWarLooper;
import com.cnl.mybot.qq.QQBot;
import com.cnl.mybot.system.CommandInvoker;
import com.cnl.mybot.system.ConfigLoader;
import com.cnl.mybot.system.GlobalCommandInvoker;
import com.cnl.mybot.system.LogRedirect;
import com.cnl.mybot.system.SysCommandClassImpl;
import com.cnl.mybot.ys.YsCommandClassImpl;
import com.cnl.mybot.ys.signin.SignInLooper;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;

public class JavaMain {

    private static ConfigLoader config;

    public static void main(String[] args) throws Exception {
        config = ConfigLoader.getInstance();
        LogRedirect.redirect();
        init(args);
    }

    public static void init(String[] args) {
        Bot bot = QQBot.create(config.getProp("qq.id"), config.getProp("qq.pwd"));

        Group ghz = bot.getGroup(Long.parseLong(config.getProp("qq.group")));   //会战群
        if (ghz == null) return;

        SignInLooper sl = new SignInLooper(System.out::println);
        TeamWarLooper tw = new TeamWarLooper(ghz::sendMessage, ghz::sendMessage);
        CommandInvoker ghzInvoker = new CommandInvoker(ghz.getId(), ghz::sendMessage,
                new SysCommandClassImpl(),
                new PcrCommandClassImpl(ghz::sendMessage, tw),
                new YsCommandClassImpl(ghz::sendMessage, sl));
        GlobalCommandInvoker globalCommandInvoker = new GlobalCommandInvoker(ghz.getId(), ghz::sendMessage,
                new PcrCommandClassImpl(ghz::sendMessage, tw));
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, ghzInvoker::invoke);
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, globalCommandInvoker::invoke);
        bot.join();
    }
}