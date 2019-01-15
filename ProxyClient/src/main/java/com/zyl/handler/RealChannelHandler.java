package com.zyl.handler;

import com.zyl.common.Message;
import com.zyl.common.MessageType;
import com.zyl.tools.ChannelManager;
import com.zyl.tools.ClientCollection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RealChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf)
            throws Exception {
        Channel channel = ChannelManager.getChannel();
        int length = byteBuf.readableBytes();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        Message message = new Message();
        message.setType(MessageType.TRAN);
        String sign = ClientCollection.gwId;
        message.setSignData(sign.getBytes());
        message.setSignLength(sign.getBytes().length);
        message.setDataLength(length + 4 + sign.getBytes().length);
        message.setData(bytes);
        channel.writeAndFlush(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
