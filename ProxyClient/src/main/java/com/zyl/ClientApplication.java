package com.zyl;

import com.zyl.client.ProxyClient;
import com.zyl.interfaces.Client;
import com.zyl.tools.ClientCollection;
import com.zyl.tools.PropertiesTools;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;

public class ClientApplication {

    public static void main(String[] args) {
        ClientCollection.gwId = args.length == 1 ? args[0] : getMACAddress();
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

    private static String getMACAddress() {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

            // 下面代码是把mac地址拼装成String
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            // 把字符串所有小写字母改为大写成为正规的mac地址并返回
            return sb.toString().toUpperCase().replaceAll("-", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
