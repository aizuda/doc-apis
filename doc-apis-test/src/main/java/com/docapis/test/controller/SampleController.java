package com.docapis.test.controller;

import com.docapis.test.model.ApiResult;
import com.docapis.test.model.DocQuery;
import com.docapis.test.model.Document;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

/**
 * 示例接口
 **/
@RestController
@RequestMapping("/sample")
public class SampleController {
    /**
     * 分页获取文档列表
     *
     * @param docQuery 文档分页查询参数
     * @return 分页数据
     */
    @PostMapping("/page")
    public ApiResult<PageInfo<Document>> pageList(@RequestBody @Valid DocQuery docQuery) {
        Document document = new Document();
        document.setId(1L);
        document.setTitle("老汉");
        document.setContent("推*");
        PageInfo<Document> pageInfo = new PageInfo<>(Collections.singletonList(document));
        return ApiResult.success(pageInfo);
    }

    /**
     * 获取文档详情
     *
     * @param documentId 文档id
     * @return 文档详情
     */
    @GetMapping("/getDocment")
    public ApiResult<Document> document(@RequestParam Long documentId) {
        Document document = new Document();
        document.setId(1L);
        document.setTitle("老汉");
        document.setContent("推*");
        return ApiResult.success(document);
    }

    /**
     * 更新文档
     *
     * @param document 文档信息
     * @return 更新结果
     */
    @PutMapping("/updateDocument")
    public ApiResult<Boolean> updateDocument(@RequestBody Document document) {
        return ApiResult.success(Boolean.TRUE);
    }

    /**
     * 删除文档
     *
     * @param documentId 文档id
     * @return 删除结果
     */
    @DeleteMapping("/delDocument")
    public ApiResult<Boolean> delDocument(@RequestParam Long documentId) {
        return ApiResult.success(Boolean.TRUE);
    }


}
