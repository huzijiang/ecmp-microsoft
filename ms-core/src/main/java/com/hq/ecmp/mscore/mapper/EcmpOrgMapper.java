package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
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
    public List<EcmpOrgDto> selectByEcmpOrgParentId(@Param("deptId") Long deptId,@Param("parentId") Long parentId,@Param("deptType")String deptType);
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
     * 查询部门
     *
     * @param deptId 部门ID
     * @return 部门
     */
    public EcmpOrg selectEcmpOrgById(Long deptId);


    /**
     * 根据公司id查询部门对象列表
     * @param deptId
     * @param name
     * @return
     */
    public List<EcmpOrg> selectEcmpOrgsByDeptId(@Param("companyId") Long deptId,@Param("name") String name);


    /**
     * 查询部门列表
     *
     * @param ecmpOrg 部门
     * @return 部门集合
     */
    public List<EcmpOrg> selectEcmpOrgList(EcmpOrg ecmpOrg);

    /**
     * 查询分/子公司、部门编号是否已存在
     *
     * @param deptCode 分/子公司、部门编号
     * @return
     */
    public int selectDeptCodeExist(String deptCode);

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
    public int updateDelFlagById(@Param("deptId")Long deptId,@Param("deptType")String deptType);

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

    /*
    *根据parentId查询组织下级分/子公司的组织id
    * @param parentId 部门ID
    * @return 结果
    * */
    public List<Long> selectCompanyByParentId(@Param("parentId")Long parentId,@Param("deptType")String deptType);

    /*
    *根据组织Id和组织类别查询公司列表
    * @param parentId 部门ID deptType组织类型
    * @return 结果
    * */
    public EcmpOrgDto selectCompanyList(@Param("deptId")Long deptId,@Param("deptType")String deptType);

    /**
     * 查询下级组织数量
     * @param
     * @return
     */
    int selectCountByParentId(Integer parentId);

    /**
     * 按照分子公司名称或编号模糊
     * @param deptName  deptCode
     * @return 结果
     */
    public List<EcmpOrgDto> selectCompanyByDeptNameOrCode(String deptName,String deptCode);

}
