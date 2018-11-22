package com.zyl.tools;


import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class ChannelCollection {

  private static volatile Map<String, Channel> ports = new HashMap<>();
  private static Map<String, String> devicePort = new HashMap<>();

  public static void putPort(String device, String port, Channel channel) {
    synchronized (ports) {
      ports.put(device, channel);
    }
  }

  public static void putPort(String device, String port) {
    synchronized (devicePort) {
      devicePort.put(device, port);
    }
  }

  public static Channel getChannelByPort(int port) {
    return ports.get(devicePort.get("" + port));
  }

  public static Channel getPort(String device) {
    return null == ports ? null : ports.get(device);
  }


  public static JSONObject getAllDevice() {
    JSONObject json = new JSONObject();
    for (Map.Entry<String, String> entry : devicePort.entrySet()) {
      json.put(entry.getKey(), entry.getValue());
    }
    return json;
  }

  public static void removePort(String device) {
    synchronized (ports) {
      if (null != ports) {
        ports.remove(device);
        devicePort.remove(device);
      }
    }
  }

  public static void removeByPort(String port) {
    for (Map.Entry<String, String> entry : devicePort.entrySet()) {
      if (entry.getKey().equals(port)) {
        synchronized (ports) {
          if (null != ports) {
            ports.remove(entry.getValue());
            //    devicePort.remove(entry.getKey());
          }
        }
      }
    }

  }
}
