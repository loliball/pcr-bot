package com.cnl.mybot.ys.signin;

public class Logger {

    public static Logger getLogger(Class<?> cls) {
        return new Logger();
    }

    private void log(String s) {
        System.out.println(s);
    }

    public void info(String str) {
        log(str);
    }

    public void error(String str) {
        log(str);
    }
}
