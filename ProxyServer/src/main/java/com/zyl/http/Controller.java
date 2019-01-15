package com.zyl.http;

import java.util.HashMap;
import java.util.Map;

public class Controller {
    private String className;
    private Map<String, String> map = new HashMap<>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
