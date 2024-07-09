package com.docapis.core;

import com.docapis.core.doc.HtmlDocGenerator;
import com.docapis.core.plugin.rap.RapSupportPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static com.docapis.core.constant.CoreConstants.*;

/**
 * docs
 * <p>
 * licence Apache 2.0,AGPL-3.0, from japidoc and doc-apis originated
 **/
public class Docs {

    private static final String CONFIG_FILE = "docs.config";

    public static void main(String[] args) {
        DocsConfig config = loadProps();
        buildHtmlDocs(config);
    }

    public static void buildHtmlDocs(DocsConfig config) {
        DocContext.init(config);
        HtmlDocGenerator docGenerator = new HtmlDocGenerator();
        DocContext.setControllerNodeList(docGenerator.getControllerNodeList());
        try {
            docGenerator.generateDocs();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            // 清理可能产生的PageInfo临时文件
            Utils.deleteFileWithRetry(System.getProperty(USER_DIR) + File.separator + PAGE_INFO_FILE);
            Utils.deleteFileWithRetry(System.getProperty(USER_DIR) + File.separator + ES_PAGE_INFO_FILE);
        }
        CacheUtils.saveControllerNodes(docGenerator.getControllerNodeList());
        DocsConfig docsConfig = DocContext.getDocsConfig();
        if (docsConfig.getRapProjectId() != null && docsConfig.getRapHost() != null) {
            IPluginSupport rapPlugin = new RapSupportPlugin();
            rapPlugin.execute(docGenerator.getControllerNodeList());
        }
        for (IPluginSupport plugin : config.getPlugins()) {
            plugin.execute(docGenerator.getControllerNodeList());
        }

        // 清理 cache.json
        Utils.deleteFileWithRetry(DocContext.getDocPath() + File.separator + CACHE_FILE);
    }

    public static void setResponseWrapper(IResponseWrapper responseWrapper) {
        DocContext.setResponseWrapper(responseWrapper);
    }

    private static DocsConfig loadProps() {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(CONFIG_FILE));
            DocsConfig config = new DocsConfig();
            config.projectPath = properties.getProperty("projectPath", null);

            if (config.projectPath == null) {
                throw new RuntimeException("projectPath property is needed in the config file.");
            }

            config.docsPath = properties.getProperty("docsPath", null);
            config.resourcePath = properties.getProperty("resourcePath", null);
            config.mvcFramework = properties.getProperty("mvcFramework", "");
            return config;
        } catch (IOException e) {
            e.printStackTrace();

            try {
                File configFile = new File(CONFIG_FILE);
                configFile.createNewFile();
            } catch (Exception ex) {
                e.printStackTrace();
            }

            throw new RuntimeException("you need to set projectPath property in " + CONFIG_FILE);
        }
    }
}
