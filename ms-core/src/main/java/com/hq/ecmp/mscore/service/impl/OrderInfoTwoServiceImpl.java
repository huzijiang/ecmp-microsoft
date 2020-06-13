package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.api.system.domain.SysDriver;
import com.hq.api.system.domain.SysRole;
import com.hq.api.system.domain.SysUser;
import com.hq.common.exception.BaseException;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.constant.enumerate.CarUserSelfDrivingEnum;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.dto.DriverCloudDto;
import com.hq.ecmp.mscore.dto.cost.ApplyPriceDetails;
import com.hq.ecmp.mscore.dto.dispatch.DispatchCarGroupDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
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
public class OrderInfoTwoServiceImpl implements OrderInfoTwoService {

    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Autowired
    private IJourneyUserCarPowerService iJourneyUserCarPowerService;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Resource
    private OrderAddressInfoMapper orderAddressInfoMapper;
    @Resource
    private IOrderSettlingInfoService orderSettlingInfoService;
    @Resource
    private OrderSettlingInfoMapper orderSettlingInfoMapper;
    @Resource
    private IOrderPayInfoService iOrderPayInfoService;
    @Resource
    private JourneyInfoMapper journeyInfoMapper;
    @Resource
    private ThirdService thirdService;
    @Resource
    private IsmsBusiness ismsBusiness;
    @Resource
    private CarInfoMapper carInfoMapper;
    @Resource
    private OrderDispatcheDetailInfoMapper dispatcheDetailInfoMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private EnterpriseCarTypeInfoMapper enterpriseCarTypeInfoMapper;
    @Resource
    private IEcmpConfigService ecmpConfigService;
    @Resource
    private EcmpUserMapper ecmpUserMapper;
    @Resource
    private IOrderStateTraceInfoService iOrderStateTraceInfoService;
    @Resource
    private CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;
    @Resource
    private ApplyInfoMapper applyInfoMapper;
    @Resource
    private CostConfigInfoMapper costConfigInfoMapper;
    @Resource
    private RegimeInfoMapper regimeInfoMapper;
    @Resource
    private JourneyAddressInfoMapper journeyAddressInfoMapper;
    @Resource
    private EcmpOrgMapper ecmpOrgMapper;


