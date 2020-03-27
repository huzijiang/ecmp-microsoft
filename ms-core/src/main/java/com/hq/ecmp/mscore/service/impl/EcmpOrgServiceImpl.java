package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.OrgConstant;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.vo.EcmpOrgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpOrgServiceImpl implements IEcmpOrgService {
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private CarInfoMapper carInfoMapper;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;//车队
    @Autowired
    private EcmpRoleDeptMapper ecmpRoleDeptMapper;
    @Autowired
    private EcmpUserRoleMapper ecmpUserRoleMapper;
    /**
     * 显示公司组织结构
     *
     * @param deptId 部门ID deptType组织类型 1公司 2部门
     * @return deptList 部门列表
     */
    @Override
    public List<EcmpOrgDto> selectCombinationOfCompany(Long deptId,String deptType){

       /* int bl = 0;
        if(deptType==null){
            deptType = "1";
            bl = 1;
        }*/
        List<EcmpOrgDto> ecmpOrgList = new ArrayList<>();
        if(deptId==null){
            Long parentId = 0L;
            //默认查询所有公司列表
            ecmpOrgList = ecmpOrgMapper.selectByEcmpOrgParentId(deptId,parentId,null);
        }else {
            ecmpOrgList = ecmpOrgMapper.selectByEcmpOrgParentId(deptId,null,null);
        }
        if(ecmpOrgList.size()>0){
            for (EcmpOrgDto company:ecmpOrgList) {
                List<EcmpOrgDto> ecmpOrgs = loadEcmpOrg(null,company.getDeptId(), deptType);
                company.setDeptList(ecmpOrgs);
            }
        }
        return ecmpOrgList;
    }

    public List<EcmpOrgDto> loadEcmpOrg(Long deptId,Long parentId,String deptType) {
        List<EcmpOrgDto> list = new ArrayList<>();
        List<EcmpOrgDto> deptList = ecmpOrgMapper.selectByEcmpOrgParentId(null,parentId,deptType);
        if(deptList.size()>0){
            for (EcmpOrgDto ecmpOrg:deptList) {
                list.add(ecmpOrg);
                ecmpOrg.setDeptList(loadEcmpOrg(null,ecmpOrg.getDeptId(),deptType));
            }
        }
        return list;
    }

    /**
     * 显示公司列表
     *
     * @param deptId 部门ID
     * @return ecmpOrg
     */
    @Override
    public List<EcmpOrgDto> selectCompanyList(Long deptId,String deptType){
        /*列表：（分/子公司名称、编号、分/子公司主管、分/子公司人数、上级公司、下属公司数、状态*/
        List<EcmpOrgDto> companyList = new ArrayList<>();
        List<Long> deptIdList = new ArrayList<>();
        if(deptId!=null){
            deptIdList = ecmpOrgMapper.selectCompanyByParentId(deptId, OrgConstant.DEPT_TYPE_1);
            EcmpOrgDto supDto=ecmpOrgMapper.getSubDetail(deptId);
            String supComName=supDto.getDeptName();
            if(deptIdList.size()>0){
                for (Long deptId1:deptIdList) {
                    EcmpOrgDto ecmpOrgDto=ecmpOrgMapper.selectCompanyList(deptId1,OrgConstant.DEPT_TYPE_1);
                    ecmpOrgDto.setSupComName(supComName);
                    companyList.add(ecmpOrgDto);
                }
            }
        }
        return companyList;
    }

    /**
     * 查询部门详情
     *
     * @param deptId 部门ID
     * @return ecmpOrg
     */
    @Override
    public EcmpOrgDto getDeptDetails(Long deptId){
        return ecmpOrgMapper.selectByDeptId(deptId);
    }

    /**
     * 查询分/子公司、部门编号是否已存在
     *
     * @param deptCode 分/子公司、部门编号
     * @return ecmpOrg
     */
    @Override
    public int selectDeptCodeExist(String deptCode){
            return ecmpOrgMapper.selectDeptCodeExist(deptCode);
    }


    /*
     * 添加部门
     *  @param  ecmpOrg
     * @return int
     * */
    @Override
    @Transactional
    public int addDept(EcmpOrgVo ecmpOrg){
        ecmpOrg.setCreateTime(DateUtils.getNowDate());
        int iz = ecmpOrgMapper.addDept(ecmpOrg);
        /*EcmpRoleDept ecmpRoleDept=new EcmpRoleDept();
        ecmpRoleDept.setDeptId(ecmpOrg.getDeptId());
        ecmpRoleDept.setRoleId(2L);
        //添加部门角色关联信息
        ecmpRoleDeptMapper.insertEcmpRoleDept(ecmpRoleDept);
        //添加角色用户关联信息
        EcmpUserRole ecmpUserRole =new  EcmpUserRole();
        ecmpUserRole.setUserId(ecmpOrg.getUserId());
        ecmpUserRole.setRoleId(2L);
        ecmpUserRoleMapper.insertEcmpUserRole(ecmpUserRole);*/
        if(iz==1){
            return 1;
        }
        return 0;
    }

    /*
     * 查询上级部门下的所有员工
     *  @param  ecmpOrg
     * @return int
     * */
    @Override
    public List<EcmpUserDto> selectUserByDeptId(EcmpOrgVo ecmpOrg){
        List<EcmpUserDto> ecmpUserList = ecmpUserMapper.selectUserByDeptId(ecmpOrg.getDeptId());
        if(ecmpUserList.size()>0){
            return ecmpUserList;
        }
        return null;
    }

    /*
     * 修改部门
     *  @param  ecmpOrg
     * @return int
     * */
    @Transactional
    @Override
    public int updateDept(EcmpOrgVo ecmpOrg){
        ecmpOrg.setUpdateTime(DateUtils.getNowDate());
        //添加部门
        int ix = ecmpOrgMapper.updateDept(ecmpOrg);
        return ix;
    }

    /**
     * 查询部门
     *
     * @param deptId 部门ID
     * @rrn 部门
     */
    @Override
    public EcmpOrg selectEcmpOrgById(Long deptId) {
        return ecmpOrgMapper.selectEcmpOrgById(deptId);
    }

    /**
     * 根据公司id查询部门对象列表
     *
     * @param deptId
     * @return
     */
    @Override
    public List<EcmpOrg> selectEcmpOrgsByDeptId(Long deptId) {
        return null;
    }

    /**
     * 查询部门列表
     *
     * @param ecmpOrg 部门
     * @return 部门
     */
    @Override
    public List<EcmpOrg> selectEcmpOrgList(EcmpOrg ecmpOrg) {
        return ecmpOrgMapper.selectEcmpOrgList(ecmpOrg);
    }


    /**
     * 修改部门
     *
     * @param ecmpOrg 部门
     * @return 结果
     */
    @Override
    @Transactional
    public int updateEcmpOrg(EcmpOrgVo ecmpOrg) {
        ecmpOrg.setUpdateTime(DateUtils.getNowDate());

        return ecmpOrgMapper.updateEcmpOrg(ecmpOrg);
    }

    /**
     * 批量删除部门
     *
     * @param deptIds 需要删除的部门ID
     * @return 结果
     */
    @Override
    public int deleteEcmpOrgByIds(Long[] deptIds) {
        return ecmpOrgMapper.deleteEcmpOrgByIds(deptIds);
    }

    /**
     * 删除部门信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteEcmpOrgById(Long deptId) {
        return ecmpOrgMapper.deleteEcmpOrgById(deptId);
    }

    /**
     * (根据部门名称模糊)查询用户 所在（子）公司的 部门列表
     * @param userId
     * @param name
     * @return
     */
    @Override
    public List<EcmpOrg> selectUserOwnCompanyDept(Long userId, String name) {
        ///根据userId查询用户信息
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(userId);
        //根据用户的部门id查询用户部门对象
        EcmpOrg userEcmpOrg = ecmpOrgMapper.selectEcmpOrgById(ecmpUser.getDeptId());
        //根据公司id（以及部门名称模糊）查询部门对象列表
        EcmpOrg ecmpOrg = new EcmpOrg();
        ecmpOrg.setDeptName(name);
        //1
       List<EcmpOrg> ecmpOrgs = ecmpOrgMapper.selectEcmpOrgList(ecmpOrg);
        return ecmpOrgs;
    }

    /**
     *查询子公司列表
     * @return
     */
    @Override
    public String[] selectSubCompany(Long deptId) {
        String[] subCompany = ecmpOrgMapper.selectSubCompany(deptId);
        return subCompany;
    }

    /**
     *查询分/子公司详情
     * @return
     */
    @Override
    public EcmpOrgDto getSubDetail(Long deptId) {
        return ecmpOrgMapper.getSubDetail(deptId);
    }

    /**
     * 逻辑删除分子公司/部门信息
     *
     * @param deptType, deptId
     * @return 结果
     */
    @Transactional
    @Override
    public String updateDelFlagById(String deptType,Long deptId) {
        //根据deptId查询组织下级是否有数据信息 ecmpOrgNum>0不可删除
        int ecmpOrgNum = ecmpOrgMapper.selectByAncestorsLikeDeptId(deptId);

        //查询该组织下的员工信息 如果ecmpUserNum>0不可删除
        int ecmpUserNum = ecmpUserMapper.selectEcmpUserByDeptId(deptId);

        //查询该组织下的驾驶员信息 如果该公司没有员工，就没有驾驶员
        int driverNum =driverInfoMapper.selectDriverCountByDeptId(deptId);

        //查询该组织下的车辆信息  如果carNum>0不可删除
        int carNum=carInfoMapper.selectCarCountByDeptId(deptId);

        //查询该组织下的车队信息  如果该组织没有车也就没有车队
        int carGroupNum = carGroupInfoMapper.selectCountByOrgdeptId(deptId);

        //如果满足上述条件，执行删除操作
        if(ecmpOrgNum==0&&ecmpUserNum==0&&carNum==0){
            int delFlag = ecmpOrgMapper.updateDelFlagById(deptId,deptType);
            if(deptType.equals("1")){
                if(delFlag==1){
                    return "删除分/子公司数据成功！";
                }else {
                    return "删除分/子公司数据失败！";
                }
            }else{
                if(delFlag==1){
                    return "删除部门数据成功！";
                }else {
                    return "删除部门数据失败！";
                }
            }
        }
        return "不可删除！";
    }

    /**
     * 逻辑批量删除部门信息
     *
     * @param deptIds 部门ID
     * @return 结果
     */
    @Transactional
    @Override
    public int updateDelFlagByIds(Long[] deptIds) {
        return ecmpOrgMapper.updateDelFlagByIds(deptIds);
    }

    /**
     * 禁用/启用  分/子公司或部门
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Transactional
    @Override
    public String updateUseStatus(String status,Long deptId){
        //禁用/启用  分/子公司
        int i = ecmpOrgMapper.updateUseStatus(status,deptId.toString(),deptId);
        //禁用/启用  员工
        int i1 = ecmpUserMapper.updateRelationUseStatus(deptId, status);
        //禁用/启用  驾驶员  state 状态 W001 待审   V000 生效中   NV00 失效
        int i2 = driverInfoMapper.updateUseStatus(deptId, "0".equals(status)?"V000":"NV00");
        if("0".equals(status)){
            return "启用成功！";
        }
        return "禁用成功！";
    }

    /**
     * 按照分子公司名称或编号模糊
     * @param deptNameOrCode
     * @return 结果
     */
    @Override
    public List<EcmpOrgDto> selectCompanyByDeptNameOrCode(String deptNameOrCode){
        return ecmpOrgMapper.selectCompanyByDeptNameOrCode(deptNameOrCode,deptNameOrCode);
    }

}
