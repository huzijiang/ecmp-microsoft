package com.hq.ecmp.mscore.service.impl;

import com.hq.api.system.mapper.SysUserMapper;
import com.hq.api.system.service.ISysDeptService;
import com.hq.api.system.service.ISysUserService;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.TreeUtil;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.CarModeEnum;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.constant.enumerate.DriverStateEnum;
import com.hq.ecmp.mscore.bo.CarInfoPlus;
import com.hq.ecmp.mscore.bo.DriverInfoPlus;
import com.hq.ecmp.mscore.bo.OrderInfoPlus;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.mapper.CarGroupInfoMapper;
import com.hq.ecmp.mscore.mapper.CarInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.service.StatisticsAbilityService;
import com.hq.ecmp.util.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class StatisticsAbilityServiceImpl implements StatisticsAbilityService {
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private CarInfoMapper carInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;
    @Autowired
    private StatisticsOrderServiceImpl statisticsOrderService;
    @Autowired(required = false)
    private SysUserMapper userMapper;
    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private ISysUserService iSysUserService;

    @Override
    public ApiResponse getCarGroupTreeByDeptIds(StatisticsParam statisticsParam) {
        List<Map> list = carGroupInfoMapper.getCarGroupTreeByDeptIds(statisticsParam.getDeptIds());
        List<String> deptIds = new ArrayList<>();
        list.stream().forEach(x->deptIds.add(x.get("pid").toString()));
        while (deptIds.size()>0){
            List<Map> newList = userMapper.selectDeptInfoByIds(deptIds);
            list.addAll(newList);
            deptIds.clear();
            newList.stream().forEach(x->deptIds.add(x.get("pid").toString()));
        }
        Collections.reverse(list);
        return ApiResponse.success(TreeUtil.getTreeByList(list));
    }

    @Override
    public ApiResponse<Map<String,String>> driverSum(StatisticsParam statisticsParam) {
        Map<String,Integer> map = getInitMapData();
        baseDataFilter(x->{
            map.put("sum",map.get("sum")+1);
            map.put("onLineCount",map.get("onLineCount")+x.getOnLine());
            map.put("orderSum",map.get("orderSum")+x.getCompleteOrderCount());
            map.put("onLineDuration",map.get("onLineDuration")+ (int) Math.ceil(x.getOnLineDuration()));
        },statisticsParam);
        return ApiResponse.success(computeMap(map,DateFormatUtils.getDayDuration(statisticsParam.getBeginDate(),statisticsParam.getEndDate())));
    }

    @Override
    public ApiResponse driverSumByDate(StatisticsParam statisticsParam) {
        //时间轴
        List<String> list = DateFormatUtils.sliceUpDateRange(statisticsParam.getBeginDate(),statisticsParam.getEndDate());
        Map<String,Map<String,String>> mapMap = new LinkedHashMap<>();
        list.stream().forEach(x->{
            StatisticsParam temp = new StatisticsParam();
            temp.setDeptIds(statisticsParam.getDeptIds());
            temp.setCarGroupIds(statisticsParam.getCarGroupIds());
            temp.setBeginDate(x);
            temp.setEndDate(x);
            mapMap.put(x,driverSum(temp).getData());
        });
        return ApiResponse.success(mapMap);
    }

    @Override
    public ApiResponse driverSumByDimension(StatisticsParam statisticsParam) {
        //时间轴
        List<String> list = DateFormatUtils.sliceUpDateRange(statisticsParam.getBeginDate(),statisticsParam.getEndDate());
        Map resultMap = new LinkedHashMap();
        list.stream().forEach(x->{
            Map<String,Map<String,String>> mapMap = new LinkedHashMap<>();
            //公司维度
            if(statisticsParam.getType()==0){
                List<Map> mapList = userMapper.selectDeptInfoByIds(statisticsParam.getDeptIds());
                mapList.stream().forEach(y->{
                    StatisticsParam temp = new StatisticsParam();
                    List<Long> longs = new ArrayList<>();
                    longs.add(Long.valueOf(y.get("id").toString()));
                    temp.setDeptIds(longs);
                    temp.setCarGroupIds(statisticsParam.getCarGroupIds());
                    temp.setBeginDate(x);
                    temp.setEndDate(x);
                    mapMap.put(y.get("showname").toString(),driverSum(temp).getData());
                });
            }else{
                //车队维度
                List<Map> mapList = carGroupInfoMapper.getCarGroupTreeByCarIds(statisticsParam.getCarGroupIds());
                mapList.stream().forEach(y->{
                    StatisticsParam temp = new StatisticsParam();
                    List<Long> longs = new ArrayList<>();
                    longs.add(Long.valueOf(y.get("id").toString()));
                    temp.setDeptIds(statisticsParam.getDeptIds());
                    temp.setCarGroupIds(longs);
                    temp.setBeginDate(x);
                    temp.setEndDate(x);
                    mapMap.put(y.get("showname").toString(),driverSum(temp).getData());
                });
            }
            resultMap.put(x,mapMap);
        });
        return ApiResponse.success(resultMap);
    }

    @Override
    public ApiResponse<Map<String,String>> carSum(StatisticsParam statisticsParam) {
        Map<String,Integer> map = getCarInitMapData();
        baseCarDataFilter(x->{
            map.put("sum",map.get("sum")+1);
            if(x.getCarInfo().getState().equals(CarConstant.START_CAR)){
                map.put("usableCar",map.get("usableCar")+1);
            }
            map.put("useCar",map.get("useCar")+x.getUseCar());
            map.put("orderSum",map.get("orderSum")+x.getCompleteOrderCount());
            map.put("mileageSum",map.get("mileageSum")+x.getMileageSum());
        },statisticsParam);
        return ApiResponse.success(computeCarMap(map,DateFormatUtils.getDayDuration(statisticsParam.getBeginDate(),statisticsParam.getEndDate())));
    }

    @Override
    public ApiResponse carSumByDate(StatisticsParam statisticsParam) {
        //时间轴
        List<String> list = DateFormatUtils.sliceUpDateRange(statisticsParam.getBeginDate(),statisticsParam.getEndDate());
        Map<String,Map<String,String>> mapMap = new LinkedHashMap<>();
        list.stream().forEach(x->{
            StatisticsParam temp = new StatisticsParam();
            temp.setDeptIds(statisticsParam.getDeptIds());
            temp.setCarGroupIds(statisticsParam.getCarGroupIds());
            temp.setBeginDate(x);
            temp.setEndDate(x);
            mapMap.put(x,carSum(temp).getData());
        });
        return ApiResponse.success(mapMap);
    }

    @Override
    public ApiResponse carSumByByDimension(StatisticsParam statisticsParam) {
        //时间轴
        List<String> list = DateFormatUtils.sliceUpDateRange(statisticsParam.getBeginDate(),statisticsParam.getEndDate());
        Map resultMap = new LinkedHashMap();
        list.stream().forEach(x->{
            Map<String,Map<String,String>> mapMap = new LinkedHashMap<>();
            //公司维度
            if(statisticsParam.getType()==0){
                List<Map> mapList = userMapper.selectDeptInfoByIds(statisticsParam.getDeptIds());
                mapList.stream().forEach(y->{
                    StatisticsParam temp = new StatisticsParam();
                    List<Long> longs = new ArrayList<>();
                    longs.add(Long.valueOf(y.get("id").toString()));
                    temp.setDeptIds(longs);
                    temp.setCarGroupIds(statisticsParam.getCarGroupIds());
                    temp.setBeginDate(x);
                    temp.setEndDate(x);
                    mapMap.put(y.get("showname").toString(),carSum(temp).getData());
                });
            }else{
                //车队维度
                List<Map> mapList = carGroupInfoMapper.getCarGroupTreeByCarIds(statisticsParam.getCarGroupIds());
                mapList.stream().forEach(y->{
                    StatisticsParam temp = new StatisticsParam();
                    List<Long> longs = new ArrayList<>();
                    longs.add(Long.valueOf(y.get("id").toString()));
                    temp.setDeptIds(statisticsParam.getDeptIds());
                    temp.setCarGroupIds(longs);
                    temp.setBeginDate(x);
                    temp.setEndDate(x);
                    mapMap.put(y.get("showname").toString(),carSum(temp).getData());
                });
            }
            resultMap.put(x,mapMap);
        });
        return ApiResponse.success(resultMap);
    }

    @Override
    public ApiResponse<Map<String,String>> dispatchSum(StatisticsParam statisticsParam) {
        Map<String,Integer> map = getDispatchMapData();
        baseDispatchDataFilter(x->{
            map.put("sum",map.get("sum")+1);
            map.put("dispatchCount",map.get("dispatchCount")+x.getDispatchCount());
            map.put("dispatchDuration",map.get("dispatchDuration")+x.getDispatchDuration());
            map.put("orderCloseCount",map.get("orderCloseCount")+x.getOrderCloseCount());
            map.put("Mileage",map.get("Mileage")+x.getMileage());
            map.put("OrderDuration",map.get("OrderDuration")+(int)Math.ceil(x.getOrderDuration()));
        },statisticsParam);
        return ApiResponse.success(computeDispatchMapData(map));
    }

    @Override
    public ApiResponse dispatchSumByDate(StatisticsParam statisticsParam) {
        //时间轴
        List<String> list = DateFormatUtils.sliceUpDateRange(statisticsParam.getBeginDate(),statisticsParam.getEndDate());
        Map<String,Map<String,String>> mapMap = new LinkedHashMap<>();
        list.stream().forEach(x->{
            StatisticsParam temp = new StatisticsParam();
            temp.setDeptIds(statisticsParam.getDeptIds());
            temp.setCarGroupIds(statisticsParam.getCarGroupIds());
            temp.setBeginDate(x);
            temp.setEndDate(x);
            mapMap.put(x,dispatchSum(temp).getData());
        });
        return ApiResponse.success(mapMap);
    }

    private Map<String,Integer> getDispatchMapData(){
        Map<String,Integer> map = new LinkedHashMap();
        //订单量
        map.put("sum",0);
        //派单量
        map.put("dispatchCount",0);
        map.put("dispatchCountProbability",0);
        //派单时长
        map.put("dispatchDuration",0);
        map.put("avgDispatchDuration",0);
        //完单量
        map.put("orderCloseCount",0);
        map.put("orderCloseCountProbability",0);
        //总里程
        map.put("Mileage",0);
        //订单耗时
        map.put("OrderDuration",0);
        return map;
    }
    private Map<String,String> computeDispatchMapData(Map<String,Integer> map){
        Map<String,String> resultMap = new LinkedHashMap();
        //订单量
        resultMap.put("sum",map.get("sum").toString());
        //派单量
        resultMap.put("dispatchCount",map.get("dispatchCount").toString());
        resultMap.put("dispatchCountProbability",String.format("%.2f",optNan(map.get("dispatchCount").doubleValue()/map.get("sum").doubleValue()*100)));
        //派单时长
        resultMap.put("dispatchDuration",map.get("dispatchDuration").toString());
        resultMap.put("avgDispatchDuration",String.format("%.2f",optNan(map.get("dispatchDuration").doubleValue()/map.get("dispatchCount").doubleValue())));
        //完单量
        resultMap.put("orderCloseCount",map.get("orderCloseCount").toString());
        resultMap.put("orderCloseCountProbability",String.format("%.2f",optNan(map.get("orderCloseCount").doubleValue()/map.get("sum").doubleValue()*100)));
        //总里程
        resultMap.put("Mileage",map.get("Mileage").toString());
        //订单耗时
        resultMap.put("OrderDuration",map.get("OrderDuration").toString());
        return resultMap;
    }

    //车辆初始化map数据
    private Map<String,Integer> getCarInitMapData(){
        Map<String,Integer> map = new LinkedHashMap();
        //车辆总数
        map.put("sum",0);
        //可用车辆
        map.put("usableCar",0);
        //车辆可用率
        map.put("usableCarProbability",0);
        //使用车辆
        map.put("useCar",0);
        //车辆利用率
        map.put("useCarProbability",0);
        //总服务订单数
        map.put("orderSum",0);
        //总运行里程
        map.put("mileageSum",0);
        //车均服务订单数
        map.put("avgOrderSum",0);
        //车均运行里程
        map.put("avgMileageSum",0);
        return map;
    }
    //计算车辆map数据
    private Map<String,String> computeCarMap(Map<String,Integer> map,int dayCount){
        Map<String,String> resultMap = new LinkedHashMap<>();
        resultMap.put("sum",map.get("sum").toString());
        resultMap.put("usableCar",map.get("usableCar").toString());
        resultMap.put("usableCarProbability",String.format("%.2f",optNan(map.get("usableCar").doubleValue()/map.get("sum").doubleValue()*100)));
        resultMap.put("useCar",map.get("useCar").toString());
        resultMap.put("useCarProbability",String.format("%.2f",optNan(map.get("useCar").doubleValue()/map.get("sum").doubleValue()*100)));
        resultMap.put("orderSum",map.get("orderSum").toString());
        resultMap.put("mileageSum",map.get("mileageSum").toString());
        resultMap.put("avgOrderSum",String.format("%.2f",optNan(map.get("orderSum").doubleValue()/map.get("useCar").doubleValue()/dayCount)));
        resultMap.put("avgMileageSum",String.format("%.2f",optNan(map.get("mileageSum").doubleValue()/map.get("useCar").doubleValue()/dayCount)));
        return resultMap;
    }

    //驾驶员初始化map数据
    private Map<String,Integer> getInitMapData(){
        Map<String,Integer> map = new LinkedHashMap();
        //总数
        map.put("sum",0);
        //驾驶员上线数
        map.put("onLineCount",0);
        //驾驶员上线率
        map.put("onLineProbability",0);
        //总服务订单数
        map.put("orderSum",0);
        //总在线时长
        map.put("onLineDuration",0);
        //人均服务订单数
        map.put("avgOrderSum",0);
        //人均每天在线时长
        map.put("avgOnLineDuration",0);
        return map;
    }
    //驾驶员数据计算
    private Map<String,String> computeMap(Map<String,Integer> map,int dayCount){
        Map<String,String> resultMap = new LinkedHashMap<>();
        //上线率
        resultMap.put("onLineProbability",String.format("%.2f",optNan(map.get("onLineCount").doubleValue()/map.get("sum").doubleValue()*100)));
        //人均服务订单数
        resultMap.put("avgOrderSum",String.format("%.2f",optNan(map.get("orderSum").doubleValue()/map.get("onLineCount").doubleValue()/dayCount)));
        //人均每天在线时长
        resultMap.put("avgOnLineDuration",String.format("%.2f",optNan(map.get("onLineDuration").doubleValue()/map.get("onLineCount").doubleValue()/dayCount)));
        resultMap.put("sum",map.get("sum").toString());
        resultMap.put("onLineCount",map.get("onLineCount").toString());
        resultMap.put("orderSum",map.get("orderSum").toString());
        resultMap.put("onLineDuration",map.get("onLineDuration").toString());
        return resultMap;
    }
    private Double optNan(Double param){
        return Double.isNaN(param)?0.00:param;
    }

    /**
     * 驾驶员-基础条件数据过滤
     * @param consumer
     * @param statisticsParam
     */
    private void baseDataFilter(Consumer<DriverInfoPlus> consumer, StatisticsParam statisticsParam){
        //所有司机 车队包含的所有司机
        List<DriverInfo> driverInfos = driverInfoMapper.selectDriverInfoListByIds(statisticsParam.getCarGroupIds());
        //所有司机id
        List<Long> driverIds = driverInfos.stream().map(DriverInfo::getDriverId).collect(Collectors.toList());
        Map<Long,DriverInfoPlus> map = new HashMap<>();
        //过滤订单，完善时长和完单数据
        statisticsOrderService.baseDataFilter(x->{
            if(driverIds.contains(x.getDriverId())){
                //驾驶员维度 总完单量和总订单时长计算
                if(map.containsKey(x.getDriverId())){
                    DriverInfoPlus driverInfoPlus = map.get((x.getDriverId()));
                    driverInfoPlus.setCompleteOrderCount(driverInfoPlus.getCompleteOrderCount()+1);
                    driverInfoPlus.setOnLineDuration(driverInfoPlus.getOnLineDuration()+orderInfoMapper.getOrderDurationById(x.getOrderId()));
                    map.put(x.getDriverId(),driverInfoPlus);
                }else {
                    DriverInfoPlus driverInfoPlus = new DriverInfoPlus();
                    driverInfoPlus.setCompleteOrderCount(1);
                    driverInfoPlus.setOnLineDuration(orderInfoMapper.getOrderDurationById(x.getOrderId()));
                    map.put(x.getDriverId(),driverInfoPlus);
                }
            }
        },statisticsParam);
        for (DriverInfo info: driverInfos) {
            DriverInfoPlus driverInfoPlus = new DriverInfoPlus();
            driverInfoPlus.setDriverInfo(info);
            driverInfoPlus.setOnLine(map.get(info.getDriverId())==null?0:1);
            driverInfoPlus.setOnLineDuration(map.get(info.getDriverId())==null?0:map.get(info.getDriverId()).getOnLineDuration());
            driverInfoPlus.setCompleteOrderCount(map.get(info.getDriverId())==null?0:map.get(info.getDriverId()).getCompleteOrderCount());
            consumer.accept(driverInfoPlus);
        }
    }
    /**
     * 车辆-基础条件数据过滤
     * @param consumer
     * @param statisticsParam
     */
    private void baseCarDataFilter(Consumer<CarInfoPlus> consumer, StatisticsParam statisticsParam){
        List<CarInfo> carInfos = carInfoMapper.selectCarInfoListByIds(statisticsParam.getCarGroupIds());
        //所有车辆id
        List<Long> carIds = carInfos.stream().map(CarInfo::getCarId).collect(Collectors.toList());
        Map<Long,CarInfoPlus> map = new HashMap<>();
        //过滤订单，完善时长和完单数据
        statisticsOrderService.baseDataFilter(x->{
            if(carIds.contains(x.getCarId())){
                //车辆维度 总完单量和总订单时长计算
                if(map.containsKey(x.getCarId())){
                    CarInfoPlus carInfoPlus = map.get((x.getCarId()));
                    carInfoPlus.setCompleteOrderCount(carInfoPlus.getCompleteOrderCount()+1);
                    carInfoPlus.setMileageSum(carInfoPlus.getMileageSum()+carInfoMapper.getMileageSumById(x.getOrderId()));
                    map.put(x.getCarId(),carInfoPlus);
                }else {
                    CarInfoPlus carInfoPlus = new CarInfoPlus();
                    carInfoPlus.setCompleteOrderCount(1);
                    carInfoPlus.setMileageSum(carInfoMapper.getMileageSumById(x.getOrderId()));
                    map.put(x.getCarId(),carInfoPlus);
                }
            }
        },statisticsParam);
        for (CarInfo info: carInfos) {
            CarInfoPlus carInfoPlus = new CarInfoPlus();
            carInfoPlus.setCarInfo(info);
            carInfoPlus.setUseCar(map.get(info.getCarId())==null?0:1);
            carInfoPlus.setMileageSum(map.get(info.getCarId())==null?0:map.get(info.getCarId()).getMileageSum());
            carInfoPlus.setCompleteOrderCount(map.get(info.getCarId())==null?0:map.get(info.getCarId()).getCompleteOrderCount());
            consumer.accept(carInfoPlus);
        }
    }
    /**
     * 自有车订单调度-基础条件数据过滤
     * @param consumer
     * @param statisticsParam
     */
    private void baseDispatchDataFilter(Consumer<OrderInfoPlus> consumer, StatisticsParam statisticsParam){
        //所有自有车订单
        OrderInfo temp = new OrderInfo();temp.setUseCarMode(CarModeEnum.ORDER_MODE_HAVE.getKey());
        List<OrderInfo> list = orderInfoMapper.selectOrderInfoList(temp);
        List<String> listDate = DateFormatUtils.sliceUpDateRange(statisticsParam.getBeginDate(),statisticsParam.getEndDate());
        for (OrderInfo orderInfo: list) {
            //过滤所选部门
            if (statisticsParam.getDeptIds().contains(iSysDeptService.findCompany(iSysUserService.selectUserById(orderInfo.getUserId()).getDeptId(), 2).getDeptId())) {
                //所有司机 车队包含的所有司机
                List<DriverInfo> driverInfos = driverInfoMapper.selectDriverInfoListByIds(statisticsParam.getCarGroupIds());
                //所有司机id
                List<Long> driverIds = driverInfos.stream().map(DriverInfo::getDriverId).collect(Collectors.toList());
                //过滤车队下的司机
                if(driverIds.contains(orderInfo.getDriverId())){
                    String dateGroupSelect = DateFormatUtils.formatDate(DateFormatUtils.getPattern(statisticsParam.getBeginDate())[0],orderInfo.getCreateTime());
                    //过滤所选时间
                    if(listDate.contains(dateGroupSelect)){
                        //封装数据
                        OrderInfoPlus orderInfoPlus = new OrderInfoPlus();
                        orderInfoPlus.setOrderInfo(orderInfo);
                        String StateTrace = orderInfoMapper.getOrderStateTraceById(orderInfo.getOrderId());
                        //派单量和派单耗时
                        if(!(orderInfo.getState().equals(OrderState.WAITINGLIST.getState()) && StateTrace.indexOf(OrderState.ORDEROVERTIME.getState())>0)){
                            orderInfoPlus.setDispatchCount(1);
                            orderInfoPlus.setDispatchDuration(orderInfoMapper.getdispatchDurationById(orderInfo.getOrderId()));
                        }
                        //完单量  订单里程和耗时
                        if(orderInfo.getState().equals(OrderState.ORDERCLOSE.getState()) && StateTrace.indexOf(OrderState.ORDERCLOSE.getState())>0){
                            orderInfoPlus.setOrderCloseCount(1);
                            orderInfoPlus.setMileage(carInfoMapper.getMileageSumById(orderInfo.getOrderId()));
                            orderInfoPlus.setOrderDuration(orderInfoMapper.getOrderDurationById(orderInfo.getOrderId()));
                        }
                        consumer.accept(orderInfoPlus);
                    }
                }
            }
        }
    }
}
