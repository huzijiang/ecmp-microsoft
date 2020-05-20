package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Maps;
import com.hq.api.system.domain.SysRole;
import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.OkHttpUtil;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.MacTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.hq.ecmp.constant.CommonConstant.*;
import static com.hq.ecmp.constant.OrderState.WAITINGLIST;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class OrderInfoTwoServiceImpl implements OrderInfoTwoService
{

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Autowired
    private IJourneyUserCarPowerService iJourneyUserCarPowerService;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Resource
    private CarGroupDispatcherInfoMapper groupDispatcherInfoMapper;
    @Resource
    private IOrderSettlingInfoService orderSettlingInfoService;
    @Resource
    private IOrderPayInfoService iOrderPayInfoService;
    @Resource
    private JourneyInfoMapper journeyInfoMapper;
    @Resource
    private ThirdService thirdService;
    @Resource
    private IsmsBusiness ismsBusiness;

    @Value("${thirdService.enterpriseId}") //企业编号
    private String enterpriseId;
    @Value("${thirdService.licenseContent}") //企业证书信息
    private String licenseContent;
    @Value("${thirdService.apiUrl}")//三方平台的接口前地址
    private String apiUrl;

    /**
     * 公务取消订单
     * @param orderId
     * @param cancelReason
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public CancelOrderCostVO cancelBusinessOrder(Long orderId, String cancelReason,Long longinUserId) throws Exception{
        CancelOrderCostVO vo=new CancelOrderCostVO();
        BigDecimal cancelFee1=BigDecimal.ZERO;
        int isPayFee=0;
        String ownerAmount=null;
        String personalAmount=null;
        String payId=null;
        String payState=null;
        OrderStateVO orderStateVO = orderInfoService.getOrderState(orderId);
        if (!longinUserId.equals(orderStateVO.getUserId())){
            throw new  Exception("取消订单操作人与申请人不一致,不可取消");
        }
        if (orderStateVO==null){
            throw new  Exception("该订单不存在!");
        }
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(orderStateVO.getJourneyId());
        String state = orderStateVO.getState();
        Date useCarDate = orderStateVO.getUseCarDate();
        if (useCarDate==null){
            throw new  Exception("用车时间不明确!");
        }
        //修改权限标识
//        boolean opType=true;
        int intState=Integer.parseInt(state.substring(1));
        //校验是否超过用车时间
        //未派车(无车无司机)
        if (intState < Integer.parseInt(OrderState.ALREADYSENDING.getState().substring(1))) {
            log.info("订单:"+orderId+"无车无司机取消订单-------> start,原因{}",cancelReason);
                int i = this.ownerCarCancel(orderId, cancelReason, orderStateVO.getUserId());
            if (CarConstant.USR_CARD_MODE_NET .equals(orderStateVO.getUseCarMode())){
                JSONObject jsonObject = thirdService.threeCancelServer(orderId, cancelReason);
            }
        } else if (intState >= Integer.parseInt(OrderState.ALREADYSENDING.getState().substring(1)) &&
                intState < Integer.parseInt(OrderState.INSERVICE.getState().substring(1))) {
            log.info("订单:"+orderId+"有车有司机取消订单------- start,原因{}",cancelReason);
            //待服务(有车有司机)
            if (CarConstant.USR_CARD_MODE_HAVE .equals(orderStateVO.getUseCarMode())){
                //自由车带服务取消
                log.info("订单:"+orderId+"自有车---有车有司机取消订单------- start,原因{}",cancelReason);
                int i = this.ownerCarCancel(orderId, cancelReason, orderStateVO.getUserId());
            }else{//网约车带服务的取消
                JSONObject jsonObject = thirdService.threeCancelServer(orderId, cancelReason);
                cancelFee1 = jsonObject.getDouble("cancelFee")==null?BigDecimal.ZERO:BigDecimal.valueOf(jsonObject.getDouble("cancelFee"));
//                cancelFee1 = new BigDecimal("30");
                if (cancelFee1.compareTo(BigDecimal.ZERO)<=0){
                    //不需要支付取消费
                    log.info("订单:"+orderId+"网约车------不需要支付取消费---有车有司机取消订单------- start,原因{}",cancelReason);
                    this.onlineCarCancel(orderId, cancelReason, orderStateVO.getUserId(),BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
                }else{
                    //需要支付取消费
                    BigDecimal personPay = iOrderPayInfoService.checkOrderFeeOver(cancelFee1, journeyInfo.getRegimenId(), orderStateVO.getUserId());
                    log.info("订单:"+orderId+"取消费超额"+personPay);
                    /*超额个人支付*/
                    if (personPay.compareTo(BigDecimal.ZERO)==1){
                        BigDecimal ownerPay=cancelFee1.subtract(personPay);
                        ownerAmount=ownerPay.stripTrailingZeros().toPlainString();
                        isPayFee=1;
                        personalAmount=personPay.toPlainString();
                        log.info("网约车订单:"+orderId+"取消参数企业支付:"+ownerPay+",个人支付:"+personPay+",总共取消费:"+cancelFee1);
                        OrderPayInfo orderPayInfo = this.onlineCarCancel(orderId, cancelReason, orderStateVO.getUserId(), cancelFee1, ownerPay, personPay);
                        payId=orderPayInfo.getPayId();
                        payState=orderPayInfo.getState();
                    }else{
                        /*全部由企业支付*/
                        OrderPayInfo orderPayInfo = this.onlineCarCancel(orderId, cancelReason, orderStateVO.getUserId(), cancelFee1, cancelFee1, BigDecimal.ZERO);
                        iOrderPayInfoService.updateOrderPayInfo(new OrderPayInfo(orderPayInfo.getPayId(), OrderPayConstant.PAID));
                        payId=orderPayInfo.getPayId();
                        payState=OrderPayConstant.PAID;
                    }
                }
            }
        }
        //修改权限为未使用
        iJourneyUserCarPowerService.updatePowerSurplus(orderStateVO.getPowerId(),2);
        //TODO 生产放开
        vo.setIsPayFee(isPayFee);
        vo.setCancelAmount(cancelFee1.stripTrailingZeros().toPlainString());
        vo.setOwnerAmount(ownerAmount);
        vo.setPersonalAmount(personalAmount);
        vo.setPayState(payState);
        vo.setPayId(payId);
        ismsBusiness.sendMessageCancelOrder(orderId,longinUserId);
        /**发送取消订单短信*/
        if(cancelFee1.compareTo(BigDecimal.ZERO)==1){
            ismsBusiness.sendSmsCancelOrder(orderId);
        }else{
            ismsBusiness.sendSmsCancelOrderHaveFee(orderId,cancelFee1.doubleValue());
        }
        return vo;
    }

    /**
     * 首次登陆进行中的行程订单
     * @param userId
     * @return
     */
    @Override
    public List<RunningOrderVo> runningOrder(Long userId) {
        String states=OrderState.INSERVICE.getState()+","+OrderState.READYSERVICE.getState()
                +","+OrderState.ALREADYSENDING.getState()+","+OrderState.REASSIGNMENT.getState();
        return orderInfoMapper.getRunningOrder(userId,states);
    }

    /**
     * 获取申请调度列表
     * @param query
     * @return
     */
    @Override
    public PageResult<DispatchVo> queryDispatchList(ApplyDispatchQuery query, LoginUser loginUser) {
        //判断登录人的身份来显示他看到的不同权限的数据
        SysUser user = loginUser.getUser();
        List<SysRole> role = loginUser.getUser().getRoles();
        Long companyId=user.getOwnerCompany();
        query.setCompanyId(companyId);
        query.setUserId(user.getUserId());
        //<系统管理员身份>
        List<DispatchVo> adminOrderList = new ArrayList<DispatchVo>();
        //<调度员身份>
        List<DispatchVo> dispatcherOrderList = new ArrayList<DispatchVo>();
        /**查寻该调度员可用查看的所有申请人*/
        if ("1".equals(user.getItIsDispatcher())){//是调度员
            dispatcherOrderList=orderInfoMapper.queryDispatchList(query);
        }
        List<SysRole> collect = role.stream().filter(p -> CommonConstant.ADMIN_ROLE.equals(p.getRoleKey()) || CommonConstant.SUB_ADMIN_ROLE.equals(p.getRoleKey())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)){//是管理员
            if (!CollectionUtils.isEmpty(dispatcherOrderList)){
                List<Long> orderIds = dispatcherOrderList.stream().map(p -> p.getOrderId()).collect(Collectors.toList());
                query.setOrderIds(orderIds);
            }
            //本公司所有的订单
            adminOrderList= orderInfoMapper.queryAdminDispatchList(query);
            if (!CollectionUtils.isEmpty(adminOrderList)){
                dispatcherOrderList.addAll(adminOrderList);
            }
        }
        PageInfo<DispatchVo> info = new PageInfo<>(dispatcherOrderList);
        return new PageResult<>(info.getTotal(),info.getPages(),dispatcherOrderList);
    }

    /**
     * 司机端改派记录
     * @param orderNo
     * @param driverId
     * @return
     */
    @Override
    public OrderReassignVO reassignDetail(Long orderNo, Long driverId) {
        OrderReassignVO vo=new OrderReassignVO();
        String state=OrderState.APPLYREASSIGN.getState()+","+OrderState.REASSIGNPASS.getState()+","+OrderState.REASSIGNREJECT.getState();
        List<RejectDispatcherUserVO> orderList=orderStateTraceInfoMapper.reassignOrderList(orderNo,state);
        if (CollectionUtils.isEmpty(orderList)){
            return null;
        }
        vo.setOrderId(orderList.get(0).getOrderId());
        vo.setApproveList(orderList);
        for (RejectDispatcherUserVO dispatcherUserVO:orderList){
            if (OrderState.APPLYREASSIGN.getState().equals(dispatcherUserVO.getState())){
                vo.setApplyReason(dispatcherUserVO.getContent());
            }
            if (OrderState.REASSIGNREJECT.getState().equals(dispatcherUserVO.getState())||
                    OrderState.REASSIGNPASS.getState().equals(dispatcherUserVO.getState())) {
                vo.setRejectReason(dispatcherUserVO.getContent());
            }
        }
        return vo;
    }


    /**自有车取消订单**/
    private int ownerCarCancel(Long orderId,String cancelReason,Long userId) throws Exception{
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setState(OrderState.ORDERCLOSE.getState());
        orderInfo.setUpdateBy(String.valueOf(userId));
        orderInfo.setOrderId(orderId);
        orderInfo.setUpdateTime(DateUtils.getNowDate());
        int suc = orderInfoMapper.updateOrderInfo(orderInfo);
        this.insertOrderStateTraceInfo(orderId,OrderState.ORDERCANCEL.getState(),cancelReason,userId);
        return suc;
    }

    /**网约车取消订单*/
    private OrderPayInfo onlineCarCancel(Long orderId,String cancelReason,Long userId,BigDecimal cancelFee,BigDecimal ownerFee,BigDecimal persionFee) throws Exception{
        int i = this.ownerCarCancel(orderId, cancelReason, userId);
        if (i!=ONE){
            throw new Exception("取消订单失败!");
        }
        OrderPayInfo orderPayInfo=null;
        if (cancelFee!=null&&cancelFee.compareTo(BigDecimal.ZERO)==1){
            String json = orderSettlingInfoService.formatCostFee(new OrderSettlingInfoVo(), persionFee,ownerFee);
            orderPayInfo = iOrderPayInfoService.insertOrderPayAndSetting(orderId, cancelFee, String.valueOf(ZERO), String.valueOf(ZERO), json, userId,persionFee);
        }
        return orderPayInfo;
    }

    /**插入订单轨迹*/
    private void insertOrderStateTraceInfo(Long orderId,String state,String cancelReason,Long userId){
        OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
        orderStateTraceInfo.setOrderId(orderId);
        orderStateTraceInfo.setState(state);
        orderStateTraceInfo.setContent(cancelReason);
        orderStateTraceInfo.setCreateBy(userId+"");
        orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
        orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
    }

    /**
     * 查询所有处于待派单(未改派)的订单及关联的信息
     * @param userId
     * @param companyId
     * @return
     */
    public List<DispatchOrderInfo> queryAllWaitingList(Long userId,Long companyId) {
        List<DispatchOrderInfo> result=new ArrayList<DispatchOrderInfo>();
        //查询所有处于待派单(未改派)的订单及关联的信息
        OrderInfo query = new OrderInfo();
        query.setState(OrderState.WAITINGLIST.getState());
        query.setCompanyId(companyId);
        List<DispatchOrderInfo> waitDispatchOrder= orderInfoMapper.queryOrderRelateInfo(query);
        return result;
    }


}
