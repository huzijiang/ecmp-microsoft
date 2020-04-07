package com.hq.ecmp.mscore.service.impl;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.config.dispatch.DispatchContent;
import com.hq.ecmp.constant.OrderConstant;
import com.hq.ecmp.constant.enumerate.NoValueCommonEnum;
import com.hq.ecmp.constant.enumerate.TaskConflictEnum;
import com.hq.ecmp.mscore.bo.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.dispatch.DispatchLockCarDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchLockDriverDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchSelectCarDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchSelectDriverDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import com.hq.ecmp.util.RedisUtil;
import lombok.Synchronized;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.util.*;

/**
 * @Author: zj.hu
 * @Date: 2020-03-17 23:36
 */
@Service
public class DispatchServiceImpl implements IDispatchService {

    @Autowired
    TokenService tokenService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    OrderInfoMapper   orderInfoMapper;

    @Autowired
    OrderAddressInfoMapper orderAddressInfoMapper;

    @Autowired
    JourneyInfoMapper journeyInfoMapper;

    @Autowired
    JourneyPassengerInfoMapper journeyPassengerInfoMapper;

    @Autowired
    CarInfoMapper carInfoMapper;

    @Autowired
    CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;

    @Autowired
    CarGroupInfoMapper carGroupInfoMapper;

    @Autowired
    CarGroupServeScopeInfoMapper carGroupServeScopeInfoMapper;

    @Autowired
    DriverInfoMapper driverInfoMapper;

    @Autowired
    JourneyPlanPriceInfoMapper journeyPlanPriceInfoMapper;

    @Autowired
    EnterpriseCarTypeInfoMapper enterpriseCarTypeInfoMapper;

    @Autowired
    EcmpOrgMapper ecmpOrgMapper;

    @Autowired
    EcmpUserMapper ecmpUserMapper;

    /**
     *
     * 调度-获取可选择的车辆
     *
     * 注意 模糊查询 需要 取交集
     *
     * @param dispatchSelectCarDto
     * @return
     */
    @Override
    public ApiResponse<DispatchResultVo> getWaitSelectedCars(DispatchSelectCarDto dispatchSelectCarDto) {
        Long orderId=Long.parseLong(dispatchSelectCarDto.getOrderNo());
        LoginUser loginUser=tokenService.getLoginUser(dispatchSelectCarDto.getDispatcherId());

        //创建一个可选车辆 集合：key= 'D'+调度员编号+订单编号
        String carKey="D"+loginUser.getUser().getUserId().toString()+dispatchSelectCarDto.getOrderNo();


        OrderInfo orderInfo=orderInfoMapper.selectOrderInfoById(orderId);
        if(orderInfo==null){
            return ApiResponse.error("订单未找到");
        }
        //查找出发地
        OrderAddressInfo orderAddressInfoParam=new OrderAddressInfo();
                         orderAddressInfoParam.setOrderId(orderId);
                         orderAddressInfoParam.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
        OrderAddressInfo orderAddressInfo=orderAddressInfoMapper.queryOrderStartAndEndInfo(orderAddressInfoParam);
        if(orderAddressInfo==null){
            return ApiResponse.error("订单没有出发地信息");
        }
        //订单-用车城市代码
        String userCarCity=orderAddressInfo.getCityPostalCode();
        //调度指派-车辆级别
        String carModelLevelType=dispatchSelectCarDto.getCarModelLevelType();
        //订单需求-乘客人数
        int passengers=journeyPassengerInfoMapper.queryPeerCount(orderId)+1;

        SelectCarConditionBo    selectCarConditionBo=new SelectCarConditionBo();
                                selectCarConditionBo.setCarLevel(carModelLevelType);
                                selectCarConditionBo.setPassengers(passengers);
                                selectCarConditionBo.setCityCode(userCarCity);
                                selectCarConditionBo.setDriverId(dispatchSelectCarDto.getDriverId());
                                selectCarConditionBo.setDispatcherId(loginUser.getUser().getUserId().toString());
                                selectCarConditionBo.setCarLicense(dispatchSelectCarDto.getPlateLicence());
                                selectCarConditionBo.setCarTypeInfo(dispatchSelectCarDto.getCarTypeInfo());

        List<WaitSelectedCarBo> cars=selectCars(selectCarConditionBo,orderInfo).getData();

        //优先级操作 识别 和 查询司机


        DispatchResultVo dispatchResultVo=new DispatchResultVo();
        dispatchResultVo.setCarList(cars);

        return ApiResponse.success(dispatchResultVo);
    }

