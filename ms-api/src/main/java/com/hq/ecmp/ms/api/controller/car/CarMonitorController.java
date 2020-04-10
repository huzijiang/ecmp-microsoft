package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.aspectj.lang.enums.OperatorType;
import com.hq.ecmp.constant.EnterpriseCarTypeConstant;
import com.hq.ecmp.interceptor.log.Log;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseInfo;
import com.hq.ecmp.mscore.domain.EnterpriseCarTypeInfo;
import com.hq.ecmp.mscore.dto.CarLocationDto;
import com.hq.ecmp.mscore.service.ICarInfoService;
import com.hq.ecmp.mscore.service.IEcmpEnterpriseInfoService;
import com.hq.ecmp.mscore.service.IEnterpriseCarTypeInfoService;
import com.hq.ecmp.mscore.vo.CarLocationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CarMonitorXController
 * @Description TODO 后管车辆监控
 * @Author yj
 * @Date 2020/3/18 10:04
 * @Version 1.0
 */
@RestController
@RequestMapping("/carMonitor")
@Api(value = "后管车辆监控",tags = "车辆监控")
public class CarMonitorController {

    @Resource
    private ICarInfoService iCarInfoService;
    @Resource
    private IEnterpriseCarTypeInfoService iEnterpriseCarTypeInfoService;

    /**
     * 车辆检索和定位
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "车辆检索",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    @Log(value = "车辆检索")
    @ApiOperation(value = "车辆检索")
    @PostMapping("/locationCars")
    public ApiResponse<List<CarLocationVo>> locationCars(@RequestBody CarLocationDto carLocationDto){
        List<CarLocationVo> carLocationVos = null;
        try {
            carLocationVos = iCarInfoService.locationCars(carLocationDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
        return ApiResponse.success(carLocationVos);
    }

    /**
     * 获取所有的车型
     * @return
     */
    @com.hq.core.aspectj.lang.annotation.Log(title = "获取所有车型",businessType = BusinessType.OTHER,operatorType = OperatorType.MANAGE)
    @Log(value = "获取所有车型")
    @ApiOperation(value = "获取所有车型")
    @PostMapping("/getAllCarType")
    public ApiResponse< List<EnterpriseCarTypeInfo> > getAllCarType(){
        try {
            EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
            enterpriseCarTypeInfo.setStatus(EnterpriseCarTypeConstant.VALID);
            List<EnterpriseCarTypeInfo> enterpriseCarTypeInfos = iEnterpriseCarTypeInfoService.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
            return ApiResponse.success(enterpriseCarTypeInfos);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("车型获取失败");
        }
    }
}
