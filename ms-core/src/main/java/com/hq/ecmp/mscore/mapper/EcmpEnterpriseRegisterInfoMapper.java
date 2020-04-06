package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpEnterpriseRegisterInfo;

import com.hq.ecmp.mscore.dto.RegisterDTO;
import com.hq.ecmp.mscore.dto.UserRegisterDTO;
import com.hq.ecmp.mscore.vo.RegisterDriverVO;
import com.hq.ecmp.mscore.vo.RegisterUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (EcmpEnterpriseRegisterInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-16 18:58:41
 */
@Repository
public interface EcmpEnterpriseRegisterInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param registerId 主键
     * @return 实例对象
     */
    EcmpEnterpriseRegisterInfo queryById(Long registerId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EcmpEnterpriseRegisterInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param ecmpEnterpriseRegisterInfo 实例对象
     * @return 对象列表
     */
    List<EcmpEnterpriseRegisterInfo> queryAll(EcmpEnterpriseRegisterInfo ecmpEnterpriseRegisterInfo);

    /**
     * 新增数据
     *
     * @param ecmpEnterpriseRegisterInfo 实例对象
     * @return 影响行数
     */
    int insert(EcmpEnterpriseRegisterInfo ecmpEnterpriseRegisterInfo);

    /**
     * 修改数据
     *
     * @param ecmpEnterpriseRegisterInfo 实例对象
     * @return 影响行数
     */
    int update(EcmpEnterpriseRegisterInfo ecmpEnterpriseRegisterInfo);

    /**
     * 通过主键删除数据
     *
     * @param registerId 主键
     * @return 影响行数
     */
    int deleteById(Long registerId);
    /**
     * 手机号是否已经申请注册
     */
    int itIsRegistration(@Param("phoneNum") String phoneNum, @Param("state") String state);

    /**
     * 待审批列表-员工
     * @param
     */
    List<RegisterUserVO> queryRegisterUserWait(@Param("deptId") Long deptId,@Param("type") String type);
    /**
     * 待审批列表-驾驶员
     * @param
     */
    List<RegisterDriverVO> queryRegisterDriverWait(String type);
    /**
     * 待审批数量
     * @param
     */
    public int waitAmountCount(@Param("deptId") Long deptId,@Param("type") String type);
    /**
     * 注册申请：拒绝/通过
     */
    int updateRegisterState(RegisterDTO registerDTO);
    /**
     * 员工注册
     * @param userRegisterDTO
     * @return
     */
    UserRegisterDTO insertUserRegister(UserRegisterDTO userRegisterDTO);


    Long queryRegisterUserWaitCount(@Param("deptId") Long deptId,@Param("type") String type);
}