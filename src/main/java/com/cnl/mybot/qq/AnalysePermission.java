package com.cnl.mybot.qq;

import com.cnl.mybot.system.Permission;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.MemberPermission;


public class AnalysePermission {

    public static Permission getPermission(Member mb, long dev) {
        MemberPermission mp = mb.getPermission();
        if (mb.getId() == dev) return Permission.DEVELOPER;
        switch (mp) {
            case OWNER:
                return Permission.GROUP_OWNER;
            case ADMINISTRATOR:
                return Permission.GROUP_ADMIN;
            case MEMBER:
                return Permission.GROUP_USER;
        }
        return Permission.EVERYONE;
    }

}
