package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.EcmpOrgVo;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dept")
@Api(description="部门管理")
public class DepartmentController {
    @Autowired
    private IEcmpOrgService orgService;

    @Autowired
    private IEcmpUserService ecmpUserService;

    /**
     * 查询部门列表
     * @param  deptId
     * @return*/
    @ApiOperation(value = "查询部门列表",notes = "查询部门列表",httpMethod ="POST")
    @PostMapping("/getDeptList")
    public ApiResponse<List<EcmpOrgDto>> getDeptList(@RequestParam(value = "deptId",required = false)String deptId,@RequestParam(value = "deptType",required = false) String deptType){
        List<EcmpOrgDto> deptList = orgService.getDeptList(StringUtils.isEmpty(deptId) ?null:Long.valueOf(deptId),deptType);
        return ApiResponse.success(deptList);
    }
    /**
     * 查询部门详情
     * @param  ecmpOrgVo
     * @return*/
    @ApiOperation(value = "查询部门详情",notes = "查询部门详情",httpMethod ="POST")
    @PostMapping("/getDeptDetails")
    public ApiResponse<EcmpOrgDto> getDeptDetails(@RequestBody EcmpOrgVo ecmpOrgVo) {
         if(ecmpOrgVo.getDeptId()==null){
             return ApiResponse.error("部门id不能为空");
         }
            EcmpOrgDto ecmpOrg = orgService.getDeptDetails(ecmpOrgVo.getDeptId());
            return ApiResponse.success(ecmpOrg);
    }

    /**
     * 获取上级组织id中的员工姓名和电话
     * @param  ecmpUserVo
     * @return List<EcmpUserDto>*/
    @ApiOperation(value = "查询上级组织id中的员工姓名和电话",notes = "查询上级组织id中的员工姓名和电话",httpMethod ="GET")
    @GetMapping("/getEcmpUserNameAndPhone")
    public ApiResponse<List<EcmpUserDto>> getEcmpUserNameAndPhone(@RequestBody EcmpUserVo ecmpUserVo){
        List<EcmpUserDto> ecmpUserList = ecmpUserService.getEcmpUserNameAndPhone(ecmpUserVo);

        if (ecmpUserList.size() > 0){
            return ApiResponse.success("获取员工信息成功!",ecmpUserList);
        }else {
            return ApiResponse.error("获取员工信息失败!");
        }
    }

    /*
    * 新增部门信息
    * @param  ecmpOrg
    * @return
    *
    * */
    @ApiOperation(value = "添加部门",notes = "添加部门",httpMethod ="POST")
    @PostMapping("/addDept")
    public ApiResponse addDept(@RequestBody EcmpOrgVo ecmpOrg){
        int i = orgService.addDept(ecmpOrg);
        if (i == 1){
            return ApiResponse.success("添加部门成功!");
        }else {
            return ApiResponse.error("添加部门失败!");
        }
    }
    /*
     * 修改部门信息
     * @param  ecmpOrg
     * @return
     *
     * */
    @ApiOperation(value = "修改部门",notes = "修改部门",httpMethod ="POST")
    @PostMapping("/updateDept")
    public ApiResponse updateDept(@RequestBody EcmpOrgVo ecmpOrg){
        int i = orgService.updateDept(ecmpOrg);
        if (i == 1){
            return ApiResponse.success("修改部门成功!");
        }else {
            return ApiResponse.error("修改部门失败!");
        }
    }
    /**
     * 部门编号验证
     * @param  deptId
     * @return*/
    /*@ApiOperation(value = "部门编号验证",notes = "部门编号验证",httpMethod ="GET")
    @GetMapping("/getCheckingDepcCompanyId")
    public ApiResponse getCheckingDepcCompanyId(Long deptId){
        int companyIdNum = orgService.getCheckingDepcCompanyId(deptId);
        if(companyIdNum==0){
            return ApiResponse.success("编号可用!");
        }
        return ApiResponse.error("编号不可用!");
    }*/

    /**
     * 逻辑删除部门信息
     * @param  deptId
     * @return
     */
    @ApiOperation(value = "updateDelFlagById",notes = "逻辑删除部门信息",httpMethod ="POST")
    @PostMapping("/updateDelFlagById")
    public ApiResponse updateDelFlagById(@RequestBody EcmpOrgVo ecmpOrg){
        Long deptId=ecmpOrgVo.getDeptId();
        String deptType=ecmpOrgVo.getDeptType();
        String msg = orgService.updateDelFlagById(ecmpOrg);
        return ApiResponse.error(msg);
    }

    /**
     * 禁用/启用  部门
     * @param  deptId
     * @return
     */
    @ApiOperation(value = "updateUseStatus",notes = "禁用/启用  部门",httpMethod ="POST")
    @PostMapping("/updateUseStatus")
    public ApiResponse updateUseStatus(@RequestBody  EcmpOrgVo ecmpOrg){
        String status=ecmpOrg.getStatus();
        Long deptId=ecmpOrg.getDeptId();
        String s = orgService.updateUseStatus(status,deptId);
        /*if (i > 0){
            return ApiResponse.success("启用成功");
        }else {
            return ApiResponse.error("启用失败");
        }*/
        return ApiResponse.error(s);
    }
}
