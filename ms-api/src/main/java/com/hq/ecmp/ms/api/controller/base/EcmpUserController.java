package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.dto.UserReqimensDTO;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ecmpUser")
@Api(description="企业员工管理")
public class EcmpUserController {

    @Autowired
    private IEcmpUserService ecmpUserService;



    /*
     * 新增员工信息
     * @param  ecmpUser
     * @return
     *
     * */
    @ApiOperation(value = "添加员工",notes = "添加员工",httpMethod ="POST")
    @PostMapping("/addEcmpUser")
    public ApiResponse addEcmpUser(@RequestBody EcmpUserVo ecmpUser){
        /*用户可自由选择该员工所属部门*/
        String s = ecmpUserService.addEcmpUser(ecmpUser);
        /*保存用车制度 （多个）*/
            return ApiResponse.success(s);
    }

    /**
     * 禁用/启用  员工
     * @param  deptId
     * @return
     */
    @ApiOperation(value = "updateUseStatus",notes = "禁用/启用  员工",httpMethod ="POST")
    @PostMapping("/updateUseStatus")
    public ApiResponse updateUseStatus(@RequestBody EcmpUserVo ecmpUser ){
        Long userId=ecmpUser.getUserId();
        String status=ecmpUser.getStatus();
        if(userId==null){
            return  ApiResponse.error("员工id不能为空！");
        }
        if(status==null){
            return  ApiResponse.error("帐号状态不能为空！");
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
            return ApiResponse.error("员工id不能为空！");
        }
        int  i= ecmpUserService.updateDelFlagById(userId);
        if(i==1){
            return ApiResponse.success("删除员工成功");
        }else {
            return ApiResponse.error("删除员工失败");
        }
    }

    /**
     * 员工列表（按部门）
     * @param  ecmpUser
     * @return*/
    @ApiOperation(value = "查询员工列表",notes = "查询员工列表",httpMethod ="POST")
    @PostMapping("/getEcmpList")
    public ApiResponse<List<EcmpUserDto>> getEcmpUserList(@RequestBody EcmpUserVo ecmpUser){
        Long deptId=ecmpUser.getDeptId();
        if(deptId==null){
            return ApiResponse.error("部门id不能为空！");
        }
        List<EcmpUserDto> ecmpUserList = ecmpUserService.getEcmpUserList(deptId);
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
        int i = ecmpUserService.updateEcmpUser(ecmpUser);
        if (i == 1){
            return ApiResponse.success("修改员工成功!");
        }else {
            return ApiResponse.error("修改员工失败!");
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
        Long userId=ecmpUserVo.getUserId();
        if(newPhoneNum==null||newPhoneNum.trim()==""){
            return ApiResponse.error("手机号不能为空！");
        }
        if(reWritePhone==null||reWritePhone.trim()==""){
            return ApiResponse.error("需要再次输入手机号！");
        }
        if(!reWritePhone.equals(newPhoneNum)){
            return ApiResponse.error("手机号码不一致！");
        }
        String s= ecmpUserService.updatePhoneNum(newPhoneNum,reWritePhone,userId);
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
            return ApiResponse.error("员工id不能为空！");
        }
        EcmpUserDto EcmpUserDto = ecmpUserService.selectEcmpUserDetail(userId);
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
             return ApiResponse.error("员工id不能为空！");
         }
         if(dimissionTime==null){
             return ApiResponse.error("离职日期不能为空！");
         }
         int i = ecmpUserService.updateDimissionTime(dimissionTime,userId);
         if (i == 1){
             return ApiResponse.success("设置离职日期成功!");
         }else {
             return ApiResponse.error("设置离职日期失败!");
         }
     }
    /*已离职数量
    * @param  userId
    * @return
    * */
    @ApiOperation(value = "已离职数量",notes = "已离职数量",httpMethod ="POST")
    @PostMapping("/selectDimissionCount")
    public ApiResponse selectDimissionCount(){
        int i = ecmpUserService.selectDimissionCount();
        return ApiResponse.success(i);
    }

    /*已离职列表
     * @param  userId
     * @return
     * */
    @ApiOperation(value = "已离职列表",notes = "已离职列表",httpMethod ="POST")
    @PostMapping("/selectDimissionList")
    public ApiResponse selectDimissionList(@RequestBody EcmpUserVo ecmpUser){
        Long deptId=ecmpUser.getDeptId();
        if(deptId==null){
            return ApiResponse.error("部门id不能为空！");
        }
        List<EcmpUserDto>  dimissionList= ecmpUserService.selectDimissionList(deptId);
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
            return ApiResponse.error("设置用车制度失败");
        }
        return ApiResponse.success("设置成功");
    }

}
