package com.hq.ecmp.ms.api.controller.base;

import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.EcmpOrgVo;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import com.hq.ecmp.mscore.vo.OrgTreeVo;
import com.hq.ecmp.mscore.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/dept")
@Api(description="部门管理")
public class DepartmentController {
    @Autowired
    private IEcmpOrgService orgService;

    @Autowired
    private IEcmpUserService ecmpUserService;
    @Autowired
    private TokenService tokenService;
    /**
     * 部门树
     * @param  deptId
     * @return*/
    @ApiOperation(value = "显示部门组织结构",notes = "显示部门组织结构",httpMethod ="POST")
    @PostMapping("/selectDeptTree")
    public ApiResponse<OrgTreeVo> selectDeptTree(@RequestParam(value = "deptId",required = false)String deptId,@RequestParam(value = "deptName",required = false)String deptName){
        OrgTreeVo deptList = orgService.selectDeptTree(StringUtils.isEmpty(deptId) ?null:Long.valueOf(deptId),deptName);
        return ApiResponse.success(deptList);
    }

    /**
     * 员工树
     * @param  deptId
     * @return*/
    @ApiOperation(value = "显示部门及员工树",notes = "显示部门及员工树",httpMethod ="POST")
    @PostMapping("/selectDeptUserTree")
    public ApiResponse<OrgTreeVo> selectDeptUserTree(@RequestParam(value = "deptId",required = false)String deptId,@RequestParam(value = "deptName",required = false)String deptName){
        OrgTreeVo deptList = orgService.selectDeptUserTree(StringUtils.isEmpty(deptId) ?null:Long.valueOf(deptId),deptName);
        return ApiResponse.success(deptList);
    }

    /**
     * 查询部门组织结构
     * @param  deptId
     * @return*/
    @ApiOperation(value = "显示部门组织结构",notes = "显示部门组织结构",httpMethod ="POST")
    @PostMapping("/selectCombinationOfCompany")
    public ApiResponse<List<EcmpOrgDto>> selectCombinationOfCompany(@RequestParam(value = "deptId",required = false)String deptId,@RequestParam(value = "deptType",required = false) String deptType){
        List<EcmpOrgDto> deptList = orgService.selectCombinationOfCompany(StringUtils.isEmpty(deptId) ?null:Long.valueOf(deptId),deptType);
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
    @ApiOperation(value = "查询上级组织id中的员工姓名和电话",notes = "查询上级组织id中的员工姓名和电话",httpMethod ="POST")
    @PostMapping("/getEcmpUserNameAndPhone")
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
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        try {
            ecmpOrg.setDeptType(CommonConstant.FINISH);
            int i = orgService.addDept(ecmpOrg,loginUser.getUser().getUserId());
            return ApiResponse.success("添加部门成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
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
     * 逻辑删除部门信息
     * @param  ecmpOrgVo
     * @return
     */
    @ApiOperation(value = "updateDelFlagById",notes = "逻辑删除部门信息",httpMethod ="POST")
    @PostMapping("/updateDelFlagById")
    public ApiResponse updateDelFlagById(@RequestBody EcmpOrgVo ecmpOrgVo){
        Long deptId=ecmpOrgVo.getDeptId();
        String deptType=ecmpOrgVo.getDeptType();
        if(deptId==null){
            return ApiResponse.error("组织id不能为空！");
        }
        if(deptType==null){
            return ApiResponse.error("组织类别不能为空！");
        }
        String msg = orgService.updateDelFlagById(deptType,deptId);
        return ApiResponse.success(msg);
    }

    /**
     * 禁用/启用  部门
     * @param  ecmpOrg
     * @return
     */
    @ApiOperation(value = "updateUseStatus",notes = "禁用/启用  部门",httpMethod ="POST")
    @PostMapping("/updateUseStatus")
    public ApiResponse updateUseStatus(@RequestBody  EcmpOrgVo ecmpOrg){
        String status=ecmpOrg.getStatus();
        Long deptId=ecmpOrg.getDeptId();
        if(deptId==null){
            return ApiResponse.error("组织id不能为空！");
        }
        if(status==null){
            return ApiResponse.error("部门状态不能为空！");
        }
        String s = orgService.updateUseStatus(status,deptId);
        return ApiResponse.success(s);
    }


    /**
     * 按照部门名称或编号模糊查询匹配的列表
     * @param  ecmpOrgVo
     * @return*/
    @ApiOperation(value = "按照部门名称或编号模糊查询匹配的列表",notes = "按照部门名称或编号模糊查询匹配的列表",httpMethod ="POST")
    @PostMapping("/selectDeptByDeptNameOrCode")
    public ApiResponse<List<EcmpOrgDto>> selectDeptByDeptNameOrCode(@RequestBody EcmpOrgVo ecmpOrgVo){
        String deptNameOrCode=ecmpOrgVo.getDeptNameOrCode();
        if("".equals(deptNameOrCode.trim())){
            return ApiResponse.error("请输入有效的公司名称或编号！");
        }
        List<EcmpOrgDto> companyList = orgService.selectDeptByDeptNameOrCode(deptNameOrCode);
        if(companyList.size()>0){
            return ApiResponse.success(companyList);
        }else{
            return ApiResponse.error("无匹配数据！");
        }
    }

    /**
     * 显示部门列表
     * @param
     * @return*/
    @ApiOperation(value = "显示部门列表",notes = "显示部门列表",httpMethod ="POST")
    @PostMapping("/selectDeptList")
    public ApiResponse<PageResult<EcmpOrgDto>> selectDeptList(@RequestBody PageRequest pageRequest){
        Long deptId=pageRequest.getDeptId();
        String deptType=pageRequest.getDeptType();
        if(deptId==null){
            return ApiResponse.error("组织id不能为空！");
        }
        PageResult<EcmpOrgDto> companyList = orgService.selectDeptList(deptId,deptType,pageRequest.getPageNum(),pageRequest.getPageSize());
        return ApiResponse.success(companyList);
    }


    /**
     * 查询当前机构信息
     * @param  ecmpOrgVo
     * @return*/
    @ApiOperation(value = "查询当前机构信息",notes = "查询当前机构信息",httpMethod ="POST")
    @PostMapping("/selectCurrentDeptInformation")
    public ApiResponse<EcmpOrgDto> selectCurrentDeptInformation(@RequestBody EcmpOrgVo ecmpOrgVo){
        Long deptId=ecmpOrgVo.getDeptId();
        if(deptId==null){
            return ApiResponse.error("组织id不能为空！");
        }
        EcmpOrgDto ecmpOrgDto = orgService.selectCurrentDeptInformation(deptId);
        return ApiResponse.success(ecmpOrgDto);
    }



    /**
     * 显示当前登陆用户所属公司与公司下的部门
     * @param  deptId
     * @return*/
    @ApiOperation(value = "显示当前登陆用户所属公司与公司下的部门",notes = "显示当前登陆用户所属公司与公司下的部门",httpMethod ="POST")
    @PostMapping("/selectDeptComTree")
    public ApiResponse<List<EcmpOrgDto>> selectDeptComTree(@RequestParam(value = "deptId",required = false)String deptId,@RequestParam(value = "deptType",required = false) String deptType){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        SysUser user = loginUser.getUser();
        List<EcmpOrgDto> deptList = orgService.selectDeptComTree(StringUtils.isEmpty(deptId) ?user.getDeptId():Long.valueOf(deptId),deptType);
        return ApiResponse.success(deptList);
    }
}
