package com.hq.ecmp.mscore.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CollectionQuittanceEnum;
import com.hq.ecmp.mscore.domain.ReckoningInfo;
import com.hq.ecmp.mscore.dto.MoneyListDto;
import com.hq.ecmp.mscore.dto.PayeeInfoDto;
import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.dto.lease.LeaseSettlementDto;
import com.hq.ecmp.mscore.mapper.CarGroupInfoMapper;
import com.hq.ecmp.mscore.mapper.CollectionQuittanceInfoMapper;
import com.hq.ecmp.mscore.service.CollectionQuittanceInfoService;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
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


    @Autowired
    private ICarGroupInfoService icarGroupInfoService;
    /**
     * 添加收款信息
     * @param param
     */
    @Override
    public void addReckoning(ReckoningInfo param) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        param.setCreateBy(userId);
        Long companyId = loginUser.getUser().getDept().getCompanyId();
        param.setCompanyId(companyId);
        Long carGroupId = icarGroupInfoService.findgroupIdByUserId(userId);
        param.setCarGroupId(carGroupId);
        param.setApplicant(userId);
        Long count = collectionService.findByCollectionNumber(param.getCollectionNumber());
        if(count == 0){
            collectionService.add(param);
        }else {
            log.info("数据存在改为更新CollectionNumber为：{}",param.getCollectionNumber());
            collectionService.updateByCollectionId(param);
        }

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

        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        param.setUserId(loginUser.getUser().getUserId());
        Map<String, Object>  resultMap = new HashMap<>();
        Map<Object, Object> allMap = new HashMap<>();
        /**车队详情 */

        PayeeInfoDto payeeInfo;
        if(null != param.getCollectionNumber() && param.getCollectionNumber() > 0){
                LeaseSettlementDto collectionNumber = collectionService.getCollectionNumber(param.getCollectionNumber());
                payeeInfo =  orderInfoService.getCarGroupInfo(collectionNumber.getCarGroupId());
                param.setCarGroupId(Long.parseLong(collectionNumber.getCarGroupId()));
                payeeInfo.setCollectionId(String.valueOf(collectionNumber.getCollectionNumber()));
                payeeInfo.setUserId(loginUser.getUser().getUserId());
        }else {
                payeeInfo = orderInfoService.getPayeeInfo(param);
                if(null != payeeInfo) {
                    payeeInfo.setUserId(loginUser.getUser().getUserId());
                    param.setCarGroupId(payeeInfo.getCarGroupId());
                    payeeInfo.setCollectionId(getRandomFileName());
                }
        }
        /** 用车费用 详情列表 */
        List<MoneyListDto> moneyList = orderInfoService.getMoneyList(param);

        if(null == moneyList){
            return null;
        }
        /** 返回没有 收款 的月份集合 */
        List<String> dateList = carGroupInfoMapper.reckoningDetail(param);

        List<Map<String,Object>> carTypeMapList = new ArrayList<>();//根据车类型统计用车天数 费用
        if(null != moneyList && moneyList.size() > 0){
            Map dateMap = null;
            for (int i = 0; i < moneyList.size(); i++) {
                MoneyListDto money = moneyList.get(i);

                /** 用车费用 */
                BigDecimal amountDetai = BigDecimal.ZERO;//用车费用
                String amountDetaiList = money.getAmountDetai();//用车费用
                JSONObject objects = JSONObject.parseObject(amountDetaiList);
                if(null != objects){
                    JSONArray otherCostList = objects.getJSONArray("otherCost");
                    if(null != otherCostList && otherCostList.size() > 0){
                        for (int j = 0; j < otherCostList.size(); j++) {
                            JSONObject jsonObject = otherCostList.getJSONObject(j);
                            BigDecimal cost = jsonObject.getBigDecimal("cost");
                            amountDetai = amountDetai.add(cost);
                        }
                    }
                }
                money.setCarTotalMoney(amountDetai);

                /** 其他总费用 */
                BigDecimal outPrice = BigDecimal.ZERO;//其他总费用
                String outPriceList = money.getOutPrice();//其他总费用
                JSONObject outPriceJson = JSONObject.parseObject(outPriceList);
                if(null != outPriceJson){
                    JSONArray otherCostList = outPriceJson.getJSONArray("otherCost");
                    if(null != otherCostList && otherCostList.size() > 0){
                        for (int j = 0; j < otherCostList.size(); j++) {
                            JSONObject jsonObject = otherCostList.getJSONObject(j);
                            BigDecimal cost = jsonObject.getBigDecimal("cost");
                            outPrice = outPrice.add(cost);
                        }
                    }
                }
                money.setOtherTotalMoney(outPrice);
                money.setAmount(money.getCarTotalMoney().add(money.getOtherTotalMoney()));

                if(null == carTypeMapList || carTypeMapList.size() == 0){
                    Map<String,Object> map = new HashMap();
                        map.put("carLevel",money.getCarLevel());
                        map.put("carType",money.getCarType());
                            Map dayAndMoney = new HashMap();
                                dayAndMoney.put("dayNum",money.getUseTime());
                                dayAndMoney.put("moneyNum",money.getAmount());
                    map.put("data",dayAndMoney);
                    carTypeMapList.add(map);
                }else {
                    for (int j = 0; j < carTypeMapList.size() ; j++) {
                        Map<String,Object> carTypeMap = carTypeMapList.get(j);
                        String carLevel = String.valueOf(carTypeMap.get("carLevel"));
                        if(carLevel.equals(money.getCarLevel())){
                            dateMap = (Map) carTypeMap.get("data");
                            Double day = dateMap.get("dayNum") == null ? 0.00: Double.parseDouble((dateMap.get("dayNum").toString()));
                            BigDecimal moneyNum = dateMap.get("moneyNum")== null ? BigDecimal.ZERO : new BigDecimal(dateMap.get("moneyNum").toString());
                            if(null != money.getUseTime() && !StringUtils.isEmpty(money.getUseTime())){
                                Double day1 = Double.valueOf(money.getUseTime());
                                dateMap.put("dayNum",day+day1);
                            }
                            if(null != money.getAmount()){
                                BigDecimal moneyNum1 = money.getAmount();
                                dateMap.put("moneyNum",moneyNum.add(moneyNum1));
                            }
                            continue;
                        }
                    }
                    /*Map<String,Object> map = new HashMap();
                    map.put("carLevel",money.getCarLevel());
                    map.put("carType",money.getCarType());
                    Map dayAndMoney = new HashMap();
                    dayAndMoney.put("dayNum",money.getUseTime());//
                    dayAndMoney.put("moneyNum",money.getAmount());
                    map.put("data",dayAndMoney);
                    carTypeMapList.add(map);*/
                }
            }
            //carTypeMapList.add(dateMap);//根绝车辆类型分组
            Double dayAll = 0.0;
            BigDecimal moneyAll = BigDecimal.ZERO;
            if(null != carTypeMapList && carTypeMapList.size() > 0){
                for(Map<String,Object>  countMap: carTypeMapList){
                    Map data = (Map)countMap.get("data");
                    if(null != data){
                        dayAll += Double.parseDouble(data.get("dayNum").toString());
                        Object moneyNum = data.get("moneyNum");
                        BigDecimal moneyNumBigDecimal = BigDecimal.ZERO;
                        if (null != moneyNum) {
                            moneyNumBigDecimal = (BigDecimal)moneyNum;
                        }
                        moneyAll = moneyAll.add(moneyNumBigDecimal);
                    }
                }
            }
            allMap.put("dayAll",dayAll);
            allMap.put("moneyAll",moneyAll);
        }
        resultMap.put("payeeInfo",payeeInfo);//收款详情 -银行卡
        resultMap.put("dateList",dateList);//收款 的月份集合
        resultMap.put("moneyList",moneyList);//用车费用详情列表
        resultMap.put("carTypeMapList",carTypeMapList);//车类型-统计用车天数 费用
        resultMap.put("countDayAndMoney",allMap);      //车类型-费用天数总计
        return resultMap;
    }


    /**
     * 收款下载
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> downloadReckoning(ReckoningInfo param) {
        ReckoningDto reckoningDto = new ReckoningDto();
        reckoningDto.setStartDate(DateUtils.formatDate(param.getBeginDate(),DateUtils.YYYY_MM_DD));
        reckoningDto.setEndDate(DateUtils.formatDate(param.getEndDate(),DateUtils.YYYY_MM_DD));
        reckoningDto.setCompanyId(param.getCompanyId());
        reckoningDto.setCollectionNumber(param.getCollectionNumber());
//        String collectionEndTime = DateUtils.formatDate(param.getCollectionEndTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
        Map<String, Object> stringObjectMap = reckoningDetail(reckoningDto);
//        stringObjectMap.put("collectionEndTime",collectionEndTime);
        stringObjectMap.remove("moneyList");
        return stringObjectMap;
    }

    @Override
    public boolean existReckoning(ReckoningInfo param) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        param.setCreateBy(userId);
        Long companyId = loginUser.getUser().getDept().getCompanyId();
        param.setCompanyId(companyId);
        Long carGroupId = icarGroupInfoService.findgroupIdByUserId(userId);
        param.setCarGroupId(carGroupId);
        param.setApplicant(userId);

        Long count = collectionService.existReckoning(param);
        if(count == 0){
            return false;
        }
        return true;
    }


    //年月日加四位随机数
    public String getRandomFileName(){
        String str = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int rannum = (int) (new Random().nextDouble() * (9999 - 1000 + 1)) + 1000;// 获取5位随机数
        return str+rannum;// 

    }
}