    /**
     * 公务取消订单
     *
     * @param orderId
     * @param cancelReason
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public CancelOrderCostVO cancelBusinessOrder(Long orderId, String cancelReason, Long longinUserId) throws Exception {
        CancelOrderCostVO vo = new CancelOrderCostVO();
        BigDecimal cancelFee1 = BigDecimal.ZERO;
        int isPayFee = 0;
        String ownerAmount = null;
        String personalAmount = null;
        String payId = null;
        String payState = null;
        OrderStateVO orderStateVO = orderInfoMapper.getOrderState(orderId);
        if (!longinUserId.equals(orderStateVO.getUserId())) {
            throw new Exception("取消订单操作人与申请人不一致,不可取消");
        }
        if (orderStateVO == null) {
            throw new Exception("该订单不存在!");
        }
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderStateVO.getJourneyId());
        String state = orderStateVO.getState();
        Date useCarDate = orderStateVO.getUseCarDate();
        if (useCarDate == null) {
            throw new Exception("用车时间不明确!");
        }
        //修改权限标识
//        boolean opType=true;
        int intState = Integer.parseInt(state.substring(1));
        //校验是否超过用车时间
        //未派车(无车无司机)
        if (intState < Integer.parseInt(OrderState.ALREADYSENDING.getState().substring(1))) {
            log.info("订单:" + orderId + "无车无司机取消订单-------> start,原因{}", cancelReason);
            int i = this.ownerCarCancel(orderId, cancelReason, orderStateVO.getUserId());
            if (CarConstant.USR_CARD_MODE_NET.equals(orderStateVO.getUseCarMode())) {
                JSONObject jsonObject = thirdService.threeCancelServer(orderId, cancelReason);
            }
        } else if (intState >= Integer.parseInt(OrderState.ALREADYSENDING.getState().substring(1)) &&
                intState < Integer.parseInt(OrderState.INSERVICE.getState().substring(1))) {
            log.info("订单:" + orderId + "有车有司机取消订单------- start,原因{}", cancelReason);
            //待服务(有车有司机)
            if (CarConstant.USR_CARD_MODE_HAVE.equals(orderStateVO.getUseCarMode())) {
                //自由车带服务取消
                log.info("订单:" + orderId + "自有车---有车有司机取消订单------- start,原因{}", cancelReason);
                int i = this.ownerCarCancel(orderId, cancelReason, orderStateVO.getUserId());
            } else {//网约车带服务的取消
                JSONObject jsonObject = thirdService.threeCancelServer(orderId, cancelReason);
                cancelFee1 = jsonObject.getDouble("cancelFee") == null ? BigDecimal.ZERO : BigDecimal.valueOf(jsonObject.getDouble("cancelFee"));
//                cancelFee1 = new BigDecimal("30");
                if (cancelFee1.compareTo(BigDecimal.ZERO) <= 0) {
                    //不需要支付取消费
                    log.info("订单:" + orderId + "网约车------不需要支付取消费---有车有司机取消订单------- start,原因{}", cancelReason);
                    this.onlineCarCancel(orderId, cancelReason, orderStateVO.getUserId(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
                } else {
                    //需要支付取消费
                    BigDecimal personPay = iOrderPayInfoService.checkOrderFeeOver(cancelFee1, journeyInfo.getRegimenId(), orderStateVO.getUserId());
                    log.info("订单:" + orderId + "取消费超额" + personPay);
                    /*超额个人支付*/
                    if (personPay.compareTo(BigDecimal.ZERO) == 1) {
                        BigDecimal ownerPay = cancelFee1.subtract(personPay);
                        ownerAmount = ownerPay.stripTrailingZeros().toPlainString();
                        isPayFee = 1;
                        personalAmount = personPay.toPlainString();
                        log.info("网约车订单:" + orderId + "取消参数企业支付:" + ownerPay + ",个人支付:" + personPay + ",总共取消费:" + cancelFee1);
                        OrderPayInfo orderPayInfo = this.onlineCarCancel(orderId, cancelReason, orderStateVO.getUserId(), cancelFee1, ownerPay, personPay);
                        payId = orderPayInfo.getPayId();
                        payState = orderPayInfo.getState();
                    } else {
                        /*全部由企业支付*/
                        OrderPayInfo orderPayInfo = this.onlineCarCancel(orderId, cancelReason, orderStateVO.getUserId(), cancelFee1, cancelFee1, BigDecimal.ZERO);
                        iOrderPayInfoService.updateOrderPayInfo(new OrderPayInfo(orderPayInfo.getPayId(), OrderPayConstant.PAID));
                        payId = orderPayInfo.getPayId();
                        payState = OrderPayConstant.PAID;
                    }
                }
            }
        }
        //修改权限为未使用
        iJourneyUserCarPowerService.updatePowerSurplus(orderStateVO.getPowerId(), 2);
        //TODO 生产放开
        vo.setIsPayFee(isPayFee);
        vo.setCancelAmount(cancelFee1.stripTrailingZeros().toPlainString());
        vo.setOwnerAmount(ownerAmount);
        vo.setPersonalAmount(personalAmount);
        vo.setPayState(payState);
        vo.setPayId(payId);
        ismsBusiness.sendMessageCancelOrder(orderId, longinUserId);
        /**发送取消订单短信*/
        if (cancelFee1.compareTo(BigDecimal.ZERO) == 1) {
            ismsBusiness.sendSmsCancelOrder(orderId);
        } else {
            ismsBusiness.sendSmsCancelOrderHaveFee(orderId, cancelFee1.doubleValue());
        }
        return vo;
    }

    /**
     * 首次登陆进行中的行程订单
     *
     * @param userId
     * @return
     */
    @Override
    public List<RunningOrderVo> runningOrder(Long userId) {
        String states = OrderState.INSERVICE.getState() + "," + OrderState.READYSERVICE.getState()
                + "," + OrderState.ALREADYSENDING.getState() + "," + OrderState.REASSIGNMENT.getState();
        return orderInfoMapper.getRunningOrder(userId, states);
    }

    /**
     * 获取申请调度列表
     *
     * @param query
     * @return
     */
    @Override
    public Map<String,Object> queryDispatchList(ApplyDispatchQuery query, LoginUser loginUser) {
        //判断登录人的身份来显示他看到的不同权限的数据
        SysUser user = loginUser.getUser();
        List<SysRole> role = loginUser.getUser().getRoles();
        Long companyId = user.getOwnerCompany();
        query.setCompanyId(companyId);
        query.setUserId(user.getUserId());
        //<系统管理员身份>
        List<DispatchVo> adminOrderList = new ArrayList<DispatchVo>();
        //<调度员身份>
        List<DispatchVo> dispatcherOrderList = new ArrayList<DispatchVo>();
        /**查寻该调度员可用查看的所有申请人*/
        if ("1".equals(user.getItIsDispatcher())) {//是调度员

            dispatcherOrderList = orderInfoMapper.queryDispatchList(query);
        }
        List<SysRole> collect = role.stream().filter(p -> CommonConstant.ADMIN_ROLE.equals(p.getRoleKey()) || CommonConstant.SUB_ADMIN_ROLE.equals(p.getRoleKey())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {//是管理员
            if (!CollectionUtils.isEmpty(dispatcherOrderList)) {
                List<Long> orderIds = dispatcherOrderList.stream().map(p -> p.getOrderId()).collect(Collectors.toList());
                query.setOrderIds(orderIds);
            }
            //本公司所有的订单
            adminOrderList = orderInfoMapper.queryAdminDispatchList(query);
            if (!CollectionUtils.isEmpty(adminOrderList)) {
                dispatcherOrderList.addAll(adminOrderList);
            }
        }
        /*Page<DispatchVo> page = new Page<>(dispatcherOrderList, query.getPageSize());
        if (dispatcherOrderList.isEmpty()) {
            Long total = 0L;
            Integer pageSize = 0;
            return new PageResult<>(total, pageSize, page.getCurrentPageData());
        }*/
        /*page.setCurrent_page(query.getPageNum());
        return new PageResult<>(Long.valueOf(page.getTotal_sum()), page.getCurrent_page(), page.getCurrentPageData());*/
        Map<String,Object> map = new HashMap();
        List<DispatchVo> page = PageUtil.startPage(dispatcherOrderList,query.getPageNum(),query.getPageSize());
        Integer count = dispatcherOrderList.size();
        Integer totalPage = count % query.getPageNum() == 0 ? count / query.getPageNum() : count / query.getPageNum() + 1;
        map.put("totalPage", count);
        map.put("page", page);
        map.put("list", totalPage);
        return map;
    }

    /**
     * 查询过期的订单,改为关闭，轨迹表插入过期轨迹记录
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkOrderIsExpired() {
        List<OrderInfo> expiredOrders = orderInfoMapper.getExpiredOrder();
        if (!CollectionUtils.isEmpty(expiredOrders)) {
            for (OrderInfo orderInfo :
                    expiredOrders) {
                OrderInfo orderInfoUp = new OrderInfo();
                orderInfoUp.setState(OrderState.ORDERCLOSE.getState());
                orderInfoUp.setOrderId(orderInfo.getOrderId());
                orderInfoUp.setUpdateTime(DateUtils.getNowDate());
                orderInfoUp.setUpdateBy("1");
                orderInfoMapper.updateOrderInfo(orderInfoUp);
                //添加超时轨迹状态
                orderInfoService.insertOrderStateTrace(String.valueOf(orderInfo.getOrderId()), OrderState.ORDEROVERTIME.getState(), "1", null, null, null);
                //用车权限次数做变化
                iJourneyUserCarPowerService.updatePowerSurplus(orderInfo.getPowerId(), 2);
            }
        }
    }

    /**
     * 司机端改派记录
     *
     * @param orderNo
     * @param driverId
     * @return
     */
    @Override
    public OrderReassignVO reassignDetail(Long orderNo, Long driverId) {
        OrderReassignVO vo = new OrderReassignVO();
        String state = OrderState.APPLYREASSIGN.getState() + "," + OrderState.REASSIGNPASS.getState() + "," + OrderState.REASSIGNREJECT.getState();
        List<RejectDispatcherUserVO> orderList = orderStateTraceInfoMapper.reassignOrderList(orderNo, state);
        if (CollectionUtils.isEmpty(orderList)) {
            return vo;
        }
        vo.setOrderId(orderList.get(0).getOrderId());
        vo.setApproveList(orderList);
        for (RejectDispatcherUserVO dispatcherUserVO : orderList) {
            if (OrderState.APPLYREASSIGN.getState().equals(dispatcherUserVO.getState())) {
                vo.setApplyReason(dispatcherUserVO.getContent());
            }
            if (OrderState.REASSIGNREJECT.getState().equals(dispatcherUserVO.getState())) {
                vo.setRejectReason(dispatcherUserVO.getContent());
            }
        }
        return vo;
    }

    @Override
    @Transactional
    public void replaceCar(OrderInfo orderInfo, Long userId) throws Exception {
        CarInfo carInfo = carInfoMapper.selectCarInfoById(orderInfo.getCarId());
        orderInfo.setCarColor(carInfo.getCarColor());
        orderInfo.setCarLicense(carInfo.getCarLicense());
        orderInfo.setCarModel(carInfo.getCarType());
        orderInfo.setDemandCarLevel(enterpriseCarTypeInfoMapper.queryCarTypeNameByCarId(orderInfo.getCarId()));
        orderInfo.setUseCarMode(CarConstant.USR_CARD_MODE_HAVE);
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        orderInfoMapper.updateOrderInfo(orderInfo);
        //插入流转状态
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setCreateBy("0");
        orderStateTraceInfo.setState(OrderState.REPLACECAR.getState());
        orderStateTraceInfo.setOrderId(orderInfo.getOrderId());
        orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
        orderStateTraceInfo.setContent("换车车牌号：" + carInfo.getCarLicense());
        orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
        ismsBusiness.sendMessageReplaceCarComplete(orderInfo.getOrderId(), userId);
        // 发送短信
        ismsBusiness.sendSmsReplaceCar(orderInfo.getOrderId());
    }

    /**
     * 网约车回调接口
     * @param jsonResult
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void callBackOrderState(String jsonResult) throws Exception {
        Long orderNo;
        JSONObject thirdPartyOrderState = JSONObject.parseObject(jsonResult);
        log.info("回调返回参数:网约车" + thirdPartyOrderState.getString("partnerOrderNo") + "订单详情:" + thirdPartyOrderState);
        String partnerOrderNo = thirdPartyOrderState.getString("partnerOrderNo");//订单id
        if (com.hq.common.utils.StringUtils.isEmpty(thirdPartyOrderState.getString("partnerOrderNo"))) {
            throw new Exception("订单id为空");
        }
        orderNo = Long.parseLong(partnerOrderNo);
        Double longitude = null;
        Double latitude = null;
        String content = "";
        String status = thirdPartyOrderState.getString("status");
        String lableState = thirdPartyOrderState.getString("status");
        String json = thirdPartyOrderState.getString("driverInfo");
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderNo);
        if (orderInfo == null) {
            throw new Exception("订单:" + orderNo + "不存在");
        }
        if (OrderState.ORDEROVERTIME.getState().equals(status) || OrderState.REASSIGNPASS.getState().equals(status) || OrderState.ALREADYSENDING.getState().equals(status)) {
            redisUtil.delKey(CommonConstant.DISPATCH_LOCK_PREFIX + orderNo);
        }
        OrderInfo newOrderInfo = new OrderInfo(orderNo, status);
        DriverCloudDto driverCloudDto = null;
        if (com.hq.common.utils.StringUtils.isNotEmpty(json)) {
            driverCloudDto = JSONObject.parseObject(json, DriverCloudDto.class);
            String driverPoint = driverCloudDto.getDriverPoint();
            if (StringUtils.isNotEmpty(driverPoint)) {
                String[] split = driverPoint.split(",");
                longitude = Double.parseDouble(split[0]);
                latitude = Double.parseDouble(split[1]);
            }
        }
        if (OrderState.ALREADYSENDING.getState().equals(status) || OrderState.REASSIGNPASS.getState().equals(status)) {
            if (driverCloudDto != null) {
                newOrderInfo.setDriverName(driverCloudDto.getName());
                newOrderInfo.setDriverMobile(driverCloudDto.getPhone());
                newOrderInfo.setDriverGrade(driverCloudDto.getDriverRate());
                newOrderInfo.setCarLicense(driverCloudDto.getLicensePlates());
                newOrderInfo.setCarColor(driverCloudDto.getVehicleColor());
                newOrderInfo.setCarModel(driverCloudDto.getModelName());
                newOrderInfo.setDemandCarLevel(driverCloudDto.getGroupId());
                newOrderInfo.setTripartiteOrderId(thirdPartyOrderState.getString("orderNo"));
                if (OrderState.REASSIGNPASS.getState().equals(status)) {
                    //改派通过订单状态为299,轨迹为279
                    status = OrderState.ALREADYSENDING.getState();
                    newOrderInfo.setState(OrderState.ALREADYSENDING.getState());
                }
            }
            content = "网约车约车成功";
        } else if (OrderState.STOPSERVICE.getState().equals(status)) {//服务结束
            //TODO 调财务结算模块
            JSONObject feeInfoBean = thirdPartyOrderState.getJSONObject("feeInfo");
            List<OrderSettlingInfo> orderSettlingInfos = orderSettlingInfoMapper.selectOrderSettlingInfoList(new OrderSettlingInfo(orderNo));
            if (CollectionUtils.isEmpty(orderSettlingInfos)) {
                String amount = feeInfoBean.getString("customerPayPrice");
                String distance = feeInfoBean.getString("mileage");//里程
                String duration = feeInfoBean.getString("min");//时长
                JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
                BigDecimal persionMoney = iOrderPayInfoService.checkOrderFeeOver(new BigDecimal(amount), journeyInfo.getRegimenId(), orderInfo.getUserId());
                log.info("网约车订单:" + orderNo + "校验超额---" + persionMoney);
                OrderPayInfo orderPayInfo = iOrderPayInfoService.insertOrderPayAndSetting(orderNo, new BigDecimal(amount), distance, duration, feeInfoBean.toJSONString(), Long.parseLong(CommonConstant.START), persionMoney);

                int orderConfirmStatus = ecmpConfigService.getOrderConfirmStatus(ConfigTypeEnum.ORDER_CONFIRM_INFO.getConfigKey(), orderInfo.getUseCarMode(), orderInfo.getCompanyId());
                if (orderConfirmStatus == ZERO) {
                    //自动确认
                    /**判断是否需要支付*/
                    if (new BigDecimal(amount).compareTo(BigDecimal.ZERO) < 1) {
                        status = OrderState.ORDERCLOSE.getState();
                        lableState = OrderState.ORDERCLOSE.getState();
                        newOrderInfo.setState(status);
                    }
                }
                log.info("网约车订单:" + orderNo + "确认方式为" + orderConfirmStatus);
            }

            content = "网约车服务结束";
            //更新网约车真实地址
            this.refreshRealAddr(longitude, latitude, OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE, orderInfo);
        } else if (OrderState.ORDEROVERTIME.getState().equals(status)) {
            //订单超时
            status = OrderState.ORDERCLOSE.getState();
            newOrderInfo.setState(status);
            content = "网约车约车超时";
            //还原权限
            iJourneyUserCarPowerService.updatePowerSurplus(orderInfo.getPowerId(), 2);
        } else if (OrderState.ORDERCANCEL.getState().equals(status)) {
            //订单云端取消
            status = OrderState.ORDERCLOSE.getState();
            newOrderInfo.setState(status);
            content = "网约车平台客服取消订单";
            //还原权限
            iJourneyUserCarPowerService.updatePowerSurplus(orderInfo.getPowerId(), 2);
            log.info("网约车订单" + orderNo + "被平台客服取消!");
        }
