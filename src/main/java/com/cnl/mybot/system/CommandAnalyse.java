package com.cnl.mybot.system;

public abstract class CommandAnalyse<T extends CommandAnalyse.Result<?>> {

    public T analyseCommand(String msg) {
        if (!msg.startsWith("!!")) return null;
        msg = msg.substring(2);
        if (msg.length() < 1) return null;
        String[] cmd = msg.split(" ");
        String args = msg.replace(cmd[0], "");
        return getResult(cmd, args);
    }

    protected abstract T getResult(String[] cmd, String args);

    public abstract static class Result<E> {

        public Result(E cmd, String args) {
            this.args = args;
            this.cmd = cmd;
        }

        public String arg0;
        public String arg1;
        public String arg2;
        public String args;
        public E cmd;
    }
}
