package com.hq.ecmp.mscore.service.impl;

import com.hq.api.system.domain.SysRole;
import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.OrderConstant;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.constant.enumerate.CarLevelMatchEnum;
import com.hq.ecmp.constant.enumerate.CarRentTypeEnum;
import com.hq.ecmp.constant.enumerate.DispatchExceptionEnum;
import com.hq.ecmp.constant.enumerate.NoValueCommonEnum;
import com.hq.ecmp.constant.enumerate.TaskConflictEnum;
import com.hq.ecmp.mscore.bo.CarGroupPricePlanInfoBo;
import com.hq.ecmp.mscore.bo.OrderTaskClashBo;
import com.hq.ecmp.mscore.bo.SelectCarConditionBo;
import com.hq.ecmp.mscore.bo.SelectDriverConditionBo;
import com.hq.ecmp.mscore.bo.WaitSelectedCarBo;
import com.hq.ecmp.mscore.bo.WaitSelectedDriverBo;
import com.hq.ecmp.mscore.domain.ApplyDispatch;
import com.hq.ecmp.mscore.domain.CarGroupDispatcherInfo;
import com.hq.ecmp.mscore.domain.CarGroupDriverRelation;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.CarGroupServeScopeInfo;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.DriverCarRelationInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.EnterpriseCarTypeInfo;
import com.hq.ecmp.mscore.domain.JourneyInfo;
import com.hq.ecmp.mscore.domain.JourneyPassengerInfo;
import com.hq.ecmp.mscore.domain.JourneyPlanPriceInfo;
import com.hq.ecmp.mscore.domain.OrderAddressInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderStateTraceInfo;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.dto.dispatch.DispatchCarGroupDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchCountCarAndDriverDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchLockCarDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchLockDriverDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchSelectCarDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchSelectDriverDto;
import com.hq.ecmp.mscore.mapper.CarGroupDispatcherInfoMapper;
import com.hq.ecmp.mscore.mapper.CarGroupDriverRelationMapper;
import com.hq.ecmp.mscore.mapper.CarGroupInfoMapper;
import com.hq.ecmp.mscore.mapper.CarGroupServeScopeInfoMapper;
import com.hq.ecmp.mscore.mapper.CarInfoMapper;
import com.hq.ecmp.mscore.mapper.CostConfigInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverCarRelationInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverInfoMapper;
import com.hq.ecmp.mscore.mapper.EcmpOrgMapper;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.mapper.EnterpriseCarTypeInfoMapper;
import com.hq.ecmp.mscore.mapper.JourneyInfoMapper;
import com.hq.ecmp.mscore.mapper.JourneyPassengerInfoMapper;
import com.hq.ecmp.mscore.mapper.JourneyPlanPriceInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderAddressInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderStateTraceInfoMapper;
import com.hq.ecmp.mscore.mapper.RegimeInfoMapper;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.vo.CostConfigDetailInfoVo;
import com.hq.ecmp.mscore.vo.DispatchResultVo;
import com.hq.ecmp.mscore.vo.OrderStateCountVO;
import com.hq.ecmp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 调度业务实现
 *
 * @Author: zj.hu
 * @Date: 2020-03-17 23:36
 */
@Service
@Slf4j
public class DispatchServiceImpl implements IDispatchService {
    @Value("${dispatch.notBackCarGroup}")
    public int notBackCarGroup;
    @Value("${dispatch.backCarGroup}")
    public int backCarGroup;
    /**
     * 无车驳回的时间限制(:分钟)
     */
    @Value("${dispatch.noCarDeniedMinutes}")
    public int noCarDeniedMinutes;

    @Resource
    TokenService tokenService;

    @Resource
    RedisUtil redisUtil;

    @Resource
    OrderInfoMapper orderInfoMapper;

    @Resource
    OrderAddressInfoMapper orderAddressInfoMapper;

    @Resource
    JourneyInfoMapper journeyInfoMapper;

    @Resource
    JourneyPassengerInfoMapper journeyPassengerInfoMapper;

    @Resource
    CarInfoMapper carInfoMapper;
    @Resource
    DriverCarRelationInfoMapper driverCarRelationInfoMapper;

    @Resource
    CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;

    @Resource
    CarGroupInfoMapper carGroupInfoMapper;

    @Resource
    CarGroupDriverRelationMapper carGroupDriverRelationMapper;

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

    @Resource
    OrderStateTraceInfoMapper orderStateTraceInfoMapper;

    @Resource
    CostConfigInfoMapper costConfigInfoMapper;


    /**
     * 调度-获取可选择的车辆
     * <p>
     * 注意 模糊查询 需要 取交集
     *
     * @param dispatchSelectCarDto dispatchSelectCarDto
     * @return ApiResponse<DispatchResultVo>
     */
    @Override
    public ApiResponse<DispatchResultVo> getWaitSelectedCars(DispatchSelectCarDto dispatchSelectCarDto) {
        log.info("select.car....dispatchSelectCarDto={}", dispatchSelectCarDto);
        Long orderId = Long.parseLong(dispatchSelectCarDto.getOrderNo());

        LoginUser loginUser = new LoginUser();
        if (StringUtils.isNotEmpty(dispatchSelectCarDto.getDispatcherId())) {
            loginUser = tokenService.getLoginUser(dispatchSelectCarDto.getDispatcherId());
        }

        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        log.info("select.car....orderInfo={}", orderInfo);
        OrderAddressInfo orderAddressInfo = verifyOrderActualSetoutAddress(orderInfo).getData();
        log.info("select.car....orderAddressInfo={}", orderAddressInfo);
        String userCarCity = orderAddressInfo.getCityPostalCode();
        String carModelLevelType = dispatchSelectCarDto.getCarModelLevelType();

        int passengers = journeyPassengerInfoMapper.queryPeerCount(orderInfo.getJourneyId()) + 2;
        log.info("select.car....passengers={}", passengers);
        SelectCarConditionBo selectCarConditionBo = new SelectCarConditionBo();
        selectCarConditionBo.setCarLevel(carModelLevelType);
        selectCarConditionBo.setPassengers(passengers);
        selectCarConditionBo.setCityCode(userCarCity);
        selectCarConditionBo.setDriverId(dispatchSelectCarDto.getDriverId());
        selectCarConditionBo.setCarLicense(dispatchSelectCarDto.getPlateLicence());
        selectCarConditionBo.setCarTypeInfo(dispatchSelectCarDto.getCarTypeInfo());
        selectCarConditionBo.setDispatcherId(0L);
        if (StringUtils.isNotEmpty(dispatchSelectCarDto.getDispatcherId())) {
            selectCarConditionBo.setDispatcherId(Long.parseLong(loginUser.getUser().getUserId().toString()));
        }

        ApiResponse<List<WaitSelectedCarBo>> cars = selectCars(selectCarConditionBo, orderInfo);
        log.info("select.car....!cars.isSuccess()={}", !cars.isSuccess());
        if (!cars.isSuccess()) {
            return ApiResponse.error(cars.getMsg());
        }

        //优先级操作 识别 和 查询司机
        DispatchResultVo dispatchResultVo = new DispatchResultVo();
        dispatchResultVo.setCarList(cars.getData());
        log.info("select.car....dispatchResultVo={}", dispatchResultVo);
        return ApiResponse.success(dispatchResultVo);
    }

    /**
     * 调度-锁定选择的车辆
     *
     * @param dispatchInfoDto dispatchInfoDto
     * @return ApiResponse
     */
    @Override
    public synchronized ApiResponse lockSelectedCar(DispatchLockCarDto dispatchInfoDto) {
        if (StringUtils.isEmpty(dispatchInfoDto.getCarId())) {
            return ApiResponse.error(DispatchExceptionEnum.LOCK_CAR_NOT_EXIST.getDesc());
        }

        ApiResponse apiResponse = checkCarOwnCarGroupPricePlanInfo(dispatchInfoDto);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }

