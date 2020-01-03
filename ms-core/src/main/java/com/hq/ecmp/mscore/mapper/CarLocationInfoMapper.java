package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CarLocationInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface CarLocationInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param carLocationId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarLocationInfo selectCarLocationInfoById(String carLocationId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carLocationInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarLocationInfo> selectCarLocationInfoList(CarLocationInfo carLocationInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carLocationInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarLocationInfo(CarLocationInfo carLocationInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carLocationInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarLocationInfo(CarLocationInfo carLocationInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param carLocationId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarLocationInfoById(String carLocationId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carLocationIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCarLocationInfoByIds(String[] carLocationIds);
}
