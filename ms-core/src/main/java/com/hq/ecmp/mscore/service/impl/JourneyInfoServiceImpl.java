package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hq.ecmp.constant.CarConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.domain.CarAuthorityInfo;
import com.hq.ecmp.mscore.domain.JourneyInfo;
import com.hq.ecmp.mscore.domain.JourneyNodeInfo;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.mapper.JourneyInfoMapper;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IJourneyInfoService;
import com.hq.ecmp.mscore.service.IJourneyNodeInfoService;
import com.hq.ecmp.mscore.service.IJourneyUserCarPowerService;
import com.hq.ecmp.mscore.service.IRegimeInfoService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class JourneyInfoServiceImpl implements IJourneyInfoService
{
    @Autowired
    private JourneyInfoMapper journeyInfoMapper;
    
    @Autowired
    private IRegimeInfoService regimeInfoService;
    
    @Autowired
    private IJourneyNodeInfoService journeyNodeInfoService;
    
    @Autowired
    private IApplyInfoService applyInfoService;
    
    @Autowired
    private IJourneyUserCarPowerService journeyUserCarPowerService;
    

    /**
     * 查询【请填写功能名称】
     *
     * @param journeyId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public JourneyInfo selectJourneyInfoById(Long journeyId)
    {
        return journeyInfoMapper.selectJourneyInfoById(journeyId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<JourneyInfo> selectJourneyInfoList(JourneyInfo journeyInfo)
    {
        return journeyInfoMapper.selectJourneyInfoList(journeyInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertJourneyInfo(JourneyInfo journeyInfo)
    {
        journeyInfo.setCreateTime(DateUtils.getNowDate());
        return journeyInfoMapper.insertJourneyInfo(journeyInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateJourneyInfo(JourneyInfo journeyInfo)
    {
        journeyInfo.setUpdateTime(DateUtils.getNowDate());
        return journeyInfoMapper.updateJourneyInfo(journeyInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param journeyIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyInfoByIds(Long[] journeyIds)
    {
        return journeyInfoMapper.deleteJourneyInfoByIds(journeyIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param journeyId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyInfoById(Long journeyId)
    {
        return journeyInfoMapper.deleteJourneyInfoById(journeyId);
    }

	@Override
	public List<CarAuthorityInfo> getUserCarAuthorityList(Long userId) {
		List<CarAuthorityInfo> carAuthorityInfoList=new ArrayList<>();
		JourneyInfo query = new JourneyInfo();
		query.setUserId(userId);
		List<JourneyInfo> journeyInfoList = selectJourneyInfoList(query);
		if(null !=journeyInfoList && journeyInfoList.size()>0){
			for (JourneyInfo journeyInfo : journeyInfoList) {
				CarAuthorityInfo carAuthorityInfo = new CarAuthorityInfo();
				//获取是差旅还是公务
				RegimeInfo regimeInfo = regimeInfoService.selectRegimeInfoById(journeyInfo.getRegimenId());
				if(null !=regimeInfo){
					carAuthorityInfo.setType(regimeInfo.parseApplyType());
					//公务用车时间
					carAuthorityInfo.setUseDate(journeyInfo.getUseCarTime());
					//公务公车方式
					carAuthorityInfo.setCarType(journeyInfo.getUseCarMode());
					if(CarConstant.USE_CAR_TYPE_TRAVEL.equals(regimeInfo.getApplyType())){
						//差旅类型
						Map<String, Integer> countMap = journeyUserCarPowerService.selectStatusCount(journeyInfo.getJourneyId());
						//统计差旅类型的几种用车类型的剩余次数
						carAuthorityInfo.setJoinCount(null==countMap.get(CarConstant.USE_CAR_AIRPORT_PICKUP)?0:countMap.get(CarConstant.USE_CAR_AIRPORT_PICKUP));
						carAuthorityInfo.setGiveCount(null==countMap.get(CarConstant.USE_CAR_AIRPORT_DROP_OFF)?0:countMap.get(CarConstant.USE_CAR_AIRPORT_DROP_OFF));
						carAuthorityInfo.setCityCount(null==countMap.get(CarConstant.CITY_USE_CAR)?0:countMap.get(CarConstant.CITY_USE_CAR));
						//获取差旅的所有出差城市
						carAuthorityInfo.setCityName(journeyNodeInfoService.selectAllCity(journeyInfo.getJourneyId()));
						//获取行程的起止时间
						JourneyNodeInfo journeyNodeInfo = journeyNodeInfoService.selectMaxAndMinDate(journeyInfo.getJourneyId());
						if(null !=journeyNodeInfo){
							carAuthorityInfo.setStartDate(journeyNodeInfo.getPlanSetoutTime());
							carAuthorityInfo.setEndDate(journeyNodeInfo.getPlanArriveTime());
						}
					}
					
					if(CarConstant.USE_CAR_TYPE_OFFICIAL.equals(regimeInfo.getApplyType())){
						//公务类型
						ApplyInfo applyInfo = new ApplyInfo();
						//查询对应的公务出差理由
						applyInfo.setJourneyId(journeyInfo.getJourneyId());
						List<ApplyInfo> applyInfoList = applyInfoService.selectApplyInfoList(applyInfo);
						carAuthorityInfo.setApplyName(applyInfoList.get(0).getReason());
						
					}
				}
				carAuthorityInfoList.add(carAuthorityInfo);	
			}
		}
		return carAuthorityInfoList;
	}
}
