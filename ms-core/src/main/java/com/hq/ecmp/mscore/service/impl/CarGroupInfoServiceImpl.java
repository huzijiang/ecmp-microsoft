package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.hq.api.system.domain.SysDriver;
import com.hq.api.system.domain.SysUser;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.common.utils.StringUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.OrgConstant;
import com.hq.ecmp.mscore.vo.CityInfo;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.CarGroupDTO;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import com.hq.ecmp.mscore.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarGroupInfoServiceImpl implements ICarGroupInfoService
{
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;
    @Autowired
    private CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private CarGroupDriverRelationMapper carGroupDriverRelationMapper;
    @Autowired
    private CarInfoMapper carInfoMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private OrderStateTraceInfoMapper orderStateTraceInfoMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ChinaCityMapper chinaCityMapper;
    //是否执行车队删除的标志
    private Boolean carGroupDelFlag = false;
    @Autowired
    private EcmpUserRoleMapper ecmpUserRoleMapper;
    @Autowired
    private  CarGroupServeScopeInfoMapper carGroupServeScopeInfoMapper;
    @Autowired
    private CarGroupServeOrgRelationMapper carGroupServeOrgRelationMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param carGroupId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarGroupInfo selectCarGroupInfoById(Long carGroupId)
    {
        return carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarGroupInfo> selectCarGroupInfoList(CarGroupInfo carGroupInfo)
    {
        return carGroupInfoMapper.selectCarGroupInfoList(carGroupInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarGroupInfo(CarGroupInfo carGroupInfo)
    {
        carGroupInfo.setCreateTime(DateUtils.getNowDate());
        return carGroupInfoMapper.insertCarGroupInfo(carGroupInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarGroupInfo(CarGroupInfo carGroupInfo)
    {
        carGroupInfo.setUpdateTime(DateUtils.getNowDate());
        return carGroupInfoMapper.updateCarGroupInfo(carGroupInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carGroupIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarGroupInfoByIds(Long[] carGroupIds)
    {
        return carGroupInfoMapper.deleteCarGroupInfoByIds(carGroupIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carGroupId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarGroupInfoById(Long carGroupId)
    {
        return carGroupInfoMapper.deleteCarGroupInfoById(carGroupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCarGroupAndDispatcher(CarGroupDTO carGroupDTO,Long userId) throws Exception {
        CarGroupInfo carGroupInfo = new CarGroupInfo();
        //查询省份代码
        String city = carGroupDTO.getCity();
        String provinceCode = null;
        if(!ObjectUtils.isEmpty(city)){
            CityInfo cityInfo = chinaCityMapper.queryCityByCityCode(city);
            if(!ObjectUtils.isEmpty(cityInfo)){
                provinceCode = cityInfo.getProvinceCode();
                //省份代码
                carGroupInfo.setProvince(provinceCode);
            }
        }
        //新增车队信息
        insertCarGroupInfo(carGroupDTO, userId, carGroupInfo);
        Long carGroupId = carGroupInfo.getCarGroupId();
        //2.车队绑定 调度员（不是调度员角色，则需要赋予角色，涉及两张表）
        Long[] userIds = carGroupDTO.getUserIds();
        saveCarGroupDispatchers(userIds, userId, carGroupId);
        //3.保存车队服务城市表
        CarGroupServeScopeInfo carGroupServeScopeInfo = CarGroupServeScopeInfo.builder().carGroupId(carGroupInfo.getCarGroupId())
                .city(city).province(provinceCode).build();
        carGroupServeScopeInfo.setCreateBy(String.valueOf(userId));
        carGroupServeScopeInfo.setCreateTime(new Date());
        carGroupServeScopeInfo.setUpdateBy(null);
        carGroupServeScopeInfo.setUpdateTime(null);
        int k = carGroupServeScopeInfoMapper.insert(carGroupServeScopeInfo);
        if(k != 1){
            throw new RuntimeException("车队服务范围新增失败");
        }
        //4.添加车队服务关系表
        //A.允许外部调用公司
        Long owneCompany = carGroupDTO.getCompanyId();
        Long[] companyIds = carGroupDTO.getCompanyIds();
        if(companyIds.length > 0){
            saveCarGroupServeCompany(carGroupId, companyIds,owneCompany);
        }
        //B.服务本公司部门
        Long[] deptIds = carGroupDTO.getDeptIds();
        if(deptIds.length > 0){
            saveCarGroupServeDepts(carGroupId, deptIds);
        }

    }

    /**
     * 保存车队允许外部调度公司数据
     * @param carGroupId
     * @param companyIds
     */
    private void saveCarGroupServeCompany(Long carGroupId, Long[] companyIds,Long owneCompany) {

        CarGroupServeOrgRelation carGroupServeOrgRelation;
        for (Long companyId : companyIds) {
            if(owneCompany != null && owneCompany.equals(companyId)){
                continue;
            }
            carGroupServeOrgRelation = CarGroupServeOrgRelation.builder()
            .carGroupId(carGroupId).deptId(companyId).type(OrgConstant.OUTER_COMPANY).build();
            int i = carGroupServeOrgRelationMapper.insert(carGroupServeOrgRelation);
            if(i != 1){
                throw new RuntimeException("新增车队服务外部公司失败");
            }
        }
    }

    /**
     *
     *保存车队服务部门表数据
     */
    private void saveCarGroupServeDepts(Long carGroupId, Long[] deptIds) {
        CarGroupServeOrgRelation carGroupServeOrgRelation;
        for (Long deptId : deptIds) {
            carGroupServeOrgRelation = CarGroupServeOrgRelation.builder()
                    .carGroupId(carGroupId).deptId(deptId).type(OrgConstant.INNER_ORG).build();
            int n = carGroupServeOrgRelationMapper.insert(carGroupServeOrgRelation);
            if(n != 1){
                throw new RuntimeException("新增车队服务部门失败");
            }
        }

    }

    /**
     * 新增车队表信息
     * @param carGroupDTO
     * @param userId
     * @param carGroupInfo
     */
    private void insertCarGroupInfo(CarGroupDTO carGroupDTO, Long userId, CarGroupInfo carGroupInfo) {
        Long[] companyIds = carGroupDTO.getCompanyIds();
        if(companyIds.length == 0){
            carGroupInfo.setAllowOuterDispatch(OrgConstant.NOT_ALLOW_OUTER_DISPATCH);
        }else if(companyIds.length == 1 && companyIds[0].equals(carGroupDTO.getCompanyId())){
            carGroupInfo.setAllowOuterDispatch(OrgConstant.NOT_ALLOW_OUTER_DISPATCH);
        }else {
            carGroupInfo.setAllowOuterDispatch(OrgConstant.ALLOW_OUTER_DISPATCH);
        }
        //车队编码
        carGroupInfo.setCarGroupCode(carGroupDTO.getCarGroupCode());
        //父车队id
        Long parentCarGroupId = carGroupDTO.getParentCarGroupId();
        if(parentCarGroupId == null){
            parentCarGroupId = 0L;
        }
        carGroupInfo.setParentCarGroupId(carGroupDTO.getOwnerOrg());
        //所属城市编码
        carGroupInfo.setCity(carGroupDTO.getCity());
        //车队名称
        carGroupInfo.setCarGroupName(carGroupDTO.getCarGroupName());
        //车队详细地址
        carGroupInfo.setFullAddress(carGroupDTO.getFullAddress());
        //车队短地址
        carGroupInfo.setShortAddress(carGroupDTO.getShortAddress());
        //所属组织
        //车队负责人  冗余字段 暂无数据
        carGroupInfo.setLeader(carGroupDTO.getLeader());
        //创建人
        carGroupInfo.setCreateBy(String.valueOf(userId));
        //驻地经度
        carGroupInfo.setLongitude(ObjectUtils.isEmpty(carGroupDTO.getLongitude()) ? null : Float.valueOf(carGroupDTO.getLongitude()));
        //驻地纬度
        carGroupInfo.setLatitude(ObjectUtils.isEmpty(carGroupDTO.getLatitude()) ? null : Float.valueOf(carGroupDTO.getLatitude()));
        //车队座机
        carGroupInfo.setTelephone(carGroupDTO.getTelephone());
        //所属公司
        carGroupInfo.setCompanyId(carGroupDTO.getOwneCompany());
        //初始化可用
        carGroupInfo.setState("Y000");
        //1.保存车队
        int i = insertCarGroupInfo(carGroupInfo);
        if(i != 1){
            throw new RuntimeException("新增车队失败");
        }
    }

    /*新增车队主管*/

    /**
     *
     * @param userIds  调度员用户id数组
     * @param userId    登录用户id
     * @param carGroupId 车队id
     * @throws Exception
     */
    private void saveCarGroupDispatchers(Long[] userIds, Long userId, Long carGroupId) throws Exception {
        if(userIds != null && userIds.length >0){
            CarGroupDispatcherInfo carGroupDispatcherInfo = null;
            for (Long id : userIds) {
                //1. 判断是否是调度员角色
                EcmpUserRole build = EcmpUserRole.builder().userId(id).roleId(4L).build();
                List<EcmpUserRole> ecmpUserRoles = ecmpUserRoleMapper.
                        selectEcmpUserRoleList(build);
                if (CollectionUtils.isEmpty(ecmpUserRoles)) {
                    //2.如果不是调度员角色 则 A.员工表赋予调度员角色
                    EcmpUserVo ecmpUserVo = new EcmpUserVo();
                    ecmpUserVo.setUserId(id);
                    ecmpUserVo.setUpdateBy(String.valueOf(userId));
                    ecmpUserVo.setUpdateTime(new Date());
                    // 是调度员为 1 ；不是调度员为 0
                    ecmpUserVo.setItIsDispatcher("1");
                    int i = ecmpUserMapper.updateEcmpUser(ecmpUserVo);
                    if(i != 1){
                        throw new RuntimeException("调度员角色赋予失败");
                    }
                    //B.员工该角色表赋予调度员角色
                    int j = ecmpUserRoleMapper.insertEcmpUserRole(EcmpUserRole.builder().roleId(4L).userId(id).build());
                    if(j != 1){
                        throw new RuntimeException("调度员角色赋予失败");
                    }
                }
                //3.是不是調度員 最后都得增加调度员跟车队关系表
                carGroupDispatcherInfo = new CarGroupDispatcherInfo();
                //车队id
                carGroupDispatcherInfo.setCarGroupId(carGroupId);
                //调度员id
                carGroupDispatcherInfo.setUserId(id);
                //调度员名字
                EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(id);
                if(ecmpUser != null){
                    String userName = ecmpUser.getNickName();
                    carGroupDispatcherInfo.setName(userName);
                }
                //创建人
                carGroupDispatcherInfo.setCreateBy(String.valueOf(userId));
                //创建时间
                carGroupDispatcherInfo.setCreateTime(new Date());
                int j = carGroupDispatcherInfoMapper.insertCarGroupDispatcherInfo(carGroupDispatcherInfo);
                if (j != 1) {
                    throw new Exception("新增车队绑定调度员失败");
                }
            }
        }
    }

    /**
     * 车队详情
     * @param carGroupId
     * @return
     */
    @Override
    public CarGroupDetailVO getCarGroupDetail(Long carGroupId) {
        //1.查询车队信息
        CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
        if(carGroupInfo == null){
            return null;
        }
        //根据cityCode查询城市名字
        CityInfo cityInfo = chinaCityMapper.queryCityByCityCode(carGroupInfo.getCity());
        //查询所属组织名字
        Long ownerOrg = carGroupInfo.getCompanyId();
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(ownerOrg);
        String deptName = null;
        if(ecmpOrg != null){
            deptName = ecmpOrg.getDeptName();
        }
        CarGroupDetailVO carGroupDetailVO = CarGroupDetailVO.builder()
                //车队编号
                .carGroupCode(carGroupInfo.getCarGroupCode())
                //车队名字
                .carGroupName(carGroupInfo.getCarGroupName())
                //所属组织
                .ownerOrg(ownerOrg)
                //所属组织名字
                .ownerOrgName(deptName)
                //所属城市
                .cityName(cityInfo.getCityName())
                //详细地址
                .fullAddress(carGroupInfo.getFullAddress())
                //短地址
                .shortAddress(carGroupInfo.getShortAddress())
                .build();
        List<UserVO> dispatchers = getCarGroupDispatchers(carGroupId);
        carGroupDetailVO.setDispatchers(dispatchers);
        //查询车队人数
        int countDriver = carGroupDriverRelationMapper.selectCountDriver(carGroupId);
        carGroupDetailVO.setCountDriver(countDriver);
        return carGroupDetailVO;
    }

    /**
     * 查询车队所有调度员
     * @param carGroupId
     * @return
     */
    private List<UserVO>  getCarGroupDispatchers(Long carGroupId) {
        //查询调度员列表
        CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
        carGroupDispatcherInfo.setCarGroupId(carGroupId);
        List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
        if(CollectionUtils.isEmpty(carGroupDispatcherInfos)){
            //没有调度员 则返回空
            return null;
        }
        //调度员id集合
        List<Long> list = carGroupDispatcherInfos.stream().map(CarGroupDispatcherInfo::getUserId).collect(Collectors.toList());
        //2.查询调度员 姓名、电话
        List<UserVO> dispatchers = new ArrayList<>();
        UserVO userVO = null;
        if(!CollectionUtils.isEmpty(list)){
            for (Long userId : list) {
                userVO = new UserVO();
                EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(userId);
                if(!ObjectUtils.isEmpty(ecmpUser)){
                    userVO.setUserId(userId);
                    userVO.setUserPhone(ecmpUser.getPhonenumber());
                    userVO.setUserName(ecmpUser.getNickName());
                    dispatchers.add(userVO);
                }
            }
        }
        return dispatchers;
    }

    /**
     * 修改车队
     * @param carGroupDTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCarGroup(CarGroupDTO carGroupDTO,Long userId) throws Exception {
        CarGroupInfo carGroupInfo = new CarGroupInfo();
        //查询省份代码
        String city = carGroupDTO.getCity();
        String provinceCode = null;
        if(!ObjectUtils.isEmpty(city)){
            CityInfo cityInfo = chinaCityMapper.queryCityByCityCode(city);
            if(!ObjectUtils.isEmpty(cityInfo)){
                provinceCode = cityInfo.getProvinceCode();
                //省份代码
                carGroupInfo.setProvince(provinceCode);
            }
        }
        //1.1修改车队信息
        updateCarGroupInfo(carGroupDTO, userId, carGroupInfo, city);
        //1.2 修改车队服务城市
        CarGroupServeScopeInfo carGroupServeScopeInfo = CarGroupServeScopeInfo.builder()
                .carGroupId(carGroupDTO.getCarGroupId())
                .city(city).province(provinceCode).build();
        carGroupServeScopeInfo.setUpdateBy(String.valueOf(userId));
        carGroupServeScopeInfo.setUpdateTime(new Date());
        int k = carGroupServeScopeInfoMapper.updateInfo(carGroupServeScopeInfo);
        //2 修改调度员信息 先删除 再新增
        Long carGroupId = carGroupDTO.getCarGroupId();
        //2.1 删除调度员 需要判断调度员角色是否能继续保留
        deleteCarGroupDispatcher(carGroupId,userId);
        //2.2 绑定车队调度员
        Long[] userIds = carGroupDTO.getUserIds();
        saveCarGroupDispatchers(userIds,userId,carGroupId);
        //3.1 删除车队服务部门 及 外部调度公司
        carGroupServeOrgRelationMapper.deleteById(carGroupDTO.getCarGroupId());
        //3.2 保存服务部门
        Long[] deptIds = carGroupDTO.getDeptIds();
        if(deptIds.length > 0){
            saveCarGroupServeDepts(carGroupId,deptIds);
        }
        //3.3 保存允许调度的公司
        Long owneCompany = carGroupDTO.getCompanyId();
        Long[] companyIds = carGroupDTO.getCompanyIds();
        if(companyIds.length > 0){
            saveCarGroupServeCompany(carGroupId,companyIds,owneCompany);
        }
    }

    /**
     * 删除车队调度员  判断调度员是否是其他车队的调度员 如果不是其他车队的调度员 则
     * A员工表 员工角色修改为不是调度员 B员工角色表删除调度员角色
     * @param carGroupId
     */
    private void deleteCarGroupDispatcher(Long carGroupId,Long userId) {
        //1.根据车队id查询调度员集合
        CarGroupDispatcherInfo carGroupDispatcherInfo = CarGroupDispatcherInfo.builder().carGroupId(carGroupId).build();
        List<CarGroupDispatcherInfo> list = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
        //2..删除调度员车队关系
        int n = carGroupDispatcherInfoMapper.deleteCarGroupDispatcherInfoByGroupId(carGroupId);
        if(!CollectionUtils.isEmpty(list)){
            //调度员的userId集合
            List<Long> collect = list.stream().map(CarGroupDispatcherInfo::getUserId).collect(Collectors.toList());
            for (Long id : collect) {
                if(id == null){
                    continue;
                }
                CarGroupDispatcherInfo build = CarGroupDispatcherInfo.builder().userId(id).build();
                //查询调度员负责的车队关系集合
                List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(build);
                //如果不是其他车队调度员
                if(CollectionUtils.isEmpty(carGroupDispatcherInfos)){
                    //A 员工表修改用户角色为 不是调度员
                    EcmpUserVo ecmpUserVo = new EcmpUserVo();
                    ecmpUserVo.setUserId(id);
                    ecmpUserVo.setUpdateTime(new Date());
                    ecmpUserVo.setUpdateBy(String.valueOf(userId));
                    // 是调度员为 1 ；不是调度员为 0
                    ecmpUserVo.setItIsDispatcher("0");
                    int m = ecmpUserMapper.updateEcmpUser(ecmpUserVo);
                    if(m != 1){
                        throw new RuntimeException("调度员用户表角色修改失败");
                    }
                    //B 员工角色表 删除员工角色表数据
                    EcmpUserRole ecmpUserRole = EcmpUserRole.builder().roleId(4L).userId(id).build();
                    int a = ecmpUserRoleMapper.deleteUserRole(ecmpUserRole);
                }
            }
        }
    }

    /**
     * 修改车队信息
     * @param carGroupDTO
     * @param userId
     * @param carGroupInfo
     * @param city
     * @throws Exception
     */
    private void updateCarGroupInfo(CarGroupDTO carGroupDTO, Long userId, CarGroupInfo carGroupInfo, String city) throws Exception {
        Long[] companyIds = carGroupDTO.getCompanyIds();
        if(companyIds.length == 0){
            carGroupInfo.setAllowOuterDispatch(OrgConstant.NOT_ALLOW_OUTER_DISPATCH);
        }else if(companyIds.length == 1 && companyIds[0].equals(carGroupDTO.getCompanyId())){
            carGroupInfo.setAllowOuterDispatch(OrgConstant.NOT_ALLOW_OUTER_DISPATCH);
        } else {
            carGroupInfo.setAllowOuterDispatch(OrgConstant.ALLOW_OUTER_DISPATCH);
        }
        //所属城市编码
        carGroupInfo.setCity(city);
        carGroupInfo.setCarGroupId(carGroupDTO.getCarGroupId());
        //车队编码
        carGroupInfo.setCarGroupCode(carGroupDTO.getCarGroupCode());
        //父id
        carGroupInfo.setParentCarGroupId(carGroupDTO.getParentCarGroupId());
        //车队名称
        carGroupInfo.setCarGroupName(carGroupDTO.getCarGroupName());
        //车队详细地址
        carGroupInfo.setFullAddress(carGroupDTO.getFullAddress());
        //车队短地址
        carGroupInfo.setShortAddress(carGroupDTO.getShortAddress());
        //车队负责人 冗余字段 暂无数据
        carGroupInfo.setLeader(carGroupDTO.getLeader());
        //修改人
        carGroupInfo.setUpdateBy(String.valueOf(userId));
        //省份代码
        carGroupInfo.setProvince(carGroupDTO.getProvince());
        //驻地经度
        String longitude = carGroupDTO.getLongitude();
        if(!ObjectUtils.isEmpty(longitude) ){
            carGroupInfo.setLongitude(Float.valueOf(longitude));
        }
        //驻地纬度
        String latitude = carGroupDTO.getLatitude();
        if(!ObjectUtils.isEmpty(latitude)){
            carGroupInfo.setLatitude(Float.valueOf(latitude));
        }
        //车队座机
        carGroupInfo.setTelephone(carGroupDTO.getTelephone());
        //1.修改车队
        int i = updateCarGroupInfo(carGroupInfo);
        if(i != 1){
            throw new Exception("修改车队信息失败");
        }
    }

    /**
     * 禁用车队
     * @param carGroupId
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableCarGroup(Long carGroupId, Long userId) throws Exception {
        //递归禁用车队及下属车队的驾驶员和车辆
        changeState(carGroupId, null, String.valueOf(userId), CarConstant.DISABLE_CAR_GROUP,
                CarConstant.DISABLE_CAR, CarConstant.DISABLE_DRIVER_TYPE);
    }

    //查询车队下的可用驾驶员
    public List<DriverVO> selectGroupEffectiveDrives(Long carGroupId){
        List<DriverVO> list = carGroupDriverRelationMapper.selectGroupEffectiveDrives(carGroupId);
        return list;
    }

    //查询车队下的可用车辆ids
    public List<Long> getGroupEffectiveCarIds(Long carGropuId){
        List<Long> list = carInfoMapper.selectGroupEffectiveCarIds(carGropuId);
        return list;
    }


    /**
     * 启用车队
     * @param carGroupId
     * @param userId
     */
    @Override
    public void startUpCarGroup(Long carGroupId, Long userId) throws Exception {
        //int startFlag = 1;
        //递归启用车队及下属车队的驾驶员和车辆
        changeState(carGroupId, null, String.valueOf(userId), CarConstant.START_UP_CAR_GROUP,
                CarConstant.START_CAR, CarConstant.START_DRIVER_TYPE);
    }

    /**
     * 递归执行车队和下属车队的启用或禁用（车队、驾驶员、车辆）
     * @param ownerOrg
     */
    private void changeState(Long carGroupId, Long ownerOrg, String userId,
                                String carGroupState, String carState, String driverState) {
        CarGroupInfo carGroupBean = null;
        List<CarGroupInfo> carGroupList = new ArrayList<>();
        //判断是不是首次进入，如果首次根据车队ID查询，否则根据归属直接组织
        if(carGroupId != null) {
            carGroupBean = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            if(carGroupBean != null) {
                carGroupList.add(carGroupBean);
            }
        } else {
            //根据车队id 查询下级车队列表
            if(ownerOrg != null) {
                carGroupBean = CarGroupInfo.builder().parentCarGroupId(ownerOrg).build();
                carGroupList = carGroupInfoMapper.selectCarGroupInfoList(carGroupBean);
            }
        }
        if(carGroupList != null && carGroupList.size() > 0) {
            for(CarGroupInfo carGroupInfo : carGroupList) {
                Long id = carGroupInfo.getCarGroupId();
                carGroupInfo.setUpdateBy(userId);
                carGroupInfo.setUpdateTime(new Date());
                carGroupInfo.setState(carGroupState);
                //更新车队状态为启用
                carGroupInfoMapper.updateCarGroupInfo(carGroupInfo);
                CarInfo carInfo = new CarInfo();
                carInfo.setCarGroupId(id);
                carInfo.setState(carState);
                carInfo.setUpdateBy(userId);
                carInfo.setUpdateTime(new Date());
                //更新车辆状态为启用
                carInfoMapper.updateCarInfoByCarGroupId(carInfo);
                //启用驾驶员
                CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
                carGroupDriverRelation.setCarGroupId(id);
                //获得车队下的驾驶员集合
                List<CarGroupDriverRelation> driverList = carGroupDriverRelationMapper.selectCarGroupDriverRelationList(carGroupDriverRelation);
                //启用驾驶员
                if(driverList != null && driverList.size() != 0) {
                    driverInfoMapper.updateDriverState(driverList, String.valueOf(userId), driverState);
                }
                //递归调用，当前车队的下属车队处理逻辑，根据车队归属的组织查询
                changeState(null, id, userId, carGroupState, carState, driverState);
            }
        }
    }


    /**
     * 分页查询总车队列表
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @Override
    public PageResult<CarGroupListVO> selectCarGroupInfoByPage(Integer pageNum, Integer pageSize,String search,String state,Long deptId,Long carGroupId,Long companyId) {
        PageHelper.startPage(pageNum,pageSize);
        List<CarGroupListVO> list =  carGroupInfoMapper.selectAllByPage(search,state,deptId,carGroupId,companyId);
        getCarGroupExtraInfo(list);
        PageInfo<CarGroupListVO> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),info.getPages(),list);
    }

    /**
     * 获取车队主管名字集合/车队人数/下级车队人数
     * @param list
     */
    public void getCarGroupExtraInfo(List<CarGroupListVO> list) {
        for (CarGroupListVO carGroupListVO : list) {
            Long carGroupId = carGroupListVO.getCarGroupId();
            Integer ownerOrg = carGroupListVO.getOwnerOrg();
            //查询所属公司名字
            Long ownerCompany = carGroupListVO.getOwnerCompany();
            EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(ownerCompany);
            if(ecmpOrg != null){
                carGroupListVO.setOwnerCompanyName(ecmpOrg.getDeptName());
            }
            //查询车队绑定车辆数
            int carNum = carInfoMapper.selectCountGroupCarByGroupId(carGroupId);
            carGroupListVO.setCarNum(carNum);
            // 1.查询下级车队数
            int subGroupNum = carGroupInfoMapper.selectCountByParentId(carGroupId);
            carGroupListVO.setCountSubCarGroup(subGroupNum);
            // 2.查询车队驾驶员数(调度员是从驾驶员里选出来的)
            int driverNum = carGroupDriverRelationMapper.selectCountDriver(carGroupId);
            carGroupListVO.setCountMember(driverNum);
            // 3. 查询调度员(主管名字)
            List<String> leaderNames = getDispatcherNames(carGroupId);
            carGroupListVO.setLeaderNames(leaderNames);
        }
    }

    /**
     * 根据车队id查询所有调度员名字
     * @param carGroupId
     * @return
     */
    @Override
    public List<String> getDispatcherNames(Long carGroupId) {
        CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
        carGroupDispatcherInfo.setCarGroupId(carGroupId);
        //查询所有调度员
        List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
        List<String> leaderNames = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(carGroupDispatcherInfos)){
            //遍历获取调度员名字
            for (CarGroupDispatcherInfo groupDispatcherInfo : carGroupDispatcherInfos) {
                leaderNames.add(groupDispatcherInfo.getName());
            }
        }
        return leaderNames;
    }

    /**
     * 删除车队
     * @param carGroupId
     * @param userId
     */
    @Override
    public String deleteCarGroup(Long carGroupId, Long userId) throws Exception {
        //判断车队及下属车队下是否有车辆和驾驶员
        //carGroupDelFlag = false;
        if(existCarOrDriver(carGroupId, null)){
            //车队下有车辆或者有驾驶员，则返回不能删除的提示信息
            //carGroupDelFlag = false;
            return "请先删除该车队下的所有车辆及人员信息";
        }
        //执行删除
        executeDeleteCarGroup(carGroupId, null, userId);
        return "删除成功";
    }

    /**
     * 执行删除车队及下属车队
     * @param carGroupId
     * @param ownerOrg
     */
    private void executeDeleteCarGroup(Long carGroupId, Long ownerOrg, Long userId) throws Exception {
        CarGroupInfo carGroupBean = null;
        List<CarGroupInfo> carGroupList = new ArrayList<>();
        //判断是不是首次进入，如果首次根据车队ID查询，否则根据归属直接组织
        if (carGroupId != null) {
            carGroupBean = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            if (carGroupBean != null) {
                carGroupList.add(carGroupBean);
            }
        } else {
            //车队归属直接组织(包含-所有总公司、分公司、部门、车队)
            if(ownerOrg != null) {
                //根据福父车队id查询下级车队集合
                carGroupBean = CarGroupInfo.builder().parentCarGroupId(ownerOrg).build();
                carGroupList = carGroupInfoMapper.selectCarGroupInfoList(carGroupBean);
            }
        }
        int row = 0;
        if (carGroupList != null && carGroupList.size() > 0) {
            //遍历车队列表执行删除
            for (CarGroupInfo carGroupInfo : carGroupList) {
                Long itemCarGroupId = carGroupInfo.getCarGroupId();
                CarGroupInfo updateBean = new CarGroupInfo();
                updateBean.setCarGroupId(itemCarGroupId);
                updateBean.setState(CarConstant.DELETE_CAR_GROUP);
                updateBean.setUpdateBy(String.valueOf(userId));
                updateBean.setUpdateTime(new Date());
                //删除车队
                row = carGroupInfoMapper.updateCarGroupInfo(updateBean);
                if( row != 1){
                    throw new Exception("删除下属车队失败");
                }
                //删除车队服务城市
                carGroupServeScopeInfoMapper.deleteByCarGroupId(itemCarGroupId);
                //删除车队服务组织表信息
                carGroupServeOrgRelationMapper.deleteById(itemCarGroupId);
                //删除车队调度员、修改员工的调度员角色、删除用户角色关系
                deleteCarGroupDispatcher(itemCarGroupId, userId);
                //递归删除车队下的车队
                executeDeleteCarGroup(null, itemCarGroupId, userId);
            }
        }
    }


    /**
     * 查询夏季车队列表信息
     * @param deptId
     * @return
     */
    @Override
    public List<CarGroupListVO> selectSubCarGroupInfoList(Long deptId) {
        List<CarGroupListVO> list = carGroupInfoMapper.selectSubCarGroupInfoList(deptId);
        if(CollectionUtils.isEmpty(list)){
            throw new RuntimeException();
        }
        getCarGroupExtraInfo(list);
        return list;
    }

    /**
     * 联系车队（通用）
     * ------1. 如果是用户不是司机，没订单： 查询用户所在公司所有车队座机               ----------- 多个车队座机
     *          2.用户有订单： 查询调度员电话，调度员管理的（多个）车队电话      ----------- 一个调度员，多个车队
     * ------3. 如果是司机，没订单： 查询司机所在（一个）车队座机              -----------  一个车队电话
     *          4.有订单： 查询调度员电话，司机所在（一个）车队电话            -----------   一个车队，一个调度员
     * @param orderId
     * @return
     */
    @Override
    public List<ContactCarGroupVO> cantactCarGroup(Long orderId) {
        List<ContactCarGroupVO> phones = new ArrayList<>();
        ContactCarGroupVO contactCarGroupVO;
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        SysUser user = loginUser.getUser();
        SysDriver driver = loginUser.getDriver();
        //员工所属公司
        //1.如果不是司机（那就是员工）
        if(driver == null){
            Long companyId = user.getDept().getCompanyId();
            if(companyId == null){
                throw new RuntimeException("员工无所属公司信息");
            }
            //如果没传订单id，则查询所在公司所有车队座机
            if(orderId == null){
                CarGroupInfo carGroupInfo = new CarGroupInfo();
                    carGroupInfo.setCompanyId(Long.valueOf(companyId));
                //查询公司所有车队（只查启用的）
                List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.selectEnableCarGroupInfoList(carGroupInfo);
                if(CollectionUtils.isEmpty(carGroupInfos)){
                    //如果整个（分子)公司都没有车队，返回空
                    return null;
                }
                //（1）查询车队所有座机电话
                for (CarGroupInfo groupInfo : carGroupInfos) {
                    contactCarGroupVO = ContactCarGroupVO.builder()
                    .name(groupInfo.getCarGroupName())
                    .phone(groupInfo.getTelephone())
                    //0 表示车队  1表示调度员
                    .type(0)
                    .build();
                    phones.add(contactCarGroupVO);
                }

            }else {
                //如果传了订单id，则查询订单对应的调度员（如果有改派，改派和第一次调度员都是同一个人）及调度员所在车队电话
                //根据订单id查询调度员的userId(调度员是有)
                String userId  = orderStateTraceInfoMapper.selectDispatcherUserId(orderId);
                //查询调度员所在车队及车队座机
                CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
                carGroupDispatcherInfo.setUserId(Long.valueOf(userId));
                //调度员车队关系表查询（一个调度员可能管理多个车队）
                List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
                //获取车队id集合
                List<Long> groupIds = carGroupDispatcherInfos.stream().map(CarGroupDispatcherInfo::getCarGroupId).collect(Collectors.toList());
                //（2.2）查询车队电话
                List<CarGroupFixedPhoneVO> groupPhones = carGroupInfoMapper.selectCarGroupPhones(groupIds);
                for (CarGroupFixedPhoneVO groupPhone : groupPhones) {
                    contactCarGroupVO = ContactCarGroupVO.builder()
                            .name(groupPhone.getCarGroupName())
                            .phone(groupPhone.getPhone())
                            //0 表示车队  1表示调度员
                            .type(0)
                            .build();
                    phones.add(contactCarGroupVO);
                }
                //（2.1）查询调度员信息
                UserVO userVO = ecmpUserMapper.selectUserVoById(Long.valueOf(userId));
                contactCarGroupVO = ContactCarGroupVO.builder()
                        .name(userVO.getUserName())
                        .phone(userVO.getUserPhone())
                        //0 表示车队  1表示调度员
                        .type(1)
                        .build();
                phones.add(contactCarGroupVO);
            }
        }else {
            //2.如果是司机  (司机可能不是公司员工) 则查询司机所在车队
            Long driverId = loginUser.getDriver().getDriverId();
            //(3)司机所属车队
            Long carGroupId = carGroupDriverRelationMapper.selectCarGroupDriverRelationById(driverId).getCarGroupId();
            CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            contactCarGroupVO = ContactCarGroupVO.builder()
                    .name(carGroupInfo.getCarGroupName())
                    .phone(carGroupInfo.getTelephone())
                    //0 表示车队  1表示调度员
                    .type(0)
                    .build();
            phones.add(contactCarGroupVO);
            if(orderId != null){
                //如果有订单，则添加一个调度员电话
                //如果传了订单id，则查询订单对应的调度员（如果有改派，改派和第一次调度员都是同一个人）及调度员所在车队电话
                //根据订单id查询调度员的userId(调度员是有)
                String userId  = orderStateTraceInfoMapper.selectDispatcherUserId(orderId);
                //（4）查询调度员信息
                UserVO userVO = ecmpUserMapper.selectUserVoById(Long.valueOf(userId));
                contactCarGroupVO = ContactCarGroupVO.builder()
                        .name(userVO.getUserName())
                        .phone(userVO.getUserPhone())
                        //0 表示车队  1表示调度员
                        .type(1)
                        .build();
                phones.add(contactCarGroupVO);
            }
        }

        return phones;
    }

    /**
     * 补单获取调度员所管理车队的服务城市
     * @param userId
     * @return
     */
    @Override
    public ApiResponse  obtainDispatcherCity(Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        //查询该调度员的车队Id
        String carGroupId = carGroupDispatcherInfoMapper.selectCarGroupDispatcherAllId(userId);
        if(!"".equals(carGroupId)){
            List<CityInfo> cityInfo =carGroupServeScopeInfoMapper.selectObtainDispatcherCity(carGroupId);
            apiResponse.setData(cityInfo);
            return apiResponse;
        }else {
            apiResponse.setMsg("该调度员没有任何的车队Id");
        }
        return apiResponse;
    }

    /**
     * 查询指定城市所有车队调度员及车队座机
     * @param
     * @return
     */
    @Override
    public List<CarGroupPhoneVO> getCarGroupPhone(String cityCode) {
        //查询城市所有车队(只查启用的)
        List<CarGroupInfo> list = carGroupInfoMapper.selectValidCarGroupListByCity(cityCode);
        List<CarGroupPhoneVO> carGroupPhoneVOS = new ArrayList<>();
        CarGroupPhoneVO carGroupPhoneVO = null;
        if(CollectionUtils.isEmpty(list)){
            //如果城市内没有车队，则查询公司所有车队调度员及车队座机
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            SysUser loginSysUser = loginUser.getUser();
            Long ownerCompany = null;
            if(loginSysUser != null){
                //如果登陆用户是公司员工
                Long loginUserId = loginUserId = loginSysUser.getUserId();
                // 查询员工所在部门
                EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(loginUserId);
                Long deptId = ecmpUser.getDeptId();
                //查询员工所在公司
                EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(deptId);
                //只要查询出来的部门type不为1（不为公司类型），则向上查询
                while (!"1".equals(ecmpOrg.getDeptType())){
                    ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(ecmpOrg.getParentId());
                    if(ecmpOrg == null || ecmpOrg.getParentId()  == null){
                        break;
                    }
                }
                ownerCompany = ecmpOrg.getDeptId();
            }else {
                //如果登录用户不是公司员工， 那么就是司机(司机可能不是公司员工) 则查询司机所在车队
                Long driverId = loginUser.getDriver().getDriverId();
                CarGroupDriverRelation carGroupDriverRelation = carGroupDriverRelationMapper.selectCarGroupDriverRelationById(driverId);
                //查询车队所在公司
                ownerCompany = carGroupInfoMapper.selectCarGroupInfoById(carGroupDriverRelation.getCarGroupId()).getCompanyId();
            }
            CarGroupInfo carGroupInfo = new CarGroupInfo();
            carGroupInfo.setCompanyId(ownerCompany);
            //查询公司所有车队（只查启用的）
            List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.selectEnableCarGroupInfoList(carGroupInfo);
            if(CollectionUtils.isEmpty(carGroupInfos)){
                //如果整个（分子)公司都没有车队，返回空
                return null;
            }
            //查询车队及调度员电话
            getCarGroupsPhones(carGroupInfos,carGroupPhoneVOS);
            return carGroupPhoneVOS;
        }
        getCarGroupsPhones(list, carGroupPhoneVOS);
        return carGroupPhoneVOS;
    }

    /**
     * 查询车队集合的所有座机，及调度员信息
     * @param list
     * @param carGroupPhoneVOS
     */
    private void getCarGroupsPhones(List<CarGroupInfo> list, List<CarGroupPhoneVO> carGroupPhoneVOS) {
        CarGroupPhoneVO carGroupPhoneVO;
        for (CarGroupInfo carGroupInfo : list) {
                carGroupPhoneVO = new CarGroupPhoneVO();
                carGroupPhoneVO.setPhone(carGroupInfo.getTelephone());
                carGroupPhoneVO.setCarGroupName(carGroupInfo.getCarGroupName());
                //查询车队所有调度员
                List<UserVO> carGroupDispatchers = getCarGroupDispatchers(carGroupInfo.getCarGroupId());
                if(!CollectionUtils.isEmpty(carGroupDispatchers)){
                    carGroupPhoneVO.setDispatchers(carGroupDispatchers);
                }
            carGroupPhoneVOS.add(carGroupPhoneVO);
        }
    }

    //获取登录用户id
    public Long getLoginUserId(){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        return loginUser.getUser().getUserId();
    }

    /**
     *
     */

    /**
     * 查询调度员电话机调度员所在车队座机
     * @param
     * @return
     */
    @Override
    public DispatcherAndFixedLineVO getDispatcherAndFixedLine(Long orderId) {
        //根据订单id查询调度员的userId(调度员是有)
        String userId  = orderStateTraceInfoMapper.selectDispatcherUserId(orderId);
       //查询调度员信息
        UserVO userVO = ecmpUserMapper.selectUserVoById(Long.valueOf(userId));
        //查询调度员所在车队及车队座机
        CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
        carGroupDispatcherInfo.setUserId(Long.valueOf(userId));
        //调度员车队关系表查询（一个调度员可能管理多个车队）
        List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
       //获取车队id集合
        List<Long> groupIds = carGroupDispatcherInfos.stream().map(CarGroupDispatcherInfo::getCarGroupId).collect(Collectors.toList());
        //查询车队电话
        List<CarGroupFixedPhoneVO> groupPhones = carGroupInfoMapper.selectCarGroupPhones(groupIds);
        DispatcherAndFixedLineVO vo = new DispatcherAndFixedLineVO();
        vo.setDispatcher(userVO);
        vo.setGroupPhones(groupPhones);
        return vo;
    }


    /**
     * 判断车队下是否有车辆  false表示没有 true表示有车辆
     * @param carGroupId
     * @return
     */
    public boolean hasCar(Long carGroupId){
        CarInfo carInfo = CarInfo.builder().carGroupId(carGroupId).build();
        List<CarInfo> carInfos = carInfoMapper.selectCarInfoList(carInfo);
        //不为空 表示 车队下有车辆
        return !CollectionUtils.isEmpty(carInfos);
    }

    /**
     * 判断车队下是否有驾驶员
     * @param carGroupId
     * @return
     */
    public boolean hasDriver(Long carGroupId){
        int i = carGroupDriverRelationMapper.selectCountDriver(carGroupId);
        //不为0表示有驾驶员
        return i != 0;
    }

    /**
     * 判断车队及下属车队下是否有车辆和驾驶员
     * @param carGroupId
     * @param ownerOrg
     * @return
     */
    public boolean existCarOrDriver(Long carGroupId, Long ownerOrg) {
        CarGroupInfo carGroupBean = null;
        List<CarGroupInfo> carGroupList = new ArrayList<>();
        //判断是不是首次进入，如果首次根据车队ID查询，否则根据归属直接组织
        if (carGroupId != null) {
            //查询车队信息
            carGroupBean = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            if (carGroupBean != null) {
                //存入车队集合
                carGroupList.add(carGroupBean);
            }
        } else {
            //不是首次进来 则查询上次车队的下级车队   车队归属直接组织(包含-所有总公司、分公司、部门、车队)
            if(ownerOrg != null) {
                carGroupBean = CarGroupInfo.builder().parentCarGroupId(ownerOrg).build();
                //根据车队id查询下级车队集合
                carGroupList = carGroupInfoMapper.selectCarGroupInfoList(carGroupBean);
            }
        }
        if (carGroupList != null && carGroupList.size() > 0) {
            for (CarGroupInfo carGroupInfo : carGroupList) {
                //判断车队下是否有车辆和驾驶员
                Long id = carGroupInfo.getCarGroupId();
                //如果车队下有车辆或者有驾驶员，则返回true
                if (hasCar(id) || hasDriver(id)) {
                    //carGroupDelFlag = true;
                    return true;
                } else {
                    //如果车队下没有车辆也没有驾驶员， 则递归查询该车队的下级车队下是否有车辆和驾驶员
                    existCarOrDriver(null, id);
                }
            }
        }
        //如果上面走完都没返回true 则说明车队下没有车辆和驾驶员了 返回false
        return false;
    }

    /*根据公司id 查询车队树*/
    @Override
    public List<CarGroupTreeVO> selectCarGroupTree(Long deptId){
        //查询分子公司的一级车队
        List<CarGroupTreeVO> list = carGroupInfoMapper.selectFirstLevelCarGroupList(deptId);
        int size = list.size();
        if(size>0){
            for (int i = 0; i < size; i++) {
                CarGroupTreeVO carGroupTreeVO = list.get(i);
                if(carGroupTreeVO != null) {
                    carGroupTreeVO.setCarGroupTreeVO(this.getCarGroupTree(carGroupTreeVO.getDeptId()));
                }
            }
        }
        return list;
    }

    /*查询所有车队编号*/
    @Override
    public List<String> selectAllCarGroupCode(Long companyId) {
        List<String> list = carGroupInfoMapper.selectAllCarGroupCode(companyId);
        return list;
    }

    /*判断车队编号是否存在*/
    @Override
    public boolean judgeCarGroupCode(String carGroupCode,Long companyId) {
        List<String> list = selectAllCarGroupCode(companyId);
        for (String s : list) {
            if (carGroupCode.equals(s)){
                return true;
            }
        }
        return false;
    }

    /*回显车队信息*/
    @Override
    public CarGroupDTO getCarGroupInfoFeedBack(Long carGroupId) {
        CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
        Long parentCarGroupId = carGroupInfo.getParentCarGroupId();
        //如果是一级车队  所属组织为部门或公司
        String ownerOrgName = null;
        long ownerOrg = carGroupInfo.getCompanyId();
        if(parentCarGroupId == 0L){
            EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(ownerOrg);
            if(ecmpOrg != null){
                ownerOrgName = ecmpOrg.getDeptName();
            }
        }else {
            //如果是下级车队  所属组织为上车队名字
            CarGroupInfo carGroupInfo1 = carGroupInfoMapper.selectCarGroupInfoById(parentCarGroupId);
            if(carGroupInfo1 != null){
                ownerOrgName = carGroupInfo1.getCarGroupName();
            }
        }
        //查询城市名字
        String cityName = null;
        String city = carGroupInfo.getCity();
        if(city != null){
            CityInfo cityInfo = chinaCityMapper.queryCityByCityCode(city);
            if(cityInfo != null){
                cityName = cityInfo.getCityName();
            }
        }
        //查询调度员
        List<UserVO> dispatchers = getCarGroupDispatchers(carGroupId);
        //查询服务部门及服务公司
        List<CarGroupServeOrgRelation> carGroupServeOrgRelations = carGroupServeOrgRelationMapper.queryById(carGroupId);
        List<Long> deptIds = new ArrayList<>();
        List<Long> companyIds = new ArrayList<>();
        for (CarGroupServeOrgRelation carGroupServeOrgRelation : carGroupServeOrgRelations) {
            if(OrgConstant.INNER_ORG.equals(carGroupServeOrgRelation.getType())){
                deptIds.add(carGroupServeOrgRelation.getDeptId());
            }else{
                companyIds.add(carGroupServeOrgRelation.getDeptId());
            }
        }
        if(carGroupInfo != null){
            //判断车队是否是一级车队
            CarGroupDTO vo = CarGroupDTO.builder()
                    .carGroupName(carGroupInfo.getCarGroupName())
                    .carGroupCode(carGroupInfo.getCarGroupCode())
                    .latitude(String.valueOf(carGroupInfo.getLatitude()) )
                    .longitude(String.valueOf(carGroupInfo.getLongitude()))
                    .ownerOrg(ownerOrg)
                    .parentCarGroupId(carGroupInfo.getParentCarGroupId())
                    .ownerOrgName(ownerOrgName)
                    .city(carGroupInfo.getCity())
                    .cityName(cityName)
                    .owneCompany(carGroupInfo.getCompanyId())
                    .telephone(carGroupInfo.getTelephone())
                    .dispatchers(dispatchers)
                    .shortAddress(carGroupInfo.getShortAddress())
                    .fullAddress(carGroupInfo.getFullAddress())
                    .companyIds(companyIds.toArray(new Long[]{}))
                    .deptIds(deptIds.toArray(new Long[]{}))
                    .build();
            return vo;
        }
        return null;
    }

    /*查询司机所属车队座机及车队调度员电话*/
    @Override
    public CarGroupPhoneVO getOwnerCarGroupPhone() {
        //查询司机所属车队
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        SysDriver driver = loginUser.getDriver();
        Long driverId = null;
        if(driver != null){
            driverId = driver.getDriverId();
        }
        if(driverId == null){
            throw new RuntimeException("该用户不是司机");
        }
        CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
        carGroupDriverRelation.setDriverId(driverId);
        List<CarGroupDriverRelation> carGroupDriverRelations = carGroupDriverRelationMapper.selectCarGroupDriverRelationList(carGroupDriverRelation);
        if(!CollectionUtils.isEmpty(carGroupDriverRelations)){
            Long carGroupId = carGroupDriverRelations.get(0).getCarGroupId();
            CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            if(ObjectUtils.isEmpty(carGroupInfo)){
                throw new RuntimeException("所属车队不存在");
            }
            CarGroupPhoneVO carGroupPhoneVO = new CarGroupPhoneVO();
            carGroupPhoneVO.setCarGroupName(carGroupInfo.getCarGroupName());
            carGroupPhoneVO.setPhone(carGroupInfo.getTelephone());
            //查询车队调度员信息
            List<UserVO> carGroupDispatchers = getCarGroupDispatchers(carGroupId);
            carGroupPhoneVO.setDispatchers(carGroupDispatchers);
            return carGroupPhoneVO;
        }else {
            return null;
        }
    }

    /**
     * 车队名字校验 同一公司下，车队名不能重复
     * @param carGroupName
     * @param owneCompany
     * @return
     */
    @Override
    public Boolean judgeCarGroupName(String carGroupName, Long owneCompany) {
        List<String> groupNames = carGroupInfoMapper.selectAllCarGroupNameByCompany(owneCompany);
        for (String groupName : groupNames) {
            if(carGroupName.equals(groupName)){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<CarGroupListVO> getCarGroupList(Long userId) {
        return carGroupInfoMapper.getCarGroupList(userId);
    }

    /* *//**
     * 判断是否是一级车队
     * @param carGroupId
     * @return
     *//*
    public boolean judgeFirstLevelCarGroup(Long carGroupId){
        carGroupInfoMapper.selectCarGroupInfoById()
    }
*/
    /*根据车队id查询车队树*/
    public List<CarGroupTreeVO> getCarGroupTree(Long carGroupId){
        List<CarGroupTreeVO> list = carGroupInfoMapper.getCarGroupTree(carGroupId);
        int size = list.size();
        if (size > 0){
            for (int i = 0; i < size; i++) {
                CarGroupTreeVO carGroupTreeVO = list.get(i);
                if(carGroupTreeVO == null){
                    continue;
                }
                carGroupTreeVO.setCarGroupTreeVO(this.getCarGroupTree(carGroupTreeVO.getDeptId()));
            }
        }
        return list;
    }
}
