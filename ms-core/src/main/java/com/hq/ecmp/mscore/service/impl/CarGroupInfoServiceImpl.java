package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.mscore.bo.CityInfo;
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
        //车队编码
        carGroupInfo.setCarGroupCode(carGroupDTO.getCarGroupCode());
        //所属城市编码
        carGroupInfo.setCity(carGroupDTO.getCity());
        //所属城市名字
       // carGroupInfo.setCityName(carGroupDTO.getCityName());
        //车队名称
        carGroupInfo.setCarGroupName(carGroupDTO.getCarGroupName());
        //车队详细地址
        carGroupInfo.setFullAddress(carGroupDTO.getFullAddress());
        //车队短地址
        carGroupInfo.setShortAddress(carGroupDTO.getShortAddress());
        //所属组织
        carGroupInfo.setOwnerOrg(carGroupDTO.getOwnerOrg());
        //车队负责人  TODO 冗余字段 暂无数据
        carGroupInfo.setLeader(carGroupDTO.getLeader());
        //创建人
        carGroupInfo.setCreateBy(String.valueOf(userId));
        //省份代码
        carGroupInfo.setProvince(carGroupDTO.getProvince());
        //驻地经度
        carGroupInfo.setLongitude(carGroupDTO.getLongitude());
        //驻地纬度
        carGroupInfo.setLatitude(carGroupDTO.getLatitude());
        //车队座机
        carGroupInfo.setTelephone(carGroupDTO.getTelephone());
        //所属公司
        carGroupInfo.setOwnerCompany(carGroupDTO.getOwneCompany());
        int i = insertCarGroupInfo(carGroupInfo);
        if(i != 1){
            throw new Exception();
        }
        Long carGroupId = carGroupInfo.getCarGroupId();
        //新增 车队主管  车队主管就是调度员
        if(!ObjectUtils.isEmpty(carGroupId)){
            CarGroupDispatcherInfo carGroupDispatcherInfo = null;
            List<UserVO> dispatchers = carGroupDTO.getDispatchers();
            for (UserVO dispatcher : dispatchers) {
                carGroupDispatcherInfo = new CarGroupDispatcherInfo();
                //车队id
                carGroupDispatcherInfo.setCarGroupId(carGroupId);
                //调度员id
                carGroupDispatcherInfo.setDispatcherId(dispatcher.getUserId());
                //调度员名字
                carGroupDispatcherInfo.setName(dispatcher.getUserName());
                //创建人
                carGroupDispatcherInfo.setCreateBy(String.valueOf(userId));
                //创建时间
                carGroupDispatcherInfo.setCreateTime(new Date());
                int j = carGroupDispatcherInfoMapper.insertCarGroupDispatcherInfo(carGroupDispatcherInfo);
                if(j != 1){
                    throw new Exception();
                }
            }
        }
        //车队 作为部门形式保存
        EcmpOrgVo ecmpOrgVo = new EcmpOrgVo();
        ecmpOrgVo.setDeptName(carGroupDTO.getCarGroupName());
        ecmpOrgVo.setParentId(carGroupDTO.getOwnerOrg());
        ecmpOrgVo.setDeptType("1"); //组织类别（1 公司 2 部门 3 车队）
        ecmpOrgVo.setPhone(carGroupDTO.getTelephone());
        ecmpOrgVo.setStatus("1");  //部门状态（0正常 1停用）
        ecmpOrgVo.setCreateTime(new Date());
        ecmpOrgVo.setCreateBy(String.valueOf(userId));
        int j = ecmpOrgMapper.addDept(ecmpOrgVo);
        if(j!=1){
            throw new RuntimeException();
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
        //根据cityCode查询城市名字
        CityInfo cityInfo = chinaCityMapper.queryCityByCityCode(carGroupInfo.getCity());
        CarGroupDetailVO carGroupDetailVO = CarGroupDetailVO.builder()
                //车队编号
                .carGroupCode(carGroupInfo.getCarGroupCode())
                //车队名字
                .carGroupName(carGroupInfo.getCarGroupName())
                //所属组织
                .ownerOrg(carGroupInfo.getOwnerOrg())
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
                    userVO.setUserPhone(ecmpUser.getPhonenumber());
                    userVO.setUserName(ecmpUser.getUserName());
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
        carGroupInfo.setCarGroupId(carGroupDTO.getCarGroupId());
        //车队编码
        carGroupInfo.setCarGroupCode(carGroupDTO.getCarGroupCode());
        //所属城市编码
        carGroupInfo.setCity(carGroupDTO.getCity());
        //所属城市名字
       // carGroupInfo.setCityName(carGroupDTO.getCityName());
        //车队名称
        carGroupInfo.setCarGroupName(carGroupDTO.getCarGroupName());
        //车队详细地址
        carGroupInfo.setFullAddress(carGroupDTO.getFullAddress());
        //车队短地址
        carGroupInfo.setShortAddress(carGroupDTO.getShortAddress());
        //所属组织
        carGroupInfo.setOwnerOrg(carGroupDTO.getOwnerOrg());
        //车队负责人  TODO 冗余字段 暂无数据
        carGroupInfo.setLeader(carGroupDTO.getLeader());
        //修改人
        carGroupInfo.setUpdateBy(String.valueOf(userId));
        //省份代码
        carGroupInfo.setProvince(carGroupDTO.getProvince());
        //驻地经度
        carGroupInfo.setLongitude(carGroupDTO.getLongitude());
        //驻地纬度
        carGroupInfo.setLatitude(carGroupDTO.getLatitude());
        //车队座机
        carGroupInfo.setTelephone(carGroupDTO.getTelephone());
        //1.修改车队
        int i = updateCarGroupInfo(carGroupInfo);
        if(i != 1){
            throw new Exception("修改车队信息失败");
        }
        //2.修改调度员信息 先删除 再新增
        Long carGroupId = carGroupDTO.getCarGroupId();
        //2.1 解绑车队所有调度员
        int n = carGroupDispatcherInfoMapper.deleteCarGroupDispatcherInfoByGroupId(carGroupId);
        List<UserVO> dispatchers = carGroupDTO.getDispatchers();
        CarGroupDispatcherInfo carGroupDispatcherInfo = null;
        for (UserVO dispatcher : dispatchers) {
            carGroupDispatcherInfo = new CarGroupDispatcherInfo();
            Long dispatcherId = dispatcher.getUserId();
            carGroupDispatcherInfo.setCarGroupId(carGroupId);
            carGroupDispatcherInfo.setDispatcherId(dispatcherId);
            carGroupDispatcherInfo.setCreateBy(String.valueOf(userId));
            carGroupDispatcherInfo.setCreateTime(new Date());
            //2.2新增调度员
            int j = carGroupDispatcherInfoMapper.insertCarGroupDispatcherInfo(carGroupDispatcherInfo);
            if(j != 1){
                throw new Exception("修改车队信息失败");
            }
        }
    }

    /**
     * 禁用车队
     * @param carGroupId
     * @param userId
     */
    @Override
    @Transactional
    public void disableCarGroup(Long carGroupId, Long userId) throws Exception {
        CarGroupInfo carGroupInfo = new CarGroupInfo();
        carGroupInfo.setUpdateBy(String.valueOf(userId));
        carGroupInfo.setUpdateTime(new Date());
        carGroupInfo.setState(CarConstant.DISABLE_CAR_GROUP);
        int i = carGroupInfoMapper.updateCarGroupInfo(carGroupInfo);
        if(i != 1){
            throw new Exception("禁用车队失败");
        }
        //禁用 车队下的 驾驶员 及 车辆
        //查询车队下的未禁用驾驶员
        List<DriverVO> list = selectGroupEffectiveDrives(carGroupId);
        for (DriverVO driverVO : list) {
            //禁用驾驶员
           int j = driverInfoMapper.disableDriver(driverVO.getDriverId());
           if(j != 1){
               throw  new RuntimeException();
           }
        }
        //查询车队下的启用用车辆ids
        List<Long> carIds = getGroupEffectiveCarIds(carGroupId);
        //禁用车辆
        for (Long carId : carIds) {
            int  k = carInfoMapper.disableCarByCarId(carId);
            if(k != 1){
                throw new RuntimeException();
            }
        }
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
        CarGroupInfo carGroupInfo = new CarGroupInfo();
        carGroupInfo.setUpdateBy(String.valueOf(userId));
        carGroupInfo.setUpdateTime(new Date());
        carGroupInfo.setState(CarConstant.START_UP_CAR_GROUP);
        int i = carGroupInfoMapper.updateCarGroupInfo(carGroupInfo);
        if(i != 1){
            throw new RuntimeException();
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
    public PageResult<CarGroupListVO> selectCarGroupInfoByPage(Integer pageNum, Integer pageSize,String search,String state,Long deptId) {
        PageHelper.startPage(pageNum,pageSize);
        List<CarGroupListVO> list =  carGroupInfoMapper.selectAllByPage(search,state,deptId);
        getCarGroupExtraInfo(list);
        PageInfo<CarGroupListVO> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),info.getPages(),list);
    }

    /**
     * 获取车队主管名字集合/车队人数/下级车队人数
     * @param list
     */
    private void getCarGroupExtraInfo(List<CarGroupListVO> list) {
        for (CarGroupListVO carGroupListVO : list) {
            Long carGroupId = carGroupListVO.getCarGroupId();
            Integer ownerOrg = carGroupListVO.getOwnerOrg();
            //查询车队绑定车辆数
            //int carNum = carInfoMapper.selectCountGroupCarByGroupId(carGroupId);
            // 1.查询下级车队数
            int subGroupNum = ecmpOrgMapper.selectCountByParentId(ownerOrg);
            carGroupListVO.setCountSubCarGroup(subGroupNum);
            // 2.查询车队驾驶员数(调度员是从驾驶员里选出来的)
            int driverNum = carGroupDriverRelationMapper.selectCountDriver(carGroupId);
            carGroupListVO.setCountMember(driverNum);
            // 3. 查询调度员(主管名字)
            CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
            carGroupDispatcherInfo.setCarGroupId(carGroupId);
            List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
            List<String> leaderNames = Lists.newArrayList();
            if(!CollectionUtils.isEmpty(carGroupDispatcherInfos)){
                for (CarGroupDispatcherInfo groupDispatcherInfo : carGroupDispatcherInfos) {
                    leaderNames.add(groupDispatcherInfo.getName());
                }
            }
            carGroupListVO.setLeaderNames(leaderNames);
        }
    }

    /**
     * 删除车队
     * @param carGroupId
     */
    @Override
    public void deleteCarGroup(Long carGroupId) throws Exception {
        if(hasCar(carGroupId) || hasCar(carGroupId)){
            throw new Exception("无法删除，先删除驾驶员及车厢信息");
        }
        int i = carGroupInfoMapper.deleteCarGroupInfoById(carGroupId);
        if( i != 1){
            throw new Exception("删除失败");
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
     * 查询指定城市所有车队调度员及车队座机
     * @param
     * @return
     */
    @Override
    public List<CarGroupPhoneVO> getCarGroupPhone(String cityCode) {
        //查询城市所有车队
        List<CarGroupInfo> list = carGroupInfoMapper.selectValidCarGroupListByCity(cityCode);
        List<CarGroupPhoneVO> carGroupPhoneVOS = new ArrayList<>();
        CarGroupPhoneVO carGroupPhoneVO = null;
        if(CollectionUtils.isEmpty(list)){
            //如果城市内没有车队，则查询公司所有车队调度员及车队座机
            Long loginUserId = getLoginUserId();
            //1. 查询员工所在部门
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
            CarGroupInfo carGroupInfo = new CarGroupInfo();
            carGroupInfo.setOwnerCompany(ecmpOrg.getDeptId());
            List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.selectCarGroupInfoList(carGroupInfo);
            //查询车队
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
        //根据订单id查询调度员的userId
        String userId  = orderStateTraceInfoMapper.selectDispatcherUserId(orderId);
       //查询调度员信息
        UserVO userVO = ecmpUserMapper.selectUserVoById(Long.valueOf(userId));
        //查询调度员所在车队及车队座机
        CarGroupDispatcherInfo carGroupDispatcherInfo = new CarGroupDispatcherInfo();
        carGroupDispatcherInfo.setUserId(Long.valueOf(userId));
        List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
        List<Long> groupIds = carGroupDispatcherInfos.stream().map(CarGroupDispatcherInfo::getCarGroupId).collect(Collectors.toList());
        //查询车队电话
        List<CarGroupFixedPhoneVO> groupPhones = carGroupInfoMapper.selectCarGroupPhones(groupIds);
        DispatcherAndFixedLineVO vo = new DispatcherAndFixedLineVO();
        vo.setDispatcher(userVO);
        vo.setGroupPhones(groupPhones);
        return vo;
    }


    /**
     * 判断车队下是否有车辆
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


}
