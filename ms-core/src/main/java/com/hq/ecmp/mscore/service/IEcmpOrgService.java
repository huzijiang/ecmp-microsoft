package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.vo.EcmpOrgVo;
import com.hq.ecmp.mscore.vo.EcmpUserVo;

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
     * 查询部门列表
     *
     * @param deptId 部门ID
     * @return deptList
     */
    public List<EcmpOrgDto> getDeptList(Long deptId,String deptType);
    /**
     * 查询部门详情
     *
     * @param deptId 部门ID
     * @return ecmpOrg
     */
    public EcmpOrgDto getDeptDetails(Long deptId);
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
     * 部门编号验证
     * @param  companyId
     * @return companyIdNum
     * */
    public int  getCheckingDepcCompanyId(Long companyId);


    /**
     * 查询部门
     *
     * @param deptId 部门ID
     * @return 部门
     */
    public EcmpOrg selectEcmpOrgById(Long deptId);

    /**
     *  根据公司id查询部门对象列表
     * @param companyId
     * @return
     */
    List<EcmpOrg> selectEcmpOrgsByCompanyId(Long companyId);

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
    public String updateDelFlagById(Long deptId,String deptType);

    /**
     * 逻辑批量删除部门信息
     *
     * @param deptIds 部门ID
     * @return 结果
     */
    public int updateDelFlagByIds(Long[] deptIds);


    /**
     * 禁用启用部门/ 分/子公司
     *
     * @param deptId 部门ID
     * @return 结果
     */
    public String updateUseStatus(String status,Long deptId);

}
