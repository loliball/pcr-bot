package com.cnl.mybot.qq;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

public class QQBot {

    private static Bot bot;

    public static Bot create(String qq, String pwd) {
        if (bot == null) {
            bot = BotFactory.INSTANCE.newBot(Long.parseLong(qq), pwd, new BotConfiguration() {{
                String folder = System.getProperty("user.dir");
                fileBasedDeviceInfo(new File(folder, "device.json").getAbsolutePath());
                setProtocol(MiraiProtocol.ANDROID_WATCH);
            }});
            bot.login();
        }
        return bot;
    }

}
