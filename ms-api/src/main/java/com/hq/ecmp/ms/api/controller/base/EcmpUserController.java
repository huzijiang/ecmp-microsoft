package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.*;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ecmpUser")
@Api(description="企业员工管理")
public class EcmpUserController {

    @Autowired
    private IEcmpUserService ecmpUserService;

    @Autowired
    TokenService tokenService;

    /*
     * 新增员工信息
     * @param  ecmpUser
     * @return
     *
     * */
    @ApiOperation(value = "添加员工",notes = "添加员工",httpMethod ="POST")
    @PostMapping("/addEcmpUser")
    public ApiResponse addEcmpUser(@RequestBody EcmpUserVo ecmpUser){
       /* String jobNumber=ecmpUser.getJobNumber();
        if(jobNumber!=null&&!("").equals(jobNumber)){
            int j = ecmpUserService.selectJobNumberExist(jobNumber);
            if(j>0){
                return ApiResponse.error(1,"该工号已存在，不可重复录入！",null);
            }
        }*/
        /*用户可自由选择该员工所属部门*/
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        ecmpUser.setOwnerCompany(loginUser.getUser().getOwnerCompany());
        String s = ecmpUserService.addEcmpUser(ecmpUser);
        /*保存用车制度 （多个）*/
        return ApiResponse.success(s);
    }

    /**
     * 禁用/启用  员工
     * @param  ecmpUser
     * @return
     */
    @ApiOperation(value = "updateUseStatus",notes = "禁用/启用  员工",httpMethod ="POST")
    @PostMapping("/updateUseStatus")
    public ApiResponse updateUseStatus(@RequestBody EcmpUserVo ecmpUser ){
        Long userId=ecmpUser.getUserId();
        String status=ecmpUser.getStatus();
        if(userId==null){
            return  ApiResponse.error(1,"员工id不能为空！",null);
        }
        if(status==null){
            return  ApiResponse.error(1,"帐号状态不能为空！",null);
        }
        String s = ecmpUserService.updateUseStatus(status,userId);
        return ApiResponse.error(s);
    }

    /**
     * 逻辑删除员工信息
     * @param  ecmpUser
     * @return
     */
    @ApiOperation(value = "updateDelFlagById",notes = "删除员工信息",httpMethod ="POST")
    @PostMapping("/updateDelFlagById")
    public ApiResponse updateDelFlagById(@RequestBody EcmpUserVo ecmpUser ){
        Long userId=ecmpUser.getUserId();
        if(userId==null){
            return ApiResponse.error(1,"员工id不能为空！",null);
        }
        String msg= ecmpUserService.updateDelFlagById(userId);
        if(!("删除员工成功！".equals(msg))){
            return ApiResponse.error(1,msg,null);
        }
        return ApiResponse.error(0,msg,null);
    }

    /**
     * 员工列表（按组织id）
     * @return*/
    @ApiOperation(value = "查询员工列表",notes = "查询员工列表",httpMethod ="POST")
    @PostMapping("/getEcmpUserList")
    public ApiResponse<PageResult<EcmpUserDto>> getEcmpUserList(@RequestBody PageRequest pageRequest){
        PageResult<EcmpUserDto> ecmpUserList = ecmpUserService.getEcmpUserPage(pageRequest,tokenService.getLoginUser(ServletUtils.getRequest()));
        return ApiResponse.success(ecmpUserList);
    }

    /*
     * 修改员工信息
     * @param  ecmpOrg
     * @return
     *
     * */
    @ApiOperation(value = "修改员工",notes = "修改员工",httpMethod ="POST")
    @PostMapping("/updateEcmpUser")
    public ApiResponse updateEcmpUser(@RequestBody EcmpUserVo ecmpUser ){
        /*String jobNumber=ecmpUser.getJobNumber();
        if(jobNumber!=null&&!("").equals(jobNumber)){
            int j = ecmpUserService.selectJobNumberExist(jobNumber);
            if(j>0){
                return ApiResponse.error(1,"该工号已存在，不可重复录入！",null);
            }
        }*/
        int i = ecmpUserService.updateEcmpUser(ecmpUser);
        if (i == 1){
            return ApiResponse.success("修改员工成功!");
        }else {
            return ApiResponse.error(1,"修改员工失败!",null);
        }
    }

