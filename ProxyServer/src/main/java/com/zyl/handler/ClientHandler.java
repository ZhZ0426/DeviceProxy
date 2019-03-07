package com.zyl.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyl.common.Constant;
import com.zyl.common.Message;
import com.zyl.common.MessageType;
import com.zyl.tools.ChannelCollection;
import com.zyl.tools.NetTool;
import com.zyl.tools.PropertiesTools;
import com.zyl.tools.ZookeeperClient;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<Message> {

    private static final String ZK_ADDRESS = PropertiesTools.getPropertiesName("zk_ip");
    private static final String ZK_PATH = "/proxy/available";
    private ServerBootstrap serverBootstrap;

    public ClientHandler(ServerBootstrap serverBootstrap) {
        this.serverBootstrap = serverBootstrap;
    }

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
            case MessageType.DIS_CONNECT:
                handlerDisConnect(channelHandlerContext, message);
                break;
            default:
                break;
        }
    }

    private void handlerBeat(ChannelHandlerContext channelHandlerContext, Message message) {
    }

    private void handlerLogin(ChannelHandlerContext channelHandlerContext, Message message) {
        String sign = new String(message.getSignData());
        if (null == ChannelCollection.getPort(sign)) {
            int port = 0;
            try {
                ServerSocket serverSocket = new ServerSocket(0);
                port = serverSocket.getLocalPort();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChannelCollection.putChannel(sign, channelHandlerContext.channel());
            /**
             * 添加到zk
             */
            CuratorFramework client = ZookeeperClient.getInstance(ZK_ADDRESS);
            try {
                byte[] bytes = client.getData().forPath(ZK_PATH);
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ip", NetTool.getLocalIP());
                jsonObject.put("port", port);
                jsonObject.put("sign", sign);
                if (bytes.length > 0 && !StringUtils.isNotBlank(new String(bytes))) {
                    jsonArray = JSONArray.parseArray(new String(bytes));
                }
                jsonArray.add(jsonObject);
                client.setData().forPath(ZK_PATH, jsonArray.toString().getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ChannelCollection.putPort(sign, port);
            InetSocketAddress inetSocketAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
            System.out.println("远程ip为" + inetSocketAddress.getHostName());
            ChannelCollection.putIp(inetSocketAddress.getHostName(), sign);
            try {
                serverBootstrap.bind(port).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
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


    private void handlerDisConnect(ChannelHandlerContext channelHandlerContext, Message message) {
        String sign = new String(message.getSignData());
        ChannelCollection.removePort(sign);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开了！！！！！！！！！！！！！！");
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        ChannelCollection.removeIp(socketAddress.getHostName());
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
