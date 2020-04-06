package com.hq.ecmp.mscore.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.OrgConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.CarDriverDTO;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IDriverCarRelationInfoService;
import com.hq.ecmp.mscore.vo.DriverVO;
import com.hq.ecmp.mscore.vo.PageResult;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class DriverCarRelationInfoServiceImpl implements IDriverCarRelationInfoService
{
    @Autowired
    private DriverCarRelationInfoMapper driverCarRelationInfoMapper;
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private CarInfoMapper carInfoMapper;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;
    @Autowired
    private DriverWorkInfoMapper driverWorkInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param userId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public DriverCarRelationInfo selectDriverCarRelationInfoById(Long userId)
    {
        return driverCarRelationInfoMapper.selectDriverCarRelationInfoById(userId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<DriverCarRelationInfo> selectDriverCarRelationInfoList(DriverCarRelationInfo driverCarRelationInfo)
    {
        return driverCarRelationInfoMapper.selectDriverCarRelationInfoList(driverCarRelationInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertDriverCarRelationInfo(DriverCarRelationInfo driverCarRelationInfo)
    {
        return driverCarRelationInfoMapper.insertDriverCarRelationInfo(driverCarRelationInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateDriverCarRelationInfo(DriverCarRelationInfo driverCarRelationInfo)
    {
        return driverCarRelationInfoMapper.updateDriverCarRelationInfo(driverCarRelationInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param userIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverCarRelationInfoByIds(Long[] userIds)
    {
        return driverCarRelationInfoMapper.deleteDriverCarRelationInfoByIds(userIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param userId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverCarRelationInfoById(Long userId)
    {
        return driverCarRelationInfoMapper.deleteDriverCarRelationInfoById(userId);
    }

	@Override
	public Integer batchDriverCarList(DriverCarRelationInfo driverCarRelationInfo) {
		return driverCarRelationInfoMapper.batchDriverCarList(driverCarRelationInfo);
	}

	@Override
	public Integer queryDriverUseCarCount(Long driverId) {
		return driverCarRelationInfoMapper.queryDriverUseCarCount(driverId);
	}

    /**
     * 车辆新增驾驶员
     * @param carDriverDTO
     * @param userId
     */
    @Override
    public void bindCarDrivers(CarDriverDTO carDriverDTO, Long userId) throws Exception {
         Long carId = carDriverDTO.getCarId();
        List<DriverVO> drivers = carDriverDTO.getDrivers();
        DriverCarRelationInfo driverCarRelationInfo = null;
        for (DriverVO driver : drivers) {
            driverCarRelationInfo = new DriverCarRelationInfo();
            driverCarRelationInfo.setUserId(driver.getUserId());
            driverCarRelationInfo.setDriverId(driver.getDriverId());
            driverCarRelationInfo.setCarId(carId);
           // driverCarRelationInfo.setCreateBy(String.valueOf(userId));
           // driverCarRelationInfo.setCreateTime(new Date());
            int i = driverCarRelationInfoMapper.insertDriverCarRelationInfo(driverCarRelationInfo);
            if(i != 1){
                throw new Exception("新增驾驶员失败");
            }
        }
    }

    /**
     * 解绑车辆驾驶员
     * @param carId
     * @param userId
     * @param driverId
     */
    @Override
    public void removeCarDriver(Long carId, Long userId, Long driverId) throws Exception {
        DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
        int i = driverCarRelationInfoMapper.deleteCarDriver(carId,userId,driverId);
        if(i != 1){
            throw new Exception("解绑车辆驾驶员失败");
        }
    }

    /**
     * 分页查询车辆绑定司机列表
     * @param pageNum
     * @param pageSize
     * @param carId
     * @return
     */
    @Override
    public PageResult<DriverVO> selectCarDriversByPage(Integer pageNum, Integer pageSize, Long carId,String workState,String itIsFullTime,String businessFlag) {
        PageHelper.startPage(pageNum,pageSize);
        DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
        CarInfo carInfo = carInfoMapper.selectCarInfoById(carId);
        Long carGroupId = carInfo.getCarGroupId();
        CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
        String carGroupName = carGroupInfo.getCarGroupName();
        driverCarRelationInfo.setCarId(carId);
        List<DriverVO> driverCarRelationInfos = driverInfoMapper.selectDriverInfoByCarId(workState, itIsFullTime, businessFlag, carId);
        DriverVO driverVO = null;
        List<DriverVO> list = new ArrayList<>();
        for (DriverVO carRelationInfo : driverCarRelationInfos) {
            driverVO = new DriverVO();
            Long driverId = carRelationInfo.getDriverId();
            driverVO.setDriverId(driverId);
            driverVO.setDriverName(carRelationInfo.getDriverName());
            driverVO.setUserId(carRelationInfo.getUserId());
            driverVO.setItIsFullTime(carRelationInfo.getItIsFullTime());
            driverVO.setCarGroupId(carGroupId);
            driverVO.setCarGroupName(carGroupName);
            driverVO.setDriverMobile(carRelationInfo.getDriverMobile());
            int carCount = driverCarRelationInfoMapper.queryCountCarByDriverId(driverId);
            driverVO.setCarCount(carCount);
            EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(carRelationInfo.getUserId());
            EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(ecmpUser.getDeptId());
            String ancestors = ecmpOrg.getAncestors();
            String[] split = ancestors.split(",");
            for (int i = split.length-1; i >=0 ; i++) {
                EcmpOrg ecmpOrgDto1 = ecmpOrgMapper.selectEcmpOrgById(Long.parseLong(split[i]));
                if(ecmpOrgDto1.getDeptType().equals(OrgConstant.DEPT_TYPE_1)){
                    driverVO.setCompany(ecmpOrgDto1.getDeptName());
                    break;
                }
            }
            driverVO.setWorkState(carRelationInfo.getWorkState());
            list.add(driverVO);
        }
        PageInfo<DriverVO> pageInfo = new PageInfo<>(list);

        return new PageResult<DriverVO>(pageInfo.getTotal(),pageInfo.getPages(),list);

    }
}
