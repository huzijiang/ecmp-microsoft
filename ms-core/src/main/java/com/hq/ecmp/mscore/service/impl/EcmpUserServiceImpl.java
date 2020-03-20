package com.hq.ecmp.mscore.service.impl;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import com.hq.ecmp.util.DateFormatUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
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
    public int updateEcmpUser(EcmpUserVo ecmpUser) {
        ecmpUser.setUpdateTime(DateUtils.getNowDate());
        /*修改用车制度 （多个）*/
        Long userId=ecmpUser.getUserId();
        List<Long> regimenIds=ecmpUser.getRegimenIds();
        try {
            updateUserRegimens(userId,regimenIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*修改用户角色信息*/
        Long[] roleIds=ecmpUser.getRoleIds();
        for (int i = 0; i < roleIds.length; i++) {
            EcmpUserRole ecmpUserRole=new EcmpUserRole();
            ecmpUserRole.setUserId(userId);
            Long roleId=roleIds[i];
            ecmpUserRole.setRoleId(roleId);
            ecmpUserRoleMapper.updateEcmpUserRole(ecmpUserRole);
        }
        ecmpUserMapper.updateUserRegimeRelation(ecmpUser);
        return ecmpUserMapper.updateEcmpUser(ecmpUser);
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
     * 获取上级组织id中的员工姓名和电话
     *  @param  ecmpUserVo
     * @return List<EcmpUserDto>
     * */
    @Override

    public List<EcmpUserDto> getEcmpUserNameAndPhone(EcmpUserVo ecmpUserVo) {
        List<EcmpUserDto> ecmpUserList = null;
        ecmpUserList = ecmpUserMapper.getEcmpUserNameAndPhone(ecmpUserVo);
        return ecmpUserList;
    }

    /**
     * 新增用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    @Override
    public int addEcmpUser(EcmpUserVo ecmpUser) {
        ecmpUser.setCreateTime(DateUtils.getNowDate());
        /*保存用车制度 （多个）*/
        Long userId=ecmpUser.getUserId();
        List<Long> regimenIds=ecmpUser.getRegimenIds();
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
        return ecmpUserMapper.addEcmpUser(ecmpUser);
    }


    /*
     *查询手机号是否已经存在
     * */
    public int selectPhoneNumberExist(String phonenumber){
        return ecmpUserMapper.selectPhoneNumberExist(phonenumber);
    }

    /*
     *查询邮箱是否已经存在
     * */
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
    public String updateUseStatus(String status,Long userId){
        //禁用/启用  员工
        int i1 = ecmpUserMapper.updateUseStatus(userId, status);
        String it_is_driver = ecmpUserMapper.selectEcmpUserIsDirver(userId);
        if("0".equals(it_is_driver)){
            driverInfoMapper.updateDriverUseStatus(userId,"0".equals(status)?"V000":"NV00");
            /*清除用户token消息*/
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
            List<String>  regimeInfoList =  regimeInfoMapper.selectByUserId(userIds.get(i));
            String regimeName = StringUtils.join(regimeInfoList.toArray(), ",");
            ecmpUserDto.setRegimeName(regimeName);
            ecmpUserList.add(ecmpUserDto);
        }
        return ecmpUserList;
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
    public EcmpUserDto selectEcmpUserDetail(Long userId){
        List<String> roleNameList = ecmpUserMapper.selectRoleNameByEcmpUserId(userId);
        String roleName = StringUtils.join(roleNameList.toArray(), ",");
        return ecmpUserMapper.selectEcmpUserDetail(userId,roleName);
    }

    /*设置离职日期
    @param  dimissionTime
     * @return
    * */
    public int updateDimissionTime(Date dimissionTime,Long userId){
        return ecmpUserMapper.updateDimissionTime(dimissionTime,userId);
    }

    /*已离职数量*/
    @ApiOperation(value = "已离职数量",notes = "已离职数量",httpMethod ="POST")
    @PostMapping("/selectDimissionCount")
    public int selectDimissionCount(){
        return ecmpUserMapper.selectDimissionCount();
    }

    /*已离职列表*/
    @ApiOperation(value = "已离职列表",notes = "已离职列表",httpMethod ="POST")
    @PostMapping("/selectDimissionList")
    public List<EcmpUserDto> selectDimissionList(Long deptId){
        List<EcmpUserDto> ecmpUserList=null;
        List<Long> userIds= ecmpUserMapper.selectDimissionEcmpUserIds();
        for (int i = 0; i < userIds.size(); i++) {
            EcmpUserDto ecmpUserDto = ecmpUserMapper.selectDimissionList(deptId,userIds.get(i));
            ecmpUserList.add(ecmpUserDto) ;
        }
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
     * 员工邀请判断是否该手机号是否已经注册
     */
    public int userItisExist(String phoneNumber) {

        return ecmpUserMapper.userItisExist(phoneNumber);

    }



}