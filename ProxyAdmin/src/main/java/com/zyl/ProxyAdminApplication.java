package com.zyl;

import com.zhz.server.WebServer;
import com.zyl.server.GatewayServer;
import com.zyl.server.ListenServer;

/**
 * @Author: zyl
 * @Description Admin启动类
 * @Date 11:48 2019/2/1
 */
public class ProxyAdminApplication {

    public static void main(String[] args) {

        new GatewayServer().start();
        new WebServer(9999, "com.zyl.controller").start();
        new ListenServer().start();
    }

}

