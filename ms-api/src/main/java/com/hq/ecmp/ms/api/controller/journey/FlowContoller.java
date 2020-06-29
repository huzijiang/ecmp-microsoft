package com.hq.ecmp.ms.api.controller.journey;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.ApproveTemplateInfo;
import com.hq.ecmp.mscore.dto.AddFolwDTO;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.config.ApproveTemplateIDTO;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IApproveTemplateInfoService;
import com.hq.ecmp.mscore.service.IApproveTemplateNodeInfoService;
import com.hq.ecmp.mscore.vo.ApprovaTemplateVO;
import com.hq.ecmp.mscore.vo.ApprovalUserVO;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: caobj
 * @Date: 2019-3-19 13:18
 */
@RestController
@RequestMapping("/flow")
@Slf4j
public class FlowContoller {

    @Autowired
    private IApplyInfoService applyInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IApproveTemplateInfoService templateInfoService;
    @Autowired
    private IApproveTemplateNodeInfoService nodeInfoService;


    @ApiOperation(value = "getApprovalList", notes = "根据用车制度Id,查询审批人", httpMethod = "POST")
    @RequestMapping("/getApprovalList")
    public ApiResponse<List<ApprovalUserVO>> getApprovalList(@RequestParam(value = "regimenId",required = true) String regimenId,
                                                             @RequestParam(value = "projectId",required = false) String projectId) {
        List<ApprovalUserVO> list= null;
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            list = nodeInfoService.getApprovalList(regimenId,projectId,loginUser.getUser());
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success(list);
    }


    /**
     *添加审批流
     * @param
     * @return
     */
    @Deprecated()
    @ApiOperation(value = "addFlowTemplate",notes = "添加审批流 ",httpMethod ="POST")
    @PostMapping("/addFlowTemplate")
    public ApiResponse addFlowTemplate(@RequestBody AddFolwDTO addFolwDTO){
        //提交行程申请
        try{
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long ownerCompany = loginUser.getUser().getOwnerCompany();
            nodeInfoService.addFlowTemplate(addFolwDTO,loginUser.getUser().getUserId(),ownerCompany);
        }catch (Exception e){
            log.error("业务处理失败", e);
            return ApiResponse.success();
        }
        return ApiResponse.success();
    }

    /**
     *审批流列表
     * @param
     * @return
     */
    @Deprecated()
    @ApiOperation(value = "flowTemplateList",notes = "审批流列表 ",httpMethod ="POST")
    @PostMapping("/flowTemplateList")
    public ApiResponse<PageResult<ApprovaTemplateVO>> flowTemplateList(@RequestBody PageRequest pageRequest){
        //提交行程申请
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long ownerCompany = loginUser.getUser().getOwnerCompany();
        List<ApprovaTemplateVO> list=templateInfoService.getTemplateList(pageRequest,ownerCompany);
        Long count=templateInfoService.getTemplateListCount(pageRequest.getSearch());
        return ApiResponse.success(new PageResult<ApprovaTemplateVO>(count,list));
    }

    /**
     *审批流详情
     * @param
     * @return
     */
    @Deprecated()
    @ApiOperation(value = "flowTemplateDetail",notes = "审批流详情 ",httpMethod ="GET")
    @PostMapping("/flowTemplateDetail")
    public ApiResponse<ApprovaTemplateVO> flowTemplateDetail(@RequestBody ApproveTemplateIDTO templateIDTO){
        //提交行程申请
        ApprovaTemplateVO vo=templateInfoService.flowTemplateDetail(templateIDTO.getApproveTemplateId());
        return ApiResponse.success(vo);
    }


    /**
     * 编辑审批流
     * @param
     * @return
     */
    @ApiOperation(value = "editFlowTemplate",notes = "编辑审批流",httpMethod ="POST")
    @PostMapping("/editFlowTemplate")
    public ApiResponse editFlowTemplate(@RequestBody AddFolwDTO addFolwDTO){
        //提交公务行程申请
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            nodeInfoService.editFlowTemplate(addFolwDTO,loginUser.getUser().getUserId());
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("编辑审批流失败");
        }
        return ApiResponse.success("编辑审批流成功"+addFolwDTO.getApproveTemplateId());
    }

    /**
     * 删除审批流
     * @return
     */
    @ApiOperation(value = "deleteFlow",notes = "删除审批流 ",httpMethod ="POST")
    @PostMapping("/deleteFlow")
    public ApiResponse deleteFlow(@RequestBody ApproveTemplateIDTO templateIDTO){
        try {
            templateInfoService.deleteFlow(templateIDTO.getApproveTemplateId());
        } catch (Exception e) {
            log.error("业务处理异常", e);
            return ApiResponse.error("删除模板失败");
        }
        return ApiResponse.success("删除模板成功:"+templateIDTO.getApproveTemplateId());
    }


    @ApiOperation(value = "getAllTemplateList",notes = "获取所有审批流 ",httpMethod ="POST")
    @PostMapping("/getAllTemplateList")
    public ApiResponse<List<ApproveTemplateInfo>> getAllTemplateList(){
        return ApiResponse.success(templateInfoService.selectApproveTemplateInfoList(new ApproveTemplateInfo()));
    }

}
