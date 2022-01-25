package com.cnl.mybot.system;

import com.cnl.mybot.annotation.GlobalNickName;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class GlobalCommandInvoker extends CommandInvoker {

    public GlobalCommandInvoker(long filterGroup, Consumer<String> errorOutput, CommandClass... functions) {
        super(filterGroup, errorOutput, functions);
    }

    @Override
    public void invoke(GroupMessageEvent event) {
        InvokeResult result = call(event);
        switch (result) {
            case SUCCESS:
            case NOT_TARGET_GROUP:
            case NOT_TARGET_MESSAGE_TYPE:
            case NOT_TARGET_PREFIX:
            case COMMAND_TOO_SHORT:
            case NOT_FOUNT_METHOD:
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

    @NotNull
    @Override
    protected String[] getNames(Method method) {
        if (method.isAnnotationPresent(GlobalNickName.class)) {
            GlobalNickName nickName = method.getAnnotation(GlobalNickName.class);
            return new String[]{nickName.value()};
        }
        return new String[0];
    }

    @Nullable
    @Override
    protected String unwrapPrefix(String msg) {
        return msg;
    }
}
