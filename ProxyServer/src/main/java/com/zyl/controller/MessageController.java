package com.zyl.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhz.http.GET;
import com.zhz.http.Rest;
import com.zyl.tools.ChannelCollection;

/**
 * @author Administrator
 * @description
 * @time 2019/2/26 14:25
 */
@Rest("msg")
public class MessageController {

    @GET("all")
    public JSONObject getAll() {
        return ChannelCollection.getAllDevice();
    }
}