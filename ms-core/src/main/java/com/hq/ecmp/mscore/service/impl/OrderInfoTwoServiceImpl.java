package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.api.system.domain.SysDriver;
import com.hq.api.system.domain.SysRole;
import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.exception.BaseException;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.DispatchSendCarDto;
import com.hq.ecmp.mscore.dto.DriverCloudDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.Page;
import com.hq.ecmp.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import oshi.jna.platform.mac.SystemB;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Value("${thirdService.enterpriseId}") //企业编号
    private String enterpriseId;
    @Value("${thirdService.licenseContent}") //企业证书信息
    private String licenseContent;
    @Value("${thirdService.apiUrl}")//三方平台的接口前地址
    private String apiUrl;

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
    public PageResult<DispatchVo> queryDispatchList(ApplyDispatchQuery query, LoginUser loginUser) {
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
        Page<DispatchVo> page = new Page<>(dispatcherOrderList, query.getPageSize());
        if (dispatcherOrderList.isEmpty()) {
            Long total = 0L;
            Integer pageSize = 0;
            return new PageResult<>(total, pageSize, page.getCurrentPageData());
        }
        page.setCurrent_page(query.getPageNum());
        return new PageResult<>(Long.valueOf(page.getTotal_sum()), page.getCurrent_page(), page.getCurrentPageData());
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
    public PageResult<DispatchVo> queryDispatchOrder(LoginUser loginUser, ApplyDispatchQuery query) {
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
        Page<DispatchVo> page = new Page<>(adminOrderList, query.getPageSize());
        if (dispatcherOrderList.isEmpty()) {
            Long total = 0L;
            Integer pageSize = 0;
            return new PageResult<>(total, pageSize, page.getCurrentPageData());
        }
        page.setCurrent_page(query.getPageNum());
        return new PageResult<>(Long.valueOf(page.getTotal_sum()), page.getCurrent_page(), page.getCurrentPageData());
    }

    /**
     * 获取改派调度列表
     * @param query
     * @param loginUser
     * @return
     */
    @Override
    public PageResult<DispatchVo> queryDispatchReassignmentList(ApplyDispatchQuery query, LoginUser loginUser) {
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
        com.hq.ecmp.util.Page<DispatchVo> page = new Page<>(dispatcherOrderList, query.getPageSize());
        if (dispatcherOrderList.isEmpty()) {
            Long total = 0L;
            Integer pageSize = 0;
            return new PageResult<>(total, pageSize, page.getCurrentPageData());
        }
        page.setCurrent_page(query.getPageNum());
        return new PageResult<>(Long.valueOf(page.getTotal_sum()), page.getCurrent_page(), page.getCurrentPageData());
    }

    /**
     *
     * @param userApplySingleVo
     * @param loginUser
     * @return
     */
    @Override
    public PageResult<UserApplySingleVo> getUseApplySearchList(UserApplySingleVo userApplySingleVo, LoginUser loginUser) {
        Long companyId = loginUser.getUser().getDept().getCompanyId();
        userApplySingleVo.setCompanyId(companyId);
        userApplySingleVo.setUserId(loginUser.getUser().getUserId());
        PageHelper.startPage(userApplySingleVo.getPageNum(), userApplySingleVo.getPageSize());
        List<UserApplySingleVo> useApplyList = orderInfoMapper.getUseApplySearchList(userApplySingleVo);
        PageInfo<UserApplySingleVo> info = new PageInfo<>(useApplyList);
        return new PageResult<>(info.getTotal(), info.getPages(), useApplyList);
    }

    @Override
    public PageResult<UserApplySingleVo> getUseApplyList(UserApplySingleVo userApplySingleVo, LoginUser loginUser) {
        Long companyId = loginUser.getUser().getDept().getCompanyId();
        userApplySingleVo.setUserId(loginUser.getUser().getUserId());
        userApplySingleVo.setCompanyId(companyId);
        PageHelper.startPage(userApplySingleVo.getPageNum(), userApplySingleVo.getPageSize());
        List<UserApplySingleVo> useApplyList = orderInfoMapper.getUseApplySearchList(userApplySingleVo);
        PageInfo<UserApplySingleVo> info = new PageInfo<>(useApplyList);
        return new PageResult<>(info.getTotal(), info.getPages(), useApplyList);
    }

    @Override
    public List<UserApplySingleVo> getUseApplyCounts(UserApplySingleVo userApplySingleVo, LoginUser loginUser) {
        Long companyId = loginUser.getUser().getDept().getCompanyId();
        userApplySingleVo.setUserId(loginUser.getUser().getUserId());
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
        userApplySingleVo.setUserId(loginUser.getUser().getUserId());
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
    public PageResult<DispatchVo> queryDispatchListCharterCar(ApplyDispatchQuery query, LoginUser loginUser) {
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

        //是首页
        if(query.getIsIndex() == 1){
            PageHelper.startPage(query.getPageNum(), query.getPageSize());
            if ("1".equals(user.getItIsDispatcher())) {//是调度员
                dispatcherOrderList = orderInfoMapper.queryHomePageDispatchListCharterCar(query);
            }
            PageInfo<DispatchVo> info = new PageInfo<>(dispatcherOrderList);
            PageResult<DispatchVo> dispatchVoPageResult = new PageResult<>(info.getTotal(), info.getPages(), dispatcherOrderList);
            log.info("首页查询出来的调度列表数据为---------------------------------"+dispatchVoPageResult);
            return dispatchVoPageResult;
        }
        //为了区别分页情况
        //不是首页
        if (query.getIsIndex() == 2) {
            if ("1".equals(user.getItIsDispatcher())) {//是调度员
                dispatcherOrderList = orderInfoMapper.queryDispatchListCharterCar(query);
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
        }
        Page<DispatchVo> page = new Page<>(dispatcherOrderList, query.getPageSize());
        if (dispatcherOrderList.isEmpty()) {
            Long total = 0L;
            Integer pageSize = 0;
            return new PageResult<>(total, pageSize, page.getCurrentPageData());
        }
        page.setCurrent_page(query.getPageNum());
        return new PageResult<>(Long.valueOf(page.getTotal_sum()), page.getCurrent_page(), page.getCurrentPageData());
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
            dispatcherOrderList = orderInfoMapper.queryDispatchListCharterCar(query);
        }
        return dispatcherOrderList;
    }

    @Override
    public List<CarGroupInfo> dispatcherCarGroupList(Long orderId, LoginUser loginUser) {
        List<CarGroupInfo> carGroupInfos = new ArrayList<>();
        SysUser user = loginUser.getUser();
        Long userId = user.getUserId();
        Long deptId = user.getDeptId();
        if (deptId != null) {
            carGroupInfos = carGroupInfoMapper.dispatcherCarGroupList(deptId);
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
        orderInfo.setState(OrderState.INSERVICE.getState());
        orderInfo.setUpdateBy(userId.toString());
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        orderInfoMapper.updateOrderInfo(orderInfo);
        OrderStateTraceInfo stateTraceInfo=new OrderStateTraceInfo();
        stateTraceInfo.setOrderId(orderId);
        stateTraceInfo.setContent("用车人已取车");
        stateTraceInfo.setCreateBy(String.valueOf(userId));
        stateTraceInfo.setCreateTime(new Date());
        stateTraceInfo.setState(OrderStateTrace.SERVICE.getState());
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
        int day = DateFormatUtils.compareDay(journeyInfo.getEndDate(), new Date());
        if (day==-1){
            throw new BaseException("当前时间不可还车");
        }
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
    }

}