    /**
     * 调度-锁定选择的车辆
     *
     * @param dispatchInfoDto
     * @return
     */
    @Override
    @Synchronized
    public ApiResponse lockSelectedCar(DispatchLockCarDto dispatchInfoDto) {
        if(StringUtils.isEmpty(dispatchInfoDto.getCarId())){
            return  ApiResponse.error("没有锁定的车辆编号");
        }

        int i=carInfoMapper.lockCar(Long.parseLong(dispatchInfoDto.getCarId()));
        if(i<=0){
            return ApiResponse.error("锁定失败,该车已被其他调度员选中锁定");
        }

        return ApiResponse.success();
    }

    /**
     * 调度-解除锁定选择的车辆
     *
     * @param dispatchLockCarDto
     * @return
     */
    @Override
    public ApiResponse unlockSelectedCar(DispatchLockCarDto dispatchLockCarDto) {
        if(StringUtils.isEmpty(dispatchLockCarDto.getCarId())){
            return  ApiResponse.error("没有需要解除锁定的车辆编号");
        }
        int i=carInfoMapper.unlockCar(Long.parseLong(dispatchLockCarDto.getCarId()));
        if(i<=0){
            return ApiResponse.error("解除锁定失败,该车已被其他调度员选中锁定");
        }
        return ApiResponse.success();
    }

    /**
     * 调度-获取可选择的司机
     *
     * @param dispatchSelectDriverDto
     * @return
     */
    @Override
    public ApiResponse<DispatchResultVo> getWaitSelectedDrivers(DispatchSelectDriverDto dispatchSelectDriverDto) {
        Long orderId=Long.parseLong(dispatchSelectDriverDto.getOrderNo());

        LoginUser loginUser=tokenService.getLoginUser(dispatchSelectDriverDto.getDispatcherId());

        //创建一个可选司机 集合：key= 'D'+ 调度员编号 + 订单编号
        //                 value= 司机信息对象集合
        String carKey="D"+loginUser.getUser().getUserId().toString()+dispatchSelectDriverDto.getOrderNo();

        SelectDriverConditionBo selectDriverConditionBo=new SelectDriverConditionBo();
        //已选车辆
        if(StringUtils.isNotEmpty(dispatchSelectDriverDto.getCarId())){
            selectDriverConditionBo.setCarId(dispatchSelectDriverDto.getCarId());
        }

        OrderInfo orderInfo=orderInfoMapper.selectOrderInfoById(orderId);

        if(orderInfo==null){
            return ApiResponse.error("订单不存在");
        }
        //查找出发地
        OrderAddressInfo orderAddressInfoParam=new OrderAddressInfo();
                         orderAddressInfoParam.setOrderId(orderId);
                         orderAddressInfoParam.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
        OrderAddressInfo orderAddressInfo=orderAddressInfoMapper.queryOrderStartAndEndInfo(orderAddressInfoParam);

        if(orderAddressInfo==null){
            return ApiResponse.error("订单没有出发地信息");
        }

        //指定司机 暂不考虑
        selectDriverConditionBo.setCityCode(orderAddressInfo.getCityPostalCode());
//        selectDriverConditionBo.setDriverId(dispatchSelectDriverDto.getDriverId());
        selectDriverConditionBo.setDispatcherId(loginUser.getUser().getUserId());
        selectDriverConditionBo.setDriverNameOrPhone(dispatchSelectDriverDto.getDriverNameOrPhone());

        List<WaitSelectedDriverBo>   waitSelectedDriverBos=selectDrivers(selectDriverConditionBo,orderInfo).getData();

        //优先级识别 和 查询车辆
        DispatchResultVo dispatchResultVo=new DispatchResultVo();
                         dispatchResultVo.setDriverList(waitSelectedDriverBos);

        return ApiResponse.success(dispatchResultVo);
    }

