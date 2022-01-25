package com.cnl.mybot.system;

public enum Permission {

    NOBODY(Integer.MAX_VALUE),
    CONSOLE(5),
    DEVELOPER(4),
    GROUP_OWNER(3),
    GROUP_ADMIN(2),
    GROUP_USER(1),
    EVERYONE(0);

    @Deprecated
    public int getLevel() {
        return level;
    }

    int level = 0;

    public boolean hasPermission(Permission target) {
        return level >= target.level;
    }

    Permission(int level) {
        this.level = level;
    }

}
