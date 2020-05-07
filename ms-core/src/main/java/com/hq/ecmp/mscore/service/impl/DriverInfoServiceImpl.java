package com.hq.ecmp.mscore.service.impl;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.InvitionTypeEnum;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.RoleConstant;
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
	@Resource
	private EcmpEnterpriseRegisterInfoMapper ecmpEnterpriseRegisterInfoMapper;
    @Autowired
    private IDriverCarRelationInfoService driverCarRelationInfoService;
    @Autowired
    private DriverCarRelationInfoMapper driverCarRelationInfoMapper;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;
	@Autowired
	private DriverWorkInfoMapper driverWorkInfoMapper;
	@Autowired
	private EcmpUserRoleMapper ecmpUserRoleMapper;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CarInfoMapper carInfoMapper;
    

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
			//新增的驾驶员是公司员工  则赋予该员工驾驶员角色
			EcmpUserRole ecmpUserRole = new EcmpUserRole();
			ecmpUserRole.setUserId(userId);
			ecmpUserRole.setRoleId(Long.valueOf(RoleConstant.DATA_SCOPE_6));
			ecmpUserRoleMapper.insertEcmpUserRole(ecmpUserRole);
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
		driverCreateInfo.setLockState("0000");
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
		setDriverWorkInfo(driverId);
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
		List<CarListVO> cars = new ArrayList<>();
		if(null !=selectDriverCarRelationInfoList && selectDriverCarRelationInfoList.size()>0){
			for (DriverCarRelationInfo d : selectDriverCarRelationInfoList) {
				carId.add(d.getCarId());
				CarInfo carInfo = carInfoMapper.selectCarInfoById(d.getCarId());
				CarListVO build = CarListVO.builder().carType(carInfo.getCarType())
						.carLicense(carInfo.getCarLicense()).carId(d.getCarId()).build();
				cars.add(build);
			}
		}
		queryDriverDetail.setOwnCarCount(driverCarRelationInfoService.queryDriverUseCarCount(driverId));
		queryDriverDetail.setCarId(carId);
		queryDriverDetail.setCarList(cars);
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
	public PageResult<DriverCanUseCarsDTO> getDriverCanCar(Integer pageNum, Integer pageSize,Long driverId,String state,String search){
		PageHelper.startPage(pageNum,pageSize);
		List<DriverCanUseCarsDTO> driverCanUseCarsVo= driverInfoMapper.getDriverCanCar(driverId,state,search);
		PageInfo<DriverCanUseCarsDTO> info = new PageInfo<>(driverCanUseCarsVo);
        return new PageResult<>(info.getTotal(),info.getPages(),driverCanUseCarsVo);
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
    public ApiResponse deleteDriver(Long driverId)throws Exception {
		DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
		driverCarRelationInfo.setDriverId(driverId);
		List<DriverCarRelationInfo> driverCarRelationInfos = driverCarRelationInfoMapper.selectDriverCarRelationInfoList(driverCarRelationInfo);
		int size = driverCarRelationInfos.size();
		if(size != 0){
			return ApiResponse.error("请先删除该驾驶员下的所有车辆，再尝试删除");
		}
		int i=driverInfoMapper.deleteDriver(driverId);
		if(i!=0){
			driverCarRelationInfoService.deleteCarByDriverId(driverId);
			carGroupDriverRelationService.deleteCarGroupDriverRelationById(driverId);
		}else {
			throw new Exception("删除失败");
		}
		return ApiResponse.success("删除成功");
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
	public CarGroupDriverInfo queryCarGroupDriverList(Map map) {
		Long carGroupId = Long.valueOf(map.get("carGroupId").toString());
		Long carId = map.get("carId")==null?null:Long.valueOf(map.get("carId").toString());
		String search = map.get("search")==null?null: map.get("search").toString();
		CarGroupDriverInfo carGroupDriverInfo = new CarGroupDriverInfo();
		//查询车队下的可用驾驶员列表（driverId 和 driverName）如果传了carId，则排除该车辆下的驾驶员
		List<DriverQueryResult> list = driverInfoMapper.queryDriverInfoList(carGroupId,carId,search);
		carGroupDriverInfo.setDriverList(list);
		//查询车队对应的部门和公司
		CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
		List<String> carGroupName = new ArrayList<>();
		if(carGroupInfo.getParentCarGroupId()==0L){
			carGroupName.add(carGroupInfo.getCarGroupName());
		}else{
			CarGroupInfo pCarGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupInfo.getParentCarGroupId());
			carGroupName.add(pCarGroupInfo.getCarGroupName());
			carGroupName.add(carGroupInfo.getCarGroupName());
		}
		carGroupDriverInfo.setCarGroupName(carGroupName);
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
//		driverQuery.setState("W001");
//		Integer waitAuditDriverNum = driverInfoMapper.queryDriverNumOfStateAndCarGroup(driverQuery);
		Integer waitAuditDriverNum =ecmpEnterpriseRegisterInfoMapper.waitAmountCount(null, InvitionTypeEnum.DRIVER.getKey(),carGroupId);
		//查询已失效驾驶员人数
		DriverQuery driverQuery = new DriverQuery();
		driverQuery.setCarGroupId(carGroupId);
		driverQuery.setState(CommonConstant.STATE_OFF);
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

	@Override
	public PageResult driverWorkOrderList(PageRequest pageRequest) {
    	PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
		List<DriverOrderVo> driverOrderVos =driverInfoMapper.driverWorkOrderList(pageRequest.getCarGroupId(),pageRequest.getDate(),pageRequest.getSearch());
		Long count=driverInfoMapper.driverWorkOrderListCount(pageRequest.getCarGroupId(),pageRequest.getDate(),pageRequest.getSearch());
		Collections.sort(driverOrderVos);
		return new PageResult(count,driverOrderVos);
	}

	public boolean setDriverWorkInfo(Long driverId) {

		List<CloudWorkIDateVo> workDateList = driverWorkInfoMapper.getCloudWorkDateList();

		//获取调用接口的用户信息
		HttpServletRequest request = ServletUtils.getRequest();
		LoginUser loginUser = tokenService.getLoginUser(request);
		Long userId = loginUser.getUser().getUserId();
		List<DriverWorkInfoVo> list = new ArrayList<>();
		if (workDateList != null && workDateList.size() > 0) {
			for (int i = 0; i < workDateList.size(); i++) {
				DriverWorkInfoVo driverWorkInfoVo = new DriverWorkInfoVo();
				driverWorkInfoVo.setDriverId(driverId);
				driverWorkInfoVo.setCalendarDate(workDateList.get(i).getCalendarDate());
				driverWorkInfoVo.setOnDutyRegisteTime(workDateList.get(i).getWorkStart());
				driverWorkInfoVo.setOffDutyRegisteTime(workDateList.get(i).getWorkEnd());
				driverWorkInfoVo.setTodayItIsOnDuty("1111");
				String itIsDuty=workDateList.get(i).getItIsWork();
				if("0000".equals(itIsDuty)){
					driverWorkInfoVo.setLeaveStatus("X999");
				}else if("1111".equals(itIsDuty)){
					driverWorkInfoVo.setLeaveStatus("X003");
				}
				driverWorkInfoVo.setCreatBy(userId);
				driverWorkInfoVo.setCreatTime(DateUtils.getNowDate());
				list.add(driverWorkInfoVo);
			}
			int m = driverWorkInfoMapper.insertDriverWorkInfo(list);
			if (m > 0) {
				int n = driverWorkInfoMapper.updateDriverWork(driverId);
				return true;
			}
		}
		return false;
	}

}
