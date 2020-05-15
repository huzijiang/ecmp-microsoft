package com.hq.ecmp.ms.api.controller.base;

import com.hq.ecmp.ms.api.controller.order.OrderController;
import com.hq.ecmp.mscore.dto.ContactorDto;
import com.hq.ecmp.mscore.service.IDriverOrderService;
import com.hq.ecmp.mscore.service.IDriverWorkInfoService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.service.IRegimeInfoService;
import com.hq.ecmp.mscore.vo.CarVO;
import com.hq.ecmp.mscore.vo.DriverWorkInfoDetailVo;
import com.hq.ecmp.mscore.vo.DriverWorkInfoMonthVo;
import com.hq.ecmp.mscore.vo.OfficialOrderReVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/4/9 18:31
 * @Version 1.0
 */
@SpringBootTest
public class ScTest {

    @Resource
    private IOrderInfoService iOrderInfoService;
    @Resource
    private IRegimeInfoService iRegimeInfoService;
    @Resource
    private IDriverWorkInfoService iDriverWorkInfoService;
    @Resource
    private IDriverOrderService iDriverOrderService;
    @Resource
    private OrderController orderController;



    @Test
    public void testOrderExpired(){
        System.out.println("aaaaaaaaaaaaaa");
        iOrderInfoService.checkOrderIsExpired();
        System.out.println("aaaaaaaaaaaaaa");
    }

//    @Test
//    public void bugcc(){
//        iRegimeInfoService.findRegimeInfoListByUserId(200321l, 147l);
//    }

    @Test
    public void  platOrderTest(){
        OfficialOrderReVo officialOrderReVo = new OfficialOrderReVo();
        officialOrderReVo.setIsDispatch(2);
        officialOrderReVo.setPowerId(1981l);
        officialOrderReVo.setCarLevel("P001,P002,P003");
        try {
            iOrderInfoService.officialOrder(officialOrderReVo,200349l);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void driverWorkTest() throws ParseException {
        String d = "2020-04-21";
        DriverWorkInfoDetailVo driverWorkInfoDetailVo = new DriverWorkInfoDetailVo();
        DriverWorkInfoMonthVo driverWorkInfoMonthVo = new DriverWorkInfoMonthVo();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        driverWorkInfoMonthVo.setCalendarDate(simpleDateFormat.parse(d));
        driverWorkInfoMonthVo.setDriverId(3243L);
        driverWorkInfoMonthVo.setWorkId(181523L);
        driverWorkInfoMonthVo.setWorkState("X003");
        List<DriverWorkInfoMonthVo> driverWorkInfoMonthVos = new ArrayList<>();
        driverWorkInfoMonthVos.add(driverWorkInfoMonthVo);
        driverWorkInfoDetailVo.setDriverWorkInfoMonthVos(driverWorkInfoMonthVos);
        iDriverWorkInfoService.updateDriverWorkDetailMonth(driverWorkInfoDetailVo,1L);
    }

    @Test
    public void contactCarGroupTest(){

        List<ContactorDto> infoWithCarGroup = iDriverOrderService.getInfoWithCarGroup("1511");
        System.out.println(infoWithCarGroup);

    }

    @Test
    public void cs(){
        String token ="eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImM3NTAyMTA1LWVjMDMtNGEwYS05NTkwLWZiYTIwY2RmODEyNiJ9.M11F5MosOpnIvhJzlPwIeDeX95X4IUEglQorKTfenGMPCekkNiqWLEjZu1vMKiJ0AJuL43fP4AQmrfs-Q5ilKQ";
    }
    @Test
    public void a1(){
        orderController.letPlatCallTaxi("19909", null);
//        try {
////            iOrderInfoService.platCallTaxiParamValid(99L, "1", "null");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
