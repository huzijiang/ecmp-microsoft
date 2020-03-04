package com.hq.ecmp.mscore.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.domain.JourneyInfo;
import com.hq.ecmp.mscore.domain.JourneyNodeInfo;
import com.hq.ecmp.mscore.domain.JourneyPassengerInfo;
import com.hq.ecmp.mscore.dto.ApplyInfoDTO;
import com.hq.ecmp.mscore.dto.ApplyOfficialRequest;
import com.hq.ecmp.mscore.dto.ApplyTravelRequest;
import com.hq.ecmp.mscore.dto.JourneyCommitApplyDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.vo.AddressVO;
import com.hq.ecmp.mscore.vo.TravelPickupCity;
import com.hq.ecmp.mscore.vo.TravelRequest;
import com.hq.ecmp.mscore.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


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
    public void applytravliCommit(ApplyTravelRequest travelCommitApply) {
        //1.保存乘客行程信息 journey_info表
        JourneyInfo journeyInfo = new JourneyInfo();
        //提交差旅行程表信息
        journeyInfoTravelCommit(travelCommitApply, journeyInfo);
        Long journeyId = journeyInfo.getJourneyId();

        //2.保存申请信息 apply_info表
        ApplyInfo applyInfo = new ApplyInfo();
        applyInfoTravelCommit(travelCommitApply, journeyId, applyInfo);

        //3.保存行程节点信息(差旅相关) journey_node_info表
        JourneyNodeInfo journeyNodeInfo = null;
        // TODO 没有往返的情况下。有往返则两个行程节点，一个去程，一个返程
        List<TravelRequest> travelRequests = travelCommitApply.getTravelRequests();
        int i = 1;
        for (TravelRequest travelRequest : travelRequests) {
            journeyNodeInfo = new JourneyNodeInfo();
            //设置差旅行程节点表信息
            SetTravelJourneyNode(travelCommitApply, journeyId, journeyNodeInfo, i, travelRequest);
            i++;
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
        }
        //如果有往返，则创建返程节点。返程 出发地为最初目的地，返程目的地为最初出发地。返程开始时间为去程结束时间
        if("Y000".equals(travelCommitApply.getIsGoBack())) {
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
            SetTravelJourneyNode(applyTravelRequest, journeyId, journeyNodeInfo, i + 1, travelRequest);
            //新增返程节点
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
        }


        //4.保存行程乘客信息 journey_passenger_info表
        // 差旅只有乘车人，没有同行人
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        // 提交差旅乘客信息表
        journeyPassengerInfoCommit(travelCommitApply, journeyId, journeyPassengerInfo);

    }

    /**
     * 设置差旅行程节点表信息
     * @param travelCommitApply
     * @param journeyId
     * @param journeyNodeInfo
     * @param i
     * @param travelRequest
     */
    private void SetTravelJourneyNode(ApplyTravelRequest travelCommitApply, Long journeyId, JourneyNodeInfo journeyNodeInfo, int i, TravelRequest travelRequest) {
        //3.1 journey_id 非空
        journeyNodeInfo.setJourneyId(journeyId);
        //3.2 user_id 行程申请人 编号
        journeyNodeInfo.setUserId(Long.valueOf(travelCommitApply.getApplyUser().getUserId())); //TODO 判空
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
        journeyNodeInfo.setNodeState("P000");  // TODO 申请通过前节点算是有效吗？待确认
        ///3.17 number 节点在 在整个行程 中的顺序编号 从  1  开始
        journeyNodeInfo.setNumber(i);
        //3.18 create_by 创建者
        journeyNodeInfo.setCreateBy(String.valueOf(travelCommitApply.getApplyUser().getUserId()));
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
    public List<ApplyInfoDTO> selectApplyInfoListByPage(Long userId, Integer pageNum, Integer pageSize) {
        //分页查询申请列表
        PageHelper.startPage(pageNum,pageSize);
        List<ApplyInfoDTO> all = applyInfoMapper.selectApplyInfoListByPage(userId);
        return all;
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
        journeyPassengerInfo.setMobile(travelCommitApply.getProjectNumber());
        //4.4 it_is_peer 是否是同行者 00   是   01   否
        journeyPassengerInfo.setItIsPeer("01");   // TODO 常量 差旅没有同行人
        //4.5 create_by 创建者
        journeyPassengerInfo.setCreateBy(String.valueOf(travelCommitApply.getApplyUser().getUserId()));
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
        List<UserVO> approvers = travelCommitApply.getApprovers();
        String approversName = null;
        if(approvers.size() == 1){
            approversName = approvers.get(0).getUserName();
        }else if (approvers.size()>1){
            for (int i = 0; i < 2; i++) {
                approversName = approvers.get(0).getUserName() +"、"+ approvers.get(1).getUserName();
            }
        }
        applyInfo.setApproverName(approversName);
        //2.6 cost_center 成本中心 从组织机构表 中获取
        applyInfo.setCostCenter(Long.valueOf(travelCommitApply.getCostCenter())); //TODO 判空
        //2.7 state 申请审批状态 S001  申请中 S002  通过 S003  驳回 S004  已撤销
        applyInfo.setState("S001"); //TODO 枚举
        //2.8 reason 行程原因
        applyInfo.setReason(travelCommitApply.getReason());
        //2.9 create_by 创建者
        applyInfo.setCreateBy(String.valueOf(travelCommitApply.getApplyUser().getUserId()));
        //2.10 create_time 创建时间
        applyInfo.setCreateTime(new Date());
        //2.11 update_by 更新者
        applyInfo.setUpdateBy(null);
        //2.12 update_time 更新时间
        applyInfo.setUpdateTime(null);

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
        journeyInfo.setUseCarTime(String.valueOf(travelCommitApply.getTravelRequests().get(0).getStartDate()));  //TODO 用车时间
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
        journeyInfo.setCreateBy(String.valueOf(travelCommitApply.getApplyUser().getUserId()));
        //1.14 create_time 创建时间
        journeyInfo.setCreateTime(new Date());
        //1.15 update_by 更新者
        journeyInfo.setUpdateBy(null);
        //1.16 update_time 更新时间
        journeyInfo.setUpdateTime(null);
        journeyInfo.setStartDate(travelCommitApply.getStartDate());       //TODO 新增 出差开始时间  需要前端提供
        journeyInfo.setEndDate(travelCommitApply.getEndDate());        //TODO 新增 出差最终结束时间  需要前端提供
        List<TravelPickupCity> travelPickupCityList = travelCommitApply.getTravelPickupCity();
        String travelPickupCityStr  = JSONArray.toJSON(travelCommitApply).toString();
        journeyInfo.setTravelPickupCity(travelPickupCityStr);  //TODO 新增 出差需接送机城市数组  需要前端提供
        journeyInfo.setTravelCitiesStr(travelCommitApply.getTravelCitiesStr());  //TODO 新增 出差需市内用车城市  需要前端提供
        journeyInfo.setPickupTimes(travelCommitApply.getPickupTimes());    // TODO 新增 出差接送机总次数  需要前端提供
        String firstCityName = travelCommitApply.getTravelRequests().get(0).getStartCity().getCityName();
        journeyInfo.setTitle(firstCityName+"-"+travelCommitApply.getTravelCitiesStr());  // TODO 新增 出差标题  需要前端提供    北京-上海、南京。需要判断
        journeyInfoMapper.insertJourneyInfo(journeyInfo);
    }

    /**
     * 提交公务行程申请
     * @param officialCommitApply
     */
    @Override
    @Transactional
    public void applyOfficialCommit(ApplyOfficialRequest officialCommitApply) {

        //1.保存乘客行程信息 journey_info表
        JourneyInfo journeyInfo = new JourneyInfo();
        //提交公务行程表信息
        journeyOfficialCommit(officialCommitApply, journeyInfo);
        Long journeyId = journeyInfo.getJourneyId();

        //2.保存申请信息 apply_info表
        ApplyInfo applyInfo = new ApplyInfo();
        //提交公务申请表信息
        applyInfoOfficialCommit(officialCommitApply, journeyId, applyInfo);

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
        int size = passedAddressList.size();
        //节点编号
        int n = 1;
        //如果没有途经点 就只有一个行程节点
        if(size == 0){
            journeyNodeInfo = new JourneyNodeInfo();
            //设置行程节点信息表
            journeyNodeOfficialCommit(applyOfficialRequest, journeyId, journeyNodeInfo);
            //判断是否途经点
            journeyNodeInfo.setItIsViaPoint("NO");  // TODO 常量
            //节点编号
            journeyNodeInfo.setNumber(n);
            //保存数据
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
        }else {
            //A...提交第一个节点 把第一个途经点作为目的地
            journeyNodeInfo = new JourneyNodeInfo();
            AddressVO firstEndAddr = passedAddressList.get(0);
            applyOfficialRequest.setEndAddr(firstEndAddr);
            //设置行程节点信息表
            journeyNodeOfficialCommit(applyOfficialRequest, journeyId, journeyNodeInfo);
            //判断是否途经点
            journeyNodeInfo.setItIsViaPoint("YES");   //TODO 常量
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
            //判断是否途经点
            journeyNodeInfo.setItIsViaPoint("NO");  //TODO 常量
            //节点编号
            journeyNodeInfo.setNumber(n+size);
            //保存数据
            journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
            for (int i = 0; i < size - 1; i++) {
                journeyNodeInfo = new JourneyNodeInfo();
                //C...提交第 n 个节点， 第 n 个节点的起点是 第 n-1个途径地，目的地是 第 n 个途径地
                AddressVO setoutAddress = passedAddressList.get(i);
                applyOfficialRequest.setStartAddr(setoutAddress);
                AddressVO endAddr = passedAddressList.get(i+1);
                applyOfficialRequest.setEndAddr(endAddr);
                //设置行程节点信息表
                journeyNodeOfficialCommit(applyOfficialRequest, journeyId, journeyNodeInfo);
                //判断是否途经点
                journeyNodeInfo.setItIsViaPoint("YES");  // TODO 常量
                //设置节点编号
                journeyNodeInfo.setNumber(n+i+1);
                //保存数据
                journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
            }
            //如果有往返，追加一个返程节点。
            if("Y000".equals(officialCommitApply.getIsGoBack())) {
                journeyNodeInfo = new JourneyNodeInfo();
                //出发地是目的地，返回地是起始地
                applyOfficialRequest.setStartAddr(officialCommitApply.getEndAddr());
                applyOfficialRequest.setEndAddr(officialCommitApply.getStartAddr());
                //设置行程节点信息表
                journeyNodeOfficialCommit(applyOfficialRequest, journeyId, journeyNodeInfo);
                //判断是否途经点
                journeyNodeInfo.setItIsViaPoint("NO");  // TODO 常量
                //设置节点编号
                journeyNodeInfo.setNumber(n+size+1);
                //保存数据
                journeyNodeInfoMapper.insertJourneyNodeInfo(journeyNodeInfo);
            }
        }

        //4.保存行程乘客信息 journey_passenger_info表
        JourneyPassengerInfo journeyPassengerInfo = new JourneyPassengerInfo();
        journeyPassergerOfficialCommit(officialCommitApply, journeyId, journeyPassengerInfo);
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
        journeyPassengerInfo.setItIsPeer("01");   // TODO 新建枚举
        //4.5 create_by 创建者
        journeyPassengerInfo.setCreateBy(String.valueOf(officialCommitApply.getApplyUser().getUserId()));
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
                journeyPartner.setItIsPeer("00");  //TODO 枚举
                journeyPartner.setCreateBy(String.valueOf(officialCommitApply.getApplyUser().getUserId()));
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
        journeyNodeInfo.setPlanBeginCityCode(null);
        //3.4 plan_end_address 计划下车地址    非空
        journeyNodeInfo.setPlanEndAddress(officialCommitApply.getEndAddr().getAddress());
        journeyNodeInfo.setPlanEndLongAddress(officialCommitApply.getEndAddr().getLongAddress());
        journeyNodeInfo.setPlanEndCityCode(null);
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
        journeyNodeInfo.setNodeState("P000");   // TODO 枚举
        ///3.17 number 节点在 在整个行程 中的顺序编号 从  1  开始
        journeyNodeInfo.setNumber(null);    // TODO 节点编号单独判断
        //3.18 create_by 创建者
        journeyNodeInfo.setCreateBy(String.valueOf(officialCommitApply.getApplyUser().getUserId()));  //TODO 申请人 和 创建人
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
        applyInfo.setApplyType(String.valueOf(officialCommitApply.getApplyType())); //TODO 跟用车制度id重合了
        //2.5 approver_name 第一审批阶段 审批人列表，前两位
        List<UserVO> approvers = officialCommitApply.getApprovers();
        if(approvers == null){
            applyInfo.setApproverName(null);
        }else {
            String approversName = null;
            if(approvers.size() == 1){
                approversName = approvers.get(0).getUserName();
            }else if (approvers.size()>1){
                for (int i = 0; i < 2; i++) {
                    approversName = approvers.get(0).getUserName() +"、"+ approvers.get(1).getUserName();
                }
            }
            applyInfo.setApproverName(approversName);
        }
        //2.6 cost_center 成本中心 从组织机构表 中获取
        applyInfo.setCostCenter(Long.valueOf(officialCommitApply.getCostCenter())); //TODO 要判空
        //2.7 state 申请审批状态 S001  申请中 S002  通过 S003  驳回 S004  已撤销
        applyInfo.setState("S001");  // TODO 定义枚举
        //2.8 reason 行程原因
        applyInfo.setReason(officialCommitApply.getReason());
        //2.9 create_by 创建者
        applyInfo.setCreateBy(String.valueOf(officialCommitApply.getApplyUser().getUserId())); //TODO 创建者 与 申请人区别
        //2.10 create_time 创建时间
        applyInfo.setCreateTime(new Date());
        //2.11 update_by 更新者
        applyInfo.setUpdateBy(null);
        //2.12 update_time 更新时间
        applyInfo.setUpdateTime(null);

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");  //TODO 时间方法改动下
        String date = dateFormat.format(applyDate);
        journeyInfo.setUseCarTime(date);
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
        journeyInfo.setCharterCarType(String.valueOf(officialCommitApply.getCharterType()));
        //1.13 create_by 创建者
        journeyInfo.setCreateBy(String.valueOf(officialCommitApply.getApplyUser().getUserId()));  //TODO 数据库中是int
        //1.14 create_time 创建时间
        journeyInfo.setCreateTime(new Date());
        //1.15 update_by 更新者
        journeyInfo.setUpdateBy(null);
        //1.16 update_time 更新时间
        journeyInfo.setUpdateBy(null);
        journeyInfo.setStartDate(officialCommitApply.getApplyDate());       //TODO 新增 行程开始时间，用车时间
        journeyInfo.setEndDate(null);        //TODO 新增 公务行程结束时间，未知。也用不着
        journeyInfo.setTravelPickupCity(null);  //TODO 新增 出差需接送机城市为空
        journeyInfo.setTravelCitiesStr(null);  //TODO 新增 出差市内用车城市为空
        journeyInfo.setPickupTimes(null);    // TODO 新增 出差接送机总次数为空
        journeyInfo.setTitle(officialCommitApply.getReason());  // TODO 新增 公务title为reason
        journeyInfoMapper.insertJourneyInfo(journeyInfo);
    }

}
