package com.zyl;

import com.zyl.client.ProxyClient;
import com.zyl.interfaces.Client;
import com.zyl.tools.ClientCollection;
import com.zyl.tools.ClientTool;
import com.zyl.tools.PropertiesTools;

import java.util.Arrays;

public class ClientApplication {

    public static void main(String[] args) {
        ClientCollection.gwId = args.length == 1 ? args[0] : ClientTool.getMACAddress();
        String serverIp = PropertiesTools.getPropertiesName("server_ip");
        int serverPort = Integer.parseInt(PropertiesTools.getPropertiesName("server_port"));
        ClientCollection clientCollection =
                new ClientCollection(Arrays.asList(new Client[]{new ProxyClient(serverIp, serverPort)}));
        clientCollection.startClient();
        System.out.println("客户端已启动……");
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    System.out.println("主线程关闭");
                                    clientCollection.stopClient();
                                }));

    }



}
