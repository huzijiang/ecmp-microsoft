package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.constant.enumerate.DispatchStrategyEnum;
import com.hq.ecmp.ms.api.MsApiApplication;
import com.hq.ecmp.mscore.dto.dispatch.DispatchSelectCarDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchSelectDriverDto;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.service.dispatchstrategy.DispatchStrategy;
import com.hq.ecmp.mscore.service.dispatchstrategy.DispatchStrategyEngineFactory;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("testk8s")
@Slf4j
@SpringBootTest(classes = {  MsApiApplication.class})
public class DispatchServiceImplTest {
    @Autowired
    private IDispatchService dispatchService;
    @Resource
    private DispatchStrategyEngineFactory dispatchStrategyEngineFactory;
    @Autowired
    private IOrderInfoService iOrderInfoService;

    @Test
    public void strategyGet() throws Exception {
        DispatchStrategy aa = dispatchStrategyEngineFactory.getDispatchStrategy(DispatchStrategyEnum.InCarAndDriverStrategy.getStrategyServiceName());
    }
    @Test
    public void seletDriver() throws Exception {

        System.out.println("companyDepartment");
//  .param("orderNo", "325")
//                .param("carGroupServiceMode", "CA00")
//                .param("carGroupSource", "C000")
//                .param("itIsSelfDriver", "N111"))
        DispatchSelectDriverDto dispatchSelectDriverDto
                = new DispatchSelectDriverDto ();
        dispatchSelectDriverDto.setOrderNo("427");
        dispatchSelectDriverDto.setDispatcherId("eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjhiZDZhMDY1LTkxZDAtNGEyMS1hZWY2LTcxOWFmYjRiNTNhNSJ9.QcawnYChO5snT7ntWoXiHem9vdQLIRNgGWP1KKJfXfVrvdRUOUKzO9xuiTLDW98i4L6DzHKmAmTEUK8hjc-GBg");
        dispatchSelectDriverDto.setCarGroupServiceMode("CA00");
        dispatchSelectDriverDto.setCarGroupSource("C000");
        dispatchSelectDriverDto.setItIsSelfDriver("N111");
        ApiResponse<DispatchResultVo>  result = dispatchService.getWaitSelectedDrivers(dispatchSelectDriverDto);

        log.info("result={}",result.getData());
    }


    @Test
    public void selectCar() throws Exception {
//
//        dispatcherId=eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjhhNWQzYTAxLWExYzItNDcwYi1hMDRhLTJiZTFmMGNhYjA1MyJ9.K8BMwrlL-QYfEen0fR
//        F2SKjn2hSJ7Qukd8Nhrw_ElTAP8MhOH3wG6gmSCrpxmP8iUiibjguk256WXXPNMxycGA, orderNo=429, carModelLevelType=null, plateLicence=N, driverId=null, car
//        TypeInfo=null, carGroupServiceMode=CA00, carGroupSource=C000, itIsSelfDriver=N111
        System.out.println("---------------selectCar-----------------");
        DispatchSelectCarDto dispatchSelectDriverDto
                = new DispatchSelectCarDto ();
        dispatchSelectDriverDto.setOrderNo("429");
//        dispatchSelectDriverDto.setPlateLicence("N");
        dispatchSelectDriverDto.setDispatcherId("eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6IjhhNWQzYTAxLWExYzItNDcwYi1hMDRhLTJiZTFmMGNhYjA1MyJ9.K8BMwrlL-QYfEen0fRF2SKjn2hSJ7Qukd8Nhrw_ElTAP8MhOH3wG6gmSCrpxmP8iUiibjguk256WXXPNMxycGA");
        dispatchSelectDriverDto.setCarGroupServiceMode("CA00");
        dispatchSelectDriverDto.setCarGroupSource("C000");
        dispatchSelectDriverDto.setItIsSelfDriver("N111");
        ApiResponse<DispatchResultVo>  result = dispatchService.getWaitSelectedCars(dispatchSelectDriverDto);

        log.info("result={}", JSON.toJSONString( result ));
    }

    @Test
    public void downloadOrder() throws Exception {
        Map<String,String> orderInfo=   iOrderInfoService.downloadOrderData(443L);

        log.info("result={}",   orderInfo   );
        log.info("result_json={}", JSON.toJSONString( orderInfo ));
    }
}