package com.hq.ecmp.mscore.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.hq.common.exception.BaseException;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.dto.ApplyInfoDTO;
import com.hq.ecmp.mscore.dto.ApplyOfficialRequest;
import com.hq.ecmp.mscore.dto.ApplyTravelRequest;
import com.hq.ecmp.mscore.dto.JourneyCommitApplyDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IApplyApproveResultInfoService;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IEnterpriseCarTypeInfoService;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.DateFormatUtils;
import com.hq.ecmp.util.RandomUtil;
import com.hq.ecmp.util.SortListUtil;
import io.netty.util.internal.ObjectUtil;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
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

    private ExecutorService executor = Executors.newFixedThreadPool(3);

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

    /**
     * 提交行程申请单  弃用，公务申请，差旅申请下面分开实现
     * @param journeyCommitApplyDto
     */
    @Deprecated
    @Override
    @Transactional
    public void applyCommit(JourneyCommitApplyDto journeyCommitApplyDto) {

    }

    /**
     * 提交差旅行程申请
     * @param travelCommitApply
     */
    @Override
    @Transactional
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

        ApplyVO applyVO = ApplyVO.builder().journeyId(journeyId).applyId(applyId).build();

        //3.保存行程节点信息(差旅相关) journey_node_info表
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
            ApplyTravelRequest applyTravelRequest = ApplyTravelRequest.builder().applyType(travelCommitApply.getApplyType())
                    .applyUser(travelCommitApply.getApplyUser())
                    .approvers(travelCommitApply.getApprovers())
                    .costCenter(travelCommitApply.getCostCenter())
                    .endDate(travelCommitApply.getEndDate())
                    .isGoBack(travelCommitApply.getIsGoBack())
                    .passenger(travelCommitApply.getPassenger())
                    .pickupTimes(travelCommitApply.getPickupTimes())
                    .projectNumber(travelCommitApply.getProjectNumber())
                    .reason(travelCommitApply.getReason())
                    .regimenId(travelCommitApply.getRegimenId())
                    .startDate(travelCommitApply.getStartDate())
                    .travelCitiesStr(travelCommitApply.getTravelCitiesStr())
                    .travelCount(travelCommitApply.getTravelCount())
                    .travelPickupCity(travelCommitApply.getTravelPickupCity())
                    .travelRequests(travelCommitApply.getTravelRequests())
                    .useType(travelCommitApply.getUseType())
                    .build();
            List<TravelRequest> travelRequestsRev = applyTravelRequest.getTravelRequests();
            //得到去程节点
            TravelRequest travelRequest = travelRequestsRev.get(0);
            TravelRequest travelRequestSetout = travelRequests.get(0);
            //设置返程出发地为最初目的地
            travelRequest.setStartCity(travelRequestSetout.getEndCity());
            //设置返程目的地为最初出发地
            travelRequest.setEndCity(travelRequestSetout.getStartCity());
            //设置返程开始日期为去程结束日期
            travelRequest.setStartDate(travelRequestSetout.getEndDate());
            //设置返程结束日期为空
            travelRequest.setEndDate(null);
            //设置返程节点时长为空
            travelRequest.setCountDate(null);
            //TODO 返程交通工具暂不做修改
            journeyNodeInfo = new JourneyNodeInfo();
            setTravelJourneyNode(applyTravelRequest, journeyId, journeyNodeInfo, i + 1, travelRequest);
            //新增返程节点
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
        }

        //差旅申请 行程表的title字段需要单独设置
        setTitleInJourneyInfo(journeyId);


        //4.保存行程乘客信息 journey_passenger_info表
        // 差旅只有乘车人，没有同行人
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        // 提交差旅乘客信息表
        journeyPassengerInfoCommit(travelCommitApply, journeyId, journeyPassengerInfo);

        // -------------- 初始化审批流   发通知（给审批人和自己） 、 发短信（给审批人）-----------------------------
        Long userId = getLoginUserId();
        Integer regimenId = travelCommitApply.getRegimenId();


        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //1.初始化审批流
                    applyApproveResultInfoService.initApproveResultInfo(applyId,Long.valueOf(regimenId),userId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        executor.submit(new Runnable() {
            @Override
            public void run() {
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
                        //给审批人发通知
                        sendNoticeToApprover(applyId,userId,approver);
                        //给审批人发短信  1.员工姓名 2.日期 3.城市
                        // 员工陈超已提交“2019年08月20日-2019年08月23日，长春-上海-长春”的差旅用车申请，请登录红旗公务APP及时处理。（差旅申请）
                        Map<String,String> map = new HashMap<>();
                        map.put("userName",userName);
                        map.put("date",date);
                        map.put("city",title);
                        try {
                            //给审批人发短信
                            iSmsTemplateInfoService.sendSms(SmsTemplateConstant.TRAVEL_APPLY_APPROVER,map,approver.getApprovalPhone());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //给自己发通知
                    sendApplyNoticeToSelf(userId,applyId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return applyVO;
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
        journeyNodeInfo.setUserId(travelCommitApply.getApplyUser().getUserId()); //TODO 判空
        //3.3 plan_begin_address 计划上车地址  非空
        journeyNodeInfo.setPlanBeginLongAddress(null);
        journeyNodeInfo.setPlanBeginAddress(travelRequest.getStartCity().getCityName());
        journeyNodeInfo.setPlanBeginCityCode(String.valueOf(travelRequest.getStartCity().getCityCode()));
        //3.4 plan_end_address 计划下车地址    非空
        journeyNodeInfo.setPlanEndLongAddress(null);
        journeyNodeInfo.setPlanEndAddress(travelRequest.getEndCity().getCityName());
        journeyNodeInfo.setPlanEndCityCode(String.valueOf(travelRequest.getEndCity().getCityCode()));
        //3.5 plan_setout_time 计划出发时间
        journeyNodeInfo.setPlanSetoutTime(travelRequest.getStartDate());  // TODO 出差某一节点开始日期
        //3.6 plan_arrive_time 计划到达时间
        journeyNodeInfo.setPlanArriveTime(travelRequest.getEndDate());  // TODO 出差某一节点结束日期
        //3.7 plan_begin_longitude 出发坐标
        journeyNodeInfo.setPlanBeginLongitude(null);   // TODO 差旅是城市代码、城市id
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
        //3.13 duration 行程节点 预估用时， X 小时 X 分钟
        journeyNodeInfo.setDuration(String.valueOf(travelRequest.getCountDate()));   // TODO 差旅节点时长是多少天字段。公务应该是X 小时 X 分钟
        //3.14 distance 行程节点 预估里程 单位公：里
        journeyNodeInfo.setDistance(null);
        //3.15 wait_duration 航班到达后等待多时时间  用车，单位 分钟 M010 10分钟 M020 20分钟 M030 30分钟 H100 一小时 H130 一个半小时
        journeyNodeInfo.setWaitDuration(null);
        //3.16 node_state 行程节点状态 订单---->用车权限----->行程节点   反推  节点任务是否已完成 失效 也算已经  节点任务已经完成
        //P000   有效中  P444   已失效
        journeyNodeInfo.setNodeState(CommonConstant.VALID_NODE);  // TODO 申请通过前节点算是有效吗？待确认
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
        ApplyDetailVO applyDetailVO = ApplyDetailVO.builder()
                //申请原因
                .reason(applyInfo.getReason()).applyId(applyInfo.getApplyId())
                //行程id
                .jouneyId(applyInfo.getJourneyId())
                //成本中心
                .costCenter(String.valueOf(applyInfo.getCostCenter()))
                //项目编号
                .projectNumber(String.valueOf(applyInfo.getProjectId()))
                //申请状态
                .status(applyInfo.getState())
                .time(applyInfo.getCreateTime())
                //申请类型
                .type(applyInfo.getApplyType())
                .build();
        //2.根据journreyId查询行程表相关信息
        JourneyInfo journeyInfo = journeyInfoMapper.selectJourneyInfoById(applyInfo.getJourneyId());
        //用车时间
        Date useCarTime = journeyInfo.getUseCarTime();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        applyDetailVO.setApplyDate(useCarTime);
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(journeyInfo.getUserId());
        //申请人
        applyDetailVO.setApplyUser(ecmpUser.getNickName());
        applyDetailVO.setApplyMobile(ecmpUser.getPhonenumber());//TODO 根据用户id查出用户名字  journeyInfo.getUserId()
        //是否往返
        applyDetailVO.setIsGoBack(journeyInfo.getItIsReturn());
        //返回等待时间
        applyDetailVO.setWaitingTime(journeyInfo.getWaitTimeLong());
        //预估价格
        applyDetailVO.setEstimatePrice(journeyInfo.getEstimatePrice());

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
        //如果是差旅申请A002:  差旅用车
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
                Integer pickup = pickupCity.getPickup();
                //送机/站次数
                Integer dropOff = pickupCity.getDropOff();
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

        return applyDetailVO;
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
//        List<EcmpUserRole> list=userRoleMapper.selectEcmpUserRoleList(new EcmpUserRole(userId));
//        List<Long> roleIds = list.stream().map(EcmpUserRole::getRoleId).collect(Collectors.toList());
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
        //4.4 it_is_peer 是否是同行者 00   是   01   否
        journeyPassengerInfo.setItIsPeer(CommonConstant.IS_NOT_PEER);   // TODO 常量 差旅没有同行人
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
        applyInfo.setProjectId(Long.valueOf(travelCommitApply.getProjectNumber()));
        //2.3 regimen_id 非空
        applyInfo.setRegimenId(Long.valueOf(travelCommitApply.getRegimenId()));  // TODO 判空
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
        applyInfo.setCostCenter(Long.valueOf(travelCommitApply.getCostCenter())); //TODO 判空
        //2.7 state 申请审批状态 S001  申请中 S002  通过 S003  驳回 S004  已撤销
        applyInfo.setState(ApplyStateConstant.ON_APPLYING); //TODO 枚举
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
        journeyInfo.setUserId(Long.valueOf(travelCommitApply.getApplyUser().getUserId())); // TODO 判空
        //1.2 用车制度id        非空
        journeyInfo.setRegimenId(Long.valueOf(travelCommitApply.getRegimenId()));  // TODO 判空
        //1.3 service_type 预约、接机、送机、包车 1000预约 2001接机 2002送机 3000包车
        journeyInfo.setServiceType(null);
        //1.4 use_car_mode 用车方式（自有、网约车）
        journeyInfo.setUseCarMode(travelCommitApply.getUseType());
        //1.5 use_car_time 用车时间
        Date startDate = travelCommitApply.getTravelRequests().get(0).getStartDate();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        //String startDateStr = dateFormat.format(startDate);
        journeyInfo.setUseCarTime(startDate);  //TODO 用车时间
        //1.6 it_is_return 是否往返 Y000 N444
        journeyInfo.setItIsReturn(travelCommitApply.getIsGoBack());
        //1.7 estimate_price 预估价格     非空
        journeyInfo.setEstimatePrice(null);
        //1.8 project_id  项目编号
        journeyInfo.setProjectId(Long.valueOf(travelCommitApply.getProjectNumber()));  // TODO 判空
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
        journeyInfo.setStartDate(travelCommitApply.getStartDate());       //TODO 新增 出差开始时间  需要前端提供
        journeyInfo.setEndDate(travelCommitApply.getEndDate());        //TODO 新增 出差最终结束时间  需要前端提供
        List<TravelPickupCity> travelPickupCityList = travelCommitApply.getTravelPickupCity();
        String travelPickupCityStr  = JSONArray.toJSON(travelPickupCityList).toString();
        journeyInfo.setTravelPickupCity(travelPickupCityStr);  //TODO 新增 出差需接送机城市数组  需要前端提供
        journeyInfo.setTravelCitiesStr(travelCommitApply.getTravelCitiesStr());  //TODO 新增 出差需市内用车城市  需要前端提供
        journeyInfo.setPickupTimes(travelCommitApply.getPickupTimes());    // TODO 新增 出差接送机总次数  需要前端提供
        String firstCityName = travelCommitApply.getTravelRequests().get(0).getStartCity().getCityName();
        journeyInfo.setTitle(firstCityName);  // TODO 新增 出差标题     北京-上海、南京。需要判断
        journeyInfoMapper.insertJourneyInfo(journeyInfo);
    }

    /**
     * 提交公务行程申请
     * @param officialCommitApply
     */
    @Override
    @Transactional
    public ApplyVO applyOfficialCommit(ApplyOfficialRequest officialCommitApply) {

        //1.保存乘客行程信息 journey_info表
        JourneyInfo journeyInfo = new JourneyInfo();
        //提交公务行程表信息
        journeyOfficialCommit(officialCommitApply, journeyInfo);
        Long journeyId = journeyInfo.getJourneyId();

        //2.保存申请信息 apply_info表
        ApplyInfo applyInfo = new ApplyInfo();
        //提交公务申请表信息
        applyInfoOfficialCommit(officialCommitApply, journeyId, applyInfo);
        Long applyId = applyInfo.getApplyId();

        ApplyVO applyVO = ApplyVO.builder().applyId(applyId).journeyId(journeyId).build();

        //复制参数对象
        ApplyOfficialRequest applyOfficialRequest = ApplyOfficialRequest.builder().
                applyDate(officialCommitApply.getApplyDate())
               .applyType(officialCommitApply.getApplyType())
               .applyUser(officialCommitApply.getApplyUser())
               .approvers(officialCommitApply.getApprovers())
               .charterType(officialCommitApply.getCharterType())
               .costCenter(officialCommitApply.getCostCenter())
               .endAddr(officialCommitApply.getEndAddr())
               .estimatePrice(officialCommitApply.getEstimatePrice())
               .flightNumber(officialCommitApply.getFlightNumber())
               .isGoBack(officialCommitApply.getIsGoBack())
               .partner(officialCommitApply.getPartner())
               .passedAddress(officialCommitApply.getPassedAddress())
               .passenger(officialCommitApply.getPassenger())
               .projectNumber(officialCommitApply.getProjectNumber())
               .reason(officialCommitApply.getReason())
               .regimenId(officialCommitApply.getRegimenId())
               .returnWaitTime(officialCommitApply.getReturnWaitTime())
               .serviceType(officialCommitApply.getServiceType())
               .startAddr(officialCommitApply.getStartAddr())
               .useType(officialCommitApply.getUseType())
               .waitDurition(officialCommitApply.getWaitDurition()).build();
        //3.保存行程节点信息 journey_node_info表
        JourneyNodeInfo journeyNodeInfo = null;
        // TODO 公务是一个行程  如果往返，需要创建两个行程节点还是一个？此处创建一个
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
        Long nodeIdNoReturn = 0L;
        Long nodeIdIsReturn = 0L;
        if(size == 0){
            journeyNodeInfo = new JourneyNodeInfo();
            //设置行程节点信息表
            journeyNodeOfficialCommit(applyOfficialRequest, journeyId, journeyNodeInfo);
            //判断是否途经点  er模型中  是这样的   途经点  同样具有顺序  Y000     是  N111    否
            journeyNodeInfo.setItIsViaPoint(CommonConstant.NO_PASS);  // TODO 常量
            //节点编号
            journeyNodeInfo.setNumber(n);
            //保存数据
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
            nodeIdNoReturn = journeyNodeInfo.getNodeId();

        }else {
            //A...提交第一个节点 把第一个途经点作为目的地
            journeyNodeInfo = new JourneyNodeInfo();
            AddressVO firstEndAddr = passedAddressList.get(0);
            applyOfficialRequest.setEndAddr(firstEndAddr);
            //设置行程节点信息表
            journeyNodeOfficialCommit(applyOfficialRequest, journeyId, journeyNodeInfo);
            //判断是否途经点  是 Y000
            journeyNodeInfo.setItIsViaPoint(CommonConstant.PASS);   //TODO 常量
            //节点编号
            journeyNodeInfo.setNumber(n);
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
            journeyNodeInfo.setItIsViaPoint(CommonConstant.NO_PASS);  //TODO 常量
            //节点编号
            journeyNodeInfo.setNumber(n+size);
            //保存数据
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);

            nodeIdNoReturn = journeyNodeInfo.getNodeId();

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
                journeyNodeInfo.setItIsViaPoint(CommonConstant.PASS);  // TODO 常量
                //设置节点编号
                journeyNodeInfo.setNumber(n+i+1);
                //保存数据
                journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
            }
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
            journeyNodeInfo.setItIsViaPoint(CommonConstant.NO_PASS);  // TODO 常量
            //设置节点编号
            journeyNodeInfo.setNumber(n+size+1);
            //保存数据
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
            nodeIdIsReturn = journeyNodeInfo.getNodeId();
        }

        //4.保存行程乘客信息 journey_passenger_info表
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        journeyPassergerOfficialCommit(officialCommitApply, journeyId, journeyPassengerInfo);

        //公务申请成功后 ---------------1. 调用初始化审批流方法 2.给审批人发送通知，给自己发送通知 3.给审批人发送短信 4.保存预估价格表
        Long userId = getLoginUserId();
        Integer regimenId = officialCommitApply.getRegimenId();

        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    //1.初始化审批流
                    applyApproveResultInfoService.initApproveResultInfo(applyId,Long.valueOf(regimenId),userId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        List<ApprovalVO> approvers = officialCommitApply.getApprovers();
        //2.给审批人和自己发通知
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
                   //(1) 员工姓名 （2）日期 时间
                   for (ApprovalVO approver : approvers) {
                       //给审批人发通知
                       sendNoticeToApprover(applyId, userId, approver);
                       //给审批人发短信
                       sendMsgToApprover(SmsTemplateConstant.OFFICIAL_APPLY_APPROVER,userName,useCarTime,approver);
                   }
                   //给自己发通知
                   sendApplyNoticeToSelf(userId, applyId);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
        });

        //-------------------  保存预估价格表 -------------------

        List<CarLevelAndPriceVO> carLevelAndPriceVOs = officialCommitApply.getCarLevelAndPriceVOs();
        for (CarLevelAndPriceVO carLevelAndPriceVO : carLevelAndPriceVOs) {
            String isGoBack = officialCommitApply.getIsGoBack();
            JourneyPlanPriceInfo journeyPlanPriceInfo = new JourneyPlanPriceInfo();
            //根據车型查询车型id
            String onlineCarLevel = carLevelAndPriceVO.getOnlineCarLevel();
            Long carTypeId = enterpriseCarTypeInfoMapper.selectCarTypeId(onlineCarLevel);
            journeyPlanPriceInfo.setJourneyId(journeyId);
            journeyPlanPriceInfo.setNodeId(nodeIdNoReturn);
            journeyPlanPriceInfo.setCarTypeId(carTypeId);
            journeyPlanPriceInfo.setPowerId(null);
            journeyPlanPriceInfo.setOrderId(null);
            journeyPlanPriceInfo.setPrice(BigDecimal.valueOf(carLevelAndPriceVO.getEstimatePrice()));
            Date applyDate = officialCommitApply.getApplyDate();
            journeyPlanPriceInfo.setPlannedDepartureTime(applyDate);
            Integer duration = carLevelAndPriceVO.getDuration();
            journeyPlanPriceInfo.setDuration(duration);
            journeyPlanPriceInfo.setPlannedArrivalTime(new Date(duration*60*1000+applyDate.getTime()));
            journeyPlanPriceInfo.setSource(carLevelAndPriceVO.getSource());
            journeyPlanPriceInfo.setCreateBy(String.valueOf(getLoginUserId()));
            journeyPlanPriceInfo.setCreateTime(new Date());
            journeyPlanPriceInfo.setUpdateBy(null);
            journeyPassengerInfo.setUpdateTime(null);
            journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
            //如果有往返 并且需要追则追加存一条
            if(CommonConstant.IS_RETURN.equals(isGoBack)){
                Long returnWaitTime = Long.valueOf(officialCommitApply.getReturnWaitTime());
                journeyPlanPriceInfo.setNodeId(nodeIdIsReturn);
                journeyPlanPriceInfo.setPlannedDepartureTime(new Date(duration*60*1000+applyDate.getTime()+returnWaitTime));
                journeyPlanPriceInfo.setPlannedArrivalTime(new Date(2*duration*60*1000+applyDate.getTime()+returnWaitTime));
                journeyPlanPriceInfoMapper.insertJourneyPlanPriceInfo(journeyPlanPriceInfo);
            }
        }



        return applyVO;
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
                .configType(MsgUserConstant.MESSAGE_USER_APPROVAL.getType())                 //对应用户类型（1.乘客，2.司机，3.调度员。4，审批员）
                .ecmpId(Long.valueOf(approver.getUserId()))  //对应用户类型id  configType为 2 此处为 driverId ，否则此处为userId
                .categoryId(applyId)   // 类别id 申请则为申请id 订单类则为 订单id
                .type(MsgTypeConstant.MESSAGE_TYPE_T001.getType())  //消息类型 T001-业务消息 T002- T003  T004
                .status(MsgStatusConstant.MESSAGE_STATUS_T002.getType())   //消息状态 0000-已读  1111-未读
                .category(MsgConstant.MESSAGE_T001.getType())   //消息类别，随业务自行添加 M001  申请通知 M002  审批通知 M003  调度通知 M004  订单改派 M005  订单取消 M999  其他
                .content("你有一条申请通知")
                .url("") //事项处理跳转链接地址， 需要密切联系业务调整规则 大部分应该是跳转到  事项处理的列表页 单个具体事项出题 请带上 业务的主键ID，
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

    //公务申请给审批人发短信
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
            e.printStackTrace();
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
        journeyNodeInfo.setPlanSetoutTime(officialCommitApply.getApplyDate());    // TODO 跟差旅申请记录时间不一样
        //3.6 plan_arrive_time 计划到达时间
        journeyNodeInfo.setPlanArriveTime(null);   // TODO 差旅申请
        //3.7 plan_begin_longitude 出发坐标
        journeyNodeInfo.setPlanBeginLongitude(officialCommitApply.getStartAddr().getLongitude());
        //3.8 plan_begin_latitude
        journeyNodeInfo.setPlanBeginLatitude(officialCommitApply.getStartAddr().getLatitude());
        //3.9 plan_end_longitude
        journeyNodeInfo.setPlanEndLongitude(officialCommitApply.getEndAddr().getLongitude());
        //3.10 plan_end_latitude
        journeyNodeInfo.setPlanEndLatitude(officialCommitApply.getEndAddr().getLatitude());
        //3.11 it_is_via_point 是否是途经点  途经点仅仅用于 地图描点  和  导航使用;途经点  同样具有顺序
        journeyNodeInfo.setItIsViaPoint(null);       // TODO 这个途径点提出来单独判断
        //3.12 vehicle 交通工具 T001  飞机 T101  火车 T201  汽车 T301  轮渡 T999  其他
        journeyNodeInfo.setVehicle(null);           // TODO 差旅申请才有
        //3.13 duration 行程节点 预估用时， X 小时 X 分钟
        journeyNodeInfo.setDuration(null);       //TODO 页面没有数据
        //3.14 distance 行程节点 预估里程 单位公：里
        journeyNodeInfo.setDistance(null);       // TODO 暂无数据
        //3.15 wait_duration 航班到达后等待多时时间  用车，单位 分钟 M010 10分钟 M020 20分钟 M030 30分钟 H100 一小时 H130 一个半小时
        journeyNodeInfo.setWaitDuration(officialCommitApply.getWaitDurition());   // TODO 新增参数
        //3.16 node_state 行程节点状态 订单---->用车权限----->行程节点   反推  节点任务是否已完成 失效 也算已经  节点任务已经完成
        //P000   有效中  P444   已失效
        journeyNodeInfo.setNodeState(CommonConstant.VALID_NODE);
        ///3.17 number 节点在 在整个行程 中的顺序编号 从  1  开始
        journeyNodeInfo.setNumber(null);    // TODO 节点编号单独判断
        //3.18 create_by 创建者
        journeyNodeInfo.setCreateBy(String.valueOf(getLoginUserId()));  //TODO 申请人 和 创建人
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
        applyInfo.setProjectId(Long.valueOf(officialCommitApply.getProjectNumber())); //TODO 要判空
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
        //2.6 cost_center 成本中心 从组织机构表 中获取
        applyInfo.setCostCenter(Long.valueOf(officialCommitApply.getCostCenter())); //TODO 要判空
        //2.7 state 申请审批状态 S001  申请中 S002  通过 S003  驳回 S004  已撤销
        applyInfo.setState(ApplyStateConstant.ON_APPLYING);
        //2.8 reason 行程原因
        applyInfo.setReason(officialCommitApply.getReason());
        //2.9 create_by 创建者
        applyInfo.setCreateBy(String.valueOf(getLoginUserId())); //TODO 创建者 与 申请人区别
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
     */
    private void journeyOfficialCommit(ApplyOfficialRequest officialCommitApply, JourneyInfo journeyInfo) {
        //1.1 userId          非空
        journeyInfo.setUserId(Long.valueOf(officialCommitApply.getApplyUser().getUserId()));
        //1.2 用车制度id        非空
        journeyInfo.setRegimenId(Long.valueOf(officialCommitApply.getRegimenId()));  //TODO 新增字段
        //1.3 service_type 预约、接机、送机、包车 1000预约 2001接机 2002送机 3000包车
        journeyInfo.setServiceType(officialCommitApply.getServiceType());
        //1.4 use_car_mode 用车方式（自有、网约车）
        journeyInfo.setUseCarMode(officialCommitApply.getUseType());
        //1.5 use_car_time 用车时间
        Date applyDate = officialCommitApply.getApplyDate();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");  //TODO 时间方法改动下
        if (applyDate != null){
            //String date = dateFormat.format(applyDate);
            journeyInfo.setUseCarTime(applyDate);
        }else {
            Long flightPlanArriveTime = officialCommitApply.getFlightPlanArriveTime().getTime();
            Date useCarTime = getUseCarTimeForFlight(officialCommitApply, flightPlanArriveTime);
            journeyInfo.setUseCarTime(useCarTime);
        }
        //1.6 it_is_return 是否往返 Y000 N444
        journeyInfo.setItIsReturn(officialCommitApply.getIsGoBack());    //TODO  有往返的话，创建两个行程
        //1.7 estimate_price 预估价格     非空
        journeyInfo.setEstimatePrice(String.valueOf(officialCommitApply.getEstimatePrice()));
        //1.8 project_id  项目编号
        journeyInfo.setProjectId(Long.valueOf(officialCommitApply.getProjectNumber()));  //TODO 要判空
        //1.9 flight_number 航班编号
        journeyInfo.setFlightNumber(officialCommitApply.getFlightNumber());
        //1.10 use_time 行程总时长  多少天
        journeyInfo.setUseTime(null);     // TODO 该字段未使用（差旅的）
        //1.11 wait_time_long 预计等待时间，出发地 第一个节点 等待时长
        journeyInfo.setWaitTimeLong(officialCommitApply.getReturnWaitTime());  // TODO 新增 往返，返回等待时长
        //1.12 charter_car_type 包车类型：T000  非包车 T001 半日租（4小时）T002 整日租（8小时）
        journeyInfo.setCharterCarType(officialCommitApply.getCharterType());
        //1.13 create_by 创建者
        journeyInfo.setCreateBy(String.valueOf(getLoginUserId()));  //TODO 数据库中是int
        //1.14 create_time 创建时间
        journeyInfo.setCreateTime(new Date());
        //1.15 update_by 更新者
        journeyInfo.setUpdateBy(null);
        //1.16 update_time 更新时间
        journeyInfo.setUpdateBy(null);
        //1.17 预计航班起飞时间
        journeyInfo.setFlightPlanTakeOffTime(officialCommitApply.getFlightPlanTakeOffTime());
        journeyInfo.setStartDate(officialCommitApply.getApplyDate());       //TODO 新增 行程开始时间，用车时间
        journeyInfo.setEndDate(null);        //TODO 新增 公务行程结束时间，未知。也用不着
        journeyInfo.setTravelPickupCity(null);  //TODO 新增 出差需接送机城市为空
        journeyInfo.setTravelCitiesStr(null);  //TODO 新增 出差市内用车城市为空
        journeyInfo.setPickupTimes(null);    // TODO 新增 出差接送机总次数为空
        journeyInfo.setTitle(officialCommitApply.getReason());  // TODO 新增 公务title为reason
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

}
