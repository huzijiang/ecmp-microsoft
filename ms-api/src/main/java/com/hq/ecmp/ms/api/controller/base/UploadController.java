package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.FeedBackDto;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpNotice;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackImageService;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackInfoService;
import com.hq.ecmp.util.FileUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:
 * @Date: 2019-12-31 12:01
 */
@RestController
@RequestMapping("/uploadController")
public class UploadController {


    /**
     * 上传文件
     * @param multipartFile
     * @param fileType
     * @return
     */
    @ApiOperation(value = "addUserEmergency",notes = "上传文件",httpMethod ="POST")
    @PostMapping("/addUserEmergency")
    public ApiResponse saveFile(MultipartFile multipartFile ,String fileType) {
        String imgPath = FileUtils.uploadfile(multipartFile, File.separator + "file");
        Map result = new HashMap();
        result.put("path", imgPath);
        result.put("errCode", "0");
        return ApiResponse.success("上传成功");
    }

}
