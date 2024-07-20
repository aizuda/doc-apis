package com.docapis.core.codegenerator;


import com.docapis.core.codegenerator.model.FieldModel;
import com.docapis.core.parser.ClassNode;

import java.util.List;

/**
 * field provider
 * <p>
 * licence Apache 2.0, from japidoc
 **/
public interface IFieldProvider {
    List<FieldModel> provideFields(ClassNode respNode);
}
