package com.hq.ecmp.mscore.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.OrderEvaluationDto;
import com.hq.ecmp.mscore.dto.OrderInfoDTO;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackInfoService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.util.HqAdmin;
import com.hq.ecmp.util.OrderUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class EcmpUserFeedbackInfoServiceImpl implements IEcmpUserFeedbackInfoService
{
    @Resource
    private EcmpUserFeedbackInfoMapper ecmpUserFeedbackInfoMapper;
    @Resource
    private EcmpUserFeedbackImageMapper ecmpUserFeedbackImageMapper;
    @Resource
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private OrderAddressInfoMapper orderAddressInfoMapper;
    @Resource
    private OrderSettlingInfoMapper orderSettlingInfoMapper;
    @Resource
    private TokenService tokenService;

    /**
     * 查询【请填写功能名称】
     *
     * @param feedbackId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public EcmpUserFeedbackInfo selectEcmpUserFeedbackInfoById(Long feedbackId)
    {
        return ecmpUserFeedbackInfoMapper.selectEcmpUserFeedbackInfoById(feedbackId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<EcmpUserFeedbackInfo> selectEcmpUserFeedbackInfoList(EcmpUserFeedbackInfo ecmpUserFeedbackInfo)
    {
        return ecmpUserFeedbackInfoMapper.selectEcmpUserFeedbackInfoList(ecmpUserFeedbackInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertEcmpUserFeedbackInfo(EcmpUserFeedbackInfo ecmpUserFeedbackInfo)
    {
        ecmpUserFeedbackInfo.setCreateTime(DateUtils.getNowDate());
        return ecmpUserFeedbackInfoMapper.insertEcmpUserFeedbackInfo(ecmpUserFeedbackInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateEcmpUserFeedbackInfo(EcmpUserFeedbackInfo ecmpUserFeedbackInfo)
    {
        ecmpUserFeedbackInfo.setUpdateTime(DateUtils.getNowDate());
        return ecmpUserFeedbackInfoMapper.updateEcmpUserFeedbackInfo(ecmpUserFeedbackInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param feedbackIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserFeedbackInfoByIds(Long[] feedbackIds)
    {
        return ecmpUserFeedbackInfoMapper.deleteEcmpUserFeedbackInfoByIds(feedbackIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param feedbackId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserFeedbackInfoById(Long feedbackId)
    {
        return ecmpUserFeedbackInfoMapper.deleteEcmpUserFeedbackInfoById(feedbackId);
    }

    @Override
    @Transactional
    public Long saveOrderEvaluation(OrderEvaluationDto evaluationDto, Long userId) throws Exception{
        List<EcmpUserFeedbackInfo> feedbackInfos = ecmpUserFeedbackInfoMapper.selectEcmpUserFeedbackInfoList(new EcmpUserFeedbackInfo(evaluationDto.getOrderId()));
        if (CollectionUtils.isNotEmpty(feedbackInfos)){
            log.info("订单:"+evaluationDto.getOrderId()+"行程异议已存在");
            ecmpUserFeedbackInfoMapper.deleteEcmpUserFeedbackInfoById(feedbackInfos.get(0).getFeedbackId());
            List<EcmpUserFeedbackImage> ecmpUserFeedbackImages = ecmpUserFeedbackImageMapper.selectEcmpUserFeedbackImageList(new EcmpUserFeedbackImage(feedbackInfos.get(0).getFeedbackId()));
            if (CollectionUtils.isNotEmpty(ecmpUserFeedbackImages)){
                List<Long> collect = ecmpUserFeedbackImages.stream().map(EcmpUserFeedbackImage::getImageId).collect(Collectors.toList());
                Long[] longs = (Long[]) collect.toArray();
                ecmpUserFeedbackImageMapper.deleteEcmpUserFeedbackImageByIds(longs);
            }
        }
        EcmpUserFeedbackInfo ecmpUserFeedbackInfo = new EcmpUserFeedbackInfo();
        BeanUtils.copyProperties(evaluationDto,ecmpUserFeedbackInfo);
        ecmpUserFeedbackInfo.setUserId(userId);
        ecmpUserFeedbackInfo.setCreateBy(String.valueOf(userId));
        ecmpUserFeedbackInfo.setCreateTime(DateUtils.getNowDate());
        log.info("订单:"+evaluationDto.getOrderId()+"异议参数"+evaluationDto.toString());
        int count = ecmpUserFeedbackInfoMapper.insertEcmpUserFeedbackInfo(ecmpUserFeedbackInfo);
        if (count>0){
            if (StringUtils.isNotBlank(evaluationDto.getImgUrls())){
                EcmpUserFeedbackImage feedbackImage = new EcmpUserFeedbackImage();
                feedbackImage.setFeedbackId(ecmpUserFeedbackInfo.getFeedbackId());
                feedbackImage.setUserId(userId);
                String[] split = evaluationDto.getImgUrls().split(",");
                for (String url:split) {
                    feedbackImage.setImageUrl(url);
                    feedbackImage.setCreateBy(String.valueOf(userId));
                    feedbackImage.setCreateTime(DateUtils.getNowDate());
                    ecmpUserFeedbackImageMapper.insertEcmpUserFeedbackImage(feedbackImage);
                }
            }
        }
        OrderStateTraceInfo stateTraceInfo=new OrderStateTraceInfo();
        stateTraceInfo.setOrderId(evaluationDto.getOrderId());
        stateTraceInfo.setContent(evaluationDto.getContent());
        stateTraceInfo.setCreateBy(String.valueOf(userId));
        stateTraceInfo.setCreateTime(new Date());
        stateTraceInfo.setState(OrderStateTrace.OBJECTION.getState());
        orderStateTraceInfoMapper.insertOrderStateTraceInfo(stateTraceInfo);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(evaluationDto.getOrderId());
        orderInfo.setState(OrderState.ORDERCLOSE.getState());
        orderInfo.setUpdateTime(new Date());
        orderInfoMapper.updateOrderInfo(orderInfo);
        return ecmpUserFeedbackInfo.getFeedbackId();
    }

    /**
     * 异议订单
     * @param ecmpUserFeedbackInfo
     * @return
     */
    @Override
    public PageResult<EcmpUserFeedbackInfoVo> getObjectionOrderList(EcmpUserFeedbackInfoVo ecmpUserFeedbackInfo) {
        PageHelper.startPage(ecmpUserFeedbackInfo.getPageNum(),ecmpUserFeedbackInfo.getPageSize());
        List<EcmpUserFeedbackInfoVo> list =  ecmpUserFeedbackInfoMapper.getObjectionOrderList(ecmpUserFeedbackInfo);
        for (EcmpUserFeedbackInfoVo EcmpUserFeedbackInfoVo :list){
            List<String> imageUrl = ecmpUserFeedbackImageMapper.selectEcmpUserFeedbackByImage(EcmpUserFeedbackInfoVo.getFeedbackId());
            if(!imageUrl.isEmpty()){
                EcmpUserFeedbackInfoVo.setImageUrl(imageUrl);
            }
        }
        PageInfo<EcmpUserFeedbackInfoVo> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),info.getPages(),list);
    }

    /**
     * 回复异议订单
     * @param ecmpUserFeedbackInfo
     */
    @Override
    public int replyObjectionOrder(EcmpUserFeedbackInfoVo ecmpUserFeedbackInfo,Long userId) {
        EcmpUserFeedbackInfoVo feedbackInfoVo = new EcmpUserFeedbackInfoVo();
        //主键id
        feedbackInfoVo.setFeedbackId(ecmpUserFeedbackInfo.getFeedbackId());
        //回复内容
        feedbackInfoVo.setResult(ecmpUserFeedbackInfo.getResult());
        //回复的格式
        feedbackInfoVo.setState(ReplyObjectionEnum.YES_REPLY.getKey());
        //回复操作人
        feedbackInfoVo.setUpdateBy(userId);
        //回复操作时间
        feedbackInfoVo.setUpdateTime(new Date());
        int i = ecmpUserFeedbackInfoMapper.updateFeedbackInfo(feedbackInfoVo);
        return i;
    }

    /**
     * 订单管理补单提交功能
     * @param orderInfoDTO
     * @return
     */
    @Override
    public ApiResponse supplementSubmit(OrderInfoDTO orderInfoDTO) {
        ApiResponse apiResponse = new ApiResponse();
        //判断提交的城市code值是否在该调度员所管理车队的服务城市中
       List<Map> cityCodes = orderInfoDTO.getCityCodes();
        int i = 0;
        for(Map list: cityCodes){
            if(orderInfoDTO.getUpCarCityCode().equals(list.get("cityCode"))){
                i++;
            }
            if (orderInfoDTO.getDownCarCityCode().equals(list.get("cityCode"))){
                i++;
            }
        }
        if(i==0){
            return  ApiResponse.error("所选择的城市不符合调度员所管理车队的服务城市");
        }
        //增加订单表数据
        OrderInfo orderInfo = new OrderInfo();
        //所属公司
        orderInfo.setOwnerCompany(orderInfoDTO.getCompanyId());
        //默认为0
        orderInfo.setJourneyId(0L);
        orderInfo.setNodeId(0L);
        orderInfo.setPowerId(0L);
        //司机id
        orderInfo.setDriverId(orderInfoDTO.getDriverId());
        //车辆id
        orderInfo.setCarId(orderInfoDTO.getCarId());
        //企业id
        orderInfo.setCompanyId(orderInfoDTO.getCompanyId());
        //订单编号
        orderInfo.setOrderNumber(OrderUtils.getOrderNum());
        //乘车人id
        orderInfo.setUserId(orderInfoDTO.getUserId());
        //订单类型
        orderInfo.setUseCarMode(CarConstant.USR_CARD_MODE_HAVE);
        //服务类型
        orderInfo.setServiceType(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState());
        //订单状态
        orderInfo.setState(OrderState.ORDERCLOSE.getState());
        //补单制度(特殊处理-用这个字段)
        orderInfo.setDriverGrade(orderInfoDTO.getDriverGrade().toString());
        //补单同行人数量(特殊处理-用这个字段)
        orderInfo.setTripartitePlatformCode(orderInfoDTO.getNum().toString());
        //创建时间
        orderInfo.setCreateTime(DateUtils.getNowDate());
        //创建人
        orderInfo.setCreateBy(orderInfoDTO.getUserId().toString());
        //补单状态
        orderInfo.setItIsSupplement(ItIsSupplementEnum.ORDER_REPLENISHMENT_STATUS.getValue());
        int  num  = orderInfoMapper.insertOrderInfo(orderInfo);
        //增加订单地址表数据
        if(num==1){
            //操作订单上车数据
            OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
            //订单id
            orderAddressInfo.setOrderId(orderInfo.getOrderId());
            orderAddressInfo.setJourneyId(0L);
            orderAddressInfo.setPowerId(0L);
            orderAddressInfo.setNodeId(0L);
            //司机id
            orderAddressInfo.setDriverId(orderInfo.getDriverId());
            //A000  真实出发地址  A999  真实到达地址
            orderAddressInfo.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
            //车辆id
            orderAddressInfo.setCarId(orderInfo.getCarId());
            //订单所属人编号
            orderAddressInfo.setUserId(orderInfo.getUserId().toString());
            //城市邮政编码
            orderAddressInfo.setCityPostalCode(orderInfoDTO.getUpCarCityCode());
            //上车时间
            orderAddressInfo.setActionTime(orderInfoDTO.getUpCarTime());
            //上车短地址
            orderAddressInfo.setAddress(orderInfoDTO.getUpCarActualSetoutAddress());
            //上车长地址
            orderAddressInfo.setAddressLong(orderInfoDTO.getUpCarActualSetoutAddressLong());
            //上车经度
            orderAddressInfo.setLongitude(orderInfoDTO.getUpCarActualSetoutLongitude());
            //上车纬度
            orderAddressInfo.setLatitude(orderInfoDTO.getUpCarActualSetoutLatitude());
            //创建时间
            orderAddressInfo.setCreateTime(DateUtils.getNowDate());
            //创建人
            orderAddressInfo.setCreateBy(orderInfoDTO.getUserId().toString());
            orderAddressInfoMapper.insertOrderAddressInfo(orderAddressInfo);
            //------------------------------------------------------------------------------------------
            //操作订单下车数据
            OrderAddressInfo orderAddress = new OrderAddressInfo();
            //订单id
            orderAddress.setOrderId(orderInfo.getOrderId());
            orderAddress.setJourneyId(0L);
            orderAddress.setPowerId(0L);
            orderAddress.setNodeId(0L);
            //司机id
            orderAddress.setDriverId(orderInfo.getDriverId());
            //A000  真实出发地址  A999  真实到达地址
            orderAddress.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
            //车辆id
            orderAddress.setCarId(orderInfo.getCarId());
            //订单所属人编号
            orderAddress.setUserId(orderInfo.getUserId().toString());
            //城市邮政编码
            orderAddress.setCityPostalCode(orderInfoDTO.getDownCarCityCode());
            //下车时间
            orderAddress.setActionTime(orderInfoDTO.getDownCarTime());
            //上车短地址
            orderAddress.setAddress(orderInfoDTO.getDownCarActualSetoutAddress());
            //上车长地址
            orderAddress.setAddressLong(orderInfoDTO.getDownCarActualSetoutAddressLong());
            //上车经度
            orderAddress.setLongitude(orderInfoDTO.getDownCarActualSetoutLongitude());
            //上车纬度
            orderAddress.setLatitude(orderInfoDTO.getDownCarActualSetoutLatitude());
            //创建时间
            orderAddress.setCreateTime(DateUtils.getNowDate());
            //创建人
            orderAddress.setCreateBy(orderInfoDTO.getUserId().toString());
            int  numTwo= orderAddressInfoMapper.insertOrderAddressInfo(orderAddress);
            if(numTwo<0){
                apiResponse.setMsg("增加订单地址失败");
            }
        }else{
            apiResponse.setMsg("增加订单失败");
        }
        //增加结算表数据
        OrderSettlingInfoVo orderSettlingInfoVo= new OrderSettlingInfoVo();
        //订单id
        orderSettlingInfoVo.setOrderId(orderInfo.getOrderId());
        //总时长
        orderSettlingInfoVo.setTotalTime(orderInfoDTO.getTotalTime());
        //总里程
        orderSettlingInfoVo.setTotalMileage(orderInfoDTO.getTotalMileage());
        //创建时间
        orderSettlingInfoVo.setCreateTime(DateUtils.getNowDate());
        //创建人
        orderSettlingInfoVo.setCreateBy(orderInfoDTO.getUserId().toString());
        //费用详情
        Map map = new HashMap();
        map.put("otherCost",orderInfoDTO.getAmountDetail());
        orderSettlingInfoVo.setAmountDetail(JSON.toJSONString(map));
        //费用总金额
        orderSettlingInfoVo.setAmount(orderInfoDTO.getAmount());
        int Three = orderSettlingInfoMapper.insertOrderSettlingInfoOne(orderSettlingInfoVo);
        if(Three<0){
            apiResponse.setMsg("增加结算表数据失败");
        }
        return apiResponse;
    }

    @Override
    public int updateFeedback(EcmpUserFeedbackInfoVo feedBackDto) {
        feedBackDto.setUpdateTime(new Date());
        feedBackDto.setState(ReplyObjectionEnum.YES_REPLY.getKey());
        return ecmpUserFeedbackInfoMapper.updateFeedback(feedBackDto);
    }


    @Override
    public List<EcmpUserFeedbackVo> findFeedback(EcmpUserFeedbackInfo ecmpUserFeedbackInfo) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        int admin = HqAdmin.isAdmin(loginUser);
        ecmpUserFeedbackInfo.setIsAdmin(admin);
        ecmpUserFeedbackInfo.setUserId(loginUser.getUser().getUserId());
        List<EcmpUserFeedbackVo> backInfoList =  ecmpUserFeedbackInfoMapper.findFeedback(ecmpUserFeedbackInfo);
        return backInfoList;
    }


}
