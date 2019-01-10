package com.zyl.handler;

import com.zyl.common.Constant;
import com.zyl.tools.ChannelCollection;
import com.zyl.tools.StatisticsCollection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.InetSocketAddress;

public class CollectionHandler extends ChannelDuplexHandler {
  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
      throws Exception {
    super.write(ctx, msg, promise);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    Channel channel = ctx.channel();
    Channel realChannel = channel.attr(Constant.CHANNEL_ATTRIBUTE_KEY).get();
    if (null != realChannel) {
      InetSocketAddress inetSocketAddress = (InetSocketAddress) realChannel.localAddress();
      int port = inetSocketAddress.getPort();
      StatisticsCollection.addBytes(port, ((ByteBuf) msg).readableBytes());
    }
    super.channelRead(ctx, msg);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().localAddress();
    super.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().localAddress();
    ChannelCollection.removePort("904e91404fc7");
    super.channelInactive(ctx);
  }
}