    /**
     * 调度-锁定选择的司机
     *
     * @param dispatchLockDriverDto  dispatchLockDriverDto
     * @return ApiResponse
     */
    @Override
    public ApiResponse lockSelectedDriver(DispatchLockDriverDto dispatchLockDriverDto) {
        if(StringUtils.isEmpty(dispatchLockDriverDto.getDriverId())){
            return  ApiResponse.error("没有需要解除锁定的车辆编号");
        }
        int i=driverInfoMapper.lockDriver(Long.parseLong(dispatchLockDriverDto.getDriverId()));

        if(i<=0){
            return ApiResponse.error("解除锁定失败,该车已被其他调度员选中锁定");
        }
        return ApiResponse.success();
    }

    /**
     * 调度-解除锁定选择的司机
     *
     * @param dispatchLockDriverDto dispatchLockDriverDto
     * @return ApiResponse ApiResponse
     */
    @Override
    public ApiResponse unlockSelectedDriver(DispatchLockDriverDto dispatchLockDriverDto) {
        if(StringUtils.isEmpty(dispatchLockDriverDto.getDriverId())){
            return  ApiResponse.error("没有需要解除锁定的车辆编号");
        }
        int i=driverInfoMapper.unlockDriver(Long.parseLong(dispatchLockDriverDto.getDriverId()));
        if(i<=0){
            return ApiResponse.error("解除锁定失败,该车已被其他调度员选中锁定");
        }
        return ApiResponse.success();
    }

