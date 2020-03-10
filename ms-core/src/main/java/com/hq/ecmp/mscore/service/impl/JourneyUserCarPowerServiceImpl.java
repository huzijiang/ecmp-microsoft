package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.domain.JourneyInfo;
import com.hq.ecmp.mscore.domain.JourneyNodeInfo;
import com.hq.ecmp.mscore.domain.JourneyUserCarPower;
import com.hq.ecmp.mscore.domain.ServiceTypeCarAuthority;
import com.hq.ecmp.mscore.domain.UserCarAuthority;
import com.hq.ecmp.mscore.mapper.JourneyUserCarPowerMapper;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IJourneyInfoService;
import com.hq.ecmp.mscore.service.IJourneyNodeInfoService;
import com.hq.ecmp.mscore.service.IJourneyUserCarPowerService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class JourneyUserCarPowerServiceImpl implements IJourneyUserCarPowerService
{
    @Autowired
    private JourneyUserCarPowerMapper journeyUserCarPowerMapper;
    @Autowired
    private IJourneyNodeInfoService journeyNodeInfoService;
    @Autowired
    private IApplyInfoService applyInfoService;
    @Autowired
    private IJourneyInfoService journeyInfoService;

    /**
     * 查询【请填写功能名称】
     *
     * @param powerId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public JourneyUserCarPower selectJourneyUserCarPowerById(Long powerId)
    {
        return journeyUserCarPowerMapper.selectJourneyUserCarPowerById(powerId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<JourneyUserCarPower> selectJourneyUserCarPowerList(JourneyUserCarPower journeyUserCarPower)
    {
        return journeyUserCarPowerMapper.selectJourneyUserCarPowerList(journeyUserCarPower);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertJourneyUserCarPower(JourneyUserCarPower journeyUserCarPower)
    {
        journeyUserCarPower.setCreateTime(DateUtils.getNowDate());
        return journeyUserCarPowerMapper.insertJourneyUserCarPower(journeyUserCarPower);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateJourneyUserCarPower(JourneyUserCarPower journeyUserCarPower)
    {
        journeyUserCarPower.setUpdateTime(DateUtils.getNowDate());
        return journeyUserCarPowerMapper.updateJourneyUserCarPower(journeyUserCarPower);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param powerIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyUserCarPowerByIds(Long[] powerIds)
    {
        return journeyUserCarPowerMapper.deleteJourneyUserCarPowerByIds(powerIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param powerId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteJourneyUserCarPowerById(Long powerId)
    {
        return journeyUserCarPowerMapper.deleteJourneyUserCarPowerById(powerId);
    }

	@Override
	public Map<String, Integer> selectStatusCount(Long journeyId) {
		Map<String, Integer> map =new HashMap<>();
		JourneyUserCarPower journeyUserCarPower = new JourneyUserCarPower();
		//查询未使用的次数
		journeyUserCarPower.setState(CarConstant.NOT_USER_USE_CAR);
		journeyUserCarPower.setJourneyId(journeyId);;
		List<JourneyUserCarPower> list = selectJourneyUserCarPowerList(journeyUserCarPower);
		if(null !=list && list.size()>0){
			//对三种类型的分组统计次数
			for (JourneyUserCarPower j : list) {
				String type = j.getType();
				Integer sum = map.get(type);
				if(null ==sum){
					sum=1;
				}else{
					sum++;
				}
				map.put(type, sum);
			}
		}
		return map;
	}

	@Override
	public List<UserCarAuthority> queryNoteAllUserAuthority(Long nodeId) {
		List<UserCarAuthority> list = journeyUserCarPowerMapper.queryNoteAllUserAuthority(nodeId);
		if(null !=list && list.size()>0){
			for (UserCarAuthority userCarAuthority : list) {
				//获取接机or送机剩余次数
				userCarAuthority.handCount();
			}
		}
		return list;
	}

	@Override
	public List<ServiceTypeCarAuthority> queryUserAuthorityFromService(String type, Long journeyId) {
		List<ServiceTypeCarAuthority> list = new ArrayList<ServiceTypeCarAuthority>();
		// 获取出差城市(去重后的)
		List<String> cityList = journeyNodeInfoService.queryGroupCity(journeyId);
		if (null != cityList && cityList.size() > 0) {
			JourneyUserCarPower query = new JourneyUserCarPower();
			query.setJourneyId(journeyId);
			query.setType(type);
			for (String cityName : cityList) {
				ServiceTypeCarAuthority serviceTypeCarAuthority = new ServiceTypeCarAuthority();
				query.setCityName(cityName);
				serviceTypeCarAuthority.setCityName(cityName);
				//获取剩余次数
				serviceTypeCarAuthority.setSurplusCount(journeyUserCarPowerMapper.querySurplusNum(query));
				//状态
				serviceTypeCarAuthority.setState("S299");
				list.add(serviceTypeCarAuthority);
			}
		}
		return list;
	}

	@Override
	public boolean createUseCarAuthority(Long applyId,Long auditUserId) {
		//查询行程信息
		ApplyInfo applyInfo = applyInfoService.selectApplyInfoById(applyId);
		Long journeyId = applyInfo.getJourneyId();
		//查询行程信息
		JourneyInfo journeyInfo = journeyInfoService.selectJourneyInfoById(journeyId);
		//查询行程节点信息  按节点顺序
		List<JourneyNodeInfo> journeyNodeInfoList = journeyNodeInfoService.queryJourneyNodeInfoOrderByNumber(journeyId);
		//途径地节点
		List<Long> throughTo=new ArrayList<Long>();
		//起点地
		List<Long> startTo=new ArrayList<Long>();
		//目的地
		List<Long> endTo=new ArrayList<Long>();
		if(null !=journeyNodeInfoList && journeyNodeInfoList.size()>0){
			//起点地
			startTo.add(journeyNodeInfoList.get(0).getNodeId());
			//目的地
			endTo.add(journeyNodeInfoList.get(journeyNodeInfoList.size()-1).getNodeId());
			for (int i = 1; i < journeyNodeInfoList.size(); i++) {
				JourneyNodeInfo currentNote = journeyNodeInfoList.get(i);
				JourneyNodeInfo lastNote = journeyNodeInfoList.get(i-1);
				if(null !=currentNote.getPlanBeginAddress()&& null !=lastNote.getPlanEndAddress()){
					//当前节点的出发地为上一个节点的目的地  则当前节点为途径地
					if(currentNote.getPlanBeginAddress().equals(lastNote.getPlanEndAddress())){
						throughTo.add(currentNote.getNodeId());
					}else{
						//  不是  则当前节点为新的起点地   例如  北京-上海  广州-深圳  当前广州
						startTo.add(currentNote.getNodeId());
					}
					
				}
			}
		}
		//生成接机，送机,市内用车权限
		List<JourneyUserCarPower> journeyUserCarPowerList=new ArrayList<JourneyUserCarPower>();
		JourneyUserCarPower journeyUserCarPower;
		if(startTo.size()>0){
			//起点地生成一次送机权限
			for (Long s : startTo) {
				journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.USE_CAR_AIRPORT_DROP_OFF,s);
				journeyUserCarPowerList.add(journeyUserCarPower);
			}
		}
		if(endTo.size()>0){
			//目的地生成一次接机权限
			for (Long s : endTo) {
				journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.USE_CAR_AIRPORT_PICKUP,s);
				journeyUserCarPowerList.add(journeyUserCarPower);
			}
		}
		
		if(throughTo.size()>0){
			//途径地会生成市内用车   接送机权限各一次
			for (Long s : throughTo) {
				journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.USE_CAR_AIRPORT_DROP_OFF,s);
				journeyUserCarPowerList.add(journeyUserCarPower);
				journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.USE_CAR_AIRPORT_PICKUP,s);
				journeyUserCarPowerList.add(journeyUserCarPower);
				journeyUserCarPower=new JourneyUserCarPower(applyId, journeyId, new Date(), auditUserId,CarConstant.NOT_USER_USE_CAR,CarConstant.CITY_USE_CAR,s);
				journeyUserCarPowerList.add(journeyUserCarPower);
			}
		}
		//批量插入用车权限
		return journeyUserCarPowerMapper.batchInsert(journeyUserCarPowerList)>0;
	}
}
