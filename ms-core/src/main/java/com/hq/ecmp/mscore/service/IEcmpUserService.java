package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.UserRegisterDTO;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import com.hq.ecmp.mscore.vo.PageResult;

import java.util.Date;
import java.util.List;

/**
 * 用户信息Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpUserService {


    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户信息ID
     * @return 用户信息
     */
    public EcmpUser selectEcmpUserById(Long userId);

    /**
     * 查询用户信息列表
     *
     * @param ecmpUser 用户信息
     * @return 用户信息集合
     */
    public List<EcmpUser> selectEcmpUserList(EcmpUser ecmpUser);

    /**
     * 新增用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    public int insertEcmpUser(EcmpUser ecmpUser);

    /**
     * 修改用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    public int updateEcmpUser(EcmpUserVo ecmpUser);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户信息ID
     * @return 结果
     */
    public int deleteEcmpUserByIds(Long[] userIds);

    /**
     * 删除用户信息信息
     *
     * @param userId 用户信息ID
     * @return 结果
     */
    public int deleteEcmpUserById(Long userId);
    
    
    /**
     * 判断用户是否是调度员
     * @param userId
     * @return
     */
    public boolean isDispatcher(Long userId);

    /**
     * 可管理员工个数
     * @return
     */
    public int  queryCompanyEmpCunt();
    /**
     * 员工邀请判断是否该手机号是否已经注册
     */
    public int userItisExist(UserRegisterDTO userRegisterDTO);

    /*
     * 获取上级组织id中的员工姓名和电话
     *  @param  ecmpUserVo
     * @return List<EcmpUserDto>
     * */
    public List<EcmpUserDto> getEcmpUserNameAndPhone(EcmpUserVo ecmpUserVo);

    /*
     * 新增员工信息
     *  @param  ecmpUserVo
     * @return int
     * */
    public String addEcmpUser(EcmpUserVo ecmpUser);

    /*
     *查询手机号是否已经存在
     * */
    public int selectPhoneNumberExist(String phonenumber);

    /*
     *查询邮箱是否已经存在
     * */
    public int selectEmailExist(String email);

    /**
     * 禁用/启用  员工
     *
     * @param userId 员工ID
     * @return 结果
     */
    public String updateUseStatus(String status,Long userId);

    /**
     * 逻辑删除员工信息
     *
     * @param userId 员工编号
     * @return 结果
     */
    public int updateDelFlagById(Long userId);

    /**
     * 查询员工列表
     *
     * @param deptId 部门ID
     * @return
     */
    public List<EcmpUserDto> getEcmpUserList(Long deptId);

    /**
     * 显示查询总条数
     * @param ecmpUser
     * @return
     */
    public Integer queryUserListCount(EcmpUserVo ecmpUser);

    /*只修改手机号
    @param  ecmpOrg
     * @return
    * */
    public String updatePhoneNum(String newPhoneNum,String reWritePhone,String userName);

    /*员工详情
   @param  userId员工编号
    * @return
   * */
    public EcmpUserDto selectEcmpUserDetail(Long userId);

    /*设置离职日期
    @param  dimissionTime
     * @return
    * */
    public int updateDimissionTime(Date dimissionTime,Long userId);

    /*已离职数量
    @param  dimissionTime
     * @return
    * */
    public int selectDimissionCount(Long deptId);

    /*已离职列表
    @param  userId
     * @return
    * */
    public PageResult<EcmpUserDto> selectDimissionList(PageRequest pageRequest);

    /**
     * 给员工设置用车制度
     * @param userId
     * @param regimenIds
     */
    void bindUserRegimens(Long userId, List<Long> regimenIds) throws Exception;

    List<EcmpUser> selectUserListByUserIds(String approveUserId);

    /**
     * 给员工修改用车制度
     * @param userId
     * @param regimenIds
     */
    void updateUserRegimens(Long userId, List<Long> regimenIds) throws Exception;

    /*核对哪些员工当天离职
    @param
     * @return
    * */
    public void checkDimissionEcmpUser();

    /**
     * 查询员工工号是否已存在
     *
     * @param jobNumber 员工工号
     * @return
     */
    public int selectJobNumberExist(String jobNumber);


    /**
     * 按照姓名/工号/手机号模糊查询匹配的列表
     *
     * @param nameOrJobNumberOrPhone
     * @return 结果
     */
    public List<EcmpUserDto> selectUserByNickNameOrJobNumber(String nameOrJobNumberOrPhone);

    PageResult<EcmpUserDto> getEcmpUserPage(PageRequest pageRequest);
    
    /**
     * 根据分子公司+员工姓名查询所有员工
     * @param companyId
     * @param name
     * @return
     */
    public List<EcmpUserDto> queryUserListByCompanyIdAndName(Long companyId,String name);
}
