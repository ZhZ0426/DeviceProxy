package com.zyl;

import com.zyl.tools.PropertiesTools;

/**
 * @author Administrator
 * @description
 * @time 2019/3/6 15:58
 */
public class RegistyApplication {

    private static final String ZK_ADDRESS = PropertiesTools.getPropertiesName("zk_ip");
    private static final String ZK_PATH = "/proxy";

    public static void main(String[] args) throws Exception {
       /* CuratorFramework client = ZookeeperClient.getInstance(ZK_ADDRESS);
        PathChildrenCache watcher = new PathChildrenCache(
                client,
                ZK_PATH,
                false
        );
        watcher.getListenable().addListener((client1, event) -> {
            ChildData data = event.getData();
            if (data == null) {
                System.out.println("No data in event[" + event + "]");
            } else {
                System.out.println("Receive event: "
                        + "type=[" + event.getType() + "]"
                        + ", path=[" + data.getPath() + "]"
                        + ", data=[" + new String(data.getData()) + "]"
                        + ", stat=[" + data.getStat() + "]");
            }
        });
        watcher.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        System.out.println("Register zk watcher successfully!");

        Thread.sleep(Integer.MAX_VALUE);*/

    }


}