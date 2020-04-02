package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.CarLocationDto;
import com.hq.ecmp.mscore.dto.CarSaveDTO;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.ICarInfoService;
import com.hq.ecmp.mscore.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarInfoServiceImpl implements ICarInfoService
{
    @Autowired
    private CarInfoMapper carInfoMapper;
    @Autowired
    private DriverCarRelationInfoMapper driverCarRelationInfoMapper;
    @Autowired
    private CarRefuelInfoMapper carRefuelInfoMapper;
    @Autowired
    private EnterpriseCarTypeInfoMapper enterpriseCarTypeInfoMapper;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param carId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarInfo selectCarInfoById(Long carId)
    {
        return carInfoMapper.selectCarInfoById(carId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarInfo> selectCarInfoList(CarInfo carInfo)
    {
        return carInfoMapper.selectCarInfoList(carInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarInfo(CarInfo carInfo)
    {
        carInfo.setCreateTime(DateUtils.getNowDate());
        return carInfoMapper.insertCarInfo(carInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarInfo(CarInfo carInfo)
    {
        carInfo.setUpdateTime(DateUtils.getNowDate());
        return carInfoMapper.updateCarInfo(carInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarInfoByIds(Long[] carIds)
    {
        return carInfoMapper.deleteCarInfoByIds(carIds);
    }

    /**
     * 删除车辆信息
     *
     * @param carId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarInfoById(Long carId) throws Exception {
        //判断车辆下是否绑定驾驶员
        if(judgeCarBindDriver(carId)){
            throw new Exception("删除失败，请先解绑驾驶员");
        }
        int i = carInfoMapper.deleteCarInfoById(carId);
        if(i != 1){
            throw new Exception("删除失败");
        }
        return i;
    }

    /**
     * 判断是否车辆是否绑定有驾驶员 true绑定有 false没绑定
     * @param carId
     * @return
     */
    public boolean judgeCarBindDriver(Long carId){
        DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
        driverCarRelationInfo.setCarId(carId);
        List<DriverCarRelationInfo> driverCarRelationInfos = driverCarRelationInfoMapper.selectDriverCarRelationInfoList(driverCarRelationInfo);
        int size = driverCarRelationInfos.size();
        //不为 0 则表示绑定有驾驶员
        return size != 0;
    }



    /**
     * 新增车辆
     * @param carSaveDTO
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveCar(CarSaveDTO carSaveDTO, Long userId) throws Exception {
        CarInfo carInfo = setCarInfo(carSaveDTO);
        carInfo.setCreateBy(String.valueOf(userId));
        carInfo.setCreateTime(new Date());
        carInfo.setState(CarConstant.DISABLE_CAR);   //初始化禁用车辆
        //新增车辆表
        int i = carInfoMapper.insertCarInfo(carInfo);
        if(i!= 1){
            throw new Exception("新增车辆失败");
        }
        Long carId = carInfo.getCarId();
        //绑定驾驶员
        List<DriverVO> drivers = carSaveDTO.getDrivers();
        DriverCarRelationInfo driverCarRelationInfo = null;
        for (DriverVO driver : drivers) {
            driverCarRelationInfo = new DriverCarRelationInfo();
            driverCarRelationInfo.setCarId(carId);
            driverCarRelationInfo.setDriverId(driver.getDriverId());
            driverCarRelationInfo.setUserId(driver.getUserId());
            int j = driverCarRelationInfoMapper.insertDriverCarRelationInfo(driverCarRelationInfo);
            if(j != 1){
                throw new Exception("新增车辆失败");
            }
        }

        return carId;
    }

    /**
     * 设置车辆基本信息
     * @param carSaveDTO
     * @return
     */
    private CarInfo setCarInfo(CarSaveDTO carSaveDTO) {
        CarInfo carInfo = new CarInfo();
        carInfo.setCarType(carSaveDTO.getCarType());
        carInfo.setCarLicense(carSaveDTO.getCarLicense());
        carInfo.setEnterpriseCarTypeId(carSaveDTO.getEnterpriseCarTypeId());
        carInfo.setOwnerOrgId(carSaveDTO.getOwnerOrgId());
        carInfo.setCarGroupId(carSaveDTO.getCarGroupId());
        carInfo.setSource(carSaveDTO.getSource());  //TODO 新增
        carInfo.setBuyDate(carSaveDTO.getBuyDate());
        carInfo.setRentStartDate(carSaveDTO.getRentStartDate()); //TODO 新增
        carInfo.setRentEndDate(carSaveDTO.getRentEndDate());
        carInfo.setBorrowStartDate(carSaveDTO.getBorrowStartDate()); //TODO 新增
        carInfo.setBorrowEndDate(carSaveDTO.getBorrowEndDate()); //TODO 新增

        //车辆实照信息
        carInfo.setAssetTag(carSaveDTO.getAssetTag());
        carInfo.setCarColor(carSaveDTO.getCarColor());
        carInfo.setPowerType(carSaveDTO.getPowerType());
        carInfo.setSeatNum(carSaveDTO.getSeatNum());
        carInfo.setPrice(carSaveDTO.getPrice()); //TODO 新增
        carInfo.setTax(carSaveDTO.getTax()); //TODO 新增
        carInfo.setLicensePrice(carSaveDTO.getLicense_price()); //TODO 新增
        carInfo.setCarImgaeUrl(carSaveDTO.getCarImgaeUrl());
        carInfo.setDrivingLicense(carSaveDTO.getDrivingLicense());  //TODO 新增
        carInfo.setDrivingLicenseStartDate(carSaveDTO.getDrivingLicenseStartDate());  //TODO 新增
        carInfo.setCarDrivingLicenseImagesUrl(carSaveDTO.getCarDrivingLicenseImagesUrl());
        return carInfo;
    }

    /**
     * 启用车辆  TODO 缺少车辆有效日期字段
     * @param carId
     * @param userId
     * @return
     */
    @Override
    public int startCar(Long carId, Long userId) throws Exception {
        CarInfo carInfo = new CarInfo();
        carInfo.setState(CarConstant.START_CAR);
        carInfo.setUpdateBy(String.valueOf(userId));
        carInfo.setUpdateTime(new Date());
        carInfo.setCarId(carId);
        int i = carInfoMapper.updateCarInfo(carInfo);
        if(i!=1){
            throw new Exception("启用失败");
        }
        return i;
    }

    /**
     * 可管理车辆总数
     */
    @Override
    public int queryCompanyCarCount(){
        return carInfoMapper.queryCompanyCar();
    }

    /**
     * 禁用车辆
     * @param carId
     * @param userId
     * @return
     */
    @Override
    public int disableCar(Long carId, Long userId) throws Exception {
        CarInfo carInfo = new CarInfo();
        carInfo.setState(CarConstant.DISABLE_CAR);
        carInfo.setUpdateBy(String.valueOf(userId));
        carInfo.setUpdateTime(new Date());
        carInfo.setCarId(carId);
        int i = carInfoMapper.updateCarInfo(carInfo);
        if(i!=1){
            throw new Exception("禁用失败");
        }
        return i;
    }

    /**
     * 维修车辆
     * @param carId
     * @param userId
     * @throws Exception
     */
    @Override
    public void maintainCar(Long carId, Long userId) throws Exception {
        CarInfo carInfo = new CarInfo();
        carInfo.setState(CarConstant.MAINTENANCE_CAR);
        carInfo.setUpdateTime(new Date());
        carInfo.setUpdateBy(String.valueOf(userId));
        int i = carInfoMapper.updateCarInfo(carInfo);
        if(i!= 1){
            throw new Exception();
        }
    }

    /**
     * 分页查询车队的车辆列表信息
     * @return
     */
    @Override
    public PageResult<CarListVO> selectCarListByGroup(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        CarInfo carInfo1 = new CarInfo();
        carInfo1.setCarGroupId(pageRequest.getCarGroupId());
        //查询车辆所属车队
        CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(pageRequest.getCarGroupId());
        String carGroupName = carGroupInfo.getCarGroupName();
        List<CarInfo> carInfos = carInfoMapper.selectCarInfoList(carInfo1);
        CarListVO carListVO = null;
        List<CarListVO> list = new ArrayList<>();
        for (CarInfo carInfo : carInfos) {
            Long carId = carInfo.getCarId();
            Long enterpriseCarTypeId = carInfo.getEnterpriseCarTypeId();
            //查询fuelType
            String fuelType = selectFuelType(carId);
            //查询level
            EnterpriseCarTypeInfo enterpriseCarTypeInfo = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(enterpriseCarTypeId);
            String level = null;
            if(enterpriseCarTypeInfo != null){
                level = enterpriseCarTypeInfo.getLevel();
            }
            //查詢车辆可选驾驶员
            List<DriverVO> list2 = selectCarEffectiveDrivers(carId);
            carListVO = CarListVO.builder().carType(carInfo.getCarType())
                    .carLicense(carInfo.getCarLicense())
                    .fuelType(fuelType)
                    .level(level)
                    .assetTag(carInfo.getAssetTag())
                    .ownerOrgId(carInfo.getOwnerOrgId())
                    .carGroupName(carGroupName)
                    .driverInfo(list2)
                    .driverNum(list2.size())
                    .source(carInfo.getSource())
                    .state(carInfo.getState())
                    .carId(carId)
                    .build();
            list.add(carListVO);
        }
        PageInfo<CarInfo> info = new PageInfo<>(carInfos);
        return new PageResult<>(info.getTotal(),info.getPages(),list);
    }

    /**
     * 车辆详情
     * @param carId
     * @return
     */
    @Override
    public CarDetailVO selectCarDetail(Long carId) {
        CarInfo carInfo = carInfoMapper.selectCarInfoById(carId);
        Long deptId = carInfo.getDeptId();
        //查询所属公司  TODO
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(deptId);
        String deptName = ecmpOrg.getDeptName();
        //根据车辆id 查询可用驾驶员
        List<DriverVO> drivers = selectCarEffectiveDrivers(carId);
        CarDetailVO carDetailVO = CarDetailVO.builder().carType(carInfo.getCarType())
                .carLicense(carInfo.getCarLicense())
                .carColor(carInfo.getCarColor())
                .powerType(carInfo.getPowerType())
                .seatNum(carInfo.getSeatNum())
                .ownerOrg(deptName)
                .deptName("车管部")  // TODO 车队 与 车辆都是有所属公司字段 但是没有所属部门字段
                .source(carInfo.getSource())
                .buyDate(carInfo.getBuyDate())
                .drivers(drivers)
                .driverNum(drivers.size())
                .assetTag(carInfo.getAssetTag())
                .price(carInfo.getPrice())
                .tax(carInfo.getTax())
                .licensePrice(carInfo.getLicensePrice())
                .carImgaeUrl(carInfo.getCarImgaeUrl())
                .carDrivingLicenseImagesUrl(carInfo.getCarDrivingLicenseImagesUrl())
                .build();
        return carDetailVO;
    }

    /**
     * 修改车辆基本信息 （不含修改绑定驾驶员信息）
     * @param carSaveDTO
     * @param userId
     */
    @Override
    public void updateCar(CarSaveDTO carSaveDTO, Long userId) throws Exception {
        CarInfo carInfo = setCarInfo(carSaveDTO);
        carInfo.setUpdateBy(String.valueOf(userId));
        carInfo.setUpdateTime(new Date());
        int i = carInfoMapper.updateCarInfo(carInfo);
        if( i!= 1){
            throw new Exception();
        }
    }

    /**
     * 根据车辆id查询车辆 可用的驾驶员信息
     */
    public List<DriverVO> selectCarEffectiveDrivers(Long carId){
        //根据carId 查询 driver 集合
        DriverCarRelationInfo driverCarRelationInfo = new DriverCarRelationInfo();
        driverCarRelationInfo.setCarId(carId);
        List<DriverCarRelationInfo> driverCarRelationInfos = driverCarRelationInfoMapper.selectDriverCarRelationInfoList(driverCarRelationInfo);
        DriverVO driverVO = null;
        List<DriverVO> list = new ArrayList<>();
        for (DriverCarRelationInfo carRelationInfo : driverCarRelationInfos) {
            Long driverId = carRelationInfo.getDriverId();
            //根据 driverId 查询驾驶员信息 （有效的驾驶员）
            DriverInfo driverInfo = driverInfoMapper.selectEffectiveDriverInfoById(driverId);
            if(ObjectUtils.isNotEmpty(driverInfo)){
                driverVO = new DriverVO();
                driverVO.setUserId(driverInfo.getUserId());
                driverVO.setDriverName(driverInfo.getDriverName());
                driverInfo.setDriverId(driverId);
                list.add(driverVO);
            }
        }
        return list;
    }

    /**
     * 查询车辆燃料类型
     * @param carId
     * @return
     */
    private String selectFuelType(Long carId){
        //查询fuelType
        CarRefuelInfo carRefuelInfo = new CarRefuelInfo();
        carRefuelInfo.setCarId(carId);
        List<CarRefuelInfo> carRefuelInfos = carRefuelInfoMapper.selectCarRefuelInfoList(carRefuelInfo);
        String fuelType = null;
        if (CollectionUtils.isNotEmpty(carRefuelInfos)) {
            fuelType = carRefuelInfos.get(0).getFuelType();
        }
        return fuelType;
    }

    @Override
    public List<CarLocationVo> locationCars(CarLocationDto carLocationDto) {
        List<CarLocationVo> carLocationVos = carInfoMapper.locationCars(carLocationDto);
        return carLocationVos;
    }

	@Override
	public CarGroupCarInfo queryCarGroupCarList(Long carGroupId) {
		CarGroupCarInfo carGroupCarInfo = new CarGroupCarInfo();
		List<CarListVO> queryCarGroupCarList = carInfoMapper.queryCarGroupCarList(carGroupId);
		carGroupCarInfo.setList(queryCarGroupCarList);
		// 查询车队对应的部门和公司
		CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
		if (null != carGroupInfo) {
			Long ownerCompany = carGroupInfo.getOwnerCompany();
			if (null != ownerCompany) {
				EcmpOrg company = ecmpOrgMapper.selectEcmpOrgById(ownerCompany);
				if (null != company) {
					carGroupCarInfo.setCompanyName(company.getDeptName());
				}
			}
			Long ownerOrg = carGroupInfo.getOwnerOrg();
			if (null != ownerOrg) {
				EcmpOrg dept = ecmpOrgMapper.selectEcmpOrgById(ownerOrg);
				if (null != dept) {
					carGroupCarInfo.setDeptName(dept.getDeptName());
				}
			}
		}
		return carGroupCarInfo;
	}
}
