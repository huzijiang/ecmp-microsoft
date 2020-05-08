package com.hq.ecmp.ms.api.controller.base;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.hq.api.system.domain.SysRole;
import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpNotice;
import com.hq.ecmp.mscore.domain.EcmpNoticeMapping;
import com.hq.ecmp.mscore.dto.EcmpNoticeDTO;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.config.ConfigInfoDTO;
import com.hq.ecmp.mscore.service.EcmpNoticeMappingService;
import com.hq.ecmp.mscore.service.IEcmpConfigService;
import com.hq.ecmp.mscore.service.IEcmpNoticeService;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private IEcmpConfigService ecmpConfigService;

    /**
     * 分页全部查询公告列表（后台管理系统）
     * @param
     * @return
     */
    @ApiOperation(value = "getNoticeSearchList",notes = "分页查询公告列表",httpMethod ="POST")
    @Log(title = "公告管理", content = "公告列表",businessType = BusinessType.OTHER)
    @PostMapping("/getNoticeSearchList")
    public ApiResponse<PageResult<EcmpNotice>> getNoticeSearchList(@RequestBody PageRequest pageRequest){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId = loginUser.getUser().getOwnerCompany();
        try {
            PageResult<EcmpNotice> list = iEcmpNoticeService.selectNoticeSearchList(pageRequest.getPageNum(),
                    pageRequest.getPageSize(),companyId);
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
    @Log(title = "公告管理",content = "公告详情", businessType = BusinessType.OTHER)
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
        List<SysRole> roles =sysUser.getRoles();
        String  role = "";
        for (SysRole sysRole :roles){
            role += sysRole.getRoleId().toString()+",";
        }
        role =role.substring(0,role.length()-1);
        List<Map < String, Object>> list = new ArrayList();
        Map allNoticeMap = new HashMap<>();
        allNoticeMap.put("configType","1");
        //如果为所有人可见公告，默认为1
        allNoticeMap.put("bucId","1");
        list.add(allNoticeMap);
        Map userMap = new HashMap<>();
        userMap.put("configType","2");
        userMap.put("bucId",role);
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
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser sysUser= loginUser.getUser();
        long userId = sysUser.getUserId();
        long deptId = sysUser.getDeptId();
        List<SysRole> roles =sysUser.getRoles();
        String  role = "";
        for (SysRole sysRole :roles){
            role += sysRole.getRoleId().toString()+",";
        }
        role =role.substring(0,role.length()-1);
        List<Map < String, Object>> list = new ArrayList();
        Map allNoticeMap = new HashMap<>();
        allNoticeMap.put("configType","1");
        //如果为所有人可见公告，默认为1
        allNoticeMap.put("bucId","1");
        list.add(allNoticeMap);
        Map userMap = new HashMap<>();
        userMap.put("configType","2");
        userMap.put("bucId",role);
        list.add(userMap);
        Map deptMap = new HashMap();
        deptMap.put("configType","3");
        deptMap.put("bucId",deptId);
        list.add(deptMap);
        Map parm  = new HashMap();
        parm.put("noticeType",2);
        parm.put("busIdList",list);
        EcmpNotice ecmpNotice = iEcmpNoticeService.selectExpirationDateNewNotice(parm);
        return ApiResponse.success(ecmpNotice);
    }

    /**
     * 新增公告信息
     * @param
     * @return
     */
    @ApiOperation(value = "addNotice",notes = "新增公告信息",httpMethod ="POST")
    @Log(title = "公告管理",content = "新增公告", businessType = BusinessType.INSERT)
    @PostMapping("/addNotice")
    public ApiResponse addNotice(@RequestBody EcmpNoticeDTO ecmpNoticeDTO) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HttpServletRequest request = ServletUtils.getRequest();
        //获取登陆用户的信息
        LoginUser loginUser = tokenService.getLoginUser(request);
        EcmpNotice notice = new EcmpNotice();
        //标题
        notice.setNoticeTitle(ecmpNoticeDTO.getNoticeTitle());
        //内容
        notice.setNoticeContent(ecmpNoticeDTO.getNoticeContent());
        //开始时间
        notice.setPublishTime(ecmpNoticeDTO.getPublishTime());
        //默认是公告
        notice.setNoticeType("2");
        notice.setCompanyId(loginUser.getUser().getOwnerCompany());
        //结束时间
        notice.setEndTime(ecmpNoticeDTO.getEndTime());
        //默认是开启状态
        Date publishTime = format.parse(ecmpNoticeDTO.getPublishTime());
        Date endTime = format.parse(ecmpNoticeDTO.getEndTime());
        //当前时间
        Date time = DateUtils.getNowDate();
        //当前时间小于开始时间并且结束时间大于当前时间 就是待发布
        if(time.before(publishTime) && endTime.after(time)){
            notice.setStatus("0");
        //当前时间大于开始时间并且当前时间小于结束时间就是发布中
        }else if(time.after(publishTime) && time.before(endTime)){
            notice.setStatus("1");
        //开始时间大于当前时间并且结束时间大于当前时间就是已结束
        }else  if(time.after(publishTime) && time.after(endTime)){
            notice.setStatus("2");
        }
        //操作人的用户名
        notice.setCreateBy(loginUser.getUsername());
        long noticeId = iEcmpNoticeService.insertEcmpNotice(notice);
        EcmpNoticeMapping mapping = new EcmpNoticeMapping();
        // 选择全部用户 默认BucId = 1
        Long defaultId = 1L;
        if(!"1".equals(ecmpNoticeDTO.getConfigType())){
            List<Long> bucIds = ecmpNoticeDTO.getBucIds();
                for (Long bucId : bucIds) {
                //类型：1.全部用户，2.角色，3.部门
                mapping.setConfigType(ecmpNoticeDTO.getConfigType());
                mapping.setBucId(bucId);
                //status
                mapping.setStatus("0");
                //公告id
                mapping.setNoticeId(notice.getNoticeId().longValue());
                //创建时间
                mapping.setCreateTime(DateUtils.getNowDate());
                ecmpNoticeMappingService.insert(mapping);
                }
        }else{
        mapping.setBucId(defaultId);
        //类型：1.全部用户，2.角色，3.部门
        mapping.setConfigType(ecmpNoticeDTO.getConfigType());
        //status
        mapping.setStatus("0");
        //公告id
        mapping.setNoticeId(notice.getNoticeId().longValue());
        //创建时间
        mapping.setCreateTime(DateUtils.getNowDate());
        ecmpNoticeMappingService.insert(mapping);
        }
        return ApiResponse.success();
    }

    /**
     * 删除公告信息
     * @param
     * @return
     */
    @ApiOperation(value = "deleteNotice",notes = "删除公告信息",httpMethod ="POST")
    @Log(title = "公告管理",content = "删除公告", businessType = BusinessType.DELETE)
    @PostMapping("/deleteNotice")
    public ApiResponse deleteNotice(@RequestBody EcmpNoticeDTO ecmpNoticeDTO){
        iEcmpNoticeService.deleteEcmpNoticeById(ecmpNoticeDTO.getNoticeId());
        ecmpNoticeMappingService.deleteByNoticeId(ecmpNoticeDTO.getNoticeId());
        return ApiResponse.success("删除成功");
    }

    /**
     * 修改公告信息
     * @param
     * @return
     */
    @ApiOperation(value = "updateNotice",notes = "修改公告信息",httpMethod ="POST")
    @Log(title = "公告管理",content = "修改公告", businessType = BusinessType.UPDATE)
    @PostMapping("/updateNotice")
    public ApiResponse updateNotice(@RequestBody EcmpNoticeDTO ecmpNoticeDTO) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HttpServletRequest request = ServletUtils.getRequest();
        //获取登陆用户的信息
        LoginUser loginUser = tokenService.getLoginUser(request);
        EcmpNotice notice = new EcmpNotice();
        notice.setNoticeTitle(ecmpNoticeDTO.getNoticeTitle());
        notice.setNoticeContent(ecmpNoticeDTO.getNoticeContent());
        notice.setPublishTime(ecmpNoticeDTO.getPublishTime());
        notice.setEndTime(ecmpNoticeDTO.getEndTime());
        notice.setNoticeId(ecmpNoticeDTO.getNoticeId());
        //默认是开启状态
        Date publishTime = format.parse(ecmpNoticeDTO.getPublishTime());
        Date endTime = format.parse(ecmpNoticeDTO.getEndTime());
        //当前时间
        Date time = DateUtils.getNowDate();
        //当前时间小于开始时间并且结束时间大于当前时间 就是待发布
        if(time.before(publishTime) && endTime.after(time)){
            notice.setStatus("0");
            //当前时间大于开始时间并且当前时间小于结束时间就是发布中
        }else if(time.after(publishTime) && time.before(endTime)){
            notice.setStatus("1");
            //开始时间大于当前时间并且结束时间大于当前时间就是已结束
        }else  if(time.after(publishTime) && time.after(endTime)){
            notice.setStatus("2");
        }
        //notice.setStatus(ecmpNoticeDTO.getStatus());
        notice.setUpdateBy(loginUser.getUsername());
        notice.setUpdateTime(DateUtils.getNowDate());
        notice.setRemark(loginUser.getUsername());
        iEcmpNoticeService.updateEcmpNotice(notice);
        if(!"1".equals(ecmpNoticeDTO.getConfigType())){
            //先删除之前的数据 新增新的数据
            ecmpNoticeMappingService.deleteByNoticeId(ecmpNoticeDTO.getNoticeId());
            List<Long> bucIds = ecmpNoticeDTO.getBucIds();
            for (Long bucId : bucIds) {
                EcmpNoticeMapping mapping = new EcmpNoticeMapping();
                //类型：1.全部用户，2.角色，3.部门
                mapping.setConfigType(ecmpNoticeDTO.getConfigType());
                mapping.setBucId(bucId);
                //status
                mapping.setStatus("0");
                //公告id
                mapping.setNoticeId(notice.getNoticeId().longValue());
                //创建时间
                mapping.setCreateTime(DateUtils.getNowDate());
                ecmpNoticeMappingService.insert(mapping);
            }
        }else {
            // 选择全部用户 默认BucId = 1
            Long defaultId = 1L;
            //先删除之前的数据 新增新的数据
            //ecmpNoticeMappingService.deleteByNoticeId(ecmpNoticeDTO.getNoticeId());
            EcmpNoticeMapping mapping = new EcmpNoticeMapping();
            //类型：1.全部用户，2.角色，3.部门
            mapping.setConfigType(ecmpNoticeDTO.getConfigType());
            //对应id
            mapping.setBucId(defaultId);
            //公告id
            mapping.setNoticeId(ecmpNoticeDTO.getNoticeId().longValue());
            //创建时间
            mapping.setUpdateTime(DateUtils.getNowDate());
            ecmpNoticeMappingService.updateEcmpNoticeMapping(mapping);
        }
        return ApiResponse.success("修改成功");
    }

    /**
     * 获取企业配置信息
     */
    @ApiOperation(value = "queryCompanyInfo ", notes = "获取企业配置信息")
    @PostMapping("/queryCompanyInfo")
    public ApiResponse<ConfigInfoDTO> query() {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        String companyId = loginUser.getUser().getOwnerCompany().toString();
        System.out.println("==========="+companyId);
        ConfigInfoDTO configInfoDTO = ecmpConfigService.selectConfigInfo(companyId);
        return ApiResponse.success(configInfoDTO);
    }

}