    /**
     * 找到符合条件的车辆
     * @param selectCarConditionBo selectCarConditionBo
     * @param orderInfo  orderInfo
     * @return  ApiResponse<List<WaitSelectedCarBo>>
     */
    private ApiResponse<List<WaitSelectedCarBo>>    selectCars(SelectCarConditionBo selectCarConditionBo,OrderInfo orderInfo){

        //调度员归属车队  服务范围查询
        ApiResponse<List<CarGroupServeScopeInfo>>  carGroupServiceScopes=selectCarGroupServiceScope(selectCarConditionBo.getCityCode(),Long.parseLong(selectCarConditionBo.getDispatcherId()));
        if(!carGroupServiceScopes.isSuccess()){
            return ApiResponse.error(carGroupServiceScopes.getMsg());
        }

        List<CarInfo> cars=new ArrayList<>();

        for (CarGroupServeScopeInfo carGroupServeScopeInfo:carGroupServiceScopes.getData()) {
            selectCarConditionBo.setCarGroupId(carGroupServeScopeInfo.getCarGroupId().toString());
            List<CarInfo> acars;
            if(StringUtils.isEmpty(selectCarConditionBo.getCarLicense())) {
                acars=carInfoMapper.dispatcherSelectCarGroupOwnedCarInfoList(selectCarConditionBo);
            }else{
                acars=carInfoMapper.dispatcherSelectCarGroupOwnedCarInfoListUseCarLicense(selectCarConditionBo);
            }
            cars.addAll(acars);
        }

        ApiResponse<OrderTaskClashBo>  apiResponseSelectOrderSetOutAndArrivalTime=selectOrderSetOutAndArrivalTime(orderInfo);
        if(!apiResponseSelectOrderSetOutAndArrivalTime.isSuccess()){
            return  ApiResponse.error(apiResponseSelectOrderSetOutAndArrivalTime.getMsg());
        }

        OrderTaskClashBo orderTaskClashBo=apiResponseSelectOrderSetOutAndArrivalTime.getData();

        List<WaitSelectedCarBo> waitSelectedCarBoList=new LinkedList<>();
        cars.stream().forEach(carInfo->{
            WaitSelectedCarBo waitSelectedCarBo=new WaitSelectedCarBo();
            waitSelectedCarBo.setCarId(carInfo.getCarId());
            waitSelectedCarBo.setCarModelName(carInfo.getCarType());
            EnterpriseCarTypeInfo enterpriseCarTypeInfo=enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(carInfo.getCarTypeId());
            waitSelectedCarBo.setCarType(enterpriseCarTypeInfo.getName());

            waitSelectedCarBo.setColor(carInfo.getCarColor());
            waitSelectedCarBo.setPlateLicence(carInfo.getCarLicense());
            waitSelectedCarBo.setStatus(carInfo.getState());
            waitSelectedCarBo.setCarGroupId(carInfo.getCarGroupId());
            waitSelectedCarBo.setState(carInfo.getState());

            waitSelectedCarBo.setPowerType(carInfo.getPowerType());
            waitSelectedCarBo.setAssetTag(carInfo.getAssetTag());

            EcmpOrg ecmpOrg=ecmpOrgMapper.selectEcmpOrgById(carInfo.getDeptId());
            waitSelectedCarBo.setDeptName(ecmpOrg.getDeptName());
            waitSelectedCarBo.setSource(carInfo.getSource());

            orderTaskClashBo.setCarId(carInfo.getCarId());
            orderTaskClashBo.setCarLicense(carInfo.getCarLicense());

            List<OrderInfo> orderInfosSetOutClash=orderInfoMapper.getSetOutClashTask(orderTaskClashBo);
            List<OrderInfo> orderInfosArrivalClash=orderInfoMapper.getSetOutClashTask(orderTaskClashBo);

            if(orderInfosSetOutClash.isEmpty() && orderInfosArrivalClash.isEmpty()){
                waitSelectedCarBo.setTaskConflict(TaskConflictEnum.CONFLICT_FREE);

                List<OrderInfo> orderInfosBefore=orderInfoMapper.getSetOutBeforeTaskForCarOrDriver(orderTaskClashBo);
                List<OrderInfo> orderInfosAfter=orderInfoMapper.getArrivalAfterTaskForCarOrDriver(orderTaskClashBo);

                if(!orderInfosBefore.isEmpty()){
                    waitSelectedCarBo.setBeforeTaskOrderId(orderInfosBefore.get(0).getOrderId());
                    waitSelectedCarBo.setBeforeTaskEndTime(new Timestamp(orderInfosBefore.get(0).getCreateTime().getTime()));
                }
                if(!orderInfosAfter.isEmpty()){
                    waitSelectedCarBo.setAfterTaskOrderId(orderInfosAfter.get(0).getOrderId());
                    waitSelectedCarBo.setAfterTaskBeginTime(new Timestamp(orderInfosAfter.get(0).getUpdateTime().getTime()));
                }
            }
            if((!orderInfosSetOutClash.isEmpty()) && (!orderInfosArrivalClash.isEmpty())){
                waitSelectedCarBo.setTaskConflict(TaskConflictEnum.BEFORE_AND_AFTER_TASK_CLASH);
            }
            if((!orderInfosSetOutClash.isEmpty()) && (orderInfosArrivalClash.isEmpty())){
                waitSelectedCarBo.setTaskConflict(TaskConflictEnum.BEFORE_TASK_CLASH);
                List<OrderInfo> orderInfosAfter=orderInfoMapper.getArrivalAfterTaskForCarOrDriver(orderTaskClashBo);
                if(!orderInfosAfter.isEmpty()){
                    waitSelectedCarBo.setAfterTaskOrderId(orderInfosAfter.get(0).getOrderId());
                    waitSelectedCarBo.setAfterTaskBeginTime(new Timestamp(orderInfosAfter.get(0).getUpdateTime().getTime()));
                }
            }
            if((orderInfosSetOutClash.isEmpty()) && (!orderInfosArrivalClash.isEmpty())){
                waitSelectedCarBo.setTaskConflict(TaskConflictEnum.AFTER_TASK_CLASH);
                List<OrderInfo> orderInfosBefore=orderInfoMapper.getSetOutBeforeTaskForCarOrDriver(orderTaskClashBo);
                if(!orderInfosBefore.isEmpty()){
                    waitSelectedCarBo.setBeforeTaskOrderId(orderInfosBefore.get(0).getOrderId());
                    waitSelectedCarBo.setBeforeTaskEndTime(new Timestamp(orderInfosBefore.get(0).getCreateTime().getTime()));
                }
            }

            waitSelectedCarBoList.add(waitSelectedCarBo);
        });

        waitSelectedCarBoList.stream().forEach(waitSelectedCarBo->{
            waitSelectedCarBo.embellish();
        });

        return  ApiResponse.success(waitSelectedCarBoList);
    }


