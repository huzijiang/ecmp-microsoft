package com.hq.ecmp.ms.api.controller.fileupload;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.service.ZimgService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author xueyong
 */
@RestController
@RequestMapping("/file/v1")
public class FileUploadController {

    @Autowired
    private ZimgService zimgService;

    /**
     * 文件上传
     */
    @ApiOperation(value = "upload ", notes = "文件上传API，直接返回文件访问地址 http 链接")
    @PostMapping(value = "/upload")
    public ApiResponse screen(@RequestParam("file") MultipartFile file) {
        String url = zimgService.uploadImage(file);
        return ApiResponse.success(url);
    }
}
