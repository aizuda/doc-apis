package com.docapis.core.doc;

import com.docapis.core.DocContext;
import com.docapis.core.LogUtils;
import com.docapis.core.Resources;
import com.docapis.core.Utils;
import com.docapis.core.parser.ControllerNode;
import com.docapis.core.parser.RequestNode;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.docapis.core.constant.CoreConstants.*;

/**
 * html doc generator
 * <p>
 * licence Apache 2.0,AGPL-3.0, from japidoc and doc-apis originated
 **/
public class HtmlDocGenerator extends AbsDocGenerator {

    public HtmlDocGenerator() {
        super(DocContext.controllerParser(), new HtmlControllerDocBuilder());
    }

    @Override
    void generateIndex(List<ControllerNode> controllerNodeList) {
        FileWriter docFileWriter = null;
        try {
            LogUtils.info("doc-apis generate index start !");
            final Template ctrlTemplate = getIndexTpl();
            final File docFile = new File(DocContext.getDocPath(), "index.html");
            docFileWriter = new FileWriter(docFile);
            Map<String, Object> data = new HashMap<>();
            controllerNodeList.forEach(controllerNode -> {
                List<RequestNode> requestNodes = controllerNode.getRequestNodes();
                requestNodes.forEach(requestNode -> {
                    String codeFileUrl = requestNode.getCodeFileUrl();
                    requestNode.setCodeFileUrl(HTML_PATH + SEPATOR + codeFileUrl);
                });
            });
            data.put("controllerNodeList", controllerNodeList);
            data.put("currentApiVersion", DocContext.getCurrentApiVersion());
            data.put("apiVersionList", DocContext.getApiVersionList());
            data.put("projectName", DocContext.getDocsConfig().getProjectName());
            data.put("i18n", DocContext.getI18n());
            data.put("watermark", DocContext.getDocsConfig().getWatermark());
            data.put("classificationLevel", DocContext.getDocsConfig().getClassificationLevel());
            ctrlTemplate.process(data, docFileWriter);
            LogUtils.info("doc-apis generate index success !");
        } catch (TemplateException | IOException ex) {
            LogUtils.error("doc-apis generate index fail !", ex);
        } finally {
            Utils.closeSilently(docFileWriter);
        }
    }

    private Template getIndexTpl() throws IOException {
        return Resources.getFreemarkerTemplate("api-index.html.ftl");
    }
}
