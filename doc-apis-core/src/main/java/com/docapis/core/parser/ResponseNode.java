package com.docapis.core.parser;

import com.alibaba.fastjson.JSONObject;
import com.docapis.core.Utils;

/**
 * response node
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class ResponseNode extends ClassNode {

    private RequestNode requestNode;

    private String stringResult;

    public RequestNode getRequestNode() {
        return requestNode;
    }

    public void setRequestNode(RequestNode requestNode) {
        this.requestNode = requestNode;
    }

    public String getStringResult() {
        return stringResult;
    }

    public void setStringResult(String stringResult) {
        this.stringResult = stringResult;
    }

    @Override
    public String toJsonApi() {
        if (stringResult != null) {
            try {
                return Utils.toPrettyJson((JSONObject.parse(stringResult)));
            } catch (Exception ex) {
                // do nothing
                return stringResult;
            }
        }
        return super.toJsonApi();
    }
}
