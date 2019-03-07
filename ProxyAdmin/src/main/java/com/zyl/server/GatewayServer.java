package com.zyl.server;

import com.zyl.interfaces.Server;
import com.zyl.tools.PropertiesTools;
import com.zyl.tools.ZookeeperClient;
import com.zyl.util.ServerCollection;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author zyl
 * @description
 * @time 2019/3/7 14:23
 */
public class GatewayServer implements Server {

    private static final String ZK_ADDRESS = PropertiesTools.getPropertiesName("zk_ip");
    private static final String ZK_PATH = "/proxy/server";


    @Override
    public void start() {
        CuratorFramework client = ZookeeperClient.getInstance(ZK_ADDRESS);
        try {
            byte[] bytes = client.getData().forPath(ZK_PATH);
            String server = new String(bytes);
            if (StringUtils.isNotBlank(server)) {
                if (server.contains(",")) {
                    ServerCollection.addIp(Arrays.stream(server.split(",")).collect(Collectors.toList()));
                } else {
                    ServerCollection.addIp(server);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stop() {

    }
}