    /**
     * 找到符合条件的司机
     * @param selectDriverConditionBo selectDriverConditionBo
     * @param orderInfo  orderInfo
     * @return ApiResponse<List<WaitSelectedDriverBo>>
     */
    private ApiResponse<List<WaitSelectedDriverBo>> selectDrivers(SelectDriverConditionBo selectDriverConditionBo,
                                                                 OrderInfo orderInfo){
        ApiResponse<List<CarGroupServeScopeInfo>>  carGroupServiceScopesApiResponse=selectCarGroupServiceScope(selectDriverConditionBo.getCityCode(),
                selectDriverConditionBo.getDispatcherId());
        if(!carGroupServiceScopesApiResponse.isSuccess()){
            return ApiResponse.error(carGroupServiceScopesApiResponse.getMsg());
        }

        ApiResponse<OrderTaskClashBo> orderTaskClashBoApiResponse=selectOrderSetOutAndArrivalTime(orderInfo);
        if(!orderTaskClashBoApiResponse.isSuccess()){
            return  ApiResponse.error(orderTaskClashBoApiResponse.getMsg());
        }
        Date setOutDate=new Date(orderTaskClashBoApiResponse.getData().getSetOutTime().getTime());
        selectDriverConditionBo.setWorkDay(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,setOutDate));

        List<DriverInfo> drivers=new ArrayList<>();

        for (CarGroupServeScopeInfo carGroupServeScopeInfo:carGroupServiceScopesApiResponse.getData()) {
            List<DriverInfo> adrivers;
            selectDriverConditionBo.setCarGroupId(carGroupServeScopeInfo.getCarGroupId());
            if(StringUtils.isEmpty(selectDriverConditionBo.getCarId())){
                adrivers=driverInfoMapper.dispatcherSelectDriver(selectDriverConditionBo);
            }else{
                adrivers=driverInfoMapper.dispatcherSelectDriverUseDriverNameOrPhone(selectDriverConditionBo);
            }
            drivers.addAll(adrivers);
        }




        ApiResponse<OrderTaskClashBo>  apiResponseSelectOrderSetOutAndArrivalTime=selectOrderSetOutAndArrivalTime(orderInfo);
        if(!apiResponseSelectOrderSetOutAndArrivalTime.isSuccess()){
            return  ApiResponse.error(apiResponseSelectOrderSetOutAndArrivalTime.getMsg());
        }
        OrderTaskClashBo orderTaskClashBo=apiResponseSelectOrderSetOutAndArrivalTime.getData();

        List<WaitSelectedDriverBo> waitSelectedDriverBoList=new ArrayList<>();

