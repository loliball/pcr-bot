package com.cnl.mybot.qq;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.FlashImage;
import net.mamoe.mirai.message.data.ForwardMessage;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;

public class MessageForward {

    public static void forwardG2G(Bot bot, int from, int to) {
        Group group = bot.getGroup(to);
        if (group == null) throw new RuntimeException("group is null");
        bot.getEventChannel().subscribeAlways(GroupMessageEvent.class, event -> {
            Group src = event.getGroup();
            Member send = event.getSender();
            MessageChain msg = event.getMessage();
            if (src.getId() == from) {
                msg.forEach(sMsg -> {
                    if (sMsg instanceof Image) {
                        group.sendMessage(sMsg);
                    } else if (sMsg instanceof FlashImage) {
                        FlashImage fi = (FlashImage) sMsg;
                        MessageReceipt<Group> receipt = group.sendMessage(fi.getImage());
                        receipt.quoteReply("FlashImage");
                    } else if (sMsg instanceof ForwardMessage) {
                        group.sendMessage(sMsg);
                    }
                });
            }
        });
    }

}
