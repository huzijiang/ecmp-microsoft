package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CarContractInfo;
import com.hq.ecmp.mscore.mapper.CarContractInfoMapper;
import com.hq.ecmp.mscore.service.ICarContractInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class CarContractInfoServiceImpl implements ICarContractInfoService
{
    @Autowired
    private CarContractInfoMapper carContractInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param contractId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CarContractInfo selectCarContractInfoById(Long contractId)
    {
        return carContractInfoMapper.selectCarContractInfoById(contractId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carContractInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CarContractInfo> selectCarContractInfoList(CarContractInfo carContractInfo)
    {
        return carContractInfoMapper.selectCarContractInfoList(carContractInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param carContractInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCarContractInfo(CarContractInfo carContractInfo)
    {
        carContractInfo.setCreateTime(DateUtils.getNowDate());
        return carContractInfoMapper.insertCarContractInfo(carContractInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param carContractInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCarContractInfo(CarContractInfo carContractInfo)
    {
        carContractInfo.setUpdateTime(DateUtils.getNowDate());
        return carContractInfoMapper.updateCarContractInfo(carContractInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param contractIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarContractInfoByIds(Long[] contractIds)
    {
        return carContractInfoMapper.deleteCarContractInfoByIds(contractIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param contractId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCarContractInfoById(Long contractId)
    {
        return carContractInfoMapper.deleteCarContractInfoById(contractId);
    }
}
