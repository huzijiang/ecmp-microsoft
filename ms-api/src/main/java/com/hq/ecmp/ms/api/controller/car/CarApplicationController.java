package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.domain.CarAuthorityInfo;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.domain.UserRegimeRelationInfo;
import com.hq.ecmp.mscore.dto.ApplyInfoDto;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IRegimeInfoService;
import com.hq.ecmp.mscore.service.IUserRegimeRelationInfoService;
import com.hq.ecmp.mscore.vo.ApprovalVO;
import com.hq.ecmp.mscore.vo.UserVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用车申请
 * @date: 2020/3/2 16:26
 * @author:guojin
 */

@RestController
@RequestMapping(value = "/list")
public class CarApplicationController {

    @Autowired
    private IRegimeInfoService iRegimeInfoService;

    @ApiOperation(value = "list", notes = "根据用车制度Id,查询审批人列表", httpMethod = "POST")
    @PostMapping("/list")
    public ApiResponse<List<ApprovalVO>> getApprovalList(RegimeInfo regimeInfo) {
        List<ApprovalVO> approvalVOList = iRegimeInfoService.selectApprovalByRegimenId(regimeInfo.getRegimenId());
        return ApiResponse.success(approvalVOList);
    }


}
