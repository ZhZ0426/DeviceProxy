package com.zyl.http.intercept;

import io.netty.handler.codec.http.FullHttpRequest;

public interface Intercepter {

  boolean intercept(FullHttpRequest request);
}
