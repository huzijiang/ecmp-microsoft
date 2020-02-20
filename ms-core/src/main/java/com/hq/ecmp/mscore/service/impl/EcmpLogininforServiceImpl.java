package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.EcmpLogininfor;
import com.hq.ecmp.mscore.mapper.EcmpLogininforMapper;
import com.hq.ecmp.mscore.service.IEcmpLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统访问记录Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpLogininforServiceImpl implements IEcmpLogininforService
{
    @Autowired
    private EcmpLogininforMapper ecmpLogininforMapper;

    /**
     * 查询系统访问记录
     *
     * @param infoId 系统访问记录ID
     * @return 系统访问记录
     */
    @Override
    public EcmpLogininfor selectEcmpLogininforById(Long infoId)
    {
        return ecmpLogininforMapper.selectEcmpLogininforById(infoId);
    }

    /**
     * 查询系统访问记录列表
     *
     * @param ecmpLogininfor 系统访问记录
     * @return 系统访问记录
     */
    @Override
    public List<EcmpLogininfor> selectEcmpLogininforList(EcmpLogininfor ecmpLogininfor)
    {
        return ecmpLogininforMapper.selectEcmpLogininforList(ecmpLogininfor);
    }

    /**
     * 新增系统访问记录
     *
     * @param ecmpLogininfor 系统访问记录
     * @return 结果
     */
    @Override
    public int insertEcmpLogininfor(EcmpLogininfor ecmpLogininfor)
    {
        return ecmpLogininforMapper.insertEcmpLogininfor(ecmpLogininfor);
    }

    /**
     * 修改系统访问记录
     *
     * @param ecmpLogininfor 系统访问记录
     * @return 结果
     */
    @Override
    public int updateEcmpLogininfor(EcmpLogininfor ecmpLogininfor)
    {
        return ecmpLogininforMapper.updateEcmpLogininfor(ecmpLogininfor);
    }

    /**
     * 批量删除系统访问记录
     *
     * @param infoIds 需要删除的系统访问记录ID
     * @return 结果
     */
    @Override
    public int deleteEcmpLogininforByIds(Long[] infoIds)
    {
        return ecmpLogininforMapper.deleteEcmpLogininforByIds(infoIds);
    }

    /**
     * 删除系统访问记录信息
     *
     * @param infoId 系统访问记录ID
     * @return 结果
     */
    @Override
    public int deleteEcmpLogininforById(Long infoId)
    {
        return ecmpLogininforMapper.deleteEcmpLogininforById(infoId);
    }
}
