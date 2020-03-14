package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hq.ecmp.constant.CarModeEnum;
import com.hq.ecmp.constant.CarPowerEnum;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.vo.JourneyVO;
import com.hq.ecmp.mscore.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CarConstant;
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
				carAuthorityInfo.setJourneyId(journeyInfo.getJourneyId());
				//获取是差旅还是公务
				RegimeInfo regimeInfo = regimeInfoService.queryRegimeType(journeyInfo.getRegimenId());
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

	@Override
public List<UserAuthorityGroupCity> getUserCarAuthority(Long journeyId) {
		List<UserAuthorityGroupCity> userAuthorityGroupCityList=new ArrayList<UserAuthorityGroupCity>();
		
		//根据行程查询下面的行程节点
		JourneyNodeInfo journeyNodeQuery = new JourneyNodeInfo();
		journeyNodeQuery.setJourneyId(journeyId);
		List<JourneyNodeInfo> journeyNodeInfoList = journeyNodeInfoService.selectJourneyNodeInfoList(journeyNodeQuery);
		if(null !=journeyNodeInfoList && journeyNodeInfoList.size()>0){
			for (JourneyNodeInfo journeyNodeInfo : journeyNodeInfoList) {
				UserAuthorityGroupCity userAuthorityGroupCity = new UserAuthorityGroupCity();
				userAuthorityGroupCity.setCityName(journeyNodeInfo.getPlanBeginAddress());
				//获取行程节点下的所有用户用车权限
				userAuthorityGroupCity.setUserCarAuthorityList(journeyUserCarPowerService.queryNoteAllUserAuthority(journeyNodeInfo.getNodeId()));
				userAuthorityGroupCityList.add(userAuthorityGroupCity);
			}
		}
		return userAuthorityGroupCityList;
	}

	@Override
	public MessageDto getJourneyMessage(Long userId) {
		return journeyInfoMapper.getJourneyMessage(userId);
	}
	/**
	 * 获取正在进行中的行程
	 * @param userId
	 * @return list
	 */
	@Override
	public List<JourneyVO> getJourneyList(Long userId){

		return journeyInfoMapper.getJourneyList(userId);
	}

	/**
	 * 获取正在进行中的行程个数
	 * @param userId
	 * @return list
	 */
	@Override
	public int getJourneyListCount(Long userId) {
		return journeyInfoMapper.getJourneyListCount(userId);
	}
	/**
	 * 获取正在进行中的行程详情
	 * @param orderId
	 * @return list
	 */
	/*@Override
	public OrderVO orderBeServiceDetail(Long orderId) {
		OrderVO vo=new OrderVO();
		OrderInfo orderInfo = this.selectOrderInfoById(orderId);
		if (orderInfo==null){
			return null;
		}
		BeanUtils.copyProperties(orderInfo,vo);
		//查询车辆信息
		CarInfo carInfo = carInfoService.selectCarInfoById(orderInfo.getCarId());
		if (carInfo!=null){
			BeanUtils.copyProperties(carInfo,vo);
		}
		vo.setPowerType(CarPowerEnum.format(carInfo.getPowerType()));
		//TODO 是否需要车队信息
		//是否添加联系人
//        DriverInfo driverInfo = driverInfoService.selectDriverInfoById(orderInfo.getDriverId());
		vo.setDriverScore("4.5");
		vo.setDriverType(CarModeEnum.format(orderInfo.getUseCarMode()));
		vo.setState(orderInfo.getState());
		//TODO 客服电话暂时写死
		vo.setCustomerServicePhone("010-88888888");
		return vo;
	}*/


}
