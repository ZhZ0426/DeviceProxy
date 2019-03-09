package com.zyl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @description
 * @time 2019/3/7 16:14
 */
public class ServerCollection {

    private static List<String> ipList = new ArrayList<>();
    private static JSONArray serverArray = new JSONArray();

    public static void addIp(String ip) {
        ipList.add(ip);
        ipList.forEach(ipStr -> System.out.println("当前IP:" + ipStr));
    }

    public static void addIp(List ip) {
        ipList.addAll(ip);
        ipList.forEach(ipStr -> System.out.println("当前IP:" + ipStr));
    }

    public static void reset() {
        serverArray.clear();
        ipList.clear();
    }

    public static JSONArray getAll() {
        return JSONArray.parseArray(JSON.toJSONString(ipList));
    }

    public static void addServer(JSONArray jsonArray) {
        serverArray = jsonArray;
    }

    public static JSONArray getAllServer() {
        return serverArray;
    }
}