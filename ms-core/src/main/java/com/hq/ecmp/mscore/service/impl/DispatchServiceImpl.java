package com.hq.ecmp.mscore.service.impl;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.config.dispatch.DispatchContent;
import com.hq.ecmp.constant.OrderConstant;
import com.hq.ecmp.constant.enumerate.DispatchExceptionEnum;
import com.hq.ecmp.constant.enumerate.NoValueCommonEnum;
import com.hq.ecmp.constant.enumerate.TaskConflictEnum;
import com.hq.ecmp.mscore.bo.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.dispatch.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import com.hq.ecmp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 调度业务实现
 * @Author: zj.hu
 * @Date: 2020-03-17 23:36
 */
@Service
@Slf4j
public class DispatchServiceImpl implements IDispatchService {

    @Resource
    TokenService tokenService;

    @Resource
    RedisUtil redisUtil;

    @Resource
    OrderInfoMapper   orderInfoMapper;

    @Resource
    OrderAddressInfoMapper orderAddressInfoMapper;

    @Resource
    JourneyInfoMapper journeyInfoMapper;

    @Resource
    JourneyPassengerInfoMapper journeyPassengerInfoMapper;

    @Resource
    CarInfoMapper carInfoMapper;

    @Resource
    CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;

    @Resource
    CarGroupInfoMapper carGroupInfoMapper;

    @Resource
    CarGroupServeScopeInfoMapper carGroupServeScopeInfoMapper;

    @Resource
    DriverInfoMapper driverInfoMapper;

    @Resource
    JourneyPlanPriceInfoMapper journeyPlanPriceInfoMapper;

    @Resource
    EnterpriseCarTypeInfoMapper enterpriseCarTypeInfoMapper;

    @Resource
    EcmpOrgMapper ecmpOrgMapper;

    @Resource
    EcmpUserMapper ecmpUserMapper;

    @Resource
    RegimeInfoMapper regimeInfoMapper;

    /**
     *
     * 调度-获取可选择的车辆
     *
     * 注意 模糊查询 需要 取交集
     *
     * @param dispatchSelectCarDto dispatchSelectCarDto
     * @return ApiResponse<DispatchResultVo>
     */
    @Override
    public ApiResponse<DispatchResultVo> getWaitSelectedCars(DispatchSelectCarDto dispatchSelectCarDto) {
        Long orderId=Long.parseLong(dispatchSelectCarDto.getOrderNo());

        LoginUser loginUser=new LoginUser();
        if(StringUtils.isNotEmpty(dispatchSelectCarDto.getDispatcherId())){
             loginUser=tokenService.getLoginUser(dispatchSelectCarDto.getDispatcherId());
        }

        OrderInfo orderInfo=orderInfoMapper.selectOrderInfoById(orderId);
        if(orderInfo==null){
            return ApiResponse.error(DispatchExceptionEnum.ORDER_NOT_EXIST.getDesc());
        }
        OrderAddressInfo orderAddressInfoParam=new OrderAddressInfo();
                         orderAddressInfoParam.setOrderId(orderId);
                         orderAddressInfoParam.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
        OrderAddressInfo orderAddressInfo=orderAddressInfoMapper.queryOrderStartAndEndInfo(orderAddressInfoParam);
        if(orderAddressInfo==null){
            return ApiResponse.error(DispatchExceptionEnum.ORDER_NOT_FIND_SET_OUT_ADDRESS.getDesc());
        }
        String userCarCity=orderAddressInfo.getCityPostalCode();
        String carModelLevelType=dispatchSelectCarDto.getCarModelLevelType();

        int passengers=journeyPassengerInfoMapper.queryPeerCount(orderId)+1;

        SelectCarConditionBo    selectCarConditionBo=new SelectCarConditionBo();
                                selectCarConditionBo.setCarLevel(carModelLevelType);
                                selectCarConditionBo.setPassengers(passengers);
                                selectCarConditionBo.setCityCode(userCarCity);
                                selectCarConditionBo.setDriverId(dispatchSelectCarDto.getDriverId());
                                selectCarConditionBo.setCarLicense(dispatchSelectCarDto.getPlateLicence());
                                selectCarConditionBo.setCarTypeInfo(dispatchSelectCarDto.getCarTypeInfo());
                                selectCarConditionBo.setDispatcherId(0L);
        if(StringUtils.isNotEmpty(dispatchSelectCarDto.getDispatcherId())){
            selectCarConditionBo.setDispatcherId(Long.parseLong(loginUser.getUser().getUserId().toString()));
        }

        ApiResponse<List<WaitSelectedCarBo>> cars=selectCars(selectCarConditionBo,orderInfo);
        if(!cars.isSuccess()){
            return  ApiResponse.error(cars.getMsg());
        }

        //优先级操作 识别 和 查询司机


        DispatchResultVo dispatchResultVo=new DispatchResultVo();
        dispatchResultVo.setCarList(cars.getData());

        return ApiResponse.success(dispatchResultVo);
    }

