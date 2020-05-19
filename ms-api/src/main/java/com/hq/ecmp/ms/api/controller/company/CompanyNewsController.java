package com.hq.ecmp.ms.api.controller.company;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.mscore.service.ICarInfoService;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.NewsVO;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/** 企业动态
 *  @author shixin
 *  @date 2020-3-12
 */


@RestController
@RequestMapping("/news")
public class CompanyNewsController {
   @Autowired
   private IEcmpUserService  iEcmpUserService;
   @Autowired
   private ICarInfoService iCarInfoService;
   @Autowired
   private IDriverInfoService iDriverInfoService;
   @Autowired
   private TokenService tokenService;

   /**
    * 企业动态查询：人员数量、驾驶员数量、车辆数量
    * @return  map
    */
   @Log(title = "企业动态模块",content = "查询企业动态信息",businessType = BusinessType.OTHER)
   @ApiOperation(value = "getCompanyNewsController",notes = "查询企业动态信息",httpMethod = "POST")
   @PostMapping("/getCompanyNewsController")
   public ApiResponse<NewsVO> getCompanyNewsController() {
      NewsVO newsVO =new NewsVO();
      HttpServletRequest request = ServletUtils.getRequest();
      LoginUser loginUser = tokenService.getLoginUser(request);
      Long companyId = loginUser.getUser().getOwnerCompany();
      //统计公司员工  员工排除待审核（不在员工表，在ecmp_enterprise_register_info表）、已离职（当前时间大于离职时间）、删除员工
      int userCount = iEcmpUserService.queryCompanyEmpCunt(companyId);
      //统计公司驾驶员   驾驶员排除待审核、已离职(当前时间大于离职时间)、删除员工
      int driverCount = iDriverInfoService.queryCompanyDriverCount(companyId);
      //统计公司车辆   车辆排除已删除车辆
      int carCount = iCarInfoService.queryCompanyCarCount(companyId);
      newsVO.setUserCount(userCount);
      newsVO.setDriverCount(driverCount);
      newsVO.setCarCount(carCount);

      return ApiResponse.success(newsVO);


   }






}
