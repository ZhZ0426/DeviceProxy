package com.zyl.tools;

import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;
import java.util.HashMap;
import java.util.Map;

public class UrlTools {
    public static boolean jedgeUrl(String url){
        return !StringUtil.isNullOrEmpty(url) && url.contains("/") && url.split("/").length >= 2 ;
    }


    public static Map<String, String> parse(FullHttpRequest req) throws Exception {
        Map<String, String> parmMap = new HashMap<>();
        //无论是GET或者POST都需要解析请求头的参数
        QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
        decoder.parameters().entrySet().forEach( entry -> {
            parmMap.put(entry.getKey(), entry.getValue().get(0));
        });
        if (HttpMethod.POST == req.getMethod()) {
            byte[] bytes = new byte[req.content().readableBytes()];
            req.content().readBytes(bytes);
            String str = new String(bytes,CharsetUtil.UTF_8);
            JSONObject json = JSONObject.parseObject(str);
            if(json != null) {
                for (Map.Entry entry : json.entrySet()) {
                    parmMap.put(entry.getKey().toString(), entry.getValue().toString());
                }
            }
        } else if(HttpMethod.GET != req.getMethod() && HttpMethod.OPTIONS !=  req.method()) {
            throw new Exception("不支持该请求");
        }

        return parmMap;
    }
}