    /**
     * 调度-锁定选择的车辆
     *
     * @param dispatchInfoDto  dispatchInfoDto
     * @return ApiResponse
     */
    @Override
    public synchronized ApiResponse lockSelectedCar(DispatchLockCarDto dispatchInfoDto) {
        if(StringUtils.isEmpty(dispatchInfoDto.getCarId())){
            return  ApiResponse.error(DispatchExceptionEnum.LOCK_CAR_NOT_EXIST.getDesc());
        }

        int i=carInfoMapper.lockCar(Long.parseLong(dispatchInfoDto.getCarId()));
        if(i<=0){
            return ApiResponse.error(DispatchExceptionEnum.LOCK_CAR_HAS_LOCKED.getDesc());
        }

        return ApiResponse.success();
    }

    /**
     * 调度-解除锁定选择的车辆
     *
     * @param dispatchLockCarDto dispatchLockCarDto
     * @return ApiResponse
     */
    @Override
    public ApiResponse unlockSelectedCar(DispatchLockCarDto dispatchLockCarDto) {
        if(StringUtils.isEmpty(dispatchLockCarDto.getCarId())){
            return  ApiResponse.error(DispatchExceptionEnum.UNLOCK_CAR_NOT_EXIST.getDesc());
        }
        carInfoMapper.unlockCar(Long.parseLong(dispatchLockCarDto.getCarId()));
        return ApiResponse.success();
    }

