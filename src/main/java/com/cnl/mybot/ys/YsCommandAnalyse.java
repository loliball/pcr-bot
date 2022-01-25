package com.cnl.mybot.ys;

import com.cnl.mybot.system.CommandAnalyse;
import org.jetbrains.annotations.Nullable;

public class YsCommandAnalyse extends CommandAnalyse<YsCommandAnalyse.Result> {

    public static final YsCommandAnalyse INSTANCE = new YsCommandAnalyse();

    @Override
    @Nullable
    protected Result getResult(String[] cmd, String args) {
        Result result = null;
        switch (cmd[0]) {
            case "yshelp":
                result = new Result(COMMAND.YS_HELP, args);
                break;
            case "ysinfo":
                result = new Result(COMMAND.YS_INFO, args);
                if (cmd.length > 1) result.arg0 = cmd[1];
                break;
            case "checkin":
                result = new Result(COMMAND.YS_CHECK_IN, args);
                break;
            case "update":
                result = new Result(COMMAND.YS_CHECK_IN_UPDATE, args);
                break;
            case "abyss1":
                result = new Result(COMMAND.YS_INFO_ABYSS, args);
                result.arg1 = "1";
                if (cmd.length > 1) result.arg0 = cmd[1];
                break;
            case "abyss2":
                result = new Result(COMMAND.YS_INFO_ABYSS, args);
                result.arg1 = "2";
                if (cmd.length > 1) result.arg0 = cmd[1];
                break;
        }
        return result;
    }

    public static class Result extends CommandAnalyse.Result<YsCommandAnalyse.COMMAND> {
        public Result(COMMAND cmd, String args) {
            super(cmd, args);
        }
    }

    public enum COMMAND {
        YS_HELP,
        YS_INFO,
        YS_CHECK_IN,
        YS_CHECK_IN_UPDATE,
        YS_INFO_ABYSS,
        YS_CHECK_IN_ADD_NEW
    }

}
