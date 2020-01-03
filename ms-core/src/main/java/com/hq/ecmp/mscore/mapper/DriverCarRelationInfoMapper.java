package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.DriverCarRelationInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface DriverCarRelationInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param userId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public DriverCarRelationInfo selectDriverCarRelationInfoById(Long userId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DriverCarRelationInfo> selectDriverCarRelationInfoList(DriverCarRelationInfo driverCarRelationInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertDriverCarRelationInfo(DriverCarRelationInfo driverCarRelationInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param driverCarRelationInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateDriverCarRelationInfo(DriverCarRelationInfo driverCarRelationInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param userId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverCarRelationInfoById(Long userId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDriverCarRelationInfoByIds(Long[] userIds);
}
