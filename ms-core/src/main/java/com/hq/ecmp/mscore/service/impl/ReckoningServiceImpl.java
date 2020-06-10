package com.hq.ecmp.mscore.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.core.web.domain.server.Sys;
import com.hq.ecmp.constant.CollectionQuittanceEnum;
import com.hq.ecmp.mscore.domain.ReckoningInfo;
import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.mapper.CarGroupInfoMapper;
import com.hq.ecmp.mscore.mapper.CollectionQuittanceInfoMapper;
import com.hq.ecmp.mscore.service.CollectionQuittanceInfoService;
import com.hq.ecmp.mscore.service.IOrderInfoService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@Service
@Slf4j
public class ReckoningServiceImpl implements CollectionQuittanceInfoService {

    @Autowired
    private CollectionQuittanceInfoMapper collectionService;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private IOrderInfoService orderInfoService;
    /**
     * 添加收款信息
     * @param param
     */
    @Override
    public void addReckoning(ReckoningInfo param) {
        collectionService.add(param);
    }

    /**
     * 查询收款信息
     * @param param
     */
    @Override
    public void findReckoning(ReckoningDto param) {

    }

    @Override
    public void updateReckoningStatus(ReckoningDto param) {

        if(param.getStatus().equals("0")){
            param.setStatus(CollectionQuittanceEnum.COLLECTION_DISCARD.getKey());
        }else if(param.getStatus().equals("1")){
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            param.setVerifier(String.valueOf(loginUser.getUser().getUserId()));
            param.setStatus(CollectionQuittanceEnum.COLLECTION_OVER.getKey());
        }
        collectionService.updateReckoningStatus(param);
    }



    @Override
    /**
     * 获取 收款详情  费用结算通知单
     */
    public Map<String, Object> reckoningDetail(ReckoningDto param) {


        Map<String, Object>  resultMap = new HashMap<>();
        Map<Object, Object> allMap = new HashMap<>();
        //返回没有 收款 的月份集合
        List<String> dateList = carGroupInfoMapper.reckoningDetail(param);
        //返回收款详情
        Map payeeInfoList = collectionService.getPayeeInfo(param.getCompanyId());
        payeeInfoList.put("collectionId",getRandomFileName());

        List<Map<String,String>> moneyList = orderInfoService.getMoneyList(param);//用车费用详情列表
        List<Map<String,Object>> carTypeMapList = new ArrayList<>();//根据车类型统计用车天数 费用
        if(null != moneyList && moneyList.size() > 0){
            for (int i = 0; i < moneyList.size(); i++) {
                Map<String,String> money = moneyList.get(i);
                double amount = Double.parseDouble(money.get("amount"));//用车费用
                double amountDetai = 0.00;//其他费用
                String amountDetaiList = money.get("amountDetai");//其他费用
                JSONObject objects = JSONObject.parseObject(amountDetaiList);
                if(null != objects){
                    JSONArray otherCostList = objects.getJSONArray("otherCost");
                    if(null != otherCostList && otherCostList.size() > 0){
                        for (int j = 0; j < otherCostList.size(); j++) {
                            JSONObject jsonObject = otherCostList.getJSONObject(j);
                            Double cost = jsonObject.getDouble("cost");
                            amountDetai += cost;
                        }
                    }
                }
                money.put("totalMoney",String.valueOf(amount + amountDetai));
                if(null == carTypeMapList || carTypeMapList.size() == 0){
                    Map<String,Object> map = new HashMap();
                        map.put("carLevel",money.get("carLevel"));
                        map.put("carType",money.get("carType"));
                            Map dayAndMoney = new HashMap();
                                dayAndMoney.put("dayNum",money.get("useTime"));
                                dayAndMoney.put("moneyNum",money.get("amount"));
                    map.put("data",dayAndMoney);
                    carTypeMapList.add(map);
                }else {
                    for (Map<String,Object> carTypeMap : carTypeMapList){
                        String carLevel = String.valueOf(carTypeMap.get("carLevel"));
                        if(carLevel.equals(money.get("carLevel"))){
                            Map dateMap = (Map) carTypeMap.get("data");
                            Double day = Double.valueOf((String)dateMap.get("dayNum"));
                            Double moneyNum = Double.valueOf((String)dateMap.get("moneyNum"));
                            Double day1 = Double.valueOf(money.get("useTime"));
                            Double moneyNum1 = Double.valueOf(money.get("amount"));
                            dateMap.put("dayNum",day+day1);
                            dateMap.put("moneyNum",moneyNum + moneyNum1);
                            carTypeMapList.add(dateMap);
                            continue;
                        }
                    }
                    Map<String,Object> map = new HashMap();
                    map.put("carLevel",money.get("carLevel"));
                    map.put("carType",money.get("carType"));
                    Map dayAndMoney = new HashMap();
                    dayAndMoney.put("dayNum",money.get("useTime"));
                    dayAndMoney.put("moneyNum",money.get("amount"));
                    map.put("data",dayAndMoney);
                    carTypeMapList.add(map);
                }
            }
            Double dayAll = 0.0;
            Double moneyAll = 0.00;
            if(null != carTypeMapList && carTypeMapList.size() > 0){
                for(Map<String,Object>  countMap: carTypeMapList){
                    Map data = (Map)countMap.get("data ");
                    dayAll += Double.valueOf((String)data.get("dayNum"));
                    moneyAll += Double.valueOf((String)data.get("moneyNum"));
                }
            }
            allMap.put("dayAll",dayAll);
            allMap.put("moneyAll",moneyAll);
        }

        resultMap.put("dateList",dateList);//收款 的月份集合
        resultMap.put("moneyList",moneyList);//用车费用详情列表
        resultMap.put("carTypeMapList",carTypeMapList);//车类型-统计用车天数 费用
        resultMap.put("countDayAndMoney",allMap);      //车类型-费用天数总计
        return resultMap;
    }








    //年月日加四位随机数
    public String getRandomFileName(){
        String str = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int rannum = (int) (new Random().nextDouble() * (9999 - 1000 + 1)) + 1000;// 获取5位随机数
        return str+rannum;// 

    }
}
