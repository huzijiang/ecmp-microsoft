package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
		return count>0;
	}
    /**
     * 可管理员工
     * @return
     */
    @Override
    public int  queryCompanyEmpCunt(){
        return ecmpUserMapper.queryCompanyEmp();
    }

    /*
     * 获取上级组织id中的员工姓名和电话
     *  @param  ecmpUserVo
     * @return List<EcmpUserDto>
     * */
    @Override
    public List<EcmpUserDto> getEcmpUserNameAndPhone(EcmpUserVo ecmpUserVo){
        List<EcmpUserDto> ecmpUserList=null;
        ecmpUserList=ecmpUserMapper.getEcmpUserNameAndPhone(ecmpUserVo);
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
        return ecmpUserMapper.addEcmpUser(ecmpUser);
    }

    /*
     *查询手机号与邮箱是否已经存在
     * */
    public int selectPhoneAndEmailExist(EcmpUserVo ecmpUser){
        return ecmpUserMapper.selectPhoneAndEmailExist(ecmpUser);
    }

    /**
     * 禁用/启用  员工
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Transactional
    public String updateUseStatus(String status,Long deptId){
        //禁用/启用  员工
        int i1 = ecmpUserMapper.updateUseStatus(deptId, status);
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
        ecmpUserList = ecmpUserMapper.getEcmpUserList(deptId);
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
    public String updatePhoneNum(String newPhoneNum,String reWritePhone){
        String msg="";
        if((newPhoneNum==null||newPhoneNum=="")||(reWritePhone==null||reWritePhone=="")){
            msg="手机号码不可为空！";
        }
        EcmpUserVo ecmpUser=new EcmpUserVo();
        ecmpUser.setNewPhoneNum(newPhoneNum);
        int i = ecmpUserMapper.selectPhoneAndEmailExist(ecmpUser);
        if(i>0){
            msg="该手机号已存在，不可重复录入！";
        }
        if(!reWritePhone.equals(newPhoneNum)){
            msg="手机号码不一致！";
        }
        int i1 = ecmpUserMapper.updatePhoneNum(newPhoneNum);
        if(i1==1){
            msg="手机号码修改成功！";
        }
        return msg;
    }

    /*员工详情
    @param  userId员工编号
    * @return
    * */
    public EcmpUserDto selectEcmpUserDetail(Long userId){
        return ecmpUserMapper.selectEcmpUserDetail(userId);
    }
}
