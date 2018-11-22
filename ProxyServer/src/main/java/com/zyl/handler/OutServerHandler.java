package com.zyl.handler;

import com.zyl.common.Constant;
import com.zyl.common.Message;
import com.zyl.common.MessageType;
import com.zyl.tools.ChannelCollection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

public class OutServerHandler extends SimpleChannelInboundHandler<Message> {

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message)
      throws Exception {
    switch (message.getType()) {
      case MessageType.LOGIN:
        handlerLogin(channelHandlerContext, message);
        break;
      case MessageType.BEAT:
        handlerBeat(channelHandlerContext, message);
        break;
      case MessageType.TRAN:
        handlerTran(channelHandlerContext, message);
        break;
      case MessageType.RE_TRAN:
        handlerReTran(channelHandlerContext, message);
        break;
      default:
        break;
    }

  }

  private void handlerBeat(ChannelHandlerContext channelHandlerContext, Message message) {
  }

  private void handlerLogin(ChannelHandlerContext channelHandlerContext, Message message) {
    InetSocketAddress inetSocketAddress = (InetSocketAddress) channelHandlerContext.channel()
        .localAddress();
    String sign = new String(message.getSignData());
    if (null == ChannelCollection.getPort(sign)) {
      ChannelCollection
          .putPort(sign, "" + inetSocketAddress.getPort(), channelHandlerContext.channel());
    } else {
      ChannelCollection.getPort(sign).config().setAutoRead(true);
    }
  }

  private void handlerTran(ChannelHandlerContext channelHandlerContext, Message message) {
    Channel channel = channelHandlerContext.channel();
    Channel userChannel = channel.attr(Constant.CHANNEL_ATTRIBUTE_KEY).get();
    if (userChannel != null) {
      ByteBuf byteBuf = channelHandlerContext.alloc().buffer(message.getDataLength());
      byteBuf.writeBytes(message.getData());
      userChannel.writeAndFlush(byteBuf);
      userChannel.config().setAutoRead(true);
    }

  }

  private void handlerReTran(ChannelHandlerContext channelHandlerContext, Message message) {

  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("断开了！！！！！！！！！！！！！！");
    Channel channel = ctx.channel().attr(Constant.CHANNEL_ATTRIBUTE_KEY).get();
    InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.localAddress();
    int port = inetSocketAddress.getPort();
    ChannelCollection.removeByPort("" + port);
    super.channelInactive(ctx);
  }

  @Override
  public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    super.channelWritabilityChanged(ctx);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
  }


}
