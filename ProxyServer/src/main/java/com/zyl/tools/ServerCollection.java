package com.zyl.tools;

import com.zyl.interfaces.Server;

import java.util.List;


public class ServerCollection {

  private List<Server> serverList;

  public ServerCollection(List<Server> serverList) {
    this.serverList = serverList;
  }

  public void startServer() {
    serverList.forEach(server -> server.start());
  }

  public void stopServer() {
    serverList.forEach(server -> server.stop());
  }
}
