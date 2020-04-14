package com.hq.ecmp.mscore.service.impl;

import java.text.SimpleDateFormat;
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
     * @param carId 车辆ID
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteCarInfoById(Long carId, Long userId) throws Exception {
        //判断车辆下是否绑定驾驶员
        if(judgeCarBindDriver(carId)){
            throw new Exception("请先删除该车辆下的所有驾驶员，再尝试删除");
        }
        CarInfo carInfo = new CarInfo();
        carInfo.setCarId(carId);
        carInfo.setState(CarConstant.DELETE_CAR);
        carInfo.setUpdateBy(String.valueOf(userId));
        carInfo.setUpdateTime(new Date());
        //更新车辆状态为启用
        int row = carInfoMapper.updateCarInfo(carInfo);
        if(row != 1){
            throw new Exception("删除失败");
        }
        return row;
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
        carInfo.setState(CarConstant.START_CAR);   //初始化启用车辆
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
            Long driverId = driver.getDriverId();
            driverCarRelationInfo.setDriverId(driverId);
            DriverInfo driverInfo = driverInfoMapper.selectDriverInfoById(driverId);
            if (driverInfo != null){
                driverCarRelationInfo.setUserId(driverInfo.getUserId());
            }
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
        carInfo.setCarId(carSaveDTO.getCarId());
        carInfo.setCarType(carSaveDTO.getCarType());
        carInfo.setCarLicense(carSaveDTO.getCarLicense());
        carInfo.setCarTypeId(carSaveDTO.getEnterpriseCarTypeId());
        carInfo.setDeptId(carSaveDTO.getOwnerOrgId());
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
        carInfo.setLicensePrice(carSaveDTO.getLicensePrice()); //TODO 新增
        carInfo.setCarImgaeUrl(carSaveDTO.getCarImgaeUrl());
        carInfo.setDrivingLicense(carSaveDTO.getDrivingLicense());  //TODO 新增
        carInfo.setDrivingLicenseStartDate(carSaveDTO.getDrivingLicenseStartDate());  //TODO 新增
        carInfo.setDrivingLicenseEndDate(carSaveDTO.getDrivingLicenseEndDate());
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
        int row = carInfoMapper.updateStartCar(carInfo);
        if(row != 1){
            throw new Exception("启用车辆失败，借调到期，租赁到期，或行驶证到期，或已删除");
        }
        return row;
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
        int row = carInfoMapper.updateCarInfo(carInfo);
        if(row != 1){
            throw new Exception("禁用失败");
        }
        return row;
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
        carInfo.setCarId(carId);
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
        CarInfo carInfo1 = new CarInfo();
        Long carGroupId = pageRequest.getCarGroupId();
        String state = pageRequest.getState();
        String search = pageRequest.getSearch();
        //查询车辆所属车队
        CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(pageRequest.getCarGroupId());
        String carGroupName = carGroupInfo.getCarGroupName();
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<CarInfo> carInfos = carInfoMapper.selectCarInfoListByGroupId(carGroupId,pageRequest.getCarTypeId(),state,search);
        CarListVO carListVO = null;
        List<CarListVO> list = new ArrayList<>();
        for (CarInfo carInfo : carInfos) {
            Long carId = carInfo.getCarId();
            Long carTypeId = carInfo.getCarTypeId();
            //查询fuelType
            String fuelType = selectFuelType(carId);
            //查询level
            EnterpriseCarTypeInfo enterpriseCarTypeInfo = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(carTypeId);
            String level = null;
            String levelName = null;
            if(enterpriseCarTypeInfo != null){
                level = enterpriseCarTypeInfo.getLevel();
                levelName = enterpriseCarTypeInfo.getName();
            }
            //查詢车辆可选驾驶员
            List<DriverVO> list2 = selectCarEffectiveDrivers(carId);
            carListVO = CarListVO.builder().carType(carInfo.getCarType())
                    .carLicense(carInfo.getCarLicense())
                    .fuelType(fuelType)
                    .level(level)
                    .levelName(levelName)
                    .assetTag(carInfo.getAssetTag())
                    .ownerOrgId(carInfo.getOwnerOrgId())
                    .carTypeId(carTypeId)
                    .carGroupName(carGroupName)
                    .driverInfo(list2)
                    .driverNum(list2.size())
                    .source(carInfo.getSource())
                    .state(carInfo.getState())
                    .carId(carId)
                    .powerType(carInfo.getPowerType())
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                .carId(carId)
                .drivingLicenseStartDate(simpleDateFormat.format(carInfo.getDrivingLicenseStartDate()))
                .drivingLicenseEndDate(simpleDateFormat.format(carInfo.getDrivingLicenseEndDate()))
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
                driverVO.setDriverId(driverInfo.getDriverId());
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

	/*车辆信息回显*/
    @Override
    public CarSaveDTO selectCarInfoFeedBack(Long carId) {
        CarInfo carInfo = carInfoMapper.selectCarInfoById(carId);
        //根据车辆id 查询可用驾驶员
        List<DriverVO> drivers = selectCarEffectiveDrivers(carId);
        //查询车队名字
        Long carGroupId = carInfo.getCarGroupId();
        CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
        String carGroupName = null;
        if(carGroupInfo != null){
            carGroupName = carGroupInfo.getCarGroupName();
        }
        //查车型名字
        EnterpriseCarTypeInfo enterpriseCarTypeInfo = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(carInfo.getCarTypeId());
        String carTypeName = null;
        if(enterpriseCarTypeInfo != null){
            carTypeName = enterpriseCarTypeInfo.getName();
        }
        //燃料类型
       // carRefuelInfoMapper.selectCarRefuelInfoById(carInfo.getPowerType())
        //查询所属公司
        Long deptId = carInfo.getDeptId();
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(deptId);
        String deptName = ecmpOrg.getDeptName();
        CarSaveDTO carSaveDTO = CarSaveDTO.builder()
                .assetTag(carInfo.getAssetTag())
                .borrowEndDate(carInfo.getBorrowEndDate())
                .borrowStartDate(carInfo.getBorrowStartDate())
                .buyDate(carInfo.getBuyDate())
                .carColor(carInfo.getCarColor())
                .carDrivingLicenseImagesUrl(carInfo.getCarDrivingLicenseImagesUrl())
                .carGroupId(carInfo.getCarGroupId())  //车队名字
                .carGroupName(carGroupName)
                .carId(carId)
                .carImgaeUrl(carInfo.getCarImgaeUrl())
                .carLicense(carInfo.getCarLicense())
                .carType(carInfo.getCarType())
                .drivers(drivers) //查的
                .drivingLicense(carInfo.getDrivingLicense())
                .drivingLicenseEndDate(carInfo.getDrivingLicenseEndDate())
                .drivingLicenseStartDate(carInfo.getDrivingLicenseStartDate())
                .enterpriseCarTypeId(carInfo.getCarTypeId())
                .carTypeName(carTypeName) //查的名字
                .licensePrice(carInfo.getLicensePrice())
                .ownerOrgId(carInfo.getDeptId())
                .ownerCompanyName(deptName) //查的名字
                .powerType(carInfo.getPowerType())
                //.powerTypeName(null) //查的名字 暂不使用
                .price(carInfo.getPrice())
                .rentEndDate(carInfo.getRentEndDate())
                .rentStartDate(carInfo.getRentStartDate())
                .seatNum(carInfo.getSeatNum())
                .source(carInfo.getSource())
                .tax(carInfo.getTax())
                .build();
        return carSaveDTO;

    }

    /**
     * 车辆品牌列表
     * @return
     */
    @Override
    public List<String> selectCarTypeList() {
        return carInfoMapper.selectCarTypeList();
    }
}
