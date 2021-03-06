package com.zyl.server;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyl.coder.ProxyMessageDecoder;
import com.zyl.coder.ProxyMessageEncoder;
import com.zyl.common.Constant;
import com.zyl.handler.ClientHandler;
import com.zyl.handler.CollectionHandler;
import com.zyl.handler.IdleProxyHandler;
import com.zyl.handler.OutServerHandler;
import com.zyl.interfaces.Server;
import com.zyl.tools.ChannelCollection;
import com.zyl.tools.PropertiesTools;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.logging.InternalLogLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ProxyServer implements Server {

  private ServerBootstrap serverBootstrap;
  private ServerBootstrap innerServerBootstrap;
  private NioEventLoopGroup workGroup;
  private NioEventLoopGroup bossGroup;
  private int port;

  public ProxyServer(int port, int workThreadCount, int bossThreadCount) {
    this.port = port;
    workGroup = new NioEventLoopGroup(workThreadCount);
    bossGroup = new NioEventLoopGroup(bossThreadCount);
    innerServerBootstrap = new ServerBootstrap();
    serverBootstrap = new ServerBootstrap();
  }

  @Override
  public void start() {
    innerServerBootstrap
        .group(workGroup, bossGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel
                    .pipeline()
                    .addFirst(new CollectionHandler())
                    .addLast(
                        new ProxyMessageDecoder(
                            Constant.MAX_FRAME_LENGTH,
                            Constant.LENGTH_FIELD_OFFSET,
                            Constant.LENGTH_FIELD_LENGTH,
                            0,
                            0))
                    .addLast(new ProxyMessageEncoder())
                    .addLast(new LoggingHandler(LogLevel.ERROR))
                    .addLast(new IdleProxyHandler(60, 60, 120, TimeUnit.SECONDS))
                    .addLast(new OutServerHandler());
              }
            });
    try {
      innerServerBootstrap.bind(port).sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    serverBootstrap = new ServerBootstrap();
    serverBootstrap
        .group(workGroup, bossGroup)
        .channel(NioServerSocketChannel.class)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel serverSocketChannel) throws Exception {
                serverSocketChannel.pipeline().addLast(new ClientHandler());
              }
            });
    String server_client = PropertiesTools.getPropertiesName("server_client");
    JSONObject jsonObject = JSONObject.parseObject(server_client);
    for (Map.Entry entry : jsonObject.entrySet()) {
      ChannelCollection.putPort(entry.getKey().toString(), entry.getValue().toString());
      try {
        serverBootstrap.bind(Integer.parseInt(entry.getKey().toString())).get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void stop() {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  workGroup.shutdownGracefully();
                  bossGroup.shutdownGracefully();
                }));
  }
}
