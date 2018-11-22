package com.zyl.tools;

import com.zyl.interfaces.Client;
import java.util.List;

public class ClientCollection {

  public List<Client> clientList;
  public static String clientIp = null;

  public ClientCollection(List<Client> clientList) {
    this.clientList = clientList;
  }

  public void startClient() {
    clientList.forEach(client -> client.start());
  }

  public void stopClient() {
    clientList.forEach(client -> client.stop());
  }

}
