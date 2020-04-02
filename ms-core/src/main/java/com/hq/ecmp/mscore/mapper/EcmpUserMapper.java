package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.EcmpRoleDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
import com.hq.ecmp.mscore.vo.UserTreeVo;
import com.hq.ecmp.mscore.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 用户信息Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface EcmpUserMapper
{
    /**
     * 查询用户信息
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
     * 删除用户信息
     *
     * @param userId 用户信息ID
     * @return 结果
     */
    public int deleteEcmpUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpUserByIds(Long[] userIds);



    public Integer queryDispatcher(Long userId);

    /**
     * 查询改组织id下的员工人数
     *
     * @param deptId 组织id
     * @return 结果
     */
    public int selectEcmpUserByDeptId(Long deptId);

    /**
     * 禁用/启用  员工
     *
     * @param userId 部门ID
     * @param status 状态
     * @return 结果
     */
    public int updateUseStatus(@Param("userId") Long userId, @Param("status") String status);

    /**
     * 关联部门/公司--禁用/启用  员工
     *
     * @param deptId 部门ID
     * @param status 状态
     * @return 结果
     */
    public int updateRelationUseStatus(@Param("deptId") Long deptId, @Param("status") String status);

    /*
     * 获取上级组织id中的员工姓名和电话、邮箱
     *  @param  deptid--上级组织id
     * @return List<EcmpUserDto>
     * */
    public List<EcmpUserDto> getEcmpUserNameAndPhone(EcmpUserVo ecmpUserVo);

    /*
     * 新增员工信息
     *  @param  ecmpUserVo
     * @return int
     * */
    public int addEcmpUser(EcmpUserVo ecmpUser);

    /*
     * 新增员工&用车制度中间表信息
     *  @param  ecmpUserVo
     * @return int
     * */
    public int addUserRegimeRelation(EcmpUserVo ecmpUser);

    /*
     * 修改员工&用车制度中间表信息
     *  @param  ecmpUserVo
     * @return int
     * */
    public int updateUserRegimeRelation(EcmpUserVo ecmpUser);

    /*
     *查询手机号是否已经存在
     * */
    public int selectPhoneNumberExist(@Param("phonenumber")String phonenumber);

    /*
     *查询邮箱是否已经存在
     * */
    public int selectEmailExist(@Param("email")String email);

    /**
     * 逻辑删除员工信息
     *
     * @param userId 员工编号
     * @return 结果
     */
    public int updateDelFlagById(Long userId);

    /**
     * 查询该部门下的所有员工编号
     *
     * @param deptId 部门编号
     * @return 结果
     */
    public List<Long> getEcmpUserIdsByDeptId(@Param("deptId") Long deptId);

    /**
     * 查询该部门下的所有员工编号
     *
     * @param deptId 部门编号
     * @return 结果
     */
    public Long selectEcmpUserSubDeptCount(@Param("deptId") Long deptId);

    /**
     * 查询该部门下的所有员工编号
     *
     * @param deptId 部门编号
     * @return 结果
     */
    public String selectEcmpUserSubDept(@Param("deptId") Long deptId);

    /**
     * 获取员工列表
     *
     * @param deptId 部门编号
     * @return 结果
     */
    public EcmpUserDto getEcmpUserList(@Param("deptId") Long deptId,@Param("userId")Long userId);

    /**
     * 逻辑删除员工信息
     *
     * @param phonenumber 手机号
     * @return 结果
     */
    public int updatePhoneNum(@Param("phonenumber")String phonenumber,@Param("userName")String userName,@Param("userId")Long userId);

    /*员工详情
    @param  userId员工编号
    * @return
    * */
    public EcmpUserDto selectEcmpUserDetail(@Param("userId")Long userId);

    public int  queryCompanyEmp();


    /*设置离职日期
    @param  dimissionTime
     * @return
    * */
    public int updateDimissionTime(@Param("dimissionTime")Date dimissionTime,@Param("userId")Long userId);

    /*已离职数量
    @param
     * @return
    * */
    public int selectDimissionCount();

    /*已离职员工编号
    @param  dimissionTime
     * @return
    * */
    public List<Long> selectDimissionEcmpUserIds();

    /*已离职列表
    @param  userId
     * @return
    * */
    public List<EcmpUserDto> selectDimissionList(@Param("deptId") Long deptId);

    /*查询当天离职的员工id
    @param  userId
     * @return
    * */
    public List<Long> checkDimissionEcmpUserIds(String dateOfTheDay);

    /*修改当天离职的员工状态
    @param  userId
     * @return
    * */
    public int updateDimissionEcmpUser(Long userId);

    /*查询员工是否为驾驶员
    @param  userId
     * @return
    * */
    public String selectEcmpUserIsDirver(Long userId);

    /*根据员工编号获取拥有的角色名称
   @param  userId
    * @return
   * */
    public List<EcmpRoleDto> selectRoleNameByEcmpUserId(Long userId);

    /**
     * 员工邀请判断是否该手机号是否已经注册
     */
    public int userItisExist(String phoneNumber);

    //查询该员工部门领导
    UserVO findDeptLeader(Long deptId);

    List<EcmpUser> selectUserListByUserIds(String userIds);


    /**
     * 查询上级部门下的所有员工
     * @param
     * @return
     */
    List<EcmpUserDto> selectUserByDeptId(Long deptId);

    /**
     * 查询员工工号是否已存在
     *
     * @param jobNumber 员工工号
     * @return
     */
    public int selectJobNumberExist(String jobNumber);


    /**
     * 按照姓名/工号/手机号模糊查询匹配userId
     * @param  nickName
     * @return 结果
     */
    public List<Long> selectUserIdsByNickNameOrJobNumber(@Param("nickName")String nickName,@Param("phonenumber")String phonenumber,@Param("jobNumber")String jobNumber);


    /**
     * 按照姓名/工号/手机号模糊查询匹配员工列表
     * @param nickName
     * @return 结果
     */
    public EcmpUserDto selectUserByNickNameOrJobNumber(@Param("nickName")String nickName,@Param("phonenumber")String phonenumber,@Param("jobNumber")String jobNumber, @Param("userId")Long userId);


    List<EcmpUser> getListByUserIds(@Param("userIds") List<Long> userIds);

    UserVO selectUserVoById(Long userId);

    List<Long> findUserIds(@Param("startUserId") Long startUserId,@Param("endUserId") Long endUserId);

    String findNameByUserIds(String userIds);

    List<UserTreeVo> selectListByDeptId(Long deptId);
}

