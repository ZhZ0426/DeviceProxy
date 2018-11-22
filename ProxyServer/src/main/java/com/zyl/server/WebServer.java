package com.zyl.server;

import com.zyl.handler.WebHandler;
import com.zyl.http.Controller;
import com.zyl.http.ControllerManager;
import com.zyl.http.GET;
import com.zyl.http.Rest;
import com.zyl.interfaces.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WebServer implements Server {

  private String basePackage = "com.zyl.http";
  private int port;
  private EventLoopGroup bossGroup;
  private EventLoopGroup workerGroup;
  private ServerBootstrap b;

  public WebServer(int port) {
    this.port = port;
    bossGroup = new NioEventLoopGroup();
    workerGroup = new NioEventLoopGroup();
    b = new ServerBootstrap();
    b.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 1024)
        .childHandler(new WebHandler()).option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true);
    Set<Class<?>> requests = new Reflections(basePackage).getTypesAnnotatedWith(Rest.class);
    for (Class<?> cls : requests) {
      Rest rest = cls.getAnnotation(Rest.class);
      String restName = rest.value();
      Method[] methods = cls.getMethods();
      Controller controller = new Controller();
      controller.setClassName(cls.getName());
      Map<String, String> map = new HashMap<String, String>();
      for (Method method : methods) {
        GET get = method.getAnnotation(GET.class);
        if (null != get) {
          map.put(get.value(), method.getName());
        }
      }
      controller.setMap(map);
      ControllerManager.putController(restName, controller);
    }
  }

  @Override
  public void start() {
    try {
      ChannelFuture f = b.bind(port).sync();
      //          f.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void stop() {
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }
}
