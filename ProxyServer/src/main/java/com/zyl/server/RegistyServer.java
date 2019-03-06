package com.zyl.server;

import com.zyl.interfaces.Server;
import com.zyl.tools.NetTool;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * @author Administrator
 * @description
 * @time 2019/3/6 16:35
 */
public class RegistyServer implements Server {

    private static final String ZK_ADDRESS = "192.168.10.129:2181";
    private static final String ZK_PATH = "/proxy/server";

    @Override
    public void start() {
// 1.Connect to zk
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        System.out.println("zk client start successfully!");

        // 2.Client API test
        // 2.1 Create node
        String data1 = NetTool.getLocalIP();
        try {
            client.create().
                    creatingParentsIfNeeded().
                    forPath(ZK_PATH, data1.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }
}