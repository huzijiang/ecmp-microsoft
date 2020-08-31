package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.hq.api.system.domain.SysDriver;
import com.hq.api.system.domain.SysRole;
import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.exception.BaseException;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.OkHttpUtil;
import com.hq.common.utils.StringUtils;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.CarModeEnum;
import com.hq.ecmp.constant.CarPowerEnum;
import com.hq.ecmp.constant.CharterTypeEnum;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.ConfigTypeEnum;
import com.hq.ecmp.constant.HintEnum;
import com.hq.ecmp.constant.ItIsSupplementEnum;
import com.hq.ecmp.constant.JourneyConstant;
import com.hq.ecmp.constant.OrderConstant;
import com.hq.ecmp.constant.OrderPayConstant;
import com.hq.ecmp.constant.OrderServiceType;
import com.hq.ecmp.constant.OrderState;
import com.hq.ecmp.constant.OrderStateTrace;
import com.hq.ecmp.constant.ResignOrderTraceState;
import com.hq.ecmp.constant.ServiceTypeConstant;
import com.hq.ecmp.constant.enumerate.DispatcherFrontState;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.ApplyUseWithTravelDto;
import com.hq.ecmp.mscore.dto.DirectionDto;
import com.hq.ecmp.mscore.dto.DriverCloudDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.dto.EcmpUserInfoDto;
import com.hq.ecmp.mscore.dto.MoneyListDto;
import com.hq.ecmp.mscore.dto.OrderDetailBackDto;
import com.hq.ecmp.mscore.dto.OrderHistoryTraceDto;
import com.hq.ecmp.mscore.dto.OrderInfoFSDto;
import com.hq.ecmp.mscore.dto.OrderListBackDto;
import com.hq.ecmp.mscore.dto.OrderTraceDto;
import com.hq.ecmp.mscore.dto.PathDto;
import com.hq.ecmp.mscore.dto.PayeeInfoDto;
import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchLockCarDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchLockDriverDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IDispatchService;
import com.hq.ecmp.mscore.service.IEcmpConfigService;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IJourneyUserCarPowerService;
import com.hq.ecmp.mscore.service.IOrderAddressInfoService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import com.hq.ecmp.mscore.service.IOrderSettlingInfoService;
import com.hq.ecmp.mscore.service.IOrderStateTraceInfoService;
import com.hq.ecmp.mscore.service.IRegimeInfoService;
import com.hq.ecmp.mscore.service.IsmsBusiness;
import com.hq.ecmp.mscore.service.ThirdService;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.CommonUtils;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.MacTools;
import com.hq.ecmp.util.OrderUtils;
import com.hq.ecmp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.hq.ecmp.constant.CommonConstant.ONE;
import static com.hq.ecmp.constant.CommonConstant.ZERO;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class OrderInfoServiceImpl implements IOrderInfoService {
    private static final String NO_DRIVER="无";
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private OrderSettlingInfoMapper orderSettlingInfoMapper;
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Resource
    private JourneyNodeInfoMapper journeyNodeInfoMapper;
    @Resource
    private JourneyInfoMapper journeyInfoMapper;
    @Resource
    private JourneyUserCarPowerMapper journeyUserCarPowerMapper;
    @Resource
    private IOrderStateTraceInfoService iOrderStateTraceInfoService;
    @Resource
    private ApplyInfoMapper applyInfoMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserEmergencyContactInfoMapper userEmergencyContactInfoMapper;
    @Resource
    private OrderViaInfoMapper orderViaInfoMapper;
    @Autowired
    private IRegimeInfoService regimeInfoService;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;
    @Resource
    private CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;
    @Resource
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private JourneyPassengerInfoMapper journeyPassengerInfoMapper;
    @Resource
    private IOrderAddressInfoService iOrderAddressInfoService;
    @Resource
    private JourneyPlanPriceInfoMapper journeyPlanPriceInfoMapper;
    @Resource
    private DriverHeartbeatInfoMapper driverHeartbeatInfoMapper;
    @Resource
    private OrderAddressInfoMapper orderAddressInfoMapper;
    @Resource
    private IJourneyUserCarPowerService iJourneyUserCarPowerService;
    @Resource
    ThirdService thirdService;
    @Resource
    private IEcmpConfigService ecmpConfigService;
    @Resource
    private DriverServiceAppraiseeInfoMapper driverServiceAppraiseeInfoMapper;
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Resource
    private IsmsBusiness ismsBusiness;
    @Resource
    private EnterpriseCarTypeInfoMapper enterpriseCarTypeInfoMapper;
    @Autowired
    private ChinaCityMapper chinaCityMapper;
    @Autowired
    private IDispatchService dispatchService;
    @Autowired
    private IEcmpOrgService ecmpOrgService;
    @Resource
    private SceneInfoMapper sceneInfoMapper;
    @Resource
    private OrderPayInfoMapper orderPayInfoMapper;
    @Resource
    private CarGroupInfoMapper carGroupInfoMapper;
    @Resource
    private ApplyUseCarTypeMapper applyUseCarTypeMapper;
    @Resource
    private JourneyAddressInfoMapper journeyAddressInfoMapper;
    @Resource
    private OrderServiceCostDetailRecordInfoMapper orderServiceCostDetailRecordInfoMapper;
    @Resource
    private OrderServiceImagesInfoMapper orderServiceImagesInfoMapper;
    @Resource
    private OrderAccountInfoMapper orderAccountInfoMapper;
    @Resource
    private OrderDispatcheDetailInfoMapper orderDispatcheDetailInfoMapper;
    @Resource
    private IOrderSettlingInfoService orderSettlingInfoService;
    @Resource
    private CarGroupDriverRelationMapper carGroupDriverRelationMapper;


    @Value("${thirdService.enterpriseId}") //企业编号
    private String enterpriseId;
    @Value("${thirdService.licenseContent}") //企业证书信息
    private String licenseContent;
    @Value("${thirdService.apiUrl}")//三方平台的接口前地址
    private String apiUrl;
    @Value("${dispatch.waitIntervalMinutes}")
    private Long waitIntervalMinutes;

    /**
     * 查询【请填写功能名称】
     *
     * @param orderId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderInfo selectOrderInfoById(Long orderId) {
        return orderInfoMapper.selectOrderInfoById(orderId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderInfo> selectOrderInfoList(OrderInfo orderInfo) {
        return orderInfoMapper.selectOrderInfoList(orderInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderInfo(OrderInfo orderInfo) {
        orderInfo.setCreateTime(DateUtils.getNowDate());
        return orderInfoMapper.insertOrderInfo(orderInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderInfo(OrderInfo orderInfo) {
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        return orderInfoMapper.updateOrderInfo(orderInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param orderIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderInfoByIds(Long[] orderIds) {
        return orderInfoMapper.deleteOrderInfoByIds(orderIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param orderId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderInfoById(Long orderId) {
        return orderInfoMapper.deleteOrderInfoById(orderId);
    }


    /**
     * 获取乘客订单列表
     *
     * @param user
     * @return
     */
    @Override
    public PageResult<OrderListInfo> getOrderList(SysUser user, int pageNum, int pageSize, int isConfirmState) {
        PageHelper.startPage(pageNum, pageSize);
        boolean flag = checkItIsDispatcher(user);
        List<OrderListInfo> orderList;
        if(flag){
            orderList = orderInfoMapper.getOrderOutList(user.getDeptId(), isConfirmState);
        }else {
            orderList = orderInfoMapper.getOrderList(user.getDeptId(), isConfirmState);
        }
        PageInfo<OrderListInfo> pageInfo = new PageInfo<>(orderList);
        PageResult<OrderListInfo> pageResult = new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), orderList);
        return pageResult;
    }

    /**
     * 如果外部调度员，当前人所在车队，外部车队，新逻辑，内部车队老逻辑
     * @param user
     */
    private boolean checkItIsDispatcher(SysUser user) {
        if(user.getItIsDispatcher().equals(1)) {
            log.warn("当前用户user={}不是调度员", user.getUserName());
            return false;
        }
        CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
        carGroupDispatcherInfo.setUserId(user.getUserId());
        List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
        if(carGroupDispatcherInfos == null || carGroupDispatcherInfos.size() == 0) {
            log.warn("通过用户id查询调度信息为空");
            return false;
        }
        CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupDispatcherInfos.get(0).getCarGroupId());
        if(carGroupInfo == null) {
            log.warn("车队信息为空carGroupId={}", carGroupDispatcherInfos.get(0).getCarGroupId());
            return false;
        }
        if(carGroupInfo.getItIsInner().equals("C111")){
            return true;
        }
        return false;
    }




    /**
     * 订单状态修改方法getOrderList
     *
     * @return
     */
    @Override
    @Transactional
    public int insertOrderStateTrace(String orderId, String updateState, String userId, String cancelReason, Long oldDriverId, Long oldCarId) {
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(Long.parseLong(orderId));
        orderStateTraceInfo.setState(updateState);
        orderStateTraceInfo.setContent(cancelReason);
        orderStateTraceInfo.setCreateBy(userId);
        orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
        int i = iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        return i;
    }


    @Override
    public List<DispatchOrderInfo> queryAllWaitDispatchList(Long userId, Boolean isAutoDis, Long companyId) {
        List<DispatchOrderInfo> result = new ArrayList<DispatchOrderInfo>();
        //查询所有处于待派单(未改派)的订单及关联的信息
        OrderInfo query = new OrderInfo();
        query.setState(OrderState.WAITINGLIST.getState());
        if (isAutoDis) {
            query.setCompanyId(companyId);
        }
        List<DispatchOrderInfo> waitDispatchOrder = orderInfoMapper.queryOrderRelateInfo(query);
        if (null != waitDispatchOrder && waitDispatchOrder.size() > 0) {
            //对用的前端状态都为待派车 -S200
            for (DispatchOrderInfo dispatchOrderInfo : waitDispatchOrder) {
                //去除改派的单子
                if (iOrderStateTraceInfoService.isReassignment(dispatchOrderInfo.getOrderId())) {
                    continue;
                }
                //去除超时的订单
                if (iOrderAddressInfoService.checkOrderOverTime(dispatchOrderInfo.getOrderId())) {
                    continue;
                }
                //正常申请的调度单取对应的用车    申请单的通过时间来排序
                dispatchOrderInfo.setUpdateDate(dispatchOrderInfo.getApplyPassDate());
                dispatchOrderInfo.setState(OrderState.WAITINGLIST.getState());
                result.add(dispatchOrderInfo);
            }
        }
        //查询所有处于待改派(订单状态为已派车,已发起改派申请)的订单及关联的信息

        query.setState(OrderState.ALREADYSENDING.getState());
        query.setOrderTraceState(OrderStateTrace.APPLYREASSIGNMENT.getState());
        if (isAutoDis) {
            query.setCompanyId(companyId);
        }
        List<DispatchOrderInfo> reassignmentOrder = orderInfoMapper.queryOrderRelateInfo(query);
        if (null != reassignmentOrder && reassignmentOrder.size() > 0) {
            for (DispatchOrderInfo dispatchOrderInfo : reassignmentOrder) {
                dispatchOrderInfo.setState(OrderState.APPLYREASSIGN.getState());
                //去除超时的订单
                if (iOrderAddressInfoService.checkOrderOverTime(dispatchOrderInfo.getOrderId())) {
                    continue;
                }
                //去除最新订单流转状态不是S270 申请改派的
                OrderStateTraceInfo orderStateTraceInfo = orderStateTraceInfoMapper.getLatestInfoByOrderId(dispatchOrderInfo.getOrderId());
                if (!OrderStateTrace.APPLYREASSIGNMENT.getState().equals(orderStateTraceInfo.getState())) {
                    continue;
                }
                if (!isAutoDis) {
                    Long oldDispatcher = orderStateTraceInfoMapper.getOldDispatcher(dispatchOrderInfo.getOrderId());
                    if (oldDispatcher != null) {
                        if (oldDispatcher != 1) {
                            if (userId.longValue() != oldDispatcher.longValue()) {
                                continue;
                            }
                        }
                    }
                }
                result.add(dispatchOrderInfo);
            }

        }
        return result;
    }


    /**
     * 乘客端待处理调度列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<DispatchOrderInfo> queryWaitDispatchList(Long userId) {
        List<DispatchOrderInfo> result = queryAllWaitDispatchList(userId, false, null);
		/*对用户进行订单可见校验
		自有车+网约车时，且上车地点在车队的用车城市范围内，只有该车队的调度员能看到该订单
	      只有自有车时，且上车地点不在车队的用车城市范围内，则所有车车队的所有调度员都能看到该订单*/
        List<DispatchOrderInfo> checkResult = new ArrayList<DispatchOrderInfo>();
        if (result.size() > 0) {
            for (DispatchOrderInfo dispatchOrderInfo : result) {
                //计算该订单的等待时长 分钟
                if (null != dispatchOrderInfo.getCreateTime()) {
                    dispatchOrderInfo.setWaitMinute(DateFormatUtils.getDateToWaitInterval(dispatchOrderInfo.getCreateTime()));
                }
                //查询订单对应的上车地点时间,下车地点时间
                buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
                //查询订单对应制度的可用用车方式
                Long regimenId = dispatchOrderInfo.getRegimenId();
                if (null == regimenId || !regimeInfoService.findOwnCar(regimenId)) {
                    log.error("该订单相关的制度id是空，或者相关制度不包含自有车");
                    continue;
                }
                //查询订单对应的上车地点时间,下车地点时间
                buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
                String cityId = dispatchOrderInfo.getCityId();
                if (StringUtil.isEmpty(cityId)) {
                    log.error("该订单相关城市为空");
                    continue;
                }
                //判断订单是否可以被当前调度员看到
                if (!judgeOrderValid(userId, dispatchOrderInfo)) {
                    continue;
                }
                //计算该订单的等待时长 分钟
                if (null != dispatchOrderInfo.getCreateTime()) {
                    dispatchOrderInfo.setWaitMinute(DateFormatUtils.getDateToWaitInterval(dispatchOrderInfo.getCreateTime()));
                }

                //订单添加用车场景用车制度以及任务来源信息
                DispatchOrderInfoPacking(dispatchOrderInfo);
                //添加同行人
                addPeerNum(dispatchOrderInfo);
                checkResult.add(dispatchOrderInfo);
            }
        }
        return checkResult;
    }

    /**
     * 调度员待调度列表以及调度完成列表规则：
     * 1. 总部在改城市没有能服务当前订单申请人所在部门的车队，分公司也没有能服务于当前订单申请人所在公司的车队
     * 1.自有车：申请单无法提交
     * 2.自有车+网约车：直接走网约车，不走调度。
     * 2.总部在改城市有能服务当前订单申请人所在部门的车队，分公司有能服务于当前订单申请人所在公司的车队
     * 1.自有车：十分钟前总部在该城市的能服务于当前订单申请人所在部门的所有车队的所有调度员可以看到，十分钟后,分公司在该城市的能服务于当前订单申请人所在公司的车队的所有调度员也可以看到
     * 2.自有车+网约车: 十分钟前总部在该城市的能服务于当前订单申请人所在部门的所有车队的所有调度员可以看到，十分钟后,分公司在该城市的能服务于当前订单申请人所在公司的车队的所有调度员也可以看到
     * 3.总部在改城市有能服务当前订单申请人所在部门的车队，分公司没有能服务于当前订单申请人所在公司的车队
     * 1.自有车：总部在该城市的能服务于当前订单申请人所在部门的所有车队的所有调度员可以看到
     * 2.自有车+网约车：总部在该城市的能服务于当前订单申请人所在部门的所有车队的所有调度员可以看到
     * 4.总部在改城市没有能服务当前订单申请人所在部门的车队，分公司有能服务于当前订单申请人所在公司的车队
     * （1.自有车：总部和分公司同时看到
     * 2.自有车+网约车：总部看不到此调度单，分公司在该城市的能服务于当前订单申请人所在公司的车队的所有调度员也可以看到，二者同时能看到）
     * --------》第4点修改为只有分公司先看到不管是自有车还是自有车+网约车，10分钟后总部的所有城市的所有调度员可以看到（括号里的规则弃用）
     *
     * @param userId 当前登录人id
     * @return
     */
    private boolean judgeOrderValid(Long userId, DispatchOrderInfo dispatchOrderInfo) {

        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(dispatchOrderInfo.getOrderId());
        if (dispatchOrderInfo.getOrderId() == 1553L) {
            System.out.println("1553");
        }
        if (orderInfo == null) {
            log.error("订单{}信息为空", dispatchOrderInfo.getOrderId());
            return false;
        }
//        if (StringUtils.isBlank(orderInfo.getUseCarMode())){
//           return false;
//        }
        JourneyUserCarPower journeyUserCarPower = journeyUserCarPowerMapper.selectJourneyUserCarPowerById(orderInfo.getPowerId());
        if (journeyUserCarPower == null) {
            log.error("订单{}相关用车权限信息为空", dispatchOrderInfo.getOrderId());
            return false;
        }
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(journeyUserCarPower.getApplyId());
        if (applyInfo == null) {
            log.error("订单{}相关申请表信息为空", dispatchOrderInfo.getOrderId());
            return false;
        }
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(Long.parseLong(applyInfo.getCreateBy()));
        if (ecmpUser == null) {
            log.error("订单{}相关申请人信息为空", dispatchOrderInfo.getOrderId());
            return false;
        }
        RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(dispatchOrderInfo.getRegimenId());
        if (regimeInfo == null) {
            log.error("订单{}相关制度信息为空", dispatchOrderInfo.getOrderId());
            return false;
        }

        String canUseCarMode = regimeInfo.getCanUseCarMode();
        if (StringUtil.isEmpty(canUseCarMode)) {
            log.error("订单{}相关制度表用车方式为空", dispatchOrderInfo.getOrderId());
            return false;
        }
        List<String> list = Arrays.asList(canUseCarMode.split(","));
        if (!list.contains(CarConstant.USR_CARD_MODE_HAVE)) {
            return false;
        }
        String cityId = dispatchOrderInfo.getCityId();
        if (StringUtil.isEmpty(cityId)) {
            log.error("订单{}相关城市信息为空", dispatchOrderInfo.getOrderId());
            return false;
        }
        //是否有内部可用车队标志
        boolean innerFlag = false;
        //是否有外部可用车队标志
        boolean outerFlag = false;
        List<Long> carGroupInnerDispatchers = null;
        List<Long> carGroupOuterDispatchers = null;
        List<Long> innerCompanyCarGroupIds = carGroupInfoMapper.queryCarGroupIdInnerCompany(ecmpUser.getDeptId(), dispatchOrderInfo.getCityId(), ecmpUser.getOwnerCompany());
        if (!CollectionUtils.isEmpty(innerCompanyCarGroupIds)) {
            innerFlag = true;
            //内部公司的调度员
            carGroupInnerDispatchers = carGroupDispatcherInfoMapper.queryUserByCarGroup(innerCompanyCarGroupIds);
        }
        List<Long> outerCompanyCarGroupIds = carGroupInfoMapper.queryCarGroupIdOuterCompany(dispatchOrderInfo.getCityId(), ecmpUser.getOwnerCompany());
        if (!CollectionUtils.isEmpty(outerCompanyCarGroupIds)) {
            outerFlag = true;
            //外部公司的调度员
            carGroupOuterDispatchers = carGroupDispatcherInfoMapper.queryUserByCarGroup(outerCompanyCarGroupIds);
        }
        if (!innerFlag && !outerFlag) {
            return false;
        }


        if (innerFlag && outerFlag) {
            if (null != carGroupInnerDispatchers && carGroupInnerDispatchers.contains(userId)) {
                return true;
            }
            if (null != carGroupOuterDispatchers && carGroupOuterDispatchers.contains(userId)) {
                Long waitMinutes = DateFormatUtils.getDateToWaitInterval(dispatchOrderInfo.getCreateTime());
                return (waitMinutes > waitIntervalMinutes) ? true : false;
            }
        }
        if (innerFlag && !outerFlag) {
            if (null != carGroupInnerDispatchers && carGroupInnerDispatchers.contains(userId)) {
                return true;
            }
        }
        if (!innerFlag && outerFlag) {
            if (null != carGroupOuterDispatchers && carGroupOuterDispatchers.contains(userId)) {
                return true;
            }
            //查询公司的所有调度员
            List<Long> carGroupInnerAllDispatchers = carGroupInfoMapper.queryAllDispatchersByCompanyId(ecmpUser.getOwnerCompany());
            if (null != carGroupInnerAllDispatchers && carGroupInnerAllDispatchers.contains(userId)) {
                Long waitMinutes = DateFormatUtils.getDateToWaitInterval(dispatchOrderInfo.getCreateTime());
                return (waitMinutes > waitIntervalMinutes) ? true : false;
            }
        }
        return false;
    }

    //// 查询订单对应的上车地点时间,下车地点时间  A000-上车     A999-下车
    private void buildOrderStartAndEndSiteAndTime(DispatchOrderInfo dispatchOrderInfo) {
        OrderAddressInfo startOrderAddressInfo = iOrderAddressInfoService
                .queryOrderStartAndEndInfo(new OrderAddressInfo("A000", dispatchOrderInfo.getOrderId()));
        if (null != startOrderAddressInfo) {
            dispatchOrderInfo.setStartSite(startOrderAddressInfo.getAddress());
            dispatchOrderInfo.setUseCarDate(startOrderAddressInfo.getActionTime());
            dispatchOrderInfo.setCityId(startOrderAddressInfo.getCityPostalCode());
            CityInfo cityInfo = chinaCityMapper.queryCityByCityCode(startOrderAddressInfo.getCityPostalCode());
            if (cityInfo != null) {
                dispatchOrderInfo.setUseCarCity(cityInfo.getCityName());
            }
        }
        OrderAddressInfo endOrderAddressInfo = iOrderAddressInfoService
                .getOrderEndAddresses(new OrderAddressInfo("A999", dispatchOrderInfo.getOrderId(), dispatchOrderInfo.getJourneyId()));

        if (null != endOrderAddressInfo) {
            if (StringUtils.isNotBlank(endOrderAddressInfo.getAddress()) && StringUtils.isNotBlank(endOrderAddressInfo.getAddressInfo())) {
                String endAddress = endOrderAddressInfo.getAddress() + "," + endOrderAddressInfo.getAddressInfo();
                dispatchOrderInfo.setEndSite(endAddress);
            } else {
                dispatchOrderInfo.setEndSite(endOrderAddressInfo.getAddress());
            }
            dispatchOrderInfo.setEndDate(endOrderAddressInfo.getActionTime());
        } else {//如果是差旅则 从形成节点表去拿预计的结束时间
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(dispatchOrderInfo.getOrderId());
            if (orderInfo.getServiceType().equals(ServiceTypeConstant.CHARTERED)) {
                JourneyNodeInfo journeyNodeInfo = journeyNodeInfoMapper.selectJourneyNodeInfoById(orderInfo.getNodeId());
                if (journeyNodeInfo != null) {
                    dispatchOrderInfo.setEndDate(journeyNodeInfo.getPlanArriveTime());
                }
            }
        }

    }

    @Override
    public List<DispatchOrderInfo> queryCompleteDispatchOrder(Long userId) {
        List<DispatchOrderInfo> result = new ArrayList<DispatchOrderInfo>();
        //获取系统里已经完成调度的订单
        List<DispatchOrderInfo> list = orderInfoMapper.queryCompleteDispatchOrder();
        if (null != list && list.size() > 0) {
            for (DispatchOrderInfo dispatchOrderInfo : list) {
                if (StringUtils.isBlank(dispatchOrderInfo.getUseCarMode())) {
                    log.error("已调度的订单{}，订单表没有用车方式", dispatchOrderInfo.getOrderId());
                    continue;
                }
                //过滤掉未走调度自动约车的
                boolean judgeNotDispatch = regimeInfoService.judgeNotDispatch(dispatchOrderInfo.getApplyId(), dispatchOrderInfo.getUseCarCityCode());
                if (judgeNotDispatch) {
                    continue;
                }
                //查询订单对应的上车地点时间,下车地点时间
                buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
                //过滤掉看不到的订单
                if (!judgeOrderValid(userId, dispatchOrderInfo)) {
                    continue;
                }

                //添加任务来源信息，用车场景名称和用车制度名称
                DispatchOrderInfoPacking(dispatchOrderInfo);
                //添加已处理调度单状态和对应的所需字段
                buildStateAndOtherInfoRelateState(dispatchOrderInfo);
                result.add(dispatchOrderInfo);

            }
        }
        return result;
    }

    /**
     * 补充已完成调度列表的前端状态，并根据前端状态补充额外和状态相关的信息
     * 状态逻辑判断逻辑：
     * 1.如果是 S270 或者S199且不包含已过期 则为 已派车 在去更加订单的状态区分自由车和网约车
     * 2.如果是S270 或者S199且包含已过期 则为 改派过期
     * 3.如果为S277 则为改派驳回
     * 4.如果是 S279则为已改派 再去区分自由车和网约车
     * 如果没有改派状态
     * 1.如果订单状态包含订单驳回则为派车申请驳回
     * 2.如果不包含订单驳回和订单超时，则为已派车，再去区分自由车和网约车
     * 3.如果包含已过期且不包含已派单 则为派车申请过期
     *
     * @param dispatchOrderInfo
     */
    private void buildStateAndOtherInfoRelateState(DispatchOrderInfo dispatchOrderInfo) {
        Long orderId = dispatchOrderInfo.getOrderId();
        String useCarMode = dispatchOrderInfo.getUseCarMode();
        addPeerNum(dispatchOrderInfo);
        List<String> states = orderStateTraceInfoMapper.queryOrderAllState(orderId);
        String ressaignLatestState = orderStateTraceInfoMapper.queryOrderLatestRessaignState(orderId);
        if (!StringUtils.isBlank(ressaignLatestState)) {
            //已派车
            boolean flagSendCar = (ressaignLatestState.equals(OrderStateTrace.APPLYREASSIGNMENT.getState()) || ressaignLatestState.equals(OrderStateTrace.APPLYINGPASS.getState()))
                    && !states.contains(OrderStateTrace.ORDEROVERTIME.getState());
            //改派已过期
            boolean flagOverTime = (ressaignLatestState.equals(OrderStateTrace.APPLYREASSIGNMENT.getState()) || ressaignLatestState.equals(OrderStateTrace.APPLYINGPASS.getState()))
                    && states.contains(OrderStateTrace.ORDEROVERTIME.getState());
            if (flagSendCar) {
                OrderStateTraceInfo orderStateTraceInfoSendCar = orderStateTraceInfoMapper.queryLatestInfoByOrderIdAndState(orderId, OrderStateTrace.SENDCAR.getState());
                dispatchOrderInfo.setOpDate(orderStateTraceInfoSendCar.getCreateTime());
                if (useCarMode.equals(CarConstant.USR_CARD_MODE_HAVE)) {
                    dispatchOrderInfo.setState(DispatcherFrontState.SENDCARHAVE.getState());
                } else if (states.contains(useCarMode.equals(CarConstant.USR_CARD_MODE_NET))) {
                    dispatchOrderInfo.setState(DispatcherFrontState.SENDCARNET.getState());
                }
            }
            if (flagOverTime) {
                OrderStateTraceInfo orderStateTraceInfoReassApply = orderStateTraceInfoMapper.queryLatestInfoByOrderIdAndState(orderId, OrderStateTrace.APPLYREASSIGNMENT.getState());
                dispatchOrderInfo.setReassignmentApplyReason(orderStateTraceInfoReassApply.getContent());
                dispatchOrderInfo.setState(DispatcherFrontState.REASSIGNMENTEXPIRED.getState());
            }
            //改派申请已驳回
            if (ressaignLatestState.equals(OrderStateTrace.TURNREASSIGNMENT.getState())) {
                dispatchOrderInfo.setState(DispatcherFrontState.REASSIGNMENTREJECT.getState());
                OrderStateTraceInfo orderStateTraceInfoReassApply = orderStateTraceInfoMapper.queryLatestInfoByOrderIdAndState(orderId, OrderStateTrace.APPLYREASSIGNMENT.getState());
                dispatchOrderInfo.setReassignmentApplyReason(orderStateTraceInfoReassApply.getContent());
                OrderStateTraceInfo orderStateTraceInfoReassReject = orderStateTraceInfoMapper.queryLatestInfoByOrderIdAndState(orderId, OrderStateTrace.TURNREASSIGNMENT.getState());
                dispatchOrderInfo.setRejectReason(orderStateTraceInfoReassReject.getContent());
                dispatchOrderInfo.setOpDate(orderStateTraceInfoReassReject.getCreateTime());
            }
            //已改派。改派的网约车派车和自有车派车，轨迹表是改派成功，订单表是已派车
            if (ressaignLatestState.equals(OrderStateTrace.PASSREASSIGNMENT.getState())) {
                OrderStateTraceInfo orderStateTraceInfoReassApply = orderStateTraceInfoMapper.queryLatestInfoByOrderIdAndState(orderId, OrderStateTrace.APPLYREASSIGNMENT.getState());
                dispatchOrderInfo.setReassignmentApplyReason(orderStateTraceInfoReassApply.getContent());
                OrderStateTraceInfo orderStateTraceInfoReassAgree = orderStateTraceInfoMapper.queryLatestInfoByOrderIdAndState(orderId, OrderStateTrace.PASSREASSIGNMENT.getState());
                dispatchOrderInfo.setOpDate(orderStateTraceInfoReassAgree.getCreateTime());
                if (useCarMode.equals(CarConstant.USR_CARD_MODE_HAVE)) {
                    dispatchOrderInfo.setState(DispatcherFrontState.REASSIGNMENTHAVE.getState());
                    //改派前的司机和车辆信息
                    if (orderStateTraceInfoReassAgree.getOldCarId() != null) {
                        CarInfo carInfo = carInfoMapper.selectCarInfoById(orderStateTraceInfoReassAgree.getOldCarId());
                        if (carInfo != null) {
                            dispatchOrderInfo.setOldCarType(carInfo.getCarType());
                            dispatchOrderInfo.setOldCarLicense(carInfo.getCarLicense());
                        }
                    }
                    if (orderStateTraceInfoReassAgree.getOldDriverId() != null) {
                        DriverInfo driverInfo = driverInfoMapper.selectDriverInfoById(orderStateTraceInfoReassAgree.getOldDriverId());
                        if (driverInfo != null) {
                            dispatchOrderInfo.setOldDriverName(driverInfo.getDriverName());
                        }
                    }
                } else if (states.contains(useCarMode.equals(CarConstant.USR_CARD_MODE_NET))) {
                    dispatchOrderInfo.setState(DispatcherFrontState.REASSIGNMENTNET.getState());
                }
            }
        } else {
            //派车申请已驳回
            if (states.contains(OrderStateTrace.ORDERDENIED.getState())) {
                dispatchOrderInfo.setState(DispatcherFrontState.SENDCARREJECT.getState());
                OrderStateTraceInfo orderStateTraceInfoApplyReject = orderStateTraceInfoMapper.queryLatestInfoByOrderIdAndState(orderId, OrderStateTrace.ORDERDENIED.getState());
                dispatchOrderInfo.setRejectReason(orderStateTraceInfoApplyReject.getContent());
                dispatchOrderInfo.setOpDate(orderStateTraceInfoApplyReject.getCreateTime());
            }
            //已派车,非改派网约车派车，轨迹表是约车中，订单表是约车中。非改派自由车，轨迹表是已派车，订单表是已派车
            if ((!states.contains(OrderStateTrace.ORDERDENIED.getState()) && !states.contains(OrderStateTrace.ORDEROVERTIME.getState())) ||
                    (states.contains(OrderStateTrace.ORDEROVERTIME.getState()) && states.contains(OrderStateTrace.SENDCAR.getState()))) {
                if (useCarMode.equals(CarConstant.USR_CARD_MODE_HAVE)) {
                    dispatchOrderInfo.setState(DispatcherFrontState.SENDCARHAVE.getState());
                    OrderStateTraceInfo orderStateTraceInfoSendCar = orderStateTraceInfoMapper.queryLatestInfoByOrderIdAndState(orderId, OrderStateTrace.SENDCAR.getState());
                    dispatchOrderInfo.setOpDate(orderStateTraceInfoSendCar.getCreateTime());
                } else if (useCarMode.equals(CarConstant.USR_CARD_MODE_NET)) {
                    OrderStateTraceInfo orderStateTraceInfoSendCar = orderStateTraceInfoMapper.queryLatestInfoByOrderIdAndState(orderId, OrderStateTrace.SENDINGCARS.getState());
                    dispatchOrderInfo.setOpDate(orderStateTraceInfoSendCar.getCreateTime());
                    dispatchOrderInfo.setState(DispatcherFrontState.SENDCARNET.getState());
                }
            }
            //派车已过期
            if (!states.contains(OrderStateTrace.SENDCAR.getState()) && states.contains(OrderStateTrace.ORDEROVERTIME.getState())) {
                dispatchOrderInfo.setState(DispatcherFrontState.SENDCAREXPIRED.getState());
            }
        }
    }

    /**
     * 添加同行人数量
     *
     * @param dispatchOrderInfo
     */
    private void addPeerNum(DispatchOrderInfo dispatchOrderInfo) {
        //同行人数量
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(dispatchOrderInfo.getOrderId());
        if (orderInfo != null) {
            JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
            journeyPassengerInfo.setJourneyId(orderInfo.getJourneyId());
            List<JourneyPassengerInfo> journeyPassengerInfos = journeyPassengerInfoMapper.selectJourneyPassengerInfoList(journeyPassengerInfo);
            if (!CollectionUtils.isEmpty(journeyPassengerInfos)) {
                dispatchOrderInfo.setPeerNum(journeyPassengerInfos.size());
            }
        }
    }

    @Override
    public ApiResponse<DispatchOrderInfo> getWaitDispatchOrderDetailInfo(Long orderId, Long userId) {
        //查询是否存在调度员已经在进行操作
        Object o = redisUtil.get(CommonConstant.DISPATCH_LOCK_PREFIX + orderId);
        if (o == null) {
            //如果没有就放进去一个过期时间  默认10分钟
            long nm = 60 * 10;
            redisUtil.set(CommonConstant.DISPATCH_LOCK_PREFIX + orderId, userId.toString(), nm);
            ApiResponse<DispatchOrderInfo> dispatchOrderInfo = this.doWaitDispatchOrderDetailInfo(orderId);
            return dispatchOrderInfo;
        } else {
            //如果存在查看是否是该调度员 如果不是则返回已经在操作
            Long redisKey = Long.parseLong(o.toString());
            if (redisKey.equals(userId)) {
                ApiResponse<DispatchOrderInfo> dispatchOrderInfo = this.doWaitDispatchOrderDetailInfo(orderId);
                return dispatchOrderInfo;
            } else {
                // long a =redisUtil.getTime("dispatch_557");
                return ApiResponse.error("已有调度员操作");
            }
        }
    }

    public ApiResponse<DispatchOrderInfo> doWaitDispatchOrderDetailInfo(Long orderId) {
        ApiResponse apiResponse = new ApiResponse();
        DispatchOrderInfo dispatchOrderInfo = orderInfoMapper.getWaitDispatchOrderDetailInfo(orderId);
        //计算等待时长 分钟
        if (null != dispatchOrderInfo.getCreateTime()) {
            dispatchOrderInfo.setWaitMinute(DateFormatUtils.getDateToWaitInterval(dispatchOrderInfo.getCreateTime()));
        }
        dispatchOrderInfo.setState(OrderState.WAITINGLIST.getState());
        //查询订单对应的上车地点时间,下车地点时间
        buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
        //订单添加用车场景用车制度以及任务来源信息
        DispatchOrderInfoPacking(dispatchOrderInfo);
        //判断该订单是否改派过
        if (iOrderStateTraceInfoService.isReassignment(orderId)) {
            //是改派过的单子  则查询改派详情
            DispatchDriverInfo dispatchDriverInfo = iOrderStateTraceInfoService.queryReassignmentOrderInfo(orderId);
            dispatchOrderInfo.setDispatchDriverInfo(dispatchDriverInfo);
            //改派过的订单对应前端状态 待改派-S270
            dispatchOrderInfo.setState(OrderState.APPLYREASSIGN.getState());
            apiResponse.setData(dispatchOrderInfo);
        } else {
            apiResponse.setData(dispatchOrderInfo);
        }
        return apiResponse;
    }

    @Override
    public DispatchOrderInfo getCompleteDispatchOrderDetailInfo(Long orderId) {
        DispatchOrderInfo dispatchOrderInfo = orderInfoMapper.queryCompleteDispatchOrderDetail(orderId);
        //对应前端状态都为已处理-S299
        dispatchOrderInfo.setState(OrderState.ALREADYSENDING.getState());
        //查询订单对应的上车地点时间,下车地点时间
        buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
        //订单添加用车场景用车制度以及任务来源信息
        DispatchOrderInfoPacking(dispatchOrderInfo);
        if (iOrderStateTraceInfoService.isReassignment(orderId)) {
            //是改派过的单子  则查询改派详情
            DispatchDriverInfo dispatchDriverInfo = iOrderStateTraceInfoService.queryReassignmentOrderInfo(orderId);
            dispatchOrderInfo.setDispatchDriverInfo(dispatchDriverInfo);
        }
        //查询派车信息
        List<SendCarInfo> sendCarInfoList = iOrderStateTraceInfoService.queryStateInfo(orderId);
        dispatchOrderInfo.setSendCarInfoList(sendCarInfoList);
        return dispatchOrderInfo;
    }

    @Override
    public OrderVO orderBeServiceDetail(Long orderId) throws Exception {
        OrderVO vo = new OrderVO();
        OrderInfo orderInfo = this.selectOrderInfoById(orderId);
        if (null == orderInfo) {
            throw new Exception("该订单不存在");
        }
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        vo.setRegimeId(journeyInfo.getRegimenId());
        vo.setUseTime(journeyInfo.getUseTime());
        vo.setBeginTime(DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT, journeyInfo.getUseCarTime()));
        JourneyNodeInfo nodeInfo = journeyNodeInfoMapper.selectJourneyNodeInfoById(orderInfo.getNodeId());
        BeanUtils.copyProperties(orderInfo, vo);

        int count = orderStateTraceInfoMapper.selectCountForAgainStrte(orderId, OrderStateTrace.manyUseCarState());
        vo.setIsFirstState(count);
        OrderStateTraceInfo orderStateTraceInfo = orderStateTraceInfoMapper.getLatestInfoByOrderId(orderId);
        List<OrderAddressInfo> startOrderAddr = orderAddressInfoMapper.selectOrderAddressInfoList(new OrderAddressInfo(orderId, OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT));
        String useCarTime = null;
        if (!CollectionUtils.isEmpty(startOrderAddr)) {
            useCarTime = DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT, startOrderAddr.get(0).getActionTime());
            vo.setUseCarTimestamp(startOrderAddr.get(0).getActionTime().getTime());
            vo.setStartAddress(startOrderAddr.get(0).getAddress());
        }
        List<OrderAddressInfo> endOrderAddr = orderAddressInfoMapper.selectOrderAddressInfoList(new OrderAddressInfo(orderId, OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE));
        if (!CollectionUtils.isEmpty(endOrderAddr)) {
            vo.setEndAddress(endOrderAddr.get(0).getAddress());
        }
        vo.setUseCarTime(useCarTime);
        vo.setCreateTimestamp(orderInfo.getCreateTime().getTime());
        vo.setOrderNumber(orderInfo.getOrderNumber());
        vo.setCustomerServicePhone(thirdService.getCustomerPhone());
        vo.setDriverType(CarModeEnum.format(orderInfo.getUseCarMode()));
        vo.setLabelState(orderStateTraceInfo.getState());
        vo.setCancelReason(orderStateTraceInfo.getContent());
        if (OrderState.WAITINGLIST.getState().equals(orderInfo.getState()) || OrderState.GETARIDE.getState().equals(orderInfo.getState())) {
            return vo;
        }
        if (OrderState.SENDINGCARS.getState().equals(orderInfo.getState())) {
            vo.setHint(HintEnum.CALLINGCAR.join(DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN, nodeInfo.getPlanSetoutTime())));
            return vo;
        }
        if (OrderState.ORDEROVERTIME.getState().equals(orderInfo.getState())) {
            vo.setHint(HintEnum.CALLCARFAILD.join(DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN, nodeInfo.getPlanSetoutTime())));
            return vo;
        }
        if (OrderState.ORDERCANCEL.getState().equals(orderStateTraceInfo.getState()) && OrderState.ORDERCLOSE.getState().equals(orderInfo.getState())) {
            this.checkIsOverMoney(orderInfo, vo, 2);
            return vo;
        }
