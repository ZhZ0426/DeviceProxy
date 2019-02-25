package com.zyl.handler;

import com.zyl.common.Constant;
import com.zyl.common.Message;
import com.zyl.common.MessageType;
import com.zyl.interfaces.Client;
import com.zyl.tools.ChannelManager;
import com.zyl.tools.ClientCollection;
import com.zyl.tools.PropertiesTools;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerChannelHandler extends SimpleChannelInboundHandler<Message> {

    private Bootstrap realBootstrap;
    private Client client;
    private int reconnectTime = 1;
    private String realIp;

    public ServerChannelHandler() {
        super();
        realIp = PropertiesTools.getPropertiesName("real_ip");
    }

    public ServerChannelHandler(Bootstrap bootstrap, Client client) {
        this.realBootstrap = bootstrap;
        this.client = client;
        realIp = PropertiesTools.getPropertiesName("real_ip");
        if(null == realIp ){
            realIp = getLocalIP();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务端断开了");
        client.start();
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message)
            throws Exception {
        switch (message.getType()) {
            case MessageType.BEAT:
                handleBeat(channelHandlerContext, message);
                break;
            case MessageType.LOGIN:
                handleLogin(channelHandlerContext, message);
                break;
            case MessageType.TRAN:
                handleTran(channelHandlerContext, message);
                break;
            default:
                break;
        }
    }

    public void handleBeat(ChannelHandlerContext channelHandlerContext, Message message) {
    }

    public void handleLogin(ChannelHandlerContext channelHandlerContext, Message message) {
        realBootstrap
                .connect(realIp, 22)
                .addListener(
                        (ChannelFutureListener) channelFuture -> {
                            System.out.println("连接真实服务端中………………");
                            if (channelFuture.isSuccess()) {
                                System.out.println("连接真实服务端成功");
                                ChannelManager.setRealChannel(channelFuture.channel());
                                channelHandlerContext.channel()
                                        .attr(Constant.CHANNEL_ATTRIBUTE_KEY)
                                        .set(channelFuture.channel());
                                channelFuture
                                        .channel()
                                        .attr(Constant.CHANNEL_ATTRIBUTE_KEY)
                                        .set(channelHandlerContext.channel());
                                String sign = ClientCollection.gwId;
                                Message mess = new Message();
                                mess.setType(MessageType.LOGIN);
                                mess.setSignLength(sign.getBytes().length);
                                mess.setSignData(sign.getBytes());
                                mess.setDataLength(9 + sign.getBytes().length);
                                mess.setData("start".getBytes());
                                channelHandlerContext.channel().writeAndFlush(mess);
                            } else {
                                System.out.println("连接目的超时，" + reconnectTime + "秒后重新连接……");
                                Thread.sleep(
                                        reconnectTime >= 10 ? (reconnectTime * 1000) : (reconnectTime++) * 1000);
                                handleLogin(channelHandlerContext, message);
                            }
                        });
    }

    public void handleTran(ChannelHandlerContext channelHandlerContext, Message message) {
        Channel channel = channelHandlerContext.channel().attr(Constant.CHANNEL_ATTRIBUTE_KEY).get();
        ByteBuf byteBuf = channelHandlerContext.alloc().buffer(message.getDataLength());
        byteBuf.writeBytes(message.getData());
        channel.writeAndFlush(byteBuf);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        Channel realChannel = ctx.channel().attr(Constant.CHANNEL_ATTRIBUTE_KEY).get();
        if (realChannel != null) {
            realChannel.config().setOption(ChannelOption.AUTO_READ, ctx.channel().isWritable());
        }
        super.channelWritabilityChanged(ctx);
    }

    public static String getLocalIP() {
        String sIP = "";
        InetAddress ip = null;
        try {
            boolean bFindIP = false;
            Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                    .getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP) {
                    break;
                }
                NetworkInterface ni = (NetworkInterface) netInterfaces
                        .nextElement();

                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = (InetAddress) ips.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().matches(
                            "(\\d{1,3}\\.){3}\\d{1,3}")) {
                        bFindIP = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
           // OutUtil.error(IpUtil.class, e.getMessage());
        }
        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        return sIP;
    }

}
