package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 12:00
 */
@RestController
@RequestMapping("/org")
public class OrgController {

    @Autowired
    private IEcmpUserService ecmpUserService;
    @Autowired
    private IEcmpOrgService orgService;
    /**
     * 查询用户 所在（子）公司的 部门列表
     * @param userDto  审批人信息
     * @return
     */
    @ApiOperation(value = "getUserOwnCompanyDept",notes = "查询用户 所在（子）公司的 部门列表 ",httpMethod ="POST")
    @PostMapping("/getUserOwnCompanyDept")
    public ApiResponse<List<EcmpOrg>> getUserOwnCompanyDept(UserDto userDto){
        //根据userId查询用户信息
        EcmpUser ecmpUser = ecmpUserService.selectEcmpUserById(userDto.getUserId());
        //根据部门id查询部门对象
        EcmpOrg ecmpOrg = orgService.selectEcmpOrgById(ecmpUser.getDeptId());
        //根据公司id查询部门对象列表
        List<EcmpOrg> ecmpOrgs = orgService.selectEcmpOrgsByCompanyId(ecmpOrg.getCompanyId());
        return ApiResponse.success(ecmpOrgs);
    }


}
