package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.CarGroupInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverCarRelationInfoMapper;
import com.hq.ecmp.mscore.vo.CarVO;
import com.hq.ecmp.mscore.vo.DriverVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarGroupDriverInfo;
import com.hq.ecmp.mscore.domain.CarGroupDriverRelation;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.DriverCarRelationInfo;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.DriverQuery;
import com.hq.ecmp.mscore.domain.DriverQueryResult;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.mapper.DriverInfoMapper;
import com.hq.ecmp.mscore.mapper.EcmpOrgMapper;
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
    @Autowired
    private DriverCarRelationInfoMapper driverCarRelationInfoMapper;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;
    

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
    	
       	//生成用户记录
    	EcmpUser ecmpUser = new EcmpUser();
    	ecmpUser.setUserName(driverCreateInfo.getMobile());
    	ecmpUser.setNickName(driverCreateInfo.getDriverName());
    	ecmpUser.setItIsDriver("0");
    	ecmpUser.setPhonenumber(driverCreateInfo.getMobile());
    	ecmpUser.setCreateBy(driverCreateInfo.getOptUserId().toString());
    	ecmpUser.setCreateTime(new Date());;
    	ecmpUserService.insertEcmpUser(ecmpUser);
    	driverCreateInfo.setUserId(ecmpUser.getUserId());
    	//生成驾驶员记录
    	Integer createDriver = driverInfoMapper.createDriver(driverCreateInfo);
    	Long driverId = driverCreateInfo.getDriverId();
 
    	//生成驾驶员-车队关系记录
    	CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
    	carGroupDriverRelation.setCarGroupId(driverCreateInfo.getCarGroupId());
    	carGroupDriverRelation.setDriverId(driverCreateInfo.getDriverId());
    	carGroupDriverRelation.setCreateBy(driverCreateInfo.getOptUserId().toString());
    	carGroupDriverRelation.setCreateTime(new Date());
    	carGroupDriverRelationService.insertCarGroupDriverRelation(carGroupDriverRelation);
    	//生成驾驶员-车辆记录
    	DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
    	driverCarRelationInfo.setUserId(ecmpUser.getUserId());
    	driverCarRelationInfo.setDriverId(driverCreateInfo.getDriverId());
    	driverCarRelationInfo.setCarIdList(driverCreateInfo.getCarId());
    	driverCarRelationInfoService.batchDriverCarList(driverCarRelationInfo);
		return true;
	}
    /**
     * 修改驾驶员
     * @param driverCreateInfo
     * @return
     */
    @Override
    public boolean updateDriver(DriverCreateInfo driverCreateInfo){
        //修改驾驶员
        driverCreateInfo.setUpdateTime(DateUtils.getNowDate());
        Integer createDriver = driverInfoMapper.updateDriver(driverCreateInfo);
        Long driverId = driverCreateInfo.getDriverId();
        //修改驾驶员-车队关系记录
        CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
        carGroupDriverRelation.setCarGroupId(driverCreateInfo.getCarGroupId());
        carGroupDriverRelation.setDriverId(driverCreateInfo.getDriverId());
        carGroupDriverRelation.setCreateBy(driverCreateInfo.getOptUserId().toString());
        carGroupDriverRelation.setCreateTime(new Date());
        carGroupDriverRelationService.updateCarGroupDriverRelation(carGroupDriverRelation);
        //修改驾驶员-车辆记录
        DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
        driverCarRelationInfo.setUserId(driverCreateInfo.getUserId());
        driverCarRelationInfo.setDriverId(driverCreateInfo.getDriverId());
        driverCarRelationInfo.setCarIdList(driverCreateInfo.getCarId());
        driverCarRelationInfoService.updateBatchDriverCarList(driverCarRelationInfo);
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
		DriverQueryResult queryDriverDetail = driverInfoMapper.queryDriverDetail(driverId);
		//查询该驾驶员可使用的车辆
		List<Long> carId=new ArrayList<>();
		DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
		driverCarRelationInfo.setDriverId(driverId);
		List<DriverCarRelationInfo> selectDriverCarRelationInfoList = driverCarRelationInfoMapper.selectDriverCarRelationInfoList(driverCarRelationInfo);
		if(null !=selectDriverCarRelationInfoList && selectDriverCarRelationInfoList.size()>0){
			for (DriverCarRelationInfo d : selectDriverCarRelationInfoList) {
				carId.add(d.getCarId());
			}
		}
		queryDriverDetail.setCarId(carId);
		return queryDriverDetail;
	}
    /**
     *驾驶员总数
     */
    @Override
    public int queryCompanyDriverCount(){
        return driverInfoMapper.queryCompanyDriver();
    }
    /**
     *
     * @param driverRegisterDTO
     * @return
     */
    public int driverItisExist(DriverRegisterDTO driverRegisterDTO){
        return driverInfoMapper.driverItisExist(driverRegisterDTO.getMobile());
    }
    /**
     *驾驶员可用车辆列表
     * @param
     */
    public List<DriverCanUseCarsDTO> getDriverCanCar(Long driverId){
        return driverInfoMapper.getDriverCanCar(driverId);
    }
    /**
     *驾驶员失效列表,离职列表
     * @param
     */
    public List<DriverLoseDTO> getDriverLoseList(Long deptId){
        return driverInfoMapper.getDriverLoseList(deptId);
    }
    /**
     * 驾驶员已离职数量
     */
    public int getDriverLoseCount(Long deptId){
        return driverInfoMapper.getDriverLoseCount(deptId);

    }
    /**
     * 已失效驾驶员进行删除
     */
    public int deleteDriver(Long driverId){
        return driverInfoMapper.deleteDriver(driverId);
    }


    /**
     * 修改驾驶员手机号
     * @param mobile
     * @return
     */
    public int updateDriverMobile(String mobile,Long driverId){
        return driverInfoMapper.updateDriverMobile(mobile,driverId);
    }

    /**
     * 设置驾驶员离职日期
     * @param dimTime
     * @return
     */
    public int updateDriverDimTime(Date dimTime,Long driverId){
        return driverInfoMapper.updateDriverDimTime(dimTime,driverId);
    }
    /**
     * 驾驶员绑定车辆
     * @param driverCarDTO
     * @return
     */
    @Override
    public void bindDriverCars(DriverCarDTO driverCarDTO, Long userId) throws Exception {
        Long driverId = driverCarDTO.getDriverId();
        List<CarVO> cars = driverCarDTO.getCars();
        DriverCarRelationInfo driverCarRelationInfo = null;
        for (CarVO car : cars) {
            driverCarRelationInfo = new DriverCarRelationInfo();
            driverCarRelationInfo.setUserId(car.getUserId());
            driverCarRelationInfo.setCarId(car.getCarId());
            driverCarRelationInfo.setDriverId(driverId);
            int i = driverCarRelationInfoMapper.insertDriverCarRelationInfo(driverCarRelationInfo);
            if(i != 1){
                throw new Exception("驾驶员绑定车辆失败");
            }
        }
    }

	@Override
	public int updateDriverStatus(Long driverId, String state) {
		return driverInfoMapper.updateDriverStatus(driverId, state);
	}

	@Override
	public CarGroupDriverInfo queryCarGroupDriverList(Long carGroupId) {
		CarGroupDriverInfo carGroupDriverInfo = new CarGroupDriverInfo();
		List<DriverQueryResult> list = driverInfoMapper.queryDriverInfoList(carGroupId);
		carGroupDriverInfo.setDriverList(list);
		//查询车队对应的部门和公司
		CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
		if(null !=carGroupInfo){
			Long ownerCompany = carGroupInfo.getOwnerCompany();
			if(null !=ownerCompany){
				EcmpOrg company = ecmpOrgMapper.selectEcmpOrgById(ownerCompany);
				if(null !=company){
					carGroupDriverInfo.setCompanyName(company.getDeptName());
				}
			}
			Long ownerOrg = carGroupInfo.getOwnerOrg();
			if(null !=ownerOrg){
				EcmpOrg dept = ecmpOrgMapper.selectEcmpOrgById(ownerOrg);
				if(null !=dept){
					carGroupDriverInfo.setDeptName(dept.getDeptName());
					//查询部门负责人
					String leader = dept.getLeader();
					if(null !=leader){
						EcmpUser deptLeader = ecmpUserService.selectEcmpUserById(Long.valueOf(leader));
						if(null !=deptLeader){
							carGroupDriverInfo.setDeptName(deptLeader.getNickName());
						}
					}
					//查询所属该部门的员工人数
					EcmpUser queryEcmpUser = new EcmpUser();
					queryEcmpUser.setDeptId(ownerOrg);
					List<EcmpUser> selectEcmpUserList = ecmpUserService.selectEcmpUserList(queryEcmpUser);
					if(null!=selectEcmpUserList){
						carGroupDriverInfo.setDeptUserNum(selectEcmpUserList.size());
					}
					
				}
			}
		}
		//查询待审核驾驶员人数
		DriverQuery driverQuery = new DriverQuery();
		driverQuery.setCarGroupId(carGroupId);
		driverQuery.setState("W001");
		Integer waitAuditDriverNum = driverInfoMapper.queryDriverNumOfStateAndCarGroup(driverQuery);
		//查询已失效驾驶员人数
		driverQuery.setState("NV00");
		Integer loseDriverNum = driverInfoMapper.queryDriverNumOfStateAndCarGroup(driverQuery);
		carGroupDriverInfo.setWaitAuditDriverNum(waitAuditDriverNum);
		carGroupDriverInfo.setLoseDriverNum(loseDriverNum);
		return carGroupDriverInfo;
	}


}
