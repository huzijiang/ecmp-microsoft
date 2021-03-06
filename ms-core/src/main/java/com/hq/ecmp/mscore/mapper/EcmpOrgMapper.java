package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    public List<EcmpOrgDto> selectByEcmpOrgParentId(@Param("parentId") Long parentId,@Param("deptType")Long deptType);

    /**
     * 查询所有公司一级
     *l
     * @param ownerCompany 顶级公司ID
     * @return 公司
     */
    public List<EcmpOrgDto> selectByEcmpOrgOwnerCompanyId(@Param("companyId") Long companyId);
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
    public int selectDeptCodeExist(@Param("deptCode")String deptCode);

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
     * 查询部门所属的公司组织
     *
     * @param deptId 部门ID
     * @return 部门
     */
    public EcmpOrgDto getSubComDept(Long deptId);


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
     * 显示查询总条数
     * @param
     * @return
     */
    public Integer queryCompanyListCount(@Param("parentId")Long parentId,@Param("deptType")String deptType);

    /*
    *根据组织Id和组织类别查询部门列表
    * @param parentId 部门ID deptType组织类型
    * @return 结果
    * */
    public EcmpOrgDto selectDeptList(@Param("deptId")Long deptId,@Param("deptType")String deptType);

    /**
     * 查询下级组织数量
     * @param
     * @return
     */
    int selectCountByParentId(Integer parentId);

    /**
     * 按照分子公司名称或编号模糊查询匹配的组织id
     * @param deptName  deptCode
     * @return 结果
     */
    public List<Long> selectDeptIdsByDeptNameOrCode(@Param("deptName")String deptName,@Param("deptCode")String deptCode,@Param("deptType")String deptId);

    /**
     * 按照分子公司名称或编号模糊
     * @param deptName  deptCode
     * @return 结果
     */
    public EcmpOrgDto selectCompanyByDeptNameOrCode(@Param("deptName")String deptName,@Param("deptCode")String deptCode,@Param("deptId")Long deptId);


    /**
     * 按照部门名称或编号模糊查询匹配的列表
     * @param deptName  deptCode
     * @return 结果
     */
    public EcmpOrgDto selectDeptByDeptNameOrCode(@Param("deptName")String deptName,@Param("deptCode")String deptCode,@Param("deptId")Long deptId);

    /**
     * 查询分/子公司下的部门名称和deptId
     * @param   deptId
     * @return 结果
     */
    public List<EcmpOrgDto> selectDeptByCompany(@Param("deptId")Long deptId);

    /**
     * 查询当前机构信息
     * @param deptId
     * @return 结果
     */
    public EcmpOrgDto selectCurrentDeptInformation(@Param("deptId")Long deptId,@Param("ancestors") String ancestors, @Param("ownerCompany") Long ownerCompany);

    /**
     * 部门树
     * @param deptId
     * @return
     */
    OrgTreeVo selectDeptTree(@Param("deptId") Long deptId,@Param("deptName") String deptName);

    List<CarGroupTreeVO> selectCarGroupTree(Long deptId);

    //查询公司树
    List<CompanyTreeVO> selectCompanyTree(Long deptId);


    /**
     * 公司车队树
     * @param deptId
     * @return
     */
    List<CompanyCarGroupTreeVO> selectCompanyCarGroupTree(@Param("deptId")Long deptId);

    List<UserVO> selectUserByLeader(@Param("leader") String leader);

    public int isRepart(@Param("name")String name,@Param("type")int type,@Param("id")Long id,@Param("pId")Long pId);

    /**
     * 公司车队树升级版 查询
     * @param deptId
     * @param parentId
     * @return
     */
    List<CarGroupTreeVO> selectNewCompanyCarGroupTree(@Param("deptId")Long deptId,@Param("parentId") Long parentId);

    List<EcmpOrg> selectCompanyDeptList(EcmpOrg ecmpOrg);

    List<Map> selectOrgTreeByDeptId(@Param("orgIds")String orgIds);

    /**
     * 获取公司下所有的存在有效用车申请的用车单位信息
     * @param companyId
     * @return
     */
    List<EcmpOrg> getUseCarOrgList(@Param("companyId")Long companyId);

    /** 查询所有公司名称 - id */
    List<Map> selectIdAndName(@Param("userId") Long userId);

    /**
     * 获取当前调度员所在的车队服务过的所有机构ids
     * @param userId
     * @return
     */
    List<Integer> selectServiceOrgIds(@Param("userId") Long userId);

    /**
     * 获取当前调度员所在的车队的id
     * @param userId
     * @return
     */
    Integer selectCarGroupIdOfDispatcher(@Param("userId") Long userId);

    List<Map> getEcmpNameAll();
}
