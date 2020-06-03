package com.hq.ecmp.mscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.DriverNatureEnum;
import com.hq.ecmp.constant.InvitionTypeEnum;
import com.hq.ecmp.constant.RoleConstant;
import com.hq.ecmp.constant.enumerate.DriverStateEnum;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.ICarGroupDriverRelationService;
import com.hq.ecmp.mscore.service.IDriverCarRelationInfoService;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class DriverInfoServiceImpl implements IDriverInfoService

{
	private static final Logger logger = LoggerFactory.getLogger(DriverInfoServiceImpl.class);

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
	private DriverNatureInfoMapper driverNatureInfoMapper;
	@Autowired
	private EcmpUserMapper ecmpUserMapper;
	@Autowired
	private CarGroupDriverRelationMapper carGroupDriverRelationMapper;


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


	/**
	 * 新增驾驶员 （新增页面走过来 或者 审核驾驶员注册通过走过来）
	 * @param driverCreateInfo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean createDriver(DriverCreateInfo driverCreateInfo) throws Exception {

    	//通过手机号和姓名去user表中查询  (只能用手机号区校验，用姓名+加手机号会出问题)
    	EcmpUser query = new EcmpUser();
		//query.setNickName(driverCreateInfo.getDriverName());
		query.setPhonenumber(driverCreateInfo.getMobile());
		List<EcmpUser> selectEcmpUserList = ecmpUserService.selectEcmpUserList(query);
		Long userId=null;
        Long newUserId = null;
		if(null !=selectEcmpUserList && selectEcmpUserList.size()>0){
			//新增驾驶员如果已经是公司员工  那么 1. 绑定用户角色表 2.用户表update驾驶员角色、没工号可以加工号
			userId=selectEcmpUserList.get(0).getUserId();
			//1. 用户角色表  新增的驾驶员是公司员工  则赋予该员工驾驶员角色
			EcmpUserRole ecmpUserRole = new EcmpUserRole();
			ecmpUserRole.setUserId(userId);
			ecmpUserRole.setRoleId(Long.valueOf(RoleConstant.DATA_SCOPE_6));
			ecmpUserRoleMapper.insertEcmpUserRole(ecmpUserRole);
			driverCreateInfo.setUserId(userId);
			String jobNumber = driverCreateInfo.getJobNumber();
			EcmpUser updateEcmpUser = new EcmpUser();
			updateEcmpUser.setUserId(userId);
			updateEcmpUser.setItIsDriver("0");
			updateEcmpUser.setUpdateTime(new Date());
			updateEcmpUser.setUpdateBy(String.valueOf(driverCreateInfo.getOptUserId()));
			if(StringUtil.isNotEmpty(jobNumber)){
				//传入了工号 如果之前没有工号 则更新驾驶员对应的公司员工的工号  如果之前已有工号，这次传进来的必须跟之前的
				updateEcmpUser.setJobNumber(jobNumber);
			}
			//2.用户表update
			ecmpUserService.updateEcmpUserjobNumber(updateEcmpUser);
			//ecmpUserService.updateEcmpUser(updateEcmpUser);
			//ecmpUserMapper.updateEcmpUser(updateEcmpUser);
		}
		//2020.6.1号新改动，新增驾驶员，不赋予驾驶员员工角色。
//		else if(CollectionUtils.isEmpty(selectEcmpUserList) && DriverNatureEnum.OWNER_DRIVER.getKey().equals(driverCreateInfo.getDriverNature())){
//			//如果不是公司已有的员工，而是新来的自有驾驶员（非借调，非外聘）
//			//1. 员工表插入数据
//			LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
//			EcmpUser newDriverUser = EcmpUser.builder().nickName(driverCreateInfo.getDriverName())
//					.phonenumber(driverCreateInfo.getMobile())
//					.ownerCompany(driverCreateInfo.getCompanyId())
//					//直属公司 不属于部门
//					.deptId(driverCreateInfo.getCompanyId())
//					.sex(driverCreateInfo.getGender())
//					.userName(driverCreateInfo.getMobile())
//					.delFlag("0")
//					.itIsDriver("0")
//					.itIsDispatcher("1")
//					.jobNumber(driverCreateInfo.getJobNumber())
//					.userType("00")
//					.build();
//			newDriverUser.setCreateBy(String.valueOf(driverCreateInfo.getOptUserId()));
//			newDriverUser.setCreateTime(new Date());
//			ecmpUserMapper.insertEcmpUser(newDriverUser);
//			newUserId = newDriverUser.getUserId();
//			//2. 插入用户驾驶员角色表
//			EcmpUserRole build = EcmpUserRole.builder().roleId(6L).userId(newUserId).build();
//			ecmpUserRoleMapper.insertEcmpUserRole(build);
//			//3. 插入用户员工角色表
//			EcmpUserRole employee = EcmpUserRole.builder().roleId(5L).userId(newUserId).build();
//			ecmpUserRoleMapper.insertEcmpUserRole(employee);
//			driverCreateInfo.setUserId(newUserId);
//		}


    	//生成驾驶员记录  是否专职
		//Z000   合同制  (自有驾驶员)
		//Z001   在编   （暂时不用）
		//Z002   外聘
		//Z003   借调
		driverCreateInfo.setLockState("0000");
		//初始化驾驶员状态  W001   待审核   V000  生效中   NV00   失效/离职   S444    被删除
		initDriverState(driverCreateInfo);
		//1. 创建驾驶员
		Integer createDriver = driverInfoMapper.createDriver(driverCreateInfo);
    	Long driverId = driverCreateInfo.getDriverId();

    	//生成驾驶员-车队关系记录
    	CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
    	carGroupDriverRelation.setCarGroupId(driverCreateInfo.getCarGroupId());
    	carGroupDriverRelation.setDriverId(driverCreateInfo.getDriverId());
    	carGroupDriverRelation.setCreateBy(driverCreateInfo.getOptUserId().toString());
    	carGroupDriverRelation.setCreateTime(new Date());
    	//2. 绑定驾驶员车队关系
    	carGroupDriverRelationService.insertCarGroupDriverRelation(carGroupDriverRelation);
    	//生成驾驶员-车辆记录
		String regimenIds = driverCreateInfo.getRegimenIds();
		if(StringUtils.isNotEmpty(regimenIds)){
			//如果regimenIds不为空 ，表示由邀请通过的流程  regimenIds表示驾驶员可用车辆Id集合
			List<String> list = Arrays.asList(regimenIds.split(","));
			List<Long> carIds = list.stream().map(a -> Long.valueOf(a)).collect(Collectors.toList());
			driverCreateInfo.setCarId(carIds);
		}
		List<Long> carIdList = driverCreateInfo.getCarId();
    	if(null !=carIdList && carIdList.size()>0){
    		DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
        	if(null !=userId){
        		driverCarRelationInfo.setUserId(userId);
        	}
        	if(newUserId != null){
                driverCarRelationInfo.setUserId(newUserId);
            }
        	driverCarRelationInfo.setDriverId(driverCreateInfo.getDriverId());
        	driverCarRelationInfo.setCarIdList(carIdList);
        	//3. 绑定驾驶员车辆关系
        	driverCarRelationInfoService.batchDriverCarList(driverCarRelationInfo);
    	}
    	//4. 初始化驾驶员排班  自有驾驶员当天开始排班   借调/外聘的从开始日期开始排班
		boolean b = setDriverWorkInfo(driverId,driverCreateInfo.getDriverNature(),driverCreateInfo.getHireBeginTime(),driverCreateInfo.getBorrowBeginTime());
    	if(!b){
    		throw new RuntimeException("驾驶员初始化排班失败");
		}
		//5. 插入驾驶员性质表数据
		Long invitationId = driverCreateInfo.getInvitationId();
    	if (invitationId != null){
			//如果是邀请的驾驶员，则在DriverNatureInfo表插入 driverId即可
			DriverNatureInfo driverNatureInfo = new DriverNatureInfo();
			driverNatureInfo.setDriverId(driverId == null ? null : Long.valueOf(driverId));
			driverNatureInfo.setUpdateBy(String.valueOf(driverCreateInfo.getOptUserId()));
			driverNatureInfo.setInvitationId(driverCreateInfo.getInvitationId());
			driverNatureInfoMapper.updateDriverNatureInfoByInvatationId(driverNatureInfo);
		}else {
			//如果是新增驾驶员，则在驾驶员性质表插入数据
			addDriverNatureInfo(driverId,driverCreateInfo.getDriverNature(),driverCreateInfo.getHireBeginTime(),driverCreateInfo.getHireEndTime(),
					driverCreateInfo.getBorrowBeginTime(),driverCreateInfo.getBorrowEndTime(),driverCreateInfo.getOptUserId());
		}

		return true;
	}

	/**
	 * 初始化驾驶员状态
	 * @param driverCreateInfo
	 */
	@Override
	public void initDriverState(DriverCreateInfo driverCreateInfo) {
		Date nowDate = DateUtils.getNowDate();
		driverCreateInfo.setState(DriverStateEnum.EFFECTIVE.getCode());
		if(DriverNatureEnum.OWNER_DRIVER.getKey().equals(driverCreateInfo.getDriverNature())){
			//自有驾驶员 则初始化为生效中
			driverCreateInfo.setState(DriverStateEnum.EFFECTIVE.getCode());
		}else if(DriverNatureEnum.HIRED_DRIVER.getKey().equals(driverCreateInfo.getDriverNature())){
			//外聘驾驶员
			Date hireBeginTime = driverCreateInfo.getHireBeginTime();
			Date hireEndTime = driverCreateInfo.getHireEndTime();
			if(hireBeginTime == null || hireEndTime == null){
				return;
			}
			if(nowDate.before(hireBeginTime)){
				//如果外聘时间还没到 则初始化为待启用
				driverCreateInfo.setState(DriverStateEnum.WAIT_EFFECTIVE.getCode());
			}else if(nowDate.after(hireBeginTime) && hireEndTime.after(nowDate)){
				//如果外聘时间大于当前时间 且 外聘结束时间还没结束 则初始化为 启用状态
				driverCreateInfo.setState(DriverStateEnum.EFFECTIVE.getCode());
			}else if(hireEndTime.before(nowDate)){
				//如果外聘时间结束时间已经结束 则 初始化为 失效中
				driverCreateInfo.setState(DriverStateEnum.DIMISSION.getCode());
			}
		}else if(DriverNatureEnum.BORROWED_DRIVER.getKey().equals(driverCreateInfo.getDriverNature())){
			//如果是借调的驾驶员
			Date borrowBeginTime = driverCreateInfo.getBorrowBeginTime();
			Date borrowEndTime = driverCreateInfo.getBorrowEndTime();
			if(borrowBeginTime == null || borrowEndTime == null){
				return;
			}
			if(nowDate.before(borrowBeginTime)){
				//如果借调 开始时间还没到 初始化为 待启用状态
				driverCreateInfo.setState(DriverStateEnum.WAIT_EFFECTIVE.getCode());
			}else if(nowDate.after(borrowBeginTime) && borrowEndTime.after(nowDate)){
				//如果借调 开始视角到了 且 结束时间没到 则初始化为启用状态
				driverCreateInfo.setState(DriverStateEnum.EFFECTIVE.getCode());
			}else if(borrowEndTime.before(nowDate)){
				//如果借调时间已经结束 初始化为 已失效
				driverCreateInfo.setState(DriverStateEnum.DIMISSION.getCode());
			}
		}
		Date licenseExpireDate = driverCreateInfo.getLicenseExpireDate();
		//如果行驶证已过期，则初始化状态为已失效
		if(licenseExpireDate != null && nowDate.after(licenseExpireDate)){
			driverCreateInfo.setState(DriverStateEnum.DIMISSION.getCode());
		}
	}

	/***
	 *添加驾驶员性质
	 * @param driverId
	 * @param driverNature
	 * @param hireBeginTime
	 * @param hireEndTime
	 * @param borrowBeginTime
	 * @param borrowEndTime
	 * @return
	 */
	private int addDriverNatureInfo(Long driverId, String  driverNature,Date hireBeginTime,
									Date hireEndTime,Date borrowBeginTime,Date borrowEndTime,Long createBy){
		try{
			DriverNatureInfo driverNatureInfo = new DriverNatureInfo();
			driverNatureInfo.setDriverId(driverId);
			driverNatureInfo.setDriverNature(driverNature);
			driverNatureInfo.setHireBeginTime(hireBeginTime);
			driverNatureInfo.setHireEndTime(hireEndTime);
			driverNatureInfo.setBorrowBeginTime(borrowBeginTime);
			driverNatureInfo.setBorrowEndTime(borrowEndTime);
			driverNatureInfo.setCreateBy(String.valueOf(createBy));
			return driverNatureInfoMapper.addDriverNatureInfo(driverNatureInfo);
		}catch(Exception e){
			logger.error("addDriverNatureInfo error",e);
		}
		return 0;
	}
    /**
     * 修改驾驶员
     * @param driverCreateInfo
     * @return
     */
    @Override
    public boolean updateDriver(DriverCreateInfo driverCreateInfo) throws Exception {
        //培铭说编辑驾驶员 目前就所属车队 和 驾驶员性质（自有，外聘，借调）不可更改，外聘和借调时间可编辑
		//其余的都可编辑   但是自有驾驶员 姓名和电话修改了，不对员工表做修改
    	//1.驾驶员姓名
		//2.驾驶员手机号
		//3.驾驶员性别
		//4.身份证号码
		//5.驾驶证类型
		//6.驾驶证号码
		//7.驾驶证照片
		//8.初次领证日期
		//9.驾驶证有效期:开始时间，结束时间
		//10如果是外聘 ： 外聘开始时间，
		// 11.外聘结束时间
		//如果是借调： 12.借调开始时间
		// 13.借调结束时间
		//14.工号
		//15.可用车辆

    	//1.修改驾驶员
        driverCreateInfo.setUpdateTime(DateUtils.getNowDate());
        Integer createDriver = driverInfoMapper.updateDriver(driverCreateInfo);
        Long driverId = driverCreateInfo.getDriverId();
        //修改驾驶员-车队关系记录  此处所属车队不可编辑
       /* CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
        carGroupDriverRelation.setCarGroupId(driverCreateInfo.getCarGroupId());
        carGroupDriverRelation.setDriverId(driverCreateInfo.getDriverId());
        carGroupDriverRelation.setCreateBy(driverCreateInfo.getOptUserId().toString());
        carGroupDriverRelation.setCreateTime(new Date());
        carGroupDriverRelationService.updateCarGroupDriverRelation(carGroupDriverRelation);*/
        //修改驾驶员-车辆记录
       /* DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
        driverCarRelationInfo.setUserId(driverCreateInfo.getUserId());
        driverCarRelationInfo.setDriverId(driverCreateInfo.getDriverId());
        driverCarRelationInfo.setCarIdList(driverCreateInfo.getCarId());
        driverCarRelationInfoService.updateBatchDriverCarList(driverCarRelationInfo);*/
        //2.删除驾驶员绑定的车辆
        driverCarRelationInfoService.deleteCarByDriverId(driverId);
        //3.生成驾驶员-车辆记录
		List<Long> carId = driverCreateInfo.getCarId();
		if(CollectionUtils.isNotEmpty(carId)){
			DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
			driverCarRelationInfo.setUserId(driverCreateInfo.getUserId());
			driverCarRelationInfo.setDriverId(driverCreateInfo.getDriverId());
			driverCarRelationInfo.setCarIdList(carId);
			driverCarRelationInfoService.batchDriverCarList(driverCarRelationInfo);
		}
        //4.修改车辆性质表信息  如果是外聘车或者借调车才可能更改此表
		if(DriverNatureEnum.HIRED_DRIVER.getKey().equals(driverCreateInfo.getDriverNature())){
			//如果是外聘驾驶员
			DriverNatureInfo driverNatureInfo = new DriverNatureInfo();
			driverNatureInfo.setDriverId(driverCreateInfo.getDriverId());
			driverNatureInfo.setUpdateBy(String.valueOf(driverCreateInfo.getOptUserId()));
			driverNatureInfo.setHireBeginTime(driverCreateInfo.getHireBeginTime());
			driverNatureInfo.setHireEndTime(driverCreateInfo.getHireEndTime());
			driverNatureInfoMapper.updateDriverNatureInfo(driverNatureInfo);
		}
		if(DriverNatureEnum.BORROWED_DRIVER.getKey().equals(driverCreateInfo.getDriverNature())){
			//如果是借调驾驶员
			DriverNatureInfo driverNatureInfo = new DriverNatureInfo();
			driverNatureInfo.setDriverId(driverCreateInfo.getDriverId());
			driverNatureInfo.setBorrowBeginTime(driverCreateInfo.getBorrowBeginTime());
			driverNatureInfo.setBorrowEndTime(driverCreateInfo.getBorrowEndTime());
			driverNatureInfo.setUpdateBy(String.valueOf(driverCreateInfo.getOptUserId()));
			driverNatureInfoMapper.updateDriverNatureInfo(driverNatureInfo);
		}
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
	public DriverQueryResult queryDriverDetail(Long driverId) throws Exception {
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
		queryDriverDetail.setOwnCarCount(driverCarRelationInfoService.queryDriverUseCarCount(driverId));
		queryDriverDetail.setCarId(carId);
		//查询驾驶员性质
		DriverNatureInfo driverNatureInfo = driverNatureInfoMapper.getDriverNatureInfo(driverId);
		if(driverNatureInfo != null){
			queryDriverDetail.setHireBeginTime(driverNatureInfo.getHireBeginTime());
			queryDriverDetail.setHireEndTime(driverNatureInfo.getHireEndTime());
			queryDriverDetail.setBorrowBeginTime(driverNatureInfo.getBorrowBeginTime());
			queryDriverDetail.setBorrowEndTime(driverNatureInfo.getBorrowEndTime());
		}
		return queryDriverDetail;
	}
    /**
     *驾驶员总数
     */
    @Override
    public int queryCompanyDriverCount(Long companyId){
        return driverInfoMapper.queryCompanyDriver(companyId);
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
			Long ownerCompany = carGroupInfo.getCompanyId();
			if(null !=ownerCompany){
				EcmpOrg company = ecmpOrgMapper.selectEcmpOrgById(ownerCompany);
				if(null !=company){
					carGroupDriverInfo.setCompanyName(company.getDeptName());
				}
			}
			Long ownerOrg = carGroupInfo.getCompanyId();
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
	public ApiResponse checkMobile(String mobile) {
		ApiResponse  apiResponse = new ApiResponse();
		DriverInfo driverInfo = new DriverInfo();
		driverInfo.setMobile(mobile);
		List<DriverInfo> selectDriverInfoList = driverInfoMapper.selectDriverInfoList(driverInfo);
		if(!selectDriverInfoList.isEmpty()){
			if(StringUtils.isNotBlank(selectDriverInfoList.get(0).getMobile())){
				apiResponse.setCode(ApiResponse.ERROR_CODE);
				apiResponse.setMsg("该手机号驾驶员已存在");
			}
		}else{
			String phonenumber =mobile;
			EcmpUser ecmpUser = ecmpUserMapper.getUserByPhone(phonenumber);
			if (ecmpUser==null){
				apiResponse.setCode(ApiResponse.ERROR_CODE);
				apiResponse.setMsg("无此用户");
			}else if(StringUtils.isNotBlank(ecmpUser.getPhonenumber())) {
				apiResponse.setCode(ApiResponse.SUCCESS_CODE);
				apiResponse.setData(ecmpUser);
			}
		}
		return apiResponse;
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

	@Override
	public boolean setDriverWorkInfo(Long driverId,String driverNature,Date hireBeginTime,Date borrowBeginTime) {

		List<CloudWorkIDateVo> workDateList = driverWorkInfoMapper.getCloudWorkDateList();
		//获取调用接口的用户信息
		HttpServletRequest request = ServletUtils.getRequest();
		LoginUser loginUser = tokenService.getLoginUser(request);
		Long userId = loginUser.getUser().getUserId();
		List<DriverWorkInfoVo> list = new ArrayList<>();
		Calendar todayStart = Calendar.getInstance();
		if(DriverNatureEnum.BORROWED_DRIVER.equals(driverNature)){
			//如果是外聘驾驶员 排班从外聘开始时间开始
			todayStart.setTime(hireBeginTime);
		}
		if(DriverNatureEnum.HIRED_DRIVER.equals(driverNature)){
			//如果是戒掉驾驶员 排班从借调开始日期开始
			todayStart.setTime(borrowBeginTime);
		}
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		//今天开始时间
		long todayStartTime = todayStart.getTime().getTime();
		//从今天开始到年末所有天拿过来  工作日默认都在上班 休息日默认都在休假
		//以前的日期设置为空
		if (workDateList != null && workDateList.size() > 0) {
			for (int i = 0; i < workDateList.size(); i++) {
				DriverWorkInfoVo driverWorkInfoVo = new DriverWorkInfoVo();
				driverWorkInfoVo.setDriverId(driverId);
				//日历某一天
				Date calendarDate = workDateList.get(i).getCalendarDate();
				long calendarDateTime = calendarDate.getTime();
				driverWorkInfoVo.setCalendarDate(calendarDate);
				//当天计划上班时间
				driverWorkInfoVo.setOnDutyRegisteTime(workDateList.get(i).getWorkStart());
				//当天计划下班时间
				driverWorkInfoVo.setOffDutyRegisteTime(workDateList.get(i).getWorkEnd());
				//今天实际是否上班:today_it_is_on_duty ;   0000   上班 , 1111    没上班
				String itIsWork=workDateList.get(i).getItIsWork();
				if(calendarDateTime >= todayStartTime){
					//今天之后的 该上班的默认都上班，不该上班的默认都休假  当日默认上班
					if("0000".equals(itIsWork)){
						//itIsWork是否是工作日   0000    上班   1111    休假
						driverWorkInfoVo.setTodayItIsOnDuty("0000");
						//leave_status  休假状态: X000  病假,  X002  年假 ,  X003 公休, X999  正常排班工作
						driverWorkInfoVo.setLeaveStatus("X999");
					}else if("1111".equals(itIsWork)){
						driverWorkInfoVo.setTodayItIsOnDuty("1111");
						//该休假的默认都休假
						driverWorkInfoVo.setLeaveStatus("X003");
					}
				}else {
					//今天之前的设置为空  今天实际是否上班: TodayItIsOnDuty  休假状态LeaveStatus
					driverWorkInfoVo.setTodayItIsOnDuty("");
					driverWorkInfoVo.setLeaveStatus("");
				}
				driverWorkInfoVo.setCreatBy(userId);
				driverWorkInfoVo.setCreatTime(DateUtils.getNowDate());
				list.add(driverWorkInfoVo);
			}
			int m = driverWorkInfoMapper.insertDriverWorkInfo(list);
			return m > 0;
		}
		return false;
	}

	/**
	 * 补单获取驾驶员列表
	 * @param driverInfo
	 * @return
	 */
	@Override
	public List<DriverInfo> supplementObtainDriver(DriverInfo driverInfo) {
		return driverInfoMapper.supplementObtainDriver(driverInfo);
	}
	/**
	 * 调度选司机以后自动解锁未解锁司机
	 */
	@Override
	public void unlockDrivers() {
		driverInfoMapper.unlockDrivers();
	}

	/***
	 * add by liuzb 根据驾驶员性质定时更新状态
	 * @throws Exception
	 */
	@Override
	public void updateDriverStatusService() throws Exception {
		List<DriverNatureInfo> list = driverNatureInfoMapper.getDriverNatureInfoList();
		if(null==list || list.size()<=0){
			logger.info("updateDriverStatusService query DriverNatureInfo is null");
			return;
		}
		for(DriverNatureInfo data : list ){
			updateDriverInfo(data,compareDate(data));
		}
	}

	/***
	 * add by liuzb
	 * 这里抓住异常需要保证定时任务当前数据异常下一个数据可继续执行
	 * @param data
	 * @param key
	 */
	private  void  updateDriverInfo(DriverNatureInfo data ,String key){
		try{
			if(null==key){
				return;
			}
			DriverCreateInfo driverCreateInfo = new DriverCreateInfo();
			driverCreateInfo.setDriverId(data.getDriverId());
			driverCreateInfo.setState(key);
			Integer sum = driverInfoMapper.updateDriver(driverCreateInfo);
			logger.info(sum>0?"key:"+data.getDriverId()+"更新状态成功":"key:"+data.getDriverId()+"更新状态失败");
		}catch(Exception e){
			logger.error("key:"+data.getDriverId()+"----updateDriverStatusService error ",e);
		}
	}

	/***
	 *add by liuzb
	 * 根据驾驶员性质获取状态
	 * @param driverNatureInfo
	 * @return
	 * @throws Exception
	 */
	private String compareDate(DriverNatureInfo driverNatureInfo)throws Exception{
		Date date = new Date();
		if("Z002".equals(driverNatureInfo.getDriverNature())){/**外聘*/
			//如果日期缺少 则为生效
			//update by huzj
			if(( driverNatureInfo.getHireBeginTime()==null)|| (driverNatureInfo.getHireEndTime()==null)){
				return DriverStateEnum.EFFECTIVE.getCode();/**外聘时间到，生效*/
			}
			if(-1==driverNatureInfo.getHireBeginTime().compareTo(date) && -1==date.compareTo(driverNatureInfo.getHireEndTime())){
				return DriverStateEnum.EFFECTIVE.getCode();/**外聘时间到，生效*/
			}
			if(-1==driverNatureInfo.getHireEndTime().compareTo(date)){
				return DriverStateEnum.DIMISSION.getCode();/**外聘时间结束，失效*/
			}
		}else if("Z003".equals(driverNatureInfo.getDriverNature())){/**借调*/
			//如果日期缺少 则为生效
			//update by huzj
			if(( driverNatureInfo.getBorrowBeginTime()==null)|| (driverNatureInfo.getBorrowEndTime()==null)){
				return DriverStateEnum.EFFECTIVE.getCode();/**外聘时间到，生效*/
			}
			if(-1==driverNatureInfo.getBorrowBeginTime().compareTo(date) && -1==date.compareTo(driverNatureInfo.getBorrowEndTime())){
				return DriverStateEnum.EFFECTIVE.getCode();/**借调时间到，实效*/
			}
			if(-1==driverNatureInfo.getBorrowEndTime().compareTo(date)){
				return DriverStateEnum.DIMISSION.getCode();/**借调时间结束，失效*/
			}
		}
		return null;
	}

	/***
	 *获取所有生效驾驶员
	 * @throws Exception
	 */
	@Override
	public void updateDriverInvalid() throws Exception {
		List<DriverInfo> list = driverInfoMapper.getDriverInvalid();
		if(null==list || list.size()<=0){
			logger.info("updateDriverInvalid query DriverInfo is null");
			return;
		}
		for(DriverInfo data : list ){
			if(compareDate(data.getLicenseExpireDate())){
                 /**驾驶员失效日期到了，或者过了失效日期*/
				int i = driverInfoMapper.updateDriverStatus(data.getDriverId(),DriverStateEnum.DIMISSION.getCode());
				logger.info("key:"+data.getDriverId()+"updateDriverStatus----"+i);
			}
		}
	}

	/***
	 *
	 * @param licenseExpireDate
	 * @return
	 * @throws Exception
	 */
	private boolean compareDate(Date licenseExpireDate)throws Exception{
		Date date = new Date();
		if(null!=licenseExpireDate){
			if(1==date.compareTo(licenseExpireDate) ){
				return true;
			}
		}
		return false;
	}

	/***
	 * add by liuzb
	 *获取当前所有的失效离职驾驶员
	 */
	@Override
	public void updateDepartureDriver(){
		List<DriverInfo> list = driverInfoMapper.getDepartureDriver();
		if(null==list || list.size()<=0){
			logger.info("updateDriverInvalid query DriverInfo is null");
			return;
		}
		for(DriverInfo data : list ){
			unbindDepartureDriver(data);
		}
	}

	/***
	 * add liuzb
	 * 接触绑定驾驶员
	 * @param data
	 */
	private void unbindDepartureDriver(DriverInfo data){
         try{
			 int i = 0;
             /***解绑驾驶员的车辆*/
             i = carGroupDriverRelationMapper.deleteCarGroupDriverRelationById(data.getDriverId());
             logger.info("key:"+data.getDriverId()+"驾驶员解绑车辆结束，解绑状态为："+i);
             /**解绑驾驶员的车队*/
			 i = driverCarRelationInfoMapper.deleteCarByDriverId(data.getDriverId());
			 logger.info("key:"+data.getDriverId()+"驾驶员解绑车队结束，解绑状态为："+i);
		 }catch(Exception e){
            logger.error("key:"+data.getDriverId()+"unbindDepartureDriver error",e);
		 }
	}
}