    /**
     * 调度-获取可选择的司机
     *
     * @param dispatchSelectDriverDto dispatchSelectDriverDto
     * @return ApiResponse<DispatchResultVo>
     */
    @Override
    public ApiResponse<DispatchResultVo> getWaitSelectedDrivers(DispatchSelectDriverDto dispatchSelectDriverDto) {
        Long orderId=Long.parseLong(dispatchSelectDriverDto.getOrderNo());
        LoginUser loginUser=new LoginUser();
        if(StringUtils.isNotEmpty(dispatchSelectDriverDto.getDispatcherId())){
            loginUser=tokenService.getLoginUser(dispatchSelectDriverDto.getDispatcherId());
        }
        SelectDriverConditionBo selectDriverConditionBo=new SelectDriverConditionBo();

        if(StringUtils.isNotEmpty(dispatchSelectDriverDto.getCarId())){
            selectDriverConditionBo.setCarId(dispatchSelectDriverDto.getCarId());
        }

        OrderInfo orderInfo=orderInfoMapper.selectOrderInfoById(orderId);
        if(orderInfo==null){
            return ApiResponse.error(DispatchExceptionEnum.ORDER_NOT_EXIST.getDesc());
        }

        OrderAddressInfo orderAddressInfoParam=new OrderAddressInfo();
                         orderAddressInfoParam.setOrderId(orderId);
                         orderAddressInfoParam.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
        OrderAddressInfo orderAddressInfo=orderAddressInfoMapper.queryOrderStartAndEndInfo(orderAddressInfoParam);

        if(orderAddressInfo==null){
            return ApiResponse.error(DispatchExceptionEnum.ORDER_NOT_FIND_SET_OUT_ADDRESS.getDesc());
        }

        selectDriverConditionBo.setCityCode(orderAddressInfo.getCityPostalCode());
        selectDriverConditionBo.setDispatcherId(0L);
        selectDriverConditionBo.setDriverNameOrPhone(dispatchSelectDriverDto.getDriverNameOrPhone());
        if(StringUtils.isNotEmpty(dispatchSelectDriverDto.getDispatcherId())){
            selectDriverConditionBo.setDispatcherId(loginUser.getUser().getUserId());
        }

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
    public synchronized ApiResponse lockSelectedDriver(DispatchLockDriverDto dispatchLockDriverDto) {
        if(StringUtils.isEmpty(dispatchLockDriverDto.getDriverId())){
            return  ApiResponse.error(DispatchExceptionEnum.LOCK_DRIVER_NOT_EXIST.getDesc());
        }
        int i=driverInfoMapper.lockDriver(Long.parseLong(dispatchLockDriverDto.getDriverId()));

        if(i<=0){
            return ApiResponse.error(DispatchExceptionEnum.LOCK_DRIVER_HAS_LOCKED.getDesc());
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
            return  ApiResponse.error(DispatchExceptionEnum.UNLOCK_DRIVER_NOT_EXIST.getDesc());
        }
        driverInfoMapper.unlockDriver(Long.parseLong(dispatchLockDriverDto.getDriverId()));
        return ApiResponse.success();
    }

    /**
     * 自动调度
     * @param dispatchCountCarAndDriverDto dispatchCountCarAndDriverDto
     * @return ApiResponse<DispatchResultVo>
     */
    @Override
    public ApiResponse<DispatchResultVo> autoDispatch(@RequestBody DispatchCountCarAndDriverDto dispatchCountCarAndDriverDto) {
        DispatchResultVo dispatchResultVo = new DispatchResultVo();

        DispatchSelectCarDto dispatchSelectCarDto = new DispatchSelectCarDto();
        dispatchSelectCarDto.setOrderNo(dispatchCountCarAndDriverDto.getOrderNo());

        ApiResponse<DispatchResultVo> carsResult=getWaitSelectedCars(dispatchSelectCarDto);
        if(!carsResult.isSuccess()){
            return ApiResponse.error(carsResult.getMsg());
        }

        List<WaitSelectedCarBo> cars=carsResult.getData().getCarList();

        if(cars.isEmpty()){
            log.info(dispatchCountCarAndDriverDto.toString() + "-" + DispatchExceptionEnum.DRIVER_OR_CAR_NOT_FIND.getDesc());
            return ApiResponse.error(DispatchExceptionEnum.DRIVER_OR_CAR_NOT_FIND.getDesc());
        }

        Iterator<WaitSelectedCarBo> carInfoVOIterator = cars.iterator();


        DispatchLockCarDto dispatchLockCarDto = new DispatchLockCarDto();
        DispatchLockDriverDto dispatchLockDriverDto = new DispatchLockDriverDto();

        AtomicReference<Boolean> carFlag = new AtomicReference<>(false);
        AtomicReference<Boolean> driverFlag = new AtomicReference<>(false);

        List<WaitSelectedDriverBo> drivers=new ArrayList<>();

        while (carInfoVOIterator.hasNext()) {
            dispatchResultVo.setLockCar(1);

            WaitSelectedCarBo car = carInfoVOIterator.next();
            dispatchLockCarDto.setOrderNo(dispatchCountCarAndDriverDto.getOrderNo());
            dispatchLockCarDto.setCarId(car.getCarId().toString());

            ApiResponse lockCarResult = lockSelectedCar(dispatchLockCarDto);
            if(!lockCarResult.isSuccess()){
                continue;
            }

            DispatchSelectDriverDto dispatchSelectDriverDto = new DispatchSelectDriverDto();
            dispatchSelectDriverDto.setOrderNo(dispatchCountCarAndDriverDto.getOrderNo());
            dispatchSelectDriverDto.setCarId(car.getCarId().toString());
            ApiResponse<DispatchResultVo> driversResult=getWaitSelectedDrivers(dispatchSelectDriverDto);
            if(!driversResult.isSuccess()){
                continue;
            }

            drivers = driversResult.getData().getDriverList();

            if(drivers.isEmpty()){
                continue;
            }

            Iterator<WaitSelectedDriverBo> driverInfoVOIterator = drivers.iterator();
            if (lockCarResult.isSuccess()) {
                dispatchResultVo.setLockCar(0);
                dispatchResultVo.setLockDriver(1);
                carFlag.set(true);

                while (driverInfoVOIterator.hasNext()) {
                    WaitSelectedDriverBo driver = driverInfoVOIterator.next();
                    dispatchLockDriverDto.setOrderNo(dispatchCountCarAndDriverDto.getOrderNo());
                    dispatchLockDriverDto.setCarId(car.getCarId().toString());
                    dispatchLockDriverDto.setDriverId(driver.getDriverId().toString());

                    ApiResponse lockDriverResult = lockSelectedDriver(dispatchLockDriverDto);
                    if(!lockDriverResult.isSuccess()){
                        continue;
                    }
                    dispatchResultVo.setLockDriver(0);
                    driverFlag.set(true);
                    break;
                }
            }

            if (! driverFlag.get()) {
                unlockSelectedCar(dispatchLockCarDto);
            }
            if (carFlag.get() && driverFlag.get()) {
                cars.clear();
                cars.add(car);
                break;
            }
        }

        dispatchResultVo.setCarList(cars);
        dispatchResultVo.setDriverList(drivers);
        dispatchResultVo.setHasCar(cars.isEmpty()?0:1);
        dispatchResultVo.setHasDriver(drivers.isEmpty()?0:1);

        return ApiResponse.success(dispatchResultVo);
    }



    /**
     * 找到符合条件的车辆
     * @param selectCarConditionBo selectCarConditionBo
     * @param orderInfo  orderInfo
     * @return  ApiResponse<List<WaitSelectedCarBo>>
     */
    private ApiResponse<List<WaitSelectedCarBo>>    selectCars(SelectCarConditionBo selectCarConditionBo,OrderInfo orderInfo){

        ApiResponse<List<CarGroupServeScopeInfo>>  carGroupServiceScopes=selectCarGroupServiceScope(selectCarConditionBo.getCityCode(),selectCarConditionBo.getDispatcherId());
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

        Iterator<CarInfo> carInfoIterator=cars.iterator();
        JourneyInfo journeyInfo=journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        RegimeInfo regimeInfo=regimeInfoMapper.selectRegimeInfoById(journeyInfo.getRegimenId());
        String allowCarModelLevel=regimeInfo.getUseCarModeOwnerLevel();
        if(!StringUtils.isEmpty(allowCarModelLevel)){
            while (carInfoIterator.hasNext()){
                CarInfo carInfo=carInfoIterator.next();
                EnterpriseCarTypeInfo enterpriseCarTypeInfo=enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(carInfo.getCarTypeId());
                if(!allowCarModelLevel.contains(enterpriseCarTypeInfo.getLevel())){
                    carInfoIterator.remove();
                }
            }
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
        ApiResponse<List<CarGroupServeScopeInfo>>  carGroupServiceScopesApiResponse=selectCarGroupServiceScope(selectDriverConditionBo.getCityCode(), selectDriverConditionBo.getDispatcherId());

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
            return ApiResponse.error(DispatchExceptionEnum.ORDER_NOT_FIND_PLAN_PRICE.getDesc());
        }
        journeyPlanPriceInfo=journeyPlanPriceInfos.get(0);

        Calendar setOutCalendar=Calendar.getInstance();
        setOutCalendar.setTime(journeyPlanPriceInfo.getPlannedDepartureTime());
        setOutCalendar.add(Calendar.MINUTE,DispatchContent.notBackCarGroup);

        Calendar arrivalCalendar=Calendar.getInstance();
        arrivalCalendar.setTime(journeyPlanPriceInfo.getPlannedArrivalTime());
        arrivalCalendar.add(Calendar.MINUTE,DispatchContent.notBackCarGroup);

        OrderTaskClashBo orderTaskClashBo=new OrderTaskClashBo();
        orderTaskClashBo.setSetOutTime(new Date(setOutCalendar.getTimeInMillis()));
        orderTaskClashBo.setArrivalTime(new Date(arrivalCalendar.getTimeInMillis()));

        return ApiResponse.success(orderTaskClashBo);
    }

    /**
     * 根据 调度员信息 查询 调度员归属的车队是否符合服务范围要求
     * @param cityCode cityCode
     * @param dispatcherUserId  dispatcherUserId
     * @return ApiResponse<List<CarGroupServeScopeInfo>>
     */
    private ApiResponse<List<CarGroupServeScopeInfo>> selectCarGroupServiceScope(String cityCode,Long dispatcherUserId){
        CarGroupDispatcherInfo carGroupDispatcher=new CarGroupDispatcherInfo();
        if(dispatcherUserId.equals(0L)){
            carGroupDispatcher.setDispatcherId(null);
        }
                               carGroupDispatcher.setUserId(dispatcherUserId);
        List<CarGroupDispatcherInfo> carGroupDispatcherInfos=carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcher);

        if(carGroupDispatcherInfos.isEmpty()){
            return ApiResponse.error(DispatchExceptionEnum.NOT_FIND_SUITABLE_CAR_SERVICE_SCOPE.getDesc());
        }

        List<CarGroupServeScopeInfo> carGroupServeScopeInfoListResult = new LinkedList<>();

        for (CarGroupDispatcherInfo carGroupDispatcherInfo:carGroupDispatcherInfos) {
            Long carGroupId=carGroupDispatcherInfo.getCarGroupId();
            CarGroupInfo carGroupInfo=carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            if(carGroupInfo==null){
                continue;
            }

            CarGroupServeScopeInfo  carGroupServeScopeInfo=new CarGroupServeScopeInfo();
                                    carGroupServeScopeInfo.setCarGroupId(carGroupInfo.getCarGroupId());
            List<CarGroupServeScopeInfo> carGroupServeScopeInfoList=carGroupServeScopeInfoMapper.queryAll(carGroupServeScopeInfo);
            if(carGroupServeScopeInfoList.isEmpty()){
                continue;
            }

            Iterator<CarGroupServeScopeInfo> iterator=carGroupServeScopeInfoList.iterator();
            boolean serviceCitySatisfy=false;
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
            return ApiResponse.error(DispatchExceptionEnum.DISPATCHER_OWN_CAR_GROUP_SCOPE_IS_TOO_SMALL.getDesc());
        }

        return ApiResponse.success(carGroupServeScopeInfoListResult);
    }
}
