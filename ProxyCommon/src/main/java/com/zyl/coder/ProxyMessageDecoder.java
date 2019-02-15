package com.zyl.coder;

import com.zyl.common.Constant;
import com.zyl.common.Message;
import com.zyl.common.MessageType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class ProxyMessageDecoder extends LengthFieldBasedFrameDecoder {

    public ProxyMessageDecoder(int maxFrameLength,
            int lengthFieldOffset,
            int lengthFieldLength,
            int lengthAdjustment,
            int initialBytesToStrip) {
        super(
                maxFrameLength,
                lengthFieldOffset,
                lengthFieldLength,
                lengthAdjustment,
                initialBytesToStrip);
    }

    @Override
    protected Message decode(ChannelHandlerContext ctx, ByteBuf byteIn) throws Exception {
        ByteBuf in = (ByteBuf) super.decode(ctx, byteIn);
        if (null == in) {
            return null;
        }
        if (in.readableBytes() <= (Constant.INT_SIZE + Constant.BYTE_SIZE)) {
            return null;
        }
        Message message = new Message();
        byte type = in.readByte();
        message.setType(type);
        int dataLength = in.readInt();
        if (type == MessageType.LOGIN || type == MessageType.TRAN) {
            int signLength = in.readInt();
            byte[] sign = new byte[signLength];
            in.readBytes(sign);
            message.setSignData(sign);
            byte[] data = new byte[dataLength - Constant.INT_SIZE - signLength];
            in.readBytes(data);
            message.setData(data);
        } else {
            byte[] data = new byte[dataLength];
            in.readBytes(data);
            message.setData(data);
        }
        message.setDataLength(dataLength);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
