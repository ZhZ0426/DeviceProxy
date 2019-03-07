package com.zyl.controller;

import com.alibaba.fastjson.JSONArray;
import com.zhz.http.GET;
import com.zhz.http.Rest;
import com.zyl.util.ServerCollection;

/**
 * @author Administrator
 * @description
 * @time 2019/2/26 14:25
 */
@Rest("msg")
public class MessageController {

    @GET("all")
    public JSONArray getAll() {
        return ServerCollection.getAll();
    }

    @GET("server")
    public JSONArray getAllServer() {
        return ServerCollection.getAllServer();
    }
}