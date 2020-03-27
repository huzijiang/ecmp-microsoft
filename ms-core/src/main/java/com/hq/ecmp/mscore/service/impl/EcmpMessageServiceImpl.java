package com.hq.ecmp.mscore.service.impl;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.hq.api.system.domain.SysUser;
import com.hq.common.exception.BaseException;
import com.hq.common.utils.ServletUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.bo.CityInfo;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.EcmpMessageService;
import com.hq.ecmp.mscore.service.IApproveTemplateNodeInfoService;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.SortListUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.hq.ecmp.constant.CommonConstant.ONE;
import static com.hq.ecmp.constant.CommonConstant.ZERO;

/**
 * (EcmpMessage)表服务实现类
 *
 * @author makejava
 * @since 2020-03-13 15:25:47
 */
@Service("ecmpMessageService")
public class EcmpMessageServiceImpl implements EcmpMessageService {
    @Resource
    private EcmpMessageMapper ecmpMessageDao;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private ApplyInfoMapper applyInfoMapper;
    @Autowired
    private JourneyNodeInfoMapper journeyNodeInfoMapper;
    @Autowired
    private JourneyInfoMapper journeyInfoMapper;
    @Autowired
    private JourneyUserCarPowerMapper userCarPowerMapper;
    @Autowired
    private CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;
    @Autowired
    private ApplyApproveResultInfoMapper applyApproveResultInfoMapper;
    @Autowired
    private ISmsTemplateInfoService smsTemplateInfoService;
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private ChinaCityMapper chinaCityMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EcmpMessage queryById(Long id) {
        return this.ecmpMessageDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<EcmpMessage> queryAllByLimit(int offset, int limit) {
        return this.ecmpMessageDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param ecmpMessage 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpMessage insert(EcmpMessage ecmpMessage) {
        this.ecmpMessageDao.insert(ecmpMessage);
        return ecmpMessage;
    }

    /**
     * 修改数据
     *
     * @param ecmpMessage 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpMessage update(EcmpMessage ecmpMessage) {
        this.ecmpMessageDao.update(ecmpMessage);
        return this.queryById(ecmpMessage.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.ecmpMessageDao.deleteById(id) > 0;
    }

    /**
     * 插入一条消息
     * @param msgConstant
     * @param userConstant
     * @param ecmpId
     * @param url
     */
    @Override
    public void insertMessage(MsgConstant msgConstant, MsgTypeConstant type, MsgUserConstant userConstant, String content,Long ecmpId, String url) {
        EcmpMessage ecmpMessage = EcmpMessage.builder()
                .configType(userConstant.getType())
                .ecmpId(ecmpId)
                .status(MsgStatusConstant.MESSAGE_STATUS_T002.getType())
                .type(type.getType())
                .category(msgConstant.getDesp())
                .createTime(new Date())
                .content(content)
                .createBy(ecmpId)
                .url(url)
                .build();
        ecmpMessageDao.insert(ecmpMessage);
    }

    @Override
    public void insertMessage(MsgConstant msgConstant, MsgTypeConstant type, MsgUserConstant userConstant,String content, Long ecmpId) {
        insertMessage(msgConstant,type,userConstant,content,ecmpId,"");
    }

    @Override
    public List<EcmpMessage> selectMessageList(String identity) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long ecmpId;
        List<Integer> configTypeList = new ArrayList();
        switch (identity){
            //乘客
            case "1":
                ecmpId = loginUser.getUser().getUserId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_APPROVAL.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_USER.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DISPATCHER.getType());
                break;
            //司机
            case "2":
                ecmpId = loginUser.getDriver().getDriverId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DRIVER.getType());
                break;
            default:
                throw new BaseException("错误信息");
        }
        Map map = new HashMap();
        map.put("configTypeList",configTypeList);
        map.put("ecmpId",ecmpId);
        return ecmpMessageDao.queryMessageList(map);
    }


    @Override
    public int getMessagesCount(String identity) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long ecmpId;
        List<Integer> configTypeList = new ArrayList();
        switch (identity){
            //乘客
            case "1":
                ecmpId = loginUser.getUser().getUserId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_APPROVAL.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_USER.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DISPATCHER.getType());
                break;
            //司机
            case "2":
                ecmpId = loginUser.getDriver().getDriverId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DRIVER.getType());
                break;
            default:
                throw new BaseException("错误信息");
        }
        Map map = new HashMap();
        map.put("configTypeList",configTypeList);
        map.put("ecmpId",ecmpId);
        return ecmpMessageDao.queryMessageCount(map);
    }

    @Override
    public List<MessageDto> getMessagesForPassenger(SysUser user) throws Exception {
        String categorys="M001,M004,M006";//申请人
        if ("1".equals(user.getItIsDispatcher())){//调度员
            categorys+=",M003";
        }
        List<ApplyApproveResultInfo> approveTemplateNodeInfos = applyApproveResultInfoMapper.selectByUserId(null,user.getUserId(), ApproveStateEnum.WAIT_APPROVE_STATE.getKey());
        if (CollectionUtils.isNotEmpty(approveTemplateNodeInfos)){//审批员
            categorys+=",M002";
        }
        List<MessageDto> list = ecmpMessageDao.getMessagesForPassenger(user.getUserId(), categorys);
        if (CollectionUtils.isNotEmpty(list)){
            for (MessageDto messageDto:list){
                messageDto.setMessageTypeStr(MsgConstant.getDespByType(messageDto.getMessageType()));
                if (MsgConstant.MESSAGE_T006.getType().equals(messageDto.getMessageType())){
                    OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(messageDto.getMessageId());
                    messageDto.setUseCarMode(orderInfo.getUseCarMode());
                }
            }
        }
        return list;
    }

    @Override
    public List<MessageDto> getRunMessageForDrive(SysUser user) throws Exception {
        if ("0".equals(user.getItIsDriver())){
            throw new Exception("该用户不是司机");
        }
        DriverInfo driverInfo = driverInfoMapper.selectDriverInfoByUserId(user.getUserId());
        String categorys="M005,M004,M007";//申请人
        List<MessageDto> runMessageForDrive = ecmpMessageDao.getRunMessageForDrive(driverInfo.getDriverId(), categorys);
        //判断当前司机是不是调度员
        if ("1".equals(user.getItIsDispatcher())){
            List<MessageDto> runMessageForDispatcher = ecmpMessageDao.getRunMessageForDispatcher(user.getUserId(), "M003");
            runMessageForDrive.addAll(runMessageForDispatcher);
        }
        if (CollectionUtils.isNotEmpty(runMessageForDrive)){
            for (MessageDto messageDto:runMessageForDrive){
                messageDto.setMessageTypeStr(MsgConstant.getDespByType(messageDto.getMessageType()));
            }
        }
        //获取即将任务开始的通知
        OrderInfo info=orderInfoMapper.selectDriverOrder(driverInfo.getDriverId(), OrderState.ALREADYSENDING.getState());
        if (info!=null){
            runMessageForDrive.add(new MessageDto(info.getOrderId(),MsgConstant.MESSAGE_T00.getType(),MsgConstant.MESSAGE_T00.getDesp(),1));
        }
        return runMessageForDrive;
    }

    //TODO 审批通过后的发通知
    @Transactional
    @Override
    @Async
    public void saveApplyMessagePass(Long applyId,Long ecmpId,Long userId,Long orderId,Long powerId,int isDispatch) throws Exception{
        //通知调度员,通知申请人审批通过
        //判断申请用车城市是否有我车队组织
        if (isDispatch==ZERO){
            return;
        }
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(applyInfo.getJourneyId());
        List<JourneyNodeInfo> journeyNodeInfos = journeyNodeInfoMapper.selectJourneyNodeInfoList(new JourneyNodeInfo(applyInfo.getJourneyId()));
        List<EcmpMessage> dispatcherMessage=new ArrayList<>();
        if (ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(applyInfo.getApplyType())){//公务
            //公务需要给申请人和调度员发送通知
            dispatcherMessage = this.getDispatcherMessage(orderId, userId, powerId);
        }
        if (CollectionUtils.isNotEmpty(dispatcherMessage)){
            ecmpMessageDao.insertList(dispatcherMessage);//保存消息通知
        }
        //给调度员发短信
        this.sendMessageForDispatch(dispatcherMessage,journeyInfo,journeyNodeInfos);
    }

    @Async
    @Override
    public void applyUserPassMessage(Long applyId,Long ecmpId,Long userId,Long orderId,Long powerId,int isDispatch) throws Exception{
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        if (applyInfo==null){
            throw new Exception("申请单:"+applyId+"不存在");
        }
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(applyInfo.getJourneyId());
        if (journeyInfo==null){
            throw new Exception("此申请对应给行程不存在");
        }
        ecmpMessageDao.insert(new EcmpMessage(MsgUserConstant.MESSAGE_USER_USER.getType(),ecmpId,applyId,MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                MsgStatusConstant.MESSAGE_STATUS_T002.getType(),"您的申请单"+applyId+"审批通过了",MsgConstant.MESSAGE_T001.getType(),userId,new Date()));
        updateApplyMessage(ecmpId,applyId);
        List<JourneyNodeInfo> journeyNodeInfos = journeyNodeInfoMapper.selectJourneyNodeInfoList(new JourneyNodeInfo(applyInfo.getJourneyId()));
        sendMessageForApplyUser(journeyInfo,journeyNodeInfos,applyInfo,isDispatch,ecmpId,ApproveStateEnum.APPROVE_PASS.getKey(),null);
    }

    @Override
    @Async
    public void saveApplyMessageReject(Long applyId,Long ecmpId,Long userId,String reson) throws Exception{
        //通知调度员,通知申请人审批驳回
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        if (applyInfo==null){
            throw new Exception("申请单:"+applyId+"不存在");
        }
        EcmpMessage ecmpMessage=new EcmpMessage(MsgUserConstant.MESSAGE_USER_USER.getType(),ecmpId,applyId,MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                MsgStatusConstant.MESSAGE_STATUS_T002.getType(),"您的申请单"+applyId+"审批驳回了",MsgConstant.MESSAGE_T001.getType(),userId,new Date());
            ecmpMessageDao.insert(ecmpMessage);//保存消息通知
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(applyInfo.getJourneyId());
        if (journeyInfo==null){
            throw new Exception("此申请对应给行程不存在");
        }
        List<JourneyNodeInfo> journeyNodeInfos = journeyNodeInfoMapper.selectJourneyNodeInfoList(new JourneyNodeInfo(applyInfo.getJourneyId()));
        updateApplyMessage(ecmpId,applyId);
        sendMessageForApplyUser(journeyInfo,journeyNodeInfos,applyInfo,ONE,ecmpId,ApproveStateEnum.APPROVE_FAIL.getKey(),reson);
    }

    @Override
    @Async
    public void sendNextApproveUsers(String approveUserId,Long applyId,Long userId) {
        List<EcmpMessage> msgList=new ArrayList<>();
        if (StringUtils.isNotBlank(approveUserId)){
            String[] split = approveUserId.split(",");
            for(String str:split){
                msgList.add(new EcmpMessage(MsgUserConstant.MESSAGE_USER_APPROVAL.getType(),Long.parseLong(str),applyId,MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                        MsgStatusConstant.MESSAGE_STATUS_T002.getType(),"您有一条待审批的消息",MsgConstant.MESSAGE_T002.getType(),userId,new Date()));
            }
            if (CollectionUtils.isNotEmpty(msgList)){
                ecmpMessageDao.insertList(msgList);
            }
        }
    }

    @Override
    public void readMessage(MessageDto messageDto, SysUser user) {
        List<EcmpMessage> ecmpMessages = ecmpMessageDao.queryAll(new EcmpMessage(messageDto.getConfigType(), MsgStatusConstant.MESSAGE_STATUS_T002.getType(), user.getUserId(), messageDto.getMessageId(),messageDto.getMessageType()));
        if (ecmpMessages!=null){
            for (EcmpMessage ecmpMessage:ecmpMessages){
                ecmpMessage.setStatus(MsgStatusConstant.MESSAGE_STATUS_T001.getType());
                ecmpMessageDao.update(ecmpMessage);
            }
        }
    }

    /**
     * 给调度员发短信
     * @throws Exception
     */
    private void sendMessageForDispatch(List<EcmpMessage> dispatcherMessage,JourneyInfo journeyInfo,List<JourneyNodeInfo> journeyNodeInfos) throws Exception{
        if (CollectionUtils.isNotEmpty(dispatcherMessage)){
            List<Long> userIds = dispatcherMessage.stream().map(EcmpMessage::getEcmpId).collect(Collectors.toList());
            CityInfo cityInfo = chinaCityMapper.queryCityByCityCode(journeyNodeInfos.get(0).getPlanBeginCityCode());
            List<EcmpUser> list = ecmpUserMapper.getListByUserIds(userIds);
            if (CollectionUtils.isNotEmpty(list)){
                Map<String,String> map=Maps.newHashMap();
                map.put("city",cityInfo.getCityName());
                String useCarTime= DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN,journeyInfo.getUseCarTime());
                map.put("useCarTime",useCarTime);
                for (EcmpUser user:list){
                    smsTemplateInfoService.sendSms(SmsTemplateConstant.approve_pass_dispatcher,map,user.getPhonenumber());//给调度发短信
                }
            }
        }
    }

    /**
     * 给申请人发短信
     * @param applyInfo
     * @param isDispatch
     * @param ecmpId
     * @param state
     * @param reason
     * @throws Exception
     */
    private void sendMessageForApplyUser(JourneyInfo journeyInfo,List<JourneyNodeInfo> journeyNodeInfos,ApplyInfo applyInfo,int isDispatch,Long ecmpId,String state,String reason)throws Exception{
        Map<String,String> map=Maps.newHashMap();
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(ecmpId);
        if (ecmpUser==null){
            throw new Exception("申请人不存在");
        }
        String tempalte="";
        if (ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(applyInfo.getApplyType())){//公务
            String useCarTime= DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN,journeyInfo.getUseCarTime());
            map.put("useCarTime",useCarTime);
            if (ApproveStateEnum.APPROVE_PASS.getKey().equals(state)){//审批通过
                //isDispatch 1走调度，0不走调度
                tempalte=isDispatch==ONE?SmsTemplateConstant.approve_pass_doDispatch:SmsTemplateConstant.approve_pass_notDispatch;
            }else{
                tempalte=SmsTemplateConstant.approve_reject_Business;
                String concet=StringUtils.isNotEmpty(reason)?reason:CommonConstant.NOTHING;
                map.put("reason",concet);
            }
        }else {//差旅
            String useCarTime= DateFormatUtils.formatDate(DateFormatUtils.DATE_FORMAT_CN,journeyInfo.getStartDate())+"-"+
                    DateFormatUtils.formatDate(DateFormatUtils.DATE_FORMAT_CN,journeyInfo.getEndDate());
            map.put("useCarTime",useCarTime);
            String city = getTraveCity(journeyNodeInfos);
            map.put("city",city);
            tempalte=SmsTemplateConstant.approve_pass_applyUser;
            if (ApproveStateEnum.APPROVE_FAIL.getKey().equals(state)){
                String concet=StringUtils.isNotEmpty(reason)?reason:CommonConstant.NOTHING;
                map.put("reason",concet);
                tempalte=SmsTemplateConstant.approve_reject_Travel;
            }
        }
        smsTemplateInfoService.sendSms(tempalte,map,ecmpUser.getPhonenumber());
    }

    /**
     * 获取差旅行程的所有城市名
     * @param journeyNodeInfos
     * @return
     */
    private String getTraveCity(List<JourneyNodeInfo> journeyNodeInfos){
        String city="";
        if (CollectionUtils.isNotEmpty(journeyNodeInfos)){
            SortListUtil.sort(journeyNodeInfos,"number",SortListUtil.ASC);
            for (int i=0;i<journeyNodeInfos.size();i++){
                if (i==0){
                    city+= journeyNodeInfos.get(0).getPlanBeginAddress()+"-"+journeyNodeInfos.get(0).getPlanEndAddress();
                }else {
                    city+="-"+journeyNodeInfos.get(i).getPlanEndAddress();
                }
            }
        }
        return city;
    }

    //审批通过后修改之前相同申请单相关的未读消息为已读状态
    private void updateApplyMessage(Long ecmpId,Long applyId){
        List<EcmpMessage> ecmpMessages = ecmpMessageDao.queryAll(new EcmpMessage(MsgUserConstant.MESSAGE_USER_USER.getType(),MsgStatusConstant.MESSAGE_STATUS_T002.getType(),ecmpId,applyId,MsgConstant.MESSAGE_T001.getType()));
        if (CollectionUtils.isNotEmpty(ecmpMessages)){
            for (EcmpMessage message:ecmpMessages){
                message.setStatus(MsgStatusConstant.MESSAGE_STATUS_T001.getType());
                message.setUpdateBy(ecmpId);
                message.setUpdateTime(new Date());
                ecmpMessageDao.update(message);//将审批之前的未读消息修改为已读
            }
        }
    }

    /**
     * 查询用车权限下的所有调度员信息
     * @param orderId
     * @param userId
     * @param powerId
     * @return
     */
    private List<EcmpMessage> getDispatcherMessage(Long orderId,Long userId,Long powerId){
        List<EcmpMessage> msgList=new ArrayList<>();
        JourneyUserCarPower journeyUserCarPower = userCarPowerMapper.selectJourneyUserCarPowerById(powerId);
        if (journeyUserCarPower==null){
            return null;
        }

        JourneyNodeInfo journeyNodeInfo = journeyNodeInfoMapper.selectJourneyNodeInfoById(journeyUserCarPower.getNodeId());
        if (journeyNodeInfo==null){
            return null;
        }
        String cityCodes="";
        if (StringUtils.isNotEmpty(journeyNodeInfo.getPlanBeginCityCode())){
            cityCodes+=","+journeyNodeInfo.getPlanBeginCityCode();
        }
        if (StringUtils.isNotEmpty(journeyNodeInfo.getPlanEndCityCode())){
            cityCodes+=","+journeyNodeInfo.getPlanEndCityCode();
        }
        if (StringUtils.isNotEmpty(cityCodes)){
            cityCodes=cityCodes.substring(1);
        }
        //查询这个城市的调度员
        List<Long> dispatchers=carGroupDispatcherInfoMapper.findByCityCode(cityCodes);
        if (CollectionUtils.isEmpty(dispatchers)){
            List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(null);
            if (CollectionUtils.isNotEmpty(carGroupDispatcherInfos)){
                dispatchers = carGroupDispatcherInfos.stream().map(CarGroupDispatcherInfo::getUserId).collect(Collectors.toList());
            }
        }
        if (CollectionUtils.isNotEmpty(dispatchers)){
            for (Long dispatcherId:dispatchers){
                msgList.add(new EcmpMessage(MsgUserConstant.MESSAGE_USER_DISPATCHER.getType(),dispatcherId,orderId,MsgTypeConstant.MESSAGE_TYPE_T001.getType(),
                        MsgStatusConstant.MESSAGE_STATUS_T002.getType(),"您有一条调度通知",MsgConstant.MESSAGE_T003.getType(),userId,new Date()));
            }
        }
        return msgList;
    }

	@Override
	public void saveDispatchCarComplete(Long orderId) {
		OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
		
	}
}