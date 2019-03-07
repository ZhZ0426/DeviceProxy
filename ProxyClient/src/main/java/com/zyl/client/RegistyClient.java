package com.zyl.client;

import com.zyl.interfaces.Client;
import com.zyl.tools.PropertiesTools;
import com.zyl.tools.ZookeeperClient;

import org.apache.curator.framework.CuratorFramework;

/**
 * @author zyl
 * @description 与注册中心交互
 * @time 2019/3/6 17:02
 */
public class RegistyClient implements Client {
    private static final String ZK_ADDRESS = PropertiesTools.getPropertiesName("zk_ip");
    private static final String ZK_PATH = "/proxy/server";

    @Override
    public void start() {
        CuratorFramework client = ZookeeperClient.getInstance(ZK_ADDRESS);
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