package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EnterpriseCarTypeInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface EnterpriseCarTypeInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param carTypeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public EnterpriseCarTypeInfo selectEnterpriseCarTypeInfoById(Long carTypeId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EnterpriseCarTypeInfo> selectEnterpriseCarTypeInfoList(EnterpriseCarTypeInfo enterpriseCarTypeInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertEnterpriseCarTypeInfo(EnterpriseCarTypeInfo enterpriseCarTypeInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateEnterpriseCarTypeInfo(EnterpriseCarTypeInfo enterpriseCarTypeInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param carTypeId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEnterpriseCarTypeInfoById(Long carTypeId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carTypeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEnterpriseCarTypeInfoByIds(Long[] carTypeIds);
}
