package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        int i = ecmpUserService.addEcmpUser(ecmpUser);
        /*保存用车制度*/

        if (i == 1){
            return ApiResponse.success("添加员工成功!");
        }else {
            return ApiResponse.error("添加员工失败!");
        }
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
            ApiResponse.error("员工编号不能为空！");
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
    @ApiOperation(value = "查询员工列表",notes = "查询员工列表",httpMethod ="GET")
    @GetMapping("/getEcmpList")
    public ApiResponse<List<EcmpUserDto>> getEcmpUserList(@RequestBody EcmpUserVo ecmpUser){
        Long deptId=ecmpUser.getDeptId();
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
    public ApiResponse updatePhoneNum(@RequestBody EcmpUserVo ecmpUser){
        String newPhoneNum=ecmpUser.getNewPhoneNum();
        String reWritePhone=ecmpUser.getRreWritePhone();
        String s= ecmpUserService.updatePhoneNum(newPhoneNum,reWritePhone);
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
        EcmpUserDto EcmpUserDto = ecmpUserService.selectEcmpUserDetail(userId);
            return ApiResponse.success(EcmpUserDto);
    }

     /*设置离职日期
    @param  dimissionTime
     * @return
    * */
     @ApiOperation(value = "设置离职日期",notes = "设置离职日期",httpMethod ="POST")
     @PostMapping("/updateDimissionTime")
     public ApiResponse updateDimissionTime(@RequestBody EcmpUserVo ecmpUser){
         Date dimissionTime=ecmpUser.getDimissionTime();
         Long userId=ecmpUser.getUserId();
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
    public ApiResponse selectDimissionCount(@RequestBody EcmpUserVo ecmpUser){
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
        Long userId=ecmpUser.getUserId();
        List<EcmpUserDto>  dimissionList= ecmpUserService.selectDimissionList(deptId,userId);
        return ApiResponse.success(dimissionList);
    }

    /*日期禁用（离职日期）*/
}
