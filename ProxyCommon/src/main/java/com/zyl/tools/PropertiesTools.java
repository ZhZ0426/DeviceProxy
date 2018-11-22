package com.zyl.tools;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesTools {

  private static final String PROPERTIES_NAME = "config.properties";
  private static Properties properties;

  static {
    properties = new Properties();
    try {
      InputStream in = PropertiesTools.class.getClassLoader().getResourceAsStream(PROPERTIES_NAME);
      properties.load(in);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String getPropertiesName(String key) {
    return properties.getProperty(key);
  }
}
