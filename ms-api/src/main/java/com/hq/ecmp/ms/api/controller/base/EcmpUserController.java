package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.dto.UserReqimensDTO;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.EcmpOrgVo;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ApiResponse addEcmpUser(EcmpUserVo ecmpUser){
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
    public ApiResponse updateUseStatus(String status,Long deptId){
        String s = ecmpUserService.updateUseStatus(status,deptId);
        return ApiResponse.error(s);
    }

    /**
     * 逻辑删除分子公司信息
     * @param  userId
     * @return
     */
    @ApiOperation(value = "updateDelFlagById",notes = "删除员工信息",httpMethod ="POST")
    @PostMapping("/updateDelFlagById")
    public ApiResponse updateDelFlagById(Long userId){
        int  i= ecmpUserService.updateDelFlagById(userId);
        if(i==1){
            return ApiResponse.success("删除员工成功");
        }else {
            return ApiResponse.error("删除员工失败");
        }
    }

    /**
     * 员工列表（按部门）
     * @param  deptId
     * @return*/
    @ApiOperation(value = "查询部门列表",notes = "查询部门列表",httpMethod ="GET")
    @GetMapping("/getEcmpList")
    public ApiResponse<List<EcmpUserDto>> getEcmpUserList(Long deptId){
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
    public ApiResponse updateEcmpUser(EcmpUserVo ecmpOrg){
        int i = ecmpUserService.updateEcmpUser(ecmpOrg);
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
    public ApiResponse updatePhoneNum(String newPhoneNum,String reWritePhone){
        String s= ecmpUserService.updatePhoneNum(newPhoneNum,reWritePhone);
            return ApiResponse.success(s);
    }

     /*员工详情
    @param  ecmpUserVo
     * @return
    * */
    @ApiOperation(value = "员工详情",notes = "员工详情",httpMethod ="POST")
    @PostMapping("/selectEcmpUserDetail")
    public ApiResponse<EcmpUserDto> selectEcmpUserDetail(Long userId){
        EcmpUserDto EcmpUserDto = ecmpUserService.selectEcmpUserDetail(userId);
            return ApiResponse.success(EcmpUserDto);
    }

     /*设置离职日期
    @param  dimissionTime
     * @return
    * */
     @ApiOperation(value = "设置离职日期",notes = "设置离职日期",httpMethod ="POST")
     @PostMapping("/updateDimissionTime")
     public ApiResponse updateDimissionTime(Date dimissionTime){
         int i = ecmpUserService.updateDimissionTime(dimissionTime);
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
    public ApiResponse selectDimissionCount(Long userId){
        int i = ecmpUserService.selectDimissionCount(userId);
        return ApiResponse.success(i);
    }

    /*已离职列表
     * @param  userId
     * @return
     * */
    @ApiOperation(value = "已离职列表",notes = "已离职列表",httpMethod ="POST")
    @PostMapping("/selectDimissionList")
    public ApiResponse selectDimissionList(Long userId){
        List<EcmpUserDto>  dimissionList= ecmpUserService.selectDimissionList(userId);
        return ApiResponse.success(dimissionList);
    }

    /*日期禁用（离职日期）*/
    /*已离职数量*/
    /*已离职列表*/
    /*设置离职日期*/


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