    /*只修改手机号
    @param  ecmpUserVo
     * @return
    * */
    @ApiOperation(value = "只修改手机号",notes = "只修改手机号",httpMethod ="POST")
    @PostMapping("/updatePhoneNum")
    public ApiResponse updatePhoneNum(@RequestBody EcmpUserVo ecmpUserVo){
        String newPhoneNum=ecmpUserVo.getNewPhoneNum();
        String reWritePhone=ecmpUserVo.getReWritePhone();
        String userName=ecmpUserVo.getUserName();
        Long userId=ecmpUserVo.getUserId();
        if(newPhoneNum==null||newPhoneNum.trim()==""){
            return ApiResponse.error(1,"手机号不能为空！",null);
        }
        if(reWritePhone==null||reWritePhone.trim()==""){
            return ApiResponse.error(1,"需要再次输入手机号！",null);
        }
        if(!reWritePhone.equals(newPhoneNum)){
            return ApiResponse.error(1,"手机号码不一致！",null);
        }
        String s= ecmpUserService.updatePhoneNum(newPhoneNum,reWritePhone,userName,userId);
        return ApiResponse.success(s);
    }

    /*员工详情
   @param  ecmpUserVo
    * @return
   * */
    @ApiOperation(value = "员工详情",notes = "员工详情",httpMethod ="POST")
    @PostMapping("/selectEcmpUserDetail")
    public ApiResponse<EcmpUserDto> selectEcmpUserDetail(@RequestBody EcmpUserVo ecmpUser){
        Long userId=ecmpUser.getUserId();
        if(userId==null){
            return ApiResponse.error(1,"员工id不能为空！",null);
        }
        Long deptId=ecmpUser.getDeptId();
        if(deptId==null){
            return ApiResponse.error(1,"员工组织id不能为空！",null);
        }
        EcmpUserDto EcmpUserDto = ecmpUserService.selectEcmpUserDetail(userId,deptId);
        return ApiResponse.success(EcmpUserDto);
    }

    /*设置离职日期
   @param  ecmpUser
    * @return
   * */
    @ApiOperation(value = "设置离职日期",notes = "设置离职日期",httpMethod ="POST")
    @PostMapping("/updateDimissionTime")
    public ApiResponse updateDimissionTime(@RequestBody EcmpUserVo ecmpUser){
        Date dimissionTime=ecmpUser.getDimissionTime();
        Long userId=ecmpUser.getUserId();
        if(userId==null){
            return ApiResponse.error(1,"员工id不能为空！",null);
        }
        if(dimissionTime==null){
            return ApiResponse.error(1,"离职日期不能为空！",null);
        }
        int i = ecmpUserService.updateDimissionTime(dimissionTime,userId);
        if (i == 1){
            return ApiResponse.success(1,"设置离职日期成功!",null);
        }else {
            return ApiResponse.error(1,"设置离职日期失败!",null);
        }
    }
    /*已离职数量
     * @param  userId
     * @return
     * */
    @ApiOperation(value = "已离职数量",notes = "已离职数量",httpMethod ="POST")
    @PostMapping("/selectDimissionCount")
    public ApiResponse selectDimissionCount(){
        int i = ecmpUserService.selectDimissionCount(null);
        return ApiResponse.success(i);
    }

