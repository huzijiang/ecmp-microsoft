package com.hq.ecmp.mscore.service.impl;

import com.hq.api.system.domain.SysDept;
import com.hq.api.system.service.ISysDeptService;
import com.hq.api.system.service.ISysUserService;
import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderSettlingInfo;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.StatisticsRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsRankingServiceImpl implements StatisticsRankingService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private CarInfoMapper carInfoMapper;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;
    @Autowired
    private CarGroupDriverRelationMapper carGroupDriverRelationMapper;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private OrderSettlingInfoMapper orderSettlingInfoMapper;

    @Override
    public ApiResponse ranking(StatisticsParam statisticsParam) {
        //公司或部门列表
        SysDept sysDept = new SysDept();
        sysDept.setDeptType(String.valueOf(statisticsParam.getType()));
        List<SysDept> list = sysDeptService.selectDeptList(sysDept);
        Map<String,Map<String,Integer>> map = list.stream().collect(Collectors.toMap(SysDept::getDeptName, x->{
            Map costData = new HashMap();
            costData.put("order",0);
            costData.put("orderHave",0);
            costData.put("orderNet",0);
            costData.put("cost",0);
            costData.put("car",0);
            costData.put("driver",0);
            return costData;
        },(k1,k2)->k2,LinkedHashMap::new));
        //完单
        List<OrderInfo> orderInfos = orderInfoMapper.selectOrderEnd();
        //网约车订单
        OrderInfo orderInfo = new OrderInfo();orderInfo.setUseCarMode(CarConstant.USR_CARD_MODE_NET);
        List<OrderInfo> orderInfosNet = orderInfoMapper.selectOrderInfoList(orderInfo);
        //司机
        List<DriverInfo> driverInfos = driverInfoMapper.selectDriverInfoList(null);
        //车辆
        List<CarInfo> carInfos = carInfoMapper.selectCarInfoList(null);
        //完单量统计
        orderInfos.stream().forEach(x->{
            String name = getTopDept(statisticsParam.getType(),x.getUserId());
            map.get(name).put("order",map.get(name).get("order")+1);
            if(x.getUseCarMode().equals(CarConstant.USR_CARD_MODE_HAVE)){
                map.get(name).put("orderHave",map.get(name).get("orderHave")+1);
            }
            if(x.getUseCarMode().equals(CarConstant.USR_CARD_MODE_NET)){
                map.get(name).put("orderNet",map.get(name).get("orderNet")+1);
            }
        });
        //成本
        orderInfosNet.stream().forEach(x->{
            String name = getTopDept(statisticsParam.getType(),x.getUserId());
            OrderSettlingInfo orderSettlingInfo = orderSettlingInfoMapper.selectOrderSettlingInfoByOrderId(x.getOrderId());
            double cost = orderSettlingInfo==null?0.0:orderSettlingInfo.getAmount()==null?0.0:orderSettlingInfo.getAmount().doubleValue();
            map.get(name).put("cost",map.get(name).get("cost")+(int) Math.ceil(cost));
        });
        if(statisticsParam.getType()==1){
            //司机
            driverInfos.stream().forEach(x->{
                SysDept temp =  sysDeptService.selectDeptById(carGroupInfoMapper.selectCarGroupInfoById(carGroupDriverRelationMapper.selectCarGroupDriverRelationById(x.getDriverId()).getCarGroupId()).getCompanyId());
                map.get(temp.getDeptName()).put("driver",map.get(temp.getDeptName()).get("driver")+1);
            });
            //车辆
            carInfos.stream().forEach(x->{
                SysDept temp =  sysDeptService.selectDeptById(carGroupInfoMapper.selectCarGroupInfoById(x.getCarGroupId()).getCompanyId());
                map.get(temp.getDeptName()).put("car",map.get(temp.getDeptName()).get("car")+1);
            });
        }else{
            //员工司机
            driverInfos.stream().forEach(x->{
                if(x.getUserId()!=null){
                    String name = getTopDept(statisticsParam.getType(),x.getUserId());
                    map.get(name).put("driver",map.get(name).get("driver")+1);
                }
            });
        }
        Map resultMap = new LinkedHashMap();
        resultMap.put("order",rankingMap("order",map));
        resultMap.put("cost",rankingMap("cost",map));
        resultMap.put("car",rankingMap("car",map));
        resultMap.put("driver",rankingMap("driver",map));
        return ApiResponse.success(resultMap);
    }
    private String getTopDept(int type,Long userId){
        Long deptId = sysUserService.selectUserById(userId).getDeptId();
        String name = "";
        if(type==1){
            SysDept temp =  sysDeptService.findCompany(deptId,2);
            name = temp.getDeptName();
        }else{
            boolean parentIsCompany = false;
            while (!parentIsCompany){
                SysDept sysDept1 = sysDeptService.selectDeptById(deptId);
                if(sysDept1.getDeptType().equals("2")){
                    name = sysDept1.getDeptName();
                    deptId = sysDept1.getParentId();
                }else {
                    parentIsCompany = true;
                }
            }
        }
        return name;
    }
    private Map rankingMap(String key,Map<String,Map<String,Integer>> map){
        Map resultMap = new LinkedHashMap();
        List<Map> mapList = new ArrayList<>();
        for (String tempKey: map.keySet()) {
            int tempInt =  map.get(tempKey).get(key);
            Map map1 = new HashMap();
            if(key.equals("order")){
                map1.put(tempKey,map.get(tempKey));
            }else {
                map1.put(tempKey,tempInt);
            }
            mapList.add(map1);
        }
        Collections.sort(mapList ,new Comparator<Map>() {
            @Override
            public int compare(Map o1, Map o2) {
                if(key.equals("order")){
                    Integer o1Value = Integer.parseInt(((Map)o1.values().toArray()[0]).get(key).toString());
                    Integer o2Value = Integer.parseInt(((Map)o2.values().toArray()[0]).get(key).toString());
                    return o2Value.compareTo(o1Value);
                }else {
                    Integer o1Value = Integer.parseInt(o1.values().toArray()[0].toString());
                    Integer o2Value = Integer.parseInt(o2.values().toArray()[0].toString());
                    return o2Value.compareTo(o1Value);
                }
            }
        });
        for (int i=0;i<5;i++){
            if(i<mapList.size()){
                resultMap.putAll(mapList.get(i));
            }
        }
        return resultMap;
    }
}