        int i = carInfoMapper.lockCar(Long.parseLong(dispatchInfoDto.getCarId()));
        if (i <= 0) {
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
        if (StringUtils.isEmpty(dispatchLockCarDto.getCarId())) {
            return ApiResponse.error(DispatchExceptionEnum.UNLOCK_CAR_NOT_EXIST.getDesc());
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

        log.info("dispatchSelectDriverDto={}", dispatchSelectDriverDto);
        Long orderId = Long.parseLong(dispatchSelectDriverDto.getOrderNo());
        LoginUser loginUser = new LoginUser();
        if (StringUtils.isNotEmpty(dispatchSelectDriverDto.getDispatcherId())) {
            loginUser = tokenService.getLoginUser(dispatchSelectDriverDto.getDispatcherId());
            log.info("loginUser={}", loginUser);
        }
        SelectDriverConditionBo selectDriverConditionBo = new SelectDriverConditionBo();

        if (StringUtils.isNotEmpty(dispatchSelectDriverDto.getCarId())) {
            selectDriverConditionBo.setCarId(dispatchSelectDriverDto.getCarId());
        }

        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        OrderAddressInfo orderAddressInfo = verifyOrderActualSetoutAddress(orderInfo).getData();

        selectDriverConditionBo.setCityCode(orderAddressInfo.getCityPostalCode());
        selectDriverConditionBo.setDispatcherId(0L);
        selectDriverConditionBo.setDriverNameOrPhone(dispatchSelectDriverDto.getDriverNameOrPhone());
        if (StringUtils.isNotEmpty(dispatchSelectDriverDto.getDispatcherId())) {
            selectDriverConditionBo.setDispatcherId(loginUser.getUser().getUserId());
        }


        List<WaitSelectedDriverBo> waitSelectedDriverBos = selectDrivers(selectDriverConditionBo, orderInfo).getData();

        //优先级识别 和 查询车辆
        DispatchResultVo dispatchResultVo = new DispatchResultVo();
        dispatchResultVo.setDriverList(waitSelectedDriverBos);

        return ApiResponse.success(dispatchResultVo);
    }

    /**
     * 调度-锁定选择的司机
     *
     * @param dispatchLockDriverDto dispatchLockDriverDto
     * @return ApiResponse
     */
    @Override
    public synchronized ApiResponse lockSelectedDriver(DispatchLockDriverDto dispatchLockDriverDto) {
        if (StringUtils.isEmpty(dispatchLockDriverDto.getDriverId())) {
            return ApiResponse.error(DispatchExceptionEnum.LOCK_DRIVER_NOT_EXIST.getDesc());
        }

        ApiResponse apiResponse = checkDriverOwnCarGroupPricePlanInfo(dispatchLockDriverDto);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }

        int i = driverInfoMapper.lockDriver(Long.parseLong(dispatchLockDriverDto.getDriverId()));

        if (i <= 0) {
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
        if (StringUtils.isEmpty(dispatchLockDriverDto.getDriverId())) {
            return ApiResponse.error(DispatchExceptionEnum.UNLOCK_DRIVER_NOT_EXIST.getDesc());
        }
        driverInfoMapper.unlockDriver(Long.parseLong(dispatchLockDriverDto.getDriverId()));
        return ApiResponse.success();
    }

    /**
     * 自动调度
     *
     * @param dispatchCountCarAndDriverDto dispatchCountCarAndDriverDto
     * @return ApiResponse<DispatchResultVo>
     */
    @Override
    public ApiResponse<DispatchResultVo> autoDispatch(@RequestBody DispatchCountCarAndDriverDto dispatchCountCarAndDriverDto) {
        DispatchResultVo dispatchResultVo = new DispatchResultVo();

        DispatchSelectCarDto dispatchSelectCarDto = new DispatchSelectCarDto();
        dispatchSelectCarDto.setOrderNo(dispatchCountCarAndDriverDto.getOrderNo());

        ApiResponse<DispatchResultVo> carsResult = getWaitSelectedCars(dispatchSelectCarDto);
        if (!carsResult.isSuccess()) {
            return ApiResponse.error(carsResult.getMsg());
        }

        List<WaitSelectedCarBo> cars = carsResult.getData().getCarList();

        if (cars.isEmpty()) {
            log.info(dispatchCountCarAndDriverDto.toString() + "-" + DispatchExceptionEnum.DRIVER_OR_CAR_NOT_FIND.getDesc());
            return ApiResponse.error(DispatchExceptionEnum.DRIVER_OR_CAR_NOT_FIND.getDesc());
        }

        Iterator<WaitSelectedCarBo> carInfoVOIterator = cars.iterator();


        DispatchLockCarDto dispatchLockCarDto = new DispatchLockCarDto();
        DispatchLockDriverDto dispatchLockDriverDto = new DispatchLockDriverDto();

        AtomicReference<Boolean> carFlag = new AtomicReference<>(false);
        AtomicReference<Boolean> driverFlag = new AtomicReference<>(false);

        List<WaitSelectedDriverBo> drivers = new ArrayList<>();

        while (carInfoVOIterator.hasNext()) {
            dispatchResultVo.setLockCar(1);

            WaitSelectedCarBo car = carInfoVOIterator.next();
            dispatchLockCarDto.setOrderNo(dispatchCountCarAndDriverDto.getOrderNo());
            dispatchLockCarDto.setCarId(car.getCarId().toString());

            ApiResponse lockCarResult = lockSelectedCar(dispatchLockCarDto);
            if (!lockCarResult.isSuccess()) {
                continue;
            }

            DispatchSelectDriverDto dispatchSelectDriverDto = new DispatchSelectDriverDto();
            dispatchSelectDriverDto.setOrderNo(dispatchCountCarAndDriverDto.getOrderNo());
            dispatchSelectDriverDto.setCarId(car.getCarId().toString());
            ApiResponse<DispatchResultVo> driversResult = getWaitSelectedDrivers(dispatchSelectDriverDto);
            if (!driversResult.isSuccess()) {
                continue;
            }

            drivers = driversResult.getData().getDriverList();

            if (drivers.isEmpty()) {
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
                    if (!lockDriverResult.isSuccess()) {
                        continue;
                    }
                    dispatchResultVo.setLockDriver(0);
                    driverFlag.set(true);
                    break;
                }
            }

            if (!driverFlag.get()) {
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
        dispatchResultVo.setHasCar(cars.isEmpty() ? 0 : 1);
        dispatchResultVo.setHasDriver(drivers.isEmpty() ? 0 : 1);

        return ApiResponse.success(dispatchResultVo);
    }

    @Override
    public OrderStateCountVO getOrderStateCount(SysUser sysUser) {
//        List<Long> users = ecmpUserMapper.getUserListByOrgId(orgComcany);
        List<SysRole> role = sysUser.getRoles();
        List<SysRole> collect = role.stream().filter(p -> CommonConstant.ADMIN_ROLE.equals(p.getRoleKey()) || CommonConstant.SUB_ADMIN_ROLE.equals(p.getRoleKey())).collect(Collectors.toList());
        Boolean isAdmin = false;
        Boolean isDispatcher = false;
        ApplyDispatch query = new ApplyDispatch();
        query.setCompanyId(sysUser.getOwnerCompany());
        query.setUserId(sysUser.getUserId());
        List<DispatchCarGroupDto> disCarGroupInfos = carGroupInfoMapper.getDisCarGroupInfoByUserId(query.getUserId(), query.getCompanyId());
        String carGroupIds = disCarGroupInfos.stream().map(p -> p.getCarGroupId().toString()).collect(Collectors.joining(",", "", ""));
        query.setCarGroupIds(carGroupIds);
        if ("1".equals(sysUser.getItIsDispatcher())) {
            isDispatcher = true;
            if (!CollectionUtils.isEmpty(disCarGroupInfos)) {
                String itIsInner = disCarGroupInfos.get(0).getItIsInner();
                if (itIsInner.equals(CarConstant.IT_IS_USE_INNER_CAR_GROUP_IN)) {
                    query.setIsInnerDispatch(1);
                } else {
                    query.setIsInnerDispatch(2);
                }
            }
        }
        if (!CollectionUtils.isEmpty(collect)) {//是管理员
            isAdmin = true;
        }
        if (isAdmin && isDispatcher) {
            query.setRoleData(2);
        } else if (isAdmin && !isDispatcher) {
            query.setRoleData(1);
        } else if (!isAdmin && isDispatcher) {
            query.setRoleData(3);
        }
        if (!CollectionUtils.isEmpty(disCarGroupInfos)) {
            String collect1 = disCarGroupInfos.stream().map(p -> p.getDeptId().toString()).collect(Collectors.joining(",", "", ""));
            query.setDeptId(collect1);
        }
        Long dispatchedCount = orderInfoMapper.getCountForDispatched(query);
//        Long reassignedCount = orderInfoMapper.getCountForReassigned(orgComcany, users);
        OrderStateCountVO vo = new OrderStateCountVO(dispatchedCount, 0L);
        return vo;
    }


    /**
     * 找到符合条件的车辆
     *
     * @param selectCarConditionBo selectCarConditionBo
     * @param orderInfo            orderInfo
     * @return ApiResponse<List       <       WaitSelectedCarBo>>
     */
    private ApiResponse<List<WaitSelectedCarBo>> selectCars(SelectCarConditionBo selectCarConditionBo, OrderInfo orderInfo) {
        log.info("select.car....selectCarConditionBo={}", selectCarConditionBo);
        ApiResponse<List<CarGroupServeScopeInfo>> carGroupServiceScopes = selectCarGroupServiceScope(selectCarConditionBo.getCityCode(), selectCarConditionBo.getDispatcherId());

        if (!carGroupServiceScopes.isSuccess()) {
            return ApiResponse.error(carGroupServiceScopes.getMsg());
        }

        log.info("select.car....carGroupServiceScopes getData={}", carGroupServiceScopes.getData());

        List<CarInfo> cars = new ArrayList<>();

        for (CarGroupServeScopeInfo carGroupServeScopeInfo : carGroupServiceScopes.getData()) {
            selectCarConditionBo.setCarGroupId(carGroupServeScopeInfo.getCarGroupId().toString());
            List<CarInfo> acars;
            if (StringUtils.isEmpty(selectCarConditionBo.getCarLicense())) {
                acars = carInfoMapper.dispatcherSelectCarGroupOwnedCarInfoList(selectCarConditionBo);
            } else {
                acars = carInfoMapper.dispatcherSelectCarGroupOwnedCarInfoListUseCarLicense(selectCarConditionBo);
            }
            cars.addAll(acars);
        }
        log.info("select.car...cars={}", cars);

        //如果有驾驶员，过滤驾驶员车辆数据
        if (selectCarConditionBo.getDriverId() != null) {
            DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
            driverCarRelationInfo.setDriverId(Long.valueOf(selectCarConditionBo.getDriverId()));
            Set<Long> longs = driverCarRelationInfoMapper.selectDriverCarRelationInfoList(driverCarRelationInfo)
                    .stream().map(DriverCarRelationInfo::getCarId).collect(Collectors.toSet());
            longs.add(orderInfo.getCarId());
            cars = cars.stream().filter(x -> longs.contains(x.getCarId())).collect(Collectors.toList());
        }
        log.info("select.car...cars.2.={}", cars);

        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(journeyInfo.getRegimenId());
        log.info("select.car...journeyInfo ={}regimeInfo={}", journeyInfo, regimeInfo);

        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        journeyPassengerInfo.setJourneyId(journeyInfo.getJourneyId());
        List<JourneyPassengerInfo> journeyPassengerInfoList = journeyPassengerInfoMapper.selectJourneyPassengerInfoList(journeyPassengerInfo);

        if (journeyPassengerInfoList.isEmpty()) {
            return ApiResponse.error("行程乘客数据异常");
        }
        log.info("select.car...journeyPassengerInfoList.={}", journeyPassengerInfoList);


        int seatNumber = 0;
        if (CarConstant.SELFDRIVER_YES.equals(selectCarConditionBo.getItIsSelfDriver())) {
            //人数=同行人+1
            seatNumber = journeyPassengerInfoList.get(0).getPeerNumber() == null ? 0 : journeyPassengerInfoList.get(0).getPeerNumber();
        } else {
            //人数=同行人+1
            seatNumber = journeyPassengerInfoList.get(0).getPeerNumber() == null ? 0 : journeyPassengerInfoList.get(0).getPeerNumber() + 1;
        }
        log.info("select.car...seatNumber.={}", seatNumber);


        //车辆荷载人数 筛选
        Iterator<CarInfo> iteratorCarSeatNumber = cars.iterator();
        while (iteratorCarSeatNumber.hasNext()) {
            CarInfo carInfo = iteratorCarSeatNumber.next();
            if (carInfo.getSeatNum() < seatNumber) {
                iteratorCarSeatNumber.remove();
            }
        }
        log.info("select.car...车辆荷载人数 筛选={}", cars);


        String allowCarModelLevel = regimeInfo.getUseCarModeOwnerLevel();

        ApiResponse<OrderTaskClashBo> apiResponseSelectOrderSetOutAndArrivalTime = selectOrderSetOutAndArrivalTime(orderInfo);
        if (!apiResponseSelectOrderSetOutAndArrivalTime.isSuccess()) {
            return ApiResponse.error(apiResponseSelectOrderSetOutAndArrivalTime.getMsg());
        }

        OrderTaskClashBo orderTaskClashBo = apiResponseSelectOrderSetOutAndArrivalTime.getData();
        log.info("select.car...orderTaskClashBo={}", orderTaskClashBo);

        List<WaitSelectedCarBo> waitSelectedCarBoList = new LinkedList<>();
        cars.stream().forEach(carInfo -> {
            WaitSelectedCarBo waitSelectedCarBo = new WaitSelectedCarBo();
            waitSelectedCarBo.setCarId(carInfo.getCarId());
            waitSelectedCarBo.setCarModelName(carInfo.getCarType());
            EnterpriseCarTypeInfo enterpriseCarTypeInfo = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(carInfo.getCarTypeId());
            waitSelectedCarBo.setCarType(enterpriseCarTypeInfo.getName());
            waitSelectedCarBo.setLevel(enterpriseCarTypeInfo.getLevel());

            waitSelectedCarBo.setColor(carInfo.getCarColor());
            waitSelectedCarBo.setPlateLicence(carInfo.getCarLicense());
            waitSelectedCarBo.setStatus(carInfo.getState());
            waitSelectedCarBo.setCarGroupId(carInfo.getCarGroupId());
            waitSelectedCarBo.setState(carInfo.getState());

            waitSelectedCarBo.setPowerType(carInfo.getPowerType());
            waitSelectedCarBo.setAssetTag(carInfo.getAssetTag());
            waitSelectedCarBo.setLockState(carInfo.getLockState());

            EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(carInfo.getDeptId());
            waitSelectedCarBo.setDeptName(ecmpOrg.getDeptName());
            waitSelectedCarBo.setSource(carInfo.getSource());

            orderTaskClashBo.setCarId(carInfo.getCarId());
            orderTaskClashBo.setCarLicense(carInfo.getCarLicense());

            if (StringUtils.isNotEmpty(allowCarModelLevel)) {
                if (allowCarModelLevel.contains(enterpriseCarTypeInfo.getLevel())) {
                    waitSelectedCarBo.setLevelIsMatch(CarLevelMatchEnum.MATCH.getCode());
                } else {
                    waitSelectedCarBo.setLevelIsMatch(CarLevelMatchEnum.UN_MATCH.getCode());
                }
            } else {
                waitSelectedCarBo.setLevelIsMatch(CarLevelMatchEnum.UN_MATCH.getCode());
            }

            log.info("select.car..orderTaskClashBo={}", orderTaskClashBo);

//            List<OrderInfo> orderInfosSetOutClash = orderInfoMapper.getSetOutClashTask(orderTaskClashBo);
//            List<OrderInfo> orderInfosArrivalClash = orderInfoMapper.getSetOutClashTask(orderTaskClashBo);
            List<OrderInfo> orderInfosSetOutClash =  getSetOutClashTask(orderTaskClashBo);
            List<OrderInfo> orderInfosArrivalClash =  getArrivalClashTask(orderTaskClashBo);


            log.info("select.car..orderInfosSetOutClash={},orderInfosArrivalClash={}", orderInfosSetOutClash, orderInfosArrivalClash);

            if (orderInfosSetOutClash.isEmpty() && orderInfosArrivalClash.isEmpty()) {
                waitSelectedCarBo.setTaskConflict(TaskConflictEnum.CONFLICT_FREE);

                List<OrderInfo> orderInfosBefore = orderInfoMapper.getSetOutBeforeTaskForCarOrDriver(orderTaskClashBo);
                List<OrderInfo> orderInfosAfter = orderInfoMapper.getArrivalAfterTaskForCarOrDriver(orderTaskClashBo);

                if (!orderInfosBefore.isEmpty()) {
                    waitSelectedCarBo.setBeforeTaskOrderId(orderInfosBefore.get(0).getOrderId());
                    waitSelectedCarBo.setBeforeTaskEndTime(new Timestamp(orderInfosBefore.get(0).getCreateTime().getTime()));
                }
                if (!orderInfosAfter.isEmpty()) {
                    waitSelectedCarBo.setAfterTaskOrderId(orderInfosAfter.get(0).getOrderId());
                    waitSelectedCarBo.setAfterTaskBeginTime(new Timestamp(orderInfosAfter.get(0).getUpdateTime().getTime()));
                }
            }
            if ((!orderInfosSetOutClash.isEmpty()) && (!orderInfosArrivalClash.isEmpty())) {
                waitSelectedCarBo.setTaskConflict(TaskConflictEnum.BEFORE_AND_AFTER_TASK_CLASH);
            }
            if ((!orderInfosSetOutClash.isEmpty()) && (orderInfosArrivalClash.isEmpty())) {
                waitSelectedCarBo.setTaskConflict(TaskConflictEnum.BEFORE_TASK_CLASH);
                List<OrderInfo> orderInfosAfter = orderInfoMapper.getArrivalAfterTaskForCarOrDriver(orderTaskClashBo);
                if (!orderInfosAfter.isEmpty()) {
                    waitSelectedCarBo.setAfterTaskOrderId(orderInfosAfter.get(0).getOrderId());
                    waitSelectedCarBo.setAfterTaskBeginTime(new Timestamp(orderInfosAfter.get(0).getUpdateTime().getTime()));
                }
            }
            if ((orderInfosSetOutClash.isEmpty()) && (!orderInfosArrivalClash.isEmpty())) {
                waitSelectedCarBo.setTaskConflict(TaskConflictEnum.AFTER_TASK_CLASH);
                List<OrderInfo> orderInfosBefore = orderInfoMapper.getSetOutBeforeTaskForCarOrDriver(orderTaskClashBo);
                if (!orderInfosBefore.isEmpty()) {
                    waitSelectedCarBo.setBeforeTaskOrderId(orderInfosBefore.get(0).getOrderId());
                    waitSelectedCarBo.setBeforeTaskEndTime(new Timestamp(orderInfosBefore.get(0).getCreateTime().getTime()));
                }
            }

            CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carInfo.getCarGroupId());
            waitSelectedCarBo.setCarGroupName(carGroupInfo.getCarGroupName());
            //车辆图片
            waitSelectedCarBo.setCarTypeImage(enterpriseCarTypeInfo.getImageUrl());
            waitSelectedCarBoList.add(waitSelectedCarBo);
        });

        log.info("select.car..waitSelectedCarBoList={}", waitSelectedCarBoList);

        //始终进行冲突检查
        Iterator<WaitSelectedCarBo> iterator = waitSelectedCarBoList.iterator();
        while (iterator.hasNext()) {
            WaitSelectedCarBo waitSelectedCarBo = iterator.next();
            if (!TaskConflictEnum.CONFLICT_FREE.equals(waitSelectedCarBo.getTaskConflict())) {
                iterator.remove();
            }
        }

        log.info("select.car..不展示冲突的车辆 waitSelectedCarBoList={}", waitSelectedCarBoList);


        waitSelectedCarBoList.stream().forEach(waitSelectedCarBo -> {
            waitSelectedCarBo.embellish();
        });

        return ApiResponse.success(waitSelectedCarBoList);
    }


    /**
     * 找到符合条件的司机
     *
     * @param selectDriverConditionBo selectDriverConditionBo
     * @param orderInfo               orderInfo
     * @return ApiResponse<List       <       WaitSelectedDriverBo>>
     */
    private ApiResponse<List<WaitSelectedDriverBo>> selectDrivers(SelectDriverConditionBo selectDriverConditionBo,
                                                                  OrderInfo orderInfo) {
        log.info("selectDrivers.......selectDriverConditionBo={},orderInfo={}", selectDriverConditionBo, orderInfo.toString());
        ApiResponse<List<CarGroupServeScopeInfo>> carGroupServiceScopesApiResponse = selectCarGroupServiceScope(selectDriverConditionBo.getCityCode(), selectDriverConditionBo.getDispatcherId());

        if (!carGroupServiceScopesApiResponse.isSuccess()) {
            log.info("selectDrivers.......!carGroupServiceScopesApiResponse.isSuccess()");
            return ApiResponse.error(carGroupServiceScopesApiResponse.getMsg());
        }

        ApiResponse<OrderTaskClashBo> orderTaskClashBoApiResponse = selectOrderSetOutAndArrivalTime(orderInfo);
        if (!orderTaskClashBoApiResponse.isSuccess()) {
            log.info("selectDrivers.......!orderTaskClashBoApiResponse.isSuccess()");
            return ApiResponse.error(orderTaskClashBoApiResponse.getMsg());
        }
        Date setOutDate = new Date(orderTaskClashBoApiResponse.getData().getSetOutTime().getTime());
        selectDriverConditionBo.setWorkDay(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, setOutDate));
        log.info("selectDrivers.......setOutDate={}...orderTaskClashBoApiResponse.getData()={}", setOutDate,orderTaskClashBoApiResponse.getData());

        List<DriverInfo> drivers = new ArrayList<>();

        for (CarGroupServeScopeInfo carGroupServeScopeInfo : carGroupServiceScopesApiResponse.getData()) {
            List<DriverInfo> adrivers;
            selectDriverConditionBo.setCarGroupId(carGroupServeScopeInfo.getCarGroupId());

            adrivers = driverInfoMapper.dispatcherSelectDriverUseDriverNameOrPhone(selectDriverConditionBo);

            drivers.addAll(adrivers);
        }

        log.info("selectDrivers.......drivers={}", drivers);

        ApiResponse<OrderTaskClashBo> apiResponseSelectOrderSetOutAndArrivalTime = selectOrderSetOutAndArrivalTime(orderInfo);
        if (!apiResponseSelectOrderSetOutAndArrivalTime.isSuccess()) {
            return ApiResponse.error(apiResponseSelectOrderSetOutAndArrivalTime.getMsg());
        }
        OrderTaskClashBo orderTaskClashBo = apiResponseSelectOrderSetOutAndArrivalTime.getData();
        log.info("selectDrivers.......orderTaskClashBo={}", orderTaskClashBo);

        List<WaitSelectedDriverBo> waitSelectedDriverBoList = new ArrayList<>();

        drivers.stream().forEach(driver -> {
            orderTaskClashBo.setDriverId(driver.getDriverId());

            WaitSelectedDriverBo waitSelectedDriverBo = new WaitSelectedDriverBo();
            waitSelectedDriverBo.setDriverId(driver.getDriverId());
            waitSelectedDriverBo.setDriverName(driver.getDriverName());
            waitSelectedDriverBo.setState(driver.getState());
            waitSelectedDriverBo.setMobile(driver.getMobile());
            waitSelectedDriverBo.setDriverPhone(driver.getMobile());

            if (driver.getUserId() != null) {
                EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(driver.getUserId());
                if (ecmpUser == null) {
                    waitSelectedDriverBo.setJobNumber(NoValueCommonEnum.NO_STRING.getCode());
                } else {
                    waitSelectedDriverBo.setJobNumber(ecmpUser.getJobNumber());
                }
                if (ecmpUser == null) {
                    waitSelectedDriverBo.setDeptName(NoValueCommonEnum.NO_STRING.getCode());
                } else {
                    EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(ecmpUser.getDeptId());
                    waitSelectedDriverBo.setDeptName(ecmpOrg.getDeptName());
                }
            } else {
                waitSelectedDriverBo.setJobNumber(NoValueCommonEnum.NO_STRING.getCode());
                waitSelectedDriverBo.setDeptName(NoValueCommonEnum.NO_STRING.getCode());
            }

            List<CarGroupInfo> carGroupInfo;
            carGroupInfo = carGroupInfoMapper.selectCarGroupsByDriverId(driver.getDriverId());
            waitSelectedDriverBo.setFleetPhone(carGroupInfo.get(0).getTelephone());

            log.info("selectDrivers.......orderTaskClashBo={}.....dirver_phone={}...", orderTaskClashBo, driver.getMobile());
            List<OrderInfo> orderInfosSetOutClash =  getSetOutClashTask(orderTaskClashBo);
            List<OrderInfo> orderInfosArrivalClash =  getArrivalClashTask(orderTaskClashBo);


            log.info("selectDrivers....... orderInfosSetOutClash={},orderInfosArrivalClash={}", orderInfosSetOutClash, orderInfosArrivalClash);
            if (orderInfosSetOutClash.isEmpty() && orderInfosArrivalClash.isEmpty()) {
                waitSelectedDriverBo.setTaskConflict(TaskConflictEnum.CONFLICT_FREE);
                List<OrderInfo> orderInfosBefore = orderInfoMapper.getSetOutBeforeTaskForCarOrDriver(orderTaskClashBo);
                List<OrderInfo> orderInfosAfter = orderInfoMapper.getArrivalAfterTaskForCarOrDriver(orderTaskClashBo);

                if (!orderInfosBefore.isEmpty()) {
                    waitSelectedDriverBo.setBeforeTaskOrderId(orderInfosBefore.get(0).getOrderId());
                    waitSelectedDriverBo.setBeforeTaskEndTime(orderInfosBefore.get(0).getCreateTime());
                }
                if (!orderInfosAfter.isEmpty()) {
                    waitSelectedDriverBo.setAfterTaskOrderId(orderInfosAfter.get(0).getOrderId());
                    waitSelectedDriverBo.setAfterTaskBeginTime(orderInfosAfter.get(0).getUpdateTime());
                }
            }
            if ((!orderInfosSetOutClash.isEmpty()) && (!orderInfosArrivalClash.isEmpty())) {
                waitSelectedDriverBo.setTaskConflict(TaskConflictEnum.BEFORE_AND_AFTER_TASK_CLASH);
            }
            if ((!orderInfosSetOutClash.isEmpty()) && (orderInfosArrivalClash.isEmpty())) {
                waitSelectedDriverBo.setTaskConflict(TaskConflictEnum.BEFORE_TASK_CLASH);
                List<OrderInfo> orderInfosAfter = orderInfoMapper.getArrivalAfterTaskForCarOrDriver(orderTaskClashBo);
                if (!orderInfosAfter.isEmpty()) {
                    waitSelectedDriverBo.setAfterTaskOrderId(orderInfosAfter.get(0).getOrderId());
                    waitSelectedDriverBo.setAfterTaskBeginTime(orderInfosAfter.get(0).getUpdateTime());
                }
            }
            if ((orderInfosSetOutClash.isEmpty()) && (!orderInfosArrivalClash.isEmpty())) {
                waitSelectedDriverBo.setTaskConflict(TaskConflictEnum.AFTER_TASK_CLASH);

                List<OrderInfo> orderInfosBefore = orderInfoMapper.getSetOutBeforeTaskForCarOrDriver(orderTaskClashBo);
                if (!orderInfosBefore.isEmpty()) {
                    waitSelectedDriverBo.setBeforeTaskOrderId(orderInfosBefore.get(0).getOrderId());
                    waitSelectedDriverBo.setBeforeTaskEndTime(orderInfosBefore.get(0).getCreateTime());
                }
            }
            log.info("selectDrivers.......waitSelectedDriverBo={}", waitSelectedDriverBo);
            waitSelectedDriverBoList.add(waitSelectedDriverBo);
        });

        log.info("selectDrivers.......waitSelectedDriverBoList={}", waitSelectedDriverBoList);

        //始终进行冲突检查
        Iterator<WaitSelectedDriverBo> iterator = waitSelectedDriverBoList.iterator();
        while (iterator.hasNext()) {
            WaitSelectedDriverBo waitSelectedDriverBo = iterator.next();
            if (!TaskConflictEnum.CONFLICT_FREE.equals(waitSelectedDriverBo.getTaskConflict())) {
                iterator.remove();
            }
        }

        log.info("selectDrivers.......2.waitSelectedDriverBoList={}", waitSelectedDriverBoList);

        waitSelectedDriverBoList.stream().forEach(driver -> {
            driver.embellish();
        });


        return ApiResponse.success(waitSelectedDriverBoList);
    }



