package com.zyl.tools;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

public class ChannelCollection {
    private static Map<String, Channel> deviceChannels = new ConcurrentHashMap<>();
    private static Map<String, Integer> devicePorts = new ConcurrentHashMap<>();
    private static Map<String, String> ipDevice = new ConcurrentHashMap<>();

    public static void putChannel(String device, Channel channel) {
        deviceChannels.put(device, channel);
        System.out.println("添加"+device);
    }

    public static void putPort(String device, Integer port) {
        devicePorts.put(device, port);
        System.out.println("添加"+device+" "+port);
    }

    public static void putIp(String ip, String device) {
        ipDevice.put(ip, device);
        System.out.println("添加Ip" + ip + "port" + device);
    }

    public static void removeIp(String ip) {
        removePort(ipDevice.get(ip));
    }

    public static Channel getChannelByPort(Integer port) {
        for (Map.Entry<String, Integer> entry : devicePorts.entrySet()) {
            if (entry.getValue().equals(port) ) {
                return deviceChannels.get(entry.getKey());
            }
        }
        return null;
    }

    public static Channel getPort(String device) {
        return null == devicePorts ? null : deviceChannels.get(device);
    }

    public static JSONObject getAllDevice() {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, Integer> entry : devicePorts.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json;
    }

    public static void removePort(String device) {
        devicePorts.remove(device);
        deviceChannels.remove(device);
    }

    public static void removeByPort(Integer port) {
        for (Map.Entry<String, Integer> entry : devicePorts.entrySet()) {
            if (entry.getValue().equals(port)) {
                deviceChannels.remove(entry.getKey());
                devicePorts.remove(entry.getKey());
            }
        }
    }
}
