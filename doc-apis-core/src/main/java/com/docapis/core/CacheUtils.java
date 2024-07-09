package com.docapis.core;


import com.docapis.core.parser.ClassNode;
import com.docapis.core.parser.ControllerNode;
import com.docapis.core.parser.ResponseNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.docapis.core.constant.CoreConstants.CACHE_FILE;

/**
 * abstract doc generator
 * <p>
 * licence Apache 2.0,AGPL-3.0, from japidoc and doc-apis originated
 **/
public class CacheUtils {

    public static void saveControllerNodes(List<ControllerNode> controllerNodes) {
        try {
            controllerNodes.forEach(controllerNode -> {
                controllerNode.getRequestNodes().forEach(requestNode -> {
                    requestNode.setControllerNode(null);
                    requestNode.setLastRequestNode(null);
                    ResponseNode responseNode = requestNode.getResponseNode();
                    responseNode.setRequestNode(null);
                    removeLoopNode(responseNode);
                });
            });
            Utils.writeToDisk(new File(DocContext.getDocPath(), CACHE_FILE), Utils.toJson(controllerNodes));
        } catch (Exception ex) {
            LogUtils.error("saveControllerNodes error!!!", ex);
        }
    }

    private static void removeLoopNode(ClassNode classNode) {
        classNode.setParentNode(null);
        classNode.setGenericNodes(null);
        classNode.getChildNodes().forEach(fieldNode -> {
            fieldNode.setClassNode(null);
            if (fieldNode.getChildNode() != null) {
                removeLoopNode(fieldNode.getChildNode());
            }
        });
    }

    public static List<ControllerNode> getControllerNodes(String apiVersion) {
        File apiRootPath = new File(new File(DocContext.getDocPath()).getParentFile(), apiVersion);
        if (!apiRootPath.exists()) {
            return null;
        }
        File cacheFile = new File(apiRootPath, CACHE_FILE);
        if (!cacheFile.exists()) {
            return null;
        }
        try {
            ControllerNode[] controllerNodes = Utils.jsonToObject(Utils.streamToString(new FileInputStream(cacheFile)), ControllerNode[].class);
            return Arrays.asList(controllerNodes);
        } catch (IOException ex) {
            LogUtils.error("get ControllerNodes error!!!", ex);
            return null;
        }
    }
}
