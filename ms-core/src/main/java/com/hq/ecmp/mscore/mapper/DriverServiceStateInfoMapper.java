package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.DriverServiceStateInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface DriverServiceStateInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public DriverServiceStateInfo selectDriverServiceStateInfoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverServiceStateInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DriverServiceStateInfo> selectDriverServiceStateInfoList(DriverServiceStateInfo driverServiceStateInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param driverServiceStateInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertDriverServiceStateInfo(DriverServiceStateInfo driverServiceStateInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param driverServiceStateInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateDriverServiceStateInfo(DriverServiceStateInfo driverServiceStateInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverServiceStateInfoById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDriverServiceStateInfoByIds(Long[] ids);
}
