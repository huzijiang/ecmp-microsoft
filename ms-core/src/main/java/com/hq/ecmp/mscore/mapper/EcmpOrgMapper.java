package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpOrg;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface EcmpOrgMapper {
    /**
     * 查询部门
     *
     * @param deptId 部门ID
     * @return 部门
     */
    public EcmpOrg selectEcmpOrgById(Long deptId);

    /**
     * 根据公司id查询部门对象列表
     * @param companyId
     * @param name
     * @return
     */
    public List<EcmpOrg> selectEcmpOrgsByCompanyId(@Param("companyId") Long companyId,@Param("name") String name);

    /**
     * 查询部门列表
     *
     * @param ecmpOrg 部门
     * @return 部门集合
     */
    public List<EcmpOrg> selectEcmpOrgList(EcmpOrg ecmpOrg);

    /**
     * 新增部门
     *
     * @param ecmpOrg 部门
     * @return 结果
     */
    public int insertEcmpOrg(EcmpOrg ecmpOrg);

    /**
     * 修改部门
     *
     * @param ecmpOrg 部门
     * @return 结果
     */
    public int updateEcmpOrg(EcmpOrg ecmpOrg);

    /**
     * 删除部门
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteEcmpOrgById(Long deptId);

    /**
     * 批量删除部门
     *
     * @param deptIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpOrgByIds(Long[] deptIds);
}
