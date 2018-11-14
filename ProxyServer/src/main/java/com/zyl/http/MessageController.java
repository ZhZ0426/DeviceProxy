package com.zyl.http;

import com.alibaba.fastjson.JSONObject;
import com.zyl.common.ResponseMessage;
import com.zyl.handler.CollectionHandler;
import com.zyl.tools.ChannelCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Rest("message")
public class MessageController {

    @GET("get")
    public Object test(){
        JSONObject jsonObject = ChannelCollection.getAllDevice();
        List<Map> list = new ArrayList<Map>();
        for(Map.Entry entry : jsonObject.entrySet()){
            Map map = new HashMap();
            map.put("device",entry.getValue());
            map.put("port",entry.getKey());
            list.add(map);
        }
        return list;
    }
}
