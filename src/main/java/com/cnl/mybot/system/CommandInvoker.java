package com.cnl.mybot.system;

import com.cnl.mybot.annotation.CommandMethod;
import com.cnl.mybot.annotation.CommandMethodEnv;
import com.cnl.mybot.annotation.CommandMethodEnvRequest;
import com.cnl.mybot.annotation.NickName;
import com.cnl.mybot.annotation.NickNames;
import com.cnl.mybot.qq.AnalysePermission;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class CommandInvoker {

    protected final Consumer<String> errorOutput;
    private final long groupId;
    private final HashMap<Class<?>, MethodBean[]> methodList;
    private final long owner;

    public CommandInvoker(long filterGroup, Consumer<String> errorOutput, CommandClass... functions) {
        ConfigLoader config = ConfigLoader.getInstance();
        owner = Long.parseLong(config.getProp("qq.owner"));
        groupId = filterGroup;
        this.errorOutput = errorOutput;
        methodList = new HashMap<>(functions.length);
        for (CommandClass object : functions) {
            addList(object, object.getClass());
        }
    }

    private void addList(CommandClass object, Class<?> clazz) {
        List<MethodBean> methods = new ArrayList<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(CommandMethod.class)) continue;
            CommandMethodEnv[] extern = new CommandMethodEnv[0];
            if (method.isAnnotationPresent(CommandMethodEnvRequest.class)) {
                extern = method.getAnnotation(CommandMethodEnvRequest.class).value();
            }
            methods.add(new MethodBean(object, method, getNames(method),
                    method.getAnnotation(CommandMethod.class).value(), extern));
        }
        methodList.put(clazz, methods.toArray(new MethodBean[0]));
    }

    @NotNull
    protected String[] getNames(Method method) {
        List<String> nameList = new ArrayList<>();
        nameList.add(method.getName());
        if (method.isAnnotationPresent(NickNames.class)) {
            NickName[] nickNames = method.getAnnotationsByType(NickName.class);
            for (NickName name : nickNames) {
                nameList.add(name.value());
            }
        } else if (method.isAnnotationPresent(NickName.class)) {
            NickName nickName = method.getAnnotation(NickName.class);
            nameList.add(nickName.value());
        }
        return nameList.toArray(new String[0]);
    }

    public void invoke(GroupMessageEvent event) {
        InvokeResult result = call(event);
        switch (result) {
            case SUCCESS:
            case NOT_TARGET_GROUP:
            case NOT_TARGET_MESSAGE_TYPE:
            case NOT_TARGET_PREFIX:
                break;
            case COMMAND_TOO_SHORT:
                System.out.println("COMMAND_TOO_SHORT " + event);
                errorOutput.accept("COMMAND_TOO_SHORT");
                break;
            case NOT_FOUNT_METHOD:
                System.out.println("NOT_FOUNT_METHOD " + event);
                errorOutput.accept("NOT_FOUNT_METHOD");
                break;
            case METHOD_ARGS_COUNT_NOT_MATCH:
                System.out.println("METHOD_ARGS_COUNT_NOT_MATCH " + event);
                errorOutput.accept("METHOD_ARGS_COUNT_NOT_MATCH");
                break;
            case PERMISSION_DENIED:
                System.out.println("PERMISSION_DENIED " + event);
                errorOutput.accept("PERMISSION_DENIED");
                break;
            case FUNCTION_CALL_EXCEPTION:
                System.out.println("FUNCTION_CALL_EXCEPTION " + event);
                errorOutput.accept("FUNCTION_CALL_EXCEPTION");
                break;
        }
    }

    public InvokeResult call(GroupMessageEvent event) {
        Group src = event.getGroup();
        Member send = event.getSender();
        MessageChain root = event.getMessage();
        if (src.getId() != groupId) return InvokeResult.NOT_TARGET_GROUP;
        SingleMessage rawMsg = root.get(1);
        if (!(rawMsg instanceof PlainText)) return InvokeResult.NOT_TARGET_MESSAGE_TYPE;
        String msg = ((PlainText) rawMsg).getContent();
        msg = unwrapPrefix(msg);
        if (msg == null) return InvokeResult.NOT_TARGET_PREFIX;
        if (msg.length() < 1) return InvokeResult.COMMAND_TOO_SHORT;
        String[] cmd = msg.split(" ");
        MethodBean method = findMethodByName(cmd[0]);
        if (method == null) return InvokeResult.NOT_FOUNT_METHOD;
        if (!checkPermission(send, method.permission)) return InvokeResult.PERMISSION_DENIED;
        int argc = cmd.length - 1 + method.extern.length;
        if (argc != method.method.getParameterCount()) return InvokeResult.METHOD_ARGS_COUNT_NOT_MATCH;
        Object[] args = new Object[argc];
        System.arraycopy(cmd, 1, args, 0, cmd.length - 1);
        int offset = cmd.length - 1;
        for (CommandMethodEnv env : method.extern) {
            switch (env) {
                case GROUP:
                    args[offset] = src;
                    break;
                case SEND:
                    args[offset] = send;
                    break;
                case RAW_MESSAGE:
                    args[offset] = cmd;
                    break;
                case MESSAGE_CHAIN:
                    args[offset] = root;
                    break;
            }
            offset++;
        }
        try {
            method.method.invoke(method.object, args);
            return InvokeResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return InvokeResult.FUNCTION_CALL_EXCEPTION;
        }
    }

    @Nullable
    protected String unwrapPrefix(String msg) {
        if (!msg.startsWith("!!") && !msg.startsWith("！！")) return null;
        return msg.substring(2);
    }

    private boolean checkPermission(Member member, Permission target) {
        Permission pm = AnalysePermission.getPermission(member, owner);
        return pm.hasPermission(target);
    }

    private MethodBean findMethodByName(String target) {
        AtomicReference<MethodBean> result = new AtomicReference<>(null);
        methodList.forEach((key, val) -> {
            if (result.get() != null) return;
            for (MethodBean method : val) {
                for (String name : method.names) {
                    if (name.equals(target)) {
                        result.set(method);
                        return;
                    }
                }
            }
        });
        return result.get();
    }

    private static class MethodBean {
        CommandClass object;
        Method method;
        String[] names;
        Permission permission;
        CommandMethodEnv[] extern;

        public MethodBean() {
        }

        public MethodBean(CommandClass object, Method method, String[] names, Permission permission) {
            this(object, method, names, permission, new CommandMethodEnv[0]);
        }

        public MethodBean(CommandClass object, Method method, String[] names, Permission permission, CommandMethodEnv[] extern) {
            this.object = object;
            this.method = method;
            this.names = names;
            this.permission = permission;
            this.extern = extern;
        }
    }

    public enum InvokeResult {
        SUCCESS,
        NOT_TARGET_GROUP,
        NOT_TARGET_MESSAGE_TYPE,
        NOT_TARGET_PREFIX,
        COMMAND_TOO_SHORT,
        NOT_FOUNT_METHOD,
        METHOD_ARGS_COUNT_NOT_MATCH,
        PERMISSION_DENIED,
        FUNCTION_CALL_EXCEPTION
    }

}
