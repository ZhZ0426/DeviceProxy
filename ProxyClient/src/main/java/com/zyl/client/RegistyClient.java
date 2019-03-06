package com.zyl.client;

import com.zyl.interfaces.Client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * @author zyl
 * @description 与注册中心交互
 * @time 2019/3/6 17:02
 */
public class RegistyClient implements Client {
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

        try {
            byte[] bytes = client.getData().forPath(ZK_PATH);
            System.out.println("发现服务" + new String(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }
}