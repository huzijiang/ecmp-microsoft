package com.hq.ecmp.mscore.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarGroupDriverRelation;
import com.hq.ecmp.mscore.domain.DriverCarRelationInfo;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.DriverQuery;
import com.hq.ecmp.mscore.domain.DriverQueryResult;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.mapper.DriverInfoMapper;
import com.hq.ecmp.mscore.service.ICarGroupDriverRelationService;
import com.hq.ecmp.mscore.service.IDriverCarRelationInfoService;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.service.IEcmpUserService;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class DriverInfoServiceImpl implements IDriverInfoService
{
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private IEcmpUserService ecmpUserService;
    @Autowired
    private ICarGroupDriverRelationService carGroupDriverRelationService;
    
    @Autowired
    private IDriverCarRelationInfoService driverCarRelationInfoService;
    

    /**
     * 查询【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public DriverInfo selectDriverInfoById(Long driverId)
    {
        return driverInfoMapper.selectDriverInfoById(driverId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<DriverInfo> selectDriverInfoList(DriverInfo driverInfo)
    {
        return driverInfoMapper.selectDriverInfoList(driverInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param driverInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertDriverInfo(DriverInfo driverInfo)
    {
        driverInfo.setCreateTime(DateUtils.getNowDate());
        return driverInfoMapper.insertDriverInfo(driverInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param driverInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateDriverInfo(DriverInfo driverInfo)
    {
        driverInfo.setUpdateTime(DateUtils.getNowDate());
        return driverInfoMapper.updateDriverInfo(driverInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param driverIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverInfoByIds(Long[] driverIds)
    {
        return driverInfoMapper.deleteDriverInfoByIds(driverIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param driverId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverInfoById(Long driverId)
    {
        return driverInfoMapper.deleteDriverInfoById(driverId);
    }
    
    
    @Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean createDriver(DriverCreateInfo driverCreateInfo) {
    	//生成驾驶员记录
    	Integer createDriver = driverInfoMapper.createDriver(driverCreateInfo);
    	Long driverId = driverCreateInfo.getDriverId();
    	//生成用户记录
    	EcmpUser ecmpUser = new EcmpUser();
    	ecmpUser.setUserId(driverCreateInfo.getUserId());
    	ecmpUser.setUserName(driverCreateInfo.getMobile());
    	ecmpUser.setNickName(driverCreateInfo.getDriverName());
    	ecmpUser.setItIsDriver("0");
    	ecmpUser.setCreateBy(driverCreateInfo.getOptUserId().toString());
    	ecmpUser.setCreateTime(new Date());;
    	ecmpUserService.insertEcmpUser(ecmpUser);
    	//生成驾驶员-车队关系记录
    	CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
    	carGroupDriverRelation.setCarGroupId(driverCreateInfo.getCarGroupId());
    	carGroupDriverRelation.setDriverId(driverCreateInfo.getDriverId());
    	carGroupDriverRelation.setCreateBy(driverCreateInfo.getOptUserId().toString());
    	carGroupDriverRelation.setCreateTime(new Date());
    	carGroupDriverRelationService.insertCarGroupDriverRelation(carGroupDriverRelation);
    	//生成驾驶员-车辆记录
    	DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
    	driverCarRelationInfo.setUserId(driverCreateInfo.getUserId());
    	driverCarRelationInfo.setDriverId(driverCreateInfo.getDriverId());
    	driverCarRelationInfo.setCarIdList(driverCreateInfo.getCarId());
    	driverCarRelationInfoService.batchDriverCarList(driverCarRelationInfo);
		return true;
	}

	@Override
	public List<DriverQueryResult> queryDriverList(DriverQuery query) {
		List<DriverQueryResult> list = driverInfoMapper.queryDriverList(query);
		if(null !=list && list.size()>0){
			for (DriverQueryResult driverQueryResult : list) {
				//查询该驾驶员可以使用的车辆数量
				Integer count = driverCarRelationInfoService.queryDriverUseCarCount(driverQueryResult.getDriverId());
				driverQueryResult.setOwnCarCount(count);
			}
		}
		return list;
	}

	@Override
	public Integer queryDriverListCount(DriverQuery query) {
		return driverInfoMapper.queryDriverListCount(query);
	}

	@Override
	public DriverQueryResult queryDriverDetail(Long driverId) {
		return driverInfoMapper.queryDriverDetail(driverId);
	}
}
