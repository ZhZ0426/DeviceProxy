package com.zyl;

import com.zyl.interfaces.Server;
import com.zyl.server.ProxyServer;
import com.zyl.server.RegistyServer;
import com.zyl.tools.PropertiesTools;
import com.zyl.tools.ServerCollection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class ServerApplication {

    private static Logger logger = LogManager.getLogger(ServerApplication.class);

    public static void main(String[] args) {
        int serverPort = Integer.parseInt(PropertiesTools.getPropertiesName("server_port"));
        ServerCollection serverCollection =
                new ServerCollection(
                        Arrays.asList(new Server[]{new ProxyServer(serverPort, 10, 10), new RegistyServer()}));
        //  new WebServer(9999, "com.zyl.controller").start();
        serverCollection.startServer();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> serverCollection.stopServer()));
        logger.info("服务端已启动");
        System.out.println("服务端已启动");
    }
}