//        if (!OrderState.ORDERCANCEL.getState().equals(status)){
        orderInfoMapper.updateOrderInfo(newOrderInfo);
        log.info("网约车订单" + newOrderInfo.getOrderId() + "状态更新为" + newOrderInfo.getState());
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo(orderNo, lableState, longitude, latitude);
        orderStateTraceInfo.setCreateBy(CommonConstant.START);
        orderStateTraceInfo.setCreateTime(new Date());
        orderStateTraceInfo.setContent(content);
        List<OrderStateTraceInfo> orderStateTraceInfos = orderStateTraceInfoMapper.selectOrderStateTraceInfoList(new OrderStateTraceInfo(orderNo, lableState));
        if (CollectionUtils.isEmpty(orderStateTraceInfos)) {
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
            log.info("网约车订单" + newOrderInfo.getOrderId() + "轨迹表中状态插入" + newOrderInfo.getState() + "成功");
        }
//        }
        if (OrderState.ALREADYSENDING.getState().equals(lableState)) {
            //约车成功 发短信，发通知
            ismsBusiness.sendSmsCallTaxiNet(orderNo);
        } else if (OrderState.READYSERVICE.getState().equals(lableState)) {
            //驾驶员已到达/准备服务
            ismsBusiness.driverArriveMessage(orderNo);
        } else if (OrderState.INSERVICE.getState().equals(lableState)) {
            this.refreshRealAddr(longitude, latitude, OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT, orderInfo);
            //开始服务 发送通知
            ismsBusiness.sendSmsDriverBeginService(orderNo);
            //司机开始服务发送消息给乘车人和申请人（行程通知）
            ismsBusiness.sendMessageServiceStart(orderNo, orderInfo.getUserId());
        } else if (OrderState.STOPSERVICE.getState().equals(lableState)) {
            //任务结束
            ismsBusiness.endServiceNotConfirm(orderNo);
        } else if (OrderState.ORDEROVERTIME.getState().equals(lableState)) {
            //订单超时
            ismsBusiness.sendSmsCallTaxiNetFail(orderNo);
        }
    }


    /**
     * 自有车取消订单
     **/
    private int ownerCarCancel(Long orderId, String cancelReason, Long userId) throws Exception {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setState(OrderState.ORDERCLOSE.getState());
        orderInfo.setUpdateBy(String.valueOf(userId));
        orderInfo.setOrderId(orderId);
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        int suc = orderInfoMapper.updateOrderInfo(orderInfo);
        this.insertOrderStateTraceInfo(orderId, OrderState.ORDERCANCEL.getState(), cancelReason, userId);
        return suc;
    }

    /**
     * 网约车取消订单
     */
    private OrderPayInfo onlineCarCancel(Long orderId, String cancelReason, Long userId, BigDecimal cancelFee, BigDecimal ownerFee, BigDecimal persionFee) throws Exception {
        int i = this.ownerCarCancel(orderId, cancelReason, userId);
        if (i != ONE) {
            throw new Exception("取消订单失败!");
        }
        OrderPayInfo orderPayInfo = null;
        if (cancelFee != null && cancelFee.compareTo(BigDecimal.ZERO) == 1) {
            String json = orderSettlingInfoService.formatCostFee(new OrderSettlingInfoVo(), persionFee, ownerFee);
            orderPayInfo = iOrderPayInfoService.insertOrderPayAndSetting(orderId, cancelFee, String.valueOf(ZERO), String.valueOf(ZERO), json, userId, persionFee);
        }
        return orderPayInfo;
    }

    /**
     * 插入订单轨迹
     */
    private void insertOrderStateTraceInfo(Long orderId, String state, String cancelReason, Long userId) {
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(orderId);
        orderStateTraceInfo.setState(state);
        orderStateTraceInfo.setContent(cancelReason);
        orderStateTraceInfo.setCreateBy(userId + "");
        orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
        orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
    }

    /**
     * 获取司机的任务列表
     *
     * @return
     */
    @Override
    public List<OrderDriverListInfo> getDriverOrderList(LoginUser loginUser, int pageNum, int pageSize) throws Exception {
        SysDriver driverInfo = loginUser.getDriver();
        if (driverInfo == null) {
            throw new Exception("当前登录人不是司机");
        }
        Long driverId = driverInfo.getDriverId();
        int flag = 0;
        if (pageNum == 1) {//首次刷新
            String states = OrderState.ALREADYSENDING.getState() + "," + OrderState.REASSIGNMENT.getState() + "," + OrderState.READYSERVICE.getState() + "," + OrderState.INSERVICE.getState();
            int count = orderInfoMapper.getDriverOrderCount(driverId, states);
            if (count > 20) {
                flag = 1;
            }
        }
       PageHelper.startPage(pageNum,pageSize);
        List<OrderDriverListInfo> driverOrderList = orderInfoMapper.getDriverOrderList(driverId,flag);
        return driverOrderList;
    }

    @Override
    public Integer getDriverOrderListCount(LoginUser loginUser) throws Exception {
        SysDriver driverInfo = loginUser.getDriver();
        if (driverInfo == null) {
            throw new Exception("当前登录人不是司机");
        }
        Long driverId = driverInfo.getDriverId();
        String states = OrderState.ALREADYSENDING.getState() + "," + OrderState.REASSIGNMENT.getState() + "," + OrderState.READYSERVICE.getState()
                + "," + OrderState.INSERVICE.getState() + "," + OrderState.ORDERCLOSE.getState() + "," + OrderState.STOPSERVICE.getState();
        return orderInfoMapper.getDriverOrderListCount(driverId, states);
    }


    @Async
    public void refreshRealAddr(Double longitude, Double latitude, String type, OrderInfo orderInfoOld) throws Exception {
        String longAddr = "";
        String shortAddr = "";
        if (longitude == null || latitude == null) {
            return;
        }
        Map<String, String> stringStringMap = thirdService.locationByLongitudeAndLatitude(String.valueOf(longitude), String.valueOf(latitude));
        if (MapUtils.isEmpty(stringStringMap)) {
            return;
        }
        longAddr = stringStringMap.get("longAddr");
        shortAddr = stringStringMap.get("shortAddr");
        //订单地址表
        OrderAddressInfo orderAddressInfoOld = new OrderAddressInfo();
        orderAddressInfoOld.setOrderId(orderInfoOld.getOrderId());
        orderAddressInfoOld.setType(type);
        List<OrderAddressInfo> orderAddressInfos = orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfoOld);
        OrderAddressInfo orderAddressInfo;
        if (!CollectionUtils.isEmpty(orderAddressInfos)) {
            orderAddressInfo = orderAddressInfos.get(0);
            orderAddressInfo.setActionTime(DateUtils.getNowDate());
            orderAddressInfo.setLongitude(longitude);
            orderAddressInfo.setLatitude(latitude);
            orderAddressInfo.setAddress(shortAddr);
            orderAddressInfo.setAddressLong(longAddr);
            orderAddressInfo.setUpdateBy(CommonConstant.START);
            orderAddressInfo.setUpdateTime(DateUtils.getNowDate());
            orderAddressInfoMapper.updateOrderAddressInfo(orderAddressInfo);
        } else {
            orderAddressInfo = new OrderAddressInfo();
            orderAddressInfo.setType(type);
            orderAddressInfo.setOrderId(orderInfoOld.getOrderId());
            orderAddressInfo.setCityPostalCode(null);
            orderAddressInfo.setActionTime(DateUtils.getNowDate());
            orderAddressInfo.setLongitude(longitude);
            orderAddressInfo.setLatitude(latitude);
            orderAddressInfo.setAddress(shortAddr);
            orderAddressInfo.setAddressLong(longAddr);
            orderAddressInfo.setJourneyId(orderInfoOld.getJourneyId());
            orderAddressInfo.setNodeId(orderInfoOld.getNodeId());
            orderAddressInfo.setPowerId(orderInfoOld.getPowerId());
            orderAddressInfo.setDriverId(orderInfoOld.getDriverId());
            orderAddressInfo.setCarId(orderInfoOld.getCarId());
            orderAddressInfo.setUserId(orderInfoOld.getUserId() + "");
            orderAddressInfo.setCreateBy(CommonConstant.START);
            orderAddressInfo.setCreateTime(DateUtils.getNowDate());
            orderAddressInfoMapper.insertOrderAddressInfo(orderAddressInfo);
        }
    }

    /**
     * 获取直接调度列表
     *
     * @param
     * @param
     * @return
     */
    @Override
    public Map<String,Object> queryDispatchOrder(LoginUser loginUser, ApplyDispatchQuery query) {
        List<DispatchVo> result = new ArrayList<DispatchVo>();
        //判断登录人的身份来显示他看到的不同权限的数据
        List<SysRole> role = loginUser.getUser().getRoles();
        //<系统管理员身份>
        List<DispatchVo> adminOrderList = new ArrayList<DispatchVo>();
        //<调度员身份>
        List<DispatchVo> dispatcherOrderList = new ArrayList<DispatchVo>();
        if (!role.isEmpty()) {
            //登录人公司
            Long companyId = loginUser.getUser().getDept().getCompanyId();
            //登录人Id
            Long userId = loginUser.getUser().getUserId();
            //循环判断登录人身份并获取他所看到的数据信息
            for (SysRole sysRole : role) {
                //判断登陆人身份（系统管理员）
                if (sysRole.getRoleKey().equals(RoleConstant.ADMIN) || sysRole.getRoleKey().equals(RoleConstant.SUB_ADMIN)) {
                    //本公司所有的订单
                    query.setCompanyId(loginUser.getUser().getDept().getCompanyId());
                    adminOrderList = orderInfoMapper.queryDispatchOrder(query);
                }
                //判断登陆人身份（调度员）
                if (sysRole.getRoleKey().equals(RoleConstant.DISPATCHER)) {
                    //调度员所看到的订单
                    query.setUserId(loginUser.getUser().getUserId());
                    dispatcherOrderList = orderInfoMapper.queryDispatchOrder(query);
                }
            }
        }
        if (adminOrderList.isEmpty()) {
            adminOrderList.addAll(dispatcherOrderList);
        }
       /* Page<DispatchVo> page = new Page<>(adminOrderList, query.getPageSize());
        if (dispatcherOrderList.isEmpty()) {
            Long total = 0L;
            Integer pageSize = 0;
            return new PageResult<>(total, pageSize, page.getCurrentPageData());
        }
        page.setCurrent_page(query.getPageNum());
        return new PageResult<>(Long.valueOf(page.getTotal_sum()), page.getCurrent_page(), page.getCurrentPageData());*/
        Map<String,Object> map = new HashMap();
        List<DispatchVo> page = PageUtil.startPage(adminOrderList,query.getPageNum(),query.getPageSize());
        Integer count = adminOrderList.size();
        Integer totalPage = count % query.getPageNum() == 0 ? count / query.getPageNum() : count / query.getPageNum() + 1;
        map.put("totalPage", count);
        map.put("page", page);
        map.put("list", totalPage);
        return map;
    }

    /**
     * 获取改派调度列表
     * @param query
     * @param loginUser
     * @return
     */
    @Override
    public Map<String,Object> queryDispatchReassignmentList(ApplyDispatchQuery query, LoginUser loginUser) {
        //判断登录人的身份来显示他看到的不同权限的数据
        SysUser user = loginUser.getUser();
        List<SysRole> role = loginUser.getUser().getRoles();
        Long companyId = user.getOwnerCompany();
        query.setCompanyId(companyId);
        query.setUserId(user.getUserId());
        //<系统管理员身份>
        List<DispatchVo> adminOrderList = new ArrayList<DispatchVo>();
        //<调度员身份>
        List<DispatchVo> dispatcherOrderList = new ArrayList<DispatchVo>();
        /**查寻该调度员可用查看的所有申请人*/
        if ("1".equals(user.getItIsDispatcher())) {//是调度员
            dispatcherOrderList = orderInfoMapper.queryDispatchReassignmentList(query);
        }
        List<SysRole> collect = role.stream().filter(p -> CommonConstant.ADMIN_ROLE.equals(p.getRoleKey()) || CommonConstant.SUB_ADMIN_ROLE.equals(p.getRoleKey())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {//是管理员
            if (!CollectionUtils.isEmpty(dispatcherOrderList)) {
                List<Long> orderIds = dispatcherOrderList.stream().map(p -> p.getOrderId()).collect(Collectors.toList());
                query.setOrderIds(orderIds);
            }
            //本公司所有的订单
            adminOrderList = orderInfoMapper.queryAdminDispatchReassignmentList(query);
            if (!CollectionUtils.isEmpty(adminOrderList)) {
                dispatcherOrderList.addAll(adminOrderList);
            }
        }
        /*com.hq.ecmp.util.Page<DispatchVo> page = new Page<>(dispatcherOrderList, query.getPageSize());
        if (dispatcherOrderList.isEmpty()) {
            Long total = 0L;
            Integer pageSize = 0;
            return new PageResult<>(total, pageSize, page.getCurrentPageData());
        }
        page.setCurrent_page(query.getPageNum());
        return new PageResult<>(Long.valueOf(page.getTotal_sum()), page.getCurrent_page(), page.getCurrentPageData());*/
        Map<String,Object> map = new HashMap();
        List<DispatchVo> page = PageUtil.startPage(dispatcherOrderList,query.getPageNum(),query.getPageSize());
        Integer count = dispatcherOrderList.size();
        Integer totalPage = count % query.getPageNum() == 0 ? count / query.getPageNum() : count / query.getPageNum() + 1;
        map.put("totalPage", count);
        map.put("page", page);
        map.put("list", totalPage);
        return map;
    }

    /**
     *
     * @param userApplySingleVo
     * @param loginUser
     * @return
     */
    @Override
    public PageResult<UserApplySingleVo> getUseApplySearchList(UserApplySingleVo userApplySingleVo, LoginUser loginUser) {
        Long companyId = loginUser.getUser().getOwnerCompany();
        userApplySingleVo.setCompanyId(companyId);
//        userApplySingleVo.setUserId(loginUser.getUser().getUserId());
        userApplySingleVo.setDeptId(loginUser.getUser().getDeptId());
        PageHelper.startPage(userApplySingleVo.getPageNum(), userApplySingleVo.getPageSize());
        List<UserApplySingleVo> useApplyList = orderInfoMapper.getUseApplySearchList(userApplySingleVo);
        for (UserApplySingleVo userApplySingle : useApplyList){
            if(StringUtils.isNotBlank(userApplySingle.getEndAddress()) && StringUtils.isNotBlank(userApplySingle.getAddressInfo())){
                 String endAddress = userApplySingle.getEndAddress()+","+userApplySingle.getAddressInfo();
                userApplySingle.setEndAddress(endAddress);
            }else{
                userApplySingle.setEndAddress(userApplySingle.getEndAddress());
            }
        }
        PageInfo<UserApplySingleVo> info = new PageInfo<>(useApplyList);
        return new PageResult<>(info.getTotal(), info.getPages(), useApplyList);
    }

    @Override
    public PageResult<UserApplySingleVo> getUseApplyList(UserApplySingleVo userApplySingleVo, LoginUser loginUser) {
        Long companyId = loginUser.getUser().getDept().getCompanyId();
//        userApplySingleVo.setUserId(loginUser.getUser().getUserId());
        userApplySingleVo.setDeptId(loginUser.getUser().getDeptId());
        userApplySingleVo.setCompanyId(companyId);
        PageHelper.startPage(userApplySingleVo.getPageNum(), userApplySingleVo.getPageSize());
        List<UserApplySingleVo> useApplyList = orderInfoMapper.getUseApplySearchList(userApplySingleVo);
        PageInfo<UserApplySingleVo> info = new PageInfo<>(useApplyList);
        return new PageResult<>(info.getTotal(), info.getPages(), useApplyList);
    }

    @Override
    public List<UserApplySingleVo> getUseApplyCounts(UserApplySingleVo userApplySingleVo, LoginUser loginUser) {
        Long companyId = loginUser.getUser().getDept().getCompanyId();
//        userApplySingleVo.setUserId(loginUser.getUser().getUserId());
        userApplySingleVo.setDeptId(loginUser.getUser().getDeptId());
        userApplySingleVo.setCompanyId(companyId);
        userApplySingleVo.setHomeDynamicBeginTime(userApplySingleVo.getHomeDynamicBeginTime().substring(0, 10)+" 00:00:00");
        userApplySingleVo.setHomeDynamicEndTime(userApplySingleVo.getHomeDynamicEndTime().substring(0, 10)+ " 23:59:59");
        List<UserApplySingleVo> useApplyList = orderInfoMapper.getUseApplyCounts(userApplySingleVo);
        return useApplyList;
    }

    @Override
    public PageResult<UserApplySingleVo> getToBeConfirmedOrder(UserApplySingleVo userApplySingleVo, LoginUser loginUser) {
        Long companyId = loginUser.getUser().getDept().getCompanyId();
        userApplySingleVo.setCompanyId(companyId);
//        userApplySingleVo.setUserId(loginUser.getUser().getUserId());
        userApplySingleVo.setDeptId(loginUser.getUser().getDeptId());
        PageHelper.startPage(userApplySingleVo.getPageNum(), userApplySingleVo.getPageSize());
        List<UserApplySingleVo> useApplyList = orderInfoMapper.getUseApplyCounts(userApplySingleVo);
        PageInfo<UserApplySingleVo> info = new PageInfo<>(useApplyList);
        return new PageResult<>(info.getTotal(), info.getPages(), useApplyList);
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
        return result;
    }

    /**
     * 佛山包车后管直接调度列表
     *
     * @param query
     * @param loginUser
     * @return
     */
    @Override
    public Map<String,Object> queryDispatchListCharterCar(ApplyDispatch query, LoginUser loginUser) {
        //判断登录人的身份来显示他看到的不同权限的数据
        SysUser user = loginUser.getUser();
        List<SysRole> role = loginUser.getUser().getRoles();
        Long companyId = user.getOwnerCompany();
        query.setCompanyId(companyId);
        query.setUserId(user.getUserId());
        //<系统管理员身份>
        List<DispatchVo> adminOrderList = new ArrayList<DispatchVo>();
        //<调度员身份>
        List<DispatchVo> dispatcherOrderList = new ArrayList<DispatchVo>();
        /**查寻该调度员可用查看的所有申请人*/

        if ("1".equals(user.getItIsDispatcher())) {//是调度员
            dispatcherOrderList = orderInfoMapper.queryDispatchListCharterCar(query);
            CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
            CarGroupDispatcherInfo carGroupDispatcherInfo1 = carGroupDispatcherInfo;
            carGroupDispatcherInfo1.setUserId(query.getUserId());
            List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo1);
            List<Long> dispatcheers = carGroupDispatcherInfos.stream().map(p ->p.getCarGroupId()).collect(Collectors.toList());
            Long carGroupId = carGroupDispatcherInfos.get(0).getCarGroupId();
            CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            if (carGroupInfo.getItIsInner().equals(CarConstant.IT_IS_USE_INNER_CAR_GROUP_IN)){
                for (DispatchVo dispatchVo:
                        dispatcherOrderList) {
                    dispatchVo.setInOrOut(1);
                }
            }else{
                Iterator<DispatchVo> iterator = dispatcherOrderList.iterator();
                while(iterator.hasNext()){
                    DispatchVo next = iterator.next();
                    next.setInOrOut(2);
                    if(next.getNextCarGroupId() == null || !dispatcheers.contains(next.getNextCarGroupId())){
                        iterator.remove();
                    }
                }
            }
        }
        List<SysRole> collect = role.stream().filter(p -> CommonConstant.ADMIN_ROLE.equals(p.getRoleKey()) || CommonConstant.SUB_ADMIN_ROLE.equals(p.getRoleKey())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {//是管理员
            if (!CollectionUtils.isEmpty(dispatcherOrderList)) {
                List<Long> orderIds = dispatcherOrderList.stream().map(p -> p.getOrderId()).collect(Collectors.toList());
                query.setOrderIds(orderIds);
            }

            //本公司所有的订单
            adminOrderList = orderInfoMapper.queryAdminDispatchList2(query);
            if (!CollectionUtils.isEmpty(adminOrderList)) {
                dispatcherOrderList.addAll(adminOrderList);
            }
        }

        //手动分页
        Map<String,Object> map = new HashMap();
        for(DispatchVo dispatchVo : dispatcherOrderList){
            if(StringUtils.isNotBlank(dispatchVo.getEndSite()) && StringUtils.isNotBlank(dispatchVo.getAddressInfo())){
                String endAddress = dispatchVo.getEndSite()+","+dispatchVo.getAddressInfo();
                dispatchVo.setEndSite(endAddress);
            }else{
                dispatchVo.setEndSite(dispatchVo.getEndSite());
            }
        }
        SortListUtil.sort(dispatcherOrderList, "orderByState", SortListUtil.ASC);
        List<DispatchVo> page = PageUtil.startPage(dispatcherOrderList,query.getPageN(),query.getPageS());
        Integer count = dispatcherOrderList.size();
        Integer totalPage = count % query.getPageN() == 0 ? count / query.getPageN() : count / query.getPageN() + 1;
        map.put("totalPage", count);
        map.put("page", page);
        map.put("list", totalPage);
        return map;
    }

    /**
     * 佛山包车后管直接调度列表
     *
     * @param query
     * @param loginUser
     * @return
     */
    @Override
    public PageResult<DispatchVo> queryHomePageDispatchListCharterCar(ApplyDispatchQuery query, LoginUser loginUser) {
        //判断登录人的身份来显示他看到的不同权限的数据
        SysUser user = loginUser.getUser();
        List<SysRole> role = loginUser.getUser().getRoles();
        Long companyId = user.getOwnerCompany();
        query.setCompanyId(companyId);
        query.setUserId(user.getUserId());
        //<调度员身份>
        List<DispatchVo> dispatcherOrderList = new ArrayList<DispatchVo>();
        /**查寻该调度员可用查看的所有申请人*/
        //是首页
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        int sum = 0;
        if ("1".equals(user.getItIsDispatcher())) {//是调度员
            dispatcherOrderList = orderInfoMapper.queryHomePageDispatchListCharterCar(query);
            CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
            CarGroupDispatcherInfo carGroupDispatcherInfo1 = carGroupDispatcherInfo;
            carGroupDispatcherInfo1.setUserId(query.getUserId());
            List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo1);
            List<Long> dispatcheers = carGroupDispatcherInfos.stream().map(p ->p.getCarGroupId()).collect(Collectors.toList());
            Long carGroupId = carGroupDispatcherInfos.get(0).getCarGroupId();
            CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            if (carGroupInfo.getItIsInner().equals(CarConstant.IT_IS_USE_INNER_CAR_GROUP_IN)){
                for (DispatchVo dispatchVo:
                        dispatcherOrderList) {
                    dispatchVo.setInOrOut(1);
                }
            }else{
                Iterator<DispatchVo> iterator = dispatcherOrderList.iterator();
                while(iterator.hasNext()){
                    DispatchVo next = iterator.next();
                    next.setInOrOut(2);
                    if(next.getNextCarGroupId() == null || !dispatcheers.contains(next.getNextCarGroupId())){
                        iterator.remove();
                        sum ++;
                    }
                }
            }
        }
        PageInfo<DispatchVo> info = new PageInfo<>(dispatcherOrderList);
        PageResult<DispatchVo> dispatchVoPageResult = new PageResult<>(info.getTotal()-sum, info.getPages(), dispatcherOrderList);
        log.info("首页查询出来的调度列表数据为---------------------------------"+dispatchVoPageResult);
        return dispatchVoPageResult;
    }

    /**
     * 佛山包车后管首页动态
     *
     * @param query
     * @param loginUser
     * @return
     */
    @Override
    public List<DispatchVo> queryDispatchListCharterCars(ApplyDispatchQuery query, LoginUser loginUser) {
        //判断登录人的身份来显示他看到的不同权限的数据
        SysUser user = loginUser.getUser();
        List<SysRole> role = loginUser.getUser().getRoles();
        Long companyId = user.getOwnerCompany();
        query.setCompanyId(companyId);
        query.setUserId(user.getUserId());
        query.setHomeDynamicBeginTime(query.getHomeDynamicBeginTime()+" 00:00:00");
        query.setHomeDynamicEndTime(query.getHomeDynamicEndTime()+ " 23:59:59");
        query.setIsIndex(2);
        //<系统管理员身份>
        List<DispatchVo> adminOrderList = new ArrayList<DispatchVo>();
        //<调度员身份>
        List<DispatchVo> dispatcherOrderList = new ArrayList<DispatchVo>();
        /**查寻该调度员可用查看的所有申请人*/
        if ("1".equals(user.getItIsDispatcher())) {//是调度员
            dispatcherOrderList = orderInfoMapper.queryHomePageDispatchListCharterCarCounts(query);
            CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
            CarGroupDispatcherInfo carGroupDispatcherInfo1 = carGroupDispatcherInfo;
            carGroupDispatcherInfo1.setUserId(query.getUserId());
            List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo1);
            List<Long> dispatcheers = carGroupDispatcherInfos.stream().map(p ->p.getCarGroupId()).collect(Collectors.toList());
            Long carGroupId = carGroupDispatcherInfos.get(0).getCarGroupId();
            CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            if (carGroupInfo.getItIsInner().equals(CarConstant.IT_IS_USE_INNER_CAR_GROUP_IN)){
                for (DispatchVo dispatchVo:
                        dispatcherOrderList) {
                    dispatchVo.setInOrOut(1);
                }
            }else{
                Iterator<DispatchVo> iterator = dispatcherOrderList.iterator();
                while(iterator.hasNext()){
                    DispatchVo next = iterator.next();
                    next.setInOrOut(2);
                    if(next.getNextCarGroupId() == null || !dispatcheers.contains(next.getNextCarGroupId())){
                        iterator.remove();
                    }
                }
            }
        }
        return dispatcherOrderList;
    }

    /**
     * 外部车队列表
     * @param deptId
     * @return
     */
    @Override
    public List<CarGroupInfo> applySingleCarGroupList(Long deptId) {
        return carGroupInfoMapper.applySingleCarGroupList(CarConstant.START_UP_CAR_GROUP,CarConstant.IT_IS_USE_INNER_CAR_GROUP_OUT,deptId);
    }

    @Override
    public List<CarGroupInfo> userDeptCarGroupList(Long deptId){
        return carGroupInfoMapper.userDeptCarGroupList(CarConstant.START_UP_CAR_GROUP,deptId);
    }

    @Override
    public List<CarGroupInfo> dispatcherCarGroupList(Long orderId, LoginUser loginUser,String carGroupUserMode) {
        List<CarGroupInfo> carGroupInfos = new ArrayList<>();
        OrderInfo orderInfo1 = orderInfoMapper.selectOrderInfoById(orderId);
        Long userId = orderInfo1.getUserId();
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(userId);
        Long deptId = ecmpUser.getDeptId();
        Long companyId = ecmpUser.getOwnerCompany();
        //获取车型id
        Long carTypeId = applyInfoMapper.getApplyCarTypeIdWithOrderId(orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        //包车类型
        String charterCarType = journeyInfo.getCharterCarType();

        OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
        orderAddressInfo.setOrderId(orderId);
        orderAddressInfo.setType("A000");
        List<OrderAddressInfo> orderAddressInfos = orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfo);
        OrderAddressInfo orderAddressInfo1 = orderAddressInfos.get(0);
        //用车城市
        String cityCode = orderAddressInfo1.getCityPostalCode();
        if (deptId != null) {
            //获取车队列表
            carGroupInfos = carGroupInfoMapper.dispatcherCarGroupList(deptId);
            if(carGroupInfos.size()>0){
                Iterator<CarGroupInfo> iterator = carGroupInfos.iterator();
                while(iterator.hasNext()){
                    CarGroupInfo next = iterator.next();
                    if (charterCarType.equals(CharterTypeEnum.HALF_DAY_TYPE.getKey())||charterCarType.equals(CharterTypeEnum.OVERALL_RENT_TYPE.getKey())){
                        List<CostConfigInfo> costConfigInfos = costConfigInfoMapper.selectCostConfigInfosByCondition(companyId, carTypeId, next.getCarGroupId(), charterCarType, carGroupUserMode, cityCode);
                        if(CollectionUtils.isEmpty(costConfigInfos)){
                            iterator.remove();
                        }
                    }else if(charterCarType.equals(CharterTypeEnum.MORE_RENT_TYPE.getKey())){
                        //包车天数
                        String useTime = journeyInfo.getUseTime();
                        double useTimeDouble = Double.parseDouble(useTime);
                        double v = useTimeDouble % 1;
                        int flag = 0;
                        if(!"0.5".equals(useTime) && v == 0.5){
                            //整日和半日
                            flag = 1;
                        }
                        if(v == 0){
                            //整日租
                            flag =2;
                        }
                        List<CostConfigInfo> costConfigInfosOverAll = costConfigInfoMapper.selectCostConfigInfosByCondition(companyId, carTypeId, next.getCarGroupId(), CharterTypeEnum.OVERALL_RENT_TYPE.getKey(), carGroupUserMode, cityCode);
                        if(flag == 1){
                            List<CostConfigInfo> costConfigInfosHalf = costConfigInfoMapper.selectCostConfigInfosByCondition(companyId, carTypeId, next.getCarGroupId(), CharterTypeEnum.HALF_DAY_TYPE.getKey(), carGroupUserMode, cityCode);
                            if (CollectionUtils.isEmpty(costConfigInfosHalf) || CollectionUtils.isEmpty(costConfigInfosOverAll)){
                                iterator.remove();
                            }
                        }else if(flag == 2){
                            if (CollectionUtils.isEmpty(costConfigInfosOverAll)){
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }
        return carGroupInfos;
    }

    /**
     * 佛山内外调度派车
     *
     * @param dispatchSendCarDto
     */
    @Override
    public void dispatcherSendCar(DispatchSendCarDto dispatchSendCarDto) {
        String useCarGroupType = dispatchSendCarDto.getUseCarGroupType();

    }


    @Override
    public int toSureToBeConfirmedOrder(UserApplySingleVo userApplySingleVo, LoginUser loginUser) {
//        Long companyId = loginUser.getUser().getDept().getCompanyId();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(userApplySingleVo.getOrderId());
        orderInfo.setState(userApplySingleVo.getHomePageToBeConfirmedState());
        int i = orderInfoMapper.updateOrderInfo(orderInfo);
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(userApplySingleVo.getOrderId());
        orderStateTraceInfo.setState(userApplySingleVo.getHomePageToBeConfirmedState());
        orderStateTraceInfo.setCreateTime(new Date());
        int j = iOrderStateTraceInfoService.insertOrderStateTraceInfo(orderStateTraceInfo);
        if (i == 1 && j == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dismissedDispatch(ApplyDispatchQuery query, LoginUser loginUser) throws Exception {
        SysUser user = loginUser.getUser();
        if (!String.valueOf(CommonConstant.ONE).equals(user.getItIsDispatcher())) {
            throw new BaseException("只有调度员身份才可驳回!");
        }
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(query.getOrderId());
        int state = Integer.parseInt(orderInfo.getState().substring(1));
        if (state >= Integer.parseInt(OrderState.ALREADYSENDING.getState().substring(1))) {
            throw new BaseException("此订单已派车不可驳回!");
        }
        if (query.getInOrOut() == ONE) {//内部调度员
            /**修改订单状态,插入轨迹*/
            orderInfo.setState(OrderState.ORDERCLOSE.getState());
            orderInfo.setUpdateBy(user.getUserId().toString());
            orderInfo.setUpdateTime(DateUtils.getNowDate());
            orderInfoMapper.updateOrderInfo(orderInfo);
            OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo(orderInfo.getOrderId(), OrderState.ORDERDENIED.getState());
            orderStateTraceInfo.setCreateBy(user.getUserId().toString());
            orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
            orderStateTraceInfo.setContent(query.getRejectReason());
            orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
            //修改申请单状态为驳回
            List<ApplyInfo> applyInfos = applyInfoMapper.selectApplyInfoList(new ApplyInfo(orderInfo.getJourneyId()));
            if (!CollectionUtils.isEmpty(applyInfos)){
                ApplyInfo applyInfo = applyInfos.get(0);
                applyInfo.setState(ApplyStateConstant.REJECT_APPLY);
                applyInfo.setUpdateBy(user.getUserId().toString());
                applyInfo.setUpdateTime(DateUtils.getNowDate());
                applyInfoMapper.updateApplyInfo(applyInfo);
            }
            ismsBusiness.sendSmsInnerDispatcherReject(orderInfo,query.getRejectReason());
        } else {
            /**订单状态不变,给内部调度员发短信通知**/
            List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos = dispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoList(new OrderDispatcheDetailInfo(query.getOrderId()));
            OrderDispatcheDetailInfo orderDispatcheDetailInfo = null;
            if (!CollectionUtils.isEmpty(orderDispatcheDetailInfos)) {
                orderDispatcheDetailInfo = orderDispatcheDetailInfos.get(0);
                orderDispatcheDetailInfo.setOuterCarGroupRefuseInfo(query.getRejectReason());
                orderDispatcheDetailInfo.setUpdateBy(user.getUserId().toString());
                orderDispatcheDetailInfo.setUpdateTime(DateUtils.getNowDate());
                dispatcheDetailInfoMapper.updateOrderDispatcheDetailInfo(orderDispatcheDetailInfo);
            }
            //给内部调度员发短信
            ismsBusiness.sendSmsDispatchReject(orderInfo,query.getRejectReason());

        }

    }

    /**取车*/
    @Override
    public void pickUpTheCar(Long userId, Long orderId) throws Exception {
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        if (orderInfo==null){
            throw new BaseException("订单不存在");
        }
        List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos = dispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoList(new OrderDispatcheDetailInfo(orderId));
        if (CollectionUtils.isEmpty(orderDispatcheDetailInfos)){
            log.error("订单:"+orderId+"的调度详情不存在!");
            throw new BaseException("订单异常");
        }
        OrderDispatcheDetailInfo orderDispatcheDetailInfo = orderDispatcheDetailInfos.get(0);
        if (!CommonConstant.PASS.equals(orderDispatcheDetailInfo.getItIsSelfDriver())){
            log.error("订单:"+orderId+"的服务模式不是自驾");
            throw new BaseException("此订单非自驾");
        }
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        if (journeyInfo==null){
            throw new BaseException("该行程不存在");
        }
        if (!OrderState.ALREADYSENDING.getState().equals(orderInfo.getState())){
            log.error("订单:"+orderId+"的状态为"+orderInfo.getState()+",服务模式不是自驾");
            throw new BaseException("当前状态不可取车");
        }
//        orderInfo.setState(OrderState.PICKUPCAR.getState());
//        orderInfo.setUpdateBy(userId.toString());
//        orderInfo.setUpdateTime(DateUtils.getNowDate());
//        orderInfoMapper.updateOrderInfo(orderInfo);
        OrderStateTraceInfo stateTraceInfo=new OrderStateTraceInfo();
        stateTraceInfo.setOrderId(orderId);
        stateTraceInfo.setContent("用车人已取车");
        stateTraceInfo.setCreateBy(String.valueOf(userId));
        stateTraceInfo.setCreateTime(new Date());
        stateTraceInfo.setState(OrderStateTrace.PICKUPCAR.getState());
        orderStateTraceInfoMapper.insertOrderStateTraceInfo(stateTraceInfo);

    }

    /**还车*/
    @Override
    public void returnCar(Long userId, Long orderId) throws Exception {
        OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
        if (orderInfo==null){
            throw new BaseException("订单不存在");
        }
        List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos = dispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoList(new OrderDispatcheDetailInfo(orderId));
        if (CollectionUtils.isEmpty(orderDispatcheDetailInfos)){
            log.error("订单:"+orderId+"的调度详情不存在!");
            throw new BaseException("订单异常");
        }
        OrderDispatcheDetailInfo orderDispatcheDetailInfo = orderDispatcheDetailInfos.get(0);
        if (!CommonConstant.PASS.equals(orderDispatcheDetailInfo.getItIsSelfDriver())){
            log.error("订单:"+orderId+"的服务模式不是自驾");
            throw new BaseException("此订单非自驾");
        }
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderInfo.getJourneyId());
        if (journeyInfo==null){
            throw new BaseException("该行程不存在");
        }
//        int day = DateFormatUtils.compareDay(journeyInfo.getEndDate(), new Date());
//        if (day==-1){
//            throw new BaseException("当前时间不可还车");
//        }
        if (!OrderState.INSERVICE.getState().equals(orderInfo.getState())){
            throw new BaseException("当前状态不可还车");
        }
        orderInfo.setState(OrderState.ORDERCLOSE.getState());
        orderInfo.setUpdateBy(userId.toString());
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        orderInfoMapper.updateOrderInfo(orderInfo);
        OrderStateTraceInfo stateTraceInfo=new OrderStateTraceInfo();
        stateTraceInfo.setOrderId(orderId);
        stateTraceInfo.setContent("用车人已还车");
        stateTraceInfo.setCreateBy(String.valueOf(userId));
        stateTraceInfo.setCreateTime(new Date());
        stateTraceInfo.setState(OrderStateTrace.ORDERCLOSE.getState());
        orderStateTraceInfoMapper.insertOrderStateTraceInfo(stateTraceInfo);
        /**计算自驾的费用*/
        CarInfo carInfo = carInfoMapper.selectCarInfoById(orderDispatcheDetailInfo.getCarId());
        if (carInfo==null){
            throw new BaseException("该订单车辆不存在!");
        }
        String charterCarType = journeyInfo.getCharterCarType();
        CharterTypeEnum charterTypeEnum = CharterTypeEnum.getCharterTypeEnum(charterCarType);
        Long carGroupId = orderDispatcheDetailInfo.getCarCgId();
        BigDecimal useTime = StringUtils.isNotEmpty(journeyInfo.getUseTime())?new BigDecimal(journeyInfo.getUseTime()):BigDecimal.ZERO;
        ApplyPriceDetails applyPriceDetails=new ApplyPriceDetails();
        applyPriceDetails.setCarGroupId(carGroupId);
        applyPriceDetails.setCarGroupUserMode(CostConfigModeEnum.Config_mode_CA01.getKey());
        applyPriceDetails.setCarTypeId(carInfo.getCarTypeId());
        BigDecimal amount=BigDecimal.ZERO;
        if (useTime.compareTo(BigDecimal.ONE)==0){//整日租
            applyPriceDetails.setRentType(charterTypeEnum.OVERALL_RENT_TYPE.getKey());
            List<ApplyPriceDetails> applyPriceDetails1 = costConfigInfoMapper.applySinglePriceDetails(applyPriceDetails);
            if (!CollectionUtils.isEmpty(applyPriceDetails1)){
                if (applyPriceDetails1.get(0).getCombosPrice()!=null){
                    amount=applyPriceDetails1.get(0).getCombosPrice();
                }
                OrderSettlingInfoVo orderSettlingInfoVo=new OrderSettlingInfoVo();
                orderSettlingInfoVo.setOrderId(orderId);
                orderSettlingInfoVo.setAmount(amount);
                orderSettlingInfoService.selfDriverCostPrice(orderSettlingInfoVo,userId,orderInfo.getCompanyId(),null);
            }
        }else if (useTime.compareTo(BigDecimal.ZERO)==1&&useTime.compareTo(BigDecimal.ONE)<0){//半日租
            applyPriceDetails.setRentType(charterTypeEnum.HALF_DAY_TYPE.getKey());
            List<ApplyPriceDetails> applyPriceDetails2 = costConfigInfoMapper.applySinglePriceDetails(applyPriceDetails);
            if (!CollectionUtils.isEmpty(applyPriceDetails2)){
                if (applyPriceDetails2.get(0).getCombosPrice()!=null){
                    amount=applyPriceDetails2.get(0).getCombosPrice();
                }
                OrderSettlingInfoVo orderSettlingInfoVo=new OrderSettlingInfoVo();
                orderSettlingInfoVo.setOrderId(orderId);
                orderSettlingInfoVo.setAmount(amount);
                orderSettlingInfoService.selfDriverCostPrice(orderSettlingInfoVo,userId,orderInfo.getCompanyId(),null);
            }
        }else if (useTime.compareTo(BigDecimal.ONE)==1){//多日租
            if (journeyInfo.getUseTime().contains(".5")){//包含半天的多日租
                applyPriceDetails.setRentType(charterTypeEnum.HALF_DAY_TYPE.getKey()+","+charterTypeEnum.OVERALL_RENT_TYPE.getKey());
                List<ApplyPriceDetails> applyPriceDetails1 = costConfigInfoMapper.applySinglePriceDetails(applyPriceDetails);
                BigDecimal number = new  BigDecimal(journeyInfo.getUseTime()).subtract(new BigDecimal("0.5"));
                if (!CollectionUtils.isEmpty(applyPriceDetails1)){
                    for (ApplyPriceDetails applyPrice:applyPriceDetails1){
                        if (charterTypeEnum.OVERALL_RENT_TYPE.getKey().equals(applyPrice.getRentType())){
                            amount=amount.add(applyPrice.getCombosPrice().multiply(number));
                        }else {
                            amount=amount.add(applyPrice.getCombosPrice());
                        }
                    }
                    OrderSettlingInfoVo orderSettlingInfoVo=new OrderSettlingInfoVo();
                    orderSettlingInfoVo.setOrderId(orderId);
                    orderSettlingInfoVo.setAmount(amount);
                    orderSettlingInfoService.selfDriverCostPrice(orderSettlingInfoVo,userId,orderInfo.getCompanyId(),null);
                }
            }else{
                applyPriceDetails.setRentType(charterTypeEnum.OVERALL_RENT_TYPE.getKey());
                List<ApplyPriceDetails> applyPriceDetails1 = costConfigInfoMapper.applySinglePriceDetails(applyPriceDetails);
                if (!CollectionUtils.isEmpty(applyPriceDetails1)){
                    if (applyPriceDetails1.get(0).getCombosPrice()!=null){
                        amount=applyPriceDetails1.get(0).getCombosPrice();
                    }
                    OrderSettlingInfoVo orderSettlingInfoVo=new OrderSettlingInfoVo();
                    orderSettlingInfoVo.setOrderId(orderId);
                    orderSettlingInfoVo.setAmount(amount.multiply(new BigDecimal(journeyInfo.getUseTime())));
                    orderSettlingInfoService.selfDriverCostPrice(orderSettlingInfoVo,userId,orderInfo.getCompanyId(),null);
                }
            }
        }



    }

    /**
     * 调度列表大sql优化
     * 1.通过调度员用户id，查询调度员所在车队可以服务的部门
     * 2.查询这些部门的人所需要服务的订单
     * @param query
     * @return
     */
    @Override
    public  Map<String,Object>  dispatchListCharterCarWithDispatcher(ApplyDispatch query,LoginUser loginUser) {
        //判断登录人的身份来显示他看到的不同权限的数据
        SysUser user = loginUser.getUser();
        List<SysRole> role = loginUser.getUser().getRoles();
        Long companyId = user.getOwnerCompany();
        query.setCompanyId(companyId);
        query.setUserId(user.getUserId());
        //<调度员身份>
        List<DispatchVo> dispatcherOrderList = new ArrayList<DispatchVo>();
        /**查寻该调度员可用查看的所有申请人*/
        Boolean isAdmin = false;
        Boolean isDispatcher = false;
        //查询当前调度员所在的车队，以及车队的服务城市和服务部门
        List<DispatchCarGroupDto> disCarGroupInfos= carGroupInfoMapper.getDisCarGroupInfoByUserId(query.getUserId(),query.getCompanyId());
        String carGroupIds = disCarGroupInfos.stream().map(p -> p.getCarGroupId().toString()).collect(Collectors.joining(",", "", ""));
        query.setCarGroupIds(carGroupIds);
        if ("1".equals(user.getItIsDispatcher())) {
            isDispatcher = true;
            if (!CollectionUtils.isEmpty(disCarGroupInfos)){
                String itIsInner = disCarGroupInfos.get(0).getItIsInner();
                if (itIsInner.equals(CarConstant.IT_IS_USE_INNER_CAR_GROUP_IN)){
                    query.setIsInnerDispatch(1);
                }else{
                    query.setIsInnerDispatch(2);
                }
            }
        }
        List<SysRole> collect = role.stream().filter(p -> CommonConstant.ADMIN_ROLE.equals(p.getRoleKey()) || CommonConstant.SUB_ADMIN_ROLE.equals(p.getRoleKey())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)) {//是管理员
            isAdmin = true;
        }
        if(isAdmin && isDispatcher){
            query.setRoleData(2);
        }else if (isAdmin && !isDispatcher){
            query.setRoleData(1);
        }else if(!isAdmin && isDispatcher){
            query.setRoleData(3);
        }
        //根据车队的服务城市和服务部门来查询对应的订单信息
        if (!CollectionUtils.isEmpty(disCarGroupInfos)){
             String collect1 = disCarGroupInfos.stream().map(p -> p.getDeptId().toString()).collect(Collectors.joining(",", "", ""));
             query.setDeptId(collect1);
        }
        PageHelper.startPage(query.getPageN(), query.getPageS());
        dispatcherOrderList = getDispatchOrderInfos(query);
        //获取各个状态的数量
        List<DisOrderStateCount> orderStateCount = new ArrayList<>(4);
        orderStateCount = orderInfoMapper.getOrderStateCount(query);
        if (CollectionUtils.isEmpty(orderStateCount)){
            orderStateCount.add(new DisOrderStateCount(DispatchOrderStateTraceEnum.WAITINGLIST.getStateName(),0));
            orderStateCount.add(new DisOrderStateCount(DispatchOrderStateTraceEnum.ALREADYSENDING.getStateName(),0));
            orderStateCount.add(new DisOrderStateCount(DispatchOrderStateTraceEnum.ORDERDENIED.getStateName(),0));
            orderStateCount.add(new DisOrderStateCount(DispatchOrderStateTraceEnum.ORDEROVERTIME.getStateName(),0));
        }else{
            List<String> collect1 = orderStateCount.stream().map(DisOrderStateCount::getState).collect(Collectors.toList());
            if(!collect1.contains(DispatchOrderStateTraceEnum.WAITINGLIST.getStateName())){
                orderStateCount.add(new DisOrderStateCount(DispatchOrderStateTraceEnum.WAITINGLIST.getStateName(),0));
            }
            if(!collect1.contains(DispatchOrderStateTraceEnum.ALREADYSENDING.getStateName())){
                orderStateCount.add(new DisOrderStateCount(DispatchOrderStateTraceEnum.ALREADYSENDING.getStateName(),0));
            }
            if(!collect1.contains(DispatchOrderStateTraceEnum.ORDERDENIED.getStateName())){
                orderStateCount.add(new DisOrderStateCount(DispatchOrderStateTraceEnum.ORDERDENIED.getStateName(),0));
            }
            if(!collect1.contains(DispatchOrderStateTraceEnum.ORDEROVERTIME.getStateName())){
                orderStateCount.add(new DisOrderStateCount(DispatchOrderStateTraceEnum.ORDEROVERTIME.getStateName(),0));
            }
            Collections.sort(orderStateCount);
        }
        PageInfo<DispatchVo> info = new PageInfo<>(dispatcherOrderList);
        Map<String,Object> map = new HashMap<>();
        map.put("totalPage", info.getTotal());
        map.put("page", info.getTotal());
        map.put("list", dispatcherOrderList);
        map.put("stateCount", orderStateCount);
        return map;
    }

    @Override
    public void updatePickupCarState() {
        //获取已取车的所有订单
        List<PiclUpCarOrderVO> orderList=orderStateTraceInfoMapper.selectOrderListByState(OrderStateTrace.PICKUPCAR.getState());
        if (CollectionUtils.isEmpty(orderList)){
            for (PiclUpCarOrderVO vo:orderList){
                int i = DateFormatUtils.compareDayAndTime(vo.getUseCarTime(), new Date());
                if (i==0){
                    ((OrderInfoTwoServiceImpl) AopContext.currentProxy()).updateOrderState(vo);
                }
            }
        }
    }

    @Override
    public UserApplySingleVo getOrderInfoDetail(Long orderId,SysUser user,Long applyId) throws Exception{
        UserApplySingleVo userApplySingleVo = new UserApplySingleVo();
        Long applyIdOrderId=applyId;
        ApplyInfo applyInfo=null;
        if (applyId==null){
            OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
            if (orderInfo==null) {
                throw new BaseException("该订单为空:"+orderId);
            }
            List<ApplyInfo> applyInfos = applyInfoMapper.selectApplyInfoList(new ApplyInfo(orderInfo.getJourneyId()));
            if (CollectionUtils.isEmpty(applyInfos)){
                throw new BaseException("该申请单为空对应行程id:"+orderInfo.getJourneyId());
            }
            applyInfo = applyInfos.get(0);
            applyIdOrderId=applyInfos.get(0).getApplyId();
        }
        List<UserApplySingleVo> useApplyList = applyInfoMapper.getApplyListPage(null,applyIdOrderId);
        if (CollectionUtils.isEmpty(useApplyList)){
            return null;
        }
        userApplySingleVo=useApplyList.get(0);
        userApplySingleVo.setDeptId(user.getDeptId());
        if (CarUserSelfDrivingEnum.ONE_SELF.getCode().equals(applyInfo.getItIsSelfDriver())){//自驾
            OrderSettlingInfo orderSettlingInfo = orderSettlingInfoMapper.selectOrderSettlingInfoByOrderId(orderId);
            Map<String, Object> orderFee = orderSettlingInfoService.getOrderFee(orderSettlingInfo);
            userApplySingleVo.setAmount(orderFee.get("amount").toString());
            userApplySingleVo.setOrderFees((List<OtherCostBean>)orderFee.get("otherCostBeans"));
        }
        return userApplySingleVo;
    }

    @Transactional
    public void updateOrderState(PiclUpCarOrderVO vo){
        OrderInfo orderInfo = new OrderInfo(vo.getOrderId(), OrderState.INSERVICE.getState());
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        orderInfoMapper.updateOrderInfo(orderInfo);
        OrderStateTraceInfo stateTraceInfo = new OrderStateTraceInfo(vo.getOrderId(), OrderState.INSERVICE.getState());
        stateTraceInfo.setCreateTime(DateUtils.getNowDate());
        stateTraceInfo.setContent("自驾取车开始服务");
        stateTraceInfo.setCreateBy(CommonConstant.START);
        orderStateTraceInfoMapper.insertOrderStateTraceInfo(stateTraceInfo);
    }

    /**
     * 通过城市code 和部门id查询对应的订单信息
     * @param query
     * @return
     */
    public List<DispatchVo> getDispatchOrderInfos(ApplyDispatch query){

        List<DispatchVo> orderInfos = orderInfoMapper.getOrderInfoByCityAndDept(query);
        if(!CollectionUtils.isEmpty(orderInfos)){
            Iterator<DispatchVo> iterator = orderInfos.iterator();
            while(iterator.hasNext()){
                DispatchVo dispatchVo = iterator.next();
                if(query.getRoleData() == 2){
                    List<Long> allUserIdByDispatchUserId = carGroupDispatcherInfoMapper.getAllUserIdByDispatchUserId(query.getUserId());
                        if(allUserIdByDispatchUserId.contains(dispatchVo.getUserId())){
                            dispatchVo.setOperationPermission("0");
                            if (query.getIsInnerDispatch() == 1){
                                dispatchVo.setInOrOut(1);
                            }else{
                                dispatchVo.setInOrOut(2);
                            }
                        }else{
                            dispatchVo.setOperationPermission("1");
                        }
                }
                //行程相关信息
                getDispatchOrderJourneyInfos(dispatchVo);
                //申请相关信息
                getDispatchOrderApplyInfos(dispatchVo);
                //场景和制度相关信息
                getSceneAndRegimeInfo(dispatchVo);
            }
        }
        return orderInfos;
    }

    /**
     * 包装行程相关信息
     */
    public void getDispatchOrderJourneyInfos(DispatchVo dispatchVo){
        DispatchVo dispatchJourneyInfoByJourneyId = journeyInfoMapper.getDispatchJourneyInfoByJourneyId(dispatchVo.getJourneyId());
        BeanUtils.copyProperties(dispatchJourneyInfoByJourneyId, dispatchVo,BeauUtilsCommon.getNullField(dispatchJourneyInfoByJourneyId));
    }

    /**
     * 包装申请相关的信息
     */
    public void getDispatchOrderApplyInfos(DispatchVo dispatchVo){
        DispatchVo dispatchApplyInfoByJourneyId = applyInfoMapper.getDispatchApplyInfoByJourneyId(dispatchVo.getJourneyId());
        JourneyAddressInfo journeyAddressInfo = new JourneyAddressInfo();
        journeyAddressInfo.setJourneyId(dispatchVo.getJourneyId());
        List<JourneyAddressInfo> journeyAddressInfos = journeyAddressInfoMapper.selectJourneyAddressInfoList(journeyAddressInfo);
        if (!CollectionUtils.isEmpty(journeyAddressInfos)){
            String endSite = dispatchVo.getEndSite();
            StringBuilder sb = new StringBuilder(endSite);
            for (JourneyAddressInfo journeyAddressInfo1:
                 journeyAddressInfos) {
               sb.append(",").append(journeyAddressInfo1.getAddressInfo());
            }
            dispatchVo.setEndSite(sb.toString());
        }
        BeanUtils.copyProperties(dispatchApplyInfoByJourneyId, dispatchVo, BeauUtilsCommon.getNullField(dispatchApplyInfoByJourneyId));

    }
    /**
     * 包装场景和制度信息
     */
    public void getSceneAndRegimeInfo(DispatchVo dispatchVo){
        DispatchVo dispatchReAndSceneInfo = regimeInfoMapper.getDispatchReAndSceneInfo(dispatchVo.getRegimeId());
        if (dispatchReAndSceneInfo != null){
            BeanUtils.copyProperties(dispatchReAndSceneInfo, dispatchVo,BeauUtilsCommon.getNullField(dispatchReAndSceneInfo));
        }
    }

    /**
     * 佛山后管申请单调度-获取用车单位列表
     * @return
     */
    @Override
    public List<EcmpOrg> getUseCarOrgList(Long companyId) {
        List<EcmpOrg>  useCarOrgList = ecmpOrgMapper.getUseCarOrgList(companyId);
        return useCarOrgList;
    }

}
