package com.hq.ecmp.mscore.service.impl;

import com.hq.api.system.domain.SysDept;
import com.hq.api.system.mapper.SysDeptMapper;
import com.hq.api.system.service.ISysDeptService;
import com.hq.api.system.service.ISysUserService;
import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrderServiceType;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.SceneInfo;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.mapper.ChinaProvinceMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.mapper.SceneInfoMapper;
import com.hq.ecmp.mscore.service.StatisticsOrderService;
import com.hq.ecmp.mscore.vo.ProvinceVO;
import com.hq.ecmp.util.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class StatisticsOrderServiceImpl implements StatisticsOrderService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ISysDeptService iSysDeptService;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private SceneInfoMapper sceneInfoMapper;
    @Autowired
    private ChinaProvinceMapper chinaProvinceMapper;

    @Override
    public ApiResponse orderEndOfCarModelOfServiceType(StatisticsParam statisticsParam) {
        List<String> list = DateFormatUtils.sliceUpDateRange(statisticsParam.getBeginDate(),statisticsParam.getEndDate());
        Map<String,Map<String,Integer>> resultMap = new LinkedHashMap<>();
        //先全部填空数据
        list.stream().forEach(x->resultMap.put(x,getInitMapData(statisticsParam.getType())));
        baseDataFilter((orderInfo)->{
            Map<String,Integer> mapData = getInitMapData(statisticsParam.getType());
            if(statisticsParam.getType()==0){
                mapData.put("all",1);
                //自有，网约
                if(orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
                    mapData.put("have",1);
                }else {
                    mapData.put("net",1);
                }
            }
            if(statisticsParam.getType()==1){
                //预约
                if(orderInfo.getServiceType().equals(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState())){
                    mapData.put("appointment",1);
                    if(orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
                        mapData.put("have_appointment",1);
                    }else {
                        mapData.put("net_appointment",1);
                    }
                }
                //接机
                if(orderInfo.getServiceType().equals(OrderServiceType.ORDER_SERVICE_TYPE_PICK_UP.getBcState())){
                    mapData.put("pick_up",1);
                    if(orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
                        mapData.put("have_pick_up",1);
                    }else {
                        mapData.put("net_pick_up",1);
                    }
                }
                //送机
                if(orderInfo.getServiceType().equals(OrderServiceType.ORDER_SERVICE_TYPE_SEND.getBcState())){
                    mapData.put("send",1);
                    if(orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
                        mapData.put("have_send",1);
                    }else {
                        mapData.put("net_send",1);
                    }
                }
                //包车
                if(orderInfo.getServiceType().equals(OrderServiceType.ORDER_SERVICE_TYPE_CHARTERED.getBcState())){
                    mapData.put("chartered",1);
                    if(orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
                        mapData.put("have_chartered",1);
                    }else {
                        mapData.put("net_chartered",1);
                    }
                }
                //即时用车
                if(orderInfo.getServiceType().equals(OrderServiceType.ORDER_SERVICE_TYPE_NOW.getBcState())){
                    mapData.put("now",1);
                    if(orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
                        mapData.put("have_now",1);
                    }else {
                        mapData.put("net_now",1);
                    }
                }
            }
            //获取x轴分组名称更新map数据
            String dateGroup = DateFormatUtils.formatDate(DateFormatUtils.getPattern(statisticsParam.getBeginDate())[0],orderInfo.getCreateTime());
            if(resultMap.containsKey(dateGroup)){
                if(statisticsParam.getType()==0) {
                    resultMap.get(dateGroup).put("all", resultMap.get(dateGroup).get("all") + mapData.get("all"));
                    resultMap.get(dateGroup).put("have", resultMap.get(dateGroup).get("have") + mapData.get("have"));
                    resultMap.get(dateGroup).put("net", resultMap.get(dateGroup).get("net") + mapData.get("net"));
                }
                if(statisticsParam.getType()==1) {
                    resultMap.get(dateGroup).put("appointment", resultMap.get(dateGroup).get("appointment") + mapData.get("appointment"));
                    resultMap.get(dateGroup).put("pick_up", resultMap.get(dateGroup).get("pick_up") + mapData.get("pick_up"));
                    resultMap.get(dateGroup).put("send", resultMap.get(dateGroup).get("send") + mapData.get("send"));
                    resultMap.get(dateGroup).put("chartered", resultMap.get(dateGroup).get("chartered") + mapData.get("chartered"));
                    resultMap.get(dateGroup).put("now", resultMap.get(dateGroup).get("now") + mapData.get("now"));

                    resultMap.get(dateGroup).put("have_appointment", resultMap.get(dateGroup).get("have_appointment") + mapData.get("have_appointment"));
                    resultMap.get(dateGroup).put("have_pick_up", resultMap.get(dateGroup).get("have_pick_up") + mapData.get("have_pick_up"));
                    resultMap.get(dateGroup).put("have_send", resultMap.get(dateGroup).get("have_send") + mapData.get("have_send"));
                    resultMap.get(dateGroup).put("have_chartered", resultMap.get(dateGroup).get("have_chartered") + mapData.get("have_chartered"));
                    resultMap.get(dateGroup).put("have_now", resultMap.get(dateGroup).get("have_now") + mapData.get("have_now"));

                    resultMap.get(dateGroup).put("net_appointment", resultMap.get(dateGroup).get("net_appointment") + mapData.get("net_appointment"));
                    resultMap.get(dateGroup).put("net_pick_up", resultMap.get(dateGroup).get("net_pick_up") + mapData.get("net_pick_up"));
                    resultMap.get(dateGroup).put("net_send", resultMap.get(dateGroup).get("net_send") + mapData.get("net_send"));
                    resultMap.get(dateGroup).put("net_chartered", resultMap.get(dateGroup).get("net_chartered") + mapData.get("net_chartered"));
                    resultMap.get(dateGroup).put("net_now", resultMap.get(dateGroup).get("net_now") + mapData.get("net_now"));
                }
            }
        },statisticsParam);
        return ApiResponse.success(resultMap);
    }

    @Override
    public ApiResponse orderEndOfTimeInterval(StatisticsParam statisticsParam) {
        List<String> list = new ArrayList<>();
        list.add("1-7点");list.add("7-9点");list.add("9-11点");list.add("11-13点");list.add("13-15点");list.add("15-17点");
        list.add("17-19点");list.add("19-21点");list.add("21-23点");list.add("23-次日1点");
        Map<String,Map<String,Integer>> resultMap = new LinkedHashMap();
        //先全部填空数据
        list.stream().forEach(x->resultMap.put(x,getInitMapData(statisticsParam.getType())));
        baseDataFilter((orderInfo)->{
            Map<String,Integer> mapData = getInitMapData(statisticsParam.getType());
            mapData.put("all",1);
            //自有，网约
            if(orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
                mapData.put("have",1);
            }else {
                mapData.put("net",1);
            }
            //获取x轴分组名称更新map数据
            int dateGroup = Integer.parseInt(DateFormatUtils.formatDate(DateFormatUtils.TIME_FORMAT_1,orderInfo.getCreateTime()));
            String dateGroupString="";
            if(dateGroup>=1 && dateGroup<7){
                dateGroupString="1-7点";
            }
            if(dateGroup>=7 && dateGroup<9){
                dateGroupString="7-9点";
            }
            if(dateGroup>=9 && dateGroup<11){
                dateGroupString="9-11点";
            }
            if(dateGroup>=11 && dateGroup<13){
                dateGroupString="11-13点";
            }
            if(dateGroup>=13 && dateGroup<15){
                dateGroupString="13-15点";
            }
            if(dateGroup>=15 && dateGroup<17){
                dateGroupString="15-17点";
            }
            if(dateGroup>=17 && dateGroup<19){
                dateGroupString="17-19点";
            }
            if(dateGroup>=19 && dateGroup<21){
                dateGroupString="19-21点";
            }
            if(dateGroup>=21 && dateGroup<23){
                dateGroupString="21-23点";
            }
            if(dateGroup>=23 || dateGroup<1){
                dateGroupString="23-次日1点";
            }
            if(resultMap.containsKey(dateGroupString)) {
                resultMap.get(dateGroupString).put("all", resultMap.get(dateGroupString).get("all") + mapData.get("all"));
                resultMap.get(dateGroupString).put("have", resultMap.get(dateGroupString).get("have") + mapData.get("have"));
                resultMap.get(dateGroupString).put("net", resultMap.get(dateGroupString).get("net") + mapData.get("net"));
            }
        },statisticsParam);
        return ApiResponse.success(resultMap);
    }

    @Override
    public ApiResponse orderEndOfScene(StatisticsParam statisticsParam) {
        List<SceneInfo> list = sceneInfoMapper.selectAll(null,null);
        Map<String,Map<String,Integer>> resultMap = new LinkedHashMap();
        //先全部填空数据
        list.stream().forEach(x->resultMap.put(x.getName(),getInitMapData(statisticsParam.getType())));
        List<Long> longs = new ArrayList<>();
        baseDataFilter((orderInfo)->{
            Map<String,Integer> mapData = getInitMapData(statisticsParam.getType());
            mapData.put("all",1);
            //自有，网约
            if(orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
                mapData.put("have",1);
            }else {
                mapData.put("net",1);
            }
            //获取x轴分组名称更新map数据
            String dateGroup = sceneInfoMapper.selectSceneInfoById(orderInfoMapper.getSceneByOrder(orderInfo.getJourneyId())).getName();
            if(resultMap.containsKey(dateGroup)) {
                longs.add(orderInfo.getOrderId());
                resultMap.get(dateGroup).put("all", resultMap.get(dateGroup).get("all") + mapData.get("all"));
                resultMap.get(dateGroup).put("have", resultMap.get(dateGroup).get("have") + mapData.get("have"));
                resultMap.get(dateGroup).put("net", resultMap.get(dateGroup).get("net") + mapData.get("net"));
            }
        },statisticsParam);
        return ApiResponse.success(resultMap);
    }

    @Override
    public ApiResponse orderEndOfCity(StatisticsParam statisticsParam) {
        List<ProvinceVO> list = chinaProvinceMapper.queryProvince();
        Map<String,Map<String,Integer>> resultMap = new LinkedHashMap();
        //先全部填空数据
        list.stream().forEach(x->resultMap.put(x.getProvinceName(),getInitMapData(statisticsParam.getType())));
        List<Long> longs = new ArrayList<>();
        baseDataFilter((orderInfo)->{
            Map<String,Integer> mapData = getInitMapData(statisticsParam.getType());
            mapData.put("all",1);
            //自有，网约
            if(orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
                mapData.put("have",1);
            }else {
                mapData.put("net",1);
            }
            //获取x轴分组名称更新map数据
            String dateGroup = orderInfoMapper.getProvinceByOrder(orderInfo.getOrderId());
            if(resultMap.containsKey(dateGroup)) {
                longs.add(orderInfo.getOrderId());
                resultMap.get(dateGroup).put("all", resultMap.get(dateGroup).get("all") + mapData.get("all"));
                resultMap.get(dateGroup).put("have", resultMap.get(dateGroup).get("have") + mapData.get("have"));
                resultMap.get(dateGroup).put("net", resultMap.get(dateGroup).get("net") + mapData.get("net"));
            }
        },statisticsParam);
        return ApiResponse.success(resultMap);
    }

    @Override
    public ApiResponse orderEndOfDept(StatisticsParam statisticsParam) {
        SysDept sysDept = new SysDept();
        sysDept.setParentId(statisticsParam.getDeptIds().get(0));
        sysDept.setDeptType("2");
        sysDept.setCompanyId(statisticsParam.getCompanyId());
        List<SysDept> list = sysDeptMapper.selectDeptList(sysDept);
        Map<String,Map<String,Integer>> resultMap = new LinkedHashMap();
        //先全部填空数据
        list.stream().forEach(x->resultMap.put(x.getDeptName(),getInitMapData(statisticsParam.getType())));
        baseDataFilter((orderInfo)->{
            Map<String,Integer> mapData = getInitMapData(statisticsParam.getType());
            mapData.put("all",1);
            //自有，网约
            if(orderInfo.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
                mapData.put("have",1);
            }else {
                mapData.put("net",1);
            }
            //获取x轴分组名称更新map数据
            Long deptId = iSysUserService.selectUserById(orderInfo.getUserId()).getDeptId();
            boolean parentIsCompany = false;
            String dateGroup = "";
            while (!parentIsCompany){
                SysDept sysDept1 = iSysDeptService.selectDeptById(deptId);
                 if(sysDept1.getDeptType().equals("2")){
                     dateGroup = sysDept1.getDeptName();
                     deptId = sysDept1.getParentId();
                 }else {
                     parentIsCompany = true;
                 }
            }
            if(resultMap.containsKey(dateGroup)) {
                resultMap.get(dateGroup).put("all", resultMap.get(dateGroup).get("all") + mapData.get("all"));
                resultMap.get(dateGroup).put("have", resultMap.get(dateGroup).get("have") + mapData.get("have"));
                resultMap.get(dateGroup).put("net", resultMap.get(dateGroup).get("net") + mapData.get("net"));
            }
        },statisticsParam);
        return ApiResponse.success(resultMap);
    }

    /**
     * 根据类型初始化mapdata数据
     * @param type 0，全部自有网约，1服务类型的全部自有网约
     * @return
     */
    private Map<String,Integer> getInitMapData(int type){
        Map<String,Integer> mapData = new LinkedHashMap<>();
        if(type==0) {
            //订单完单量
            mapData.put("all", 0);
            //自有网约
            mapData.put("have", 0);
            mapData.put("net", 0);
        }
        if(type==1){
            //服务类型all
            mapData.put("appointment",0);
            mapData.put("pick_up",0);
            mapData.put("send",0);
            mapData.put("chartered",0);
            mapData.put("now",0);
            //服务类型自有
            mapData.put("have_appointment",0);
            mapData.put("have_pick_up",0);
            mapData.put("have_send",0);
            mapData.put("have_chartered",0);
            mapData.put("have_now",0);
            //服务类型网约
            mapData.put("net_appointment",0);
            mapData.put("net_pick_up",0);
            mapData.put("net_send",0);
            mapData.put("net_chartered",0);
            mapData.put("net_now",0);
        }
        return mapData;
    }

    /**
     * 基础条件数据过滤
     * @param consumer
     * @param statisticsParam
     */
    public void baseDataFilter(Consumer<OrderInfo> consumer, StatisticsParam statisticsParam){
        List<OrderInfo> orderInfos = orderInfoMapper.selectOrderEnd();
        List<String> listDate = DateFormatUtils.sliceUpDateRange(statisticsParam.getBeginDate(),statisticsParam.getEndDate());
        for (OrderInfo orderInfo: orderInfos) {
            //过滤所选部门
            if (statisticsParam.getDeptIds().contains(iSysDeptService.findCompany(iSysUserService.selectUserById(orderInfo.getUserId()).getDeptId(), 2).getDeptId())) {
                String dateGroupSelect = DateFormatUtils.formatDate(DateFormatUtils.getPattern(statisticsParam.getBeginDate())[0],orderInfo.getCreateTime());
                //过滤所选时间
                if(listDate.contains(dateGroupSelect)){
                    consumer.accept(orderInfo);
                }
            }
        }
    }
}
