package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.OrgConstant;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.EcmpUserRole;
import com.hq.ecmp.mscore.domain.RegimeVo;
import com.hq.ecmp.mscore.domain.UserRegimeRelationInfo;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.EcmpRoleDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.EcmpOrgVo;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import com.hq.ecmp.util.DateFormatUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户信息Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpUserServiceImpl implements IEcmpUserService {
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private UserRegimeRelationInfoMapper userRegimeRelationInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;

    @Autowired
    private EcmpUserRoleMapper ecmpUserRoleMapper;

    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;

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
    public int queryCompanyEmpCunt() {
        return ecmpUserMapper.queryCompanyEmp();
    }

    /*
     * 获取上级组织id中的员工姓名和电话、邮箱
     *  @param  ecmpUserVo
     * @return List<EcmpUserDto>
     * */
    @Override

    public List<EcmpUserDto> getEcmpUserNameAndPhone(EcmpUserVo ecmpUserVo) {
        List<EcmpUserDto> ecmpUserList = null;
        ecmpUserList = ecmpUserMapper.getEcmpUserNameAndPhone(ecmpUserVo);
       /* for (EcmpUserDto ecmpUserDto:ecmpUserList){

        }*/
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
        int b = selectEmailExist(email);
        if(b>0){
            return "此邮箱已存在请重新输入";
        }
        int count=ecmpUserMapper.addEcmpUser(ecmpUser);
        /*保存用车制度 （多个）*/
        Long userId=ecmpUser.getUserId();
        List<Long> regimenIds=ecmpUser.getRegimenId();
        try {
            bindUserRegimens(userId, regimenIds);
        } catch (Exception e) {
            e.printStackTrace();
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
        if("0".equals(it_is_driver)){
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
    public int updateDelFlagById(Long userId) {
        /*若该员工在业务表中有数据，如是某个审批流的审批人等，则弹出提示不可删除操作
        无法【删除】该员工！；*/

        return ecmpUserMapper.updateDelFlagById(userId);
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
        List<Long> userIds=ecmpUserMapper.getEcmpUserIdsByDeptId(deptId);
        for (int i = 0; i < userIds.size(); i++) {
            EcmpUserDto ecmpUserDto = ecmpUserMapper.getEcmpUserList(deptId, userIds.get(i));
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
    public String updatePhoneNum(String newPhoneNum,String reWritePhone,Long userId){
        String msg="";
        int i = ecmpUserMapper.selectPhoneNumberExist(newPhoneNum);
        if(i>0){
            msg="该手机号已存在，不可重复录入！";
        }else{
            int i1 = ecmpUserMapper.updatePhoneNum(newPhoneNum,reWritePhone,userId);
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
    public EcmpUserDto selectEcmpUserDetail(Long userId){
        EcmpUserDto ecmpUserDto = ecmpUserMapper.selectEcmpUserDetail(userId);
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
        Long deptId=ecmpUserDto.getDeptId();
        EcmpOrgDto subDetail = ecmpOrgMapper.getSubDetail(deptId);
        ecmpUserDto.setSubCompany(subDetail.getSupComName());
        ecmpUserDto.setSubDept(subDetail.getDeptName());
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
    public int selectDimissionCount(){
        return ecmpUserMapper.selectDimissionCount();
    }

    /*已离职列表*/
    @Override
    public List<EcmpUserDto> selectDimissionList(Long deptId){
        List<EcmpUserDto> ecmpUserList = ecmpUserMapper.selectDimissionList(deptId);
        return ecmpUserList;
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
     * 员工邀请判断是否该手机号是否已经注册
     */
    @Override
    public int userItisExist(String phoneNumber) {

        return ecmpUserMapper.userItisExist(phoneNumber);

    }
}