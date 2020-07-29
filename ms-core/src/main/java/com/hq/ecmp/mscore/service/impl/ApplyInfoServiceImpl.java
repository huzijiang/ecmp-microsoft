package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.reflect.TypeToken;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.exception.BaseException;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.bo.JourneyNodeBo;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.hq.ecmp.constant.CommonConstant.*;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class ApplyInfoServiceImpl implements IApplyInfoService
{
    @Autowired
    private ApplyInfoMapper applyInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;
    @Autowired
    private JourneyInfoMapper journeyInfoMapper;
    @Autowired
    private JourneyNodeInfoMapper journeyNodeInfoMapper;
    @Autowired
    private JourneyPassengerInfoMapper journeyPassengerInfoMapper;
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;
    @Autowired
    private ApproveTemplateInfoMapper templateInfoMapper;
    @Autowired
    private ApproveTemplateNodeInfoMapper approveTemplateNodeInfoMapper;
    @Autowired
    private ApplyApproveResultInfoMapper resultInfoMapper;
    @Autowired
    private EcmpUserRoleMapper userRoleMapper;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;
    @Autowired
    private ApplyApproveResultInfoMapper applyApproveResultInfoMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IApplyApproveResultInfoService applyApproveResultInfoService;
    @Autowired
    private EcmpMessageMapper ecmpMessageMapper;
    @Resource
    private ISmsTemplateInfoService iSmsTemplateInfoService;
    @Autowired
    private EnterpriseCarTypeInfoMapper enterpriseCarTypeInfoMapper;
    @Autowired
    private JourneyPlanPriceInfoMapper journeyPlanPriceInfoMapper;
    @Autowired
    private ApplyUseCarTypeMapper applyUseCarTypeMapper;
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Autowired
    private OrderDispatcheDetailInfoMapper orderDispatcheDetailInfoMapper;
    @Autowired
    private CarGroupServeScopeInfoMapper carGroupServeScopeInfoMapper;
    @Resource
    private ThirdService thirdService;
    @Autowired
    private IJourneyUserCarPowerService journeyUserCarPowerService;
    @Autowired
    private EcmpMessageService ecmpMessageService;
    @Autowired
    @Lazy
    private IOrderInfoService orderInfoService;

    @Resource
    private IsmsBusiness ismsBusiness;

    @Autowired
    private OrderAddressInfoMapper orderAddressInfoMapper;

    @Autowired
    private OrderDispatcheDetailInfoMapper dispatcheDetailInfoMapper;

    @Autowired
    private IEcmpUserService userService;

    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("apply-pool-%d").build();
    private ExecutorService executor = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 查询【请填写功能名称】
     *
     * @param applyId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ApplyInfo selectApplyInfoById(Long applyId)
    {
        return applyInfoMapper.selectApplyInfoById(applyId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param applyInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ApplyInfo> selectApplyInfoList(ApplyInfo applyInfo)
    {
        return applyInfoMapper.selectApplyInfoList(applyInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param applyInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertApplyInfo(ApplyInfo applyInfo)
    {
        applyInfo.setCreateTime(DateUtils.getNowDate());
        return applyInfoMapper.insertApplyInfo(applyInfo);
    }

    /**
     * 修改陈情表信息（撤销行程申请）
     *
     * @param applyInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateApplyInfo(ApplyInfo applyInfo)
    {
        applyInfo.setUpdateTime(DateUtils.getNowDate());
        return applyInfoMapper.updateApplyInfo(applyInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param applyIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApplyInfoByIds(Long[] applyIds)
    {
        return applyInfoMapper.deleteApplyInfoByIds(applyIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param applyId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApplyInfoById(Long applyId)
    {
        return applyInfoMapper.deleteApplyInfoById(applyId);
    }

    /* *//**
 * 提交行程申请单  弃用，公务申请，差旅申请下面分开实现
 * @param journeyCommitApplyDto
 *//*
    @Deprecated
    @Override
    @Transactional
    public void applyCommit(JourneyCommitApplyDto journeyCommitApplyDto) {

    }*/

    /**
     * 提交差旅行程申请
     * @param travelCommitApply
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApplyVO applytravliCommit(ApplyTravelRequest travelCommitApply) {
        //1.保存乘客行程信息 journey_info表
        JourneyInfo journeyInfo = new JourneyInfo();
        //提交差旅行程表信息
        journeyInfoTravelCommit(travelCommitApply, journeyInfo);
        Long journeyId = journeyInfo.getJourneyId();

        //2.保存申请信息 apply_info表
        ApplyInfo applyInfo = new ApplyInfo();
        applyInfoTravelCommit(travelCommitApply, journeyId, applyInfo);
        Long applyId = applyInfo.getApplyId();

        ApplyVO applyVO = ApplyVO.builder().journeyId(journeyId).applyId(applyId).applyNumber(applyInfo.getApplyNumber()).build();

        //3.保存行程节点信息(差旅相关) journey_node_info表
        saveTravelJourneyNodeInfos(travelCommitApply, journeyId);

        //差旅申请 行程表的title字段需要单独设置
        setTitleInJourneyInfo(journeyId);


        //4.保存行程乘客信息 journey_passenger_info表
        // 差旅只有乘车人，没有同行人
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        // 提交差旅乘客信息表
        journeyPassengerInfoCommit(travelCommitApply, journeyId, journeyPassengerInfo);

        //5. -------------- 初始化审批流和权限   发通知（给审批人和自己） 、 发短信（给审批人）-----------------------------
        //initialPowerAndApprovalFlow(travelCommitApply, journeyId, applyId);

        //6.插入可用车型表数据
        List<UseCarTypeVO> canUseCarTypes = travelCommitApply.getCanUseCarTypes();
        saveCanUseCarTypeInfo(applyId,canUseCarTypes);

        return applyVO;
    }

    /**
     * 差旅申请初始化审批流和权限
     * @param travelCommitApply
     * @param journeyId
     * @param applyId
     * @return
     */
    @Override
    public void initialPowerAndApprovalFlow(ApplyTravelRequest travelCommitApply, Long journeyId, Long applyId) throws Exception {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        Integer regimenId = travelCommitApply.getRegimenId();
        if(regimenId != null){
            RegimenVO regimenVO = regimeInfoMapper.selectRegimenVOById(Long.valueOf(regimenId));
            if(regimenVO == null){
                throw new RuntimeException("用车制度不存在");
            }
            String needApprovalProcess = regimenVO.getNeedApprovalProcess();
            //1.差旅申请 初始化审批流  不管需要审批与否都需要初始化审批流
            initOfficialApproveFlow(applyId, userId, regimenId);
            //2.给审批人和自己发通知
            ecmpMessageService.saveMessageUnite(loginUser,applyId,MsgConstant.MESSAGE_T001);
            if(NeedApproveEnum.NEED_NOT_APPROVE.getKey().equals(needApprovalProcess)){
                //3. 如果不需要审批 则初始化生成用车权限  差旅申请不需要初始化订单
                executor.submit(()-> {
                    try {
                        boolean optFlag = journeyUserCarPowerService.createUseCarAuthority(applyId, userId);
                        if(!optFlag){
                            log.error("生成用车权限失败");
                        }
                    } catch (Exception e) {
                        log.error("生成用车权限失败，参数 ",e);
                    }
                });
            }else {
                //4. 如果需要审批 给审批人发短信
                executor.submit(()->{
                    try {
                        String userName = travelCommitApply.getApplyUser().getUserName();
                        Date startDate = travelCommitApply.getStartDate();
                        Date endDate = travelCommitApply.getEndDate();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                        String format = simpleDateFormat.format(startDate);
                        String format1 = simpleDateFormat.format(endDate);
                        String date = format + "-" + format1;
                        List<ApprovalVO> approvers = travelCommitApply.getApprovers();
                        String title = journeyInfoMapper.selectTitleById(journeyId);
                        for (ApprovalVO approver : approvers) {
                            //给审批人发短信  1.员工姓名 2.日期 3.城市
                            // 员工陈超已提交“2019年08月20日-2019年08月23日，长春-上海-长春”的差旅用车申请，请登录红旗公务APP及时处理。（差旅申请）
                            Map<String,String> map = new HashMap<>();
                            map.put("userName",userName);
                            map.put("date",date);
                            map.put("city",title);
                            //给审批人发短信
                            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TRAVEL_APPLY_APPROVER,map,approver.getApprovalPhone());
                        }
                    } catch (Exception e) {
                        log.error("差旅申请，给审批人发送短信失败，参数：{},applyId:{}",JSONArray.toJSON(travelCommitApply).toString(),applyId,e);
                    }
                });
            }
        }
    }

    /**
     * 保存差旅行程节点表信息
     * @param travelCommitApply
     * @param journeyId
     */
    private void saveTravelJourneyNodeInfos(ApplyTravelRequest travelCommitApply, Long journeyId) {
        JourneyNodeInfo journeyNodeInfo = null;
        // TODO 没有往返的情况下。有往返则两个行程节点，一个去程，一个返程
        List<TravelRequest> travelRequests = travelCommitApply.getTravelRequests();
        int i = 1;
        for (TravelRequest travelRequest : travelRequests) {
            journeyNodeInfo = new JourneyNodeInfo();
            //设置差旅行程节点表信息
            setTravelJourneyNode(travelCommitApply, journeyId, journeyNodeInfo, i, travelRequest);
            i++;
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
        }
        //如果有往返，则创建返程节点。返程 出发地为最初目的地，返程目的地为最初出发地。返程开始时间为去程结束时间
        if(CommonConstant.IS_RETURN.equals(travelCommitApply.getIsGoBack())) {

            String json = GsonUtils.objectToJson(travelCommitApply);
            Type type = new TypeToken<ApplyTravelRequest>() {
            }.getRawType();
            ApplyTravelRequest applyTravelRequestCopy = GsonUtils.jsonToBean(json, type);
            List<TravelRequest> travelRequestsCopy = applyTravelRequestCopy.getTravelRequests();
            int size = travelRequestsCopy.size();
            //得到去程节点
            TravelRequest travelRequest2 = travelRequestsCopy.get(0);
            CityInfo startCity = travelRequest2.getStartCity();
            TravelRequest travelRequestSetout = travelRequests.get(size - 1);
            CityInfo endCity = travelRequestSetout.getEndCity();
            //设置返程出发地为最初目的地
            travelRequest2.setStartCity(endCity);
            //设置返程目的地为最初出发地
            travelRequest2.setEndCity(startCity);
            //设置返程开始日期为去程结束日期
            Date endDate = travelRequestSetout.getEndDate();
            travelRequest2.setStartDate(endDate);
            //设置返程结束日期  暂定3天宽限期
            travelRequest2.setEndDate(new Date(endDate.getTime()+3*24*60*60*1000));
            //设置返程节点时长为空
            travelRequest2.setCountDate(3);
            // 返程交通工具暂不做修改
            journeyNodeInfo = new JourneyNodeInfo();
            setTravelJourneyNode(applyTravelRequestCopy, journeyId, journeyNodeInfo, i + 1, travelRequest2);
            //新增返程节点
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
        }
    }

    /**
     * 设置行程表的title字段
     * @param journeyId
     */
    private void setTitleInJourneyInfo(Long journeyId) {
        //行程表的title字段需要重新生成  2种情况
        // 情况1： 行程①：北京-上海   行程②：上海-南京   行程③：南京-北京   生成：北京-上海、南京、北京
        // 情况2：  行程①：北京-上海   行程②：南京-西安                  生成：北京-上海；南京-西安
        //查询申请单的所有行程节点
        JourneyNodeInfo build = JourneyNodeInfo.builder().journeyId(journeyId).build();
        List<JourneyNodeInfo> list = journeyNodeInfoMapper.selectJourneyNodeInfoList(build);
        Collections.sort(list, new Comparator<JourneyNodeInfo>() {
            @Override
            public int compare(JourneyNodeInfo o1, JourneyNodeInfo o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        //第一个节点
        sb.append(list.get(0).getPlanBeginAddress() + "-"+list.get(0).getPlanEndAddress());
        for (int j = 1; j < size; j++) {
            //如果下一个节点的起点不是上一个起点的终点，则下一个节点追加   ；上海-北京  样字段
            if (!list.get(j).getPlanBeginAddress().equals(list.get(j - 1).getPlanEndAddress())) {
                sb.append("；");
                sb.append(list.get(j).getPlanBeginAddress() + "-" + list.get(j).getPlanEndAddress());
            } else {
                //如果下一个节点的起点是上一个节点的终点，则下一个节点追加  、上海、南京  样字段
                sb.append("、"  + list.get(j).getPlanEndAddress());
            }
        }

        String title = sb.toString();

        //行程表添加title信息
        JourneyInfo build1 = JourneyInfo.builder().title(title).journeyId(journeyId).build();
        journeyInfoMapper.updateJourneyInfo(build1);
    }

    /**
     * 设置差旅行程节点表信息
     * @param travelCommitApply
     * @param journeyId
     * @param journeyNodeInfo
     * @param i
     * @param travelRequest
     */
    private void setTravelJourneyNode(ApplyTravelRequest travelCommitApply, Long journeyId, JourneyNodeInfo journeyNodeInfo, int i, TravelRequest travelRequest) {
        //3.1 journey_id 非空
        journeyNodeInfo.setJourneyId(journeyId);
        //3.2 user_id 行程申请人 编号
        journeyNodeInfo.setUserId(travelCommitApply.getApplyUser().getUserId());
        //3.3 plan_begin_address 计划上车地址  非空
        journeyNodeInfo.setPlanBeginLongAddress(null);
        journeyNodeInfo.setPlanBeginAddress(travelRequest.getStartCity().getCityName());
        journeyNodeInfo.setPlanBeginCityCode(travelRequest.getStartCity().getCityCode());
        //3.4 plan_end_address 计划下车地址    非空
        journeyNodeInfo.setPlanEndLongAddress(null);
        journeyNodeInfo.setPlanEndAddress(travelRequest.getEndCity().getCityName());
        journeyNodeInfo.setPlanEndCityCode(travelRequest.getEndCity().getCityCode());
        //3.5 plan_setout_time 计划出发时间     出差某一节点开始日期
        journeyNodeInfo.setPlanSetoutTime(travelRequest.getStartDate());
        //3.6 plan_arrive_time 计划到达时间  出差某一节点结束日期
        Date endDate = travelRequest.getEndDate();
        journeyNodeInfo.setPlanArriveTime(new Date(endDate.getTime() + 24*60*60*1000-1000));
        //3.7 plan_begin_longitude 出发坐标   差旅是城市代码、城市id
        journeyNodeInfo.setPlanBeginLongitude(null);
        //3.8 plan_begin_latitude
        journeyNodeInfo.setPlanBeginLatitude(null);
        //3.9 plan_end_longitude
        journeyNodeInfo.setPlanEndLongitude(null);
        //3.10 plan_end_latitude
        journeyNodeInfo.setPlanEndLatitude(null);
        //3.11 it_is_via_point 是否是途经点  途经点仅仅用于 地图描点  和  导航使用;途经点  同样具有顺序
        journeyNodeInfo.setItIsViaPoint(null);
        //3.12 vehicle 交通工具 T001  飞机 T101  火车 T201  汽车 T301  轮渡 T999  其他
        journeyNodeInfo.setVehicle(travelRequest.getVehicle());
        //3.13 duration 行程节点 预估用时， X 小时 X 分钟    差旅节点时长是多少天字段。公务应该是X 小时 X 分钟
        journeyNodeInfo.setDuration(String.valueOf(travelRequest.getCountDate()));
        //3.14 distance 行程节点 预估里程 单位公：里
        journeyNodeInfo.setDistance(null);
        //3.15 wait_duration 航班到达后等待多时时间  用车，单位 分钟 M010 10分钟 M020 20分钟 M030 30分钟 H100 一小时 H130 一个半小时
        journeyNodeInfo.setWaitDuration(null);
        //3.16 node_state 行程节点状态 订单---->用车权限----->行程节点   反推  节点任务是否已完成 失效 也算已经  节点任务已经完成
        //P000   有效中  P444   已失效
        journeyNodeInfo.setNodeState(CommonConstant.VALID_NODE);
        ///3.17 number 节点在 在整个行程 中的顺序编号 从  1  开始
        journeyNodeInfo.setNumber(i);
        //3.18 create_by 创建者
        journeyNodeInfo.setCreateBy(String.valueOf(getLoginUserId()));
        //3.19 create_time 创建时间
        journeyNodeInfo.setCreateTime(new Date());
        //3.20 update_by 更新者
        journeyNodeInfo.setUpdateBy(null);
        //3.21 update_time 更新时间
        journeyNodeInfo.setUpdateTime(null);
    }

    /**
     * 分页查询用户申请列表
     * @param userId  用户id
     * @param pageNum 查询页码
     * @return
     */
    @Override
    public PageResult<ApplyInfoDTO> selectApplyInfoListByPage(Long userId, Integer pageNum, Integer pageSize) {
        //分页查询申请列表
        PageHelper.startPage(pageNum,pageSize);
        List<ApplyInfoDTO> all = applyInfoMapper.selectApplyInfoListByPage(userId);
        PageInfo<ApplyInfoDTO> pageInfo = new PageInfo<>(all);
        PageResult<ApplyInfoDTO> pageResult = new PageResult<>(pageInfo.getTotal(),pageInfo.getPages(),all);
        return pageResult;
    }

    /**
     * 查询申请单详情
     * @param applyId
     * @return
     */
    @Override
    public ApplyDetailVO selectApplyDetail(Long applyId) {
        //1.根据applyId查询申请表相关信息
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        String projectName="";
        String costCenter="";
        ProjectInfo projectInfo = projectInfoMapper.selectProjectInfoById(applyInfo.getProjectId());
        if (projectInfo!=null){
            projectName=projectInfo.getName();
        }
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(applyInfo.getCostCenter());
        if (ecmpOrg!=null){
            costCenter=ecmpOrg.getDeptName();
        }
        String rejectReason="";
        if (ApplyStateConstant.REJECT_APPLY.equals(applyInfo.getState())){
            //查询驳回原因
            List<ApplyApproveResultInfo> applyApproveResultInfos = applyApproveResultInfoMapper.selectApplyApproveResultInfoList(new ApplyApproveResultInfo(applyId, ApproveStateEnum.APPROVE_FAIL.getKey(), ApproveStateEnum.COMPLETE_APPROVE_STATE.getKey()));
            if (!CollectionUtils.isEmpty(applyApproveResultInfos)){
                rejectReason=applyApproveResultInfos.get(0).getContent();
            }
        }
        ApplyDetailVO applyDetailVO = ApplyDetailVO.builder()
                //申请原因
                .reason(applyInfo.getReason()).applyId(applyInfo.getApplyId())
                //行程id
                .jouneyId(applyInfo.getJourneyId())
                //成本中心
                .costCenter(costCenter)
                //项目编号
                .projectNumber(projectName)
                //申请状态
                .status(applyInfo.getState())
                .time(applyInfo.getCreateTime())
                .rejectReason(rejectReason)
                //申请类型
                .type(applyInfo.getApplyType())
                .build();
        //2.根据journreyId查询行程表相关信息
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(applyInfo.getJourneyId());
        setApplyDetailFromJourney(applyInfo, applyDetailVO, journeyInfo);
        //3.根据journeyId查询行程乘客表相关信息
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        journeyPassengerInfo.setJourneyId(applyInfo.getJourneyId());
        List<JourneyPassengerInfo> journeyPassengerInfos = journeyPassengerInfoMapper.selectJourneyPassengerInfoList(journeyPassengerInfo);
        StringBuilder stringBuilder = new StringBuilder();
        for (JourneyPassengerInfo passengerInfo : journeyPassengerInfos) {
            //乘车人01  公务跟差旅 必有且仅有一个
            if (CommonConstant.IS_NOT_PEER.equals(passengerInfo.getItIsPeer())){
                applyDetailVO.setRiderName(passengerInfo.getName());
            }else {
                // 同行者  00
                stringBuilder.append(passengerInfo.getName()+"、");
            }
        }
        if(stringBuilder !=  null){
            //同行者  00
            String parters = stringBuilder.toString();
            if (StringUtils.isNotBlank(parters)){
                applyDetailVO.setPartner(parters.substring(0, parters.length() - 1));
            }
        }
        //4.根据journeyId查询行程节点表数据  差旅 和 公务有所不同
        JourneyNodeInfo journeyNodeInfo = new JourneyNodeInfo();
        journeyNodeInfo.setJourneyId(applyInfo.getJourneyId());
        List<JourneyNodeInfo> journeyNodeInfos = journeyNodeInfoMapper.selectJourneyNodeInfoList(journeyNodeInfo);
        int size = journeyNodeInfos.size();
        String applyType = applyInfo.getApplyType();
        //如果是公务申请 A001:  公务用车   A002:  差旅用车   公务的节点 只有上车地点和下车地点
        if(CommonConstant.AFFICIAL_APPLY.equals(applyType)){
            setBeginAndEndAddrForOfficial(applyDetailVO, journeyInfo, journeyNodeInfos, size);
        }
        //如果是差旅申请A002:  差旅用车
        setApplyDetailForTravel(applyDetailVO, journeyInfo, journeyNodeInfos, applyType);

        return applyDetailVO;
    }

    /**
     * 申请详情中的行程表信息
     * @param applyInfo
     * @param applyDetailVO
     * @param journeyInfo
     */
    private void setApplyDetailFromJourney(ApplyInfo applyInfo, ApplyDetailVO applyDetailVO, JourneyInfo journeyInfo) {
        //用车时间
        Date useCarTime = journeyInfo.getUseCarTime();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        applyDetailVO.setApplyDate(useCarTime);
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(journeyInfo.getUserId());
        //申请人
        applyDetailVO.setApplyUser(ecmpUser.getNickName());
        applyDetailVO.setApplyMobile(ecmpUser.getPhonenumber());
        //是否往返
        applyDetailVO.setIsGoBack(journeyInfo.getItIsReturn());
        //返回等待时间
        applyDetailVO.setWaitingTime(journeyInfo.getWaitTimeLong());
        //预估价格
        applyDetailVO.setEstimatePrice(journeyInfo.getEstimatePrice());
        //根据制度id查询是否需要审批
        RegimeVo regimeInfo = regimeInfoMapper.queryRegimeDetail(applyInfo.getRegimenId());
        applyDetailVO.setNeedApprovalProcess(regimeInfo.getNeedApprovalProcess());
    }

    /**
     * 差旅申请单详情信息设置
     * @param applyDetailVO
     * @param journeyInfo
     * @param journeyNodeInfos
     * @param applyType
     */
    private void setApplyDetailForTravel(ApplyDetailVO applyDetailVO, JourneyInfo journeyInfo, List<JourneyNodeInfo> journeyNodeInfos, String applyType) {
        TripDescription tripDescription = new TripDescription();
        //市内用车城市
        tripDescription.setTripCity(journeyInfo.getTravelCitiesStr());
        //差旅开始时间
        tripDescription.setStartDate(journeyInfo.getStartDate());
        //差旅结束时间
        tripDescription.setEndDate(journeyInfo.getEndDate());
        //接送机/站总次数
        tripDescription.setPickupTimes(journeyInfo.getPickupTimes());
        //接送城市 及 接送次数 集合
        String travelPickupCity = journeyInfo.getTravelPickupCity();
        List<TravelPickupCity> travelPickupCities = JSONObject.parseArray(travelPickupCity, TravelPickupCity.class);
        ArrayList<String> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(travelPickupCities)){
            for (TravelPickupCity pickupCity : travelPickupCities) {
                String cityName = pickupCity.getCityName();
                //接机/站次数
                int pickup = pickupCity.getPickup();
                //送机/站次数
                int dropOff = pickupCity.getDropOff();
                if(pickup == dropOff ){
                    list.add(cityName+"   "+"接送服务各"+pickup+"次");
                }else {
                    if(pickup == 0){
                        list.add(cityName+"   "+"送机/站服务"+dropOff+"次");
                    }
                    if(dropOff == 0){
                        list.add(cityName+"   "+"接机/站服务"+pickup+"次");
                    }
                }
            }
        }

        tripDescription.setTripDesc(list);
        //如果是差旅申请  行程节点集合 上海-北京
        ArrayList<String> tripList = new ArrayList<>();
        if(CommonConstant.TRAVLE_APPLY.equals(applyType)){
            for (JourneyNodeInfo nodeInfo : journeyNodeInfos) {
                tripList.add(nodeInfo.getPlanBeginAddress()+"-"+nodeInfo.getPlanEndAddress());
            }
        }
        tripDescription.setTrips(tripList);
        applyDetailVO.setTripDescription(tripDescription);
    }


    /**
     * 公务申请单详情 设置上下车地址
     * @param applyDetailVO
     * @param journeyInfo
     * @param journeyNodeInfos
     * @param size
     */
    private void setBeginAndEndAddrForOfficial(ApplyDetailVO applyDetailVO, JourneyInfo journeyInfo, List<JourneyNodeInfo> journeyNodeInfos, int size) {
        for (JourneyNodeInfo nodeInfo : journeyNodeInfos) {
            if(nodeInfo.getNumber() == 1){
                //上车地点 即是节点编号为1的出发地点
                applyDetailVO.setStartAddress(nodeInfo.getPlanBeginAddress());
            }
            //下车地点  如果没有往返，则下车地点为非途径地节点的目的地 是否往返 Y000 N444
            if(CommonConstant.IS_NOT_RETURN.equals(journeyInfo.getItIsReturn())){
                if(nodeInfo.getNumber() == size){
                    applyDetailVO.setEndAddress(nodeInfo.getPlanEndAddress());
                }
            }else {
                // 如果有往返，则下车地点为倒数第二个节点的目的地
                if(nodeInfo.getNumber() == size -1){
                    applyDetailVO.setEndAddress(nodeInfo.getPlanEndAddress());
                }
            }
        }
    }

    @Override
    public List<MessageDto> getOrderCount(Long userId) {
        List<MessageDto> list= applyInfoMapper.getOrderCount(userId);
        return list;
    }

    //获取申请通知
    @Override
    public MessageDto getApplyMessage(Long userId) {
        String[] states=new String[]{"S299","S600","S616","S699","S900"};//排除订单已派车后的申请单
        MessageDto applyMessage = applyInfoMapper.getApplyMessage(userId, states);
        return applyMessage;
    }

    @Override
    public int getApplyApproveCount(Long userId) {
        //获取用户权限
//        List<EcmpUserRole> list=userRoleMapper.selectEcmpUserRoleList(new EcmpUserRole(userId));
//        List<Long> collect = list.stream().map(EcmpUserRole::getRoleId).collect(Collectors.toList());
        return applyInfoMapper.getApplyApproveCount(userId, ApproveStateEnum.WAIT_APPROVE_STATE.getKey(),null);
    }

    /**
     * 审批列表
     * @param pageIndex
     * @param pageSize
     * @param userId
     * @return
     */
    @Override
    public List<ApprovaReesultVO> getApprovePage(int pageIndex,int pageSize,Long userId) {
        PageHelper.startPage(pageIndex,pageSize);
        List<ApprovaReesultVO> applyApproveResultInfos = resultInfoMapper.selectResultList(userId,ApproveStateEnum.NOT_ARRIVED_STATE.getKey());
        if (!CollectionUtils.isEmpty(applyApproveResultInfos)){
            for (ApprovaReesultVO info:applyApproveResultInfos){
                //查询申请信息
                info.setState(ApproveStateEnum.format(info.getState()));
                List<JourneyNodeInfo> journeyNodeInfos = journeyNodeInfoMapper.selectJourneyNodeInfoList(new JourneyNodeInfo(info.getJouneyId()));
                if (!CollectionUtils.isEmpty(journeyNodeInfos)){
                    //判断是差旅还是公务
                    if (ApplyTypeEnum.APPLY_TRAVEL_TYPE.getKey().equals(info.getApplyType())){//差旅
                        info.setUseCarTime(DateFormatUtils.formatDate(DateFormatUtils.DATE_FORMAT_CN,info.getStartDate())+"-"+DateFormatUtils.formatDate(DateFormatUtils.DATE_FORMAT_CN,info.getEndDate()));
                        String stroke="";
                        for (JourneyNodeInfo nodeInfo:journeyNodeInfos){
                            stroke+=","+nodeInfo.getPlanBeginAddress()+"-"+nodeInfo.getPlanEndAddress();
                        }
                        info.setStroke(stroke.substring(1));
                    }else{//公务
                        info.setStartAddress(journeyNodeInfos.get(0).getPlanBeginAddress());
                        info.setEndAddress(journeyNodeInfos.get(0).getPlanEndAddress());
                    }
                }
                info.setApplyType(ApplyTypeEnum.format(info.getApplyType()));
            }
        }
        return applyApproveResultInfos;
    }

    @Override
    public Integer getApprovePageCount(Long userId) {
        return resultInfoMapper.getApprovePageCount(userId,ApproveStateEnum.NOT_ARRIVED_STATE.getKey());
    }

    /**
     * 提交差旅乘客信息表
     * @param travelCommitApply
     * @param journeyId
     * @param journeyPassengerInfo
     */
    private void journeyPassengerInfoCommit(ApplyTravelRequest travelCommitApply, Long journeyId, JourneyPassengerInfo journeyPassengerInfo) {
        //4.1 journey_id 非空
        journeyPassengerInfo.setJourneyId(journeyId);
        //4.2 name 姓名
        journeyPassengerInfo.setName(travelCommitApply.getPassenger().getUserName());
        //4.3 mobile
        journeyPassengerInfo.setMobile(travelCommitApply.getApplyUser().getUserPhone());
        //4.4 it_is_peer 是否是同行者 00   是   01   否    常量 差旅没有同行人
        journeyPassengerInfo.setItIsPeer(CommonConstant.IS_NOT_PEER);
        //4.5 create_by 创建者
        journeyPassengerInfo.setCreateBy(String.valueOf(getLoginUserId()));
        //4.6 create_time 创建时间
        journeyPassengerInfo.setCreateTime(new Date());
        //4.7 update_by 更新者
        journeyPassengerInfo.setUpdateBy(null);
        //4.8 update_time 更新时间
        journeyPassengerInfo.setUpdateTime(null);
        journeyPassengerInfoMapper.insertJourneyPassengerInfo(journeyPassengerInfo);
    }

    /**
     * 提交差旅申请信息表
     * @param travelCommitApply
     * @param journeyId
     * @param applyInfo
     */
    private void applyInfoTravelCommit(ApplyTravelRequest travelCommitApply, Long journeyId, ApplyInfo applyInfo) {
        //2.1 journey_id 非空
        applyInfo.setJourneyId(journeyId);
        //2.2 project_id
        String projectNumber = travelCommitApply.getProjectNumber();
        if(projectNumber != null) {
            applyInfo.setProjectId(Long.valueOf(projectNumber));
        }
        //2.3 regimen_id 非空
        Integer regimenId = travelCommitApply.getRegimenId();
        if(regimenId != null) {
            applyInfo.setRegimenId(Long.valueOf(regimenId));
        }
        //所属公司
        applyInfo.setCompanyId(travelCommitApply.getCompanyId());
        //2.4 apply_type 用车申请类型；A001:  公务用车 A002:  差旅用车
        applyInfo.setApplyType(String.valueOf(travelCommitApply.getApplyType()));
        //2.5 approver_name 第一审批阶段 审批人列表，前两位
        List<ApprovalVO> approvers = travelCommitApply.getApprovers();
        String approversName = null;
        if(approvers.size() == 1){
            approversName = approvers.get(0).getApprovalName();
        }else if (approvers.size()>1){
            for (int i = 0; i < 2; i++) {
                approversName = approvers.get(0).getApprovalName() +"、"+ approvers.get(1).getApprovalName();
            }
        }
        applyInfo.setApproverName(approversName);
        //2.6 cost_center 成本中心 从组织机构表 中获取
        String costCenter = travelCommitApply.getCostCenter();
        if(costCenter != null) {
            applyInfo.setCostCenter(Long.valueOf(costCenter));
        }
        //2.7 state 申请审批状态 S001  申请中 S002  通过 S003  驳回 S004  已撤销
        applyInfo.setState(ApplyStateConstant.ON_APPLYING);
        //2.8 reason 行程原因
        applyInfo.setReason(travelCommitApply.getReason());
        //2.9 create_by 创建者
        applyInfo.setCreateBy(String.valueOf(getLoginUserId()));
        //2.10 create_time 创建时间
        applyInfo.setCreateTime(new Date());
        //2.11 update_by 更新者
        applyInfo.setUpdateBy(null);
        //2.12 update_time 更新时间
        applyInfo.setUpdateTime(null);
        //2.13 申请单编号
        applyInfo.setApplyNumber(RandomUtil.getRandomNumber());

        applyInfoMapper.insertApplyInfo(applyInfo);
    }

    /**
     * 提交差旅行程表信息
     * @param travelCommitApply
     * @param journeyInfo
     */
    private void journeyInfoTravelCommit(ApplyTravelRequest travelCommitApply, JourneyInfo journeyInfo) {
        //1.1 userId     申请人用户编号     非空
        journeyInfo.setUserId(Long.valueOf(travelCommitApply.getApplyUser().getUserId()));
        //1.2 用车制度id        非空
        Integer regimenId = travelCommitApply.getRegimenId();
        if(regimenId != null) {
            journeyInfo.setRegimenId(Long.valueOf(regimenId));
        }
        //1.3 service_type 预约、接机、送机、包车 1000预约 2001接机 2002送机 3000包车
        journeyInfo.setServiceType(null);
        //1.4 use_car_mode 用车方式（自有、网约车）
        journeyInfo.setUseCarMode(travelCommitApply.getUseType());
        //增加所属公司
        journeyInfo.setCompanyId(travelCommitApply.getCompanyId());
        //1.5 use_car_time 用车时间
        Date startDate = travelCommitApply.getTravelRequests().get(0).getStartDate();
        journeyInfo.setUseCarTime(startDate);
        //1.6 it_is_return 是否往返 Y000 N444
        journeyInfo.setItIsReturn(travelCommitApply.getIsGoBack());
        //1.7 estimate_price 预估价格     非空
        journeyInfo.setEstimatePrice(null);
        //1.8 project_id  项目编号
        String projectNumber = travelCommitApply.getProjectNumber();
        if(projectNumber != null) {
            journeyInfo.setProjectId(Long.valueOf(projectNumber));
        }
        //1.9 flight_number 航班编号
        journeyInfo.setFlightNumber(null);
        //1.10 use_time 行程总时长  多少天
        journeyInfo.setUseTime(String.valueOf(travelCommitApply.getTravelCount()));
        //1.11 wait_time_long 预计等待时间，出发地 第一个节点 等待时长
        journeyInfo.setWaitTimeLong(null);
        //1.12 charter_car_type 包车类型：T000  非包车 T001 半日租（4小时）T002 整日租（8小时）
        journeyInfo.setCharterCarType(null);
        //1.13 create_by 创建者
        journeyInfo.setCreateBy(String.valueOf(getLoginUserId()));
        //1.14 create_time 创建时间
        journeyInfo.setCreateTime(new Date());
        //1.15 update_by 更新者
        journeyInfo.setUpdateBy(null);
        //1.16 update_time 更新时间
        journeyInfo.setUpdateTime(null);
        journeyInfo.setStartDate(travelCommitApply.getStartDate());
        Date endDate = travelCommitApply.getEndDate();
        journeyInfo.setEndDate(new Date(endDate.getTime() + 24*60*60*1000-1000));
        List<TravelPickupCity> travelPickupCityList = travelCommitApply.getTravelPickupCity();
        String travelPickupCityStr  = JSONArray.toJSON(travelPickupCityList).toString();
        journeyInfo.setTravelPickupCity(travelPickupCityStr);
        journeyInfo.setTravelCitiesStr(travelCommitApply.getTravelCitiesStr());
        journeyInfo.setPickupTimes(travelCommitApply.getPickupTimes());
        String firstCityName = travelCommitApply.getTravelRequests().get(0).getStartCity().getCityName();
        journeyInfo.setTitle(firstCityName);  //
        journeyInfoMapper.insertJourneyInfo(journeyInfo);
    }

    /**
     * 提交公务行程申请
     * @param officialCommitApply
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApplyVO applyOfficialCommit(ApplyOfficialRequest officialCommitApply) throws Exception {

        //公务申请 单程的行车时长（单位秒）（调预约价或者高德接口）
        Integer duration2;
        DirectionDto directionDto = null;
        String useCarModeOwnerLevel="";
        //调用高德情况下 单程的行车时长（单位秒）
        int totalTime = 0;
        //预估行驶时间
        RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById((long) officialCommitApply.getRegimenId());
        String canUseCarMode = regimeInfo.getCanUseCarMode();
        //如果用车方式包含自有车则调用高德接口查询单程用车时间（秒）
        if(canUseCarMode.contains(CarConstant.USR_CARD_MODE_HAVE)) {
            //公务自有车的可选车型
            useCarModeOwnerLevel = regimeInfo.getUseCarModeOwnerLevel();
            if (StringUtils.isBlank(useCarModeOwnerLevel)) {
                throw new Exception("自有车无可用车型");
            }
            AddressVO startAddr = officialCommitApply.getStartAddr();
            String startPoint = startAddr.getLongitude() + "," + startAddr.getLatitude();
            AddressVO endAddr = officialCommitApply.getEndAddr();
            String endPoint = endAddr.getLongitude() + "," + endAddr.getLatitude();
            //不是包车才能调高德
            if(!ServiceTypeConstant.CHARTERED.equals(officialCommitApply.getServiceType())){
                directionDto = thirdService.drivingRoute(startPoint, endPoint);
                if (directionDto == null || directionDto.getCount() == 0) {
                    throw new Exception("获取时长和里程失败");
                }
                List<PathDto> paths = directionDto.getRoute().getPaths();
                for (int i = 0; i <paths.size() ; i++) {
                    //此处高德时间单位是秒
                    totalTime = (totalTime + paths.get(i).getDuration());
                }
                totalTime = Math.round(totalTime/paths.size());
            }
        }
        if(officialCommitApply.getCarLevelAndPriceVOs() !=null){
            // 如果预估价接口返回数据则使用预估价计算的单程行驶时间 单位为分钟 *60 转为秒
            duration2 = officialCommitApply.getCarLevelAndPriceVOs().get(0).getDuration()*60;
        }else{
            //预估价接口没有数据则使用（totalTime）高德地图接口返回的单程预计时间  单位是秒
            duration2 = totalTime;
        }

        //1.保存乘客行程信息 journey_info表
        JourneyInfo journeyInfo = new JourneyInfo();
        //提交公务行程表信息
        journeyOfficialCommit(officialCommitApply, journeyInfo,duration2);
        Long journeyId = journeyInfo.getJourneyId();

        //2.保存申请信息 apply_info表
        ApplyInfo applyInfo = new ApplyInfo();
        //提交公务申请表信息
        applyInfoOfficialCommit(officialCommitApply, journeyId, applyInfo);
        Long applyId = applyInfo.getApplyId();

        ApplyVO applyVO = ApplyVO.builder().applyId(applyId).journeyId(journeyId).applyNumber(applyInfo.getApplyNumber()).build();

        String json = GsonUtils.objectToJson(officialCommitApply);
        Type type = new TypeToken<ApplyOfficialRequest>() {
        }.getRawType();
        ApplyOfficialRequest applyOfficialRequest = GsonUtils.jsonToBean(json, type);
        //3.保存行程节点信息 journey_node_info表
        JourneyNodeBo journeyNodeBo = new JourneyNodeBo();
        saveJourneyNodeInfos(officialCommitApply, duration2, journeyId, applyOfficialRequest, journeyNodeBo);

        //4.保存行程乘客信息 journey_passenger_info表
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        journeyPassergerOfficialCommit(officialCommitApply, journeyId, journeyPassengerInfo);
        Long userId = getLoginUserId();

        //5.------------------- 公务申请 保存预估价格表 -------------------
        saveEstimatePriceInfo(officialCommitApply, totalTime, regimeInfo, journeyId, journeyNodeBo, userId);

        //6.公务申请成功后 ---------1. 调用初始化审批流方法 2.给审批人发送通知，给自己发送通知 3.给审批人发送短信  4.如果不需要审批则还需要初始化权限、订单

        //initialOfficialPowerAndApprovalFlow(officialCommitApply, journeyId,  applyId, userId);

        //7.新增可用车型表信息
        List<UseCarTypeVO> canUseCarTypes = officialCommitApply.getCanUseCarTypes();
        saveCanUseCarTypeInfo(applyId, canUseCarTypes);

        return applyVO;
    }

    /**
     * 公务申请 初始化审批流、（如果不需要审批则还需要初始化权限、订单）
     * @param officialCommitApply
     * @param journeyId
     * @param applyId
     * @param userId
     */
    @Override
    public List<Long> initialOfficialPowerAndApprovalFlow(LoginUser loginUser,ApplyOfficialRequest officialCommitApply, Long journeyId,  Long applyId, Long userId) throws Exception {
        String applyType = officialCommitApply.getApplyType();
        Integer regimenId = officialCommitApply.getRegimenId();
        List<Long> orderIds = null;
        if(regimenId != null){
            RegimenVO regimenVO = regimeInfoMapper.selectRegimenVOById(Long.valueOf(regimenId));
            String needApprovalProcess = regimenVO.getNeedApprovalProcess();
            //1.初始化审批流  不管需要审批与否，都需要初始化审批流
            initOfficialApproveFlow(applyId, userId, regimenId);
            //2.发通知
            ecmpMessageService.saveMessageUnite(loginUser,applyId,MsgConstant.MESSAGE_T001);
            if(NeedApproveEnum.NEED_NOT_APPROVE.getKey().equals(needApprovalProcess)){
                //3.1 不需要审批 则初始化权限和初始化订单
                orderIds = initPowerAndOrder(journeyId, applyType, applyId, userId,officialCommitApply);
            }else {
                //----------------- 如果需要审批     给审批人发送短信
                //3.2  需要审批 给审批人发短信
                if(ItIsSupplementEnum.ORDER_DIRECT_SCHEDULING_STATUS.getValue().equals(officialCommitApply.getDistinguish())){
                    //3.2.1如果是直接调度，走直接调度流程
                    applyApproveResultInfoMapper.updateApproveState(applyId, ApproveStateEnum.COMPLETE_APPROVE_STATE.getKey(),ApproveStateEnum.APPROVE_PASS.getKey());
                    orderIds = initPowerAndOrder(journeyId, applyType, applyId, userId,officialCommitApply);
                }else {
                    //3.2.2 不是直接调度 则给审批人发短信
                    sendMessageToApproverForOfficial(officialCommitApply, applyId, userId);
                }
            }
        }
        return orderIds;
    }

    /**
     * 公务申请保存行程所有节点信息
     * @param officialCommitApply
     * @param duration2
     * @param journeyId
     * @param applyOfficialRequest
     * @param journeyNodeBo
     */
    private void saveJourneyNodeInfos(ApplyOfficialRequest officialCommitApply, Integer duration2, Long journeyId, ApplyOfficialRequest applyOfficialRequest, JourneyNodeBo journeyNodeBo) {
        JourneyNodeInfo journeyNodeInfo = null;
        //  公务是一个行程  如果往返，需要创建两个行程节点还是一个？ 创建两个
        // 遍历途径地
        List<AddressVO> passedAddressList = officialCommitApply.getPassedAddress();
        // 途径地数量为size个
        // 第一个节点出发地是出发地，目的地是第一个途径地，
        // 第二个节点出发地是第一个途径地，目的地是第二个途径地
        // 第n个节点的出发地是第n-1个途径地，目的地是第n个途径地
        // 最后一个节点的出发地是最后一个途径地，目的地是目的地
        int size = 0;
        if(passedAddressList != null){
            size = passedAddressList.size();
        }
        //节点编号
        int n = 1;
        //如果没有途经点 就只有一个行程节点

        if(size == 0){
            journeyNodeInfo = new JourneyNodeInfo();
            //设置行程节点信息表
            journeyNodeOfficialCommit(applyOfficialRequest, journeyId, journeyNodeInfo);
            //设置用车时间
            // use_car_time 用车时间
            Date applyDate = officialCommitApply.getApplyDate();
            if (applyDate != null){
                journeyNodeInfo.setPlanSetoutTime(applyDate);
                if(!"5000".equals(officialCommitApply.getServiceType())){
                    //如果不是包车 则预计到达时间为出发时间 加上行驶时间(此处为没有途经点 情况 的去程节点预计到达时间)
                    journeyNodeInfo.setPlanArriveTime(new Date(applyDate.getTime() + duration2*1000));
                }else {
                    //如果是包车 包车只有一个节点 是没有途经点，也没有往返的
                    String charterType = officialCommitApply.getCharterType();
                    if (CharterTypeEnum.HALF_DAY_TYPE.getKey().equals(charterType)) {
                        //半日租 4小时
                        Date endTime = new Date(applyDate.getTime() + 4 * 60 * 60 * 1000);
                        journeyNodeInfo.setPlanArriveTime(endTime);
                    } else if (CharterTypeEnum.OVERALL_RENT_TYPE.getKey().equals(charterType)) {
                        //整日组 8小时
                        Date endTime = new Date(applyDate.getTime() + 8 * 60 * 60 * 1000);
                        journeyNodeInfo.setPlanArriveTime(endTime);
                    }
                }
            }else {
                //如果applyDate为空，则表示接机
                Long flightPlanArriveTime = officialCommitApply.getFlightPlanArriveTime().getTime();
                Date useCarTime = getUseCarTimeForFlight(officialCommitApply, flightPlanArriveTime);
                journeyNodeInfo.setPlanSetoutTime(useCarTime);
                journeyNodeInfo.setPlanArriveTime(new Date(useCarTime.getTime() + duration2*1000));
            }
            //判断是否途经点  er模型中  是这样的   途经点  同样具有顺序  Y000     是  N111    否
            journeyNodeInfo.setItIsViaPoint(CommonConstant.NO_PASS);
            //节点编号
            journeyNodeInfo.setNumber(n);
            //保存数据
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
            journeyNodeBo.setNodeIdNoReturn(journeyNodeInfo.getNodeId());
            //nodeIdNoReturn = journeyNodeInfo.getNodeId();

        }else {
            //A...提交第一个节点 把第一个途经点作为目的地
            journeyNodeInfo = new JourneyNodeInfo();
            AddressVO firstEndAddr = passedAddressList.get(0);
            applyOfficialRequest.setEndAddr(firstEndAddr);
            //设置行程节点信息表
            journeyNodeOfficialCommit(applyOfficialRequest, journeyId, journeyNodeInfo);
            //判断是否途经点  是 Y000
            journeyNodeInfo.setItIsViaPoint(CommonConstant.PASS);
            //节点编号
            journeyNodeInfo.setNumber(n);
            // use_car_time 用车时间
            Date applyDate = officialCommitApply.getApplyDate();
            setUseCarTime(officialCommitApply, journeyNodeInfo, applyDate);
            //保存数据
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
            //B...提交最后一个节点
            journeyNodeInfo = new JourneyNodeInfo();
            //把最后一个途径地作为起点
            AddressVO lastStartAddr = passedAddressList.get(size-1);
            applyOfficialRequest.setStartAddr(lastStartAddr);
            //目的地为参数传递目的地
            applyOfficialRequest.setEndAddr(officialCommitApply.getEndAddr());
            //设置行程节点信息表
            journeyNodeOfficialCommit(applyOfficialRequest, journeyId, journeyNodeInfo);
            //判断是否途经点  最后一个节点 不是途经点 N111
            journeyNodeInfo.setItIsViaPoint(CommonConstant.NO_PASS);
            //节点编号
            journeyNodeInfo.setNumber(n+size);
            // 设置去程最后一个节点的起止时间 （有结束时间，无开始时间）
            setArriveTime(officialCommitApply, journeyNodeInfo, duration2, applyDate);
            //保存数据
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
            journeyNodeBo.setNodeIdNoReturn(journeyNodeInfo.getNodeId());
            //nodeIdNoReturn = journeyNodeInfo.getNodeId();
            //保存中间行程节点
            saveMiddleJourneyNode(journeyId, applyOfficialRequest, passedAddressList, size, n);
        }
        //如果有往返 Y000，追加一个返程节点。
        if(CommonConstant.IS_RETURN.equals(officialCommitApply.getIsGoBack())) {
            journeyNodeInfo = new JourneyNodeInfo();
            //出发地是目的地，返回地是起始地
            applyOfficialRequest.setStartAddr(officialCommitApply.getEndAddr());
            applyOfficialRequest.setEndAddr(officialCommitApply.getStartAddr());
            //设置行程节点信息表
            journeyNodeOfficialCommit(applyOfficialRequest, journeyId, journeyNodeInfo);
            //判断是否途经点   返程没有途经点 N111
            journeyNodeInfo.setItIsViaPoint(CommonConstant.NO_PASS);
            //设置节点编号
            journeyNodeInfo.setNumber(n+size+1);
            // use_car_time 用车时间
            Date applyDate = officialCommitApply.getApplyDate();
            if (applyDate != null) {
                //接机是没有往返的
                Long time = applyDate.getTime() + duration2 * 1000 + Long.valueOf(officialCommitApply.getReturnWaitTime());
                journeyNodeInfo.setPlanSetoutTime(new Date(time));
                journeyNodeInfo.setPlanArriveTime(new Date(time + duration2 * 1000));
            }
            //保存数据
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
            journeyNodeBo.setNodeIdIsReturn(journeyNodeInfo.getNodeId());
        }
    }

    /**
     * 插入可用车型表数据
     * @param applyId
     * @param
     * @param canUseCarTypes
     */
    private void saveCanUseCarTypeInfo(Long applyId, List<UseCarTypeVO> canUseCarTypes) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            // 插入可用车型表
            if(!CollectionUtils.isEmpty(canUseCarTypes)){
                applyUseCarTypeMapper.insertApplyUseCarTypeBatch(canUseCarTypes,applyId,userId, DateUtils.getNowDate());
            }
        } catch (Exception e) {
            log.error("插入可用车型表数据失败，用户：{}车型参数:{}",userId,JSONArray.toJSON(canUseCarTypes).toString(),e);
        }
    }

    /**
     * 公务申请保存预估价格表信息
     * @param officialCommitApply
     * @param totalTime
     * @param regimeInfo
     * @param journeyId
     * @param
     * @param
     * @param userId
     * @throws Exception
     */
    private void saveEstimatePriceInfo(ApplyOfficialRequest officialCommitApply, int totalTime, RegimeInfo regimeInfo, Long journeyId, JourneyNodeBo journeyNodeBo, Long userId) throws Exception {
        Long nodeIdNoReturn = journeyNodeBo.getNodeIdNoReturn();
        Long nodeIdIsReturn = journeyNodeBo.getNodeIdIsReturn();
        String useCarModeOwnerLevel = regimeInfo.getUseCarModeOwnerLevel();
        String canUseCarMode = regimeInfo.getCanUseCarMode();
        String isGoBack = officialCommitApply.getIsGoBack();
        //包含自有车且不是包车
        if(canUseCarMode.contains(CarConstant.USR_CARD_MODE_HAVE) && !ServiceTypeConstant.CHARTERED.equals(officialCommitApply.getServiceType())){
            JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
            journeyPlanPriceInfo.setUseCarMode(CarConstant.USR_CARD_MODE_HAVE);
            journeyPlanPriceInfo.setSource("高德");
            journeyPlanPriceInfo.setCreateTime(DateUtils.getNowDate());
            journeyPlanPriceInfo.setCreateBy(String.valueOf(userId));
            journeyPlanPriceInfo.setNodeId(nodeIdNoReturn);
            journeyPlanPriceInfo.setJourneyId(journeyId);
            journeyPlanPriceInfo.setPrice(new BigDecimal(0));
            Date applyDate = officialCommitApply.getApplyDate();
            //如果是接机，时间单独判断
            setPlanTime(officialCommitApply, totalTime, journeyPlanPriceInfo, applyDate);
            journeyPlanPriceInfo.setDuration((int) TimeUnit.MINUTES.convert(totalTime, TimeUnit.SECONDS));
            journeyPlanPriceInfo.setPowerId(null);
            journeyPlanPriceInfo.setOrderId(null);
            if (StringUtils.isBlank(useCarModeOwnerLevel)) {
                throw new Exception("自有车无可用车型");
            }
            String[] splits = useCarModeOwnerLevel.split(",");
            for (String split: splits) {
                EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
                enterpriseCarTypeInfo.setLevel(split);
                List<EnterpriseCarTypeInfo> enterpriseCarTypeInfos = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
                if(enterpriseCarTypeInfos !=null && enterpriseCarTypeInfos.size()>0){
                    EnterpriseCarTypeInfo enterpriseCarTypeInfo1 = enterpriseCarTypeInfos.get(0);
                    journeyPlanPriceInfo.setCarTypeId(enterpriseCarTypeInfo1.getCarTypeId());
                }
                journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
                //如果有往返 需要追则追加存一条  接机没有往返，applyDate不为空
                saveReturnPlanPriceHave(officialCommitApply, nodeIdIsReturn, totalTime, isGoBack, journeyPlanPriceInfo);
            }
        }
        //包含网约车且不是包车
        if(canUseCarMode.contains(CarConstant.USR_CARD_MODE_NET) && !ServiceTypeConstant.CHARTERED.equals(officialCommitApply.getServiceType())){
            List<CarLevelAndPriceVO> carLevelAndPriceVOs = officialCommitApply.getCarLevelAndPriceVOs();
            //包车情况没有预估价
            if(!CollectionUtils.isEmpty(carLevelAndPriceVOs)){
                for (CarLevelAndPriceVO carLevelAndPriceVO : carLevelAndPriceVOs) {
                    Integer duration = carLevelAndPriceVO.getDuration();
                    Date applyDate = officialCommitApply.getApplyDate();
                    JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
                    journeyPlanPriceInfo.setUseCarMode(CarConstant.USR_CARD_MODE_NET);
                    //如果是接机，时间单独判断
                    setPlanTime(officialCommitApply, duration, applyDate, journeyPlanPriceInfo);
                    //根據车型查询车型id
                    String onlineCarLevel = carLevelAndPriceVO.getOnlineCarLevel();
                    Long carTypeId = enterpriseCarTypeInfoMapper.selectCarTypeId(onlineCarLevel);
                    journeyPlanPriceInfo.setJourneyId(journeyId);
                    journeyPlanPriceInfo.setNodeId(nodeIdNoReturn);
                    journeyPlanPriceInfo.setCarTypeId(carTypeId);
                    journeyPlanPriceInfo.setPowerId(null);
                    journeyPlanPriceInfo.setOrderId(null);
                    journeyPlanPriceInfo.setPrice(BigDecimal.valueOf(carLevelAndPriceVO.getEstimatePrice()));

                    journeyPlanPriceInfo.setDuration(duration);
                    journeyPlanPriceInfo.setSource(carLevelAndPriceVO.getSource());
                    journeyPlanPriceInfo.setCreateBy(String.valueOf(getLoginUserId()));
                    journeyPlanPriceInfo.setCreateTime(new Date());
                    journeyPlanPriceInfo.setUpdateBy(null);
                    journeyPlanPriceInfo.setUpdateTime(null);
                    journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
                    //如果有往返  需要追则追加存一条  applyDate不为空
                    saveReturnPlanPriceNet(officialCommitApply, nodeIdIsReturn, isGoBack, duration, journeyPlanPriceInfo);
                }
            }
        }
        //如果是包车
        if(ServiceTypeConstant.CHARTERED.equals(officialCommitApply.getServiceType())){
            Date applyDate = officialCommitApply.getApplyDate();
            //包车类型
            Date planArriveTime = null;
            String charterType = officialCommitApply.getCharterType();
            int duration = 0;
            if (CharterTypeEnum.HALF_DAY_TYPE.getKey().equals(charterType)) {
                //半日租 4小时
                planArriveTime = new Date(applyDate.getTime() + 4 * 60 * 60 * 1000);
                duration = 4 * 60;
            } else if (CharterTypeEnum.OVERALL_RENT_TYPE.getKey().equals(charterType)) {
                //整日组 8小时
                planArriveTime = new Date(applyDate.getTime() + 8 * 60 * 60 * 1000);
                duration = 8 * 60;
            }
            JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
            //根據车型查询车型id
            String canUseCarModes = regimeInfo.getCanUseCarMode();
            journeyPlanPriceInfo.setJourneyId(journeyId);
            journeyPlanPriceInfo.setNodeId(nodeIdNoReturn);
            journeyPlanPriceInfo.setUseCarMode(CarConstant.USR_CARD_MODE_HAVE);
            journeyPlanPriceInfo.setPowerId(null);
            journeyPlanPriceInfo.setOrderId(null);
            journeyPlanPriceInfo.setPrice(new BigDecimal(0));
            journeyPlanPriceInfo.setPlannedDepartureTime(applyDate);
            journeyPlanPriceInfo.setDuration(duration);
            journeyPlanPriceInfo.setPlannedArrivalTime(planArriveTime);
            journeyPlanPriceInfo.setSource("无");
            journeyPlanPriceInfo.setCreateBy(String.valueOf(getLoginUserId()));
            journeyPlanPriceInfo.setCreateTime(new Date());
            journeyPlanPriceInfo.setUpdateBy(null);
            journeyPlanPriceInfo.setUpdateTime(null);
            if(canUseCarMode.contains(CarConstant.USR_CARD_MODE_HAVE)) {
                String[] splits = useCarModeOwnerLevel.split(",");
                for (String split : splits) {
                    EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
                    enterpriseCarTypeInfo.setLevel(split);
                    List<EnterpriseCarTypeInfo> enterpriseCarTypeInfos = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
                    if (enterpriseCarTypeInfos != null && enterpriseCarTypeInfos.size() > 0) {
                        EnterpriseCarTypeInfo enterpriseCarTypeInfo1 = enterpriseCarTypeInfos.get(0);
                        journeyPlanPriceInfo.setCarTypeId(enterpriseCarTypeInfo1.getCarTypeId());
                        journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
                    }
                }
            }
        }
    }

    /**
     * 公务申请 （包含网约车） 增加返程预估价表信息
     * @param officialCommitApply
     * @param nodeIdIsReturn
     * @param isGoBack
     * @param duration
     * @param journeyPlanPriceInfo
     */
    private void saveReturnPlanPriceNet(ApplyOfficialRequest officialCommitApply, Long nodeIdIsReturn, String isGoBack, Integer duration, JourneyPlanPriceInfo journeyPlanPriceInfo) {
        if(CommonConstant.IS_RETURN.equals(isGoBack)){
            Long returnWaitTime = Long.valueOf(officialCommitApply.getReturnWaitTime());
            journeyPlanPriceInfo.setNodeId(nodeIdIsReturn);
            journeyPlanPriceInfo.setPlannedDepartureTime(new Date(duration*60*1000+officialCommitApply.getApplyDate().getTime()+returnWaitTime));
            journeyPlanPriceInfo.setPlannedArrivalTime(new Date(2*duration*60*1000+officialCommitApply.getApplyDate().getTime()+returnWaitTime));
            journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
        }
    }

    /**
     * 公务申请  包含自有车  增加返程预估价表
     * @param officialCommitApply
     * @param nodeIdIsReturn
     * @param totalTime
     * @param isGoBack
     * @param journeyPlanPriceInfo
     */
    private void saveReturnPlanPriceHave(ApplyOfficialRequest officialCommitApply, Long nodeIdIsReturn, int totalTime, String isGoBack, JourneyPlanPriceInfo journeyPlanPriceInfo) {
        if(CommonConstant.IS_RETURN.equals(isGoBack)){
            Long returnWaitTime = Long.valueOf(officialCommitApply.getReturnWaitTime());
            journeyPlanPriceInfo.setNodeId(nodeIdIsReturn);
            journeyPlanPriceInfo.setPlannedDepartureTime(new Date(totalTime*1000+officialCommitApply.getApplyDate().getTime()+returnWaitTime));
            journeyPlanPriceInfo.setPlannedArrivalTime(new Date(2*totalTime*1000+officialCommitApply.getApplyDate().getTime()+returnWaitTime));
            journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
        }
    }

    /**
     * 公务申请-预估价格表设置起止时间
     * @param officialCommitApply
     * @param duration
     * @param applyDate
     * @param journeyPlanPriceInfo
     */
    private void setPlanTime(ApplyOfficialRequest officialCommitApply, Integer duration, Date applyDate, JourneyPlanPriceInfo journeyPlanPriceInfo) {
        if (applyDate != null){
            journeyPlanPriceInfo.setPlannedDepartureTime(applyDate);
            journeyPlanPriceInfo.setPlannedArrivalTime(new Date(duration*60*1000+applyDate.getTime()));
        }else {
            Long flightPlanArriveTime = officialCommitApply.getFlightPlanArriveTime().getTime();
            Date useCarTime = getUseCarTimeForFlight(officialCommitApply, flightPlanArriveTime);
            journeyPlanPriceInfo.setPlannedDepartureTime(useCarTime);
            journeyPlanPriceInfo.setPlannedArrivalTime(new Date(useCarTime.getTime() + duration*60*1000));
        }
    }

    /**
     * 公务申请 有途经点时设置最后一个节点的起止时间（有结束时间，无开始时间）
     * @param officialCommitApply
     * @param journeyNodeInfo
     * @param duration2
     * @param applyDate
     */
    private void setArriveTime(ApplyOfficialRequest officialCommitApply, JourneyNodeInfo journeyNodeInfo, Integer duration2, Date applyDate) {
        if (applyDate != null){
            journeyNodeInfo.setPlanSetoutTime(null);
            journeyNodeInfo.setPlanArriveTime(new Date(applyDate.getTime() + duration2*1000));
        }else {
            //如果applyDate为空，则表示接机
            Long flightPlanArriveTime = officialCommitApply.getFlightPlanArriveTime().getTime();
            Date useCarTime = getUseCarTimeForFlight(officialCommitApply, flightPlanArriveTime);
            journeyNodeInfo.setPlanSetoutTime(null);
            journeyNodeInfo.setPlanArriveTime(new Date(useCarTime.getTime() + duration2*1000));
        }
    }

    /**
     * 公务申请 有途经点情况下 设置第一个节点的起止时间
     * @param officialCommitApply
     * @param journeyNodeInfo
     * @param applyDate
     */
    private void setUseCarTime(ApplyOfficialRequest officialCommitApply, JourneyNodeInfo journeyNodeInfo, Date applyDate) {
        if (applyDate != null){
            journeyNodeInfo.setPlanSetoutTime(applyDate);
            journeyNodeInfo.setPlanArriveTime(null);
        }else {
            //如果applyDate为空，则表示接机
            Long flightPlanArriveTime = officialCommitApply.getFlightPlanArriveTime().getTime();
            Date useCarTime = getUseCarTimeForFlight(officialCommitApply, flightPlanArriveTime);
            journeyNodeInfo.setPlanSetoutTime(useCarTime);
            journeyNodeInfo.setPlanArriveTime(null);
        }
    }

    /**
     * 公务申请 保存中间行程节点
     * @param journeyId
     * @param applyOfficialRequest
     * @param passedAddressList
     * @param size
     * @param n
     */
    private void saveMiddleJourneyNode(Long journeyId, ApplyOfficialRequest applyOfficialRequest, List<AddressVO> passedAddressList, int size, int n) {
        JourneyNodeInfo journeyNodeInfo;
        for (int i = 0; i < size - 1; i++) {
            journeyNodeInfo = new JourneyNodeInfo();
            //C...提交第 n 个节点， 第 n 个节点的起点是 第 n-1个途径地，目的地是 第 n 个途径地
            AddressVO setoutAddress = passedAddressList.get(i);
            applyOfficialRequest.setStartAddr(setoutAddress);
            AddressVO endAddr = passedAddressList.get(i+1);
            applyOfficialRequest.setEndAddr(endAddr);
            //设置行程节点信息表
            journeyNodeOfficialCommit(applyOfficialRequest, journeyId, journeyNodeInfo);
            //判断是否途经点 Y000 是
            journeyNodeInfo.setItIsViaPoint(CommonConstant.PASS);
            //设置节点编号
            journeyNodeInfo.setNumber(n+i+1);
            //保存数据
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
        }
    }

    /**
     * 公务申请 预估价格表设置计划出发时间到计划到达时间
     * @param officialCommitApply
     * @param totalTime
     * @param journeyPlanPriceInfo
     * @param applyDate
     */
    private void setPlanTime(ApplyOfficialRequest officialCommitApply, int totalTime, JourneyPlanPriceInfo journeyPlanPriceInfo, Date applyDate) {
        if (applyDate != null){
            journeyPlanPriceInfo.setPlannedDepartureTime(applyDate);
            journeyPlanPriceInfo.setPlannedArrivalTime(new Date(totalTime*1000+applyDate.getTime()));
        }else {
            Long flightPlanArriveTime = officialCommitApply.getFlightPlanArriveTime().getTime();
            Date useCarTime = getUseCarTimeForFlight(officialCommitApply, flightPlanArriveTime);
            journeyPlanPriceInfo.setPlannedDepartureTime(useCarTime);
            journeyPlanPriceInfo.setPlannedArrivalTime(new Date(useCarTime.getTime() + totalTime*1000));
        }
    }

    /**
     * 公务和差旅 初始化审批流
     * @param applyId
     * @param userId
     * @param regimenId
     */
    private void initOfficialApproveFlow(Long applyId, Long userId, Integer regimenId) {
        executor.submit(()-> {
            try {
                //初始化审批流
                applyApproveResultInfoService.initApproveResultInfo(applyId, Long.valueOf(regimenId), userId);
            } catch (Exception e) {
                log.error("差旅申请，初始化审批流失败，用户：{}，用车制度：{}",userId,regimenId,e);
            }
        });
    }

    /**
     * 公务申请 给审批人发短信
     * @param officialCommitApply
     * @param applyId
     * @param userId
     *
     */
    private void sendMessageToApproverForOfficial(ApplyOfficialRequest officialCommitApply, Long applyId, Long userId) {
        List<ApprovalVO> approvers = officialCommitApply.getApprovers();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //申请人名字
                    String userName = officialCommitApply.getApplyUser().getUserName();
                    //申请用车时间
                    Date applyDate = officialCommitApply.getApplyDate();
                    if(applyDate == null){
                        // 航班到达时间
                        long flightPlanArriveTime = officialCommitApply.getFlightPlanArriveTime().getTime();
                        applyDate = getUseCarTimeForFlight(officialCommitApply, flightPlanArriveTime);
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
                    String useCarTime = simpleDateFormat.format(applyDate);
                    //短信 员工安宁已提交“2019年08月14日13:00”的公务用车申请，请登录红旗公务APP及时处理。（公务申请）
                    //(1) 员工姓名 （2）日期 时
                    for (ApprovalVO approver : approvers) {
                        //给审批人发短信
                        sendMsgToApprover(SmsTemplateConstant.OFFICIAL_APPLY_APPROVER,userName,useCarTime,approver);
                    }
                } catch (Exception e) {
                    log.error("公务申请：给审批人发短信失败：applyId:{},userId:{}",applyId,userId,e);
                }
            }
        });
    }

    /**
     * 公务申请 不需要审批情况下初始化权限和初始化订单
     * @param journeyId
     * @param
     * @param applyId
     * @param userId
     */
    private List<Long> initPowerAndOrder(Long journeyId, String applyType, Long applyId, Long userId,ApplyOfficialRequest officialCommitApply) {
        try {
            ArrayList<Long> orderIds = new ArrayList<>();
            //1. 公务申请 初始化用车权限
            boolean optFlag = journeyUserCarPowerService.createUseCarAuthority(applyId, userId);
            if(!optFlag){
                log.error("生成用车权限失败");
            }
            ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
            //2.初始化订单
            List<CarAuthorityInfo> carAuthorityInfos = journeyUserCarPowerService.queryOfficialOrderNeedPower(journeyId);
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(carAuthorityInfos)){
                int flag=carAuthorityInfos.get(0).getDispatchOrder()?ONE:ZERO;
                if("F001".equals(officialCommitApply.getSmsDifference())){
                    //佛山用户申请短信
                    ismsBusiness.sendVehicleUserApply(journeyId,applyId,officialCommitApply);
                }else {
                    ecmpMessageService.applyUserPassMessage(applyId, userId, userId, null, carAuthorityInfos.get(0).getTicketId(), flag);
                }
                for (CarAuthorityInfo carAuthorityInfo:carAuthorityInfos){
                    int isDispatch=carAuthorityInfo.getDispatchOrder()?ONE:TWO;
                    //OfficialOrderReVo officialOrderReVo = new OfficialOrderReVo(carAuthorityInfo.getTicketId(),isDispatch, CarLeaveEnum.getAll());
                    OfficialOrderReVo officialOrderReVo = OfficialOrderReVo.builder().powerId(carAuthorityInfo.getTicketId())
                            .isDispatch(isDispatch).carLevel(CarLeaveEnum.getAll()).companyId(officialCommitApply.getCompanyId()).build();
                    Long orderId=null;
                    if (ApplyTypeEnum.APPLY_BUSINESS_TYPE.getKey().equals(applyType)){
                        orderId = orderInfoService.officialOrder(officialOrderReVo, userId);
                        if(ItIsSupplementEnum.ORDER_DIRECT_SCHEDULING_STATUS.getValue().equals(officialCommitApply.getDistinguish())){
                            OrderInfo orderInfo = new OrderInfo();
                            orderInfo.setItIsSupplement(ItIsSupplementEnum.ORDER_DIRECT_SCHEDULING_STATUS.getValue());
                            orderInfo.setUseCarMode(officialCommitApply.getUseType());
                            orderInfo.setOrderId(orderId);
                            orderInfoService.updateOrderInfo(orderInfo);
                        }
                        orderIds.add(orderId);
                    }
                    if("F001".equals(officialCommitApply.getSmsDifference())){
                        //佛山调度员申请短信
                        ismsBusiness.sendDispatcherApply(journeyId,applyId,officialCommitApply);
                    }else {
                        ecmpMessageService.saveApplyMessagePass(applyInfo,userId,orderId,carAuthorityInfos.get(0).getTicketId(),isDispatch);
                    }
                }
            }
            return orderIds;
        } catch (Exception e) {
            log.error("公务申请，初始化权限和订单失败，用户：{}，请求参数：{}",userId,JSONArray.toJSON(officialCommitApply).toString(),e);
            return null;
        }
    }

    private Long getLoginUserId() {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        return loginUser.getUser().getUserId();
    }

    /**
     * 给自己发申请通知
     * @param userId 登录用户id
     * @param applyId 申请id
     */
    private void sendApplyNoticeToSelf(Long userId, Long applyId) {
        EcmpMessage ecmpMessage1 = EcmpMessage.builder().configType(MsgUserConstant.MESSAGE_USER_USER.getType())
                .ecmpId(userId).categoryId(applyId).type(MsgTypeConstant.MESSAGE_TYPE_T001.getType())
                .status(MsgStatusConstant.MESSAGE_STATUS_T002.getType())
                .category(MsgConstant.MESSAGE_T001.getType()).content("你有一条申请待审批").url("")
                .createBy(userId).createTime(new Date()).updateBy(null).updateTime(null).build();
        int j = ecmpMessageMapper.insert(ecmpMessage1);
        if(j != 1){
            throw new RuntimeException("发送通知失败");
        }
    }

    /**
     * 给审批人发送通知
     * @param applyId  申请ID
     * @param userId   登录人id
     * @param approver  审批人（id,名字，电话）
     */
    private void sendNoticeToApprover(Long applyId, Long userId, ApprovalVO approver) {
        EcmpMessage ecmpMessage;
        ecmpMessage = EcmpMessage.builder()
                //对应用户类型（1.乘客，2.司机，3.调度员。4，审批员）
                .configType(MsgUserConstant.MESSAGE_USER_APPROVAL.getType())
                //对应用户类型id  configType为 2 此处为 driverId ，否则此处为userId
                .ecmpId(Long.valueOf(approver.getUserId()))
                // 类别id 申请则为申请id 订单类则为 订单id
                .categoryId(applyId)
                //消息类型 T001-业务消息 T002- T003  T004
                .type(MsgTypeConstant.MESSAGE_TYPE_T001.getType())
                //消息状态 0000-未读 1111-已读
                .status(MsgStatusConstant.MESSAGE_STATUS_T002.getType())
                //消息类别，随业务自行添加 M001  申请通知 M002  审批通知 M003  调度通知 M004  订单改派 M005  订单取消 M999  其他
                .category(MsgConstant.MESSAGE_T002.getType())
                .content("你有一条待审批通知")
                //事项处理跳转链接地址， 需要密切联系业务调整规则 大部分应该是跳转到  事项处理的列表页 单个具体事项出题 请带上 业务的主键ID，
                .url("")
                .createBy(userId)
                .createTime(new Date())
                .updateBy(null)
                .updateTime(null)
                .build();
        int i = ecmpMessageMapper.insert(ecmpMessage);
        if(i != 1){
            throw new RuntimeException("发送通知失败");
        }
    }

    /**
     *  公务申请给审批人发短信
     * @param template
     * @param userName
     * @param useCarTime
     * @param approver
     */
    private void sendMsgToApprover(String template,String userName, String useCarTime,ApprovalVO approver) {
        String userPhone = approver.getApprovalPhone();
        HashMap<String, String> map = Maps.newHashMap();
        //用车时间
        map.put("useCarTime",useCarTime);
        //申请人名字
        map.put("userName",userName);
        try {
            iSmsTemplateInfoService.sendSms(template,map,userPhone);
        } catch (Exception e) {
            log.error("业务处理异常", e);
        }
    }


    /**
     * 提交公务行程乘客/同行人信息表
     * @param officialCommitApply
     * @param journeyId
     * @param journeyPassengerInfo
     */
    private void journeyPassergerOfficialCommit(ApplyOfficialRequest officialCommitApply, Long journeyId, JourneyPassengerInfo journeyPassengerInfo) {
        //保存乘车人
        //4.1 journey_id 非空
        journeyPassengerInfo.setJourneyId(journeyId);
        //4.2 name 姓名
        journeyPassengerInfo.setName(officialCommitApply.getPassenger().getUserName());
        //4.3 mobile
        journeyPassengerInfo.setMobile(officialCommitApply.getPassenger().getUserPhone());
        //4.4 it_is_peer 是否是同行者 00   是   01   否
        journeyPassengerInfo.setItIsPeer(CommonConstant.IS_NOT_PEER);
        //4.5 create_by 创建者
        journeyPassengerInfo.setCreateBy(String.valueOf(getLoginUserId()));
        //4.6 create_time 创建时间
        journeyPassengerInfo.setCreateTime(new Date());
        //4.7 update_by 更新者
        journeyPassengerInfo.setUpdateBy(null);
        //4.8 update_time 更新时间
        journeyPassengerInfo.setUpdateTime(null);
        //4.9 新增  同行人数
        journeyPassengerInfo.setPeerNumber(officialCommitApply.getPeerNumber());
        journeyPassengerInfoMapper.insertJourneyPassengerInfo(journeyPassengerInfo);
        //保存同行人
        //同行人
        JourneyPassengerInfo journeyPartner = null;
        List<UserVO> partners= officialCommitApply.getPartner();
        if (!CollectionUtils.isEmpty(partners)){
            for (UserVO partner : partners) {
                journeyPartner = new JourneyPassengerInfo();
                journeyPartner.setJourneyId(journeyId);
                journeyPartner.setName(partner.getUserName());
                journeyPartner.setMobile(partner.getUserPhone());
                journeyPartner.setItIsPeer(CommonConstant.IS_PEER);
                journeyPartner.setCreateBy(String.valueOf(getLoginUserId()));
                journeyPartner.setCreateTime(new Date());
                journeyPartner.setUpdateBy(null);
                journeyPartner.setUpdateTime(null);
                journeyPassengerInfoMapper.insertJourneyPassengerInfo(journeyPartner);
            }
        }
    }

    /**
     * 设置公务行程节点信息表
     * @param officialCommitApply
     * @param journeyId
     * @param journeyNodeInfo
     */
    private void journeyNodeOfficialCommit(ApplyOfficialRequest officialCommitApply, Long journeyId, JourneyNodeInfo journeyNodeInfo) {
        //3.1 journey_id 非空
        journeyNodeInfo.setJourneyId(journeyId);
        //3.2 user_id 行程申请人 编号
        journeyNodeInfo.setUserId(Long.valueOf(officialCommitApply.getApplyUser().getUserId())); //TODO 判空
        //3.3 plan_begin_address 计划上车地址  非空
        journeyNodeInfo.setPlanBeginAddress(officialCommitApply.getStartAddr().getAddress());
        journeyNodeInfo.setPlanBeginLongAddress(officialCommitApply.getStartAddr().getLongAddress());
        journeyNodeInfo.setPlanBeginCityCode(officialCommitApply.getStartAddr().getCityCode());
        //3.4 plan_end_address 计划下车地址    非空
        journeyNodeInfo.setPlanEndAddress(officialCommitApply.getEndAddr().getAddress());
        journeyNodeInfo.setPlanEndLongAddress(officialCommitApply.getEndAddr().getLongAddress());
        journeyNodeInfo.setPlanEndCityCode(officialCommitApply.getEndAddr().getCityCode());
        //3.5 plan_setout_time 计划出发时间
        journeyNodeInfo.setPlanSetoutTime(officialCommitApply.getApplyDate());
        //3.6 plan_arrive_time 计划到达时间  跟差旅申请记录时间不一样
        journeyNodeInfo.setPlanArriveTime(null);
        //3.7 plan_begin_longitude 出发坐标
        journeyNodeInfo.setPlanBeginLongitude(officialCommitApply.getStartAddr().getLongitude());
        //3.8 plan_begin_latitude
        journeyNodeInfo.setPlanBeginLatitude(officialCommitApply.getStartAddr().getLatitude());
        //3.9 plan_end_longitude
        journeyNodeInfo.setPlanEndLongitude(officialCommitApply.getEndAddr().getLongitude());
        //3.10 plan_end_latitude
        journeyNodeInfo.setPlanEndLatitude(officialCommitApply.getEndAddr().getLatitude());
        //3.11 it_is_via_point 是否是途经点  途经点仅仅用于 地图描点  和  导航使用;途经点  同样具有顺序   这个途径点提出来单独判断
        journeyNodeInfo.setItIsViaPoint(null);
        //3.12 vehicle 交通工具 T001  飞机 T101  火车 T201  汽车 T301  轮渡 T999  其他   差旅申请才有
        journeyNodeInfo.setVehicle(null);
        //3.13 duration 行程节点 预估用时， X 小时 X 分钟   页面没有数据
        journeyNodeInfo.setDuration(null);
        //3.14 distance 行程节点 预估里程 单位公：里
        journeyNodeInfo.setDistance(null);
        //3.15 wait_duration 航班到达后等待多时时间  用车，单位 分钟 M010 10分钟 M020 20分钟 M030 30分钟 H100 一小时 H130 一个半小时
        journeyNodeInfo.setWaitDuration(officialCommitApply.getWaitDurition());
        //3.16 node_state 行程节点状态 订单---->用车权限----->行程节点   反推  节点任务是否已完成 失效 也算已经  节点任务已经完成
        //P000   有效中  P444   已失效
        journeyNodeInfo.setNodeState(CommonConstant.VALID_NODE);
        ///3.17 number 节点在 在整个行程 中的顺序编号 从  1  开始   节点编号单独判断
        journeyNodeInfo.setNumber(null);
        //3.18 create_by 创建者
        journeyNodeInfo.setCreateBy(String.valueOf(getLoginUserId()));
        //3.19 create_time 创建时间
        journeyNodeInfo.setCreateTime(new Date());
        //3.20 update_by 更新者
        journeyNodeInfo.setUpdateBy(null);
        //3.21 update_time 更新时间
        journeyNodeInfo.setUpdateTime(null);

    }

    /**
     * 提交公务申请表信息
     * @param officialCommitApply
     * @param journeyId
     * @param applyInfo
     */
    private void applyInfoOfficialCommit(ApplyOfficialRequest officialCommitApply, Long journeyId, ApplyInfo applyInfo) {
        //2.1 journey_id 非空
        applyInfo.setJourneyId(journeyId);
        //2.2 project_id
        Long projectId=StringUtils.isBlank(officialCommitApply.getProjectNumber())?null:Long.valueOf(officialCommitApply.getProjectNumber());
        applyInfo.setProjectId(projectId);
        applyInfo.setCompanyId(officialCommitApply.getCompanyId());
        //2.3 regimen_id 非空
        applyInfo.setRegimenId(Long.valueOf(officialCommitApply.getRegimenId()));
        //2.4 apply_type 用车申请类型；A001:  公务用车 A002:  差旅用车
        applyInfo.setApplyType(String.valueOf(officialCommitApply.getApplyType()));
        //2.5 approver_name 第一审批阶段 审批人列表，前两位
        List<ApprovalVO> approvers = officialCommitApply.getApprovers();
        if(approvers == null){
            applyInfo.setApproverName(null);
        }else {
            String approversName = null;
            if(approvers.size() == 1){
                approversName = approvers.get(0).getApprovalName();
            }else if (approvers.size()>1){
                for (int i = 0; i < 2; i++) {
                    approversName = approvers.get(0).getApprovalName() +"、"+ approvers.get(1).getApprovalName();
                }
            }
            applyInfo.setApproverName(approversName);
        }
        Long costCenter=StringUtils.isBlank(officialCommitApply.getCostCenter())?null:Long.valueOf(officialCommitApply.getCostCenter());
        //2.6 cost_center 成本中心 从组织机构表 中获取
        applyInfo.setCostCenter(costCenter);
        //2.7 state 申请审批状态 S001  申请中 S002  通过 S003  驳回 S004  已撤销
        if(ItIsSupplementEnum.ORDER_DIRECT_SCHEDULING_STATUS.getValue().equals(officialCommitApply.getDistinguish())){
            //如果是直接调度，走直接调度流程
            applyInfo.setState(ApplyStateConstant.APPLY_PASS);
        }else {
            applyInfo.setState(ApplyStateConstant.ON_APPLYING);
        }
        //2.8 reason 行程原因
        applyInfo.setReason(officialCommitApply.getReason());
        //2.9 create_by 创建者
        applyInfo.setCreateBy(String.valueOf(getLoginUserId()));
        //2.10 create_time 创建时间
        applyInfo.setCreateTime(new Date());
        //2.11 update_by 更新者
        applyInfo.setUpdateBy(null);
        //2.12 update_time 更新时间
        applyInfo.setUpdateTime(null);
        //2.13 申请单编号
        applyInfo.setApplyNumber(RandomUtil.getRandomNumber());

        applyInfoMapper.insertApplyInfo(applyInfo);
    }

    /**
     * 提交公务行程表信息
     * @param officialCommitApply
     * @param journeyInfo
     * @param driveTime 单程预计行驶时间(秒)
     */
    private void journeyOfficialCommit(ApplyOfficialRequest officialCommitApply, JourneyInfo journeyInfo,Integer driveTime) {
        //1.1 userId          非空
        journeyInfo.setUserId(Long.valueOf(officialCommitApply.getApplyUser().getUserId()));
        //1.2 用车制度id        非空
        journeyInfo.setRegimenId(Long.valueOf(officialCommitApply.getRegimenId()));
        //1.3 service_type 预约、接机、送机、包车 1000预约 2001接机 2002送机 3000包车
        journeyInfo.setServiceType(officialCommitApply.getServiceType());
        //1.4 use_car_mode 用车方式（自有、网约车）
        journeyInfo.setUseCarMode(officialCommitApply.getUseType());
        //增加 companyId
        journeyInfo.setCompanyId(officialCommitApply.getCompanyId());

        //1.5 use_car_time 用车时间
        Date applyDate = officialCommitApply.getApplyDate();
        //1.6 it_is_return 是否往返 Y000 N444
        String isGoBack = officialCommitApply.getIsGoBack();
        // 行程开始时间，用车时间  预约(2000)、送机(4000) 有往返  两种情况一起判断   接机(3000)、包车(5000) 没往返 这两种分别单独判断
        //包车类型
        String charterType = officialCommitApply.getCharterType();
        String serviceType = officialCommitApply.getServiceType();
        //往返等待时间 单位毫秒
        String returnWaitTime = officialCommitApply.getReturnWaitTime();
        if (applyDate != null){
            // 预约（2000） 送机（4000） 包车（5000） 三种情况下 用车时间都为applyDate
            journeyInfo.setUseCarTime(applyDate);
            journeyInfo.setStartDate(applyDate);
            // 预计行程结束时间
            // (5000)如果是包车情况 包车没有往返
            if("5000".equals(serviceType)){
                if (CharterTypeEnum.HALF_DAY_TYPE.getKey().equals(charterType)) {
                    //半日租 4小时
                    Date endTime = new Date(applyDate.getTime() + 4 * 60 * 60 * 1000);
                    journeyInfo.setEndDate(endTime);
                } else if (CharterTypeEnum.OVERALL_RENT_TYPE.getKey().equals(charterType)) {
                    //整日组 8小时
                    Date endTime = new Date(applyDate.getTime() + 8 * 60 * 60 * 1000);
                    journeyInfo.setEndDate(endTime);
                }
            }else {
                // 预约(2000)、送机(4000)情况 有往返
                //如果没有往返
                if("N444".equals(isGoBack)){
                    journeyInfo.setEndDate(new Date(applyDate.getTime() + driveTime*1000));
                }else {
                    //如果有往返 则要算上 往返等待时间 和 返程行驶时间
                    if(returnWaitTime != null){
                        journeyInfo.setEndDate(new Date(applyDate.getTime() + 2*driveTime*1000 + Long.valueOf(returnWaitTime)));
                    }
                }

            }
        }else {
            //如果是接机（3000）  接机没有往返
            Long flightPlanArriveTime = officialCommitApply.getFlightPlanArriveTime().getTime();
            Date useCarTime = getUseCarTimeForFlight(officialCommitApply, flightPlanArriveTime);
            // 用车时间
            journeyInfo.setUseCarTime(useCarTime);
            // 行程开始时间
            journeyInfo.setStartDate(useCarTime);
            // 预计行程结束时间
            journeyInfo.setEndDate(new Date(useCarTime.getTime()+driveTime));
        }
        //  有往返的话，创建两个行程
        journeyInfo.setItIsReturn(isGoBack == null ? JourneyConstant.IT_IS_NOT_RETURN : isGoBack);
        //1.7 estimate_price 预估价格     非空
        journeyInfo.setEstimatePrice(officialCommitApply.getEstimatePrice());
        //1.8 project_id  项目id
        Long projectId=StringUtils.isBlank(officialCommitApply.getProjectNumber())?null:Long.valueOf(officialCommitApply.getProjectNumber());
        journeyInfo.setProjectId(projectId);
        //1.9 flight_number 航班编号
        journeyInfo.setFlightNumber(officialCommitApply.getFlightNumber());
        //1.10 use_time 行程总时长  多少天 该字段未使用（差旅的）
        journeyInfo.setUseTime(null);
        //1.11 wait_time_long 预计等待时间，出发地 第一个节点 等待时长   往返，返回等待时长(单位毫秒)
        journeyInfo.setWaitTimeLong(officialCommitApply.getReturnWaitTime());
        //1.12 charter_car_type 包车类型：T000  非包车 T001 半日租（4小时）T002 整日租（8小时）
        journeyInfo.setCharterCarType(officialCommitApply.getCharterType());
        //1.13 create_by 创建者
        journeyInfo.setCreateBy(String.valueOf(getLoginUserId()));
        //1.14 create_time 创建时间
        journeyInfo.setCreateTime(new Date());
        //1.15 update_by 更新者
        journeyInfo.setUpdateBy(null);
        //1.16 update_time 更新时间
        journeyInfo.setUpdateBy(null);
        //1.17 预计航班起飞时间
        journeyInfo.setFlightPlanTakeOffTime(officialCommitApply.getFlightPlanTakeOffTime());
        // 新增 出差需接送机城市为空
        journeyInfo.setTravelPickupCity(null);
        // 新增 出差市内用车城市为空
        journeyInfo.setTravelCitiesStr(null);
        //  新增 出差接送机总次数为空
        journeyInfo.setPickupTimes(null);
        //  新增 公务title为reason
        journeyInfo.setTitle(StringUtils.isBlank(officialCommitApply.getReason()) ? "公务用车申请" : officialCommitApply.getReason());
        journeyInfoMapper.insertJourneyInfo(journeyInfo);
    }

    /**
     * 接机预计用车时间
     * @param officialCommitApply   等待时间
     * @param flightPlanArriveTime   航班到达时间
     * @return
     */
    private Date getUseCarTimeForFlight(ApplyOfficialRequest officialCommitApply, Long flightPlanArriveTime) {
        Date useCarTime = null;
        switch (officialCommitApply.getWaitDurition()){
            case WaitTimeConstant.WAIT_TEN_MINUTE:
                useCarTime = new Date(10*60*1000 + flightPlanArriveTime);
                break;
            case WaitTimeConstant.WAIT_TWENTY_MINUTE:
                useCarTime = new Date(20*60*1000 + flightPlanArriveTime);
                break;
            case WaitTimeConstant.WAIT_THIRTY_MINUTE:
                useCarTime = new Date(30*60*1000 + flightPlanArriveTime);
                break;
            case WaitTimeConstant.WAIT_ONE_HOUR:
                useCarTime = new Date(60*60*1000 + flightPlanArriveTime);
                break;
            case WaitTimeConstant.WAIT_ONE_AND_HALF_HOUR:
                useCarTime = new Date(90*60*1000 + flightPlanArriveTime);
                break;
            default:
                throw new BaseException("接机航班等待时长有误");
        }
        return useCarTime;
    }

    @Override
    public List<ApprovalListVO> getApproveList(String applyUser,String applyMobile,Long applyId,Date time){
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        //根据制度id查询是否需要审批
        RegimeVo regimeInfo = regimeInfoMapper.queryRegimeDetail(applyInfo.getRegimenId());
        if (regimeInfo!=null&&CommonConstant.NO_PASS.equals(regimeInfo.getNeedApprovalProcess())){
            log.info("申请单"+applyId+"制度id"+applyInfo.getRegimenId()+"无需审批");
            return null;
        }
        List<ApprovalListVO> result=new ArrayList<>();
        List<ApplyApproveResultInfo> applyApproveResultInfos = resultInfoMapper.selectApplyApproveResultInfoList(new ApplyApproveResultInfo(applyId));
        List<ApprovalInfoVO> list=new ArrayList<>();
        //TODO 后期优化
        list.add(new ApprovalInfoVO(0L,applyUser,applyMobile,"发起申请","申请成功"));
        result.add(new ApprovalListVO(applyInfo.getApplyNumber(),"申请人",list, DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN_3,time)));
        if (CollectionUtils.isEmpty(applyApproveResultInfos)){
            return result;
        }
        SortListUtil.sort(applyApproveResultInfos,"approveNodeId",SortListUtil.ASC);
        boolean ISHASEXPIREDSTATE=false;
        for (ApplyApproveResultInfo resultInfo:applyApproveResultInfos){
            String approveTime=null;
            String approveUserId = resultInfo.getApproveUserId();
            String appresult = resultInfo.getApproveResult();
            String state = resultInfo.getState();
            if (ApproveStateEnum.COMPLETE_APPROVE_STATE.getKey().equals(resultInfo.getState())){
                if (resultInfo.getUpdateTime()!=null){
                    approveTime=DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN_3,resultInfo.getUpdateTime());
                }
                if (StringUtils.isNotBlank(resultInfo.getUpdateBy())){
                    approveUserId=resultInfo.getUpdateBy();
                }
            }
            //撤销
            if (ApproveStateEnum.CANCEL_APPROVE_STATE.getKey().equals(resultInfo.getState())){
                break;
            }
            if (ApproveStateEnum.EXPIRED_APPROVE_STATE.getKey().equals(resultInfo.getState())){
                if (ISHASEXPIREDSTATE){
                    break;
                }else{
                    ISHASEXPIREDSTATE=true;
                }
            }
            list=new ArrayList<>();
            if (StringUtils.isNotBlank(approveUserId)){
                List<EcmpUser> userList=ecmpUserMapper.selectUserListByUserIds(approveUserId);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userList)) {
                    for (EcmpUser user:userList){
                        ApprovalInfoVO approvalInfoVO = new ApprovalInfoVO(resultInfo.getApproveNodeId(), user.getNickName(), user.getPhonenumber(), ApproveStateEnum.format(appresult), ApproveStateEnum.format(state));
                        approvalInfoVO.setContent(resultInfo.getContent());
                        approvalInfoVO.setUserId(user.getUserId());
                        list.add(approvalInfoVO);
                    }
                }
            }
            result.add(new ApprovalListVO(applyInfo.getApplyNumber(),"审批人",list, approveTime));
        }
        if (ApplyStateConstant.CANCEL_APPLY.equals(applyInfo.getState())){
            list=new ArrayList<>();
            String undoTime=DateFormatUtils.formatDate(DateFormatUtils.DATE_TIME_FORMAT_CN_3,applyInfo.getUpdateTime());
            list.add(new ApprovalInfoVO(Long.MAX_VALUE,applyUser,applyMobile,"撤销申请",ApproveStateEnum.CANCEL_APPROVE_STATE.getDesc()));
            result.add(new ApprovalListVO(applyInfo.getApplyNumber(),"撤销人",list, undoTime));
        }
