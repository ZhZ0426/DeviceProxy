package com.zyl.server;

import com.alibaba.fastjson.JSONArray;
import com.zyl.interfaces.Server;
import com.zyl.tools.PropertiesTools;
import com.zyl.tools.ZookeeperClient;
import com.zyl.util.ServerCollection;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description
 * @time 2019/3/7 15:55
 */
public class ListenServer implements Server {

    private static final String ZK_ADDRESS = PropertiesTools.getPropertiesName("zk_ip");
    private static final String ZK_PATH = "/proxy";


    @Override
    public void start() {
        try {
            CuratorFramework client = ZookeeperClient.getInstance(ZK_ADDRESS);
            PathChildrenCache watcher = new PathChildrenCache(
                    client,
                    ZK_PATH,
                    true
            );
            watcher.getListenable().addListener((client1, event) -> {
                ChildData data = event.getData();
                if (data == null) {
                    System.out.println("No data in event[" + event + "]");
                } else {
                    if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                        System.out.println("Receive event: "
                                + "type=[" + event.getType() + "]"
                                + ", path=[" + data.getPath() + "]"
                                + ", data=[" + new String(data.getData()) + "]"
                                + ", stat=[" + data.getStat() + "]");
                        String server = new String(data.getData());
                        if (data.getPath().endsWith("available")) {
                            ServerCollection.reset();
                            if (StringUtils.isNotBlank(server)) {
                                JSONArray jsonArray = JSONArray.parseArray(server);
                                ServerCollection.reset();
                                ServerCollection.addServer(jsonArray);
                            }
                        } else {
                            ServerCollection.reset();
                            if (StringUtils.isNotBlank(server)) {
                                if (server.contains(",")) {
                                    ServerCollection.addIp(Arrays.stream(server.split(",")).collect(Collectors.toList()));
                                } else {
                                    ServerCollection.addIp(server);
                                }
                            }
                        }
                    }
                }
            });
            watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }
}