    /*已离职列表
     * @param  userId
     * @return
     * */
    @ApiOperation(value = "已离职列表",notes = "已离职列表",httpMethod ="POST")
    @PostMapping("/selectDimissionList")
    public ApiResponse<PageResult<EcmpUserDto>> selectDimissionList(@RequestBody PageRequest pageRequest){
        PageResult<EcmpUserDto>  dimissionList= ecmpUserService.selectDimissionList(pageRequest);
        return ApiResponse.success(dimissionList);
    }

    /**
     * 给员工设置用车制度
     * @param
     * @return
     */
    @ApiOperation(value = "bindUserRegimens",notes = "给员工设置用车制度",httpMethod ="POST")
    @PostMapping("/bindUserRegimens")
    public ApiResponse bindUserRegimens(@RequestBody UserReqimensDTO userReqimensDTO){
        try {
            ecmpUserService.bindUserRegimens(userReqimensDTO.getUserId(),userReqimensDTO.getRegimenIds());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(1,"设置用车制度失败",null);
        }
        return ApiResponse.success("设置成功");
    }


    /**
     * 按照姓名/工号/手机号模糊查询匹配的列表
     * @param  ecmpUser
     * @return*/
    @ApiOperation(value = "按照姓名/工号/手机号模糊查询匹配的列表",notes = "按照姓名/工号/手机号模糊查询匹配的列表",httpMethod ="POST")
    @PostMapping("/selectUserByNameOrJobNumberOrPhone")
    public ApiResponse<List<EcmpUserDto>> selectUserByNameOrJobNumberOrPhone(@RequestBody EcmpUserVo ecmpUser){
        String nameOrJobNumberOrPhone=ecmpUser.getNameOrJobNumberOrPhone();
        if("".equals(nameOrJobNumberOrPhone.trim())){
            return ApiResponse.error(1,"请输入有效的员工名称或编号！",null);
        }
        List<EcmpUserDto> companyList = ecmpUserService.selectUserByNickNameOrJobNumber(nameOrJobNumberOrPhone);
        if(companyList.size()>0){
            return ApiResponse.success(companyList);
        }else{
            return ApiResponse.error(1,"无匹配数据！",null);
        }
    }


    /**
     * 查询所有有效员工
     * @param  //ecmpUser
     * @return*/
    @ApiOperation(value = "查询所有有效员工",notes = "查询所有有效员工",httpMethod ="POST")
    @PostMapping("/queryAllValidUserList")
    public ApiResponse<List<EcmpUser>> queryAllValidUserList(){
        EcmpUser ecmpUser = new EcmpUser();
        ecmpUser.setStatus("0");
        ecmpUser.setDelFlag("0");
        List<EcmpUser> selectEcmpUserList = ecmpUserService.selectEcmpUserList(ecmpUser);
        return ApiResponse.success(selectEcmpUserList);
    }


    /**
     * 据分子公司+员工姓名查询所有员工
     * @return*/
    @ApiOperation(value = "据分子公司+员工姓名或电话查询所有员工",notes = "据分子公司+员工姓名或电话查询所有员工",httpMethod ="POST")
    @PostMapping("/queryUserListByCompanyIdAndName")
    public ApiResponse<List<EcmpUserDto>> queryUserListByCompanyIdAndName(@RequestBody EcmpUserVo ecmpUser){
        try {
            List<EcmpUserDto> result=ecmpUserService.queryUserListByCompanyIdAndName(ecmpUser.getDeptId(),ecmpUser.getNickName(),ecmpUser.getItIsDispatcher());
            return ApiResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询公司员工失败");
        }
    }


