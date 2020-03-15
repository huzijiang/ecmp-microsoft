package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.mscore.domain.CarGroupDispatcherInfo;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.CarGroupDTO;
import com.hq.ecmp.mscore.mapper.CarGroupDispatcherInfoMapper;
import com.hq.ecmp.mscore.mapper.CarGroupDriverRelationMapper;
import com.hq.ecmp.mscore.mapper.CarGroupInfoMapper;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import com.hq.ecmp.mscore.vo.CarGroupDetailVO;
import com.hq.ecmp.mscore.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;


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
        carGroupInfo.setCityName(carGroupDTO.getCityName());
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
                carGroupDispatcherInfo.setDispatcherId(Long.valueOf(dispatcher.getUserId()));
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
        CarGroupDetailVO carGroupDetailVO = CarGroupDetailVO.builder()
                //车队编号
                .carGroupCode(carGroupInfo.getCarGroupCode())
                //车队名字
                .carGroupName(carGroupInfo.getCarGroupName())
                //所属组织
                .ownerOrg(carGroupInfo.getOwnerOrg())
                //所属城市
                .cityName(carGroupInfo.getCityName())
                //详细地址
                .fullAddress(carGroupInfo.getFullAddress())
                //短地址
                .shortAddress(carGroupInfo.getShortAddress())
                .build();
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
                userVO.setUserPhone(ecmpUser.getPhonenumber());
                userVO.setUserName(ecmpUser.getUserName());
                dispatchers.add(userVO);
            }
        }
        carGroupDetailVO.setDispatchers(dispatchers);
        //查询车队人数
        int countDriver = carGroupDriverRelationMapper.selectCountDriver(carGroupId);
        carGroupDetailVO.setCountDriver(countDriver);
        return carGroupDetailVO;
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
        carGroupInfo.setCityName(carGroupDTO.getCityName());
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
            Integer dispatcherId = dispatcher.getUserId();
            carGroupDispatcherInfo.setCarGroupId(carGroupId);
            carGroupDispatcherInfo.setDispatcherId(Long.valueOf(dispatcherId));
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
    public void disableCarGroup(Long carGroupId, Long userId) throws Exception {
        CarGroupInfo carGroupInfo = new CarGroupInfo();
        carGroupInfo.setUpdateBy(String.valueOf(userId));
        carGroupInfo.setUpdateTime(new Date());
        carGroupInfo.setState(CarConstant.DISABLE_CAR_GROUP);
        int i = carGroupInfoMapper.updateCarGroupInfo(carGroupInfo);
        if(i != 1){
            throw new Exception("禁用车队失败");
        }
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
            throw new Exception();
        }
    }
}
