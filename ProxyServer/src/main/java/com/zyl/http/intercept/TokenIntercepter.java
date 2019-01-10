package com.zyl.http.intercept;

import com.zyl.tools.JwtTools;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.internal.StringUtil;

public class TokenIntercepter implements Intercepter {
  @Override
  public boolean intercept(FullHttpRequest request) {
    String url = request.getUri();
    /*  if(!url.contains("user/login")){
        String token  = request.headers().get("X-Token");
        if(StringUtil.isNullOrEmpty(token) || null==JwtTools.parserJavaWebToken(token)){
            return false;
        }
    }*/
    return true;
  }
}
