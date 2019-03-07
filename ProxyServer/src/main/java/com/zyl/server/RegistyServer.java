package com.zyl.server;

import com.zyl.interfaces.Server;
import com.zyl.tools.NetTool;
import com.zyl.tools.PropertiesTools;
import com.zyl.tools.ZookeeperClient;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;

/**
 * @author Administrator
 * @description
 * @time 2019/3/6 16:35
 */
public class RegistyServer implements Server {

    private static final String ZK_ADDRESS = PropertiesTools.getPropertiesName("zk_ip");
    private static final String ZK_PATH = "/proxy/server";

    @Override
    public void start() {
        CuratorFramework client = ZookeeperClient.getInstance(ZK_ADDRESS);
        String data1 = NetTool.getLocalIP();
        try {
            Stat stat = client.checkExists().forPath(ZK_PATH);
            if (stat == null) {
                client.create().
                        creatingParentsIfNeeded().
                        forPath(ZK_PATH, data1.getBytes());
            } else {
                String storageIp = new String(client.getData().forPath(ZK_PATH));
                if (!storageIp.contains(data1)) {
                    client.setData().forPath(ZK_PATH, (storageIp + "," + data1).getBytes());
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