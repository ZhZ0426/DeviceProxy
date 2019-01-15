package com.zyl.common;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class Constant {
    public static final AttributeKey<Channel> CHANNEL_ATTRIBUTE_KEY =
            AttributeKey.newInstance("CHANNEL_ATTRIBUTE_KEY");
    public static final int INT_SIZE = 4;
    public static final int BYTE_SIZE = 1;
    public static final int TRAN_SIZE = 2 * 1024 * 1024;
    public static final int MAX_FRAME_LENGTH = 4 * 1024 * 1024;
    public static final int LENGTH_FIELD_LENGTH = 4;
    public static final int LENGTH_FIELD_OFFSET = 1;
}
