package com.zyl;

import com.zyl.interfaces.Server;
import com.zyl.server.ProxyServer;
import com.zyl.server.WebServer;
import com.zyl.tools.PropertiesTools;
import com.zyl.tools.ServerCollection;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.util.Arrays;

public class ServerApplication {

  static {
    InternalLoggerFactory.setDefaultFactory(new Log4JLoggerFactory());
  }

  public static void main(String[] args) {
    int serverPort = Integer.parseInt(PropertiesTools.getPropertiesName("server_port"));
    ServerCollection serverCollection = new ServerCollection(
        Arrays.asList(new Server[]{new ProxyServer(serverPort, 10, 10), new WebServer(9999)}));
    serverCollection.startServer();
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      serverCollection.stopServer();
    }));
    InternalLogger logger = InternalLoggerFactory.getInstance(ServerApplication.class);
    logger.info("服务端已启动");
    System.out.println("服务端已启动");
  }
}

