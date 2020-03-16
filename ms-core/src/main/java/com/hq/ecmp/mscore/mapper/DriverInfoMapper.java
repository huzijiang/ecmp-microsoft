package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.DriverInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface DriverInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public DriverInfo selectDriverInfoById(Long driverId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DriverInfo> selectDriverInfoList(DriverInfo driverInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param driverInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertDriverInfo(DriverInfo driverInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param driverInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateDriverInfo(DriverInfo driverInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverInfoById(Long driverId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param driverIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDriverInfoByIds(Long[] driverIds);

    /**
     * 根据deptId查询归属驾驶员数量
     *
     * @param deptId 组织id
     * @return 结果
     */
    public int selectDriverCountByDeptId(Long deptId);

    /**
     * 禁用/启用  驾驶员
     *
     * @param deptId 部门ID
     * @param state 状态 W001 待审   V000 生效中   NV00 失效
     * @return 结果
     */
    public int updateUseStatus(@Param("deptId") Long deptId, @Param("state") String state);
}