    Predicate<OrderInfo> clashOrder = order -> {
        log.info("OrderState.clash.endServerStates()={},order.getState()={},orderId={}",OrderState.endServerStates(),order.getState(),order.getOrderId());

        boolean clash = !OrderState.endServerStates().contains(order.getState());
        log.info("OrderState.clash={}",clash);
        return clash;
//                || !waitingServiceExpired(order);
    };

    /**
     *   如果过了时间一直是待服务的话，车辆可以被再派单，也就是列表默认应该查出来
     * @param order
     * @return
     */
//    private boolean waitingServiceExpired(OrderInfo order) {
//        if(OrderState.waitingServiceExpired().contains(order.getState())){
//
//            journeyPlanPriceInfoMapper.
//            JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(order.getJourneyId());
//            journeyInfo.get
//
//        }
//
//        return false;
//    }


    private List<OrderInfo> getArrivalClashTask(OrderTaskClashBo orderTaskClashBo) {
        List<OrderInfo> orderInfosArrivalClash = orderInfoMapper.getArrivalClashTask(orderTaskClashBo);
        log.info("OrderState.clash.orderInfosArrivalClash={}",orderInfosArrivalClash);
        return orderInfosArrivalClash.stream().filter(o->clashOrder.test(  o ))
                .collect(Collectors.toList());
    }

