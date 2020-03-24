package com.hq.ecmp.ms.api.controller.company;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.service.ICarInfoService;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.ir.ReturnNode;
import org.apache.commons.collections4.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

   /**
    * 企业动态查询：人员数量、驾驶员数量、车辆数量
    * @return  map
    */
   @ApiOperation(value = "getCompanyNewsController",notes = "查询企业动态信息",httpMethod = "POST")
   @PostMapping("/getCompanyNewsController")
   public ApiResponse<Map> getCompanyNewsController(Long userId){
      Map mapNews = new HashMap();
      int userCount = iEcmpUserService.queryCompanyEmpCunt();
      int driverCount = iDriverInfoService.queryCompanyDriverCount();
      int carCount = iCarInfoService.queryCompanyCarCount();
      mapNews.put("userCount",userCount);
      mapNews.put("driverCount",driverCount);
      mapNews.put("carCount",carCount);
      return ApiResponse.success(mapNews);
   }








}
