package com.zyl.tools;


import java.util.HashMap;
import java.util.Map;

public class StatisticsCollection {
    private static Map<Integer,Long> bytesMap = new HashMap();

    public static void addBytes(int port,long bytes){
        synchronized (bytesMap){
            long temp = bytesMap.get(port)==null?0L:bytesMap.get(port);
            bytesMap.put(port,(bytes+temp));
        }
    }

    public static Map<Integer,Long> getBytes(){
        return bytesMap;
    }

    public static Long getBytesByPort(int port){
        return bytesMap.get(port);
    }
}
