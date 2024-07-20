package com.docapis.core.parser;

import com.docapis.core.Utils;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;

import java.util.Arrays;

/**
 * jFinal controller parser
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class JFinalControllerParser extends AbsControllerParser {

    @Override
    protected void afterHandleMethod(RequestNode requestNode, MethodDeclaration md) {
        String methodName = md.getNameAsString();
        requestNode.setUrl(getUrl(methodName));
        md.getAnnotationByName("ActionKey").ifPresent(an -> {
            if (an instanceof SingleMemberAnnotationExpr) {
                String url = ((SingleMemberAnnotationExpr) an).getMemberValue().toString();
                requestNode.setMethod(Arrays.asList(RequestMethod.GET.name(), RequestMethod.POST.name()));
                requestNode.setUrl(Utils.removeQuotations(url));
                return;
            }
        });
    }

    private String getUrl(String methodName) {
        JFinalRoutesParser.RouteNode routeNode = JFinalRoutesParser.INSTANCE.getRouteNode(getControllerFile().getAbsolutePath());
        return routeNode == null ? "" : routeNode.basicUrl + "/" + methodName;
    }
}
