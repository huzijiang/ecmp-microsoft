package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpLogininfor;

import java.util.List;

/**
 * 系统访问记录Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface EcmpLogininforMapper
{
    /**
     * 查询系统访问记录
     *
     * @param infoId 系统访问记录ID
     * @return 系统访问记录
     */
    public EcmpLogininfor selectEcmpLogininforById(Long infoId);

    /**
     * 查询系统访问记录列表
     *
     * @param ecmpLogininfor 系统访问记录
     * @return 系统访问记录集合
     */
    public List<EcmpLogininfor> selectEcmpLogininforList(EcmpLogininfor ecmpLogininfor);

    /**
     * 新增系统访问记录
     *
     * @param ecmpLogininfor 系统访问记录
     * @return 结果
     */
    public int insertEcmpLogininfor(EcmpLogininfor ecmpLogininfor);

    /**
     * 修改系统访问记录
     *
     * @param ecmpLogininfor 系统访问记录
     * @return 结果
     */
    public int updateEcmpLogininfor(EcmpLogininfor ecmpLogininfor);

    /**
     * 删除系统访问记录
     *
     * @param infoId 系统访问记录ID
     * @return 结果
     */
    public int deleteEcmpLogininforById(Long infoId);

    /**
     * 批量删除系统访问记录
     *
     * @param infoIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpLogininforByIds(Long[] infoIds);
}
