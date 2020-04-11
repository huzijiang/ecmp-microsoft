package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.bo.SelectCarConditionBo;
import com.hq.ecmp.mscore.bo.WaitSelectedCarBo;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.dto.CarLocationDto;
import com.hq.ecmp.mscore.vo.CarListVO;
import com.hq.ecmp.mscore.vo.CarLocationVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface CarInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param carId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarInfo selectCarInfoById(Long carId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarInfo> selectCarInfoList(CarInfo carInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarInfo(CarInfo carInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarInfo(CarInfo carInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param carId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarInfoById(Long carId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCarInfoByIds(Long[] carIds);

    /**
     * 查询某车队的车辆数
     * @param carGroupId
     * @return
     */
    int selectCountGroupCarByGroupId(Long carGroupId);
    /**
     * 可管理车辆总数
     */
    public int queryCompanyCar();

    /**
     * 根据组织id查询归属车辆信息
     *
     * @param deptId 组织id
     * @return 结果
     */
    public int selectCarCountByDeptId(Long deptId);

    /**
     * 后管监控车辆检索
     * @param carLocationDto
     * @return
     */
    List<CarLocationVo> locationCars(CarLocationDto carLocationDto);

    /**
     * 查询车队启用车辆ids
     * @param carGropuId
     * @return
     */
    List<Long> selectGroupEffectiveCarIds(Long carGropuId);

    /**
     * 禁用车辆
     * @param carId
     * @return
     */
    int disableCarByCarId(Long carId);

    /**
     * 调度员  根据条件查询车队的所有车辆
     * @param selectCarConditionBo
     * @return
     */
    public List<CarInfo> dispatcherSelectCarGroupOwnedCarInfoList(SelectCarConditionBo selectCarConditionBo);

    /**
     * 调度员  根据条件查询车辆
     * @param selectCarConditionBo
     * @return
     */
    public List<CarInfo> dispatcherSelectCarGroupOwnedCarInfoListUseCarLicense(SelectCarConditionBo selectCarConditionBo);

    /**
     * 车辆锁定
     * @param carId
     * @return
     */
    public int lockCar(@Param("carId")long  carId);

    /**
     * 车辆 解除 锁定
     * @param carId
     * @return
     */
    public int unlockCar(@Param("carId")long carId);

    /**
     * 根据车队ID查询车辆的集合
     * @param carGroupId
     * @return
     */
    public List<CarListVO> queryCarGroupCarList(Long carGroupId);

    /**
     * 根据车队id 条件查询车辆列表
     * @param carGroupId
     * @param carTypeId
     * @param state
     * @param search
     * @return
     */
    List<CarInfo> selectCarInfoListByGroupId(@Param("carGroupId") Long carGroupId,@Param("carTypeId") Long carTypeId,@Param("state") String state,@Param("search") String search);

    /**
     * 通过车队ID更新车辆状态
     * @param carInfo
     * @return
     */
    public int updateCarInfoByCarGroupId(CarInfo carInfo);

    /**
     * 车辆品牌列表
     * @return
     */
    List<String> selectCarTypeList();
}
