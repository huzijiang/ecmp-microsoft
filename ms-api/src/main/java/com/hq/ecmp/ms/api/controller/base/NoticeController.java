package com.hq.ecmp.ms.api.controller.base;
import	java.util.Map;

import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpNotice;
import com.hq.ecmp.mscore.domain.EcmpNoticeMapping;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.EcmpNoticeDTO;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.service.EcmpNoticeMappingService;
import com.hq.ecmp.mscore.service.IEcmpNoticeService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.CarGroupDetailVO;
import com.hq.ecmp.mscore.vo.CarGroupListVO;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private EcmpNoticeMappingService ecmpNoticeMappingService;
    @Autowired
    TokenService tokenService;

    /**
     * 分页全部查询公告列表（带搜索功能 后台管理系统）
     * @param
     * @return
     */
    @ApiOperation(value = "getNoticeSearchList",notes = "分页查询公告列表",httpMethod ="POST")
    @PostMapping("/getNoticeSearchList")
    public ApiResponse<PageResult<EcmpNotice>> getNoticeSearchList(@RequestBody PageRequest pageRequest){
        try {
            PageResult<EcmpNotice> list = iEcmpNoticeService.selectNoticeSearchList(pageRequest.getPageNum(),
                    pageRequest.getPageSize(),pageRequest.getSearch());
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("分页查询公告列表失败");
        }
    }

    /**
     * 查询公告列表详情（后台管理系统）
     * @param //carGroupId
     * @return
     */
    @ApiOperation(value = "getNoticeDetails",notes = "查询公告列表详情",httpMethod ="POST")
    @PostMapping("/getNoticeDetails")
    public ApiResponse<EcmpNotice> getNoticeDetails(@RequestBody Integer noticeId){
        try {
            EcmpNotice ecmpNotice = iEcmpNoticeService.getNoticeDetails(noticeId);
            return ApiResponse.success(ecmpNotice);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询公告列表详情失败");
        }
    }

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
     * 查询消息、公告详细信息
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
     * 查询消息、公告列表
     * @param
     * @return
     */
    @ApiOperation(value = "getNoticeList",notes = "查询消息列表",httpMethod ="POST")
    @PostMapping("/getNoticeList")
    public ApiResponse<List<EcmpNotice>> getNoticeList(EcmpNotice ecmpNotice){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser sysUser= loginUser.getUser();
        long userId = sysUser.getUserId();
        long deptId = sysUser.getDeptId();
        List<Map < String, Object>> list = new ArrayList();
        Map allNoticeMap = new HashMap<>();
        allNoticeMap.put("configType","1");
        //如果为所有人可见公告，默认为0
        allNoticeMap.put("bucId","0");
        list.add(allNoticeMap);
        Map userMap = new HashMap<>();
        userMap.put("configType","2");
        userMap.put("bucId",userId);
        list.add(userMap);
        Map deptMap = new HashMap();
        deptMap.put("configType","3");
        deptMap.put("bucId",deptId);
        list.add(deptMap);
        Map parm  = new HashMap();
        parm.put("noticeType",ecmpNotice.getNoticeType());
        parm.put("busIdList",list);
        List<EcmpNotice> ecmpNoticeList = iEcmpNoticeService.selectEcmpNoticeListByOtherId(parm);
        return ApiResponse.success(ecmpNoticeList);
    }

    /**
     * 根据用户Id获取有效期内的最新公告、消息
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

    /**
     * 新增公告信息
     * @param
     * @return
     */
    @ApiOperation(value = "addNotice",notes = "新增公告信息",httpMethod ="POST")
    @PostMapping("/addNotice")
    public ApiResponse addNotice(EcmpNoticeDTO ecmpNoticeDTO){
        EcmpNotice notice = new EcmpNotice();
        notice.setNoticeTitle(ecmpNoticeDTO.getNoticeTitle());
        notice.setNoticeContent(ecmpNoticeDTO.getNoticeContent());
        notice.setPublishTime(ecmpNoticeDTO.getPublishTime());
        notice.setEndTime(ecmpNoticeDTO.getEndTime());
        notice.setStatus("0");
        long noticeId = iEcmpNoticeService.insertEcmpNotice(notice);
        EcmpNoticeMapping mapping = new EcmpNoticeMapping();
        mapping.setConfigType(ecmpNoticeDTO.getConfigType());
        mapping.setBucId(ecmpNoticeDTO.getBucId());
        mapping.setStatus("0");
        mapping.setNoticeId(noticeId);
        ecmpNoticeMappingService.insert(mapping);
        return ApiResponse.success();
    }

    /**
     * 删除公告信息
     * @param
     * @return
     */
    @ApiOperation(value = "deleteNotice",notes = "新增公告信息",httpMethod ="POST")
    @PostMapping("/deleteNotice")
    public ApiResponse deleteNotice(Integer noticeId){
        iEcmpNoticeService.deleteEcmpNoticeById(noticeId);
        return ApiResponse.success("删除成功");
    }

    /**
     * 修改公告信息
     * @param
     * @return
     */
    @ApiOperation(value = "updateNotice",notes = "新增公告信息",httpMethod ="POST")
    @PostMapping("/updateNotice")
    public ApiResponse updateNotice(EcmpNoticeDTO ecmpNoticeDTO){
        EcmpNotice notice = new EcmpNotice();
        notice.setNoticeTitle(ecmpNoticeDTO.getNoticeTitle());
        notice.setNoticeContent(ecmpNoticeDTO.getNoticeContent());
        notice.setPublishTime(ecmpNoticeDTO.getPublishTime());
        notice.setEndTime(ecmpNoticeDTO.getEndTime());
        notice.setNoticeId(ecmpNoticeDTO.getNoticeId());
        notice.setStatus(ecmpNoticeDTO.getStatus());
        iEcmpNoticeService.updateEcmpNotice(notice);
        return ApiResponse.success("修改成功");
    }


}
