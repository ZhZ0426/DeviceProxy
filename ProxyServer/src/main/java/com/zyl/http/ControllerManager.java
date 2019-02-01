package com.zyl.http;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class ControllerManager {

    private static Map<String, Controller> controllerMap = new HashMap<>();

    public static void putController(String key, Controller controller) {
        controllerMap.put(key, controller);
    }

    public static Controller getController(String key) {
        return controllerMap.get(key);
    }

    public Object handler(String controllerName, String method, Map<String, String> paramsMap) {
        Controller controller = controllerMap.get(controllerName);
        if (null == controller) {
            return "未响应请求";
        }
        String className = controller.getClassName();
        Map<String, String> map = controller.getMap();
        String methodName = map.get(method);
        Class<?> cls = null;
        try {
            cls = Class.forName(className);
            Object o = cls.newInstance();
            Class[] classes = new Class[paramsMap.size()];
            for (int i = 0; i < paramsMap.size(); ) {
                classes[i++] = String.class;
            }
            String[] strs = new String[paramsMap.size()];
            int i = 0;
            Method me = cls.getMethod(methodName, classes);
            Parameter[] parameters = me.getParameters();
            for (Parameter parameter : parameters) {
                for (Map.Entry<String, String> paramEntry : paramsMap.entrySet()) {
                    Param param = parameter.getAnnotation(Param.class);
                    if (param.value().equals(paramEntry.getKey())) {
                        strs[i] = paramEntry.getValue();
                        classes[i++] = String.class;
                        break;
                    }
                }
            }
            return me.invoke(o, strs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