    private List<OrderInfo> getSetOutClashTask(OrderTaskClashBo orderTaskClashBo) {
        List<OrderInfo> orderInfosSetOutClash = orderInfoMapper.getSetOutClashTask(orderTaskClashBo);
        log.info("OrderState.clash.orderInfosSetOutClash={}",orderInfosSetOutClash);
        return orderInfosSetOutClash.stream().filter(o->clashOrder.test(  o ))
                .collect(Collectors.toList());
    }

    /**
     * 获取  订单对的行程节点的 计划开始时间和结束时间  从预算价表中获取
     *
     * @param orderInfo orderInfo
     * @return ApiResponse<OrderTaskClashBo>
     */
    private ApiResponse<OrderTaskClashBo> selectOrderSetOutAndArrivalTime(OrderInfo orderInfo) {
        JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
        journeyPlanPriceInfo.setNodeId(orderInfo.getNodeId());
        journeyPlanPriceInfo.setJourneyId(orderInfo.getJourneyId());

        log.info("journeyPlanPriceInfo.1.={}", journeyPlanPriceInfo);
        List<JourneyPlanPriceInfo> journeyPlanPriceInfos = journeyPlanPriceInfoMapper.selectJourneyPlanPriceInfoList(journeyPlanPriceInfo);
        log.info(" journeyPlanPriceInfos.={} ", journeyPlanPriceInfos);
        if (journeyPlanPriceInfos.isEmpty()) {
            return ApiResponse.error(DispatchExceptionEnum.ORDER_NOT_FIND_PLAN_PRICE.getDesc());
        }
        journeyPlanPriceInfo = journeyPlanPriceInfos.get(0);
        log.info("journeyPlanPriceInfo.2.={} ", journeyPlanPriceInfo );
        Calendar setOutCalendar = Calendar.getInstance();
        setOutCalendar.setTime(journeyPlanPriceInfo.getPlannedDepartureTime());
        setOutCalendar.add(Calendar.MINUTE, -notBackCarGroup);


        Calendar arrivalCalendar = Calendar.getInstance();
        arrivalCalendar.setTime(journeyPlanPriceInfo.getPlannedArrivalTime());
        arrivalCalendar.add(Calendar.MINUTE, notBackCarGroup);

        OrderTaskClashBo orderTaskClashBo = new OrderTaskClashBo();
        orderTaskClashBo.setSetOutTime(new Date(setOutCalendar.getTimeInMillis()));
        orderTaskClashBo.setArrivalTime(new Date(arrivalCalendar.getTimeInMillis()));

        log.info("orderTaskClashBo={}",orderTaskClashBo);
        return ApiResponse.success(orderTaskClashBo);
    }

