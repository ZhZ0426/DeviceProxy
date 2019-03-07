package com.zyl.tools;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * @author zyl
 * @description
 * @time 2019/3/7 14:01
 */
public class ZookeeperClient {

    private static CuratorFramework client;

    public static synchronized CuratorFramework getInstance(String ZK_ADDRESS) {
        if (null == ZookeeperClient.client) {
            CuratorFramework curatorClient = CuratorFrameworkFactory.newClient(
                    ZK_ADDRESS,
                    new RetryNTimes(10, 5000)
            );
            curatorClient.start();
            ZookeeperClient.client = curatorClient;
        }
        return client;
    }
}