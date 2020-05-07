package com.hq.ecmp.mscore.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.formula.functions.Now;
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
@Slf4j
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
        //设置车辆初始化状态
        setCarInitialState(carSaveDTO, carInfo);
        //初始化未锁定状态 锁定状态 0000   未锁定    1111   已锁定
        carInfo.setLockState(CarConstant.UN_LOCKED);
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
     * //如果是自有车 初始化微启用状态 如果是借调车辆/租赁车辆 则根据开始时间看 没到开始时间为待启用 已到开始时间为启用状态
     * @param carSaveDTO
     * @param carInfo
     */
    private void setCarInitialState(CarSaveDTO carSaveDTO, CarInfo carInfo) {
        String source = carSaveDTO.getSource();
        if(CarConstant.OWN_CAR.equals(source)){
            //1.自有车 初始化为启用状态
            carInfo.setState(CarConstant.START_CAR);
        }else if(CarConstant.BORROW_CAR.equals(source)){
            //2.借来的车
            Date borrowStartDate = carSaveDTO.getBorrowStartDate();
            if(borrowStartDate == null){
                throw new RuntimeException("借调开始时间不能为空");
            }
            if(borrowStartDate.before(new Date())){
                //当借调开始时间 小于 当前时间时 初始化启用状态
                carInfo.setState(CarConstant.START_CAR);
            }else {
                // 借调开始时间 大于 当前时间时 初始化待启用状态
                carInfo.setState(CarConstant.WAIT_START_CAR);
            }
        }else if(CarConstant.RENT_CAR.equals(source)){
            //3. 租来的车
            Date rentStartDate = carSaveDTO.getRentStartDate();
            if(rentStartDate == null){
                throw new RuntimeException("租赁车辆开始时间不能为空");
            }
            if(rentStartDate.before(new Date())){
                //租赁开始时间 小于 当前时间时 初始化为 启用状态
                carInfo.setState(CarConstant.START_CAR);
            }else {
                //租赁开始时间 大于等于开始时间 初始化为 待启用状态
                carInfo.setState(CarConstant.WAIT_START_CAR);
            }
        }else {
            throw new RuntimeException("车辆来源不明");
        }
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
        carInfo.setCompanyId(carSaveDTO.getCompanyId());
        carInfo.setCarGroupId(carSaveDTO.getCarGroupId());
        carInfo.setSource(carSaveDTO.getSource());
        carInfo.setBuyDate(carSaveDTO.getBuyDate());
        carInfo.setRentStartDate(carSaveDTO.getRentStartDate());
        carInfo.setRentEndDate(carSaveDTO.getRentEndDate());
        carInfo.setBorrowStartDate(carSaveDTO.getBorrowStartDate());
        carInfo.setBorrowEndDate(carSaveDTO.getBorrowEndDate());
        carInfo.setCompanyId(carSaveDTO.getCompanyId());
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
     * 启用车辆
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
                .drivingLicenseStartDate(carInfo.getDrivingLicenseStartDate()==null?"":simpleDateFormat.format(carInfo.getDrivingLicenseStartDate()))
                .drivingLicenseEndDate(carInfo.getDrivingLicenseStartDate()==null?"":simpleDateFormat.format(carInfo.getDrivingLicenseEndDate()))
                .build();
        return carDetailVO;
    }

    /**
     * 修改车辆基本信息 （不含修改绑定驾驶员信息）
     * @param carSaveDTO
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCar(CarSaveDTO carSaveDTO, Long userId) throws Exception {
        CarInfo carInfo = setCarInfo(carSaveDTO);
        carInfo.setUpdateBy(String.valueOf(userId));
        carInfo.setUpdateTime(new Date());
        int i = carInfoMapper.updateCarInfo(carInfo);
        if(i!= 1){
            throw new Exception("修改车辆失败");
        }
        Long carId = carInfo.getCarId();
        //绑定驾驶员
        List<DriverVO> drivers = carSaveDTO.getDrivers();
        DriverCarRelationInfo driverCarRelationInfo = null;
        //删除原有驾驶员信息
        driverCarRelationInfoMapper.deleteCarByCarId(carId);
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
	public CarGroupCarInfo queryCarGroupCarList(Map map) {
        Long carGroupId = Long.valueOf(map.get("carGroupId").toString());
        Long driverId = map.get("driverId")==null?null:Long.valueOf(map.get("driverId").toString());
        String search = map.get("search")==null?null:map.get("search").toString();
		CarGroupCarInfo carGroupCarInfo = new CarGroupCarInfo();
		List<CarListVO> queryCarGroupCarList = carInfoMapper.queryCarGroupCarList(carGroupId,driverId,search);
		carGroupCarInfo.setList(queryCarGroupCarList);
		// 查询车队对应的部门和公司
		CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
		if (null != carGroupInfo) {
			Long ownerCompany = carGroupInfo.getCompanyId();
			if (null != ownerCompany) {
				EcmpOrg company = ecmpOrgMapper.selectEcmpOrgById(ownerCompany);
				if (null != company) {
					carGroupCarInfo.setCompanyName(company.getDeptName());
				}
			}
			carGroupCarInfo.setDeptName(carGroupInfo.getCarGroupName());
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
                .carTypeName(carTypeName)
                .licensePrice(carInfo.getLicensePrice())
                .ownerOrgId(carInfo.getDeptId())
                .ownerCompanyName(deptName)
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

    @Override
    public PageResult carWorkOrderList(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<DriverOrderVo> driverOrderVos =carInfoMapper.carWorkOrderList(pageRequest.getCarGroupId(),pageRequest.getDate(),pageRequest.getSearch());
        Long count=carInfoMapper.carWorkOrderListCount(pageRequest.getCarGroupId(),pageRequest.getDate(),pageRequest.getSearch());
        Collections.sort(driverOrderVos);
        return new PageResult(count,driverOrderVos);
    }

    /**
     * 检验车辆状态
     * -------状态
     *    S000    启用中
     *    S001    禁用中
     *    S002    维护中
     *    S003    已到期
     *    S004    待启用
     *    S101    被借调
     *    S444    被删除
     *    1.租赁到期   2.借调到期  3.行驶证到期（所有来源车辆都要考虑） 4.租赁时间没开始 5.借调时间没开始
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkCarState() {
        //1.查询所有没被删除的车辆
        List<CarInfo> carInfoList = carInfoMapper.selectAll();
        StringBuilder licenseTimeOutCars = new StringBuilder();
        StringBuilder rentTimeOutCars = new StringBuilder();
        StringBuilder rentStartCars = new StringBuilder();
        StringBuilder borrowStartCars = new StringBuilder();
        StringBuilder borrowTimeOutCars = new StringBuilder();
        Date date = new Date();
        for (CarInfo carInfo : carInfoList) {
            String source = carInfo.getSource();
            String state = carInfo.getState();
            //1.查看行驶证是否过期 行驶证过期 以上所有状态被覆盖 为已过期状态
            Date drivingLicenseEndDate = carInfo.getDrivingLicenseEndDate();
            if(drivingLicenseEndDate != null){
                if(drivingLicenseEndDate.before(date) && !CarConstant.TIME_OUT_CAR.equals(state)){
                    //A.------如果行驶证结束日期 小于 当前时间 （即行驶证已过期）并且状态没改为已过期的 把状态改为已过期
                    carInfo.setState(CarConstant.TIME_OUT_CAR);
                    licenseTimeOutCars.append(carInfo.getCarId()+" ");
                    carInfoMapper.updateCarInfo(carInfo);
                }else if(drivingLicenseEndDate.after(date) && !CarConstant.TIME_OUT_CAR.equals(state)){
                    //B.------如果行驶证没过期 并且状态不是已过期的车辆 （因为已过期的车辆的状态只能通过手动修改）
                    //2.如果是租来的车 查看租赁开始时间 是否到了
                    if(CarConstant.RENT_CAR.equals(source)){
                        Date rentStartDate = carInfo.getRentStartDate();
                        Date rentEndDate = carInfo.getRentEndDate();
                        if(rentStartDate != null || rentEndDate != null){
                            log.error("租赁车辆开始时间或结束时间为空,车辆id:{}",carInfo.getCarId());
                            throw new RuntimeException("租赁车辆开始时间和结束时间不能为空");
                        }
                        if(rentEndDate.before(date) && rentEndDate.after(date) && CarConstant.WAIT_START_CAR.equals(state)){
                            //2.1租赁开始时间到了 结束时间没到 并且 则状态为待启用状态 则启用车辆
                            carInfo.setState(CarConstant.START_CAR);
                            rentStartCars.append(carInfo.getCarId()+" ");
                            carInfoMapper.updateCarInfo(carInfo);
                        }
                        if(rentEndDate.before(date) && !CarConstant.TIME_OUT_CAR.equals(state)){
                            //2.2租赁结束时间到了 没改为已到期状态的 状态改为为已到期
                            carInfo.setState(CarConstant.TIME_OUT_CAR);
                            rentTimeOutCars.append(carInfo.getCarId()+" ");
                            carInfoMapper.updateCarInfo(carInfo);
                        }
                    }
                    //3.如果是借来的车 借调开始时间 和 借调结束时间
                    if(CarConstant.BORROW_CAR.equals(source)){
                        Date borrowStartDate = carInfo.getBorrowStartDate();
                        Date borrowEndDate = carInfo.getBorrowEndDate();
                        if(borrowStartDate == null || borrowEndDate == null){
                            log.error("借来车辆开始时间或结束时间为空,车辆id:{}",carInfo.getCarId());
                            throw new RuntimeException("借调车辆开始时间和结束时间不能为空");
                        }
                        if(borrowStartDate.before(date) && borrowEndDate.after(date) && CarConstant.WAIT_START_CAR.equals(state)){
                            //3.1借来车辆开始时间到了 结束时间没到 并且是待启用状态的 则状态为启用状态
                            carInfo.setState(CarConstant.START_CAR);
                            borrowStartCars.append(carInfo.getCarId()+" ");
                            carInfoMapper.updateCarInfo(carInfo);
                        }
                        if(borrowEndDate.before(date) && !CarConstant.TIME_OUT_CAR.equals(state)){
                            //3.2借来车辆结束时间到了 但是状态还没改为已到期状态的 车辆状态改为已到期
                            carInfo.setState(CarConstant.TIME_OUT_CAR);
                            borrowTimeOutCars.append(carInfo.getCarId()+" ");
                            carInfoMapper.updateCarInfo(carInfo);
                        }
                    }
                }
            }
        }
        log.info("{}行驶证已过期的车辆id集合：{},租赁到期的车辆id集合：{},租赁车辆开始启用的车辆id集合：{},借来车辆开始启用的车辆id集合：{},借来车辆到期的车辆id集合：{}",
                DateUtils.getMonthAndToday(),licenseTimeOutCars,
                rentTimeOutCars,rentStartCars,
                borrowStartCars,borrowTimeOutCars);
    }
}
