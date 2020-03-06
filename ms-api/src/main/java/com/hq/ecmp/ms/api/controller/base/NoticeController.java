package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpNotice;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.service.IEcmpNoticeService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IEcmpNoticeService iEcmpNoticeService;

    @Autowired
    TokenService tokenService;

    /**
     * 查询所有的公公告信息
     * @param
     * @return
     */
    @ApiOperation(value = "getAllNotice",notes = "查询所有的公公告信息",httpMethod ="POST")
    @PostMapping("/getAllNotice")
    public ApiResponse<List<EcmpNotice>> getAllNotice(){

        List<EcmpNotice> ecmpNoticeList = iEcmpNoticeService.selectAll();
        return ApiResponse.success(ecmpNoticeList);
    }
    /**
     * 查询公告详细信息
     * @param
     * @return
     */
    @ApiOperation(value = "getNoticeDetail",notes = "查询公告详细信息",httpMethod ="POST")
    @PostMapping("/getNoticeDetail")
    public ApiResponse<EcmpNotice> getNoticeDetail(UserDto userDto){

        EcmpNotice ecmpNotice = iEcmpNoticeService.selectNoticeDetailByUserId(userDto.getUserId());
        return ApiResponse.success(ecmpNotice);
    }

    /**
     * 查询消息列表
     * @param
     * @return
     */
    @ApiOperation(value = "getNoticeList",notes = "查询消息列表",httpMethod ="POST")
    @PostMapping("/getNoticeList")
    public ApiResponse<List<EcmpNotice>> getNoticeList(String noticeType){
        List<EcmpNotice> ecmpNoticeList = iEcmpNoticeService.selectEcmpNoticeList(EcmpNotice.builder().noticeType(noticeType).build());
        return ApiResponse.success(ecmpNoticeList);
    }

    /**
     * 根据用户Id获取有效期内的最新公告
     * @param
     * @return
     */
    @ApiOperation(value = "getExpirationDateNewNotice",notes = "获取有效期内的最新公告",httpMethod ="POST")
    @PostMapping("/getExpirationDateNewNotice")
    public ApiResponse getExpirationDateNewNotice(){
        Long userId = tokenService.getLoginUser(ServletUtils.getRequest()).getUser().getUserId();
        EcmpNotice ecmpNotice = iEcmpNoticeService.selectExpirationDateNewNotice(userId);
        return ApiResponse.success(ecmpNotice);
    }

}
