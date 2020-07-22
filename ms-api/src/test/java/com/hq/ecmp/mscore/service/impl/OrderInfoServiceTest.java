package com.hq.ecmp.mscore.service.impl;

import com.hq.core.security.LoginUser;
import com.hq.ecmp.constant.enumerate.DispatchStrategyEnum;
import com.hq.ecmp.ms.api.MsApiApplication;
import com.hq.ecmp.mscore.dto.OrderListBackDto;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.service.dispatchstrategy.DispatchStrategy;
import com.hq.ecmp.mscore.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @author liuliang
 * @date 2020/7/20 下午1:26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("testk8s")
@Slf4j
@SpringBootTest(classes = {  MsApiApplication.class})
public class OrderInfoServiceTest {
    @Autowired
    private IOrderInfoService orderInfoService;

    @Test
    public void downloadOrderData() throws Exception {
        Map aa = orderInfoService.downloadOrderData(939L);
    }

    @Test
    public void getOrderList() throws Exception {
        OrderListBackDto orderListBackDto = new OrderListBackDto();
        LoginUser loginUser = new LoginUser();
        PageResult<OrderListBackDto> aa = orderInfoService.getOrderListBackDto(orderListBackDto,loginUser);
    }
}
