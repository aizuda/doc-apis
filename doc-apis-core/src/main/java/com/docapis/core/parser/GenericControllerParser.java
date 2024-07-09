package com.docapis.core.parser;

import com.docapis.core.DocApi;
import com.docapis.core.Utils;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;

/**
 * generic controller parser
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class GenericControllerParser extends AbsControllerParser {

    @Override
    protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {
        md.getAnnotationByName(DocApi.class.getSimpleName()).ifPresent(an -> {
            if (an instanceof NormalAnnotationExpr) {
                ((NormalAnnotationExpr) an).getPairs().forEach(p -> {
                    String n = p.getNameAsString();
                    if (n.equals("url")) {
                        requestNode.setUrl(Utils.removeQuotations(p.getValue().toString()));
                    } else if (n.equals("method")) {
                        requestNode.addMethod(Utils.removeQuotations(p.getValue().toString()));
                    }
                });
            }
        });
    }

}
