package com.zyl.handler;

import com.zyl.common.Message;
import com.zyl.common.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class IdleProxyHandler extends IdleStateHandler {

  public IdleProxyHandler(long readerIdleTime, long writerIdleTime, long allIdleTime,
      TimeUnit unit) {
    super(readerIdleTime, writerIdleTime, allIdleTime, unit);
  }

  @Override
  protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
    if (IdleState.READER_IDLE == evt.state()) {
      System.out.println("To proxyServer Read Timeout");
      Message message = new Message();
      message.setType(MessageType.BEAT);
      ctx.channel().writeAndFlush(message);
    } else if (IdleState.WRITER_IDLE == evt.state()) {
      System.out.println("To proxyServer Write Timeout");
      Message message = new Message();
      message.setType(MessageType.BEAT);
      ctx.channel().writeAndFlush(message);
    }
    super.channelIdle(ctx, evt);
  }
}
