package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.base.UserDto;
import com.hq.ecmp.mscore.domain.EcmpRole;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.service.IEcmpRoleService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 12:01
 */
@RestController
@RequestMapping("/roleController")
public class RoleController {

    @Autowired
    private IEcmpRoleService ecmpRoleService;



    /**
     * 获取用户角色清单
     * @return
     */
    @ApiOperation(value = "getRoleList",notes = "获取用户角色清单",httpMethod ="POST")
    @PostMapping("/getRoleList")
    public ApiResponse getRoleList(){
        EcmpRole ecmpRole = new EcmpRole();
        ecmpRole.setStatus("0");
        List roles = ecmpRoleService.selectEcmpRoleList(ecmpRole);
        return ApiResponse.success(roles);
    }






}
