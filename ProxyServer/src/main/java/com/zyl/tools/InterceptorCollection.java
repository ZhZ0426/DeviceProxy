package com.zyl.tools;

import com.zyl.http.intercept.Intercepter;
import com.zyl.http.intercept.TokenIntercepter;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class InterceptorCollection {
  static List<Intercepter> intercepterList =
      Arrays.asList(new Intercepter[] {new TokenIntercepter()});

  public static boolean interceptor(FullHttpRequest request) {
    for (Iterator<Intercepter> iterator = intercepterList.iterator(); iterator.hasNext(); ) {
      if (!iterator.next().intercept(request)) {
        return false;
      }
    }
    return true;
  }
}