        drivers.stream().forEach(driver->{
            orderTaskClashBo.setDriverId(driver.getDriverId());

            WaitSelectedDriverBo waitSelectedDriverBo=new WaitSelectedDriverBo();
            waitSelectedDriverBo.setDriverId(driver.getDriverId());
            waitSelectedDriverBo.setDriverName(driver.getDriverName());
            waitSelectedDriverBo.setState(driver.getState());
            waitSelectedDriverBo.setMobile(driver.getMobile());
            waitSelectedDriverBo.setDriverPhone(driver.getMobile());

            if(driver.getUserId()!=0 && driver.getUserId()!=null){
                EcmpUser ecmpUser=ecmpUserMapper.selectEcmpUserById(driver.getUserId());
                waitSelectedDriverBo.setJobNumber(ecmpUser.getJobNumber());
                EcmpOrg  ecmpOrg=ecmpOrgMapper.selectEcmpOrgById(ecmpUser.getDeptId());
                waitSelectedDriverBo.setDeptName(ecmpOrg.getDeptName());
            }else{
                waitSelectedDriverBo.setJobNumber(NoValueCommonEnum.NO_STRING.getCode());
                waitSelectedDriverBo.setDeptName(NoValueCommonEnum.NO_STRING.getCode());
            }


            //查询车队电话,任意一个车队应该都可以
            List<CarGroupInfo> carGroupInfo;
            carGroupInfo=carGroupInfoMapper.selectCarGroupsByDriverId(driver.getDriverId());
            waitSelectedDriverBo.setFleetPhone(carGroupInfo.get(0).getTelephone());

            List<OrderInfo> orderInfosSetOutClash=orderInfoMapper.getSetOutClashTask(orderTaskClashBo);
            List<OrderInfo> orderInfosArrivalClash=orderInfoMapper.getSetOutClashTask(orderTaskClashBo);

            if(orderInfosSetOutClash.isEmpty() && orderInfosArrivalClash.isEmpty()){
                waitSelectedDriverBo.setTaskConflict(TaskConflictEnum.CONFLICT_FREE);
                List<OrderInfo> orderInfosBefore=orderInfoMapper.getSetOutBeforeTaskForCarOrDriver(orderTaskClashBo);
                List<OrderInfo> orderInfosAfter=orderInfoMapper.getArrivalAfterTaskForCarOrDriver(orderTaskClashBo);

                if(!orderInfosBefore.isEmpty()){
                    waitSelectedDriverBo.setBeforeTaskOrderId(orderInfosBefore.get(0).getOrderId());
                    waitSelectedDriverBo.setBeforeTaskEndTime(orderInfosBefore.get(0).getCreateTime());
                }
                if(!orderInfosAfter.isEmpty()){
                    waitSelectedDriverBo.setAfterTaskOrderId(orderInfosAfter.get(0).getOrderId());
                    waitSelectedDriverBo.setAfterTaskBeginTime(orderInfosAfter.get(0).getUpdateTime());
                }
            }
            if((!orderInfosSetOutClash.isEmpty()) && (!orderInfosArrivalClash.isEmpty())){
                waitSelectedDriverBo.setTaskConflict(TaskConflictEnum.BEFORE_AND_AFTER_TASK_CLASH);
            }
            if((!orderInfosSetOutClash.isEmpty()) && (orderInfosArrivalClash.isEmpty())){
                waitSelectedDriverBo.setTaskConflict(TaskConflictEnum.BEFORE_TASK_CLASH);
                List<OrderInfo> orderInfosAfter=orderInfoMapper.getArrivalAfterTaskForCarOrDriver(orderTaskClashBo);
                if(!orderInfosAfter.isEmpty()){
                    waitSelectedDriverBo.setAfterTaskOrderId(orderInfosAfter.get(0).getOrderId());
                    waitSelectedDriverBo.setAfterTaskBeginTime(orderInfosAfter.get(0).getUpdateTime());
                }
            }
            if((orderInfosSetOutClash.isEmpty()) && (!orderInfosArrivalClash.isEmpty())){
                waitSelectedDriverBo.setTaskConflict(TaskConflictEnum.AFTER_TASK_CLASH);

                List<OrderInfo> orderInfosBefore=orderInfoMapper.getSetOutBeforeTaskForCarOrDriver(orderTaskClashBo);
                if(!orderInfosBefore.isEmpty()){
                    waitSelectedDriverBo.setBeforeTaskOrderId(orderInfosBefore.get(0).getOrderId());
                    waitSelectedDriverBo.setBeforeTaskEndTime(orderInfosBefore.get(0).getCreateTime());
                }
            }
            waitSelectedDriverBoList.add(waitSelectedDriverBo);
        });

        waitSelectedDriverBoList.stream().forEach(driver->{
            driver.embellish();
        });