//        String states=OrderState.ALREADYSENDING.getState()+","+ OrderState.REASSIGNPASS.getState();
//        UserVO str= orderStateTraceInfoMapper.getOrderDispatcher(orderId,states);
//        if (str!=null){
//            vo.setDispatcherPhone(str.getUserPhone());
//            vo.setDispatcherName(str.getUserName());
//        }
        Map<String, String> map = orderDispatcheDetailInfoMapper.selectGroupInfo(orderId);
        if (!CollectionUtils.isEmpty(map)) {
            vo.setDispatcherPhone(map.get("dispatcherPhone"));
            vo.setDispatcherName(map.get("dispatcherName"));
            vo.setCarGroupPhone(map.get("carGroupPhone"));
            vo.setCarGroupName(map.get("carGroupName"));
        }
        //服务结束时间
        if (orderStateTraceInfo != null || OrderStateTrace.SERVICEOVER.getState().equals(orderStateTraceInfo.getState())) {
            vo.setOrderEndTime(DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT, orderStateTraceInfo.getCreateTime()));
        }
        List<UserEmergencyContactInfo> contactInfos = userEmergencyContactInfoMapper.queryAll(new UserEmergencyContactInfo(journeyInfo.getUserId()));
        String isAddContact = CollectionUtils.isEmpty(contactInfos) ? CommonConstant.SWITCH_ON : CommonConstant.SWITCH_OFF;
        vo.setIsAddContact(isAddContact);
        //TODO 查询企业配置是否自动行程确认/异议
        int orderConfirmStatus = ecmpConfigService.getOrderConfirmStatus(ConfigTypeEnum.ORDER_CONFIRM_INFO.getConfigKey(), orderInfo.getUseCarMode(), orderInfo.getCompanyId());
        int isVirtualPhone = ecmpConfigService.getOrderConfirmStatus(ConfigTypeEnum.VIRTUAL_PHONE_INFO.getConfigKey(), null, orderInfo.getCompanyId());//是否号码保护
        vo.setIsDisagree(orderConfirmStatus);
        vo.setIsVirtualPhone(isVirtualPhone);
        DriverServiceAppraiseeInfo driverServiceAppraiseeInfos = driverServiceAppraiseeInfoMapper.queryByOrderId(orderId);
        if (driverServiceAppraiseeInfos != null) {
            vo.setDescription(driverServiceAppraiseeInfos.getContent());
            vo.setScore(driverServiceAppraiseeInfos.getScore() + "");
        } else {
            vo.setScore(null);
            vo.setDescription(null);
        }
        if (CarModeEnum.ORDER_MODE_HAVE.getKey().equals(orderInfo.getUseCarMode())) {//自有车
            vo.setDriverId(orderInfo.getDriverId());
            vo.setCardId(orderInfo.getCarId());
            //查询车辆信息
            CarInfo carInfo = carInfoMapper.selectCarInfoById(orderInfo.getCarId());
            EnterpriseCarTypeInfo enterpriseCarTypeInfo = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(carInfo.getCarTypeId());
            if (carInfo != null) {
                BeanUtils.copyProperties(carInfo, vo);
                vo.setPowerType(CarPowerEnum.format(carInfo.getPowerType()));
                if (enterpriseCarTypeInfo != null) {
                    vo.setCarPhoto(enterpriseCarTypeInfo.getImageUrl());
                }
            }
            List<DriverServiceAppraiseeInfo> driverServiceAppraiseeInfos1 = driverServiceAppraiseeInfoMapper.queryAll(new DriverServiceAppraiseeInfo(orderInfo.getDriverId()));
            String star = "";
            if (!CollectionUtils.isEmpty(driverServiceAppraiseeInfos1)) {
                Double sumAge = driverServiceAppraiseeInfos1.stream().collect(Collectors.averagingDouble(DriverServiceAppraiseeInfo::getScore));
                star = BigDecimal.valueOf(sumAge).stripTrailingZeros().setScale(ONE, BigDecimal.ROUND_HALF_UP).toPlainString();
            }
            vo.setDriverScore(star);
            if (OrderState.STOPSERVICE.getState().equals(orderInfo.getState()) || OrderState.ORDERCLOSE.getState().equals(orderInfo.getState()) || OrderState.DISSENT.getState().equals(orderInfo.getState())) {
                //服务结束后获取里程用车时长
                List<OrderSettlingInfo> orderSettlingInfos = orderSettlingInfoMapper.selectOrderSettlingInfoList(new OrderSettlingInfo(orderId));
                if (!CollectionUtils.isEmpty(orderSettlingInfos)) {
                    OrderSettlingInfo orderSettlingInfo = orderSettlingInfos.get(0);
                    vo.setDistance(orderSettlingInfo.getTotalMileage().stripTrailingZeros().toPlainString() + "公里");
                    vo.setDuration(DateFormatUtils.formatMinute(orderSettlingInfo.getTotalTime().intValue()));
                    vo.setAmount(orderSettlingInfo.getAmount().stripTrailingZeros().toPlainString());
                    Map<String, Object> orderFee = orderSettlingInfoService.getOrderFee(orderSettlingInfo);
                    vo.setOrderFees((List<OtherCostBean>) orderFee.get("otherCostBeans"));
                }
            }
        } else {
            vo.setCarLicense(orderInfo.getCarLicense());//车牌号
            vo.setCarColor(orderInfo.getCarColor());
            vo.setCarType(orderInfo.getCarModel());
            vo.setDriverScore(orderInfo.getDriverGrade());
            List<ThridCarTypeVo> onlienCarType = thirdService.getOnlienCarType();
            if (!CollectionUtils.isEmpty(onlienCarType)) {
                String demandCarLevel = orderInfo.getDemandCarLevel();
                String carPhoto = onlienCarType.stream().filter(p -> p.getName().equals(demandCarLevel)).map(ThridCarTypeVo::getRemark).collect(Collectors.joining());
                vo.setCarPhoto(carPhoto);
            }
            if (OrderState.STOPSERVICE.getState().equals(orderInfo.getState()) || OrderState.endServerStates().contains(orderStateTraceInfo.getState())) {
                OrderCostDetailVO orderCost = this.getOrderCost(orderId);
                vo.setOrderCostDetailVO(orderCost);
                //网约车是否限额
                //查询出用车制度表的限额额度，和限额类型
                this.checkIsOverMoney(orderInfo, vo, 1);
            }
        }
        if (OrderState.endServerStates().contains(orderStateTraceInfo.getState())) {
            // TODO 开发发版放开
            List<OrderHistoryTraceDto> orderHistoryTrace = this.getOrderHistoryTrace(orderId);
            vo.setHistoryTraceList(orderHistoryTrace);
        }
        return vo;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void platCallTaxiParamValid(Long orderId, String userId, String carLevel) throws Exception {
        log.info("调用网约车接口入参：orderId:{},userId:{},Carlevel:{}", orderId, userId, carLevel);
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        boolean reassignment = iOrderStateTraceInfoService.isReassignment(orderId);
        //改派的订单需要操作改派同意
        if (reassignment) {
            this.insertOrderStateTrace(String.valueOf(orderId), ResignOrderTraceState.AGREE.getState(), userId, null, orderInfo.getDriverId(), orderInfo.getCarId());
            //改派订单消息通知
            ismsBusiness.sendMessageReassignSucc(orderId, Long.parseLong(userId));
        } else {
            //添加约车中轨迹状态
            this.insertOrderStateTrace(String.valueOf(orderId), OrderState.SENDINGCARS.getState(), userId, null, null, null);
        }
        //使用汽车的方式，改为网约
        OrderInfo orderInfoUp = new OrderInfo();
        orderInfoUp.setUseCarMode(CarConstant.USR_CARD_MODE_NET);
        orderInfoUp.setState(OrderState.SENDINGCARS.getState());
        orderInfoUp.setOrderId(orderId);
        orderInfoUp.setDriverId(null);
        orderInfoUp.setCarId(null);
        int j = orderInfoMapper.updateOrderInfoNull(orderInfoUp);
        if (j != 1) {
            throw new Exception("约车失败");
        }
        //改派的订单需要操作改派同意
        String serviceType = orderInfo.getServiceType();
        if (serviceType == null || !OrderServiceType.getNetServiceType().contains(serviceType)) {
            throw new Exception("调用网约车参数异常-》服务类型异常！");
        }

        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(orderId);
        List<OrderAddressInfo> orderAddressInfos = iOrderAddressInfoService.selectOrderAddressInfoList(orderAddressInfo);
        for (int i = 0; i < orderAddressInfos.size(); i++) {
            OrderAddressInfo orderAddressInfo1 = orderAddressInfos.get(i);
            //出发地址
            if (orderAddressInfo1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT)) {
                if (serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())) {
                    String icaoCode = orderAddressInfo1.getIcaoCode();
                    if (icaoCode == null || "".equals(icaoCode)) {
                        throw new Exception("调用网约车参数异常-》接机类型，三字码数据不能为空！");
                    }
                }
                Date timeSt = orderAddressInfo1.getActionTime();
                if (timeSt == null || orderAddressInfo1.getCityPostalCode() == null
                        || orderAddressInfo1.getLongitude() == null || orderAddressInfo1.getLatitude() == null
                        || orderAddressInfo1.getAddress() == null) {
                    throw new Exception("调用网约车参数异常-》起点地址相关信息异常！");
                }
            } else if (orderAddressInfo1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE)) {//到达地址
                if (orderAddressInfo1.getLongitude() == null || orderAddressInfo1.getLatitude() == null
                        || orderAddressInfo1.getAddress() == null) {
                    throw new Exception("调用网约车参数异常-》终点地址相关信息异常！");
                }
            }
        }
        Long userIdOrder = orderInfo.getUserId();
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(userIdOrder);
        if (ecmpUser == null || ecmpUser.getUserName() == null || "".equals(ecmpUser.getUserName())
                || ecmpUser.getPhonenumber() == null || "".equals(ecmpUser.getPhonenumber())) {
            throw new Exception("调用网约车参数异常-》乘车人姓名或电话信息异常！");
        }

        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setJourneyId(orderInfo.getJourneyId());
        List<ApplyInfo> applyInfos = applyInfoMapper.selectApplyInfoList(applyInfo);
        ApplyInfo applyInfo1 = applyInfos.get(0);
        String applyType = applyInfo1.getApplyType();
        //如果是差旅用车，需要添加价格预算表
        if (applyType.equals(CarConstant.USE_CAR_TYPE_TRAVEL)) {
            if (carLevel == null || carLevel.equals("") || carLevel.equals("null")) {
                String groups = regimeInfoService.queryCarModeLevel(orderId, null);
                if ("".equals(groups)) {
                    throw new Exception("调用网约车参数异常-》差旅用车，获取车型失败！");
                } else {
                    List<CarLevelAndPriceReVo> carlevelAndPriceByOrderId = regimeInfoService.getCarlevelAndPriceByOrderId(orderId, null);
                    OrderInfo orderInfo1 = orderInfoMapper.selectOrderInfoById(orderId);
                    if (carlevelAndPriceByOrderId != null && carlevelAndPriceByOrderId.size() > 0) {
                        for (CarLevelAndPriceReVo carLevelAndPriceReVo :
                                carlevelAndPriceByOrderId) {
                            JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
                            journeyPlanPriceInfo.setUseCarMode(CarConstant.USR_CARD_MODE_NET);
                            journeyPlanPriceInfo.setCreateTime(DateUtils.getNowDate());
                            journeyPlanPriceInfo.setCreateBy(userId);
                            journeyPlanPriceInfo.setOrderId(orderId);
                            journeyPlanPriceInfo.setPowerId(orderInfo.getPowerId());
                            journeyPlanPriceInfo.setSource(carLevelAndPriceReVo.getSource());
                            journeyPlanPriceInfo.setNodeId(orderInfo1.getNodeId());
                            journeyPlanPriceInfo.setJourneyId(orderInfo1.getJourneyId());
                            journeyPlanPriceInfo.setPrice(new BigDecimal(carLevelAndPriceReVo.getEstimatePrice()));
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date parse = carLevelAndPriceReVo.getBookingStartTime();
                            String formatEnd = simpleDateFormat.format(parse.getTime() + (carLevelAndPriceReVo.getDuration() * 60000));
                            journeyPlanPriceInfo.setPlannedArrivalTime(simpleDateFormat.parse(formatEnd));
                            journeyPlanPriceInfo.setPlannedDepartureTime(parse);
                            journeyPlanPriceInfo.setDuration(carLevelAndPriceReVo.getDuration());
                            EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
                            enterpriseCarTypeInfo.setLevel(carLevelAndPriceReVo.getOnlineCarLevel());
                            List<EnterpriseCarTypeInfo> enterpriseCarTypeInfos = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
                            if (enterpriseCarTypeInfos != null && enterpriseCarTypeInfos.size() > 0) {
                                EnterpriseCarTypeInfo enterpriseCarTypeInfo1 = enterpriseCarTypeInfos.get(0);
                                journeyPlanPriceInfo.setCarTypeId(enterpriseCarTypeInfo1.getCarTypeId());
                            }
                            journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
                        }
                    }
                }
            }
        } else if (applyType.equals(CarConstant.USE_CAR_TYPE_OFFICIAL)) { //如果是公务用车，没传车型（不是取消后选车型的情况），校验车型价格表是否有数据
            if (carLevel == null || carLevel.equals("") || carLevel.equals("null")) {
                String groups = regimeInfoService.queryCarModeLevel(orderId, null);
                if ("".equals(groups) || groups == null) {
                    throw new Exception("调用网约车参数异常-》公务用车，获取车型失败！");
                }
            }
        } else {
            throw new Exception("调用网约车参数异常-》申请单类型" + applyType + "错误！");
        }
        //校验车型价格表是否有数据
        JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
        journeyPlanPriceInfo.setJourneyId(orderInfo.getJourneyId());
        journeyPlanPriceInfo.setNodeId(orderInfo.getNodeId());
        journeyPlanPriceInfo.setUseCarMode(CarConstant.USR_CARD_MODE_NET);
        List<JourneyPlanPriceInfo> journeyPlanPriceInfos = journeyPlanPriceInfoMapper.selectJourneyPlanPriceInfoList(journeyPlanPriceInfo);
        if (journeyPlanPriceInfos == null || journeyPlanPriceInfos.size() == 0) {
            throw new Exception("调用网约车参数异常-》车型价格表无可用数据，预估价获取失败！");
        }
        ((IOrderInfoService) AopContext.currentProxy()).platCallTaxi(orderId, enterpriseId, licenseContent, apiUrl, userId, carLevel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void platCallTaxi(Long orderId, String enterpriseId, String licenseContent, String apiUrl, String userId, String carLevel) throws Exception {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        try {
            //MAC地址
            List<String> macList = MacTools.getMacList();
            String macAdd = macList.get(0);
            //调用网约车约车接口参数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("enterpriseId", enterpriseId);
            paramMap.put("licenseContent", licenseContent);
            paramMap.put("mac", macAdd);
            paramMap.put("enterpriseOrderId", orderId + "");
            paramMap.put("payFlag", "0");
            //目前状态固定为S200
            paramMap.put("status", "S200");
            OrderInfo orderInfoOld = orderInfoMapper.selectOrderInfoById(orderId);
            OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
            orderAddressInfo.setOrderId(orderId);
            long timeSt = 0L;
            //三字码
            String icaoCode = null;
            String serviceType = orderInfoOld.getServiceType();
            Long userIdOrder = orderInfoOld.getUserId();
            EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(userIdOrder);
            paramMap.put("riderName", ecmpUser.getUserName());
            paramMap.put("riderPhone", ecmpUser.getPhonenumber());
            List<OrderAddressInfo> orderAddressInfos = iOrderAddressInfoService.selectOrderAddressInfoList(orderAddressInfo);
            if (carLevel != null && !carLevel.equals("") && !carLevel.equals("null")) {
                paramMap.put("groupIds", carLevel);
            } else {
//                String groups = regimeInfoService.queryCarModeLevel(orderId, null);
                String groups = "";
                String startCity = "";
                List<ApplyInfo> applyInfos = applyInfoMapper.selectApplyInfoList(new ApplyInfo(orderInfoOld.getJourneyId()));
                Long applyId = null;
                if (!CollectionUtils.isEmpty(applyInfos)) {
                    applyId = applyInfos.get(0).getApplyId();
                    startCity = orderAddressInfos.stream().filter(p -> p.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT)).map(OrderAddressInfo::getCityPostalCode).collect(Collectors.joining());
                    List<ApplyUseCarType> applyUseCarTypes = applyUseCarTypeMapper.selectApplyUseCarTypeList(new ApplyUseCarType(applyId, startCity));
                    if (!CollectionUtils.isEmpty(applyUseCarTypes)) {
                        ApplyUseCarType applyUseCarType = applyUseCarTypes.get(0);
                        if (StringUtils.isNotEmpty(applyUseCarType.getOnlineCarType())) {
                            groups += "," + applyUseCarType.getOnlineCarType();
                        }
                        if (StringUtils.isNotEmpty(applyUseCarType.getShuttleOnlineCarType())) {
                            groups += "," + applyUseCarType.getOnlineCarType();
                        }
                    }
                }
                if (StringUtils.isEmpty(groups)) {
                    throw new Exception("调用网约车下单前通过订单id" + orderId + "城市" + startCity + "网约车型为空");
                }
                paramMap.put("groupIds", groups.substring(1));
            }
            for (int i = 0; i < orderAddressInfos.size(); i++) {
                OrderAddressInfo orderAddressInfo1 = orderAddressInfos.get(i);
                //出发地址
                if (orderAddressInfo1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT)) {
                    if (serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())) {
                        icaoCode = orderAddressInfo1.getIcaoCode();
                    }
                    timeSt = orderAddressInfo1.getActionTime().getTime();
                    paramMap.put("cityId", orderAddressInfo1.getCityPostalCode());
                    paramMap.put("bookingDate", (orderAddressInfo1.getActionTime().getTime() + "").substring(0, 10));
                    paramMap.put("bookingStartPointLo", orderAddressInfo1.getLongitude() + "");
                    paramMap.put("bookingStartPointLa", orderAddressInfo1.getLatitude() + "");
                    paramMap.put("bookingStartAddr", URLEncoder.encode(orderAddressInfo1.getAddress()));
                } else if (orderAddressInfo1.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE)) {//到达地址
                    paramMap.put("bookingEndPointLo", orderAddressInfo1.getLongitude() + "");
                    paramMap.put("bookingEndPointLa", orderAddressInfo1.getLatitude() + "");
                    paramMap.put("bookingEndAddr", URLEncoder.encode(orderAddressInfo1.getAddress()));
                }
            }

            JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
            journeyPlanPriceInfo.setJourneyId(orderInfoOld.getJourneyId());
            journeyPlanPriceInfo.setNodeId(orderInfoOld.getNodeId());
            journeyPlanPriceInfo.setUseCarMode(CarConstant.USR_CARD_MODE_NET);
            List<JourneyPlanPriceInfo> journeyPlanPriceInfos = journeyPlanPriceInfoMapper.selectJourneyPlanPriceInfoList(journeyPlanPriceInfo);
            if (journeyPlanPriceInfos.size() > 0) {
                paramMap.put("estimatedAmount", journeyPlanPriceInfos.get(0).getPrice() + "");
            } else {
                throw new Exception("预估价获取失败");
            }
            //发起约车
            //订单类型,1:随叫随到;2:预约用车;3:接机;5:送机
            String result = null;
            try {
                if (serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_NOW.getBcState()) || serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState())) {
                    paramMap.put("serviceType", OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState());
                    result = OkHttpUtil.postForm(apiUrl + "/service/applyPlatReceiveOrder", paramMap);
                } else if (serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())) {
                    paramMap.put("serviceType", OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState());
                    if (icaoCode != null && icaoCode.contains("\\,")) {
                        String[] split = icaoCode.split("\\,|\\，");
                        paramMap.put("depCode", split[0]);
                        paramMap.put("arrCode", split[1]);
                    }
                    paramMap.put("airlineNum", orderInfoOld.getFlightNumber());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formatDate = simpleDateFormat.format(orderInfoOld.getFlightPlanTakeOffTime());
                    paramMap.put("planDate", formatDate);
                    result = OkHttpUtil.postForm(apiUrl + "/service/applyPlatReceivePickUpOrder", paramMap);
                } else if (serviceType.equals((OrderServiceType.ORDER_SERVICE_TYPE_SEND.getBcState()))) {
                    paramMap.put("serviceType", "4000");
                    result = OkHttpUtil.postForm(apiUrl + "/service/applyPlatReceiveSendToOrder", paramMap);
                } else {

                }
            } catch (SocketTimeoutException e) {
                log.error("业务处理异常", e);
                throw new Exception("网约车下单超时");
            }
            log.info("订单{}下单参数，paramMap:[{}] result:[{}]", orderId, paramMap, result);
        } catch (Exception e) {
            log.error("业务处理异常", e);
            throw new Exception("网约车下单异常");
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean ownCarSendCar(Long orderId, Long driverId, Long carId, Long userId) throws Exception {
        OrderInfo orderOldInfo = orderInfoMapper.selectOrderInfoById(orderId);
        Long oldDriverId = orderOldInfo.getDriverId();
        Long oldCarId = orderOldInfo.getCarId();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setState(OrderState.ALREADYSENDING.getState());
        // 查询司机信息
        DriverInfo driverInfo = driverInfoMapper.selectDriverInfoById(driverId);
        orderInfo.setOrderId(orderId);
        orderInfo.setDriverId(driverId);
        if (null != driverInfo) {
            orderInfo.setDriverName(driverInfo.getDriverName());
            orderInfo.setDriverMobile(driverInfo.getMobile());
        }
        // 查询车辆信息
        CarInfo carInfo = carInfoMapper.selectCarInfoById(carId);
        if (null != carInfo) {
            orderInfo.setCarLicense(carInfo.getCarLicense());
            orderInfo.setCarModel(carInfo.getCarType());
            orderInfo.setCarColor(carInfo.getCarColor());
            //查询车辆的车型名称
            String carTypeName = enterpriseCarTypeInfoMapper.queryCarTypeNameByCarId(carId);
            orderInfo.setDemandCarLevel(carTypeName);
        }
        orderInfo.setCarId(carId);
        orderInfo.setUpdateBy(String.valueOf(userId));
        orderInfo.setUpdateTime(new Date());
        orderInfo.setUseCarMode(CarConstant.USR_CARD_MODE_HAVE);
        // 更新订单信息
        int updateFlag = updateOrderInfo(orderInfo);
        //释放司车辆
        DispatchLockCarDto dispatchLockCarDto = new DispatchLockCarDto();
        dispatchLockCarDto.setCarId(carId.toString());
        dispatchService.unlockSelectedCar(dispatchLockCarDto);
        //释放司机
        DispatchLockDriverDto dispatchLockDriverDto = new DispatchLockDriverDto();
        dispatchLockDriverDto.setDriverId(driverId.toString());
        dispatchService.unlockSelectedDriver(dispatchLockDriverDto);
        /*
         * * // 判读该单子是否是改派单 if
         * (iOrderStateTraceInfoService.isReassignment(orderId)) { // 是改派单
         * orderStateTraceInfo.setState(OrderStateTrace.PASSREASSIGNMENT.
         * getState()); orderInfo.setState(OrderState.REASSIGNPASS.getState());
         * } else { //申请单
         * orderStateTraceInfo.setState(OrderStateTrace.SENDCAR.getState());
         * orderInfo.setState(OrderState.ALREADYSENDING.getState()); }
         */

        if (iOrderStateTraceInfoService.isReassignment(orderId)) {
            // 是改派单
            reassign(orderId.toString(), null, "1", userId, oldDriverId, oldCarId);
        } else {
            // 是申请单
            OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
            orderStateTraceInfo.setState(OrderStateTrace.SENDCAR.getState());
            orderStateTraceInfo.setCreateBy(String.valueOf(userId));
            orderStateTraceInfo.setOrderId(orderId);
            // 新增订单状态流转记录
            int insertFlag = iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
            // 发送给司机和申请人消息通知
            ismsBusiness.sendMessageDispatchCarComplete(orderId, userId);
        }
        // 发送短信
        ismsBusiness.sendSmsCallTaxiNet(orderId);
        return updateFlag > 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Long officialOrder(OfficialOrderReVo officialOrderReVo, Long userId) throws Exception {
        log.info("公务下单-------------》接口参数:{}", officialOrderReVo);
        JourneyUserCarPower journeyUserCarPower = journeyUserCarPowerMapper.selectJourneyUserCarPowerById(officialOrderReVo.getPowerId());
        if (journeyUserCarPower == null) {
            throw new Exception("用车权限不存在");
        }
        List<OrderInfo> validOrderByPowerId = orderInfoMapper.getValidOrderByPowerId(officialOrderReVo.getPowerId());
        if (validOrderByPowerId != null && validOrderByPowerId.size() > 0) {
            throw new Exception("此用车权限已存在有效订单");
        }
        Long journeyId = journeyUserCarPower.getJourneyId();
        Long applyId = journeyUserCarPower.getApplyId();
        //获取行程主表信息
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(journeyId);
        String serviceType = journeyInfo.getServiceType();
        //是否往返 是Y000  否N444
        String itIsReturn = journeyInfo.getItIsReturn();

        //获取申请表信息
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        //插入订单初始信息
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNumber(OrderUtils.getOrderNum());
        orderInfo.setCompanyId(officialOrderReVo.getCompanyId());
        orderInfo.setServiceType(serviceType);
        if (serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())) {
            orderInfo.setFlightNumber(journeyInfo.getFlightNumber());
            orderInfo.setFlightPlanTakeOffTime(journeyInfo.getFlightPlanTakeOffTime());
        }
        orderInfo.setJourneyId(journeyId);
        orderInfo.setDriverId(null);
        orderInfo.setCarId(null);
        orderInfo.setUserId(journeyInfo.getUserId());
        orderInfo.setCreateBy(String.valueOf(userId));
        orderInfo.setCreateTime(new Date());
        String applyType = applyInfo.getApplyType();
        //如果是公务用车且走调度则状态直接为待派单,走网约车则变为约车中
        if (CarConstant.USE_CAR_TYPE_OFFICIAL.equals(applyType)) {
            if (officialOrderReVo.getIsDispatch() == 2) {
                orderInfo.setState(OrderState.SENDINGCARS.getState());
            } else {
                orderInfo.setState(OrderState.WAITINGLIST.getState());
            }
        } else {
            throw new Exception("下单失败,只支持公务下单");
        }
        String type = journeyUserCarPower.getType();
        Long nodeId = journeyUserCarPower.getNodeId();
        Long powerId = journeyUserCarPower.getPowerId();
        orderInfo.setNodeId(nodeId);
        orderInfo.setPowerId(powerId);// TODO: 2020/3/3  权限表何时去创建？ 申请审批通过以后创建用车权限表记录，一个行程节点可能对应多个用车权限，比如往返超过固定时间
        orderInfoMapper.insertOrderInfo(orderInfo);
        //如果是公务用车，插入订单途经点信息表
        JourneyNodeInfo journeyNodeInfo = new JourneyNodeInfo();
        journeyNodeInfo.setJourneyId(journeyId);
        List<JourneyNodeInfo> journeyNodeInfoList = journeyNodeInfoMapper.selectJourneyNodeInfoList(journeyNodeInfo);
        if (journeyNodeInfoList != null && journeyNodeInfoList.size() > 0) {
            int realSize = journeyNodeInfoList.size();
            int cycleBegin = 0;
            List<OrderViaInfo> orderViaInfos = new ArrayList<>();
            //是否往返 ,不是则为 0 到 n，否则按下面逻辑
            if (itIsReturn.equals(JourneyConstant.IT_IS_RETURN)) {
                //去程 0 到n-1
                if (type.equals(CarConstant.OUTWARD_VOYAGE)) {
                    cycleBegin = 0;
                    realSize = realSize - 1;
                } else {//返程 n-1 到 n
                    cycleBegin = realSize - 1;
                }
            }
            for (int j = cycleBegin; j < realSize; j++) {
                JourneyNodeInfo journeyNodeInfoCh = journeyNodeInfoList.get(j);
                Integer number = journeyNodeInfoCh.getNumber();
                OrderViaInfo orderViaInfo = new OrderViaInfo();
                orderViaInfo.setOrderId(orderInfo.getOrderId());
                orderViaInfo.setItIsPassed(CommonConstant.NO_PASS);
                orderViaInfo.setArrivedTime(null);
                orderViaInfo.setDuration(null);
                orderViaInfo.setLeaveTime(null);
                orderViaInfo.setCreateBy(String.valueOf(userId));
                orderViaInfo.setCreateTime(DateUtils.getNowDate());
                orderViaInfo.setUpdateTime(null);
                orderViaInfo.setUpdateBy(null);
                if (number == (cycleBegin + 1)) {
                    orderViaInfo.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLongitude()));
                    orderViaInfo.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLatitude()));
                    orderViaInfo.setFullAddress(journeyNodeInfoCh.getPlanBeginLongAddress());
                    orderViaInfo.setShortAddress(journeyNodeInfoCh.getPlanBeginAddress());
                    orderViaInfo.setSortNumber(1);
                    if (realSize == number && !ServiceTypeConstant.CHARTERED.equals(serviceType)) {//只有一条数据时候，插入终点
                        OrderViaInfo orderViaInfoEnd = new OrderViaInfo();
                        orderViaInfoEnd.setOrderId(orderInfo.getOrderId());
                        orderViaInfoEnd.setItIsPassed(CommonConstant.NO_PASS);
                        orderViaInfoEnd.setArrivedTime(null);
                        orderViaInfoEnd.setDuration(null);
                        orderViaInfoEnd.setLeaveTime(null);
                        orderViaInfoEnd.setCreateBy(String.valueOf(userId));
                        orderViaInfoEnd.setCreateTime(DateUtils.getNowDate());
                        orderViaInfoEnd.setUpdateTime(null);
                        orderViaInfoEnd.setUpdateBy(null);
                        orderViaInfoEnd.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLongitude()));
                        orderViaInfoEnd.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLatitude()));
                        orderViaInfoEnd.setFullAddress(journeyNodeInfoCh.getPlanEndLongAddress());
                        orderViaInfoEnd.setShortAddress(journeyNodeInfoCh.getPlanEndAddress());
                        orderViaInfoEnd.setSortNumber(2);
                        orderViaInfos.add(orderViaInfoEnd);
                    }
                } else {
                    orderViaInfo.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLongitude()));
                    orderViaInfo.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanEndLatitude()));
                    orderViaInfo.setFullAddress(journeyNodeInfoCh.getPlanEndLongAddress());
                    orderViaInfo.setShortAddress(journeyNodeInfoCh.getPlanEndAddress());
                    orderViaInfo.setSortNumber(journeyNodeInfoCh.getNumber());
                }
                orderViaInfos.add(orderViaInfo);

                //将公务计划时间地点信息插入订单地址表
                OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
                orderAddressInfo.setOrderId(orderInfo.getOrderId());
                orderAddressInfo.setJourneyId(journeyId);
                orderAddressInfo.setNodeId(nodeId);
                orderAddressInfo.setPowerId(powerId);
                orderAddressInfo.setUserId(journeyInfo.getUserId() + "");
                orderAddressInfo.setCreateBy(userId + "");
                if (serviceType.equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())) {
                    SimpleDateFormat si = new SimpleDateFormat("yyyy-MM-dd");
                    String format = si.format(journeyInfo.getFlightPlanTakeOffTime());
                    FlightInfoVo flightInfoVo = thirdService.loadDepartment(journeyInfo.getFlightNumber(), format);
                    if (flightInfoVo != null) {
                        String flightDepcode = flightInfoVo.getFlightDepcode();
                        String flightArrcode = flightInfoVo.getFlightArrcode();
                        orderAddressInfo.setIcaoCode(flightDepcode + "," + flightArrcode);
                    }
                }
                //起点
                if (j == cycleBegin) {
                    orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
                    orderAddressInfo.setActionTime(journeyNodeInfoCh.getPlanSetoutTime());
                    orderAddressInfo.setLongitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLongitude()));
                    orderAddressInfo.setLatitude(Double.parseDouble(journeyNodeInfoCh.getPlanBeginLatitude()));
                    orderAddressInfo.setAddress(journeyNodeInfoCh.getPlanBeginAddress());
                    orderAddressInfo.setAddressLong(journeyNodeInfoCh.getPlanBeginLongAddress());
                    orderAddressInfo.setCityPostalCode(journeyNodeInfoCh.getPlanBeginCityCode());
                    iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
                }
                //终点
                if (j == realSize - 1) {
                    orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
                    orderAddressInfo.setActionTime(journeyNodeInfoCh.getPlanArriveTime());
                    Double lat = journeyNodeInfoCh.getPlanEndLongitude() == null ? null : Double.parseDouble(journeyNodeInfoCh.getPlanEndLongitude());
                    Double lng = journeyNodeInfoCh.getPlanEndLatitude() == null ? null : Double.parseDouble(journeyNodeInfoCh.getPlanEndLatitude());
                    orderAddressInfo.setLongitude(lat);
                    orderAddressInfo.setLatitude(lng);
                    orderAddressInfo.setAddress(journeyNodeInfoCh.getPlanEndAddress());
                    orderAddressInfo.setAddressLong(journeyNodeInfoCh.getPlanEndLongAddress());
                    orderAddressInfo.setCityPostalCode(journeyNodeInfoCh.getPlanEndCityCode());
                    iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
                }
            }
            if (orderViaInfos.size() > 0) {
                orderViaInfoMapper.insertOrderViaInfoBatch(orderViaInfos);
            }
        }
        //插入订单轨迹表
        this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.INITIALIZING.getState(), String.valueOf(userId), null, null, null);
        this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.WAITINGLIST.getState(), String.valueOf(userId), null, null, null);
        //用车权限次数变化
        iJourneyUserCarPowerService.updatePowerSurplus(powerId, 1);
        //如果是网约车，发起异步约车请求
        if (officialOrderReVo.getIsDispatch() == 2) {
            ((OrderInfoServiceImpl) AopContext.currentProxy()).platCallTaxiParamValid(orderInfo.getOrderId(), String.valueOf(userId), officialOrderReVo.getCarLevel());
        }
        log.info("公务下单-------------》返回结果:{}", orderInfo.getOrderId());
        return orderInfo.getOrderId();
    }

    @Override
    public OrderDriverListInfo getNextTaskWithDriver(Long driverId) {
        return orderInfoMapper.getNextTaskWithDriver(driverId);
    }

    @Override
    public OrderDriverListInfo getNextTaskWithCar(Long carId) {
        return orderInfoMapper.getNextTaskWithCar(carId);
    }

    @Override
    public List<OrderDriverListInfo> driverOrderUndoneList(LoginUser loginUser, Integer pageNum, Integer pageSize, int day) throws Exception {
        SysDriver driver = loginUser.getDriver();
        if (driver == null) {
            throw new Exception("当前登录人不是司机");
        }
//        DriverInfo driverInfo = driverInfos.get(0);
        Long driverId = driver.getDriverId();
        List<OrderDriverListInfo> lsit = orderInfoMapper.driverOrderUndoneList(driverId, day);
        return lsit;
    }

    @Override
    public int driverOrderCount(LoginUser loginUser) throws Exception {
        SysDriver driver = loginUser.getDriver();
        if (driver == null) {
            throw new Exception("当前登录人不是司机");
        }
        Long driverId = driver.getDriverId();
        String states = OrderState.ALREADYSENDING.getState() + "," + OrderState.READYSERVICE.getState();
        return orderInfoMapper.getDriverOrderCount(driverId, states);
    }

    //获取司机任务详情
    @Override
    public DriverOrderInfoVO driverOrderDetail(Long orderId) throws Exception {
        DriverOrderInfoVO vo = orderInfoMapper.selectOrderDetail(orderId);
        vo.setCustomerServicePhone(thirdService.getCustomerPhone());
        OrderSettlingInfo orderSettlingInfo = orderSettlingInfoMapper.selectOrderSettlingInfoByOrderId(orderId);
//        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(vo.getUserId());
        String passengerPhone = null;
        String passengerName = null;
        //获取乘车人信息
        List<JourneyPassengerInfo> journeyPassengerInfos = journeyPassengerInfoMapper.selectJourneyPassengerInfoList(new JourneyPassengerInfo(vo.getJourneyId()));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(journeyPassengerInfos)) {
            passengerPhone = journeyPassengerInfos.get(0).getMobile();
            passengerName = journeyPassengerInfos.get(0).getName();
        }

        vo.setUserName(passengerName);
        vo.setUserPhone(passengerPhone);
        List<OtherCostBean> otherCostBeans = new ArrayList<>();
        if (orderSettlingInfo != null) {
            String amount = orderSettlingInfo.getAmount() == null ? null : orderSettlingInfo.getAmount().stripTrailingZeros().toPlainString();
            vo.setOrderAmount(amount);
            vo.setTotalMileage(orderSettlingInfo.getTotalMileage() == null ? String.valueOf(ZERO) : orderSettlingInfo.getTotalMileage().stripTrailingZeros().toPlainString());
            vo.setTotalTime(orderSettlingInfo.getTotalTime() == null ? String.valueOf(ZERO) : orderSettlingInfo.getTotalTime().stripTrailingZeros().toPlainString());
            String amountDetail = orderSettlingInfo.getAmountDetail();
            String outPrice = orderSettlingInfo.getOutPrice();
            if (StringUtils.isNotEmpty(amountDetail)) {
                JSONObject jsonObject = JSONObject.parseObject(amountDetail);
                String otherCostJson = jsonObject.getString("otherCost");
                List<OtherCostBean> amountDetailList = JSONObject.parseArray(otherCostJson, OtherCostBean.class);
                otherCostBeans.addAll(amountDetailList);
            }
            if (StringUtils.isNotEmpty(outPrice)) {
                JSONObject jsonObject = JSONObject.parseObject(outPrice);
                String otherCostJson = jsonObject.getString("otherCost");
                List<OtherCostBean> outPriceList = JSONObject.parseArray(otherCostJson, OtherCostBean.class);
                otherCostBeans.addAll(outPriceList);
            }
            if (!CollectionUtils.isEmpty(otherCostBeans)) {
                vo.setOrderFees(otherCostBeans);
            }
        }
        List<String> imgUrls = orderSettlingInfoMapper.selectOrderSettlingImageList(orderId);
        vo.setFeeImageUrls(imgUrls);
        return vo;
    }

    @Override
    public OrderStateVO getOrderState(Long orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        JourneyUserCarPower journeyUserCarPower = journeyUserCarPowerMapper.selectJourneyUserCarPowerById(orderInfo.getPowerId());
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(journeyUserCarPower.getApplyId());
        OrderStateVO orderState = orderInfoMapper.getOrderState(orderId);
        int count = orderStateTraceInfoMapper.selectCountForAgainStrte(orderId, OrderStateTrace.manyUseCarState());
        orderState.setIsFirstState(count);
        //判断是否是多日租车
        if (null != orderState && CharterTypeEnum.MORE_RENT_TYPE.getKey().equals(orderState.getCharterCarType())) {
            Date startDate = DateUtils.parseDate(orderState.getStartDate());//租车开始时间
            Date endDate = DateUtils.parseDate(orderState.getEndDate());//租车结束时间
            //判断是否半日租
            String carType = CommonUtils.getCarType(startDate, endDate, orderState.getUseTime());
            orderState.setWhole(carType);
        }
        orderState.setApplyType(applyInfo.getApplyType());
        orderState.setCharterCarType(CharterTypeEnum.format(orderState.getCharterCarType()));
        List<JourneyPlanPriceInfo> journeyPlanPriceInfos = journeyPlanPriceInfoMapper.selectJourneyPlanPriceInfoList(new JourneyPlanPriceInfo(orderId));
        if (!CollectionUtils.isEmpty(journeyPlanPriceInfos)) {
            orderState.setPlanPrice(journeyPlanPriceInfos.get(0).getPrice().toPlainString());
        }
        return orderState;
    }

    @Override
    public PageResult<OrderListBackDto> getOrderListBackDto(OrderListBackDto orderListBackDto, LoginUser user) {
        String key = isDispatcher(user);
        if (null == key) {
            orderListBackDto.setDeptId(user.getUser().getDeptId());
        } else if ("admin".equals(key)) {
            orderListBackDto.setUserId(null);
            orderListBackDto.setUpdateBy(null);
        } else {
            orderListBackDto.setUpdateBy("C000".equals(key) ? null : user.getUser().getUserId());
        }
        //订单管理需要的状态 已取消  S911    已完成 S900  待确认 S699     服务中S616  待上车 S600   接驾中 S500  待服务 S299
        List<OrderListBackDto> list = orderInfoMapper.getOrderListBackDto(orderListBackDto);
        for (OrderListBackDto orderListBack : list) {
            //补单数据单独处理
            if (ItIsSupplementEnum.ORDER_REPLENISHMENT_STATUS.getValue().equals(orderListBack.getItIsSupplement())) {
                OrderListBackDto orderList = orderInfoMapper.getReplentshmentOrder(orderListBack);
                //申请人姓名
                orderListBack.setApplyName(orderList.getApplyName());
                //申请人手机号
                orderListBack.setApplyPhoneNumber(orderList.getApplyPhoneNumber());
                //乘车人
                orderListBack.setPassengerName(orderList.getPassengerName());
                //同行人数量
                orderListBack.setPeerName(orderList.getPeerName());
                //实际用车时间
                orderListBack.setBeginTime(orderList.getBeginTime());
                //实际下车时间
                orderListBack.setEndTime(orderList.getEndTime());
                //实际上车地址
                orderListBack.setBeginAddress(orderList.getBeginAddress());
                //实际下车地址
                orderListBack.setEndAddress(orderList.getEndAddress());
                //服务类型
                orderListBack.setServiceType(orderList.getServiceType());
                //用车方式
                orderListBack.setUseCarMode(orderList.getUseCarMode());
                //费用合计
                orderListBack.setAmount(orderList.getAmount());
                //订单状态
                orderListBack.setState(orderList.getState());
                //订单编号
                orderListBack.setOrderNumber(orderList.getOrderNumber());
                //itIsSupplement订单标签
                orderListBack.setItIsSupplement(orderList.getItIsSupplement());
            }
        }
        if (orderListBackDto.getOrderState() != null && orderListBackDto.getLabelState() != null) {
            List<OrderListBackDto> list1 = list.stream().filter(e -> orderListBackDto.getOrderState().contains(e.getOrderState())).collect(Collectors.toList());
            String labelState = orderListBackDto.getLabelState();
            List<OrderListBackDto> list2 = list1.stream().filter(e -> !labelState.contains(e.getLabelState())).collect(Collectors.toList());
            list2 = addState(list2, user);
            PageInfo<OrderListBackDto> info = new PageInfo<>(list2);
            return new PageResult<>(info.getTotal(), info.getPages(), list2);
        } else if (orderListBackDto.getLabelState() != null) {
            List<OrderListBackDto> list1 = list.stream().filter(e -> orderListBackDto.getLabelState().contains(e.getLabelState())).collect(Collectors.toList());
            list1 = addState(list1, user);
            PageInfo<OrderListBackDto> info = new PageInfo<>(list1);
            return new PageResult<>(info.getTotal(), info.getPages(), list1);
        }
        list = addState(list, user);
        PageInfo<OrderListBackDto> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), info.getPages(), list);
    }

    @Override
    public OrderDetailBackDto getOrderListDetail(String orderNo) {
        String itIsSupplement = orderInfoMapper.getOrderById(orderNo);
        OrderDetailBackDto orderDetailBackDto = null;
        if (ItIsSupplementEnum.ORDER_REPLENISHMENT_STATUS.getValue().equals(itIsSupplement)) {
            orderDetailBackDto = orderInfoMapper.getOrderListDetailById(orderNo);
        } else {
            orderDetailBackDto = orderInfoMapper.getOrderListDetail(orderNo);
        }
        OrderFeeDetailVO orderFeeDetailVO = null;
        List list = new ArrayList<>();
        if (CarConstant.USR_CARD_MODE_NET.equals(orderDetailBackDto.getUseCarMode())) {
            String amountDetail = orderDetailBackDto.getAmountDetail();
            if (!amountDetail.isEmpty()) {
                orderFeeDetailVO = new Gson().fromJson(amountDetail, OrderFeeDetailVO.class);
            }
            if (orderFeeDetailVO == null) {
                return null;
            }
            //基础套餐费：包含基础时长和基础里程，超出另外计费
            String basePrice = orderFeeDetailVO.getBasePrice();
            list.add(new OtherCostVO("起步价", basePrice));
            //超时长费：超出基础时长时计算，同时会区分高峰和平峰时段
            String overTimeNumTotal = orderFeeDetailVO.getOverTimeNumTotal();//超时长费:高峰时长费+平超时长费
            list.add(new OtherCostVO("超时长费", overTimeNumTotal));
            //超里程费：超出基础里程时计算，同时会区分高峰和平峰时段
            String overMilageNumTotal = orderFeeDetailVO.getOverMilageNumTotal();//超里程费用:高峰里程费+平超里程
            list.add(new OtherCostVO("超里程费", overMilageNumTotal));
            //等待费
            String waitingFee = orderFeeDetailVO.getWaitingFee();
            list.add(new OtherCostVO("等待费", waitingFee));
            orderDetailBackDto.setAmountDetail(JSON.toJSONString(list));
        }
        orderDetailBackDto.setCostList(getOrderCostGroup(Long.valueOf(orderNo)));
        return orderDetailBackDto;
    }


    @Override
    public JSONObject getThirdPartyOrderState(Long orderId) throws Exception {
        Map<String, Object> queryOrderStateMap = new HashMap<>();
        queryOrderStateMap.put("enterpriseId", enterpriseId);
        queryOrderStateMap.put("licenseContent", licenseContent);
        queryOrderStateMap.put("mac", MacTools.getMacList().get(0));
        queryOrderStateMap.put("enterpriseOrderId", orderId + "");
        queryOrderStateMap.put("status", "S200");
        String resultQuery = OkHttpUtil.postForm(apiUrl + "/service/getOrderState", queryOrderStateMap);
        JSONObject jsonObjectQuery = JSONObject.parseObject(resultQuery);
        log.info("订单号" + orderId + "调用三方订单详情:" + jsonObjectQuery);
        JSONObject data = jsonObjectQuery.getJSONObject("data");
        if (!"0".equals(jsonObjectQuery.getString("code"))) {
            throw new Exception("获取网约车信息失败");
        }
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Long applyUseCarWithTravel(ApplyUseWithTravelDto applyUseWithTravelDto, Long userId) throws Exception {
        JourneyUserCarPower journeyUserCarPower = journeyUserCarPowerMapper.selectJourneyUserCarPowerById(applyUseWithTravelDto.getTicketId());
        if (journeyUserCarPower == null) {
            throw new Exception("用车权限不存在");
        }
        List<OrderInfo> validOrderByPowerId = orderInfoMapper.getValidOrderByPowerId(applyUseWithTravelDto.getTicketId());
        if (validOrderByPowerId != null && validOrderByPowerId.size() > 0) {
            throw new Exception("此用车权限已存在有效订单");
        }
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(journeyUserCarPower.getJourneyId());
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNumber(OrderUtils.getOrderNum());
        orderInfo.setPowerId(journeyUserCarPower.getPowerId());
        orderInfo.setJourneyId(journeyUserCarPower.getJourneyId());
        orderInfo.setNodeId(journeyUserCarPower.getNodeId());
        orderInfo.setUserId(journeyInfo.getUserId());
        orderInfo.setCompanyId(applyUseWithTravelDto.getCompanyId());
        //直接约车，约车中。走调度，待派单
        if (applyUseWithTravelDto.getIsDispatch() == 2) {
            orderInfo.setState(OrderState.SENDINGCARS.getState());
            String groupId = applyUseWithTravelDto.getGroupId();
            String[] splits = groupId.split(",|，");
            StringBuilder demandCarLevel = new StringBuilder();
            for (String split :
                    splits) {
                String[] split1 = split.split(":");
                String carLevel = split1[0];
                demandCarLevel.append(carLevel + ",");
            }
            String s = demandCarLevel.toString();
            String substring = s.substring(0, s.lastIndexOf(","));
            orderInfo.setDemandCarLevel(substring);
        } else {
            orderInfo.setState(OrderState.WAITINGLIST.getState());
        }
        String type = applyUseWithTravelDto.getType();
        if (OrderServiceType.ORDER_SERVICE_TYPE_NOW.getPrState().equals(type) || OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getPrState().equals(type)) {
            orderInfo.setServiceType(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState());
        } else if (OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getPrState().equals(type)) {
            orderInfo.setServiceType(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState());
            orderInfo.setFlightNumber(applyUseWithTravelDto.getAirlineNum());
            SimpleDateFormat si = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date planDate = si.parse(applyUseWithTravelDto.getPlanDate());
            orderInfo.setFlightPlanTakeOffTime(planDate);
        } else if (OrderServiceType.ORDER_SERVICE_TYPE_SEND.getPrState().equals(type)) {
            orderInfo.setServiceType(OrderServiceType.ORDER_SERVICE_TYPE_SEND.getBcState());
        } else {
            orderInfo.setServiceType(OrderServiceType.ORDER_SERVICE_TYPE_CHARTERED.getBcState());
        }
        orderInfo.setCreateTime(DateUtils.getNowDate());
        orderInfo.setCreateBy(userId + "");
        orderInfoMapper.insertOrderInfo(orderInfo);
        //不走调度时，根据前台传的时长算出结束时间存入地址表，走调度根据高德算时间
        Date endAction = null;
        if (applyUseWithTravelDto.getIsDispatch() == 2) {
            //添加预估价
            String groupId = applyUseWithTravelDto.getGroupId();
            String[] splits = groupId.split(",|，");
            StringBuilder demandCarLevel = new StringBuilder();
            for (String split :
                    splits) {
                String[] split1 = split.split(":");
                String carLevel = split1[0];
                String price = split1[1];
                JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
                journeyPlanPriceInfo.setUseCarMode(CarConstant.USR_CARD_MODE_NET);
                journeyPlanPriceInfo.setSource(applyUseWithTravelDto.getSource());
                journeyPlanPriceInfo.setCreateTime(DateUtils.getNowDate());
                journeyPlanPriceInfo.setCreateBy(String.valueOf(userId));
                journeyPlanPriceInfo.setNodeId(journeyUserCarPower.getNodeId());
                journeyPlanPriceInfo.setJourneyId(journeyUserCarPower.getJourneyId());
                journeyPlanPriceInfo.setPrice(new BigDecimal(price));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parse = simpleDateFormat.parse(applyUseWithTravelDto.getCalculatePriceStartTime());
                String formatEnd = simpleDateFormat.format(parse.getTime() + (Integer.parseInt(applyUseWithTravelDto.getDuration()) * 60000));
                endAction = simpleDateFormat.parse(formatEnd);
                journeyPlanPriceInfo.setPlannedArrivalTime(endAction);
                journeyPlanPriceInfo.setPlannedDepartureTime(simpleDateFormat.parse(applyUseWithTravelDto.getCalculatePriceStartTime()));
                journeyPlanPriceInfo.setDuration(Integer.parseInt(applyUseWithTravelDto.getDuration()));
                journeyPlanPriceInfo.setPowerId(applyUseWithTravelDto.getTicketId());
                journeyPlanPriceInfo.setOrderId(orderInfo.getOrderId());
                EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
                enterpriseCarTypeInfo.setLevel(carLevel);
                List<EnterpriseCarTypeInfo> enterpriseCarTypeInfos = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
                if (enterpriseCarTypeInfos != null && enterpriseCarTypeInfos.size() > 0) {
                    EnterpriseCarTypeInfo enterpriseCarTypeInfo1 = enterpriseCarTypeInfos.get(0);
                    journeyPlanPriceInfo.setCarTypeId(enterpriseCarTypeInfo1.getCarTypeId());
                }
                journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
                demandCarLevel.append(carLevel + ",");
            }
            String s = demandCarLevel.toString();
            String substring = s.substring(0, s.lastIndexOf(","));
            applyUseWithTravelDto.setGroupId(substring);
        }
        //订单轨迹
        this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.INITIALIZING.getState(), String.valueOf(userId), null, null, null);
        this.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.WAITINGLIST.getState(), String.valueOf(userId), null, null, null);
        //订单地址表
        String startPoint = applyUseWithTravelDto.getStartPoint();
        String endPoint = applyUseWithTravelDto.getEndPoint();


        //自有车添加车型预估价格表时长里程数据
        if (applyUseWithTravelDto.getIsDispatch() == 1) {
            DirectionDto directionDto = thirdService.drivingRoute(startPoint, endPoint);
            if (directionDto == null || directionDto.getCount() == 0) {
                throw new Exception("获取时长和里程失败");
            }
            List<PathDto> paths = directionDto.getRoute().getPaths();
            int totalTime = 0;
            for (int i = 0; i < paths.size(); i++) {
                totalTime = totalTime + paths.get(i).getDuration();
            }
            totalTime = Math.round(totalTime / paths.size());
            JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
            journeyPlanPriceInfo.setUseCarMode(CarConstant.USR_CARD_MODE_HAVE);
            journeyPlanPriceInfo.setSource("高德");
            journeyPlanPriceInfo.setCreateTime(DateUtils.getNowDate());
            journeyPlanPriceInfo.setCreateBy(String.valueOf(userId));
            journeyPlanPriceInfo.setNodeId(journeyUserCarPower.getNodeId());
            journeyPlanPriceInfo.setJourneyId(journeyUserCarPower.getJourneyId());
            journeyPlanPriceInfo.setPrice(new BigDecimal(0));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = simpleDateFormat.parse(applyUseWithTravelDto.getBookingDate());
            String formatEnd = simpleDateFormat.format(parse.getTime() + (totalTime * 1000));
            endAction = simpleDateFormat.parse(formatEnd);
            journeyPlanPriceInfo.setPlannedArrivalTime(endAction);
            journeyPlanPriceInfo.setPlannedDepartureTime(simpleDateFormat.parse(applyUseWithTravelDto.getBookingDate()));
            journeyPlanPriceInfo.setDuration((int) TimeUnit.MINUTES.convert(totalTime, TimeUnit.SECONDS));
            journeyPlanPriceInfo.setPowerId(applyUseWithTravelDto.getTicketId());
            journeyPlanPriceInfo.setOrderId(orderInfo.getOrderId());
            String groupIds = regimeInfoService.queryCarModeLevel(orderInfo.getOrderId(), CarConstant.USR_CARD_MODE_HAVE);
            String[] splits = groupIds.split(",");
            for (String split :
                    splits) {
                EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
                enterpriseCarTypeInfo.setLevel(split);
                List<EnterpriseCarTypeInfo> enterpriseCarTypeInfos = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
                if (enterpriseCarTypeInfos != null && enterpriseCarTypeInfos.size() > 0) {
                    EnterpriseCarTypeInfo enterpriseCarTypeInfo1 = enterpriseCarTypeInfos.get(0);
                    journeyPlanPriceInfo.setCarTypeId(enterpriseCarTypeInfo1.getCarTypeId());
                }
                journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
            }
        }
        //订单地址表
        String[] start = startPoint.split("\\,");
        String[] end = endPoint.split("\\,");

        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(orderInfo.getOrderId());
        orderAddressInfo.setJourneyId(orderInfo.getJourneyId());
        orderAddressInfo.setNodeId(orderInfo.getNodeId());
        orderAddressInfo.setPowerId(orderInfo.getPowerId());
        orderAddressInfo.setUserId(orderInfo.getUserId() + "");
        orderAddressInfo.setCityPostalCode(applyUseWithTravelDto.getCityId());
        if (OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getPrState().equals(type)) {
            String depCode = applyUseWithTravelDto.getDepCode();
            String arrCode = applyUseWithTravelDto.getArrCode();
            orderAddressInfo.setIcaoCode(arrCode + "," + depCode);
        }
        orderAddressInfo.setCreateBy(userId + "");
        //起点
        orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(applyUseWithTravelDto.getBookingDate());
        orderAddressInfo.setActionTime(date);
        orderAddressInfo.setLongitude(Double.parseDouble(start[0]));
        orderAddressInfo.setLatitude(Double.parseDouble(start[1]));
        orderAddressInfo.setAddress(applyUseWithTravelDto.getStartAddr());
        orderAddressInfo.setAddressLong(applyUseWithTravelDto.getStarAddrLong());


        iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
        //终点
        orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
        orderAddressInfo.setActionTime(endAction);
        orderAddressInfo.setLongitude(Double.parseDouble(end[0]));
        orderAddressInfo.setLatitude(Double.parseDouble(end[1]));
        orderAddressInfo.setAddress(applyUseWithTravelDto.getEndAddr());
        orderAddressInfo.setAddressLong(applyUseWithTravelDto.getEndAddrLong());
        iOrderAddressInfoService.insertOrderAddressInfo(orderAddressInfo);
        //用车权限次数变化
        iJourneyUserCarPowerService.updatePowerSurplus(applyUseWithTravelDto.getTicketId(), 1);

        //走调度给调度员发短信
        if (applyUseWithTravelDto.getIsDispatch() == 1) {
            ismsBusiness.sendMessagePriTravelOrderSucc(orderInfo.getOrderId(), userId);
        }
        //如果调网约车进行参数校验和成功则下单
        if (applyUseWithTravelDto.getIsDispatch() == 2) {
            ((OrderInfoServiceImpl) AopContext.currentProxy()).platCallTaxiParamValid(orderInfo.getOrderId(), String.valueOf(userId), applyUseWithTravelDto.getGroupId());
        }
        return orderInfo.getOrderId();
    }

    @Override
    public List<ApplyDispatchVo> queryApplyDispatchList(ApplyDispatchQuery query) {
        List<ApplyDispatchVo> applyDispatchVoList = orderInfoMapper.queryApplyDispatchList(query);
        if (null != applyDispatchVoList && applyDispatchVoList.size() > 0) {
            for (ApplyDispatchVo applyDispatchVo : applyDispatchVoList) {
                //预计等待时长 转化为分钟
                String waitTimeLong = applyDispatchVo.getWaitTimeLong();
                if (StringUtil.isNotEmpty(waitTimeLong)) {
                    Integer waitTime = Integer.valueOf(waitTimeLong);
                    applyDispatchVo.setWaitTime(Long.valueOf(waitTime / (1000 * 60)));
                }

                Long orderId = applyDispatchVo.getOrderId();
                Long journeyId = applyDispatchVo.getJourneyId();
                //查询乘车人
                applyDispatchVo.setUseCarUser(journeyPassengerInfoMapper.getPeerPeople(journeyId));
                //查询同行人数
                applyDispatchVo.setPeerUserNum(journeyPassengerInfoMapper.queryPeerCount(journeyId));
                //查询出发时间地点    目的地时间地点
                DispatchOrderInfo dispatchOrderInfo = new DispatchOrderInfo();
                dispatchOrderInfo.setOrderId(orderId);
                buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
                applyDispatchVo.parseOrderStartAndEndSiteAndTime(dispatchOrderInfo);
                //转化状态
                applyDispatchVo.parseApplyDispatchStatus();
            }
        }
        return applyDispatchVoList;
    }


    @Override
    public Integer queryApplyDispatchListCount(ApplyDispatchQuery query) {
        return orderInfoMapper.queryApplyDispatchListCount(query);
    }

    @Override
    public List<ApplyDispatchVo> queryReassignmentDispatchList(ApplyDispatchQuery query) {
        List<ApplyDispatchVo> reassignmentDispatchList = orderInfoMapper.queryReassignmentDispatchList(query);
        if (null != reassignmentDispatchList && reassignmentDispatchList.size() > 0) {
            for (ApplyDispatchVo applyDispatchVo : reassignmentDispatchList) {
                //预计等待时长 转化为分钟
                String waitTimeLong = applyDispatchVo.getWaitTimeLong();
                if (StringUtil.isNotEmpty(waitTimeLong)) {
                    Integer waitTime = Integer.valueOf(waitTimeLong);
                    applyDispatchVo.setWaitTime(Long.valueOf(waitTime / (1000 * 60)));
                }
                Long orderId = applyDispatchVo.getOrderId();
                Long journeyId = applyDispatchVo.getJourneyId();
                //查询乘车人
                applyDispatchVo.setUseCarUser(journeyPassengerInfoMapper.getPeerPeople(journeyId));
                //查询同行人数
                applyDispatchVo.setPeerUserNum(journeyPassengerInfoMapper.queryPeerCount(journeyId));
                //查询出发时间地点    目的地时间地点
                DispatchOrderInfo dispatchOrderInfo = new DispatchOrderInfo();
                dispatchOrderInfo.setOrderId(orderId);
                buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
                applyDispatchVo.parseOrderStartAndEndSiteAndTime(dispatchOrderInfo);
                //查询订单最新的状态流转记录
                DispatchDriverInfo dispatchDriverInfo = orderStateTraceInfoMapper.queryReassignmentOrderStatus(orderId);
                applyDispatchVo.parseReassignmentDispatchStatus(dispatchDriverInfo.getState());
            }
        }
        return reassignmentDispatchList;
    }

    @Override
    public Integer queryReassignmentDispatchListCount(ApplyDispatchQuery query) {
        return orderInfoMapper.queryReassignmentDispatchListCount(query);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean rejectReassign(Long orderId, String rejectReason, Long optUserId) {

        OrderInfo order = orderInfoMapper.selectOrderInfoById(orderId);
        int state = Integer.parseInt(order.getState().substring(1));
        if (state >= Integer.parseInt(OrderState.ALREADYSENDING.getState().substring(1))) {
            throw new BaseException("此订单已派车不可驳回!");
        }
        // 生成订单状态流转记录
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setCreateBy(String.valueOf(optUserId));
        orderStateTraceInfo.setState(ResignOrderTraceState.DISAGREE.getState());
        orderStateTraceInfo.setOrderId(orderId);
        orderStateTraceInfo.setContent(rejectReason);
        iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        // 更改订单状态
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        orderInfo.setState(OrderState.REASSIGNREJECT.getState());
        orderInfo.setUpdateBy(String.valueOf(orderId));
        orderInfo.setUpdateTime(new Date());
        return updateOrderInfo(orderInfo) > 0;
    }


    @Override
    public List<OrderHistoryTraceDto> getOrderHistoryTrace(Long orderId) throws Exception {
        List<OrderHistoryTraceDto> orderHistoryTraceDtos = new ArrayList<>();
        OrderInfo orderInfo = orderInfoMapper.selectOrderStateById(orderId);
        if (orderInfo.getLabelState() != null) {
            if (!OrderState.carAuthorityJundgeOrderComplete().contains(orderInfo.getLabelState())) {
                return orderHistoryTraceDtos;
            }
        }
        String useCarMode = orderInfo.getUseCarMode();
        if (useCarMode.equals(CarConstant.USR_CARD_MODE_HAVE)) {
            DriverHeartbeatInfo driverHeartbeatInfo = new DriverHeartbeatInfo();
            driverHeartbeatInfo.setOrderId(orderId);
            driverHeartbeatInfo.setCreateTime(orderInfo.getUpdateTime());
            List<DriverHeartbeatInfo> driverHeartbeatInfos = driverHeartbeatInfoMapper.selectDriverHeartbeatInfoList(driverHeartbeatInfo);
            for (DriverHeartbeatInfo driverHeartbeatInfo1 : driverHeartbeatInfos) {
                OrderHistoryTraceDto orderHistoryTraceDto = new OrderHistoryTraceDto();
                //BeanUtils.copyProperties(driverHeartbeatInfo1,orderHistoryTraceDto);
                orderHistoryTraceDto.setOrderId(driverHeartbeatInfo1.getOrderId().toString());
                orderHistoryTraceDto.setLatitude(driverHeartbeatInfo1.getLatitude().toString());
                orderHistoryTraceDto.setLongitude(driverHeartbeatInfo1.getLongitude().toString());
                orderHistoryTraceDto.setCreateTime(driverHeartbeatInfo1.getCreateTime());
                orderHistoryTraceDtos.add(orderHistoryTraceDto);
            }
        } else if (useCarMode.equals(CarConstant.USR_CARD_MODE_NET)) {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("enterPriseOrderNo", orderId + "");
            paramsMap.put("enterpriseId", enterpriseId);
            paramsMap.put("licenseContent", licenseContent);
            String postForm = OkHttpUtil.postForm(apiUrl + "/service/orderTrack", paramsMap);
            JSONObject jsonObject = JSONObject.parseObject(postForm);
            if (jsonObject.getInteger("code") != ApiResponse.SUCCESS_CODE) {
                throw new Exception("获取轨迹失败");
            }
            String data = jsonObject.getString("data");
            if (data != null) {
                List<OrderTraceDto> resultList = JSONObject.parseArray(data, OrderTraceDto.class);
                if (resultList.size() > 0) {
                    for (OrderTraceDto orderTraceDto :
                            resultList) {
                        OrderHistoryTraceDto orderHistoryTraceDto = new OrderHistoryTraceDto();
                        orderHistoryTraceDto.setOrderId(orderId + "");
                        orderHistoryTraceDto.setLongitude(orderTraceDto.getX());
                        orderHistoryTraceDto.setLatitude(orderTraceDto.getY());
                        Date date = new Date(Long.parseLong(orderTraceDto.getPt()));
                        orderHistoryTraceDto.setCreateTime(date);
                        orderHistoryTraceDtos.add(orderHistoryTraceDto);
                    }
                }
            }
        } else {
            throw new Exception("用车方式有误");
        }
        return orderHistoryTraceDtos;
    }


    @Override
    public OrderCostDetailVO getOrderCost(Long orderId) {
        OrderCostDetailVO result = new OrderCostDetailVO();
        List<OtherCostVO> list = new ArrayList<>();
        List<OrderSettlingInfo> orderSettlingInfos = orderSettlingInfoMapper.selectOrderSettlingInfoList(new OrderSettlingInfo(orderId));
        OrderFeeDetailVO orderFeeDetailVO = null;
        if (!CollectionUtils.isEmpty(orderSettlingInfos)) {
            //TODO 生产记得放开
            OrderSettlingInfo orderSettlingInfo = orderSettlingInfos.get(0);
            String amountDetail = orderSettlingInfo.getAmountDetail();
            System.out.println(amountDetail);
            orderFeeDetailVO = new Gson().fromJson(amountDetail, OrderFeeDetailVO.class);
        }
        if (orderFeeDetailVO == null) {
            return null;
        }
        BeanUtils.copyProperties(orderFeeDetailVO, result);
        //基础套餐费：包含基础时长和基础里程，超出另外计费
        String basePrice = orderFeeDetailVO.getBasePrice();
        String includeMileage = orderFeeDetailVO.getIncludeMileage();
        String includeMinute = orderFeeDetailVO.getIncludeMinute();
        if (BigDecimal.ZERO.compareTo(new BigDecimal(basePrice).stripTrailingZeros()) < 0) {
            list.add(new OtherCostVO("起步价", basePrice, includeMileage, includeMinute, "含时长" + includeMinute + "分钟,含里程" + includeMileage + "公里"));
        }
        //超时长费：超出基础时长时计算，同时会区分高峰和平峰时段
        String overTimeNumTotal = orderFeeDetailVO.getOverTimeNumTotal();//超时长费:高峰时长费+平超时长费
        String hotDuration = orderFeeDetailVO.getHotDuration();//高峰时长
        String hotDurationFees = orderFeeDetailVO.getHotDurationFees();//高峰时长费
        String peakPriceTime = orderFeeDetailVO.getPeakPriceTime();//高峰单价(时长)
        String overTimeNum = orderFeeDetailVO.getOverTimeNum();//平超时长
        String overTimePrice = orderFeeDetailVO.getOverTimePrice();//平超时长费用
        String allTime = new BigDecimal(overTimeNum).add(new BigDecimal(hotDuration)).stripTrailingZeros().toPlainString();
        if (BigDecimal.ZERO.compareTo(new BigDecimal(overTimeNumTotal).stripTrailingZeros()) < 0) {
            list.add(new OtherCostVO("超时长费", overTimeNumTotal, "平峰时长:" + overTimeNum + "分钟,高峰时长:" + hotDuration + "分钟,累计超时时长:" + allTime + "分钟"));
        }
        //超里程费：超出基础里程时计算，同时会区分高峰和平峰时段
        String overMilageNumTotal = orderFeeDetailVO.getOverMilageNumTotal();//超里程费用:高峰里程费+平超里程费
        String overMilagePrice = orderFeeDetailVO.getOverMilagePrice();//平超里程费
        String overMilageNum = orderFeeDetailVO.getOverMilageNum();//平超里程数
        String hotMileageFees = orderFeeDetailVO.getHotMileageFees();//高峰里程费
        String hotMileage = orderFeeDetailVO.getHotMileage();//高峰里程
        String peakPrice = orderFeeDetailVO.getPeakPrice();//高峰里程单价
        String allMileage = new BigDecimal(hotMileage).add(new BigDecimal(overMilageNum)).stripTrailingZeros().toPlainString();
        if (BigDecimal.ZERO.compareTo(new BigDecimal(overMilageNumTotal).stripTrailingZeros()) < 0) {
            list.add(new OtherCostVO("超里程费", overMilageNumTotal, "平峰里程:" + overMilageNum + "公里,高峰里程:" + hotMileage + "公里,累计超时里程:" + allMileage + "公里"));
        }
        //夜间里程费：用车过程在设定为夜间服务时间的，加收夜间服务费。
        String nightDistancePrice = orderFeeDetailVO.getNightDistancePrice();
        String nightDistanceNum = orderFeeDetailVO.getNightDistanceNum();
        String nightPrice = orderFeeDetailVO.getNightPrice();
        if (BigDecimal.ZERO.compareTo(new BigDecimal(nightDistancePrice).stripTrailingZeros()) < 0) {
            list.add(new OtherCostVO("夜间时长费", nightDistancePrice, "夜间服务费（里程）单价:" + nightPrice + ",夜间服务里程" + nightDistanceNum + "公里"));
        }
        //夜间时长费：用车过程在设定为夜间服务时间的，加收夜间服务费。
        String nighitDurationFees = orderFeeDetailVO.getNighitDurationFees();
        String nighitDuration = orderFeeDetailVO.getNighitDuration();
        String nightPriceTime = orderFeeDetailVO.getNightPriceTime();
        if (BigDecimal.ZERO.compareTo(new BigDecimal(nighitDurationFees).stripTrailingZeros()) < 0) {
            list.add(new OtherCostVO("夜间时长费", nighitDurationFees, "夜间服务费（时长）单价:" + nightPriceTime + ",夜间服务时长" + nighitDuration + "分钟"));
        }
        //等待费
        String waitingFee = orderFeeDetailVO.getWaitingFee();
        String waitingMinutes = orderFeeDetailVO.getWaitingMinutes();
        if (BigDecimal.ZERO.compareTo(new BigDecimal(waitingFee).stripTrailingZeros()) < 0) {
            list.add(new OtherCostVO("等待费", waitingFee, "等待时长为" + waitingMinutes + "分钟"));
        }
        //长途费：超过长途费起始里程时计算
        String longDistancePrice = orderFeeDetailVO.getLongDistancePrice();
        String longDistanceNum = orderFeeDetailVO.getLongDistanceNum();
        String longPrice = orderFeeDetailVO.getLongPrice();
        if (BigDecimal.ZERO.compareTo(new BigDecimal(longDistancePrice).stripTrailingZeros()) < 0) {
            list.add(new OtherCostVO("长途费", longDistancePrice, "长途单价:" + longPrice + ",长途里程" + longDistanceNum + "公里"));
        }
        //价外税:如高速费和停车费
        List<OtherCostBean> otherCostBeans = orderFeeDetailVO.getOtherCost();
        Double otherFee = 0.0;
        if (!CollectionUtils.isEmpty(otherCostBeans)) {
            otherFee = otherCostBeans.stream().map(OtherCostBean::getCostFee).collect(Collectors.reducing(Double::sum)).get();
        }
        if (BigDecimal.ZERO.compareTo(BigDecimal.valueOf(otherFee)) < 0) {
            list.add(new OtherCostVO("其他费用", String.valueOf(otherFee), "包含停车费、高速费、机场服务费"));
        }
        result.setOtherCost(list);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, Long userId, String cancelReason) throws Exception {
        log.info("取消订单入参-----》orderId:{},userId:{},cancelReason:{}", orderId, userId, cancelReason);
        OrderInfo orderInfoOld = orderInfoMapper.selectOrderInfoById(orderId);
        Double cancelFee = 0d;
        if (orderInfoOld == null) {
            throw new Exception("未查询到订单号【" + orderId + "】对应的订单信息");
        }
        String useCarMode = orderInfoOld.getUseCarMode();
        String state = orderInfoOld.getState();
        //消息发送使用
        Long driverId = orderInfoOld.getDriverId();
        //状态为约到车未服务的状态，用车方式为网约车，调用三方取消订单接口
        if (useCarMode != null && useCarMode.equals(CarConstant.USR_CARD_MODE_NET)) {
            //TODO 调用网约车的取消订单接口
            List<String> macList = MacTools.getMacList();
            String macAdd = macList.get(0);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("enterpriseId", enterpriseId);
            paramMap.put("enterpriseOrderId", String.valueOf(orderId));
            paramMap.put("licenseContent", licenseContent);
            paramMap.put("mac", macAdd);
            paramMap.put("reason", cancelReason);
            log.info("网约车订单{}取消参数{}", orderId, paramMap);
            String result = OkHttpUtil.postForm(apiUrl + "/service/cancelOrder", paramMap);
            log.info("网约车订单{}取消返回结果{}", orderId, result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (ApiResponse.SUCCESS_CODE != jsonObject.getInteger("code")) {
                throw new Exception("调用三方取消订单服务-》取消失败");
            } else {
                JSONObject data = jsonObject.getJSONObject("data");
                cancelFee = data.getDouble("cancelFee");
            }
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setState(OrderState.ORDERCLOSE.getState());
        orderInfo.setUpdateBy(String.valueOf(userId));
        orderInfo.setOrderId(orderId);
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        int suc = orderInfoMapper.updateOrderInfo(orderInfo);
        //自有车，且状态变更成功
        if (suc == 1 && useCarMode != null && useCarMode.equals(CarConstant.USR_CARD_MODE_HAVE) && !state.equals(OrderState.WAITINGLIST.getState())) {
            //TODO 调用消息通知接口，给司机发送乘客取消订单的消息
            ismsBusiness.sendMessageCancelOrder(orderId, userId);
        }
        //取消订单短信发送
        if (cancelFee == 0d) {
            ismsBusiness.sendSmsCancelOrder(orderId);
        } else {
            ismsBusiness.sendSmsCancelOrderHaveFee(orderId, cancelFee);
        }

        //插入订单轨迹表
        this.insertOrderStateTrace(String.valueOf(orderId), OrderStateTrace.CANCEL.getState(), String.valueOf(userId), cancelReason, null, null);
        //用车权限次数做变化
        iJourneyUserCarPowerService.updatePowerSurplus(orderInfoOld.getPowerId(), 2);
    }


    /**
     * 改派订单
     *
     * @param orderNo
     * @param rejectReason
     * @param status
     * @param userId
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reassign(String orderNo, String rejectReason, String status, Long userId, Long oldDriverId, Long oldCarId) throws Exception {
        if ("1".equals(status)) {
            OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
            orderStateTraceInfo.setCreateBy(String.valueOf(userId));
            orderStateTraceInfo.setState(ResignOrderTraceState.AGREE.getState());
            orderStateTraceInfo.setOrderId(Long.parseLong(orderNo));
            orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
            orderStateTraceInfo.setOldCarId(oldCarId);
            orderStateTraceInfo.setOldDriverId(oldDriverId);
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
            //改派订单消息通知
//            ismsBusiness.sendMessageReassignSucc(Long.parseLong(orderNo),userId);
        } else if ("2".equals(status)) {
            OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
            orderStateTraceInfo.setCreateBy(String.valueOf(userId));
            orderStateTraceInfo.setState(ResignOrderTraceState.DISAGREE.getState());
            orderStateTraceInfo.setOrderId(Long.parseLong(orderNo));
            orderStateTraceInfo.setContent(rejectReason);
            orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
        } else {
            throw new Exception("操作异常");
        }
    }

    /**
     * 获取网约车状态
     */
    @Override
    public OrderStateVO getTaxiState(OrderStateVO orderVO, Long orderNo) throws Exception {
        log.info("订单号:" + orderNo + "状态详情:" + orderVO.toString());
        List<String> states = Arrays.asList(OrderState.INITIALIZING.getState(), OrderState.WAITINGLIST.getState(), OrderState.GETARIDE.getState(),
                OrderState.SENDINGCARS.getState(), OrderState.ORDERCLOSE.getState(), OrderState.STOPSERVICE.getState());
        OrderPayInfo orderPayInfo = orderPayInfoMapper.getOrderPayInfo(orderNo);
        String payState = OrderPayConstant.UNPAID;
        if (orderPayInfo != null) {
            payState = orderPayInfo.getState();
        }
        orderVO.setPayState(payState);
        if (states.contains(orderVO.getState())) {
            return orderVO;
        }
        JSONObject thirdPartyOrderState = this.getThirdPartyOrderState(orderNo);
        log.info("轮询获取网约车" + orderNo + "订单详情:" + thirdPartyOrderState);
        Double longitude = null;
        Double latitude = null;
        String status = thirdPartyOrderState.getString("status");
        String lableState = thirdPartyOrderState.getString("status");
        String json = thirdPartyOrderState.getString("driverInfo");
        DriverCloudDto driverCloudDto = new DriverCloudDto();
        if (StringUtils.isNotEmpty(json)) {
            driverCloudDto = JSONObject.parseObject(json, DriverCloudDto.class);
            String driverPoint = driverCloudDto.getDriverPoint();
            if (StringUtils.isNotEmpty(driverPoint)) {
                String[] split = driverPoint.split(",");
                longitude = Double.parseDouble(split[0]);
                latitude = Double.parseDouble(split[1]);
            }
        }
        orderVO.setDriverLongitude(String.valueOf(longitude));
//        orderVO.setState(status);
//        orderVO.setLabelState(lableState);
        orderVO.setDriverLatitude(String.valueOf(latitude));

        return orderVO;
    }


    @Override
    public DispatchSendCarPageInfo getDispatchSendCarPageInfo(Long orderId) {
        DispatchSendCarPageInfo dispatchSendCarPageInfo = new DispatchSendCarPageInfo();
        //查询对应制度里面的网约车车型
        regimeInfoService.queryCarModeLevel(orderId, CarConstant.USR_CARD_MODE_NET);
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        if (null != orderInfo) {
            JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
            if (null != journeyInfo) {
                dispatchSendCarPageInfo.setServiceType(journeyInfo.getServiceType());
                dispatchSendCarPageInfo.setUseCarMode(journeyInfo.getUseCarMode());
                dispatchSendCarPageInfo.setItIsReturn(journeyInfo.getItIsReturn());
                dispatchSendCarPageInfo.setCharterCarDaysCount(journeyInfo.getUseTime());
                if(StringUtils.isNotEmpty(journeyInfo.getOldUseTime())){
                    dispatchSendCarPageInfo.setOldUseTime(journeyInfo.getOldUseTime());
                }
                Long applyUserId = journeyInfo.getUserId();
                if (null != applyUserId) {
                    //申请人手机名字
                    EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(applyUserId);
                    dispatchSendCarPageInfo.setApplyUserMobile(ecmpUser.getPhonenumber());
                    dispatchSendCarPageInfo.setApplyUserName(ecmpUser.getNickName());
                }
                //查询乘车人和同行人
                String peerPeople = journeyPassengerInfoMapper.getPeerPeople(journeyInfo.getJourneyId());
                dispatchSendCarPageInfo.setUseCarUser(peerPeople);
                List<String> peerUserList = journeyPassengerInfoMapper.queryPeerUserNameList(journeyInfo.getJourneyId());
                dispatchSendCarPageInfo.setPeerUserList(peerUserList);
            }
            //查询上下车地点 时间
            DispatchOrderInfo dispatchOrderInfo = new DispatchOrderInfo();
            dispatchOrderInfo.setOrderId(orderId);
            dispatchOrderInfo.setJourneyId(journeyInfo.getJourneyId());
            buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
            dispatchSendCarPageInfo.setSetOutAderess(dispatchOrderInfo.getStartSite());
            dispatchSendCarPageInfo.setStartDate(dispatchOrderInfo.getUseCarDate());
            dispatchSendCarPageInfo.setArriveAdress(dispatchOrderInfo.getEndSite());
            dispatchSendCarPageInfo.setEndDate(dispatchOrderInfo.getEndDate());
            //查询用车城市
            OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
            orderAddressInfo.setOrderId(orderId);
            List<OrderAddressInfo> selectOrderAddressInfoList = iOrderAddressInfoService.selectOrderAddressInfoList(orderAddressInfo);
            if (null != selectOrderAddressInfoList && selectOrderAddressInfoList.size() > 0) {
                for (OrderAddressInfo addressInfo : selectOrderAddressInfoList) {
                    String cityPostalCode = addressInfo.getCityPostalCode();
                    String type = addressInfo.getType();
                    if (StringUtils.isNotEmpty(cityPostalCode) && StringUtils.isNotEmpty(type)) {
                        CityInfo cityInfo = chinaCityMapper.queryCityByCityCode(cityPostalCode);

                        if (null != cityInfo) {
                            if ("A000".equals(type)) {
                                //出发地城市
                                dispatchSendCarPageInfo.setCityName(cityInfo.getCityName());
                                dispatchSendCarPageInfo.setStartCity(cityInfo.getCityFullName());
                            }
                            if ("A999".equals(type)) {
                                //目的地城市
                                dispatchSendCarPageInfo.setEndCity(cityInfo.getCityFullName());
                            }
                        }
                    }
                }
            }

        }
        return dispatchSendCarPageInfo;
    }

    @Override
    public DispatchSendCarPageInfo getUserDispatchedOrder(Long orderId) {
        DispatchSendCarPageInfo dispatchSendCarPageInfo = getDispatchSendCarPageInfo(orderId);
        //查询派车备注
        String dispatchRemark=orderDispatcheDetailInfoMapper.selectDispatchRemark(orderId);
        dispatchSendCarPageInfo.setDispatchRemark(dispatchRemark);

        if (iOrderStateTraceInfoService.isReassignment(orderId)) {
            //是改派过的单子  则查询原有调度信息
            DispatchOptRecord oldDispatchOptRecord = new DispatchOptRecord();
            oldDispatchOptRecord.setUseCarModel(CarConstant.USR_CARD_MODE_HAVE);
            DispatchDriverInfo dispatchDriverInfo = iOrderStateTraceInfoService.queryReassignmentOrderInfo(orderId);
            if (null != dispatchDriverInfo) {
                oldDispatchOptRecord.setCarLicense(dispatchDriverInfo.getCarLicense());
                oldDispatchOptRecord.setCarType(dispatchDriverInfo.getCarType());
                oldDispatchOptRecord.setDriverMobile(dispatchDriverInfo.getDriverTel());
                oldDispatchOptRecord.setDriverName(dispatchDriverInfo.getDriverName());
            }
            //查询上次调度的时候调度员信息和时间
            OrderStateTraceInfo orderStateTraceInfo = iOrderStateTraceInfoService.queryFirstDispatchIndo(orderId);
            if (null != orderStateTraceInfo) {
                oldDispatchOptRecord.setDispatchDate(orderStateTraceInfo.getCreateTime());
                EcmpUser user = ecmpUserMapper.selectEcmpUserById(Long.valueOf(orderStateTraceInfo.getCreateBy()));
                if (null != user) {
                    oldDispatchOptRecord.setDispatchMobile(user.getPhonenumber());
                    oldDispatchOptRecord.setDispatchName(user.getNickName());
                }
            }
            dispatchSendCarPageInfo.setOldDispatchOptRecord(oldDispatchOptRecord);
        }
        //查询当前调度的信息
        DispatchOptRecord currentDispatchOptRecord = new DispatchOptRecord();
        OrderStateTraceInfo currentOrderStateTraceInfo = iOrderStateTraceInfoService.queryRecentlyDispatchInfo(orderId);
        if (null != currentOrderStateTraceInfo) {
            currentDispatchOptRecord.setDispatchDate(currentOrderStateTraceInfo.getCreateTime());
            //调度员信息
            EcmpUser currentUser = ecmpUserMapper.selectEcmpUserById(Long.valueOf(currentOrderStateTraceInfo.getCreateBy()));
            if (null != currentUser) {
                currentDispatchOptRecord.setDispatchMobile(currentUser.getPhonenumber());
                currentDispatchOptRecord.setDispatchName(currentUser.getNickName());
            }
            if (OrderStateTrace.TURNREASSIGNMENT.equals(currentOrderStateTraceInfo.getState())) {
                //审核驳回
                currentDispatchOptRecord.setRejectReason(currentOrderStateTraceInfo.getContent());
            } else {
                //派车或者改派通过
                OrderInfo currentOrder = orderInfoMapper.selectOrderInfoById(orderId);
                if (null != currentOrder) {
                    if (CarConstant.USR_CARD_MODE_HAVE.equals(currentOrder.getUseCarMode())) {
                        DriverInfo driverInfo = driverInfoMapper.selectDriverInfoById(currentOrder.getDriverId());
                        if (driverInfo != null) {
                            currentDispatchOptRecord.setDriverMobile(driverInfo.getMobile());
                            currentDispatchOptRecord.setDriverName(driverInfo.getDriverName());
                        }
                        CarInfo carInfo = carInfoMapper.selectCarInfoById(currentOrder.getCarId());
                        if (carInfo != null) {
                            currentDispatchOptRecord.setCarType(carInfo.getCarType());
                            currentDispatchOptRecord.setCarLicense(carInfo.getCarLicense());
                        }
                    }
                    currentDispatchOptRecord.setUseCarModel(currentOrder.getUseCarMode());
                }
            }
        }
        dispatchSendCarPageInfo.setCurrentDispatchOptRecord(currentDispatchOptRecord);
        return dispatchSendCarPageInfo;
    }


    @Override
    public boolean sendCarBeforeCreatePlanPrice(Long orderId, Long userId) throws Exception {
        //生成行程预估价格记录
        List<CarLevelAndPriceReVo> carlevelAndPriceByOrderId = regimeInfoService.getCarlevelAndPriceByOrderId(orderId, CarConstant.USR_CARD_MODE_HAVE);
        OrderInfo orderInfo1 = orderInfoMapper.selectOrderInfoById(orderId);
        if (carlevelAndPriceByOrderId != null && carlevelAndPriceByOrderId.size() > 0) {
            for (CarLevelAndPriceReVo carLevelAndPriceReVo :
                    carlevelAndPriceByOrderId) {
                JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
                journeyPlanPriceInfo.setCreateTime(DateUtils.getNowDate());
                journeyPlanPriceInfo.setCreateBy(userId.toString());
                journeyPlanPriceInfo.setOrderId(orderId);
                journeyPlanPriceInfo.setPowerId(orderInfo1.getPowerId());
                journeyPlanPriceInfo.setSource(carLevelAndPriceReVo.getSource());
                journeyPlanPriceInfo.setNodeId(orderInfo1.getNodeId());
                journeyPlanPriceInfo.setJourneyId(orderInfo1.getJourneyId());
                journeyPlanPriceInfo.setPrice(new BigDecimal(carLevelAndPriceReVo.getEstimatePrice()));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date parse = carLevelAndPriceReVo.getBookingStartTime();
                String formatEnd = simpleDateFormat.format(parse.getTime() + (carLevelAndPriceReVo.getDuration() * 60000));
                journeyPlanPriceInfo.setPlannedArrivalTime(simpleDateFormat.parse(formatEnd));
                journeyPlanPriceInfo.setPlannedDepartureTime(parse);
                journeyPlanPriceInfo.setDuration(carLevelAndPriceReVo.getDuration());
                EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
                enterpriseCarTypeInfo.setLevel(carLevelAndPriceReVo.getOnlineCarLevel());
                List<EnterpriseCarTypeInfo> enterpriseCarTypeInfos = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
                if (enterpriseCarTypeInfos != null && enterpriseCarTypeInfos.size() > 0) {
                    EnterpriseCarTypeInfo enterpriseCarTypeInfo1 = enterpriseCarTypeInfos.get(0);
                    journeyPlanPriceInfo.setCarTypeId(enterpriseCarTypeInfo1.getCarTypeId());
                }
                journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
            }
        }
        return true;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void checkCreateReturnAuthority(Long orderId, Long optUserId) throws Exception {
        DispatchOrderInfo waitDispatchOrderDetailInfo = orderInfoMapper.getWaitDispatchOrderDetailInfo(orderId);
        //判断是公务还是差旅
        Long regimenId = waitDispatchOrderDetailInfo.getRegimenId();
        if (null == regimenId) {
            return;
        }
        RegimeInfo regimeInfo = regimeInfoService.selectRegimeInfoById(regimenId);
        if (null == regimeInfo) {
            return;
        }
        String regimenType = regimeInfo.getRegimenType();
        if (CarConstant.USE_CAR_TYPE_TRAVEL.equals(regimenType)) {
            return;
        }
        String itIsReturn = waitDispatchOrderDetailInfo.getItIsReturn();
        if ("N444".equals(itIsReturn)) {
            //没有往返
            return;
        }
        String waitTimeStr = waitDispatchOrderDetailInfo.getWaitTimeLong();
        if (StringUtil.isEmpty(waitTimeStr)) {
            //往返没有设置预估等待时长
            log.info("订单【" + orderId + "对应的行程往返时没有设置预估等待时长】");
            return;
        }
        Long waitTimeLong = Long.valueOf(waitTimeStr);
        String minStr = (waitTimeLong / (1000 * 60)) + "";
        if (!ecmpConfigService.checkUpWaitMaxMinute(Long.valueOf(minStr))) {
            //企业设置中没开启等待时长或者大于预估等待时长
            return;
        }
        //生成一条新返回的的公务用车权限
        List<JourneyNodeInfo> journeyNodeInfoList = journeyNodeInfoMapper.queryJourneyNodeInfoOrderByNumber(waitDispatchOrderDetailInfo.getJourneyId());
        //取返程的行程节点
        Long nodeId = journeyNodeInfoList.get(journeyNodeInfoList.size() - 1).getNodeId();
        JourneyUserCarPower journeyUserCarPower = new JourneyUserCarPower(waitDispatchOrderDetailInfo.getApplyId(), waitDispatchOrderDetailInfo.getJourneyId(), new Date(), optUserId, CarConstant.NOT_USER_USE_CAR, CarConstant.BACK_TRACKING, nodeId);
        journeyUserCarPowerMapper.insertJourneyUserCarPower(journeyUserCarPower);
        //生成订单
        OfficialOrderReVo officialOrderReVo = new OfficialOrderReVo();
        officialOrderReVo.setPowerId(journeyUserCarPower.getPowerId());
        officialOrderReVo.setIsDispatch(2);
        officialOrder(officialOrderReVo, optUserId);
    }


    /**
     * 过12小时自动确认行程
     */
    @Override
    public void confirmOrderJourneyAuto(int timeout) {
        List<OrderStateTraceInfo> expiredConfirmOrder = orderStateTraceInfoMapper.getExpiredConfirmOrder(timeout);
        if (expiredConfirmOrder != null && expiredConfirmOrder.size() > 0) {
            for (OrderStateTraceInfo orderStateTraceInfo :
                    expiredConfirmOrder) {
                this.insertOrderStateTrace(String.valueOf(orderStateTraceInfo.getOrderId()), OrderStateTrace.ORDERCLOSE.getState(), "1", "超时自动确认", null, null);
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderId(orderStateTraceInfo.getOrderId());
                orderInfo.setState(OrderState.ORDERCLOSE.getState());
                orderInfo.setUpdateBy("1");
                orderInfo.setUpdateTime(DateUtils.getNowDate());
                orderInfoMapper.updateOrderInfo(orderInfo);
            }
        }
    }


    /**
     * 调度信息包装
     * 添加任务来源信息，用车场景名称和用车制度名称
     *
     * @return
     */
    private void DispatchOrderInfoPacking(DispatchOrderInfo dispatchOrderInfo) {
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(dispatchOrderInfo.getOrderId());
        if (orderInfo != null) {
            JourneyUserCarPower journeyUserCarPower = journeyUserCarPowerMapper.selectJourneyUserCarPowerById(orderInfo.getPowerId());
            if (journeyUserCarPower != null) {
                ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(journeyUserCarPower.getApplyId());
                if (applyInfo != null) {
                    EcmpUserInfoDto ecmpUserInfoDto = ecmpOrgService.getUserLatestDeptInfoByUserId(Long.parseLong(applyInfo.getCreateBy()));
                    if (ecmpUserInfoDto.getUserDeptInfo() != null) {
                        dispatchOrderInfo.setDeptName(ecmpUserInfoDto.getUserDeptInfo().getDeptName());
                    }
                    if (ecmpUserInfoDto.getUserCompanyInfo() != null) {
                        dispatchOrderInfo.setCompanyName(ecmpUserInfoDto.getUserCompanyInfo().getDeptName());
                    }
                }
            }
            JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
            if (journeyInfo != null) {
                RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(journeyInfo.getRegimenId());
                if (regimeInfo != null) {
                    dispatchOrderInfo.setUserCarRegime(regimeInfo.getName());
                }
                SceneInfo sceneInfo = sceneInfoMapper.querySceneByRegimeId(journeyInfo.getRegimenId());
                if (sceneInfo != null) {
                    dispatchOrderInfo.setUserCarScene(sceneInfo.getName());
                }
            }
        }
    }

    /***
     * 获取乘车信息 ad by liuzb
     * @param orderId
     * @return
     * @throws Exception
     */
    @Override
    public OrderInfoMessage getMessage(Long orderId) throws Exception {
        if (null == orderId) {
            return null;
        }
        OrderInfoMessage data = orderInfoMapper.getCarMessage(orderId);
        return data;
    }


    private OrderVO checkIsOverMoney(OrderInfo orderInfo, OrderVO vo, int flag) throws Exception {
        List<OrderSettlingInfo> orderSettlingInfos = orderSettlingInfoMapper.selectOrderSettlingInfoList(new OrderSettlingInfo(orderInfo.getOrderId()));
        if (flag == 1) {//正常结单
            if (!CollectionUtils.isEmpty(orderSettlingInfos)) {
                OrderSettlingInfo orderSettlingInfo = orderSettlingInfos.get(0);
                vo.setAmount(orderSettlingInfo.getAmount().stripTrailingZeros().toPlainString());
            }
        } else {//取消结单
            if (!CollectionUtils.isEmpty(orderSettlingInfos)) {
                String personalAmount = "";
                String ownerAmount = "";
                OrderSettlingInfo orderSettlingInfo = orderSettlingInfos.get(0);
                CancelOrderCostVO cancelOrderCostVO = new CancelOrderCostVO();
                int isPayFee = 0;
                cancelOrderCostVO.setCancelAmount(orderSettlingInfo.getAmount().stripTrailingZeros().toPlainString());
                String amountDetail = orderSettlingInfo.getAmountDetail();
                if (StringUtils.isNotEmpty(amountDetail)) {
                    JSONObject parse = JSONObject.parseObject(amountDetail);
                    String otherCost = parse.getString("otherCost");
                    if (StringUtils.isNotEmpty(otherCost)) {
                        List<OtherCostBean> otherCostBeans = JSONObject.parseArray(otherCost, OtherCostBean.class);
                        for (OtherCostBean bean : otherCostBeans) {
                            if ("personalAmount".equals(bean.getTypeName())) {
                                personalAmount = bean.getCost();
                            } else if ("ownerAmount".equals(bean.getTypeName())) {
                                ownerAmount = bean.getCost();
                            }
                        }
                    }
                }
                cancelOrderCostVO.setOwnerAmount(ownerAmount);
                if (StringUtils.isNotEmpty(personalAmount) && new BigDecimal(personalAmount).compareTo(BigDecimal.ZERO) == 1) {
                    isPayFee = 1;
                }
                cancelOrderCostVO.setIsPayFee(isPayFee);
                cancelOrderCostVO.setPersonalAmount(personalAmount);
                vo.setCancelFee(cancelOrderCostVO);
            }
        }
        OrderPayInfo orderPayInfo = orderPayInfoMapper.getOrderPayInfo(orderInfo.getOrderId());
        if (orderPayInfo != null) {
            vo.setPayId(orderPayInfo.getPayId());
            vo.setPayState(orderPayInfo.getState());
            vo.setIsExcess(ONE);
            vo.setExcessMoney(orderPayInfo.getAmount().stripTrailingZeros().toPlainString());
        } else {
            vo.setIsExcess(ZERO);
            vo.setExcessMoney(String.valueOf(ZERO));
        }
        if (CarConstant.USR_CARD_MODE_NET.equals(orderInfo.getUseCarMode())) {
            List<ThridCarTypeVo> onlienCarType = thirdService.getOnlienCarType();
            if (!CollectionUtils.isEmpty(onlienCarType)) {
                String demandCarLevel = orderInfo.getDemandCarLevel();
                String carPhoto = onlienCarType.stream().filter(p -> p.getName().equals(demandCarLevel)).map(ThridCarTypeVo::getRemark).collect(Collectors.joining());
                vo.setCarPhoto(carPhoto);
            }
        }
        return vo;
    }


    /**
     * 查询所有处于待派单(未改派)的订单及关联的信息
     *
     * @param userId
     * @param companyId
     * @return
     */
    public List<DispatchOrderInfo> queryAllWaitingList(Long userId, Long companyId) {
        List<DispatchOrderInfo> result = new ArrayList<DispatchOrderInfo>();
        //查询所有处于待派单(未改派)的订单及关联的信息
        OrderInfo query = new OrderInfo();
        query.setState(OrderState.WAITINGLIST.getState());
        query.setCompanyId(companyId);
        List<DispatchOrderInfo> waitDispatchOrder = orderInfoMapper.queryOrderRelateInfo(query);
        if (!waitDispatchOrder.isEmpty()) {
            //对用的前端状态都为待派车 -S200
            for (DispatchOrderInfo dispatchOrderInfo : waitDispatchOrder) {
                //去除改派的单子
                if (iOrderStateTraceInfoService.isReassignment(dispatchOrderInfo.getOrderId())) {
                    continue;
                }
                //去除超时的订单
                /*if(iOrderAddressInfoService.checkOrderOverTime(dispatchOrderInfo.getOrderId())){
                    continue;
                }*/
                //正常申请的调度单取对应的用车    申请单的通过时间来排序
                dispatchOrderInfo.setUpdateDate(dispatchOrderInfo.getApplyPassDate());
                dispatchOrderInfo.setState(OrderState.WAITINGLIST.getState());
                result.add(dispatchOrderInfo);
            }
        }
        return result;
    }

    /**
     * 查询所有处于待改派(订单状态为已派车,已发起改派申请)的订单及关联的信息
     *
     * @param userId
     * @param companyId
     * @return
     */
    public List<DispatchOrderInfo> queryAllReformistList(Long userId, Long companyId) {
        List<DispatchOrderInfo> result = new ArrayList<DispatchOrderInfo>();
        //查询所有处于待改派(订单状态为已派车,已发起改派申请)的订单及关联的信息
        OrderInfo query = new OrderInfo();
        query.setState(OrderState.WAITINGLIST.getState());
        query.setCompanyId(companyId);
        List<DispatchOrderInfo> reassignmentOrder = orderInfoMapper.queryOrderRelateInfo(query);
        if (null != reassignmentOrder && reassignmentOrder.size() > 0) {
            for (DispatchOrderInfo dispatchOrderInfo : reassignmentOrder) {
                dispatchOrderInfo.setState(OrderState.APPLYREASSIGN.getState());
                //去除超时的订单
                if (iOrderAddressInfoService.checkOrderOverTime(dispatchOrderInfo.getOrderId())) {
                    continue;
                }
                //去除最新订单流转状态不是S270 申请改派的
                OrderStateTraceInfo orderStateTraceInfo = orderStateTraceInfoMapper.getLatestInfoByOrderId(dispatchOrderInfo.getOrderId());
                if (!OrderStateTrace.APPLYREASSIGNMENT.getState().equals(orderStateTraceInfo.getState())) {
                    continue;
                }
                Long oldDispatcher = orderStateTraceInfoMapper.getOldDispatcher(dispatchOrderInfo.getOrderId());
                if (oldDispatcher != null) {
                    if (oldDispatcher != 1) {
                        if (userId.longValue() != oldDispatcher.longValue()) {
                            continue;
                        }
                    }
                }
                result.add(dispatchOrderInfo);
            }

        }
        return result;
    }


    /**
     * 对用户进行订单可见校验
     * 自有车+网约车时，且上车地点在车队的用车城市范围内，只有该车队的调度员能看到该订单
     * 只有自有车时，且上车地点不在车队的用车城市范围内，则所有车车队的所有调度员都能看到该订单
     *
     * @param userId
     * @return
     */
    public List<DispatchOrderInfo> queryDispatchList(List<DispatchOrderInfo> result, Long userId) {
		/*对用户进行订单可见校验
		自有车+网约车时，且上车地点在车队的用车城市范围内，只有该车队的调度员能看到该订单
	      只有自有车时，且上车地点不在车队的用车城市范围内，则所有车车队的所有调度员都能看到该订单*/
        List<DispatchOrderInfo> checkResult = new ArrayList<DispatchOrderInfo>();
        if (!result.isEmpty()) {
            for (DispatchOrderInfo dispatchOrderInfo : result) {
                //计算该订单的等待时长 分钟
                if (null != dispatchOrderInfo.getCreateTime()) {
                    dispatchOrderInfo.setWaitMinute(DateFormatUtils.getDateToWaitInterval(dispatchOrderInfo.getCreateTime()));
                }
                //查询订单对应的上车地点时间,下车地点时间
                buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
                //查询订单对应制度的可用用车方式
                Long regimenId = dispatchOrderInfo.getRegimenId();
                if (null == regimenId || !regimeInfoService.findOwnCar(regimenId)) {
                    log.error("该订单相关的制度id是空，或者相关制度不包含自有车");
                    continue;
                }
                //查询订单对应的上车地点时间,下车地点时间
                buildOrderStartAndEndSiteAndTime(dispatchOrderInfo);
                String cityId = dispatchOrderInfo.getCityId();
                if (StringUtil.isEmpty(cityId)) {
                    log.error("该订单相关城市为空");
                    continue;
                }
                //判断订单是否可以被当前调度员看到
                if (!judgeOrderValid(userId, dispatchOrderInfo)) {
                    continue;
                }
                //计算该订单的等待时长 分钟
                if (null != dispatchOrderInfo.getCreateTime()) {
                    dispatchOrderInfo.setWaitMinute(DateFormatUtils.getDateToWaitInterval(dispatchOrderInfo.getCreateTime()));
                }

                //订单添加用车场景用车制度以及任务来源信息
                DispatchOrderInfoPacking(dispatchOrderInfo);
                checkResult.add(dispatchOrderInfo);
            }
        }
        return checkResult;
    }

    /***
     * 处理是否当前自己调度的订单
     * add by liuzb
     * @param list
     * @param user
     * @return
     */
    private List<OrderListBackDto> addState(List<OrderListBackDto> list, LoginUser user) {
        if (null != list && list.size() > 0) {
            for (OrderListBackDto data : list) {
                data.setFlag(false);
                List<String> tList = carGroupInfoMapper.getTakeBack(user.getUser().getUserId(), Long.valueOf(data.getOrderId()));
                data.setTakeBack(null != tList && tList.size() > 0 ? true : false);
                OrderServiceCostDetailRecordInfo orderData = new OrderServiceCostDetailRecordInfo();
                orderData.setOrderId(Long.valueOf(data.getOrderId()));
                List<OrderServiceCostDetailRecordInfo> orderList = orderServiceCostDetailRecordInfoMapper.queryAll(orderData);
                if (null != orderList && orderList.size() > 0) {
                    for (OrderServiceCostDetailRecordInfo key : orderList) {
                        if (String.valueOf(user.getUser().getUserId()).equals(String.valueOf(key.getCreateBy()))) {
                            data.setFlag(true);
                            continue;
                        }
                    }

                }
            }
        }
        return list;
    }


    /***
     * 区分角色（内部调度员，外部调度员，员工）
     * add by liuzb
     * @param user
     * @return
     */
    private String isDispatcher(LoginUser user) {
        if (isAdmin(user)) {
            return "admin";
        }
        List<SysRole> roleList = user.getUser().getRoles();
        for (SysRole data : roleList) {
            /**调度员（区分内部调度，外部调度）*/
            if ("dispatcher".equals(data.getRoleKey())) {
                List<String> list = carGroupInfoMapper.selectIsDispatcher(user.getUser().getUserId());
                for (String str : list) {
                    if ("C000".equals(str)) {
                        return "C000";
                    }
                    if ("C111".equals(str)) {
                        return "C111";
                    }
                }
            }
        }
        return null;
    }

    /***
     *
     * @param user
     * @return
     * @throws Exception
     */
    private boolean isAdmin(LoginUser user) {
        List<SysRole> roleList = user.getUser().getRoles();
        for (SysRole data : roleList) {
            if ("admin".equals(data.getRoleKey()) || "sub_admin".equals(data.getRoleKey())) {
                return true;
            }
        }
        return false;
    }

    /***
     * 确认订单
     * add by liuzb
     * @param userId
     * @param orderId
     * @return
     * @throws Exception
     */
    @Override
    public int orderConfirm(Long userId, Long orderId) throws Exception {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        orderInfo.setState("S900");
        orderInfo.setUpdateBy(String.valueOf(userId));
        int a = orderInfoMapper.updateOrderInfo(orderInfo);/**更新订单状态*/
        int b = addOrderStateTraceInfo(userId, orderId);/**添加订单轨迹*/
        int c = addOrderAccountInfo(userId, orderId);/**添加订单财务信息*/
        if (a > 0 && b > 0 && c > 0) {
            return 1;
        }
        return 0;
    }


    /***
     * 订单改单逻辑
     * add bu liuzb
     * @param userId
     * @param data
     * @return
     * @throws Exception
     */
    @Override
    public int updateTheOrder(Long userId, OrderServiceCostDetailRecordInfo data) throws Exception {
        log.info("updateTheOrder#OrderServiceCostDetailRecordInfo请求参数=[{}]", JSON.toJSONString(data));
        // 修改订单真实出发地址
        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setUpdateBy(String.valueOf(userId));
        orderAddressInfo.setUpdateTime(new Date());
        if (data.getStartTime() != null) {
            orderAddressInfo.setOrderId(data.getOrderId());
            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
            orderAddressInfo.setActionTime(data.getStartTime());
            orderAddressInfo.setAddress(data.getStartAddress());
            orderAddressInfo.setLatitude(data.getStartLatitude() != null ? data.getStartLatitude().doubleValue() : null);
            orderAddressInfo.setAddressLong(data.getStartAddress());
            orderAddressInfo.setLongitude(data.getStartLongitude() != null ? data.getStartLongitude().doubleValue() : null);
            log.info("orderAddressInfoStart={}", JSON.toJSONString(orderAddressInfo));
            orderAddressInfoMapper.updateOrderAddressInfoByOrderId(orderAddressInfo);
        }
        // 修改订单真实到达地址
        if (data.getEndTime() != null) {
            orderAddressInfo.setOrderId(data.getOrderId());
            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
            orderAddressInfo.setActionTime(data.getEndTime());
            orderAddressInfo.setAddress(data.getEndAddress());
            orderAddressInfo.setLatitude(data.getEndLatitude() != null ? data.getEndLatitude().doubleValue() : null);
            orderAddressInfo.setAddressLong(data.getEndAddress());
            orderAddressInfo.setLongitude(data.getEndLongitude() != null ? data.getEndLongitude().doubleValue() : null);
            log.info("orderAddressInfoEnd={}", JSON.toJSONString(orderAddressInfo));
            orderAddressInfoMapper.updateOrderAddressInfoByOrderId(orderAddressInfo);
        }
        try {
            // 如果其他费用是负数，总费用需要减掉
            OrderServiceCostDetailRecordInfo orderServiceCostDetailRecordInfo = orderServiceCostDetailRecordInfoMapper.queryById(data.getRecordId());
            if (data.getOthersFee() != null && data.getOthersFee().compareTo(BigDecimal.ZERO) < 0) {
                // 加一个负数，等于减
                data.setTotalFee(orderServiceCostDetailRecordInfo.getTotalFee().add(data.getOthersFee()));
            }
            // 修改结算明细（抠一个值出来改，好难）
            OrderSettlingInfo orderSettlingInfo = orderSettlingInfoMapper.selectOrderSettlingInfoByOrderId(data.getOrderId());
            if (orderSettlingInfo != null) {
                // 总费用
                orderSettlingInfo.setAmount(data.getTotalFee());
                // 其他费用-map
                HashMap otherCostTmp = JSONObject.parseObject(orderSettlingInfo.getOutPrice(), HashMap.class);
                // 其他费用-子项赋值
                List<OtherCostBean> otherCost = JSONObject.parseArray(otherCostTmp.get("otherCost").toString(), OtherCostBean.class);
                otherCost = otherCost.stream().map(otherCostBean -> doOtherCost(otherCostBean, data.getOthersFee())).collect(Collectors.toList());
                // 其他费用-封装 转 json 修改
                Map<String, Object> map = new HashMap<>();
                map.put("otherCost", otherCost);
                orderSettlingInfo.setOutPrice(JSON.toJSONString(map));

                log.info("修改结算明细,orderSettlingInfo={}", JSON.toJSONString(orderSettlingInfo));
                orderSettlingInfoMapper.updateOrderSettlingInfo(orderSettlingInfo);
            }
            // 修改账户明细
        } catch (Exception e) {
            log.error("修改结算明细异常!", e);
        }
        log.info("修改费用明细={}", JSON.toJSONString(data));
        // 修改费用明细
        data.setUpdateBy(userId);
        data.setUpdateTime(new Date());
        return orderServiceCostDetailRecordInfoMapper.update(data);
    }

    private OtherCostBean doOtherCost(OtherCostBean costBean, BigDecimal othersFee) {
        if ("其他费用".equals(costBean.getTypeName())) {
            costBean.setCost(othersFee.toString());
        }
        return costBean;
    }


    /***
     * 订单确认添加轨迹数据
     * add by liuzb
     * @param userId
     * @param orderId
     * @return
     */
    private int addOrderStateTraceInfo(Long userId, Long orderId) {
        OrderStateTraceInfo data = new OrderStateTraceInfo();
        data.setOrderId(orderId);
        data.setState("S900");
        data.setCreateBy(String.valueOf(userId));
        data.setCreateTime(new Date());
        return orderStateTraceInfoMapper.insertOrderStateTraceInfo(data);
    }

    /***
     *添加账务信息
     * add by liuzb
     * @param userId
     * @param orderId
     * @return
     */
    private int addOrderAccountInfo(Long userId, Long orderId) {
        OrderSettlingInfo data = new OrderSettlingInfo();
        data.setOrderId(orderId);
        List<OrderSettlingInfo> list = orderSettlingInfoMapper.selectOrderSettlingInfoList(data);
        if (null != list && list.size() > 0) {
            OrderAccountInfo orderAccountInfo = new OrderAccountInfo();
            orderAccountInfo.setBillId(list.get(0).getBillId());
            orderAccountInfo.setOrderId(orderId);
            orderAccountInfo.setAmount(list.get(0).getAmount());
            orderAccountInfo.setState("S008");
            orderAccountInfo.setCreateBy(String.valueOf(userId));
            orderAccountInfo.setCreateTime(new Date());
            return orderAccountInfoMapper.insertOrderAccountInfo(orderAccountInfo);
        }
        return 1;
    }


    /***
     * 当前订单的所有费用
     * add by liuzb
     * @param orderId
     * @return
     */
    private List<List<OrderServiceCostDetailRecordInfo>> getOrderCostGroup(Long orderId) {
        try {
            OrderServiceCostDetailRecordInfo data = new OrderServiceCostDetailRecordInfo();
            data.setOrderId(orderId);
            List<OrderServiceCostDetailRecordInfo> list = orderServiceCostDetailRecordInfoMapper.getList(data);
            if (null != list && list.size() > 0) {
                return returnResult(list);
            }
        } catch (Exception e) {
            log.error("getOrderCostGroup error", e);
        }

        return null;
    }

    /***
     *多天多单，一天多单处理逻辑
     * add by liuzb
     * @param list
     * @return
     * @throws Exception
     */
    private List<List<OrderServiceCostDetailRecordInfo>> returnResult(List<OrderServiceCostDetailRecordInfo> list) throws Exception {
        List<List<OrderServiceCostDetailRecordInfo>> result = new ArrayList<>();
        List<OrderServiceCostDetailRecordInfo> groupList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setImageList(getOrderServiceImagesInfoList(list.get(i).getRecordId()));
            list.get(i).setHeartbeatList(getOrderDay(list.get(i)));
            if (null != list.get(i).getStartLatitude() && null != list.get(i).getEndLatitude() && null != list.get(i).getStartLongitude() && null != list.get(i).getEndLongitude()) {
                try {
                   /* Map<String,String> map = thirdService.locationByLongitudeAndLatitude(list.get(i).getStartLatitude().toString(),list.get(i).getEndLatitude().toString());
                    list.get(i).setStartLatitudeAddress(map.get("longAddr").toString());
                    list.get(i).setEndLatitudeAddress(map.get("shortAddr").toString());
                    map = thirdService.locationByLongitudeAndLatitude(list.get(i).getStartLongitude().toString(),list.get(i).getEndLongitude().toString());
                    list.get(i).setStartLongitudeAddress(map.get("longAddr").toString());
                    list.get(i).setEndLongitudeAddress(map.get("shortAddr").toString());*/
                } catch (Exception e) {
                    log.error("thirdService error", e);
                }
            }
            if (i == 0 ? dataGrouping(list.get(i).getEndTime(), list.get(i)) : dataGrouping(list.get(i - 1).getEndTime(), list.get(i))) {
                groupList.add(list.get(i));
            } else {
                result.add(groupList);
                groupList = new ArrayList<>();
                groupList.add(list.get(i));
            }
        }
        result.add(groupList);
        return result;
    }


    /***
     *当前订单是否还存在同一天
     * add by liuzb
     * @param date
     * @param data
     * @return
     */
    private boolean dataGrouping(Date date, OrderServiceCostDetailRecordInfo data) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (null != date && null != data.getStartTime()) {
            if (sdf.format(data.getStartTime()).toString().equals(sdf.format(date).toString())) {
                return true;
            }
        }
        return false;
    }

    /***
     *一个服务订单的费用图片集合
     * add by liuzb
     * @param recordId
     * @return
     * @throws Exception
     */
    private List<OrderServiceImagesInfo> getOrderServiceImagesInfoList(Long recordId) throws Exception {
        OrderServiceImagesInfo orderServiceImagesInfo = new OrderServiceImagesInfo();
        orderServiceImagesInfo.setRecordId(recordId);
        return orderServiceImagesInfoMapper.getList(orderServiceImagesInfo);
    }

    /***
     *当前订单一单的轨迹
     * add by liuzb
     * @param data
     * @return
     */
    private List<DriverHeartbeatInfo> getOrderDay(OrderServiceCostDetailRecordInfo data) throws Exception {
        DriverHeartbeatInfo toData = new DriverHeartbeatInfo();
        toData.setOrderId(data.getOrderId());
        toData.setBeginDate(data.getStartTime());
        toData.setEndDate(data.getEndTime());
        List<DriverHeartbeatInfo> list = driverHeartbeatInfoMapper.equipmentTrajectory(toData);
        if (null != list && list.size() > 0) {
            log.info("=============================硬件设备轨迹数据源================================");
            return list;
        }
        log.info("=============================心跳轨迹数据源================================");
        return driverHeartbeatInfoMapper.getOrderDay(toData);
    }

    /***
     *
     * @param userId
     * @param orderId
     * @return
     * @throws Exception
     */
    @Override
    public String orderReassignment(Long userId, Long orderId) throws Exception {
        OrderInfo data = new OrderInfo();
        data.setOrderId(orderId);
        data.setUpdateBy(String.valueOf(userId));
        data.setUpdateTime(new Date());
        orderInfoMapper.changeOrder(data);
        //7.订单调度数据还原
        orderDispatcheDetailInfoMapper.revertOrderDispatcheDetailInfoByOrderId(orderId, userId, DateUtils.getNowDate());
        orderStateTraceInfoMapper.deleteInfoByState(OrderStateTrace.SENDCAR.getState(), orderId);
        return "改派成功";
    }


    /***
     *add by liuzb
     * @param orderId
     * @return
     * @throws Exception
     */
    @Override
    public String dispatcherPhone(Long orderId) throws Exception {
        EcmpUserDto data = ecmpUserMapper.dispatcherPhone(orderId);
        if (null != data) {
            return data.getNickName() + ":" + data.getUserName();
        }
        return null;
    }

    /***
     *
     * @param orderId
     * @return
     * @throws Exception
     */
    @Override
    public Map downloadOrderData(Long orderId) throws Exception {
        Map<String, String> result = orderInfoMapper.downloadOrderData(orderId);
        result.put("getKeyTime", DateUtils.formatDate(DateUtils.parseDate(result.get("actionBeginTime")), "yyyy年MM月dd日 HH时mm分ss秒"));
        result.put("flag", "1");
        if (NO_DRIVER.equals(result.get("driverName"))) {
            //自驾
            OrderStateTraceInfo orderStateTraceInfo = orderStateTraceInfoMapper.queryLatestInfoByOrderIdAndState(orderId,OrderStateTrace.PICKUPCAR.getState());
            OrderStateTraceInfo giveUpOrderStateTraceInfo = orderStateTraceInfoMapper.queryLatestInfoByOrderIdAndState(orderId,OrderStateTrace.GIVE_UP_CAR.getState());
            if(orderStateTraceInfo!=null&&orderStateTraceInfo.getCreateTime()!=null){
                result.put("getKeyTime", DateUtils.formatDate( orderStateTraceInfo.getCreateTime() , "yyyy年MM月dd日 HH时mm分ss秒"));
            }
            result.put("flag", "1");
            if(giveUpOrderStateTraceInfo != null && giveUpOrderStateTraceInfo.getCreateTime() != null) {
                result.put("actionEndTime", DateUtils.formatDate( giveUpOrderStateTraceInfo.getCreateTime() , "yyyy年MM月dd日 HH时mm分ss秒"));
            }
        }

        OrderDetailBackDto orderDetailBackDto = orderInfoMapper.getOrderListDetail(String.valueOf(orderId));
        if(orderDetailBackDto == null){
            return result;
        }
        String shortName = result.get("shortName");
        JourneyNodeInfo journeyNodeInfo = journeyNodeInfoMapper.selectJourneyNodeInfoByJourneyId(orderDetailBackDto.getJourneyId());
        //按需求，此处导出计划地址，不按照实际地址导出
        if(journeyNodeInfo != null){
            if(!StringUtils.isEmpty(journeyNodeInfo.getPlanEndAddress())){
                shortName = journeyNodeInfo.getPlanEndAddress();
            }
            if(!StringUtils.isEmpty(journeyNodeInfo.getPlanBeginAddress())){
                result.put("addressLong",journeyNodeInfo.getPlanBeginAddress());
            }
        }
        String newEndAdress = orderDetailBackDto.getNewEndAddress();
        if(!StringUtils.isEmpty(newEndAdress)){
            shortName = shortName+","+newEndAdress;
        }
        result.put("shortName",shortName);
        //车队名称
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        if(orderInfo == null){
            return result;
        }
        Long carId = orderInfo.getCarId();
        Long driverId = orderInfo.getDriverId();
        String carGroupName = "";
        if(driverId != null){
            CarGroupDriverRelation carGroupDriverRelation = carGroupDriverRelationMapper.selectCarGroupDriverRelationById(driverId);
            if(carGroupDriverRelation != null){
                CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupDriverRelation.getCarGroupId());
                if(carGroupInfo != null && !StringUtils.isEmpty(carGroupInfo.getCarGroupName())){
                    carGroupName = carGroupInfo.getCarGroupName();
                }
            }
        }
        if(carId != null){
            CarInfo carInfo = carInfoMapper.selectCarInfoById(carId);
            if(carInfo != null){
                CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carInfo.getCarGroupId());
                if(carGroupInfo != null && !StringUtils.isEmpty(carGroupInfo.getCarGroupName())){
                    if(!StringUtils.isEmpty(carGroupName)){
                        carGroupName = carGroupName +","+ carGroupInfo.getCarGroupName();
                    }else {
                        carGroupName = carGroupInfo.getCarGroupName();
                    }
                }
            }
        }
        result.put("carGroupName",carGroupName);
        return result;
    }

    /***
     *订单列表的tale页签的数据模型
     * add by liuzb
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> orderServiceCategory(LoginUser user) throws Exception {
        String key = isDispatcher(user);
        OrderListBackDto orderListBackDto = new OrderListBackDto();
        if (null == key) {
            orderListBackDto.setDeptId(user.getUser().getDeptId());
        } else if ("admin".equals(key)) {
            orderListBackDto.setUserId(null);
            orderListBackDto.setUpdateBy(null);
        } else {
            orderListBackDto.setUpdateBy("C000".equals(key) ? null : user.getUser().getUserId());
        }
        JSONObject json = new JSONObject(addDataTable(orderListBackDto));
        return json;
    }


    /***
     * add by liuzb
     * @param data
     * @return
     * @throws Exception
     */
    private Map<String, Object> addDataTable(OrderListBackDto data) throws Exception {
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();

        data.setState("S299");
        List<OrderListBackDto> toBeServed = orderInfoMapper.getCount(data);
        dataMap.put("msg", "待服务");
        dataMap.put("value", null == toBeServed ? 0 : toBeServed.size());
        dataMap.put("code", "S299");
        dataMap.put("sort", "1");
        map.put("toBeServed", dataMap);

        dataMap = new HashMap<>();
        dataMap.put("msg", "待出车");
        data.setState("S380");
        List<OrderListBackDto> waitingToLeave = orderInfoMapper.getCount(data);
        dataMap.put("value", null == waitingToLeave ? 0 : waitingToLeave.size());
        dataMap.put("code", "S380");
        dataMap.put("sort", "2");
        map.put("waitingToLeave", dataMap);

        dataMap = new HashMap<>();
        data.setState("S616");
        data.setItIsSelfDriver("1");
        List<OrderListBackDto> toBePickedUp = orderInfoMapper.getCount(data);
        dataMap.put("msg", "待还车");
        dataMap.put("value", null == toBePickedUp ? 0 : toBePickedUp.size());
        dataMap.put("code", "S616");
        dataMap.put("sort", "3");
        map.put("toBePickedUp", dataMap);

        dataMap = new HashMap<>();
        data.setState("S500");
        data.setItIsSelfDriver(null);
        List<OrderListBackDto> takingOver = orderInfoMapper.getCount(data);
        dataMap.put("msg", "接驾中");
        dataMap.put("value", null == takingOver ? 0 : takingOver.size());
        dataMap.put("code", "S500");
        dataMap.put("sort", "4");
        map.put("takingOver", dataMap);

        dataMap = new HashMap<>();
        data.setState("S600");
        List<OrderListBackDto> etcUpperCar = orderInfoMapper.getCount(data);
        dataMap.put("msg", "待上车");
        dataMap.put("value", null == etcUpperCar ? 0 : etcUpperCar.size());
        dataMap.put("code", "S600");
        dataMap.put("sort", "5");
        map.put("etcUpperCar", dataMap);

        dataMap = new HashMap<>();
        data.setState("S616");
        List<OrderListBackDto> inService = orderInfoMapper.getCount(data);
        dataMap.put("msg", "服务中");
        dataMap.put("value", null == inService ? 0 : inService.size());
        dataMap.put("code", "S616");
        dataMap.put("sort", "6");
        map.put("inService", dataMap);

        dataMap = new HashMap<>();
        data.setState("S635");
        List<OrderListBackDto> serviceSuspension = orderInfoMapper.getCount(data);
        dataMap.put("msg", "服务暂停");
        dataMap.put("value", null == serviceSuspension ? 0 : serviceSuspension.size());
        dataMap.put("code", "S635");
        dataMap.put("sort", "7");
        map.put("serviceSuspension", dataMap);

        dataMap = new HashMap<>();
        data.setState("S699");
        List<OrderListBackDto> toBeConfirmed = orderInfoMapper.getCount(data);
        dataMap.put("msg", "待确认");
        dataMap.put("value", null == toBeConfirmed ? 0 : toBeConfirmed.size());
        dataMap.put("code", "S699");
        dataMap.put("sort", "8");
        map.put("toBeConfirmed", dataMap);

        dataMap = new HashMap<>();
        data.setState("S900");
        List<OrderListBackDto> completed = orderInfoMapper.getCount(data);
        dataMap.put("msg", "已完成");
        dataMap.put("value", null == completed ? 0 : completed.size());
        dataMap.put("code", "S900");
        dataMap.put("sort", "9");
        map.put("completed", dataMap);

        dataMap = new HashMap<>();
        data.setState("S921");
        List<OrderListBackDto> expired= orderInfoMapper.getCount(data);
        dataMap.put("msg","已过期");
        dataMap.put("value",null==expired ?0:expired.size());
        dataMap.put("code","S921");
        dataMap.put("sort","10");
        map.put("expired",dataMap);
        return map;
    }


    /***
     *
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public List<String> getUseTheCar(LoginUser user) throws Exception {
        String role = isDispatcher(user);
        return orderInfoMapper.getUseTheCar("C111".equals(role) ? user.getUser().getUserId() : null, null == role ? user.getUser().getOwnerCompany() : null);
    }


    /***
     *
     * @param orderInfoFSDto
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public PageResult<OrderInfoFSDto> getOrderInfoList(OrderInfoFSDto orderInfoFSDto, LoginUser user) throws Exception {
        String role = isDispatcher(user);
        if (null == role) {/**员工看本部门*/
            orderInfoFSDto.setCompanyId(user.getUser().getOwnerCompany());
        } else if ("C111".equals(role)) {/**外部调度员*/
            orderInfoFSDto.setUserId(user.getUser().getUserId());
        }
        List<OrderInfoFSDto> list = orderInfoMapper.getOrderInfoList(orderInfoFSDto);
        PageInfo<OrderInfoFSDto> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(), info.getPages(), list);
    }


    @Override
    public Map<String, Map<String, Integer>> selectOrderCarGroup(Long companyId) {
        String startDate = DateFormatUtils.formatDate(DateFormatUtils.DATE_FORMAT, DateFormatUtils.getPastDate(6));
        String endDate = DateFormatUtils.formatDate(DateFormatUtils.DATE_FORMAT, new Date());
        Map<String, Map<String, Integer>> map = DateFormatUtils.sliceUpDateRange(startDate, endDate).stream()
                .collect(Collectors.toMap(x -> x, x -> new LinkedHashMap() {{
                    put("in_have", 0);
                    put("in_rent", 0);
                    put("out", 0);
                }}, (k1, k2) -> k2, LinkedHashMap::new));
        orderInfoMapper.selectOrderCarGroup(companyId).stream()
                .filter(x -> x.get("states").indexOf(OrderStateTrace.CANCEL.getState()) == -1)//过滤取消
                .filter(x -> x.get("states").indexOf(OrderStateTrace.ORDEROVERTIME.getState()) == -1)//过滤超时
                .filter(x -> x.get("states").indexOf(OrderStateTrace.ORDERDENIED.getState()) == -1)//过滤驳回
                .forEach(x -> {
                    //S299之后的所有单子
                    if (Integer.parseInt(x.get("state").substring(1)) >= 299) {
                        //map.get(x.get("date")).put("all",map.get(x.get("date")).get("all")+1);
                        if (CarConstant.IT_IS_USE_INNER_CAR_GROUP_IN.equals(x.get("it_is_inner"))) {
                            if ("S001".equals(x.get("source"))) {
                                List<String> dates = DateFormatUtils.sliceUpDateRange(x.get("date"), x.get("end_date"));
                                dates.stream().forEach(t -> {
                                    if (map.containsKey(t)) {
                                        map.get(t).put("in_have", map.get(t).get("in_have") + 1);
                                    }
                                });
                            }
                            if ("S002".equals(x.get("source")) || "S003".equals(x.get("source"))) {
                                List<String> dates = DateFormatUtils.sliceUpDateRange(x.get("date"), x.get("end_date"));
                                dates.stream().forEach(t -> {
                                    if (map.containsKey(t)) {
                                        map.get(t).put("in_rent", map.get(t).get("in_rent") + 1);
                                    }
                                });
                            }
                        }
                        if (CarConstant.IT_IS_USE_INNER_CAR_GROUP_OUT.equals(x.get("it_is_inner"))) {
                            List<String> dates = DateFormatUtils.sliceUpDateRange(x.get("date"), x.get("end_date"));
                            dates.stream().forEach(t -> {
                                if (map.containsKey(t)) {
                                    map.get(t).put("out", map.get(t).get("out") + 1);
                                }
                            });
                        }
                    }
                });
        return map;
    }

    @Override
    public Map<String, Integer> selectNormalOrderReserveTime(Long companyId, String beginDate, String endDate) {
        List<Map> list = orderInfoMapper.selectNormalOrderReserveTime(companyId, beginDate, endDate);
        Map<String, Integer> map = new LinkedHashMap() {{
            put("8-9", 0);
            put("9-10", 0);
            put("10-11", 0);
            put("11-12", 0);
            put("12-14", 0);
            put("14-15", 0);
            put("15-16", 0);
            put("16-17", 0);
            put("17-18", 0);
            put("18时-次日八时", 0);
        }};
        list.stream()
                .filter(x -> x.get("states").toString().indexOf(OrderStateTrace.CANCEL.getState()) == -1)//过滤取消
                .filter(x -> x.get("states").toString().indexOf(OrderStateTrace.ORDEROVERTIME.getState()) == -1)//过滤超时
                .filter(x -> x.get("states").toString().indexOf(OrderStateTrace.ORDERDENIED.getState()) == -1)//过滤驳回
                .forEach(x -> {
                    int dateGroup = Integer.parseInt(DateFormatUtils.formatDate(DateFormatUtils.TIME_FORMAT_1, DateFormatUtils.parseDate(DateFormatUtils.DATE_TIME_FORMAT, x.get("start_date").toString())));
                    if (dateGroup == 8) {
                        map.put("8-9", map.get("8-9") + 1);
                    }
                    if (dateGroup == 9) {
                        map.put("9-10", map.get("9-10") + 1);
                    }
                    if (dateGroup == 10) {
                        map.put("10-11", map.get("10-11") + 1);
                    }
                    if (dateGroup == 11) {
                        map.put("11-12", map.get("11-12") + 1);
                    }
                    if (dateGroup >= 12 && dateGroup < 14) {
                        map.put("12-14", map.get("12-14") + 1);
                    }
                    if (dateGroup == 14) {
                        map.put("14-15", map.get("14-15") + 1);
                    }
                    if (dateGroup == 15) {
                        map.put("15-16", map.get("15-16") + 1);
                    }
                    if (dateGroup == 16) {
                        map.put("16-17", map.get("16-17") + 1);
                    }
                    if (dateGroup == 18) {
                        map.put("17-18", map.get("17-18") + 1);
                    }
                    if (dateGroup >= 18 || dateGroup < 8) {
                        map.put("18时-次日八时", map.get("18时-次日八时") + 1);
                    }
                });
        return map;
    }

    @Override
    public PayeeInfoDto getPayeeInfo(ReckoningDto param) {
        return orderInfoMapper.getPayeeInfo(param);
    }

    @Override
    public PayeeInfoDto getCarGroupInfo(String carGroupId) {
        return orderInfoMapper.getCarGroupInfo(carGroupId);
    }

    @Override
    public String selectOrderApplyInfoByOrderNumber(OrderInfo orderInfo) {
        String applyId=orderInfoMapper.selectOrderApplyInfoByOrderNumber(orderInfo);
        return applyId;
    }

    @Override
    public String getOrderStateByOrderInfo(OrderInfo orderInfo) {
        String orderState=orderInfoMapper.getOrderStateByOrderInfo(orderInfo);
        return orderState;
    }

    @Override
    public List<MoneyListDto> getMoneyList(ReckoningDto param) {

        return orderInfoMapper.getMoneyList(param);

    }

}
