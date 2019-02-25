package com.zyl;

import com.zyl.client.ProxyClient;
import com.zyl.interfaces.Client;
import com.zyl.tools.ClientCollection;
import com.zyl.tools.PropertiesTools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;

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
        String macId = "";
        InetAddress ip = null;
        NetworkInterface ni = null;
        try {
            boolean bFindIP = false;
            Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                    .getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP) {
                    break;
                }
                ni = (NetworkInterface) netInterfaces
                        .nextElement();
                // ----------特定情况，可以考虑用ni.getName判断
                // 遍历所有ip
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = (InetAddress) ips.nextElement();
                    if (!ip.isLoopbackAddress() // 非127.0.0.1
                            && ip.getHostAddress().matches(
                            "(\\d{1,3}\\.){3}\\d{1,3}")) {
                        bFindIP = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
        if (null != ip) {
            try {
                macId = getMacFromBytes(ni.getHardwareAddress());
            } catch (SocketException e) {
            }
        }
        return macId.replaceAll("-","");

    }

    private static String getMacFromBytes(byte[] bytes) {
        StringBuffer mac = new StringBuffer();
        byte currentByte;
        boolean first = false;
        for (byte b : bytes) {
            if (first) {
                mac.append("-");
            }
            currentByte = (byte) ((b & 240) >> 4);
            mac.append(Integer.toHexString(currentByte));
            currentByte = (byte) (b & 15);
            mac.append(Integer.toHexString(currentByte));
            first = true;
        }
        return mac.toString().toUpperCase();
    }

}
