package com.cnl.mybot.system;

import org.jetbrains.annotations.Nullable;

public class SysCommandAnalyse extends CommandAnalyse<SysCommandAnalyse.Result> {

    public static final SysCommandAnalyse INSTANCE = new SysCommandAnalyse();

    @Override
    @Nullable
    protected Result getResult(String[] cmd, String args) {
        Result result = null;
        switch (cmd[0]) {
            case "reload":
                result = new Result(COMMAND.RELOAD_CONFIG, args);
                break;
            case "shutdown":
                result = new Result(COMMAND.SHUTDOWN, args);
                break;
        }
        return result;
    }

    public static class Result extends CommandAnalyse.Result<COMMAND> {
        public Result(COMMAND cmd, String args) {
            super(cmd, args);
        }
    }

    public enum COMMAND {
        RELOAD_CONFIG,
        SHUTDOWN
    }

}
