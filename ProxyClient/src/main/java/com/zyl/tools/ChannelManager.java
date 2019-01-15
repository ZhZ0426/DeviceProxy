package com.zyl.tools;

import io.netty.channel.Channel;

public class ChannelManager {

    public static Channel channel;

    public static Channel Realchannel;

    public static Channel getChannel() {
        return channel;
    }

    public static void setChannel(Channel cha) {
        channel = cha;
    }

    public static Channel getRealChannel() {
        return Realchannel;
    }

    public static void setRealChannel(Channel cha) {
        Realchannel = cha;
    }
}
