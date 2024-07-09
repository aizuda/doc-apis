package com.docapis.core;

import com.docapis.core.parser.ResponseNode;

import java.util.Map;

/**
 * response wrapper
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public interface IResponseWrapper {
    Map<String, Object> wrapResponse(ResponseNode responseNode);

}
