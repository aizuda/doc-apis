package com.docapis.core.parser;

import com.github.javaparser.ast.type.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * generic node
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public class GenericNode {

    private Type classType;
    private Class modelClass;
    private String placeholder;
    private File fromJavaFile;
    private List<GenericNode> childGenericNode = new ArrayList<>();

    public GenericNode() {
    }

    public Type getClassType() {
        return classType;
    }

    public void setClassType(Type classType) {
        this.classType = classType;
    }

    public File getFromJavaFile() {
        return fromJavaFile;
    }

    public void setFromJavaFile(File fromJavaFile) {
        this.fromJavaFile = fromJavaFile;
    }

    public List<GenericNode> getChildGenericNode() {
        return childGenericNode;
    }

    public void setChildGenericNode(List<GenericNode> childGenericNode) {
        this.childGenericNode = childGenericNode;
    }

    public void addChildGenericNode(GenericNode childNode) {
        this.childGenericNode.add(childNode);
    }

    public String getPlaceholder() {
        return placeholder;
    }


    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public Class getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class modelClass) {
        this.modelClass = modelClass;
    }
}
