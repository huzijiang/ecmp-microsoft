package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hq.api.system.domain.SysDept;
import com.hq.api.system.mapper.SysDeptMapper;
import com.hq.api.system.mapper.SysUserMapper;
import com.hq.api.system.service.ISysDeptService;
import com.hq.core.security.LoginUser;
import com.hq.core.web.domain.TreeSelect;
import com.hq.ecmp.constant.OrgConstant;
import com.hq.ecmp.constant.RoleConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.vo.AddressVO;
import com.hq.ecmp.mscore.vo.UserAddressVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.util.DateFormatUtils;

import javax.annotation.Resource;

/**
 * 用户信息Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpUserServiceImpl implements IEcmpUserService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Resource
    private UserRegimeRelationInfoMapper userRegimeRelationInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;

    @Autowired
    private EcmpUserRoleMapper ecmpUserRoleMapper;

    @Autowired
    private EcmpRoleMapper ecmpRoleMapper;

    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;

    @Autowired
    private CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;

    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;

    @Autowired
    private IEcmpOrgService ecmpOrgService;
    @Autowired(required = false)
    private SysUserMapper userMapper;

    @Autowired(required = false)
    private SysDeptMapper deptMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;


    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户信息ID
     * @return 用户信息
     */
    @Override
    public EcmpUser selectEcmpUserById(Long userId) {
        return ecmpUserMapper.selectEcmpUserById(userId);
    }

    /**
     * 查询用户信息列表
     *
     * @param ecmpUser 用户信息
     * @return 用户信息
     */
    @Override
    public List<EcmpUser> selectEcmpUserList(EcmpUser ecmpUser) {
        return ecmpUserMapper.selectEcmpUserList(ecmpUser);
    }

    /**
     * 新增用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    @Override
    public int insertEcmpUser(EcmpUser ecmpUser) {
        ecmpUser.setCreateTime(DateUtils.getNowDate());
        return ecmpUserMapper.insertEcmpUser(ecmpUser);
    }

    /**
     * 修改用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateEcmpUser(EcmpUserVo ecmpUser) {
        String phonenumber=ecmpUser.getPhonenumber();
        ecmpUser.setUserName(phonenumber);
        //更新用户信息
        int updateEcmpUserNum = ecmpUserMapper.updateEcmpUser(ecmpUser);
        //更新用户角色关联信息  查询该用户对应的所有角色 删除 再全部插入
        int deleteEcmpUserRoleNum = ecmpUserRoleMapper.deleteEcmpUserRoleById(ecmpUser.getUserId());
        Long[]  roleIds = ecmpUser.getRoleIds();
        if(roleIds.length>0){
            for (Long roleId:roleIds) {
                EcmpUserRole ecmpUserRole =new  EcmpUserRole();
                ecmpUserRole.setUserId(ecmpUser.getUserId());
                ecmpUserRole.setRoleId(roleId);
                ecmpUserRoleMapper.insertEcmpUserRole(ecmpUserRole);
            }
        }
        //更新用户用车制度信息
        int deleteUserRegimeRelationInfoByIdNum = userRegimeRelationInfoMapper.deleteUserRegimeRelationInfoById(ecmpUser.getUserId());
        List<Long> regimenIds = ecmpUser.getRegimenId();
        if(regimenIds.size()>0){
            for (Long regimenId:regimenIds) {
                UserRegimeRelationInfo userRegimeRelationInfo =new  UserRegimeRelationInfo();
                userRegimeRelationInfo.setRegimenId(regimenId);
                userRegimeRelationInfo.setUserId(ecmpUser.getUserId());
                userRegimeRelationInfoMapper.insertUserRegimeRelationInfo(userRegimeRelationInfo);
            }
        }
        return updateEcmpUserNum;
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户信息ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserByIds(Long[] userIds) {
        return ecmpUserMapper.deleteEcmpUserByIds(userIds);
    }

    /**
     * 删除用户信息信息
     *
     * @param userId 用户信息ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserById(Long userId) {
        return ecmpUserMapper.deleteEcmpUserById(userId);
    }

    @Override
    public boolean isDispatcher(Long userId) {
        Integer count = ecmpUserMapper.queryDispatcher(userId);
        return count > 0;
    }

    /**
     * 可管理员工
     *
     * @return
     */
    @Override
    public int queryCompanyEmpCunt(Long companyId) {
      //  return ecmpUserMapper.queryCompanyEmp();

//        return userMapper.selectUserByRoleId(5L,null,companyId).size();
        //return ecmpUserMapper.selectUserByCompanyId(companyId).size();
        return ecmpUserMapper.selectCompanyUserCount(companyId);
    }


    /*
     * 获取上级组织id中的员工姓名和电话、邮箱
     *  @param  ecmpUserVo
     * @return List<EcmpUserDto>
     * */
    @Override

    public List<EcmpUserDto> getEcmpUserNameAndPhone(EcmpUserVo ecmpUserVo) {
        List<EcmpUserDto> ecmpUserList = null;

        //查询分/子公司下是否有部门
        int i = ecmpOrgMapper.selectCountByParentId(ecmpUserVo.getDeptId().intValue());
        if(i>0){
            ecmpUserList = ecmpUserMapper.getEcmpUserNameAndPhone(ecmpUserVo.getDeptId());
            //公司下有员工的数据
            int i1 = ecmpUserMapper.selectEcmpUserByDeptId(ecmpUserVo.getDeptId());
            if(i1>0){
              List<EcmpUserDto>  ecmpUserList1 = ecmpUserMapper.getCompanyEcmpUserNameAndPhone(ecmpUserVo.getDeptId());
              if(ecmpUserList.size()==0&&ecmpUserList1.size()==0){
                  return null;
              }else{
                  if(ecmpUserList.size()>0){
                      if(ecmpUserList1.size()>0){
                          for (EcmpUserDto ecmpUserDto:ecmpUserList1){
                              ecmpUserList.add(ecmpUserDto);
                          }
                          return ecmpUserList;
                      }
                      return ecmpUserList;
                  }else{
                      return ecmpUserList1;
                  }
              }
            }
        }else{
            int i1 = ecmpUserMapper.selectEcmpUserByDeptId(ecmpUserVo.getDeptId());
            if(i1>0){
                ecmpUserList = ecmpUserMapper.getCompanyEcmpUserNameAndPhone(ecmpUserVo.getDeptId());
            }
        }
        return ecmpUserList;
    }

    /**
     * 新增用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public String addEcmpUser(EcmpUserVo ecmpUser) {
        ecmpUser.setCreateTime(DateUtils.getNowDate());
        String phonenumber=ecmpUser.getPhonenumber();
        String email=ecmpUser.getEmail();
        int a = selectPhoneNumberExist(phonenumber);
        if(a>0){
            return "此手机号已存在请重新输入";
        }
        ecmpUser.setUserName(phonenumber);
        if(email!=null){
            int b = selectEmailExist(email);
            if(b>0){
                return "此邮箱已存在请重新输入";
            }
        }
        int count=ecmpUserMapper.addEcmpUser(ecmpUser);
        /*保存用车制度 （多个）*/
        Long userId=ecmpUser.getUserId();
        List<Long> regimenIds=ecmpUser.getRegimenId();
        try {
            bindUserRegimens(userId, regimenIds);
        } catch (Exception e) {
            logger.error("业务处理异常", e);
        }
        /*保存用户角色信息*/
        Long[] roleIds=ecmpUser.getRoleIds();
        for (int i = 0; i < roleIds.length; i++) {
            EcmpUserRole ecmpUserRole=new EcmpUserRole();
            ecmpUserRole.setUserId(userId);
            Long roleId=roleIds[i];
            ecmpUserRole.setRoleId(roleId);
            ecmpUserRoleMapper.insertEcmpUserRole(ecmpUserRole);
        }
        if(count != 1){
            return "添加失败";
        }
        return "成功添加"+count+"条数据";
    }


    /*
     *查询手机号是否已经存在
     * */
    @Override
    public int selectPhoneNumberExist(String phonenumber){
        return ecmpUserMapper.selectPhoneNumberExist(phonenumber);

    }

    /*
     *查询邮箱是否已经存在
     * */
    @Override
    public int selectEmailExist(String email){
        return ecmpUserMapper.selectEmailExist(email);
    }

    /**
     * 禁用/启用  员工
     *
     * @param userId 员工ID
     * @return 结果
     */
    @Transactional
    @Override
    public String updateUseStatus(String status,Long userId){
        //禁用/启用  员工
        int i1 = ecmpUserMapper.updateUseStatus(userId, status);
        String it_is_driver = ecmpUserMapper.selectEcmpUserIsDirver(userId);
        /*是否司机 0否 1是*/
        if("1".equals(it_is_driver)){
            driverInfoMapper.updateDriverUseStatus(userId,"NV00");
            /*清除用户token消息      delLoginUser(String token)*/
        }
        if("0".equals(status)){
            return "启用成功！";
        }
        return "禁用成功！";
    }

    /**
     * 逻辑删除员工信息
     *
     * @param userId 员工编号
     * @return 结果
     */
    @Transactional
    @Override
    public String  updateDelFlagById(Long userId) {
        /*
        管理员、部门主管、项目主管、车队主管、
        员工拥有审批权限和查看自己单据权限的
        无法【删除】该员工！；*/
        //根据roleId查询dataScope
        EcmpUserRole ecmpUserRole=new EcmpUserRole(userId);
        List<EcmpUserRole> ecmpUserRoleList = ecmpUserRoleMapper.selectEcmpUserRoleList(ecmpUserRole);
       /* System.out.println("-----ecmpUserRoleList-----"+ecmpUserRoleList.toString());
        System.out.println("-----size-----"+ecmpUserRoleList.size());*/
        if(!CollectionUtils.isEmpty(ecmpUserRoleList)){
            for(EcmpUserRole ecmpUserRole1 : ecmpUserRoleList) {
                EcmpRole ecmpRole = ecmpRoleMapper.selectEcmpRoleById(ecmpUserRole1.getRoleId());
                String roleKey = ecmpRole.getRoleKey();
                System.out.println("-----roleKey-----"+roleKey);
                if(RoleConstant.ADMIN.equals(roleKey)){
                    return "此员工为系统管理员，不可删除！";
                }
                if(RoleConstant.DEPT_MANAGER.equals(roleKey)){
                    return "此员工为部门主管，不可删除！";
                }
                if(RoleConstant.PROJECT_MANAGER.equals(roleKey)){
                    return "此员工为项目主管，不可删除！";
                }
            }
        }
        /*CarGroupDispatcherInfo carGroupDispatcherInfo=new CarGroupDispatcherInfo();
        carGroupDispatcherInfo.setUserId(userId);
        List<CarGroupDispatcherInfo> carGroupDispatcherInfos = carGroupDispatcherInfoMapper.selectCarGroupDispatcherInfoList(carGroupDispatcherInfo);
        Long carGroupId = carGroupDispatcherInfos.get(0).getCarGroupId();
        carGroupInfoMapper.selectCarGroupInfoById(carGroupId);*/
        int delFlag=ecmpUserMapper.updateDelFlagById(userId);
        if(delFlag==1){
            return "删除员工成功！";
        }else {
            return "删除员工数据失败！";
        }
    }

    /**
     * 查询员工列表
     *
     * @param deptId 部门ID
     * @return ecmpUserList
     */
    @Override
    public List<EcmpUserDto> getEcmpUserList(Long deptId){
        List<EcmpUserDto> ecmpUserList = new ArrayList<>();
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(deptId);
        String deptType = ecmpOrg.getDeptType();
        List<Long> userIds=ecmpUserMapper.getEcmpUserIdsByDeptId(deptId);
        EcmpUserDto ecmpUserDto=new EcmpUserDto();
        for (int i = 0; i < userIds.size(); i++) {
            if(OrgConstant.DEPT_TYPE_1.equals(deptType)){
                ecmpUserDto= ecmpUserMapper.getCompanyEcmpUserList(deptId, userIds.get(i));
                ecmpUserDto.setSubDept("无");
            }else{
                ecmpUserDto = ecmpUserMapper.getEcmpUserList(deptId, userIds.get(i));
            }
            String regimeName = "";
            List<RegimeVo> regimeVoList =  regimeInfoMapper.selectByUserId(userIds.get(i));
            if(regimeVoList.size()>0){
                for (RegimeVo regimeVo:regimeVoList) {
                    if(regimeVo!=null){
                        if(!StringUtils.isEmpty(regimeVo.getRegimeName())){
                            regimeName=regimeName+",";
                        }
                    }
                }
            }
            //String regimeName = StringUtils.join(regimeInfoList.toArray(), ",");
            ecmpUserDto.setRegimeName(regimeName);
            ecmpUserList.add(ecmpUserDto);
        }
        return ecmpUserList;
    }


    /**
     * 显示查询总条数
     * @param ecmpUser
     * @return
     */
    @Override
    public Integer queryUserListCount(EcmpUserVo ecmpUser){
        return ecmpUserMapper.queryUserListCount(ecmpUser.getDeptId());
    }


    /*只修改手机号
    @param  ecmpOrg
     * @return
    * 新手机号码：判断重复（该手机号已存在，不可重复录入！）判断未录入（手机号不可为空！）；
    再次输入：判断未录入（手机号码不可为空！）；判断前后输入一致（手机号码不一致）
    * */
    @Override
    @Transactional
    public String updatePhoneNum(String newPhoneNum,String reWritePhone,String userName,Long userId){
        String msg="";
        int i = ecmpUserMapper.selectPhoneNumberExist(newPhoneNum);
        if(i>0){
            msg="该手机号已存在，不可重复录入！";
        }else{
            int i1 = ecmpUserMapper.updatePhoneByUserId(newPhoneNum,userId);
            if(i1==1){
                msg="手机号码修改成功！";
            }else {
                msg="手机号码修改失败！";
            }
        }
        return msg;
    }
    /*员工详情
    @param  userId员工编号
    * @return
    * */
    @Override
    public EcmpUserDto selectEcmpUserDetail(Long userId,Long deptId){
        EcmpUser ecmpUser=ecmpUserMapper.selectEcmpUserById(userId);
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(ecmpUser.getDeptId());
        String deptType = ecmpOrg.getDeptType();
        EcmpUserDto ecmpUserDto=new EcmpUserDto();
        if(OrgConstant.DEPT_TYPE_1.equals(deptType)){
            ecmpUserDto = ecmpUserMapper.selectCompanyEcmpUserDetail(userId,deptId);
            ecmpUserDto.setSubDept("无");
        }
        if(OrgConstant.DEPT_TYPE_2.equals(deptType)){
            ecmpUserDto = ecmpUserMapper.selectEcmpUserDetail(userId,deptId);
        }
        List<EcmpRoleDto> roleList = ecmpUserMapper.selectRoleNameByEcmpUserId(userId);
        List<String> roleNameList= new ArrayList<>();
        if(roleList.size()>0){
            for (EcmpRoleDto ecmpRoleDto:roleList) {
                if(ecmpRoleDto!=null){
                    if(!StringUtils.isEmpty(ecmpRoleDto.getRoleName())){
                        roleNameList.add(ecmpRoleDto.getRoleName());
                    }
                }
            }
        }
        ecmpUserDto.setRoleName(roleNameList);
        //用户角色赋值
        ecmpUserDto.setRoleList(roleList);
        //用户用车制度赋值
        List<RegimeVo> regimeVoList = regimeInfoMapper.selectByUserId(userId);
        String regimeName = "";
        if(regimeVoList.size()>0){
            for (RegimeVo regimeVo:regimeVoList) {
                if(regimeVo!=null){
                    if(!StringUtils.isEmpty(regimeVo.getRegimeName())){
                        regimeName =regimeName+regimeVo.getRegimeName()+",";
                    }
                }
            }
        }
        ecmpUserDto.setRegimeName(regimeName);
        ecmpUserDto.setRegimeVoList(regimeVoList);
        //查询所属公司名称
        /*EcmpOrgDto subDetail = ecmpOrgMapper.getSubDetail(deptId);
        ecmpUserDto.setSubCompany(subDetail.getSupComName());
        ecmpUserDto.setSubDept(subDetail.getDeptName());*/
        return ecmpUserDto;
    }

    /*设置离职日期
    @param  dimissionTime
     * @return
    * */
    @Override
    public int updateDimissionTime(Date dimissionTime,Long userId){
        return ecmpUserMapper.updateDimissionTime(dimissionTime,userId);
    }

    /*已离职数量*/
    @Override
    public int selectDimissionCount(Long deptId){
        return ecmpUserMapper.selectDimissionCount(deptId);
    }

    /*已离职列表*/
    @Override
    public PageResult<EcmpUserDto> selectDimissionList(PageRequest pageRequest){
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Long> longs = new ArrayList<>();
        longs.add(pageRequest.getDeptId());
        List<EcmpUserDto> list=ecmpUserMapper.getEcmpUserPage(pageRequest.getSearch(),pageRequest.getDeptId(),CommonConstant.ONE,longs);
        Long ecmpUserPageCount = ecmpUserMapper.getEcmpUserPageCount(pageRequest.getSearch(), pageRequest.getDeptId(),CommonConstant.ONE);
        return new PageResult<>(ecmpUserPageCount,list);
    }

    /**
     * 给员工设置用车制度
     * @param userId
     * @param regimenIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindUserRegimens(Long userId, List<Long> regimenIds) throws Exception {
        UserRegimeRelationInfo userRegimeRelationInfo = null;
        for (Long regimenId : regimenIds) {
            userRegimeRelationInfo = new UserRegimeRelationInfo();
            userRegimeRelationInfo.setUserId(userId);
            userRegimeRelationInfo.setRegimenId(regimenId);
            int i = userRegimeRelationInfoMapper.insertUserRegimeRelationInfo(userRegimeRelationInfo);
            if(i != 1){
                throw new Exception();
            }
        }
    }

    @Override
    public List<EcmpUser> selectUserListByUserIds(String approveUserId) {
        return ecmpUserMapper.selectUserListByUserIds(approveUserId);
    }

    /**
     * 给员工修改用车制度
     * @param userId
     * @param regimenIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRegimens(Long userId, List<Long> regimenIds) throws Exception {
        UserRegimeRelationInfo userRegimeRelationInfo = null;
        for (Long regimenId : regimenIds) {
            userRegimeRelationInfo = new UserRegimeRelationInfo();
            userRegimeRelationInfo.setUserId(userId);
            userRegimeRelationInfo.setRegimenId(regimenId);
            int i = userRegimeRelationInfoMapper.updateUserRegimeRelationInfo(userRegimeRelationInfo);
            if(i != 1){
                throw new Exception();
            }
        }
    }

    /**
     * 日期禁用（离职日期）
     */
    @Override
    public void checkDimissionEcmpUser() {
        List<Long> userIds= ecmpUserMapper.checkDimissionEcmpUserIds(DateFormatUtils.formatDate(DateFormatUtils.DATE_FORMAT,new Date()));
        if (CollectionUtils.isNotEmpty(userIds)){
            for (int i = 0; i < userIds.size(); i++) {
                Long userId=userIds.get(i);
                int count = ecmpUserMapper.updateDimissionEcmpUser(userId);
                String it_is_driver = ecmpUserMapper.selectEcmpUserIsDirver(userId);
                if("0".equals(it_is_driver)){
                    driverInfoMapper.updateDriverUseStatus(userId,"NV00");
                    /*清除用户token消息      delLoginUser(String token)*/
                }
            }
        }
    }

    /**
     * 查询员工工号是否已存在
     *
     * @param jobNumber 员工工号
     * @return
     */
    @Override
    public int selectJobNumberExist(String jobNumber){
        return ecmpUserMapper.selectJobNumberExist(jobNumber);
    }


    /**
     * 按照姓名/工号/手机号模糊查询匹配的列表
     *
     * @param nameOrJobNumberOrPhone
     * @return 结果
     */
    @Override
    public List<EcmpUserDto> selectUserByNickNameOrJobNumber(String nameOrJobNumberOrPhone){
        List<EcmpUserDto> ecmpUserList=new ArrayList<>();
        List<EcmpUserDto> ecmpUserDtoList = ecmpUserMapper.selectUserIdsByNickNameOrJobNumber(nameOrJobNumberOrPhone, nameOrJobNumberOrPhone,nameOrJobNumberOrPhone);
        if(ecmpUserDtoList.size()>0){
            for (EcmpUserDto ecmpUserDto1: ecmpUserDtoList) {
                Long userId=ecmpUserDto1.getUserId();
                Long deptId=ecmpUserDto1.getDeptId();
                EcmpUserDto ecmpUserDto = ecmpUserMapper.selectUserByNickNameOrJobNumber(nameOrJobNumberOrPhone, nameOrJobNumberOrPhone,nameOrJobNumberOrPhone, userId,deptId);
                ecmpUserList.add(ecmpUserDto);
            }
        }
        return ecmpUserList;
    }
    /**
     * 查询所有有效员工
     * @param  //ecmpUser
     * @return*/
    @Override
    public EcmpUserDto selectEcmpUser(EcmpUserVo ecmpUser) {
        return ecmpUserMapper.selectEcmpUser(ecmpUser);
    }

    //员工分页
    @Override
    public PageResult<EcmpUserDto> getEcmpUserPage(PageRequest pageRequest, LoginUser loginUser) {
        List<SysDept> depts = new ArrayList<>();
        SysDept sysDept = deptMapper.selectDeptById(loginUser.getUser().getOwnerCompany());
        depts.add(sysDept);
        buildTreeDept(sysDept,depts);
        List<Long> longs = StringUtils.isBlank(pageRequest.getSearch())?new ArrayList<>():depts.stream().map(SysDept::getDeptId).collect(Collectors.toList());
        List<EcmpUserDto> list=new ArrayList<>();
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(pageRequest.getDeptId());
        String deptType = ecmpOrg.getDeptType();
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        if(OrgConstant.DEPT_TYPE_1.equals(deptType)){
            list=ecmpUserMapper.getCompanyEcmpUserPage(pageRequest.getSearch(),pageRequest.getDeptId(), CommonConstant.ZERO,longs);
            /*for (EcmpUserDto ecmpUserDto:list){
                ecmpUserDto.setSubDept("无");
            }*/
        }
        if(OrgConstant.DEPT_TYPE_2.equals(deptType)){
            list=ecmpUserMapper.getEcmpUserPage(pageRequest.getSearch(),pageRequest.getDeptId(), CommonConstant.ZERO,longs);
        }
        PageInfo<EcmpUserDto> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),info.getPages(),list);
    }
    void buildTreeDept(SysDept sysDept,List<SysDept> list){
        List<SysDept>  depts = deptMapper.selectChildrenDeptByParentId(sysDept.getDeptId());
        if(!org.springframework.util.CollectionUtils.isEmpty(depts)){
            depts.forEach(x->buildTreeDept(x,list));
        }
        list.addAll(depts);
    }

    /**
     * 员工邀请判断是否该手机号是否已经注册
     */
    @Override
    public int userItisExist(UserRegisterDTO userRegisterDTO) {

        return ecmpUserMapper.userItisExist(userRegisterDTO.getMobile());

    }

	@Override
	public List<EcmpUserDto> queryUserListByCompanyIdAndName(Long companyId, String name,String itIsDispatcher) {
		List<Long> deptIds = ecmpOrgService.queryDeptIdOfCompany(companyId);
		if(null !=ecmpUserMapper && deptIds.size()>0){
			List<EcmpUserDto> queryUserListByDeptIdsAndName = ecmpUserMapper.queryUserListByDeptIdsAndName(deptIds, name,itIsDispatcher);
			return queryUserListByDeptIdsAndName;
		}
		return null;
	}

	@Override
	public int updateEcmpUserjobNumber(EcmpUser ecmpUser) {
		return ecmpUserMapper.updateEcmpUserjobNumber(ecmpUser);
	}

    /**
     * 保存用户常用地址信息
     */
    @Override
    public void updateUserAddress(Long userId, AddressVO startAddress,AddressVO endAddress,List<AddressVO> multipleDropAddress) {
        Map<String,List<AddressVO>> addressInfo = Maps.newHashMap();
        logger.info("开始保存用户常用地址,userId:{},startAddress:{},endAddress:{}",userId,startAddress,endAddress);
        UserAddress userAddress = userAddressMapper.getByUserId(userId);
        if(userAddress!=null&&StringUtils.isNotBlank(userAddress.getAddressJson())){
            JSONObject jsonMap = JSON.parseObject(userAddress.getAddressJson());
            jsonMap.keySet().forEach(key->addressInfo.put(key,jsonMap.getJSONArray(key).toJavaList(AddressVO.class)));
        }

        //拼装开始地址信息
        if(startAddress!=null){
            String startCode = startAddress.getCityCode()+"_start";
            fillAddressInfo(addressInfo,startCode,startAddress);
        }

        if(endAddress!=null) {
            //拼装结束地址信息
            String endCode = endAddress.getCityCode() + "_end";
            fillAddressInfo(addressInfo, endCode, endAddress);
        }

        //拼装途径点
        if(multipleDropAddress!=null&&multipleDropAddress.size()>0){
            multipleDropAddress.forEach(addressVO -> {
                String endCode = addressVO.getCityCode() + "_end";
                fillAddressInfo(addressInfo, endCode, addressVO);
            });
        }

        if(userAddress==null){
            userAddress = UserAddress.builder().userId(userId).build();
            userAddress.setAddressJson(JSON.toJSONString(addressInfo));
            userAddress.setCreateBy(userId.toString());
            userAddressMapper.insertUserAddress(userAddress);
            logger.info("新增用户常用地址成功,userAddress:{}",userAddress);
        }else{
            userAddress.setAddressJson(JSON.toJSONString(addressInfo));
            userAddressMapper.updateUserAddress(userAddress);
            userAddress.setUpdateBy(userId.toString());
            logger.info("更新用户常用地址成功,userAddress:{}",userAddress);
        }
    }

    //拼装地址数据
    private void fillAddressInfo(Map<String,List<AddressVO>> addressInfo,String code,AddressVO addressVO){
        if(addressVO==null){
            return;
        }
        List<AddressVO> addressList = addressInfo.get(code);
        if(addressList==null){
            addressList = Lists.newArrayList(addressVO);
            addressInfo.put(code,addressList);
        }else{
            Long count = addressList.stream().filter(address->address.getLongAddress().equals(addressVO.getLongAddress())).count();
            if(count==0){
                addressList.add(0,addressVO);
                addressList = addressList.size()>10?addressList.subList(0,10):addressList;
                addressInfo.put(code,addressList);
            }else{
                AddressVO addressvo = addressList.stream().filter(address->address.getLongAddress().equals(addressVO.getLongAddress())).findFirst().get();
                addressList.remove(addressvo);
                addressList.add(0,addressVO);
            }
        }
    }

    /**
     * 获取用户常用地址信息
     */
    @Override
    public UserAddressVO getUserAddress(Long userId) {
        UserAddress userAddress = userAddressMapper.getByUserId(userId);
        UserAddressVO userAddressVO = new UserAddressVO();
        if(userAddress==null){
            userAddressVO.setAddressInfo(Maps.newHashMap());
            return userAddressVO;
        }
        Map<String,List<AddressVO>> addressMap = Maps.newHashMap();
        JSONObject jsonMap = JSON.parseObject(userAddress.getAddressJson());
        jsonMap.keySet().forEach(key->addressMap.put(key,jsonMap.getJSONArray(key).toJavaList(AddressVO.class)));
        userAddressVO.setAddressInfo(addressMap);
        return userAddressVO;
    }
}