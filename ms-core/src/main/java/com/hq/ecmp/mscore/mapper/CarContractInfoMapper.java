package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CarContractInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface CarContractInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param contractId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarContractInfo selectCarContractInfoById(Long contractId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carContractInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarContractInfo> selectCarContractInfoList(CarContractInfo carContractInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carContractInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarContractInfo(CarContractInfo carContractInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carContractInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarContractInfo(CarContractInfo carContractInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param contractId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarContractInfoById(Long contractId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param contractIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCarContractInfoByIds(Long[] contractIds);
}
