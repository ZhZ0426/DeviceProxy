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

public class ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    Channel userChannel = ctx.channel();
    InetSocketAddress inetSocketAddress = (InetSocketAddress) userChannel.localAddress();
    Channel proxyChannel = ChannelCollection.getChannelByPort(inetSocketAddress.getPort());
    proxyChannel.attr(Constant.CHANNEL_ATTRIBUTE_KEY).set(userChannel);
    userChannel.attr(Constant.CHANNEL_ATTRIBUTE_KEY).set(proxyChannel);
    userChannel.config().setAutoRead(false);
    if (null == proxyChannel) {
      userChannel.close();
    } else {
      Message message = new Message();
      message.setType(MessageType.LOGIN);
      message.setSignLength(0);
      message.setSignData(new byte[] {});
      message.setDataLength(12);
      message.setData("deviceID".getBytes());
      proxyChannel.writeAndFlush(message);
    }
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    super.channelInactive(ctx);
  }

  @Override
  public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    super.channelWritabilityChanged(ctx);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf)
      throws Exception {
    Channel comeChannel = channelHandlerContext.channel();
    Channel channel = comeChannel.attr(Constant.CHANNEL_ATTRIBUTE_KEY).get();
    Message message = new Message();
    message.setType(MessageType.TRAN);
    message.setSignData("".getBytes());
    message.setSignLength("".getBytes().length);
    int length = byteBuf.readableBytes();
    //   System.out.println("服务端数据长度"+length);
    message.setDataLength(length + 4);
    byte[] bytes = new byte[length];
    byteBuf.readBytes(bytes);
    message.setData(bytes);
    //     System.out.println(new String(message.getData()));
    channel.writeAndFlush(message);
  }
}
