package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EnterpriseCarTypeInfo;
import com.hq.ecmp.mscore.mapper.EnterpriseCarTypeInfoMapper;
import com.hq.ecmp.mscore.service.IEnterpriseCarTypeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EnterpriseCarTypeInfoServiceImpl implements IEnterpriseCarTypeInfoService
{
    @Autowired
    private EnterpriseCarTypeInfoMapper enterpriseCarTypeInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param carTypeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public EnterpriseCarTypeInfo selectEnterpriseCarTypeInfoById(Long carTypeId)
    {
        return enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(carTypeId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<EnterpriseCarTypeInfo> selectEnterpriseCarTypeInfoList(EnterpriseCarTypeInfo enterpriseCarTypeInfo)
    {
        return enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertEnterpriseCarTypeInfo(EnterpriseCarTypeInfo enterpriseCarTypeInfo)
    {
        enterpriseCarTypeInfo.setCreateTime(DateUtils.getNowDate());
        return enterpriseCarTypeInfoMapper.insertEnterpriseCarTypeInfo(enterpriseCarTypeInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateEnterpriseCarTypeInfo(EnterpriseCarTypeInfo enterpriseCarTypeInfo)
    {
        enterpriseCarTypeInfo.setUpdateTime(DateUtils.getNowDate());
        return enterpriseCarTypeInfoMapper.updateEnterpriseCarTypeInfo(enterpriseCarTypeInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carTypeIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEnterpriseCarTypeInfoByIds(Long[] carTypeIds)
    {
        return enterpriseCarTypeInfoMapper.deleteEnterpriseCarTypeInfoByIds(carTypeIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carTypeId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEnterpriseCarTypeInfoById(Long carTypeId)
    {
        return enterpriseCarTypeInfoMapper.deleteEnterpriseCarTypeInfoById(carTypeId);
    }
}
