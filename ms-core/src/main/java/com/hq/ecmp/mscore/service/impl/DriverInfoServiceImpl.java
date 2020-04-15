package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.CarGroupInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverCarRelationInfoMapper;
import com.hq.ecmp.mscore.vo.*;
import org.apache.http.impl.execchain.TunnelRefusedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarGroupDriverInfo;
import com.hq.ecmp.mscore.domain.CarGroupDriverRelation;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.DriverCarRelationInfo;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.DriverQuery;
import com.hq.ecmp.mscore.domain.DriverQueryResult;
import com.hq.ecmp.mscore.domain.DriverUserJobNumber;
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
    	
/*       	//生成用户记录
    	EcmpUser ecmpUser = new EcmpUser();
    	ecmpUser.setUserName(driverCreateInfo.getMobile());
    	ecmpUser.setNickName(driverCreateInfo.getDriverName());
    	ecmpUser.setItIsDriver("0");
    	ecmpUser.setPhonenumber(driverCreateInfo.getMobile());
    	ecmpUser.setCreateBy(driverCreateInfo.getOptUserId().toString());
    	ecmpUser.setCreateTime(new Date());;
    	//查询归属车队所在的部门
    	CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(driverCreateInfo.getCarGroupId());
    	if(null !=carGroupInfo){
    		ecmpUser.setDeptId(carGroupInfo.getOwnerOrg());
    	}
    	ecmpUserService.insertEcmpUser(ecmpUser);
    	driverCreateInfo.setUserId(ecmpUser.getUserId());*/
    	//通过手机号和姓名去user表中查询
    	EcmpUser query = new EcmpUser();
		query.setNickName(driverCreateInfo.getDriverName());
		query.setPhonenumber(driverCreateInfo.getMobile());
		List<EcmpUser> selectEcmpUserList = ecmpUserService.selectEcmpUserList(query);
		Long userId=null;
		if(null !=selectEcmpUserList && selectEcmpUserList.size()>0){
			userId=selectEcmpUserList.get(0).getUserId();
			driverCreateInfo.setUserId(userId);
			String jobNumber = driverCreateInfo.getJobNumber();
			if(StringUtil.isNotEmpty(jobNumber)){
				//传入了工号  则更新驾驶员对应的公司员工的工号
				EcmpUser updateEcmpUser = new EcmpUser();
				updateEcmpUser.setUserId(userId);
				updateEcmpUser.setJobNumber(jobNumber);
				ecmpUserService.updateEcmpUserjobNumber(updateEcmpUser);
			}
		}
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
    	List<Long> carIdList = driverCreateInfo.getCarId();
    	if(null !=carIdList && carIdList.size()>0){
    		DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
        	if(null !=userId){
        		driverCarRelationInfo.setUserId(userId);
        	}
        	driverCarRelationInfo.setDriverId(driverCreateInfo.getDriverId());
        	driverCarRelationInfo.setCarIdList(carIdList);
        	driverCarRelationInfoService.batchDriverCarList(driverCarRelationInfo);
    	}
    	
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
       /* DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
        driverCarRelationInfo.setUserId(driverCreateInfo.getUserId());
        driverCarRelationInfo.setDriverId(driverCreateInfo.getDriverId());
        driverCarRelationInfo.setCarIdList(driverCreateInfo.getCarId());
        driverCarRelationInfoService.updateBatchDriverCarList(driverCarRelationInfo);*/

        driverCarRelationInfoService.deleteCarByDriverId(driverId);
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
				Long userId = driverQueryResult.getUserId();
				if(null !=userId){
					//该驾驶员也是公司的员工 则查询工号
					EcmpUser ecmpUser = ecmpUserService.selectEcmpUserById(userId);
					if(null !=ecmpUser){
						driverQueryResult.setJobNumber(ecmpUser.getJobNumber());
					}
				}
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
			queryDriverDetail.setOwnCarCount(selectDriverCarRelationInfoList.size());
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
    @Override
    public int driverItisExist(DriverRegisterDTO driverRegisterDTO){
        return driverInfoMapper.driverItisExist(driverRegisterDTO.getMobile());
    }
    /**
     *驾驶员可用车辆列表
     * @param
     */
    @Override
    public List<DriverCanUseCarsDTO> getDriverCanCar(Long driverId){
        return driverInfoMapper.getDriverCanCar(driverId);
    }
    /**
     *驾驶员失效列表,离职列表
     * @param
     */
    @Override
    public PageResult<DriverLoseDTO> getDriverLoseList(Integer pageNum, Integer pageSize,Long carGroupId,String search){
		PageHelper.startPage(pageNum,pageSize);
		List<DriverLoseDTO> loseDriverVOS= driverInfoMapper.getDriverLoseList(carGroupId,search);
		PageInfo<DriverLoseDTO> info = new PageInfo<>(loseDriverVOS);
		return new PageResult<>(info.getTotal(),info.getPages(),loseDriverVOS);

    }
    /**
     * 驾驶员已离职数量
     */
    @Override
    public Long getDriverLoseCount(Long carGroupId){
        return driverInfoMapper.getDriverLoseCount(carGroupId);

    }
    /**
     * 已失效驾驶员进行删除
     */
    @Override
    public int deleteDriver(Long driverId){

		int i=driverInfoMapper.deleteDriver(driverId);
		if(i!=0){
			driverCarRelationInfoService.deleteCarByDriverId(driverId);
			carGroupDriverRelationService.deleteCarGroupDriverRelationById(driverId);
		}

    	return i;
    }


    /**
     * 修改驾驶员手机号
     * @param mobile
     * @return
     */
    @Override
    public int updateDriverMobile(String mobile, Long driverId){
        return driverInfoMapper.updateDriverMobile(mobile,driverId);
    }

    /**
     * 设置驾驶员离职日期
     * @param dimTime
     * @return
     */
    @Override
    public int updateDriverDimTime(Date dimTime, Long driverId){
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
							carGroupDriverInfo.setDeptLeader(deptLeader.getNickName());
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

	@Override
	public boolean checkMobile(String mobile) {
		DriverInfo driverInfo = new DriverInfo();
		driverInfo.setMobile(mobile);
		List<DriverInfo> selectDriverInfoList = driverInfoMapper.selectDriverInfoList(driverInfo);
		if(null !=selectDriverInfoList && selectDriverInfoList.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public void checkjobNumber(DriverUserJobNumber driverUserJobNumber) throws Exception {
		String driverName = driverUserJobNumber.getDriverName();
		String jobNumber = driverUserJobNumber.getJobNumber();
		String mobile = driverUserJobNumber.getMobile();
		if(StringUtil.isEmpty(driverName)){
			throw new Exception("请输入驾驶员姓名");
		}
		if(StringUtil.isEmpty(jobNumber)){
			throw new Exception("请输入驾驶员工号");
		}
		if(StringUtil.isEmpty(mobile)){
			throw new Exception("请输入驾驶员手机号");
		}
		EcmpUser query = new EcmpUser();
		query.setNickName(driverName);
		query.setPhonenumber(mobile);
		List<EcmpUser> selectEcmpUserList = ecmpUserService.selectEcmpUserList(query);
		if(null==selectEcmpUserList || selectEcmpUserList.size()==0){
			return;
		}
		EcmpUser ecmpUser = selectEcmpUserList.get(0);
		String userJobNumber = ecmpUser.getJobNumber();
		if(StringUtil.isEmpty(userJobNumber)){
			//查询user表中是否已经存在了输入的工号 jobNumber
			EcmpUser queryJobNumber = new EcmpUser();
			queryJobNumber.setJobNumber(jobNumber);
			List<EcmpUser> queryJobNumberUserList = ecmpUserService.selectEcmpUserList(queryJobNumber);
			if(null !=queryJobNumberUserList && queryJobNumberUserList.size()>0){
				throw new Exception("工号已被占用，请重新录入!");
			}
			return;
		}
		if(jobNumber.equals(userJobNumber)){
			return;
		}else{
			throw new Exception("员工"+driverName+"："+mobile+"对应的工号是"+userJobNumber+"，请核实后重新输入!");
		}
	}
}
