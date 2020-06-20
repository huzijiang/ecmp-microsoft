package com.hq.ecmp.mscore.service.impl;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.MsApiApplication;
import com.hq.ecmp.mscore.dto.dispatch.DispatchSelectDriverDto;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Slf4j
@SpringBootTest(classes = {  MsApiApplication.class})
public class DispatchServiceImplTest {
    @Autowired
    private IDispatchService dispatchService;


    @Test
    public void companyDepartment() throws Exception {

        System.out.println("companyDepartment");
//  .param("orderNo", "325")
//                .param("carGroupServiceMode", "CA00")
//                .param("carGroupSource", "C000")
//                .param("itIsSelfDriver", "N111"))
        DispatchSelectDriverDto dispatchSelectDriverDto
                = new DispatchSelectDriverDto ();
        dispatchSelectDriverDto.setOrderNo("327");
        dispatchSelectDriverDto.setCarGroupServiceMode("CA00");
        dispatchSelectDriverDto.setCarGroupSource("C000");
        dispatchSelectDriverDto.setItIsSelfDriver("N111");
        ApiResponse<DispatchResultVo>  result = dispatchService.getWaitSelectedDrivers(dispatchSelectDriverDto);

        log.info("result={}",result.getData());
    }
}