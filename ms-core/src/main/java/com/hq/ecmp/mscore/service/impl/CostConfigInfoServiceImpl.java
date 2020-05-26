package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.StringUtils;
import com.hq.ecmp.constant.CharterTypeEnum;
import com.hq.ecmp.constant.CostConfigModeEnum;
import com.hq.ecmp.constant.OrderServiceType;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.cost.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.CostCalculation;
import com.hq.ecmp.mscore.service.ICostConfigInfoService;
import com.hq.ecmp.mscore.vo.CarGroupCostVO;
import com.hq.ecmp.mscore.vo.CityInfo;
import com.hq.ecmp.mscore.vo.PriceOverviewVO;
import com.hq.ecmp.mscore.vo.SupplementVO;
import com.hq.ecmp.util.SortListUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-05-06
 */
@Service
public class CostConfigInfoServiceImpl implements ICostConfigInfoService
{
    @Resource
    private CostConfigInfoMapper costConfigInfoMapper;
    @Resource
    private CostConfigCityInfoMapper costConfigCityInfoMapper;
    @Resource
    private CostConfigCarTypeInfoMapper costConfigCarTypeInfoMapper;
    @Resource
    private CostConfigCarGroupInfoMapper costConfigCarGroupInfoMapper;
    @Resource
    private EnterpriseCarTypeInfoMapper enterpriseCarTypeInfoMapper;

