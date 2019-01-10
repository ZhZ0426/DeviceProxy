package com.zyl.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class WebHandler extends ChannelInitializer<SocketChannel> {
  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    socketChannel
        .pipeline()
        .addLast(new HttpResponseEncoder())
        .addLast(new HttpRequestDecoder())
        .addLast(new HttpObjectAggregator(65535))
        .addLast(new HttpChannelHandler());
  }
}
