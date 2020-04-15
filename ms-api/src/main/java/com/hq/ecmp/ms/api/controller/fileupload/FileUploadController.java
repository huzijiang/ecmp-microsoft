package com.hq.ecmp.ms.api.controller.fileupload;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.service.ZimgService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author xueyong
 */
@RestController
@RequestMapping("/file")
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
            return ApiResponse.success("上传成功",url);
    }

    /**
     * 图片删除
     */
    @ApiOperation(value = "deleteImage ", notes = "返回删除状态")
    @PostMapping(value = "/deleteImage")
    public ApiResponse deleteImage(@RequestBody String md5) {
        Boolean state = zimgService.deleteImage(md5);
        return ApiResponse.success("返回删除状态",state);
    }
}