    /**
     * 根据 调度员信息 查询 调度员归属的车队是否符合服务范围要求
     *
     * @param cityCode         cityCode
     * @param dispatcherUserId dispatcherUserId
     * @return ApiResponse<List       <       CarGroupServeScopeInfo>>
     */
    private ApiResponse<List<CarGroupServeScopeInfo>> selectCarGroupServiceScope(String cityCode, Long dispatcherUserId) {
        log.info("selectCarGroupServiceScope...cityCode={},dispatcherUserId={}", cityCode, dispatcherUserId);
        CarGroupDispatcherInfo carGroupDispatcher = new CarGroupDispatcherInfo();
        if (dispatcherUserId.equals(0L)) {
            carGroupDispatcher.setDispatcherId(null);
        }
        carGroupDispatcher.setUserId(dispatcherUserId);
        log.info("selectCarGroupServiceScope...carGroupDispatcher={}", carGroupDispatcher);
        List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcher);
        log.info("selectCarGroupServiceScope.......carGroupDispatcherInfos={}", carGroupDispatcherInfos);
        if (carGroupDispatcherInfos.isEmpty()) {
            return ApiResponse.error(DispatchExceptionEnum.NOT_FIND_SUITABLE_CAR_SERVICE_SCOPE.getDesc());
        }

