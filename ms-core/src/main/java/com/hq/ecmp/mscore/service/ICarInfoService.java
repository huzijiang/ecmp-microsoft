package com.hq.ecmp.mscore.service;

import java.util.List;
import java.util.Map;

import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.dto.CarLocationDto;
import com.hq.ecmp.mscore.dto.CarSaveDTO;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.vo.CarDetailVO;
import com.hq.ecmp.mscore.vo.CarGroupCarInfo;
import com.hq.ecmp.mscore.vo.CarListVO;
import com.hq.ecmp.mscore.vo.CarLocationVo;
import com.hq.ecmp.mscore.vo.PageResult;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ICarInfoService
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
     * 批量删除【请填写功能名称】
     *
     * @param carIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarInfoByIds(Long[] carIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carId 车辆ID
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteCarInfoById(Long carId, Long userId) throws Exception;

    /**
     * 新增车辆
     * @param carSaveDTO
     * @param userId
     */
    Long saveCar(CarSaveDTO carSaveDTO, Long userId) throws Exception;

    int startCar(Long carId, Long userId) throws Exception;

    int disableCar(Long carId, Long userId) throws Exception;

    /**
     * 维保车辆
     * @param carId
     * @param userId
     */
    void maintainCar(Long carId, Long userId) throws Exception;

    /**
     * 分页查询车队的车辆列表信息
     *
     * @return
     */
    PageResult<CarListVO> selectCarListByGroup(PageRequest pageRequest);

    /**
     * 查询车辆详情
     * @param carId
     * @return
     */
    CarDetailVO selectCarDetail(Long carId);

    /**
     * 修改车辆信息
     * @param carSaveDTO
     * @param userId
     */
    void updateCar(CarSaveDTO carSaveDTO, Long userId) throws Exception;
 
    /**
     * 可管理车辆总数
     */
    public int queryCompanyCarCount();

    public List<CarLocationVo> locationCars(CarLocationDto carLocationDto);
    
    
    public CarGroupCarInfo queryCarGroupCarList(Map map);

    /*车辆信息回显*/
    CarSaveDTO selectCarInfoFeedBack(Long carId);

    /**
     * 查询车辆品牌列表
     * @return
     */
    List<String> selectCarTypeList();

    /**
     * 检验车辆状态
     */
    public void checkCarState();

}
