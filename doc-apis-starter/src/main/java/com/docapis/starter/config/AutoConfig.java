package com.docapis.starter.config;


import com.docapis.core.Docs;
import com.docapis.core.DocsConfig;
import com.docapis.core.LogUtils;
import com.docapis.core.plugin.markdown.MarkdownDocPlugin;
import com.docapis.starter.property.DocPlusConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CompletableFuture;

/**
 * 自动生成装配
 * <p>
 * Copyright © 2024 xpc1024 All Rights Reserved
 **/
@Configuration
@EnableConfigurationProperties(value = {DocPlusConfigProperties.class})
@ConditionalOnProperty(prefix = "doc-apis", name = {"enable"}, havingValue = "true", matchIfMissing = true)
public class AutoConfig implements InitializingBean {
    @Autowired
    private DocPlusConfigProperties docPlusConfigProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!docPlusConfigProperties.isEnable()) {
            // 未开启, 直接不生成
            LogUtils.info("doc-apis not enabled, won't generate docs");
            return;
        }
        CompletableFuture.runAsync(this::doGenerate)
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    private void doGenerate() {
        DocsConfig config = new DocsConfig();
        // 项目根目录
        config.setProjectPath(docPlusConfigProperties.getProjectPath());
        // 项目名称
        config.setProjectName(docPlusConfigProperties.getProjectName());
        // 声明该API的版本
        config.setApiVersion(docPlusConfigProperties.getDocVersion());
        // 生成API 文档所在目录
        config.setDocsPath(docPlusConfigProperties.getDocPath());
        // 配置自动生成
        config.setAutoGenerate(docPlusConfigProperties.isAutoGenerate());
        // 添加MarkDown文档
        if (docPlusConfigProperties.isGenerateMarkDown()) {
            config.addPlugin(new MarkdownDocPlugin());
        }

        // 设置默认语言
        config.setLocale(docPlusConfigProperties.getLocale());

        // 执行生成文档
        Docs.buildHtmlDocs(config);
    }
}
