package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseInfo;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.EnterpriseCarTypeInfo;
import com.hq.ecmp.mscore.mapper.EcmpEnterpriseInfoMapper;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.mapper.EnterpriseCarTypeInfoMapper;
import com.hq.ecmp.mscore.service.IEnterpriseCarTypeInfoService;
import org.apache.commons.collections.CollectionUtils;
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
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private EcmpEnterpriseInfoMapper ecmpEnterpriseInfoMapper;

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

    /**
     * 查询用户企业有效车型 豪华型 公务型
     * @param userId
     * @return
     */
    @Override
    public List<EnterpriseCarTypeInfo> selectEffectiveCarTypes(Long userId) {
        //查询用户所在公司  用户 -- 部门 -- 公司
        //查询用户所在部门
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(userId);
        //查询用户所在公司
        Long deptId = ecmpUser.getDeptId();
        EcmpEnterpriseInfo ecmpEnterpriseInfo = new EcmpEnterpriseInfo();
        ecmpEnterpriseInfo.setDeptId(deptId);
        //根据部门id查公司
        List<EcmpEnterpriseInfo> ecmpEnterpriseInfos = ecmpEnterpriseInfoMapper.selectEcmpEnterpriseInfoList(ecmpEnterpriseInfo);
        //公司id
        Long enterpriseId = ecmpEnterpriseInfos.get(0).getEnterpriseId();
        //查询公司有效车型 状态 S000   生效中  S444   失效中
        EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
        enterpriseCarTypeInfo.setEnterpriseId(enterpriseId);
        enterpriseCarTypeInfo.setStatus("S000");
        //查询公司车型
        List<EnterpriseCarTypeInfo> enterpriseCarTypeInfos = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
        return enterpriseCarTypeInfos;
    }
}