        return ApiResponse.success(waitSelectedDriverBoList);
    }

    /**
     * 获取  订单对的行程节点的 计划开始时间和结束时间  从预算价表中获取
     * @param orderInfo orderInfo
     * @return ApiResponse<OrderTaskClashBo>
     */
    private ApiResponse<OrderTaskClashBo>  selectOrderSetOutAndArrivalTime(OrderInfo orderInfo){
        JourneyPlanPriceInfo journeyPlanPriceInfo=new JourneyPlanPriceInfo();
        journeyPlanPriceInfo.setNodeId(orderInfo.getNodeId());
        journeyPlanPriceInfo.setJourneyId(orderInfo.getJourneyId());

        List<JourneyPlanPriceInfo>  journeyPlanPriceInfos=journeyPlanPriceInfoMapper.selectJourneyPlanPriceInfoList(journeyPlanPriceInfo);
        if(journeyPlanPriceInfos.isEmpty()){
            return ApiResponse.error("未找到订单对应的行程预算(价格和时间)信息");
        }
        journeyPlanPriceInfo=journeyPlanPriceInfos.get(0);

        Calendar setOutCalendar=Calendar.getInstance();
        setOutCalendar.setTime(journeyPlanPriceInfo.getPlannedDepartureTime());
        setOutCalendar.add(Calendar.MINUTE,DispatchContent.notBackCarGroup);

        Calendar arrivalCalendar=Calendar.getInstance();
        arrivalCalendar.setTime(journeyPlanPriceInfo.getPlannedArrivalTime());
        arrivalCalendar.add(Calendar.MINUTE,DispatchContent.notBackCarGroup);

        OrderTaskClashBo orderTaskClashBo=new OrderTaskClashBo();
        orderTaskClashBo.setSetOutTime(new Timestamp(setOutCalendar.getTimeInMillis()));
        orderTaskClashBo.setArrivalTime(new Timestamp(arrivalCalendar.getTimeInMillis()));

        return ApiResponse.success(orderTaskClashBo);
    }

    /**
     * 根据 调度员信息 查询 调度员归属的车队是否符合服务范围要求
     * @return ApiResponse<List<CarGroupServeScopeInfo>>
     */
    private ApiResponse<List<CarGroupServeScopeInfo>> selectCarGroupServiceScope(String cityCode,Long dispatcherUserId){
        //找到调度员管理的车队
        CarGroupDispatcherInfo carGroupDispatcher=new CarGroupDispatcherInfo();
                               carGroupDispatcher.setUserId(dispatcherUserId);
        List<CarGroupDispatcherInfo> carGroupDispatcherInfos=carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcher);
        if(carGroupDispatcherInfos.isEmpty()){
            return ApiResponse.error("当前调度员不存在");
        }

        List<CarGroupServeScopeInfo> carGroupServeScopeInfoListResult = new LinkedList<>();

        for (CarGroupDispatcherInfo carGroupDispatcherInfo:carGroupDispatcherInfos) {
            Long carGroupId=carGroupDispatcherInfo.getCarGroupId();
            CarGroupInfo carGroupInfo=carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            if(carGroupInfo==null){
                return ApiResponse.error("没有找到调度员的归属车队");
            }

            //查看车队是否满足城市要求
            CarGroupServeScopeInfo  carGroupServeScopeInfo=new CarGroupServeScopeInfo();
                                    carGroupServeScopeInfo.setCarGroupId(carGroupInfo.getCarGroupId());
            List<CarGroupServeScopeInfo> carGroupServeScopeInfoList=carGroupServeScopeInfoMapper.queryAll(carGroupServeScopeInfo);
            if(carGroupServeScopeInfoList.isEmpty()){
                continue;
            }

            Iterator<CarGroupServeScopeInfo> iterator=carGroupServeScopeInfoList.iterator();
            Boolean serviceCitySatisfy=false;
            while (iterator.hasNext()){
                CarGroupServeScopeInfo cgss=iterator.next();
                if(cityCode.equals(cgss.getCity())){
                    serviceCitySatisfy=true;
                }else {
                    iterator.remove();
                }
            }

            if(serviceCitySatisfy){
                carGroupServeScopeInfoListResult.addAll(carGroupServeScopeInfoList);
            }

        }

        if(carGroupServeScopeInfoListResult.isEmpty()){
            return ApiResponse.error("您的车队服务范围不满足订单要求");
        }

        return ApiResponse.success(carGroupServeScopeInfoListResult);
    }
}
