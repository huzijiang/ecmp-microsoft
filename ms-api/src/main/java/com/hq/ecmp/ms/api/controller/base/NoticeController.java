package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpNotice;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公告
 * @Author: zj.hu
 * @Date: 2019-12-31 12:01
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    /**
     * 查询所有的公公告信息
     * @param  userDto
     * @return
     */
    @ApiOperation(value = "getAllNotice",notes = "查询所有的公公告信息",httpMethod ="POST")
    @PostMapping("/getAllNotice")
    public ApiResponse<List<EcmpNotice>> getAllNotice(UserDto userDto){

        return null;
    }

    /**
     * 查询公告详细信息
     * @param  userDto
     * @return
     */
    @ApiOperation(value = "getNoticeDetail",notes = "查询公告详细信息",httpMethod ="POST")
    @PostMapping("/getNoticeDetail")
    public ApiResponse<EcmpNotice> getNoticeDetail(UserDto userDto){

        return null;
    }

}
