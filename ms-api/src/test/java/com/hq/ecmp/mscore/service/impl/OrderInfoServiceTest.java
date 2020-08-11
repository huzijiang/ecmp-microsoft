package com.hq.ecmp.mscore.service.impl;

import com.hq.core.security.LoginUser;
import com.hq.ecmp.constant.enumerate.DispatchStrategyEnum;
import com.hq.ecmp.ms.api.MsApiApplication;
import com.hq.ecmp.mscore.domain.OrderServiceCostDetailRecordInfo;
import com.hq.ecmp.mscore.dto.JourneyPassengerInfoDto;
import com.hq.ecmp.mscore.dto.OrderListBackDto;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.service.IJourneyPassengerInfoService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author liuliang
 * @date 2020/7/20 下午1:26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@ActiveProfiles("testk8s")
@Slf4j
@SpringBootTest(classes = {MsApiApplication.class})
public class OrderInfoServiceTest {
    @Autowired
    private IOrderInfoService orderInfoService;

    @Autowired
    private IJourneyPassengerInfoService journeyPassengerInfoService;

    @Test
    public void downloadOrderData() throws Exception {
        Map aa = orderInfoService.downloadOrderData(939L);
    }

    @Test
    public void getOrderList() throws Exception {
        OrderListBackDto orderListBackDto = new OrderListBackDto();
        LoginUser loginUser = new LoginUser();
        PageResult<OrderListBackDto> aa = orderInfoService.getOrderListBackDto(orderListBackDto, loginUser);
    }

    @Test
    public void updateOrder() throws Exception {
        OrderServiceCostDetailRecordInfo data = new OrderServiceCostDetailRecordInfo();
        data.setAccommodationFee(BigDecimal.ZERO);
        data.setBeyondMileage(BigDecimal.ZERO);
        data.setBeyondTime(0);
        data.setFoodFee(BigDecimal.ZERO);
        data.setHighwayTollFee(BigDecimal.ZERO);
        data.setOrderId(1105L);
        data.setOthersFee(BigDecimal.valueOf(-1));
        data.setRecordId(849L);
        data.setSetMealCost(BigDecimal.ZERO);
        data.setStopCarFee(BigDecimal.ZERO);
        orderInfoService.updateTheOrder(201897L, data);
    }

    @Test
    public void doTest() {

        List<JourneyPassengerInfoDto> list = journeyPassengerInfoService.selectJourneyPassengerInfoByName(491L,"");
        list.forEach(dto->System.out.println(String.join(",",dto.getName(),dto.getMobile())));
        System.out.println("集合--》"+list.size());

    }
}
