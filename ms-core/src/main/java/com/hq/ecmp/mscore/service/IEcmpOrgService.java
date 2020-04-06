package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.vo.*;

import java.util.List;

/**
 * 部门Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpOrgService
{

    /**
     * 显示公司组织结构
     *
     * @param deptId 部门ID
     * @return
     */
    public List<EcmpOrgDto> selectCombinationOfCompany(Long deptId,String deptType);
    /**
     * 显示当前登陆用户所属公司与公司下的部门
     *
     * @param deptId 部门ID
     * @return
     */
    public List<EcmpOrgDto> selectDeptComTree(Long deptId,String deptType);

    /**
     * 显示公司列表
     *
     * @param deptId 部门ID
     * @return
     */
    public List<EcmpOrgDto> selectCompanyList(Long deptId,String deptType);

    /**
     * 显示查询总条数
     * @param ecmpOrg
     * @return
     */
    public Integer queryCompanyListCount(EcmpOrgVo ecmpOrg);

    /**
     * 显示部门列表
     *
     * @param deptId 部门ID
     * @return
     */
    public List<EcmpOrgDto> selectDeptList(Long deptId,String deptType);
    /**
     * 查询部门详情
     *
     * @param deptId 部门ID
     * @return ecmpOrg
     */
    public EcmpOrgDto getDeptDetails(Long deptId);

    /**
     * 查询分/子公司、部门编号是否已存在
     *
     * @param deptCode 分/子公司、部门编号
     * @return ecmpOrg
     */
    public int selectDeptCodeExist(String deptCode);
    /*
    * 添加部门
    *  @param  ecmpOrg
    * @return int
    * */
    public int addDept(EcmpOrgVo ecmpOrg);

    /*
     * 修改部门
     *  @param  ecmpOrg
     * @return int
     * */
    public int updateDept(EcmpOrgVo ecmpOrg);

    /**
     * 查询部门
     *
     * @param deptId 部门ID
     * @return 部门
     */
    public EcmpOrg selectEcmpOrgById(Long deptId);

    /**
     *  根据公司id查询部门对象列表
     * @param deptId
     * @return
     */
    List<EcmpOrg> selectEcmpOrgsByDeptId(Long deptId);

    /**
     * 查询部门列表
     *
     * @param ecmpOrg 部门
     * @return 部门集合
     */
    public List<EcmpOrg> selectEcmpOrgList(EcmpOrg ecmpOrg);

    /**
     * 修改部门
     *
     * @param ecmpOrg 部门
     * @return 结果
     */
    public int updateEcmpOrg(EcmpOrgVo ecmpOrg);

    /**
     * 批量删除部门
     *
     * @param deptIds 需要删除的部门ID
     * @return 结果
     */
    public int deleteEcmpOrgByIds(Long[] deptIds);

    /**
     * 删除部门信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteEcmpOrgById(Long deptId);


    /**
     * (根据部门名称模糊)查询用户 所在（子）公司的 部门列表
     * @param userId
     * @param name
     * @return
     */
    List<EcmpOrg> selectUserOwnCompanyDept(Long userId, String name);


    /**
     *查询子公司列表
     * @return
     */
    String[] selectSubCompany(Long deptId);


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
    public String updateDelFlagById(String deptType,Long deptId);

    /**
     * 逻辑批量删除部门信息
     *
     * @param deptIds 部门ID
     * @return 结果
     */
    public int updateDelFlagByIds(Long[] deptIds);


    /**
     * 禁用启用部门
     *
     * @param userId 部门ID
     * @return 结果
     */
    public String updateUseStatus(String status,Long userId);

    /**
     * 按照分子公司名称或编号模糊查询匹配的列表
     *
     * @param deptNameOrCode
     * @return 结果
     */
    public List<EcmpOrgDto> selectCompanyByDeptNameOrCode(String deptNameOrCode);

    /**
     * 当前登录机构的名称；分/子公司编号；分/子公司主管；分/子公司人数
     *
     * @param deptId 部门ID
     * @return
     */
    public EcmpOrgDto selectCurrentDeptInformation(Long deptId);

    /**
     * 按照部门名称或编号模糊查询匹配的列表
     *
     * @param deptNameOrCode
     * @return 结果
     */
    public List<EcmpOrgDto> selectDeptByDeptNameOrCode(String deptNameOrCode);

    /**
     *查询分/子公司下的部门名称和deptId
     * @return
     */
    public List<EcmpOrgDto> selectDeptByCompany(Long deptId);

    /**
     * 部门树
     * @param deptId
     * @param deptName
     * @return
     */
    OrgTreeVo selectDeptTree(Long deptId,String deptName);

    /**
     * 员工树
     * @param deptId
     * @param deptName
     * @return
     */
    OrgTreeVo selectDeptUserTree(Long deptId, String deptName);




    //公司车队树
    List<CompanyCarGroupTreeVO> selectCompanyCarGroupTree(Long deptId);

    /*公司树*/
    public List<CompanyTreeVO> getCompanyTree(Long deptId);

    /*查询公司车队总人数*/
    CarGroupCountVO selectCarGroupCount(Long deptId);
}
