package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.bo.CarTrackInfo;

import java.util.List;

/**
 * 车辆轨迹信息Service接口
 * 
 * @author hqer
 * @date 2020-06-02
 */
public interface ICarTrackInfoService 
{
    /**
     * 查询车辆轨迹信息
     * 
     * @param id 车辆轨迹信息ID
     * @return 车辆轨迹信息
     */
    public CarTrackInfo selectCarTrackInfoById(Long id);

    /**
     * 查询车辆轨迹信息列表
     * 
     * @param carTrackInfo 车辆轨迹信息
     * @return 车辆轨迹信息集合
     */
    public List<CarTrackInfo> selectCarTrackInfoList(CarTrackInfo carTrackInfo);

    /**
     * 新增车辆轨迹信息
     * 
     * @param carTrackInfo 车辆轨迹信息
     * @return 结果
     */
    public int insertCarTrackInfo(CarTrackInfo carTrackInfo);

    /**
     * 修改车辆轨迹信息
     * 
     * @param carTrackInfo 车辆轨迹信息
     * @return 结果
     */
    public int updateCarTrackInfo(CarTrackInfo carTrackInfo);

    /**
     * 批量删除车辆轨迹信息
     * 
     * @param ids 需要删除的车辆轨迹信息ID
     * @return 结果
     */
    public int deleteCarTrackInfoByIds(Long[] ids);

    /**
     * 删除车辆轨迹信息信息
     * 
     * @param id 车辆轨迹信息ID
     * @return 结果
     */
    public int deleteCarTrackInfoById(Long id);
}