    /**
     * 通过id查询成本配置信息
     * 
     * @param costId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CostConfigListResult selectCostConfigInfoById(Long costId,Integer cityCode)
    {
        CostConfigListResult costConfigListResult = costConfigInfoMapper.selectCostConfigInfoById(costId,cityCode);
        List<CostConfigCarTypeInfo> costConfigCarTypeInfos = costConfigCarTypeInfoMapper.selectCostConfigCarTypeInfoList( new CostConfigCarTypeInfo(costConfigListResult.getCostId()));
        costConfigListResult.setCarTypes(costConfigCarTypeInfos);
        return costConfigListResult;
    }

    /**
     * 成本中心列表查询
     * @param costConfigQueryDto 成本中心列表查询条件
     * @return List<CostConfigListResult>
     */
    @Override
    public CostConfigListResultPage selectCostConfigInfoList(CostConfigQueryDto costConfigQueryDto)
    {
        CostConfigListResultPage costConfigListResultPage = new CostConfigListResultPage();
        PageHelper.startPage(costConfigQueryDto.getPageNum(), costConfigQueryDto.getPageSize());
        List<CostConfigListResult> costConfigListResults = costConfigInfoMapper.selectCostConfigInfoList(costConfigQueryDto);
        for (CostConfigListResult costConfigListResult: costConfigListResults) {
            List<CostConfigCarTypeInfo> costConfigCarTypeInfos = costConfigCarTypeInfoMapper.selectCostConfigCarTypeInfoList(new CostConfigCarTypeInfo(costConfigListResult.getCostId()));
            costConfigListResult.setCarTypes(costConfigCarTypeInfos);
        }
        int totalNum = costConfigInfoMapper.getTotalNum(costConfigQueryDto);
        costConfigListResultPage.setResults(costConfigListResults);
        costConfigListResultPage.setTotal(totalNum);
        return costConfigListResultPage;
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param costConfigInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCostConfigInfo(CostConfigInfo costConfigInfo)
    {
        costConfigInfo.setCreateTime(DateUtils.getNowDate());
        return costConfigInfoMapper.insertCostConfigInfo(costConfigInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param costConfigInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCostConfigInfo(CostConfigInfo costConfigInfo)
    {
        costConfigInfo.setUpdateTime(DateUtils.getNowDate());
        return costConfigInfoMapper.updateCostConfigInfo(costConfigInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param costIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCostConfigInfoByIds(Long[] costIds)
    {
        return costConfigInfoMapper.deleteCostConfigInfoByIds(costIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param costId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCostConfigInfoById(Long costId)
    {
        return costConfigInfoMapper.deleteCostConfigInfoById(costId);
    }

    /**
     * 录入成本中心配置信息
     * @param costConfigDto 创建成本设置入参model
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCostConfig(CostConfigInsertDto costConfigDto, Long userId) throws Exception {
        CostConfigInfo costConfigInfo = new CostConfigInfo();
        BeanUtils.copyProperties(costConfigDto, costConfigInfo);
        costConfigInfo.setCreateBy(String.valueOf(userId));
        costConfigInfo.setCostId(null);
        ((ICostConfigInfoService) AopContext.currentProxy()).insertCostConfigInfo(costConfigInfo);
        Long costId = costConfigInfo.getCostId();
        if (costId == null) {
            throw new Exception("新增失败");
        }
        costConfigCarTypeInfoMapper.insertCostConfigCarTypeInfoBatch(costConfigDto.getCarTypes(), costId, userId, DateUtils.getNowDate());
        if (!CollectionUtils.isEmpty(costConfigDto.getCities())){
            costConfigCityInfoMapper.insertCostConfigCityInfoBatch(costConfigDto.getCities(),costId,userId,DateUtils.getNowDate());
            costConfigCarGroupInfoMapper.insertCostConfigCarGroupInfoBatch(costConfigDto.getCities(),costId,userId,DateUtils.getNowDate());
        }
    }

    /**
     * 更新成本中心配置信息
     * 1.基础表信息更新
     * 2.车型辅表根据成本配置id删除后，重新插入
     * @param costConfigListResult 成本设置更新入参条件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCostConfig(CostConfigListResult costConfigListResult,Long userId) throws Exception {
        CostConfigInfo costConfigInfo = new CostConfigInfo();
        costConfigInfo.setUpdateBy(String.valueOf(userId));
        BeanUtils.copyProperties(costConfigListResult, costConfigInfo);
        int i = ((ICostConfigInfoService) AopContext.currentProxy()).updateCostConfigInfo(costConfigInfo);
        if(i==0){
            throw new Exception("更新失败");
        }
        Long costId = costConfigListResult.getCostId();
        costConfigCarTypeInfoMapper.deleteCostConfigByCostId(costId);
        costConfigCarGroupInfoMapper.deleteCostConfigCarGroupInfoByCostId(costId);
        costConfigCityInfoMapper.deleteCostConfigCityInfoByCostId(costId);
        CostConfigCarGroupInfo configCarGroupInfo = new CostConfigCarGroupInfo(costId, costConfigListResult.getCarGroupId());
        configCarGroupInfo.setCreateBy(String.valueOf(userId));
        configCarGroupInfo.setCreateTime(DateUtils.getNowDate());
        costConfigCarGroupInfoMapper.insertCostConfigCarGroupInfo(configCarGroupInfo);
        CostConfigCityInfo costConfigCityInfo = new CostConfigCityInfo(costConfigListResult.getCostId(), costConfigListResult.getCityCode(),costConfigListResult.getCityName());
        costConfigCityInfo.setCreateBy(String.valueOf(userId));
        costConfigCityInfo.setCreateTime(DateUtils.getNowDate());
        costConfigCityInfoMapper.insertCostConfigCityInfo(costConfigCityInfo);
        costConfigCarTypeInfoMapper.insertCostConfigCarTypeInfoBatch(costConfigListResult.getCarTypes(), costId,userId ,DateUtils.getNowDate() );
    }

    /**
     * 通过成本城市附表id删除城市相关配置
     * @param costConfigCityId  成本城市附表id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCostConfigByCostCityId(Long costConfigCityId,Long costId,int cityCode) {
        int i = costConfigCityInfoMapper.queryOtherDataByCostIdAndCityCode(costId, cityCode);
        if(i==0){
            costConfigInfoMapper.deleteCostConfigInfoById(costId);
            costConfigCarTypeInfoMapper.deleteCostConfigByCostId(costId);
        }
        costConfigCityInfoMapper.deleteCostConfigCityInfoById(String.valueOf(costConfigCityId));
    }

    /**
     *
     * @param costConfigQueryDto  判重条件
     * @return 重复的城市集合
     */
    @Override
    public List<ValidDoubleDtoResult> checkDoubleByServiceTypeCityCarType(CostConfigQueryDoubleValidDto costConfigQueryDto) {
        List<ValidDoubleDtoResult> result = new ArrayList<>();
        List<CostConfigCityInfo> cities = costConfigQueryDto.getCities();
        for (CostConfigCityInfo costConfigCityInfo: cities) {
            int count = 0;
            for (CostConfigCarTypeInfo costConfigCarTypeInfo: costConfigQueryDto.getCarTypes()) {
                count = costConfigInfoMapper.checkDoubleByServiceTypeCityCarType(
                        costConfigCarTypeInfo.getCarTypeId(),Integer.parseInt(costConfigCityInfo.getCityCode()),costConfigQueryDto.getServiceType(),
                        costConfigQueryDto.getRentType());
                if (count >0){
                    ValidDoubleDtoResult validDoubleDtoResult = ValidDoubleDtoResult.builder().carTypeName(costConfigCarTypeInfo.getCarTypeName())
                            .cityName(costConfigCityInfo.getCityName())
                            .serviceType(costConfigQueryDto.getServiceType())
                            .build();
                    result.add(validDoubleDtoResult);
                }
            }

        }
        return result;
    }

    /**
     *
     * 补单成本计算
     * @param
     * @return
     */
    @Override
    public String supplementAmountCalculation(SupplementVO supplementVO,Long companyId) {
        CostConfigQueryDto costConfigQueryDto = new CostConfigQueryDto();
        //公司id
        costConfigQueryDto.setCompanyId(companyId);
        //城市
        costConfigQueryDto.setCityCode(supplementVO.getCityCode());
        String cityName = costConfigCityInfoMapper.selectCostConfigCity(supplementVO.getCityCode());
        //服务类型
        costConfigQueryDto.setServiceType(OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getBcState());
        //车型级别
        costConfigQueryDto.setCarTypeId(supplementVO.getCarTypeId());
        String carTypeName = costConfigCarTypeInfoMapper.selectCostConfigCarTypeInfo(supplementVO.getCarTypeId());
        List<CostConfigListResult> costConfigListResult = costConfigInfoMapper.selectCostConfigInfoList(costConfigQueryDto);
        if(costConfigListResult.isEmpty()){
            String result=cityName+"-"+OrderServiceType.ORDER_SERVICE_TYPE_APPOINTMENT.getStateName()+"-"+carTypeName+":暂无成本设置,前往配置";
            Map map = new HashMap();
            map.put("amount",result);
            String json= JSON.toJSONString(map);
            return json;
        }
        costConfigQueryDto.setCostId(costConfigListResult.get(0).getCostId());
        CostConfigInfo costConfigInfo = costConfigInfoMapper.selectCostConfigInfo(costConfigQueryDto);
        //计算成本的方法
        CostCalculation calculator = new CostCalculator();
        OrderSettlingInfoVo orderSettlingInfoVo =new OrderSettlingInfoVo();
        //订单总时长
        orderSettlingInfoVo.setTotalTime(supplementVO.getTotalTime());
        //订单总里程
        orderSettlingInfoVo.setTotalMileage(supplementVO.getTotalMileage());
        //订单等待时间
        orderSettlingInfoVo.setWaitingTime(supplementVO.getWaitingTime());
        List<CostConfigInfo> costConfigInfos = new ArrayList<>();
        orderSettlingInfoVo.setServiceType(costConfigInfo.getServiceType());
        costConfigInfos.add(costConfigInfo);
        OrderSettlingInfoVo orderSettlingInfo = calculator.calculator(costConfigInfos, orderSettlingInfoVo);
        Map map = new HashMap();
        List list= new ArrayList();
        //超里程价格
        Map overMileagePrice = new HashMap();
        overMileagePrice.put("cost",orderSettlingInfoVo.getOverMileagePrice().setScale(2,BigDecimal.ROUND_HALF_UP));
        overMileagePrice.put("typeName","超里程价格");
        list.add(overMileagePrice);
        //超时长价格
        Map overtimeLongPrice = new HashMap();
        overtimeLongPrice.put("cost",orderSettlingInfoVo.getOvertimeLongPrice().setScale(2,BigDecimal.ROUND_HALF_UP));
        overtimeLongPrice.put("typeName","超时长价格");
        list.add(overtimeLongPrice);
        //起步价
        Map startingPrice = new HashMap();
        startingPrice.put("cost",orderSettlingInfoVo.getStartingPrice().setScale(2,BigDecimal.ROUND_HALF_UP));
        startingPrice.put("typeName","起步价");
        list.add(startingPrice);
        //等待费
        Map waitingFee = new HashMap();
        waitingFee.put("cost",orderSettlingInfoVo.getWaitingFee().setScale(2,BigDecimal.ROUND_HALF_UP));
        waitingFee.put("typeName","等待费");
        list.add(waitingFee);
        //总金额
        map.put("otherCost",list);
        map.put("amount",orderSettlingInfoVo.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP));
        String json= JSON.toJSONString(map);
        return json;
    }

    /**
     * 成本名稱判重
     * @param configName
     * @return
     */
    @Override
    public Boolean costConfigNameIsDouble(String configName) {
        CostConfigInfo costConfigInfo = new CostConfigInfo();
        costConfigInfo.setCostConfigName(configName);
        List<CostConfigInfo> costConfigInfos = costConfigInfoMapper.selectCostConfigList(costConfigInfo);
        if (costConfigInfos!=null && costConfigInfos.size()>0){
            return true;
        }
        return false;
    }

    /**
     * 校验包车服务模式服务类型判重
     * @param costConfigQueryDto
     * @return
     */
    @Override
    public List<ValidDoubleDtoResult> checkCharteredCost(CostConfigQueryDoubleValidDto costConfigQueryDto) {
        List<CostConfigCarTypeInfo> carTypes = costConfigQueryDto.getCarTypes();
        List<String> carTypeId=null;
        if (!CollectionUtils.isEmpty(carTypes)){
            carTypeId = carTypes.stream().map(p -> p.getCarTypeId().toString()).collect(Collectors.toList());
        }
        List<ValidDoubleDtoResult> list=costConfigInfoMapper.checkCharteredCost(costConfigQueryDto.getCarGroupId(),costConfigQueryDto.getCarGroupUserMode()
                ,costConfigQueryDto.getRentType(),costConfigQueryDto.getCompanyId());
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        for (ValidDoubleDtoResult result:list){
            List<String> newCarTypeId=carTypeId;
            result.setCarGroupUserMode(CostConfigModeEnum.format(result.getCarGroupUserMode()));
            result.setCharterCarType(CharterTypeEnum.format(result.getCharterCarType()));
            if (StringUtils.isNotEmpty(result.getCarTypeIds())){
                List<String> canUseType= Arrays.asList(result.getCarTypeIds().split(","));
                newCarTypeId.retainAll(canUseType);
                if (!CollectionUtils.isEmpty(newCarTypeId)){
                    List<EnterpriseCarTypeInfo> enterpriseCarTypeInfos = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeIds(carTypeId);
                    String collect = enterpriseCarTypeInfos.stream().map(p -> p.getName()).collect(Collectors.joining(",", "", ""));
                    result.setCarTypeName(collect);
                }
            }
        }
        return list;
    }

    @Override
    public List<PriceOverviewVO> getGroupPrice(String cityCode,Long companyId) {
        List<PriceOverviewVO> result=new ArrayList<>();
        List<CarGroupCostVO> list=costConfigCityInfoMapper.findGroupByCity(cityCode,companyId);
        List<CarGroupCostVO> grouplist=costConfigCarGroupInfoMapper.selectGroupByCityCode(cityCode,companyId);
        result.add(new PriceOverviewVO("0",grouplist));
        result.add(new PriceOverviewVO("1",grouplist));
        if (!CollectionUtils.isEmpty(list)){
            result.add(new PriceOverviewVO());
            Map<String, List<CarGroupCostVO>> collect = list.stream().collect(Collectors.groupingBy(o -> o.getRentType() + "_" + o.getCarGroupUserMode() + "_" + o.getCarTypeId(), Collectors.toList()));
            if (!CollectionUtils.isEmpty(collect)){
                for(Map.Entry<String,List<CarGroupCostVO>> map:collect.entrySet()){
                    String key = map.getKey();
                    String[] s = key.split("_");
                    List<CarGroupCostVO> value = map.getValue();
                    PriceOverviewVO vo=new PriceOverviewVO();
                    vo.setRentType(s[0]);
                    vo.setRentTypeStr(CharterTypeEnum.format(s[0]));
                    vo.setCityCode(cityCode);
                    vo.setCityName(value.get(0).getCityName());
                    vo.setCarGroupUserMode(s[1]);
                    vo.setCarGroupUserModeStr(CostConfigModeEnum.format(s[1]));
                    vo.setCarTypeId(s[2]);
                    vo.setCarTypeName(value.get(0).getCarTypeName());
                    SortListUtil.sort(value,"itIsInner",SortListUtil.ASC);
                    vo.setCostList(value);
                    result.add(vo);
                }
                SortListUtil.sort(result,"rentType",SortListUtil.ASC);
            }
        }
       return result;
    }

    @Override
    public List<CityInfo> getCostCityList(Long companyId) {
        return costConfigCityInfoMapper.getCostCityList(companyId);
    }

}