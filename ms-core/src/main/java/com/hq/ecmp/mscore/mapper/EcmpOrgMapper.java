package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.vo.EcmpOrgVo;
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
     * 查询所有公司一级
     *l
     * @param parentId 上级ID
     * @return 公司
     */
    public List<EcmpOrgVo> selectByEcmpOrgParentId(@Param("deptId") Long deptId,@Param("parentId") Long parentId,@Param("deptType")String deptType);
    /**
     * 查询部门详情
     *
     * @param deptId 部门ID
     * @return ecmpOrg
     */
    public EcmpOrgDto selectByDeptId(@Param("deptId") Long deptId);
    /*
     * 添加部门
     *  @param  ecmpOrg
     * @return int
     * */
    public  int addDept(EcmpOrgVo ecmpOrg);
    /*
     * 修改部门
     *  @param  ecmpOrg
     * @return int
     * */
    public  int updateDept(EcmpOrgVo ecmpOrg);
    /**
     * 部门编号验证
     * @param  companyId
     * @return companyIdNum
     * */
    public  int getCheckingDepcCompanyId(@Param("companyId")Long companyId);




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
    public int insertEcmpOrg(EcmpOrgVo ecmpOrg);

    /**
     * 修改部门
     *
     * @param ecmpOrg 部门
     * @return 结果
     */
    public int updateEcmpOrg(EcmpOrgVo ecmpOrg);

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

    /**
     * 查询子公司列表
     *
     * @return 结果
     */
    public String[] selectSubCompany(Long deptId);

    /**
     * 查询分/子公司详情
     *
     * @param deptId 部门ID
     * @return 部门
     */
    public EcmpOrgDto getSubDetail(Long deptId);

    /**
     * 逻辑删除部门信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int updateDelFlagById( Long deptId);

    /**
     * 逻辑批量删除部门信息
     *
     * @param deptIds 部门ID
     * @return 结果
     */
    public int updateDelFlagByIds(Long[] deptIds);

    /**
     * 禁用/启用  分/子公司
     *
     * @param deptId 部门ID
     * @param status 状态
     * @return 结果
     */
    public int updateUseStatus(@Param("status") String status,@Param("ancestors") String ancestors,@Param("deptId") Long deptId);


    /**
     * 修改部门状态为不可用
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int updateUnUseStatus(Long deptId);

    /*
    *根据deptId查询组织下级是否有数据信息
    * @param deptId 部门ID
    * @return 结果
    * */
    public int selectByAncestorsLikeDeptId(Long deptId);

}