        List<CarGroupServeScopeInfo> carGroupServeScopeInfoListResult = new LinkedList<>();

        for (CarGroupDispatcherInfo carGroupDispatcherInfo : carGroupDispatcherInfos) {
            Long carGroupId = carGroupDispatcherInfo.getCarGroupId();
            CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            if (carGroupInfo == null) {
                continue;
            }

            CarGroupServeScopeInfo carGroupServeScopeInfo = new CarGroupServeScopeInfo();
            carGroupServeScopeInfo.setCarGroupId(carGroupInfo.getCarGroupId());
            List<CarGroupServeScopeInfo> carGroupServeScopeInfoList = carGroupServeScopeInfoMapper.queryAll(carGroupServeScopeInfo);
            if (carGroupServeScopeInfoList.isEmpty()) {
                continue;
            }

            Iterator<CarGroupServeScopeInfo> iterator = carGroupServeScopeInfoList.iterator();
            boolean serviceCitySatisfy = false;
            while (iterator.hasNext()) {
                CarGroupServeScopeInfo cgss = iterator.next();
                if (cityCode.equals(cgss.getCity())) {
                    serviceCitySatisfy = true;
                } else {
                    iterator.remove();
                }
            }
            if (serviceCitySatisfy) {
                carGroupServeScopeInfoListResult.addAll(carGroupServeScopeInfoList);
            }
        }
        log.info("selectCarGroupServiceScope......carGroupServeScopeInfoListResult={}", carGroupServeScopeInfoListResult);
        if (carGroupServeScopeInfoListResult.isEmpty()) {
            return ApiResponse.error(DispatchExceptionEnum.DISPATCHER_OWN_CAR_GROUP_SCOPE_IS_TOO_SMALL.getDesc());
        }

