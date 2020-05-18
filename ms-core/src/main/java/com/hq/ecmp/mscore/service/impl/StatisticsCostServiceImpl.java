package com.hq.ecmp.mscore.service.impl;

import com.hq.api.system.domain.SysDept;
import com.hq.api.system.mapper.SysDeptMapper;
import com.hq.api.system.service.ISysDeptService;
import com.hq.api.system.service.ISysUserService;
import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.constant.CarModeEnum;
import com.hq.ecmp.mscore.domain.OrderInfo;
import com.hq.ecmp.mscore.domain.OrderSettlingInfo;
import com.hq.ecmp.mscore.dto.statistics.StatisticsParam;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderSettlingInfoMapper;
import com.hq.ecmp.mscore.service.StatisticsCostService;
import com.hq.ecmp.util.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsCostServiceImpl implements StatisticsCostService {
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderSettlingInfoMapper orderSettlingInfoMapper;

    @Override
    public ApiResponse cost(StatisticsParam statisticsParam) {
        //公司或部门集合数据
        List<SysDept> list = getDepts(statisticsParam);
        //封装map
        Map<String,Map<String,Double>> map = list.stream().collect(Collectors.toMap(SysDept::getDeptName,x->{
            Map costData = new HashMap();
            costData.put("have",0.0);
            costData.put("net",0.0);
            return costData;
        },(k1,k2)->k2,LinkedHashMap::new));
        //订单数据
        List<OrderInfo> orderInfos = orderInfoMapper.selectOrderInfoList(null);
        List<String> listDate = DateFormatUtils.sliceUpDateRange(statisticsParam.getBeginDate(),statisticsParam.getEndDate());
        orderInfos.stream().filter(x->x.getUseCarMode()!=null).forEach(x->{
            String dateGroupSelect = DateFormatUtils.formatDate(DateFormatUtils.getPattern(statisticsParam.getBeginDate())[0],x.getCreateTime());
            //过滤所选时间
            if(listDate.contains(dateGroupSelect)){
                //算成本
                OrderSettlingInfo orderSettlingInfo = orderSettlingInfoMapper.selectOrderSettlingInfoByOrderId(x.getOrderId());
                double cost = orderSettlingInfo==null?0.0:orderSettlingInfo.getAmount()==null?0.0:orderSettlingInfo.getAmount().doubleValue();
                if(statisticsParam.getType()==1){
                    String name = sysDeptService.findCompany(sysUserService.selectUserById(x.getUserId()).getDeptId(),2).getDeptName();
                    //自有
                    if(x.getUseCarMode().equals(CarModeEnum.ORDER_MODE_HAVE.getKey())){
                        map.get(name).put("have",map.get(name).get("have")+cost);
                    }
                    //网约
                    if(x.getUseCarMode().equals(CarModeEnum.ORDER_MODE_NET.getKey())){
                        map.get(name).put("net",map.get(name).get("net")+cost);
                    }
                }else{
                    //订单所属一级部门
                    Long deptId = sysUserService.selectUserById(x.getUserId()).getDeptId();
                    boolean parentIsCompany = false;
                    String name = "";
                    while (!parentIsCompany){
                        SysDept sysDept1 = sysDeptService.selectDeptById(deptId);
                        if(sysDept1.getDeptType().equals("2")){
                            name = sysDept1.getDeptName();
                            deptId = sysDept1.getParentId();
                        }else {
                            parentIsCompany = true;
                        }
                    }
                    //自有
                    if(x.getUseCarMode().equals(CarModeEnum.ORDER_MODE_HAVE.getKey())){
                        map.get(name).put("have",map.get(name).get("have")+cost);
                    }
                    //网约
                    if(x.getUseCarMode().equals(CarModeEnum.ORDER_MODE_NET.getKey())){
                        map.get(name).put("net",map.get(name).get("net")+cost);
                    }
                }
            }
        });
        return ApiResponse.success(map);
    }

    /**
     * @param statisticsParam
     * @return
     */
    private List<SysDept> getDepts(StatisticsParam statisticsParam){
        List<SysDept> list = new ArrayList<>();
        if(statisticsParam.getType()==1){
            statisticsParam.getDeptIds().stream().forEach(x->{
                SysDept sysDept = new SysDept();
                sysDept.setDeptId(statisticsParam.getDeptIds().get(0));
                sysDept.setDeptType("1");
                sysDept.setCompanyId(statisticsParam.getCompanyId());
                List<SysDept> tempList = sysDeptService.selectDeptList(sysDept);
                list.addAll(tempList);
                while (tempList.size()>0){
                    List<SysDept> tempList_1 = new ArrayList<>();
                    for (SysDept temp: tempList) {
                        SysDept sysDept_c = new SysDept();
                        sysDept_c.setParentId(temp.getDeptId());
                        sysDept_c.setDeptType("1");
                        sysDept_c.setCompanyId(statisticsParam.getCompanyId());
                        tempList_1.addAll(sysDeptService.selectDeptList(sysDept_c));
                    }
                    list.addAll(tempList_1);
                    tempList.clear();
                    tempList.addAll(tempList_1);
                }
            });
        }else{
            SysDept sysDept = new SysDept();
            sysDept.setParentId(statisticsParam.getDeptIds().get(0));
            sysDept.setDeptType("2");
            sysDept.setCompanyId(statisticsParam.getCompanyId());
            list.addAll(sysDeptMapper.selectDeptList(sysDept));
        }
        return list;
    }
}