    /**
     * 查询所有有效员工
     * @param  //ecmpUser
     * @return*/
    @ApiOperation(value = "查询所有有效员工",notes = "查询所有有效员工",httpMethod ="POST")
    @PostMapping("/seeStaffCalibration")
    public ApiResponse seeStaffCalibration(@RequestBody EcmpUserVo ecmpUser){
        EcmpUserVo vo = new EcmpUserVo();
        //增加的时候验证
        if(ecmpUser.getUserId()!=null){
            //验证邮箱是否重复
            if(StringUtils.isNotBlank(ecmpUser.getEmail())){
                vo.setEmail(ecmpUser.getEmail());
                EcmpUserDto user= ecmpUserService.selectEcmpUser(vo);
                if(user!=null){
                    if(user.getUserId().equals(ecmpUser.getUserId()) && user.getEmail().equals(ecmpUser.getEmail())){
                        return ApiResponse.success();
                    }else if(user.getUserId().equals(ecmpUser.getUserId()) &&  !user.getEmail().equals(ecmpUser.getEmail())){
                        return ApiResponse.success();
                    }else if(!user.getUserId().equals(ecmpUser.getUserId())){
                        return ApiResponse.error(1,"所填邮箱已存在",null);
                    }
                }else{
                    return ApiResponse.success();
                }
            }else if(StringUtils.isNotBlank(ecmpUser.getPhonenumber())){//验证手机号是否重复
                 vo.setPhonenumber(ecmpUser.getPhonenumber());
                EcmpUserDto user= ecmpUserService.selectEcmpUser(vo);
                if(user!=null){
                    if(user.getUserId().equals(ecmpUser.getUserId()) && user.getPhonenumber().equals(ecmpUser.getPhonenumber())){
                        return ApiResponse.success();
                    }else if(user.getUserId().equals(ecmpUser.getUserId()) &&  !user.getPhonenumber().equals(ecmpUser.getPhonenumber())){
                        return ApiResponse.success();
                    }else if(!user.getUserId().equals(ecmpUser.getUserId())){
                        return ApiResponse.error(1,"所填手机号已存在",null);
                    }
                }else{
                    return ApiResponse.success();
                }
            }else if(StringUtils.isNotBlank(ecmpUser.getJobNumber())){//验证工号是否重复
                vo.setJobNumber(ecmpUser.getJobNumber());
                EcmpUserDto user= ecmpUserService.selectEcmpUser(vo);
                if(user!=null){
                if(user.getUserId().equals(ecmpUser.getUserId()) && user.getJobNumber().equals(ecmpUser.getJobNumber())){
                    return ApiResponse.success();
                }else if(user.getUserId().equals(ecmpUser.getUserId()) &&  !user.getJobNumber().equals(ecmpUser.getJobNumber())){
                    return ApiResponse.success();
                }else if(!user.getUserId().equals(ecmpUser.getUserId())){
                    return ApiResponse.error(1,"所填员工工号已存在",null);
                }
                }else{
                    return ApiResponse.success();
                }
            }
        }else{
            if(StringUtils.isNotBlank(ecmpUser.getEmail())){
                vo.setEmail(ecmpUser.getEmail());
                EcmpUserDto user= ecmpUserService.selectEcmpUser(vo);
                if(user!=null && user.getEmail().equals(ecmpUser.getEmail())  ){
                    return ApiResponse.error(1,"所填邮箱已存在",null);
                }else{
                    return ApiResponse.success();
                }
            }
            if(StringUtils.isNotBlank(ecmpUser.getPhonenumber())){
                vo.setPhonenumber(ecmpUser.getPhonenumber());
                EcmpUserDto user= ecmpUserService.selectEcmpUser(vo);
                if(user!=null && user.getPhonenumber().equals(ecmpUser.getPhonenumber())){
                    return ApiResponse.error(1,"所填手机号已存在",null);
                }else{
                    return ApiResponse.success();
                }
            }
            if(StringUtils.isNotBlank(ecmpUser.getJobNumber())){
                vo.setJobNumber(ecmpUser.getJobNumber());
                EcmpUserDto user= ecmpUserService.selectEcmpUser(vo);
                if(user!=null && user.getJobNumber().equals(ecmpUser.getJobNumber())){
                    return ApiResponse.error(1,"所填员工工号已存在",null);
                }else{
                    return ApiResponse.success();
                }
            }
        }
        return ApiResponse.success();
    }
}
