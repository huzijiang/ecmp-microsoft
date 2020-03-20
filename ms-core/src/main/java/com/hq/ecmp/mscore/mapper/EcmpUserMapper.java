package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.vo.EcmpUserVo;
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
     * 获取上级组织id中的员工姓名和电话
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
     *查询手机号与邮箱是否已经存在
     * */
    public int selectPhoneAndEmailExist(@Param("phonenumber")String phonenumber,@Param("email")String email);

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
    public EcmpUserDto selectEcmpUserDetail(Long userId);

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
    public Long[] selectDimissionEcmpUserIds();

    /*已离职列表
    @param  userId
     * @return
    * */
    public List<EcmpUserDto> selectDimissionList(Long userId);

    /**
     * 员工邀请判断是否该手机号是否已经注册
     */
    public int userItisExist(String phoneNumber);

    public List<EcmpUserDto> selectDimissionList(Long deptId,Long userId);
    //查询该员工部门领导
    UserVO findDeptLeader(long parseLong);

    List<EcmpUser> selectUserListByUserIds(String userIds);
}