        return ApiResponse.success(carGroupServeScopeInfoListResult);
    }

    /**
     * 校验订单的真实出发地址
     */
    public ApiResponse<OrderAddressInfo> verifyOrderActualSetoutAddress(OrderInfo orderInfo) {

        if (orderInfo == null) {
            return ApiResponse.error(DispatchExceptionEnum.ORDER_NOT_EXIST.getDesc());
        }
        OrderAddressInfo orderAddressInfoParam = new OrderAddressInfo();
        orderAddressInfoParam.setOrderId(orderInfo.getOrderId());
        orderAddressInfoParam.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);

        OrderAddressInfo orderAddressInfo = orderAddressInfoMapper.queryOrderStartAndEndInfo(orderAddressInfoParam);

        if (orderAddressInfo == null) {
            return ApiResponse.error(DispatchExceptionEnum.ORDER_NOT_FIND_SET_OUT_ADDRESS.getDesc());
        }
        return ApiResponse.success(orderAddressInfo);
    }


    /**
     * 无车驳回操作（非改派的，改派的直接用1.0的申请改派审核功能即可）
     *
     * @param orderId 订单id
     * @param reason  驳回原因
     * @param userId  创建人
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void noCarDenied(Long orderId, String reason, Long userId) throws Exception {
        OrderInfo orderInfoOld = orderInfoMapper.selectOrderInfoById(orderId);
        Long duration = (DateUtils.getNowDate().getTime() - orderInfoOld.getCreateTime().getTime()) / 1000;
        if (duration < noCarDeniedMinutes * 60) {
            throw new Exception("未到" + noCarDeniedMinutes + "分钟,驳回失败");
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setState(OrderState.ORDERCLOSE.getState());
        orderInfo.setOrderId(orderId);
        orderInfoMapper.updateOrderInfo(orderInfo);
        OrderStateTraceInfo orderStateTraceInfo = OrderStateTraceInfo.builder().orderId(orderId)
                .content(reason)
                .state(OrderStateTrace.ORDERDENIED.getState())
                .build();
        orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
        orderStateTraceInfo.setCreateBy(String.valueOf(userId));
        orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
    }


    /**
     * 检查 车辆所属车队的价格计划信息
     *
     * @return ApiResponse ApiResponse
     */
    public ApiResponse checkCarOwnCarGroupPricePlanInfo(DispatchLockCarDto dispatchLockCarDto) {
        //包车时长  半日 整日  多日
        String rentTime = dispatchLockCarDto.getRentTime();
        if (Double.parseDouble(rentTime) >= 1) {

            if (((int) (Double.parseDouble(rentTime) * 10) / 10) != 0) {
                rentTime = "T009";
            } else {
                rentTime = "T002";
            }
        } else {
            rentTime = "T001";
        }

        //车队服务模式
        String carGroupServiceMode = dispatchLockCarDto.getCarGroupServiceMode();
        //内外车队
        String carGroupSource = dispatchLockCarDto.getCarGroupSource();
        //是否自驾
        String itIsSelfDriver = dispatchLockCarDto.getItIsSelfDriver();

        //查询车辆归属车队
        //CarGroupInfo carGroupInfo_car=null;
        //查询车辆信息
        CarInfo carInfo;
        //查询车辆车型级别
        EnterpriseCarTypeInfo carTypeInfo;

        if (StringUtils.isNotEmpty(dispatchLockCarDto.getCarId())) {
            carInfo = carInfoMapper.selectCarInfoById(Long.parseLong(dispatchLockCarDto.getCarId()));

            if (carInfo == null) {
                return ApiResponse.error("车辆未找到");
            }
            //一个车辆只属于一个车队
            //carGroupInfo_car=carGroupInfoMapper.selectCarGroupInfoById(carInfo.getCarGroupId());


            carTypeInfo = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(carInfo.getCarTypeId());

            if (carTypeInfo == null) {
                return ApiResponse.error("车辆对应车型未找到");
            }
        } else {
            return ApiResponse.error("车辆未正确传递");
        }

        //查询订单出发城市
        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(Long.parseLong(dispatchLockCarDto.getOrderNo()));
        orderAddressInfo.setType("A000");
        List<OrderAddressInfo> orderAddressInfoList = orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfo);
        orderAddressInfo = orderAddressInfoList.get(0);

        //查询调度员归属车队
        LoginUser loginUser = new LoginUser();
        if (StringUtils.isNotEmpty(dispatchLockCarDto.getDispatcherId())) {
            loginUser = tokenService.getLoginUser(dispatchLockCarDto.getDispatcherId());
        }

        //查询车辆归属车队
        CarGroupInfo carGroupInfo_car = null;

        if (StringUtils.isNotEmpty(dispatchLockCarDto.getCarId())) {
            CarInfo carInfo_ac = carInfoMapper.selectCarInfoById(Long.parseLong(dispatchLockCarDto.getCarId()));
            carGroupInfo_car = carGroupInfoMapper.selectCarGroupInfoById(carInfo_ac.getCarGroupId());
            if (carGroupInfo_car == null) {
                return ApiResponse.error("未找到车辆所在的车队");
            }
        }

        //查询用车人所在公司编号
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(Long.parseLong(dispatchLockCarDto.getOrderNo()));

        //查询车队（调度员、司机、）归价格计划视图对象
        CarGroupPricePlanInfoBo carGroupPricePlanInfoBo = new CarGroupPricePlanInfoBo();
        carGroupPricePlanInfoBo.setCarGroupId(carGroupInfo_car.getCarGroupId());
        carGroupPricePlanInfoBo.setCityCode(orderAddressInfo.getCityPostalCode());
        carGroupPricePlanInfoBo.setCompanyId(orderInfo.getCompanyId());

        List<CostConfigDetailInfoVo> costConfigDetailInfoVoList = costConfigInfoMapper.selectCostConfigDetailInfo(carGroupPricePlanInfoBo);
        Iterator<CostConfigDetailInfoVo> costConfigDetailInfoVoIterator = costConfigDetailInfoVoList.iterator();

        //价格计划是否存在
        Boolean fourExist = false;
        Boolean eightExist = false;

        while (costConfigDetailInfoVoIterator.hasNext()) {
            CostConfigDetailInfoVo costConfigDetailInfoVo = costConfigDetailInfoVoIterator.next();
            if (rentTime.equals(CarRentTypeEnum.FOUR_HOURS.getCode())) {
                //包车时长是否匹配（日租、半日租）
                if (rentTime.equals(costConfigDetailInfoVo.getRentType()) &&
                        //车队
                        (carGroupInfo_car.getCarGroupId().equals(costConfigDetailInfoVo.getCarGroupId())) &&
                        //城市
                        (orderAddressInfo.getCityPostalCode().equals(costConfigDetailInfoVo.getCityCode())) &&
                        //车队服务模式
                        carGroupServiceMode.equals(costConfigDetailInfoVo.getCarGroupUserMode())
                ) {
                    fourExist = true;
                }
            }

            if (rentTime.equals(CarRentTypeEnum.EIGHT_HOURS.getCode())) {
                //包车时长是否匹配（日租、半日租）
                if (rentTime.equals(costConfigDetailInfoVo.getRentType()) &&
                        //车队
                        (carGroupInfo_car.getCarGroupId().equals(costConfigDetailInfoVo.getCarGroupId())) &&
                        //城市
                        (orderAddressInfo.getCityPostalCode().equals(costConfigDetailInfoVo.getCityCode())) &&
                        //车队服务模式
                        carGroupServiceMode.equals(costConfigDetailInfoVo.getCarGroupUserMode())
                ) {
                    eightExist = true;
                }
            }
            //多日租 联合判断
            if (rentTime.equals(CarRentTypeEnum.MORE_HOURS.getCode())) {
                //包车时长是否匹配（日租、半日租）
                if (CarRentTypeEnum.FOUR_HOURS.getCode().equals(costConfigDetailInfoVo.getRentType()) &&
                        //车队
                        (carGroupInfo_car.getCarGroupId().equals(costConfigDetailInfoVo.getCarGroupId())) &&
                        //城市
                        (orderAddressInfo.getCityPostalCode().equals(costConfigDetailInfoVo.getCityCode())) &&
                        //车队服务模式
                        carGroupServiceMode.equals(costConfigDetailInfoVo.getCarGroupUserMode())
                ) {
                    fourExist = true;
                }

                //包车时长是否匹配（日租、半日租）
                if (CarRentTypeEnum.EIGHT_HOURS.getCode().equals(costConfigDetailInfoVo.getRentType()) &&
                        //车队
                        (carGroupInfo_car.getCarGroupId().equals(costConfigDetailInfoVo.getCarGroupId())) &&
                        //城市
                        (orderAddressInfo.getCityPostalCode().equals(costConfigDetailInfoVo.getCityCode())) &&
                        //车队服务模式
                        carGroupServiceMode.equals(costConfigDetailInfoVo.getCarGroupUserMode())
                ) {
                    eightExist = true;
                }
            }
        }

        if (rentTime.equals(CarRentTypeEnum.FOUR_HOURS.getCode())) {
            if (!fourExist) {
                return ApiResponse.error("未找到匹配的价格计划。");
            }

        }
        if (rentTime.equals(CarRentTypeEnum.EIGHT_HOURS.getCode())) {
            if (!eightExist) {
                return ApiResponse.error("未找到匹配的价格计划。");
            }
        }
        if (rentTime.equals(CarRentTypeEnum.MORE_HOURS.getCode())) {
            if (!(fourExist || eightExist)) {
                return ApiResponse.error("未找到匹配的价格计划。");
            }
        }

        return ApiResponse.success();
    }

    /**
     * 检查 司机所属车队的价格计划信息
     *
     * @return ApiResponse ApiResponse
     */
    public ApiResponse checkDriverOwnCarGroupPricePlanInfo(DispatchLockDriverDto dispatchLockDriverDto) {
        //包车时长  半日 整日  多日
        String rentTime = dispatchLockDriverDto.getRentTime();
        if (Double.parseDouble(rentTime) > 1.0) {
            if (((int) (Double.parseDouble(rentTime) * 10) / 10) != 0) {
                rentTime = "T009";
            } else {
                rentTime = "T002";
            }
        } else {
            rentTime = "T001";
        }

        //车队服务模式
        String carGroupServiceMode = dispatchLockDriverDto.getCarGroupServiceMode();
        //内外车队
        String carGroupSource = dispatchLockDriverDto.getCarGroupSource();
        //是否自驾
        String itIsSelfDriver = dispatchLockDriverDto.getItIsSelfDriver();

        //查询订单出发城市
        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(Long.parseLong(dispatchLockDriverDto.getOrderNo()));
        orderAddressInfo.setType("A000");
        List<OrderAddressInfo> orderAddressInfoList = orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfo);
        orderAddressInfo = orderAddressInfoList.get(0);

        //查询调度员归属车队
        LoginUser loginUser = new LoginUser();
        if (StringUtils.isNotEmpty(dispatchLockDriverDto.getDispatcherId())) {
            loginUser = tokenService.getLoginUser(dispatchLockDriverDto.getDispatcherId());
        }

        //查询司机归属车队
        CarGroupInfo carGroupInfo_driver = null;

        if (StringUtils.isNotEmpty(dispatchLockDriverDto.getDriverId())) {
            CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
            carGroupDriverRelation.setDriverId(Long.parseLong(dispatchLockDriverDto.getDriverId()));
            //查找司机所在车队
            List<CarGroupDriverRelation> carGroupDriverRelationList = carGroupDriverRelationMapper.selectCarGroupDriverRelationList(carGroupDriverRelation);
            if (carGroupDriverRelationList.isEmpty()) {
                return ApiResponse.error("未找到司机所在的车队");
            }
            if (carGroupDriverRelationList.size() > 1) {
                return ApiResponse.error("司机归属于多个车队，请确保司机属于一个车队");
            }
            carGroupInfo_driver = carGroupInfoMapper.selectCarGroupInfoById(carGroupDriverRelationList.get(0).getCarGroupId());
            if (carGroupInfo_driver == null) {
                return ApiResponse.error("司机归属车队未找到");
            }
        }

        //查询用车人所在公司编号
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(Long.parseLong(dispatchLockDriverDto.getOrderNo()));

        //查询车队（调度员、司机、）归价格计划视图对象
        CarGroupPricePlanInfoBo carGroupPricePlanInfoBo = new CarGroupPricePlanInfoBo();
        carGroupPricePlanInfoBo.setCarGroupId(carGroupInfo_driver.getCarGroupId());
        carGroupPricePlanInfoBo.setCityCode(orderAddressInfo.getCityPostalCode());
        carGroupPricePlanInfoBo.setCompanyId(orderInfo.getCompanyId());

        List<CostConfigDetailInfoVo> costConfigDetailInfoVoList = costConfigInfoMapper.selectCostConfigDetailInfo(carGroupPricePlanInfoBo);
        Iterator<CostConfigDetailInfoVo> costConfigDetailInfoVoIterator = costConfigDetailInfoVoList.iterator();

        //价格计划是否存在
        Boolean fourExist = false;
        Boolean eightExist = false;

        while (costConfigDetailInfoVoIterator.hasNext()) {
            CostConfigDetailInfoVo costConfigDetailInfoVo = costConfigDetailInfoVoIterator.next();

            if (rentTime.equals(CarRentTypeEnum.FOUR_HOURS.getCode())) {
                //包车时长是否匹配（日租、半日租）
                if (rentTime.equals(costConfigDetailInfoVo.getRentType()) &&
                        //车队
                        (carGroupInfo_driver.getCarGroupId().equals(costConfigDetailInfoVo.getCarGroupId())) &&
                        //城市
                        (orderAddressInfo.getCityPostalCode().equals(costConfigDetailInfoVo.getCityCode())) &&
                        //车队服务模式
                        carGroupServiceMode.equals(costConfigDetailInfoVo.getCarGroupUserMode())
                ) {
                    fourExist = true;
                }
            }

            if (rentTime.equals(CarRentTypeEnum.EIGHT_HOURS.getCode())) {
                //包车时长是否匹配（日租、半日租）
                if (rentTime.equals(costConfigDetailInfoVo.getRentType()) &&
                        //车队
                        (carGroupInfo_driver.getCarGroupId().equals(costConfigDetailInfoVo.getCarGroupId())) &&
                        //城市
                        (orderAddressInfo.getCityPostalCode().equals(costConfigDetailInfoVo.getCityCode())) &&
                        //车队服务模式
                        carGroupServiceMode.equals(costConfigDetailInfoVo.getCarGroupUserMode())
                ) {
                    eightExist = true;
                }
            }
            //多日租 联合判断
            if (rentTime.equals(CarRentTypeEnum.MORE_HOURS.getCode())) {
                //包车时长是否匹配（日租、半日租）
                if (CarRentTypeEnum.FOUR_HOURS.getCode().equals(costConfigDetailInfoVo.getRentType()) &&
                        //车队
                        (carGroupInfo_driver.getCarGroupId().equals(costConfigDetailInfoVo.getCarGroupId())) &&
                        //城市
                        (orderAddressInfo.getCityPostalCode().equals(costConfigDetailInfoVo.getCityCode())) &&
                        //车队服务模式
                        carGroupServiceMode.equals(costConfigDetailInfoVo.getCarGroupUserMode())
                ) {
                    fourExist = true;
                }

                //包车时长是否匹配（日租、半日租）
                if (CarRentTypeEnum.EIGHT_HOURS.getCode().equals(costConfigDetailInfoVo.getRentType()) &&
                        //车队
                        (carGroupInfo_driver.getCarGroupId().equals(costConfigDetailInfoVo.getCarGroupId())) &&
                        //城市
                        (orderAddressInfo.getCityPostalCode().equals(costConfigDetailInfoVo.getCityCode())) &&
                        //车队服务模式
                        carGroupServiceMode.equals(costConfigDetailInfoVo.getCarGroupUserMode())
                ) {
                    eightExist = true;
                }
            }
        }

        if (rentTime.equals(CarRentTypeEnum.FOUR_HOURS.getCode())) {
            if (!fourExist) {
                return ApiResponse.error("未找到匹配的价格计划。");
            }

        }
        if (rentTime.equals(CarRentTypeEnum.EIGHT_HOURS.getCode())) {
            if (!eightExist) {
                return ApiResponse.error("未找到匹配的价格计划。");
            }
        }
        if (rentTime.equals(CarRentTypeEnum.MORE_HOURS.getCode())) {
            if (!(fourExist || eightExist)) {
                return ApiResponse.error("未找到匹配的价格计划。");
            }
        }

        return ApiResponse.success();
    }

}
