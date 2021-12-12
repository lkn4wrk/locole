package org.adamalang.apikit.codegen;

import org.adamalang.apikit.model.Common;
import org.adamalang.apikit.model.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssembleHandlers {

    public static HashMap<String, ArrayList<Method>> shred(Method[] methods) {
        HashMap<String, ArrayList<Method>> byHandler = new HashMap<>();
        for (Method method : methods) {
            ArrayList<Method> methodsForHandler = byHandler.get(method.handler);
            if (methodsForHandler == null) {
                methodsForHandler = new ArrayList<>();
                byHandler.put(method.handler, methodsForHandler);
            }
            methodsForHandler.add(method);
        }
        return byHandler;
    }

    public static Map<String, String> make(String packageName, Method[] methods) throws Exception {
        HashMap<String, ArrayList<Method>> byHandler = shred(methods);
        Map<String, String> files = new HashMap<>();
        for (Map.Entry<String, ArrayList<Method>> entry : byHandler.entrySet()) {
            String root = entry.getKey();
            StringBuilder java = new StringBuilder();
            java.append("package ").append(packageName).append(";\n\n");
            java.append("\n");
            java.append("public interface ").append(root).append("Handler {\n");
            for (Method method : byHandler.get(root)) {
                java.append("  public ");
                if (method.create != null) {
                    java.append(Common.camelize(method.create)).append("Handler");
                } else {
                    java.append("void");
                }
                java.append(" handle(").append(method.camelName).append("Request request, ").append(method.responder.camelName).append("Responder responder);\n\n");
            }
            java.append("}\n");
            files.put(root + "Handler.java", java.toString());
        }
        return files;
    }
}
