package com.zyl.client;

import com.zyl.coder.ProxyMessageDecoder;
import com.zyl.coder.ProxyMessageEncoder;
import com.zyl.common.Constant;
import com.zyl.common.Message;
import com.zyl.common.MessageType;
import com.zyl.handler.IdleProxyHandler;
import com.zyl.handler.RealChannelHandler;
import com.zyl.handler.ServerChannelHandler;
import com.zyl.interfaces.Client;
import com.zyl.tools.ChannelManager;
import com.zyl.tools.ClientCollection;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

public class ProxyClient implements Client {

    private String host;
    private int port;
    private Bootstrap bootstrap;
    private Bootstrap localBootstrap;
    private EventLoopGroup localGroup;
    private EventLoopGroup group;
    private int reconnectTime = 1;

    public ProxyClient(String host, int port) {
        this.host = host;
        this.port = port;
        localBootstrap = new Bootstrap();
        bootstrap = new Bootstrap();
        localGroup = new NioEventLoopGroup();
        localBootstrap
                .group(localGroup)
                .channel(NioSocketChannel.class)
                .handler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(new RealChannelHandler());
                            }
                        });

        // 与Server通讯的
        group = new NioEventLoopGroup();
        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(host, port)
                .handler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel
                                        .pipeline()
                                        .addLast(
                                                new ProxyMessageDecoder(
                                                        Constant.MAX_FRAME_LENGTH,
                                                        Constant.LENGTH_FIELD_OFFSET,
                                                        Constant.LENGTH_FIELD_LENGTH,
                                                        0,
                                                        0))
                                        .addLast(new ProxyMessageEncoder())
                                        .addLast(new IdleProxyHandler(60, 60, 120, TimeUnit.SECONDS))
                                        .addLast(new ServerChannelHandler(localBootstrap, ProxyClient.this));
                            }
                        });
    }

    @Override
    public void start() {
        startConnectServer();
    }

    public void startConnectServer() {
        bootstrap
                .connect()
                .addListener(
                        (ChannelFutureListener)
                                channelFuture -> {
                                    System.out.println("连接服务端中……");
                                    if (channelFuture.isSuccess()) {
                                        ChannelManager.setChannel(channelFuture.channel());
                                        Message message = new Message();
                                        message.setType(MessageType.LOGIN);
                                        String sign = ClientCollection.gwId;
                                        int signLength = sign.getBytes().length;
                                        message.setDataLength(signLength + 4);
                                        message.setSignLength(signLength);
                                        message.setSignData(sign.getBytes());
                                        message.setData(new byte[]{});
                                        channelFuture.channel().writeAndFlush(message);
                                        System.out.println("连接成功");
                                    } else {
                                        System.out.println("连接超时，" + reconnectTime + "秒后重新连接……");
                                        Thread.sleep(
                                                reconnectTime >= 10 ? (reconnectTime * 1000) : (reconnectTime++) * 1000);
                                        startConnectServer();
                                    }
                                });
    }

    @Override
    public void stop() {
        Runtime.getRuntime().addShutdownHook(
                        new Thread(() -> {
                                    localGroup.shutdownGracefully();
                                    group.shutdownGracefully();
                                }));
    }
}