//        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(result)&&result.size()>1){
//            Collections.sort(result, new Comparator<ApprovalListVO>() {
//                @Override
//                public int compare(ApprovalListVO o1, ApprovalListVO o2) {
//                    int i = o1.getList().get(0).getApprovalNodeId().intValue()- o2.getList().get(0).getApprovalNodeId().intValue();
//                    if(i == 0){
//                        return o1.getList().get(0).getApprovalNodeId().intValue() - o2.getList().get(0).getApprovalNodeId().intValue();
//                    }
//                    return i;
//                }
//            });
//        }
        return result;
    }

    @Override
    @Transactional
    public int updateApplyState(Long applyId,String applyState,String approveState,Long userId)throws Exception {
        ApplyInfo applyInfo = new ApplyInfo(applyId,applyState);
        applyInfo.setUpdateBy(String.valueOf(userId));
        applyInfo.setUpdateTime(new Date());
        int i=0;
        if (StringUtils.isNotBlank(applyState)){
            i = applyInfoMapper.updateApplyInfo(applyInfo);
        }
        this.updateApproveResult(applyId,approveState,userId);
        return i;
    }

    @Override
    public void updateApproveResult(Long applyId,String state,Long userId) throws Exception{
        List<ApplyApproveResultInfo> resultInfos = resultInfoMapper.selectApplyApproveResultInfoList(new ApplyApproveResultInfo(applyId));
        List<ApplyApproveResultInfo> collect = resultInfos.stream().filter(p -> ApproveStateEnum.NOT_ARRIVED_STATE.getKey().equals(p.getState()) || ApproveStateEnum.WAIT_APPROVE_STATE.getKey().equals(p.getState())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collect)){
            for (ApplyApproveResultInfo approveResultInfo:collect){
                approveResultInfo.setUpdateTime(new Date());
                approveResultInfo.setUpdateBy(String.valueOf(userId));
                approveResultInfo.setState(state);
                resultInfoMapper.updateApplyApproveResultInfo(approveResultInfo);
            }
        }
    }

    @Override
    public void checkApplyExpired() {
        try {
            List<ApplyInfo> applyInfos=applyInfoMapper.checkApplyExpiredList(ApplyStateConstant.ON_APPLYING);
            StringBuilder applyIds=new StringBuilder();
            if (!CollectionUtils.isEmpty(applyInfos)){
                for (ApplyInfo applyInfo:applyInfos){
                    this.updateApplyState(applyInfo.getApplyId(),ApplyStateConstant.EXPIRED_APPLY,ApproveStateEnum.EXPIRED_APPROVE_STATE.getKey(),1L);
                    applyIds.append(applyInfo.getApplyId());
                }
            }
            log.info(DateUtils.getMonthAndToday()+"申请单已过期的单号:"+applyIds);
        } catch (Exception e) {
            log.error("业务处理异常", e);
            log.info("定时任务:checkApplyExpired 异常");
        }
    }

    /**
     *
     * @param applyId
     * @param applyState
     * @param approveState
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public ApiResponse updateApplyOrderState(Long applyId, String applyState, String approveState, Long userId)throws Exception {
        ApiResponse  apiResponse = new ApiResponse();
        ApplyInfo applyInfo = new ApplyInfo(applyId,applyState);
        applyInfo.setUpdateBy(String.valueOf(userId));
        applyInfo.setUpdateTime(new Date());
        UndoSMSTemplate undoSMSTemplate = applyInfoMapper.queryApplyUndoList(applyId);
        //任何状态下的 订单都可以撤销
//        if(Integer.valueOf(undoSMSTemplate.getState().substring(1))>=Integer.valueOf(OrderState.INSERVICE.getState().substring(1))){
//            apiResponse.setCode(1);
//            apiResponse.setMsg("您所撤销的订单已经属于服务中或完成状态，不可以撤销了");
//            return  apiResponse;
//        }
        int i=0;
        if (StringUtils.isNotBlank(applyState)){
            i = applyInfoMapper.updateApplyInfo(applyInfo);
        }
        this.updateApproveResult(applyId,approveState,userId);
        this.updateOrderResult(applyId,userId);
        //UndoSMSTemplate undoSMSTemplate = applyInfoMapper.queryApplyUndoList(applyId);
        if(OrderState.WAITINGLIST.getState().equals(undoSMSTemplate.getState())){
            //未派单
            ismsBusiness.sendRevokeUndelivered(undoSMSTemplate);
        }else if(OrderState.ALREADYSENDING.getState().equals(undoSMSTemplate.getState())){
            // 已派单
            ismsBusiness.sendRevokealSentList(undoSMSTemplate);
        }else if(OrderState.READYSERVICE.getState().equals(undoSMSTemplate.getState())){
            //待服务
            ismsBusiness.sendRevokeToBeServed (undoSMSTemplate);
        }

        return apiResponse;
    }

    /**
     * 提交申请单
     * @param loginUser
     * @param applySingleVO
     * @return
     */
    @Override
    //@Transactional
    public ApiResponse submitApplySingle(LoginUser loginUser, ApplySingleVO applySingleVO) {
        ApiResponse apiResponse = new ApiResponse();
        //判断上下车地点的城市是否至少有一个在服务城市集范围内
        String startCode = applySingleVO.getStartAddr().getCityCode();
        String endCode = applySingleVO.getEndAddr().getCityCode();
        List<CarGroupInfo> list = carGroupServeScopeInfoMapper.getGroupIdByCode(startCode,endCode,loginUser.getUser().getDept().getCompanyId());
        if(list.isEmpty()){
            apiResponse.setCode(1);
            apiResponse.setMsg("非常抱歉，您的用车申请无可服务车队，申请失败，如有问题，请联系管理员！");
            return apiResponse;
        }
        //申请人id
        applySingleVO.setUserId(loginUser.getUser().getUserId());
        //申请人公司
        applySingleVO.setCompanyId(loginUser.getUser().getDept().getCompanyId());
        //获取当前申请单的制度Id
        Long regimenId = regimeInfoMapper.queryRegimeInfoByCompanyId(loginUser.getUser().getDept().getCompanyId());
        applySingleVO.setRegimenId(regimenId);
        //1.保存乘客行程信息 journey_info表
        JourneyInfo journeyInfo = new JourneyInfo();
        submitJourneyInfoCommit(applySingleVO,journeyInfo);
        //2.保存申请信息 apply_info表
        ApplyInfo applyInfo = new ApplyInfo();
        submitApplyInfoCommit(applySingleVO, journeyInfo.getJourneyId(), applyInfo);
        //3.保存行程节点信息 journey_node_info表
        JourneyNodeInfo journeyNodeInfo = new JourneyNodeInfo();
        submitJourneyNodeInfoCommit(applySingleVO, journeyInfo.getJourneyId(), journeyNodeInfo);
        //4.保存行程乘客信息 journey_passenger_info表
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        submitJourneyPassengerInfoCommit(applySingleVO, journeyInfo.getJourneyId(), journeyPassengerInfo);
        //5.新增可用车型表信息
        List<UseCarTypeVO> canUseCarTypes = regimeInfoMapper.getCanUseCarTypes(regimenId,loginUser.getUser().getDept().getCompanyId());
        for (UseCarTypeVO useCarTypeVO :canUseCarTypes){
            useCarTypeVO.setCityCode(applySingleVO.getStartAddr().getCityCode());
        }
        saveCanUseCarTypeInfo(applyInfo.getApplyId(), canUseCarTypes);
        //公务申请不需要审批情况下初始化权限和初始化订单
        ApplyOfficialRequest officialCommitApply = new ApplyOfficialRequest();
        officialCommitApply.setCompanyId(applySingleVO.getCompanyId());
        officialCommitApply.setSmsDifference("F001");//红旗公务=H001   佛山公务 = F001
        officialCommitApply.setApplyDate(applySingleVO.getApplyDate()); //用车时间
        officialCommitApply.setPassenger(applySingleVO.getPassenger());
        officialCommitApply.setApplyUser(applySingleVO.getApplyUser());
        officialCommitApply.setApplyDays(applySingleVO.getApplyDays());
        officialCommitApply.setReason(applySingleVO.getNotes());
        List<Long> orderIds = initPowerAndOrder(journeyInfo.getJourneyId(),applyInfo.getApplyType(), applyInfo.getApplyId(), applySingleVO.getUserId(),officialCommitApply);
        // 6.行程预算价表
        JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
        submitJourneyPlanPriceInfoCommit(applySingleVO,journeyInfo.getJourneyId(),journeyNodeInfo.getNodeId(),journeyPlanPriceInfo,orderIds.get(0));
        //新增下车多个地址表数据
        JourneyAddressInfoDto journeyAddressInfoDto = new JourneyAddressInfoDto();
        if(!applySingleVO.getMultipleDropAddress().isEmpty()) {
            List<AddressVO> addressInfo = applySingleVO.getMultipleDropAddress();
            for (AddressVO addressVO : addressInfo) {
                journeyAddressInfoDto.setJourneyId(journeyInfo.getJourneyId());
                journeyAddressInfoDto.setAddressInfo(addressVO.getAddress());
                journeyAddressInfoDto.setCreateBy(String.valueOf(applySingleVO.getUserId()));
                journeyAddressInfoDto.setCreateTime(DateUtils.getNowDate());
                int f  = applyInfoMapper.insertJourneyAddressInfo(journeyAddressInfoDto);
            }
        }
        //7.包车调度详情  order_dispatche_detail_info
        OrderDispatcheDetailInfo orderDispatcheDetailInfo = new OrderDispatcheDetailInfo();
        orderDispatcheDetailInfo.setOrderId(orderIds.get(0));
        orderDispatcheDetailInfo.setCharterCarType(journeyInfo.getCharterCarType());
        orderDispatcheDetailInfo.setDispatchState(CarConstant.DISPATCH_NOT_COMPLETED);
        orderDispatcheDetailInfo.setCreateBy(String.valueOf(applySingleVO.getUserId()));
        orderDispatcheDetailInfo.setCreateTime(DateUtils.getNowDate());
        int i = orderDispatcheDetailInfoMapper.insertOrderDispatcheDetailInfo(orderDispatcheDetailInfo);

        //8.保存用户常用地址
        try {
            userService.updateUserAddress(loginUser.getUser().getUserId(),applySingleVO.getStartAddr(),applySingleVO.getEndAddr(),applySingleVO.getMultipleDropAddress());
        }catch (Exception e){
            log.error("保存用户常用地址信息出错",e);
        }


        if(i == 1){
            apiResponse.setMsg("提交申请单成功");
        }else {
            apiResponse.setMsg("提交申请单失败");
        }
        return apiResponse;
    }

    @Override
    public List<Map<String,String>> getApplyStateCount( LoginUser loginUser) {
        Long companyId = loginUser.getUser().getDept().getCompanyId();
        List<Map<String,String>> list= applyInfoMapper.getApplyStateCount(companyId,loginUser.getUser().getDeptId());
        return list;
    }

    /**
     * 修改申请单
     * @param loginUser
     * @param applySingleVO
     * @return
     */
    @Override
    public ApiResponse updateApplySingle(LoginUser loginUser, ApplySingleVO applySingleVO) throws Exception{
        ApiResponse apiResponse = new ApiResponse();
        //判断上下车地点的城市是否至少有一个在服务城市集范围内
        String startCode = applySingleVO.getStartAddr().getCityCode();
        String endCode = applySingleVO.getEndAddr().getCityCode();
        List<CarGroupInfo> list = carGroupServeScopeInfoMapper.getGroupIdByCode(startCode,endCode,loginUser.getUser().getDept().getCompanyId());
        if(list.isEmpty()){
            apiResponse.setCode(1);
            apiResponse.setMsg("非常抱歉，您的用车申请无可服务车队，申请失败，如有问题，请联系管理员！");
            return apiResponse;
        }
        //申请人id
        applySingleVO.setUserId(loginUser.getUser().getUserId());
        //申请人公司
        applySingleVO.setCompanyId(loginUser.getUser().getDept().getCompanyId());
        //获取当前申请单的制度Id
        Long regimenId = regimeInfoMapper.queryRegimeInfoByCompanyId(loginUser.getUser().getDept().getCompanyId());
        applySingleVO.setRegimenId(regimenId);
        //查询所需要的id
        ApplySingleIdVO applySingleIdVO = applyInfoMapper.getApplySingleIdVO(applySingleVO.getApplyId());
        List<OrderDispatcheDetailInfo> orderDispatcheDetailInfos = dispatcheDetailInfoMapper.selectOrderDispatcheDetailInfoList(new OrderDispatcheDetailInfo(applySingleIdVO.getOrderId()));
        OrderDispatcheDetailInfo dispatcheDetailInfo = orderDispatcheDetailInfos.get(0);
        if (StringUtils.isNotBlank(dispatcheDetailInfo.getItIsUseInnerCarGroup())){
            apiResponse.setCode(1);
            apiResponse.setMsg("您所修改的订单已经属于派车中或服务中，不可以修改了");
            return  apiResponse;
        }
        //1.修改乘客行程信息 journey_info表
        JourneyInfo journeyInfo = new JourneyInfo();
        journeyInfo.setJourneyId(applySingleIdVO.getJourneyId());
        submitJourneyInfoCommit(applySingleVO,journeyInfo);
        //2.保存申请信息 apply_info表
        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setApplyId(applySingleVO.getApplyId());
        submitApplyInfoCommit(applySingleVO, journeyInfo.getJourneyId(), applyInfo);
        //3.保存行程节点信息 journey_node_info表
        JourneyNodeInfo journeyNodeInfo = new JourneyNodeInfo();
        journeyNodeInfo.setNodeId(applySingleIdVO.getNodeId());
        submitJourneyNodeInfoCommit(applySingleVO, journeyInfo.getJourneyId(), journeyNodeInfo);
        //4.保存行程乘客信息 journey_passenger_info表
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        journeyPassengerInfo.setJourneyPassengerId(applySingleIdVO.getJourneyPassengerId());
        submitJourneyPassengerInfoCommit(applySingleVO, journeyInfo.getJourneyId(), journeyPassengerInfo);
        //5.新增可用车型表信息
        List<UseCarTypeVO> canUseCarTypes = regimeInfoMapper.getCanUseCarTypes(regimenId,loginUser.getUser().getDept().getCompanyId());
        for (UseCarTypeVO useCarTypeVO :canUseCarTypes){
            useCarTypeVO.setCityCode(applySingleVO.getStartAddr().getCityCode());
        }
        //-----先删除后增加
        applyUseCarTypeMapper.deleteApplyUseCarTypeById(applySingleVO.getApplyId());
        saveCanUseCarTypeInfo(applyInfo.getApplyId(), canUseCarTypes);
        //修改订单地址表
        OrderAddressInfo   orderAddressInfo = new OrderAddressInfo();
        updateOrderAddressInfo(applySingleVO,applySingleIdVO , orderAddressInfo);
        // 6.行程预算价表
        JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
        journeyPlanPriceInfo.setPriceId(applySingleIdVO.getPriceId());
        submitJourneyPlanPriceInfoCommit(applySingleVO,journeyInfo.getJourneyId(),journeyNodeInfo.getNodeId(),journeyPlanPriceInfo,applySingleIdVO.getOrderId());
        //新增下车多个地址表数据
        //先做删除在做新增
        applyInfoMapper.deleteJourneyAddressInfo(applySingleIdVO.getJourneyId());
        JourneyAddressInfoDto journeyAddressInfoDto = new JourneyAddressInfoDto();
        if(!applySingleVO.getMultipleDropAddress().isEmpty()) {
            List<AddressVO> addressInfo = applySingleVO.getMultipleDropAddress();
            for (AddressVO addressVO : addressInfo) {
                journeyAddressInfoDto.setJourneyId(journeyInfo.getJourneyId());
                journeyAddressInfoDto.setAddressInfo(addressVO.getAddress());
                journeyAddressInfoDto.setCreateBy(String.valueOf(applySingleVO.getUserId()));
                journeyAddressInfoDto.setCreateTime(DateUtils.getNowDate());
                int f  = applyInfoMapper.insertJourneyAddressInfo(journeyAddressInfoDto);
            }
        }
        //7.修改包车调度详情  order_dispatche_detail_info
        OrderDispatcheDetailInfo orderDispatcheDetailInfo = new OrderDispatcheDetailInfo();
        orderDispatcheDetailInfo.setOrderId(applySingleIdVO.getOrderId());
        //包车类型
        String  halfDayRent = "0.5";  //半日租
        String  fullDayRent = "1";    //整日租
        if(CarConstant.RETURN_ZERO_CODE.equals(applySingleVO.getApplyDays().compareTo(halfDayRent))){
            //半日
            orderDispatcheDetailInfo.setCharterCarType(CharterTypeEnum.HALF_DAY_TYPE.getKey());
        }else if(CarConstant.RETURN_ZERO_CODE.equals(applySingleVO.getApplyDays().compareTo(fullDayRent))){
            //整日
            orderDispatcheDetailInfo.setCharterCarType(CharterTypeEnum.OVERALL_RENT_TYPE.getKey());
        }else{
            //多日
            orderDispatcheDetailInfo.setCharterCarType(CharterTypeEnum.MORE_RENT_TYPE.getKey());
        }
        //orderDispatcheDetailInfo.setCharterCarType(journeyInfo.getCharterCarType());
        orderDispatcheDetailInfo.setCreateBy(String.valueOf(applySingleVO.getUserId()));
        orderDispatcheDetailInfo.setCreateTime(DateUtils.getNowDate());
        orderDispatcheDetailInfo.setDispatchId(applySingleIdVO.getDispatchId().intValue());
        int i = orderDispatcheDetailInfoMapper.updateOrderDispatcheDetailInfo(orderDispatcheDetailInfo);
        //发送短信 --- 用车人撤销  用车人申请短信  内部所有调度员
        UndoSMSTemplate undoSMSTemplate = applyInfoMapper.queryApplyUndoList(applySingleIdVO.getApplyId());
        ismsBusiness.sendUpdateApplyInfoSms(undoSMSTemplate);
        if(i == 1){
            apiResponse.setMsg("修改申请单成功");
        }else {
            apiResponse.setMsg("修改申请单失败");
        }
        return apiResponse;
    }

    /**
     * 申请单详情
     * @param applyId
     * @return
     */
    @Override
    public ApplySingleVO getApplyInfoDetail(Long applyId) {
        ApplySingleVO  applySingleVO=applyInfoMapper.getApplyInfoDetail(applyId);
        if(applySingleVO!=null){
            JourneyNodeInfo journeyNodeInfo = journeyNodeInfoMapper.selectJourneyNodeInfoByJourneyId(applySingleVO.getJourneyId());
            AddressVO startAddr = new AddressVO();
            startAddr.setAddress(journeyNodeInfo.getPlanBeginAddress());
            startAddr.setCityCode(journeyNodeInfo.getPlanBeginCityCode());
            startAddr.setLatitude(journeyNodeInfo.getPlanBeginLatitude());
            startAddr.setLongAddress(journeyNodeInfo.getPlanBeginLongAddress());
            startAddr.setLongitude(journeyNodeInfo.getPlanBeginLongitude());
            applySingleVO.setStartAddr(startAddr);
            AddressVO endAddr = new AddressVO();
            endAddr.setAddress(journeyNodeInfo.getPlanEndAddress());
            endAddr.setCityCode(journeyNodeInfo.getPlanEndCityCode());
            endAddr.setLatitude(journeyNodeInfo.getPlanEndLatitude());
            endAddr.setLongAddress(journeyNodeInfo.getPlanEndLongAddress());
            endAddr.setLongitude(journeyNodeInfo.getPlanEndLongitude());
            applySingleVO.setEndAddr(endAddr);
            UserVO userVO =new UserVO();
            userVO.setUserName(applySingleVO.getUserName());
            userVO.setUserPhone(applySingleVO.getUserPhone());
            applySingleVO.setPassenger(userVO);
            List<AddressVO> list = applyInfoMapper.getJourneyAddressInfoByJourneyId(applySingleVO.getJourneyId());
            applySingleVO.setMultipleDropAddress(list);
        }
        return applySingleVO;
    }

    /**
     * 修改订单地址表数据
     * @param applySingleVO
     * @param applySingleIdVO
     * @param orderAddressInfo
     */
    private void updateOrderAddressInfo(ApplySingleVO applySingleVO, ApplySingleIdVO applySingleIdVO, OrderAddressInfo   orderAddressInfo) {
        //上车数据
        applySingleIdVO.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT);
        Long startId =orderAddressInfoMapper.selectOrderAddressInfo(applySingleIdVO);
        //下车数据
        applySingleIdVO.setType(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE);
        Long endId   =orderAddressInfoMapper.selectOrderAddressInfoTwo(applySingleIdVO);
        //修改上车数据
        OrderAddressInfo   orderAddressInfoStart = new OrderAddressInfo();
        orderAddressInfoStart.setOrderAddressId(startId);
        orderAddressInfoStart.setUserId(applySingleVO.getUserId().toString());
        orderAddressInfoStart.setCityPostalCode(applySingleVO.getStartAddr().getCityCode());
        orderAddressInfoStart.setActionTime(applySingleVO.getApplyDate());
        orderAddressInfoStart.setAddress(applySingleVO.getStartAddr().getAddress());
        orderAddressInfoStart.setAddressLong(applySingleVO.getStartAddr().getLongAddress());
        orderAddressInfoStart.setLatitude(Double.valueOf(applySingleVO.getStartAddr().getLatitude()));
        orderAddressInfoStart.setLongitude(Double.valueOf(applySingleVO.getStartAddr().getLongitude()));
        orderAddressInfoStart.setUpdateBy(applySingleVO.getUserId().toString());
        orderAddressInfoStart.setUpdateTime(DateUtils.getNowDate());
        orderAddressInfoMapper.updateOrderAddressInfo(orderAddressInfoStart);
        //修改下车数据
        OrderAddressInfo   orderAddressInfoEnd   = new OrderAddressInfo();
        orderAddressInfoEnd.setOrderAddressId(endId);
        orderAddressInfoEnd.setUserId(applySingleVO.getUserId().toString());
        orderAddressInfoEnd.setCityPostalCode(applySingleVO.getEndAddr().getCityCode());
        Long day = Math.round(Double.valueOf(applySingleVO.getApplyDays()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(applySingleVO.getApplyDate());
        calendar.add(Calendar.DATE, day.intValue()-1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        orderAddressInfoEnd.setActionTime(calendar.getTime());
        orderAddressInfoEnd.setAddress(applySingleVO.getEndAddr().getAddress());
        orderAddressInfoEnd.setAddressLong(applySingleVO.getEndAddr().getLongAddress());
        orderAddressInfoEnd.setLatitude(Double.valueOf(applySingleVO.getEndAddr().getLatitude()));
        orderAddressInfoEnd.setLongitude(Double.valueOf(applySingleVO.getEndAddr().getLongitude()));
        orderAddressInfoEnd.setUpdateBy(applySingleVO.getUserId().toString());
        orderAddressInfoEnd.setUpdateTime(DateUtils.getNowDate());
        orderAddressInfoMapper.updateOrderAddressInfo(orderAddressInfoEnd);
    }

    /**
     *  申请单行程预算价表
     * @param applySingleVO
     * @param journeyId
     * @param nodeId
     */
    private void submitJourneyPlanPriceInfoCommit(ApplySingleVO applySingleVO, Long journeyId, Long nodeId,JourneyPlanPriceInfo journeyPlanPriceInfo,Long orderId) {
        journeyPlanPriceInfo.setJourneyId(journeyId);
        journeyPlanPriceInfo.setNodeId(nodeId);
        //车型
        journeyPlanPriceInfo.setCarTypeId(applySingleVO.getCarTypeId());
        //订单类型
        journeyPlanPriceInfo.setUseCarMode(CarConstant.USR_CARD_MODE_HAVE);
        //订单id
        journeyPlanPriceInfo.setOrderId(orderId);
        //预算价格
        journeyPlanPriceInfo.setPrice(BigDecimal.ZERO);
        //计划启程时间
        journeyPlanPriceInfo.setPlannedDepartureTime(applySingleVO.getApplyDate());
        //计划到达时间
        Long day = Math.round(Double.valueOf(applySingleVO.getApplyDays()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(applySingleVO.getApplyDate());
        calendar.add(Calendar.DATE, day.intValue()-1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        journeyPlanPriceInfo.setPlannedArrivalTime(calendar.getTime());
        //预计用时-分钟
        journeyPlanPriceInfo.setDuration(day.intValue()*24*60);
        //预算价来源平台
        journeyPlanPriceInfo.setSource("无");
        if (null != journeyPlanPriceInfo.getPriceId()){
            //修改创建者
            journeyPlanPriceInfo.setUpdateBy(applySingleVO.getUserId().toString());
            //修改创建时间
            journeyPlanPriceInfo.setUpdateTime(DateUtils.getNowDate());
            journeyPlanPriceInfoMapper.updateJourneyPlanPriceInfo(journeyPlanPriceInfo);
        }else{
            //创建者
            journeyPlanPriceInfo.setCreateBy(applySingleVO.getUserId().toString());
            //创建时间
            journeyPlanPriceInfo.setCreateTime(DateUtils.getNowDate());
            journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
        }
    }

    /**
     * 修改订单表状态与轨迹表的状态
     * @param applyId
     */
    private void updateOrderResult(Long applyId,Long userId) {
        //订单id
        Long orderId = applyInfoMapper.selectOrderInfoById(applyId);
        if(null != orderId){
            //修改订单表状态
            OrderInfo orderInfo =new OrderInfo();
            orderInfo.setOrderId(orderId);
            orderInfo.setState(OrderState.ORDERCLOSE.getState());
            orderInfo.setUpdateBy(String.valueOf(userId));
            orderInfo.setUpdateTime(DateUtils.getNowDate());
            int i = orderInfoService.updateOrderInfo(orderInfo);
            if(i==1){
                //修改轨迹表状态
                OrderStateTraceInfo orderStateTraceInfo = new OrderStateTraceInfo();
                orderStateTraceInfo.setOrderId(orderId);
                orderStateTraceInfo.setState(OrderState.ORDERCANCEL.getState());
                //先写死后期做统一常量处理
                orderStateTraceInfo.setContent("申请单撤销");
                orderStateTraceInfo.setCreateBy(String.valueOf(userId));
                orderStateTraceInfo.setCreateTime(DateUtils.getNowDate());
                int j= orderStateTraceInfoMapper.insertOrderStateTraceInfo(orderStateTraceInfo);
            }
        }
    }

    /**
     * 保存乘客行程信息 journey_info表
     * @param applySingleVO
     * @param journeyInfo
     */
    private void submitJourneyInfoCommit(ApplySingleVO applySingleVO,JourneyInfo journeyInfo) {
        //用车制度Id
        journeyInfo.setRegimenId(applySingleVO.getRegimenId());
        //申请人Id
        journeyInfo.setUserId(applySingleVO.getUserId());
        //公司Id
        journeyInfo.setCompanyId(applySingleVO.getCompanyId());
        // service_type 包车
        journeyInfo.setServiceType(OrderServiceType .ORDER_SERVICE_TYPE_CHARTERED.getBcState());
        //use_car_mode 自有车
        journeyInfo.setUseCarMode(CarConstant.USR_CARD_MODE_HAVE);
        //是否往返
        journeyInfo.setItIsReturn(JourneyConstant.IT_IS_NOT_RETURN);
        //用车时间
        journeyInfo.setUseCarTime(applySingleVO.getApplyDate());
        //行程总时长
        journeyInfo.setUseTime(applySingleVO.getApplyDays());
        //包车类型
        String  halfDayRent = "0.5";  //半日租
        String  fullDayRent = "1";    //整日租
        if(CarConstant.RETURN_ZERO_CODE.equals(applySingleVO.getApplyDays().compareTo(halfDayRent))){
            //半日
            journeyInfo.setCharterCarType(CharterTypeEnum.HALF_DAY_TYPE.getKey());
        }else if(CarConstant.RETURN_ZERO_CODE.equals(applySingleVO.getApplyDays().compareTo(fullDayRent))){
            //整日
            journeyInfo.setCharterCarType(CharterTypeEnum.OVERALL_RENT_TYPE.getKey());
        }else{
            //多日
            journeyInfo.setCharterCarType(CharterTypeEnum.MORE_RENT_TYPE.getKey());
        }
        //行程开始时间
        journeyInfo.setStartDate(applySingleVO.getApplyDate());
        //行程结束时间
        Long day = Math.round(Double.valueOf(applySingleVO.getApplyDays()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(applySingleVO.getApplyDate());
        calendar.add(Calendar.DATE, day.intValue()-1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        journeyInfo.setEndDate(calendar.getTime());
        //行程标题
        journeyInfo.setTitle("包车申请单");
        if(null != journeyInfo.getJourneyId()){
            //修改者
            journeyInfo.setCreateBy(applySingleVO.getUserId().toString());
            //修改时间
            journeyInfo.setCreateTime(DateUtils.getNowDate());
            journeyInfoMapper.updateJourneyInfo(journeyInfo);
        }else{
            //创建者
            journeyInfo.setCreateBy(applySingleVO.getUserId().toString());
            //创建时间
            journeyInfo.setCreateTime(DateUtils.getNowDate());
            journeyInfoMapper.insertJourneyInfo(journeyInfo);
        }
    }

    /**
     * 提交差旅申请信息 apply_info
     * @param applySingleVO
     * @param journeyId
     * @param applyInfo
     */
    private void submitApplyInfoCommit(ApplySingleVO applySingleVO, Long journeyId, ApplyInfo applyInfo) {
        //乘客行程journey_id
        applyInfo.setJourneyId(journeyId);
        //制度Id regimen_id
        applyInfo.setRegimenId(applySingleVO.getRegimenId());
        //所属公司
        applyInfo.setCompanyId(applySingleVO.getCompanyId());
        //申请单编号
        applyInfo.setApplyNumber(RandomUtil.getRandomNumber());
        //用车申请类型
        applyInfo.setApplyType(CarConstant.USE_CAR_TYPE_OFFICIAL);
        //state 申请审批状态 S001  申请中 S002  通过 S003  驳回 S004  已撤销
        applyInfo.setState(ApplyStateConstant.APPLY_PASS);
        //车型级别编号
        applyInfo.setCarTypeId(applySingleVO.getCarTypeId());
        //外部车队id
        applyInfo.setOuterCarGroupId(applySingleVO.getOuterCarGroupId());
        //是否自驾
        applyInfo.setItIsSelfDriver(applySingleVO.getItIsSelfDriver());
        //reason 行程原因
        applyInfo.setReason(applySingleVO.getReason());
        //notes  用车注意事项
        applyInfo.setNotes(applySingleVO.getNotes());
        //是否安全提醒  Y000  是，安全提醒已勾选      N111  否，安全提醒未勾选
        applyInfo.setSafeRemind(applySingleVO.getSafeRemind());
        if (null != applyInfo.getApplyId()){
            // update_by 修改创建者
            applyInfo.setUpdateBy(applySingleVO.getUserId().toString());
            //update_time 修改创建时间
            applyInfo.setUpdateTime(DateUtils.getNowDate());
            applyInfoMapper.updateApplyInfo(applyInfo);
        }else {
            // create_by 创建者
            applyInfo.setCreateBy(applySingleVO.getUserId().toString());
            //create_time 创建时间
            applyInfo.setCreateTime(DateUtils.getNowDate());
            applyInfoMapper.insertApplyInfo(applyInfo);
        }
    }

    /**
     * 申请行程节点信息 journey_node_info
     * @param applySingleVO
     * @param journeyId
     * @param journeyNodeInfo
     */
    private void submitJourneyNodeInfoCommit(ApplySingleVO applySingleVO, Long journeyId,JourneyNodeInfo journeyNodeInfo) {
        //journey_id 非空
        journeyNodeInfo.setJourneyId(journeyId);
        //user_id 行程申请人 编号
        journeyNodeInfo.setUserId(applySingleVO.getUserId());
        //计划上车地址
        journeyNodeInfo.setPlanBeginAddress(applySingleVO.getStartAddr().getAddress()); //上车地址
        journeyNodeInfo.setPlanBeginLongAddress(applySingleVO.getStartAddr().getLongAddress()); //上车长地址
        journeyNodeInfo.setPlanBeginCityCode(applySingleVO.getStartAddr().getCityCode()); //上车城市
        journeyNodeInfo.setPlanBeginLongitude(applySingleVO.getStartAddr().getLongitude()); //上车长经度
        journeyNodeInfo.setPlanBeginLatitude(applySingleVO.getStartAddr().getLatitude()); //上车短纬度
        //计划下车地址
        journeyNodeInfo.setPlanEndAddress(applySingleVO.getEndAddr().getAddress()); //下车地址
        journeyNodeInfo.setPlanEndLongAddress(applySingleVO.getEndAddr().getLongAddress()); //下车长地址
        journeyNodeInfo.setPlanEndCityCode(applySingleVO.getEndAddr().getCityCode()); //下车城市
        journeyNodeInfo.setPlanEndLongitude(applySingleVO.getEndAddr().getLongitude()); //下车长经度
        journeyNodeInfo.setPlanEndLatitude(applySingleVO.getEndAddr().getLatitude()); //下车短纬度
        //计划出发时间
        journeyNodeInfo.setPlanSetoutTime(applySingleVO.getApplyDate());
        //计划到达时间
        Long day = Math.round(Double.valueOf(applySingleVO.getApplyDays()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(applySingleVO.getApplyDate());
        calendar.add(Calendar.DATE, day.intValue()-1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        journeyNodeInfo.setPlanArriveTime(calendar.getTime());
        //是否是途经点
        journeyNodeInfo.setItIsViaPoint(CarConstant.NOT_ALLOW_USE);
        //交通工具 T001  飞机 T101  火车 T201  汽车 T301  轮渡 T999  其他   差旅申请才有
        journeyNodeInfo.setVehicle(CarConstant.TRAFFIC_AUTOMOBILE);
        //行程节点 （天）
        journeyNodeInfo.setDuration(applySingleVO.getApplyDays());
        //P000   有效中  P444   已失效
        journeyNodeInfo.setNodeState(CommonConstant.VALID_NODE);
        ///节点在 在整个行程 中的顺序编号 从  1  开始   节点编号单独判断
        journeyNodeInfo.setNumber(CarConstant.RETURN_ONE_CODE);
        if (null != journeyNodeInfo.getNodeId()){
            //修改创建者
            journeyNodeInfo.setUpdateBy(applySingleVO.getUserId().toString());
            //修改创建时间
            journeyNodeInfo.setUpdateTime(DateUtils.getNowDate());
            journeyNodeInfoMapper.updateJourneyNodeInfo(journeyNodeInfo);
        }else {
            //创建者
            journeyNodeInfo.setCreateBy(applySingleVO.getUserId().toString());
            //创建时间
            journeyNodeInfo.setCreateTime(DateUtils.getNowDate());
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
        }
        }

    /**
     * 提交乘客信息表
     * @param applySingleVO
     * @param journeyId
     * @param journeyPassengerInfo
     */
    private void submitJourneyPassengerInfoCommit(ApplySingleVO applySingleVO, Long journeyId, JourneyPassengerInfo journeyPassengerInfo) {
        //journey_id 非空
        journeyPassengerInfo.setJourneyId(journeyId);
        //乘车人姓名
        journeyPassengerInfo.setName(applySingleVO.getPassenger().getUserName());
        //乘车人手机号
        journeyPassengerInfo.setMobile(applySingleVO.getPassenger().getUserPhone());
        // 是否是同行者 00   是   01   否
        journeyPassengerInfo.setItIsPeer(CommonConstant.IS_PEER);
        //同行人数
        journeyPassengerInfo.setPeerNumber(applySingleVO.getPeerNumber());
        if (null != journeyPassengerInfo.getJourneyPassengerId()){
            //修改创建者
            journeyPassengerInfo.setUpdateBy(applySingleVO.getUserId().toString());
            //修改创建时间
            journeyPassengerInfo.setUpdateTime(DateUtils.getNowDate());
            journeyPassengerInfoMapper.updateJourneyPassengerInfo(journeyPassengerInfo);
        }else {
            //创建者
            journeyPassengerInfo.setCreateBy(applySingleVO.getUserId().toString());
            //创建时间
            journeyPassengerInfo.setCreateTime(DateUtils.getNowDate());
            journeyPassengerInfoMapper.insertJourneyPassengerInfo(journeyPassengerInfo);
        }
    }
}
