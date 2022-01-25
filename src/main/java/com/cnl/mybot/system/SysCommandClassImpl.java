package com.cnl.mybot.system;

import com.cnl.mybot.annotation.CommandClassAnnotation;
import com.cnl.mybot.annotation.CommandMethod;
import com.cnl.mybot.annotation.NickName;

@CommandClassAnnotation("system")
public class SysCommandClassImpl implements CommandClass {

    @NickName("close")
    @CommandMethod(Permission.DEVELOPER)
    public static void shutdown() {
        System.exit(0);
    }

    @NickName("config")
    @CommandMethod(Permission.DEVELOPER)
    public static void reload() {
        ConfigLoader.getInstance().reload();
        System.out.println("config reload success");
    }

}
