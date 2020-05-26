package com.hq.ecmp.mscore.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CharterTypeEnum;
import com.hq.ecmp.constant.CostConfigModeEnum;
import com.hq.ecmp.constant.OrderServiceType;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.cost.CostConfigListResult;
import com.hq.ecmp.mscore.dto.cost.CostConfigQueryDto;
import com.hq.ecmp.mscore.domain.OrderServiceCostDetailRecordInfo;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.CostCalculation;
import com.hq.ecmp.mscore.service.IOrderSettlingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderSettlingInfoServiceImpl implements IOrderSettlingInfoService
{
    @Autowired
    private OrderSettlingInfoMapper orderSettlingInfoMapper;

    @Autowired
    private OrderAddressInfoMapper OrderAddressInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private JourneyInfoMapper journeyInfoMapper;
    @Autowired
    private CarInfoMapper carInfoMapper;
    @Autowired
    private CostConfigInfoMapper costConfigInfoMapper;
    @Autowired
    private OrderWaitTraceInfoMapper orderWaitTraceInfoMapper;
    @Autowired
    private OrderServiceCostDetailRecordInfoMapper costDetailRecordInfoMapper;
    @Autowired
    private OrderServiceImagesInfoMapper imagesInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param billId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderSettlingInfo selectOrderSettlingInfoById(Long billId)
    {
        return orderSettlingInfoMapper.selectOrderSettlingInfoById(billId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderSettlingInfo> selectOrderSettlingInfoList(OrderSettlingInfo orderSettlingInfo)
    {
        return orderSettlingInfoMapper.selectOrderSettlingInfoList(orderSettlingInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderSettlingInfo(OrderSettlingInfo orderSettlingInfo)
    {
        orderSettlingInfo.setCreateTime(DateUtils.getNowDate());
        return orderSettlingInfoMapper.insertOrderSettlingInfo(orderSettlingInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderSettlingInfo(OrderSettlingInfo orderSettlingInfo)
    {
        orderSettlingInfo.setUpdateTime(DateUtils.getNowDate());
        return orderSettlingInfoMapper.updateOrderSettlingInfo(orderSettlingInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param billIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderSettlingInfoByIds(Long[] billIds)
    {
        return orderSettlingInfoMapper.deleteOrderSettlingInfoByIds(billIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param billId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderSettlingInfoById(Long billId)
    {
        return orderSettlingInfoMapper.deleteOrderSettlingInfoById(billId);
    }

    /**
     * 司机端费用上报提交
     * @param orderSettlingInfoVo
     * @param userId
     * @param companyId
     */
    @Override
    public int addExpenseReport(OrderSettlingInfoVo orderSettlingInfoVo, Long userId, Long companyId) throws ParseException {
        GetAllfee getAllfee = new GetAllfee(orderSettlingInfoVo, userId, companyId).invoke();
        if (getAllfee.is()) return -1;
        List<CostConfigInfo> costConfigInfoMap = getAllfee.getCostConfigInfo();
        boolean isInsertOrderConfing = getAllfee.isInsertOrderConfing();
        boolean isMoreDay = getAllfee.isChartered();
        OrderSettlingInfoVo orderSettlingInfo = getAllfee.getOrderSettlingInfo();

        int i = 0;
        //是否为多日组，如果为多日租，则记录费用子表
        if(isMoreDay){
            OrderServiceCostDetailRecordInfo lastRecordInfo =  OrderServiceCostDetailRecordInfo.builder().orderId(orderSettlingInfoVo.getOrderId()).build();
            List<OrderServiceCostDetailRecordInfo> recordInfos = costDetailRecordInfoMapper.getList(lastRecordInfo);
            lastRecordInfo = recordInfos.get(recordInfos.size()-1);
            lastRecordInfo.setAccommodationFee(orderSettlingInfoVo.getHotelExpenseFee());
            lastRecordInfo.setFoodFee(orderSettlingInfo.getRestaurantFee());
            lastRecordInfo.setHighwayTollFee(orderSettlingInfoVo.getHighSpeedFee());
            lastRecordInfo.setRoadAndBridgeFee(orderSettlingInfoVo.getRoadBridgeFee());
            lastRecordInfo.setOthersFee(orderSettlingInfoVo.getOtherFee());
            lastRecordInfo.setStopCarFee(orderSettlingInfoVo.getParkingRateFee());
            lastRecordInfo.setOrderId(orderSettlingInfoVo.getOrderId());
            lastRecordInfo.setMileage(orderSettlingInfoVo.getTotalMileage());
//            lastRecordInfo.setSetMealCost(costConfigInfo.getCombosPrice());
//            lastRecordInfo.setSetMealMileage(costConfigInfo.getCombosMileage());
//            lastRecordInfo.setSetMealTimes(costConfigInfo.getCombosTimes().intValue());
//            lastRecordInfo.setBeyondMileage(beyondMileage);
//            lastRecordInfo.setBeyondTime(beyondTime);
            lastRecordInfo.setTotalFee(orderSettlingInfoVo.getAmount());
            lastRecordInfo.setBeyondMileageFee(orderSettlingInfoVo.getOverMileagePrice());
            lastRecordInfo.setBeyondTimeFee(orderSettlingInfoVo.getOvertimeLongPrice());
            i = costDetailRecordInfoMapper.update(lastRecordInfo);
            if(!orderSettlingInfoVo.getImageUrl().equals(null) && !orderSettlingInfoVo.getImageUrl().equals("")){
                String [] imageUrl = orderSettlingInfoVo.getImageUrl().split(",");
                OrderServiceImagesInfo imagesInfo = OrderServiceImagesInfo.builder()
                        .recordId(lastRecordInfo.getRecordId()).build();
                for (String url:imageUrl){
                    imagesInfo.setImageurl(url);
                    imagesInfoMapper.insert(imagesInfo);
                }
            }
            settlement(orderSettlingInfoVo, recordInfos);
        }
        //判断是否插入主表
        if(isInsertOrderConfing){
            i = orderSettlingInfoMapper.insertOrderSettlingInfoOne(orderSettlingInfoVo);
            if(!orderSettlingInfoVo.getImageUrl().equals(null) && !orderSettlingInfoVo.getImageUrl().equals("")){
                String [] imageUrl = orderSettlingInfoVo.getImageUrl().split(",");
                for (String url:imageUrl){
                    orderSettlingInfoVo.setImageUrl(url);
                    orderSettlingInfoMapper.insertOrderSettlingImageInfo(orderSettlingInfoVo);
                }
            }
        }

        return i;
    }

    //订单总费用结算
    private void settlement(OrderSettlingInfoVo orderSettlingInfoVo, List<OrderServiceCostDetailRecordInfo> recordInfos) {
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal otherFee = BigDecimal.ZERO;
        BigDecimal highSpeedFee = BigDecimal.ZERO;
        BigDecimal hotelExpenseFee = BigDecimal.ZERO;
        BigDecimal parkingRateFee = BigDecimal.ZERO;
        BigDecimal restaurantFee = BigDecimal.ZERO;
        BigDecimal overMileagePrice = BigDecimal.ZERO;
        BigDecimal overtimeLongPrice = BigDecimal.ZERO;
        for (OrderServiceCostDetailRecordInfo recordInfo : recordInfos) {
            amount.add(recordInfo.getTotalFee());
            otherFee.add(recordInfo.getOthersFee());
            highSpeedFee.add(recordInfo.getHighwayTollFee());
            hotelExpenseFee.add(recordInfo.getAccommodationFee());
            parkingRateFee.add(recordInfo.getStopCarFee());
            restaurantFee.add(recordInfo.getFoodFee());
            overMileagePrice.add(recordInfo.getRoadAndBridgeFee());
            overtimeLongPrice.add(recordInfo.getBeyondTimeFee());
        }
        String json = costPrice(orderSettlingInfoVo);
        //计算总费用
        orderSettlingInfoVo.setAmountDetail(json);
        orderSettlingInfoVo.setAmount(amount);
        orderSettlingInfoVo.setOtherFee(otherFee);
        orderSettlingInfoVo.setHighSpeedFee(highSpeedFee);
        orderSettlingInfoVo.setHotelExpenseFee(hotelExpenseFee);
        orderSettlingInfoVo.setParkingRateFee(parkingRateFee);
        orderSettlingInfoVo.setRestaurantFee(restaurantFee);
        orderSettlingInfoVo.setOverMileagePrice(overMileagePrice);
        orderSettlingInfoVo.setOvertimeLongPrice(overtimeLongPrice);
    }

    @Override
    public BigDecimal getAllFeeAmount(OrderSettlingInfoVo orderSettlingInfoVo, Long userId, Long companyId) throws ParseException {
        return  new GetAllfee(orderSettlingInfoVo, userId, companyId).invoke().getOrderSettlingInfo().getAmount();
    }
    /**
     * 取消费格式化
     * @param orderSettlingInfoVo
     * @param personalCancellationFee
     * @param enterpriseCancellationFee
     * @return
     */
    @Override
    public String formatCostFee(OrderSettlingInfoVo orderSettlingInfoVo, BigDecimal personalCancellationFee, BigDecimal enterpriseCancellationFee) {
        Map map = new HashMap();
        List list= new ArrayList();
        //默认个人取消费用为0
        //BigDecimal personalCancellationFee = BigDecimal.ZERO;
        //默认个人取消费用为0
        //BigDecimal enterpriseCancellationFee = BigDecimal.ZERO;
        //个人取消费
        //OrderSettling.setPersonalCancellationFee(personalCancellationFee);
        Map personalCancellation = new HashMap();
        personalCancellation.put("cost",personalCancellationFee.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        personalCancellation.put("typeName","personalAmount");
        list.add(personalCancellation);
        //企业取消费
        //OrderSettling.setEnterpriseCancellationFee(enterpriseCancellationFee);
        Map enterpriseCancellation = new HashMap();
        enterpriseCancellation.put("cost",enterpriseCancellationFee.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        enterpriseCancellation.put("typeName","ownerAmount");
        list.add(enterpriseCancellation);
        map.put("otherCost",list);
        String json= JSON.toJSONString(map);
        return json;
    }



    /**
     * 成本价详情
     * @param orderSettlingInfoVo
     * @return
     */
    public String costPrice(OrderSettlingInfoVo orderSettlingInfoVo){
        Map map = new HashMap();
        List list= new ArrayList();
        String serviceType = orderSettlingInfoVo.getServiceType();
        //默认个人取消费用为0
//        BigDecimal personalCancellationFee = BigDecimal.ZERO;
        //默认个人取消费用为0
//        BigDecimal enterpriseCancellationFee = BigDecimal.ZERO;
        //路桥费
        //OrderSettling.setRoadBridgeFee(orderSettlingInfoVo.getRoadBridgeFee());
        //Map roadBridgeFee = new HashMap();
        //roadBridgeFee.put("cost",orderSettlingInfoVo.getRoadBridgeFee().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //roadBridgeFee.put("typeName","路桥费");
        //list.add(roadBridgeFee);
        //高速费
        //OrderSettling.setHighSpeedFee(orderSettlingInfoVo.getHighSpeedFee());
        //Map highSpeedFee = new HashMap();
        //highSpeedFee.put("cost",orderSettlingInfoVo.getHighSpeedFee().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //highSpeedFee.put("typeName","高速费");
        //list.add(highSpeedFee);
        //停车费
        //OrderSettling.setParkingRateFee(orderSettlingInfoVo.getParkingRateFee());
        //Map parkingRateFee = new HashMap();
        //parkingRateFee.put("cost",orderSettlingInfoVo.getParkingRateFee().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //parkingRateFee.put("typeName","停车费");
        //list.add(parkingRateFee);
        //住宿费
        //OrderSettling.setHotelExpenseFee(orderSettlingInfoVo.getHotelExpenseFee());
        //Map hotelExpenseFee = new HashMap();
        //hotelExpenseFee.put("cost",orderSettlingInfoVo.getHotelExpenseFee().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //hotelExpenseFee.put("typeName","住宿费");
        //list.add(hotelExpenseFee);
        //餐饮费
        //OrderSettling.setRestaurantFee(orderSettlingInfoVo.getRestaurantFee());
        //Map restaurantFee = new HashMap();
        //restaurantFee.put("cost",orderSettlingInfoVo.getRestaurantFee().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //restaurantFee.put("typeName","餐饮费");
        //list.add(restaurantFee);
        //超里程价格
        //OrderSettling.setOverMileagePrice(orderSettlingInfoVo.getOverMileagePrice());
        Map overMileagePrice = new HashMap();
        overMileagePrice.put("cost",orderSettlingInfoVo.getOverMileagePrice().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        overMileagePrice.put("typeName","超里程价格");
        list.add(overMileagePrice);
        //超时长价格
        //OrderSettling.setOvertimeLongPrice(orderSettlingInfoVo.getOvertimeLongPrice());
        Map overtimeLongPrice = new HashMap();
        overtimeLongPrice.put("cost",orderSettlingInfoVo.getOvertimeLongPrice().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        overtimeLongPrice.put("typeName","超时长价格");
        list.add(overtimeLongPrice);
        //起步价
        //OrderSettling.setStartingPrice(orderSettlingInfoVo.getStartingPrice());
        Map startingPrice = new HashMap();
        startingPrice.put("cost",orderSettlingInfoVo.getStartingPrice().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        startingPrice.put("typeName","起步价");
        list.add(startingPrice);
        //等待费
        //OrderSettling.setWaitingFee(orderSettlingInfoVo.getWaitingFee());
        if (serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_CHARTERED.getBcState())) {
            Map waitingFee = new HashMap();
            waitingFee.put("cost", orderSettlingInfoVo.getWaitingFee().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            waitingFee.put("typeName", "等待费");
            list.add(waitingFee);
        }
        //个人取消费
        //OrderSettling.setPersonalCancellationFee(personalCancellationFee);
        //Map personalCancellation = new HashMap();
        //personalCancellation.put("cost",personalCancellationFee.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //personalCancellation.put("typeName","个人取消费");
        //list.add(personalCancellation);
        //企业取消费
        //OrderSettling.setEnterpriseCancellationFee(enterpriseCancellationFee);
        //Map enterpriseCancellation = new HashMap();
        //enterpriseCancellation.put("cost",enterpriseCancellationFee.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //enterpriseCancellation.put("typeName","企业取消费");
        //list.add(enterpriseCancellation);
        map.put("otherCost",list);
        String json= JSON.toJSONString(map);
        return json;
    }

    /**
     * 价外费详情
     * @param orderSettlingInfoVo
     * @return
     */
    public String extraPrice(OrderSettlingInfoVo orderSettlingInfoVo){
        Map map = new HashMap();
        List list= new ArrayList();
        //默认个人取消费用为0
//        BigDecimal personalCancellationFee = BigDecimal.ZERO;
        //默认个人取消费用为0
//        BigDecimal enterpriseCancellationFee = BigDecimal.ZERO;
        //路桥费
        //OrderSettling.setRoadBridgeFee(orderSettlingInfoVo.getRoadBridgeFee());
        Map roadBridgeFee = new HashMap();
        roadBridgeFee.put("cost",orderSettlingInfoVo.getRoadBridgeFee().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        roadBridgeFee.put("typeName","路桥费");
        list.add(roadBridgeFee);
        //高速费
        //OrderSettling.setHighSpeedFee(orderSettlingInfoVo.getHighSpeedFee());
        Map highSpeedFee = new HashMap();
        highSpeedFee.put("cost",orderSettlingInfoVo.getHighSpeedFee().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        highSpeedFee.put("typeName","高速费");
        list.add(highSpeedFee);
        //停车费
        //OrderSettling.setParkingRateFee(orderSettlingInfoVo.getParkingRateFee());
        Map parkingRateFee = new HashMap();
        parkingRateFee.put("cost",orderSettlingInfoVo.getParkingRateFee().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        parkingRateFee.put("typeName","停车费");
        list.add(parkingRateFee);
        //住宿费
        //OrderSettling.setHotelExpenseFee(orderSettlingInfoVo.getHotelExpenseFee());
        Map hotelExpenseFee = new HashMap();
        hotelExpenseFee.put("cost",orderSettlingInfoVo.getHotelExpenseFee().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        hotelExpenseFee.put("typeName","住宿费");
        list.add(hotelExpenseFee);
        //餐饮费
        //OrderSettling.setRestaurantFee(orderSettlingInfoVo.getRestaurantFee());
        Map restaurantFee = new HashMap();
        restaurantFee.put("cost",orderSettlingInfoVo.getRestaurantFee().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        restaurantFee.put("typeName","餐饮费");
        list.add(restaurantFee);
        //超里程价格
        //OrderSettling.setOverMileagePrice(orderSettlingInfoVo.getOverMileagePrice());
        //Map overMileagePrice = new HashMap();
        //overMileagePrice.put("cost",orderSettlingInfoVo.getOverMileagePrice().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //overMileagePrice.put("typeName","超里程价格");
        //list.add(overMileagePrice);
        //超时长价格
        //OrderSettling.setOvertimeLongPrice(orderSettlingInfoVo.getOvertimeLongPrice());
        //Map overtimeLongPrice = new HashMap();
        //overtimeLongPrice.put("cost",orderSettlingInfoVo.getOvertimeLongPrice().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //overtimeLongPrice.put("typeName","超时长价格");
        //list.add(overtimeLongPrice);
        //起步价
        //OrderSettling.setStartingPrice(orderSettlingInfoVo.getStartingPrice());
        //Map startingPrice = new HashMap();
        //startingPrice.put("cost",orderSettlingInfoVo.getStartingPrice().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //startingPrice.put("typeName","起步价");
        //list.add(startingPrice);
        //等待费
        //OrderSettling.setWaitingFee(orderSettlingInfoVo.getWaitingFee());
        //Map waitingFee = new HashMap();
        //waitingFee.put("cost",orderSettlingInfoVo.getWaitingFee().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //waitingFee.put("typeName","等待费");
        //list.add(waitingFee);
        //个人取消费
        //OrderSettling.setPersonalCancellationFee(personalCancellationFee);
        //Map personalCancellation = new HashMap();
        //personalCancellation.put("cost",personalCancellationFee.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //personalCancellation.put("typeName","个人取消费");
        //list.add(personalCancellation);
        //企业取消费
        //OrderSettling.setEnterpriseCancellationFee(enterpriseCancellationFee);
        //Map enterpriseCancellation = new HashMap();
        //enterpriseCancellation.put("cost",enterpriseCancellationFee.setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        //enterpriseCancellation.put("typeName","企业取消费");
        //list.add(enterpriseCancellation);
        map.put("otherCost",list);
        String json= JSON.toJSONString(map);
        return json;
    }

    //根据订单设置计算价格
    private class GetAllfee {
        private boolean myResult;
        private OrderSettlingInfoVo orderSettlingInfoVo;
        private Long userId;
        private Long companyId;
        private List<CostConfigInfo> costConfigInfoList;
        private boolean isInsertOrderConfing;
        private boolean isChartered;
        private OrderSettlingInfoVo orderSettlingInfo;

        public GetAllfee(OrderSettlingInfoVo orderSettlingInfoVo, Long userId, Long companyId) {
            this.orderSettlingInfoVo = orderSettlingInfoVo;
            this.userId = userId;
            this.companyId = companyId;
        }

        boolean is() {
            return myResult;
        }

        public List<CostConfigInfo> getCostConfigInfo() {
            return costConfigInfoList;
        }

        public boolean isInsertOrderConfing() {
            return isInsertOrderConfing;
        }

        public boolean isChartered() {
            return isChartered;
        }

        public OrderSettlingInfoVo getOrderSettlingInfo() {
            return orderSettlingInfo;
        }

        public GetAllfee invoke() throws ParseException {
            //计算等待时长
            BigDecimal waitingTime = orderWaitTraceInfoMapper.selectOrderWaitingTimeById(orderSettlingInfoVo.getOrderId());
            orderSettlingInfoVo.setWaitingTime(waitingTime);

            //查询该订单的记录  城市  服务类型  车型级别  包车类型 公司id
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderSettlingInfoVo.getOrderId());
            orderSettlingInfoVo.setServiceType(orderInfo.getServiceType());

            //查询所在的城市
            OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
            orderAddressInfo.setOrderId(orderSettlingInfoVo.getOrderId());
            OrderAddressInfo addressStartInfo = OrderAddressInfoMapper.queryOrderStartAndEndInfo(orderAddressInfo);

            //车型级别
            CarInfo carInfo = carInfoMapper.selectCarInfoById(orderInfo.getCarId());
            //筛选出成本数据model
            CostConfigQueryDto costConfigQueryDto = new CostConfigQueryDto();
            //公司id
            costConfigQueryDto.setCompanyId(companyId);
            //城市
            costConfigQueryDto.setCityCode(addressStartInfo.getCityPostalCode());
            //服务类型
            costConfigQueryDto.setServiceType(orderInfo.getServiceType());
            //车型级别
            costConfigQueryDto.setCarTypeId(carInfo.getCarTypeId());

            costConfigInfoList = new ArrayList<>();
            //是否插入总表
            isInsertOrderConfing = true;
            //是否为插入费用子表
            isChartered = false;
            //服务类型为包车
            if (orderInfo.getServiceType().equals(OrderServiceType.ORDER_SERVICE_TYPE_CHARTERED.getBcState())) {
                //服务类型属于包车
                //包车类型:半日租，整日租;多日租
                JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
                //车辆类型
                //T000  非包车 T001 半日租（4小时） T002 整日租（8小时） T009多日租
                String carType = journeyInfo.getCharterCarType();
                //判断多日租中当天是为半日组还是整日租
                if(CharterTypeEnum.MORE_RENT_TYPE.equals(carType)){
                    Date startDate = journeyInfo.getStartDate();
                    Date endDate = journeyInfo.getEndDate();
                    Date nowDate = new Date();
                    //判断当天是否为起始时间
                    if(DateUtils.isSameDay(startDate,nowDate)){
                        if(startDate.compareTo(DateUtils.parseDate(DateUtils.formatDate(startDate,DateUtils.YYYY_MM_DD)+" 12:00:00"))>-1){
                            carType = CharterTypeEnum.OVERALL_RENT_TYPE.getKey();
                        }else{
                            carType = CharterTypeEnum.HALF_DAY_TYPE.getKey();
                        }
                    }
                    //判断是当天是否为结束时间
                    if(DateUtils.isSameDay(endDate,nowDate)){
                        if(endDate.compareTo(DateUtils.parseDate(DateUtils.formatDate(startDate,DateUtils.YYYY_MM_DD)+" 12:00:00"))>-1){
                            carType = CharterTypeEnum.OVERALL_RENT_TYPE.getKey();
                        }else{
                            carType = CharterTypeEnum.HALF_DAY_TYPE.getKey();
                        }
                    }

                }
                //包车类型
                costConfigQueryDto.setRentType(carType);
                //是否为同一车队
                Map<String,String> driverCostInfo = costConfigInfoMapper.getDriverInfo(orderInfo.getDriverId());
                Map<String,String> carCostInfo = costConfigInfoMapper.getCarInfo(orderInfo.getCarId());

                //判断是否为
                if(driverCostInfo.get("driverCarGroupId").equals(carCostInfo.equals("carCarGroupId"))){
                    //车队使用模式
                    costConfigQueryDto.setCarGroupUserMode(CostConfigModeEnum.Config_mode_CA00.getKey());
                    //车队id
                    costConfigQueryDto.setCarGroupId(carInfo.getCarGroupId());
                    CostConfigInfo costConfigInfo = costConfigInfoMapper.selectCostConfigInfo(costConfigQueryDto);
                    costConfigInfoList.add(costConfigInfo);
                }else{
                    //仅用车
                    costConfigQueryDto.setCarGroupUserMode(CostConfigModeEnum.Config_mode_CA01.getKey());
                    //车队id
                    costConfigQueryDto.setCarGroupId(Long.parseLong(carCostInfo.get("carCarGroupId")));
                    CostConfigInfo costConfigInfo1 = costConfigInfoMapper.selectCostConfigInfo(costConfigQueryDto);
                    costConfigInfoList.add(costConfigInfo1);
                    if(driverCostInfo!=null){
                        //仅用驾驶员
                        costConfigQueryDto.setCarGroupUserMode(CostConfigModeEnum.Config_mode_CA10.getKey());
                        //车队id
                        costConfigQueryDto.setCarGroupId(Long.parseLong(driverCostInfo.get("carCarGroupId")));
                        CostConfigInfo costConfigInfo2 = costConfigInfoMapper.selectCostConfigInfo(costConfigQueryDto);
                        costConfigInfoList.add(costConfigInfo2);
                    }
                }
                //判断是否为包车业务
                if("T001".equals(carType) ||"T002".equals(carType)||"T009".equals(carType)){
                    isChartered = true;
                    //订单预计结束时间
                    if(DateUtils.isBeforeNowDate(journeyInfo.getEndDate())){
                        isInsertOrderConfing = false;
                    }
                }
            } else {
                List<CostConfigListResult> costConfigListResult = costConfigInfoMapper.selectCostConfigInfoList(costConfigQueryDto);
                if(costConfigListResult.isEmpty()){
                    myResult = true;
                    return this;
                }
                costConfigQueryDto.setCostId(costConfigListResult.get(0).getCostId());
                CostConfigInfo costConfigInfo = costConfigInfoMapper.selectCostConfigInfo(costConfigQueryDto);
                costConfigInfoList.add(costConfigInfo);
            }
            //计算成本的方法
            CostCalculation calculator = new CostCalculator();
            orderSettlingInfo = calculator.calculator(costConfigInfoList, orderSettlingInfoVo);
            //落库到订单结算信息表
            String json = OrderSettlingInfoServiceImpl.this.costPrice(orderSettlingInfoVo);//成本价详情
            String extraPrice = OrderSettlingInfoServiceImpl.this.extraPrice(orderSettlingInfoVo);//价外费详情
            orderSettlingInfoVo.setCreateTime(DateUtils.getNowDate());
            orderSettlingInfoVo.setCreateBy(userId.toString());
            orderSettlingInfoVo.setAmountDetail(json);
            orderSettlingInfoVo.setOutPrice(extraPrice);
            orderSettlingInfoVo.setAmount(orderSettlingInfoVo.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
            myResult = false;
            return this;
        }
    }
}
