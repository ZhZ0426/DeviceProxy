package com.zyl.http;

import com.zyl.tools.StatisticsCollection;

@Rest("count")
public class StatisticsController {
  @GET("getAll")
  public Object findAll() {
    return StatisticsCollection.getBytes();
  }
}
