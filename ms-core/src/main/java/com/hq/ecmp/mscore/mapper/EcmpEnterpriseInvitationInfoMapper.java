package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpEnterpriseInvitationInfo;
import com.hq.ecmp.mscore.dto.DriverInvitationDTO;
import com.hq.ecmp.mscore.dto.InvitationDto;
import com.hq.ecmp.mscore.dto.UserInvitationDTO;
import com.hq.ecmp.mscore.vo.InvitationDriverVO;
import com.hq.ecmp.mscore.vo.InvitationUrlVO;
import com.hq.ecmp.mscore.vo.InvitationUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (EcmpEnterpriseInvitationInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-16 18:28:44
 */
@Repository
public interface  EcmpEnterpriseInvitationInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param invitationId 主键
     * @return 实例对象
     */
    EcmpEnterpriseInvitationInfo queryById(Long invitationId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EcmpEnterpriseInvitationInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param ecmpEnterpriseInvitationInfo 实例对象
     * @return 对象列表
     */
    List<EcmpEnterpriseInvitationInfo> queryAll(EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo);

    /**
     * 新增数据
     *
     * @param ecmpEnterpriseInvitationInfo 实例对象
     * @return 影响行数
     */
    int insert(EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo);

    /**
     * 修改数据
     *
     * @param ecmpEnterpriseInvitationInfo 实例对象
     * @return 影响行数
     */
    int update(EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo);

    /**
     * 通过主键删除数据
     *
     * @param invitationId 主键
     * @return 影响行数
     */
    int deleteById(Long invitationId);

    /**
     * 邀请失效/生效
     *
     * @param invitationDto
     * @return
     */
    int updateInvitationState(InvitationDto invitationDto);
    /**
     * 邀请列表-员工
     */
    List<InvitationUserVO> queryInvitationUser(String type);
    /**
     * 邀请员工详情
     */
    public InvitationUserVO  queryInvitationUserDetial(Long invitationId);
    /**
     * 邀请列表-驾驶员
     */
    List<InvitationDriverVO> queryInvitationDriver(String type);
    /**
     * 邀请驾驶员详情
     */
    public InvitationDriverVO  queryInvitationDriverDetial(Long invitationId);

    /**
     *
     * @param driverInvitationDTO
     * @return
     */
    public int insertDriverInvitation(DriverInvitationDTO driverInvitationDTO);
    /**
     *
     * @param uerInvitationDTO
     * @return
     */
     int insertUserInvitation(UserInvitationDTO uerInvitationDTO);

    public InvitationUrlVO queryInvitationUserUrl(@Param("invitationId") Long invitationId );
    public int invitationDel(@Param("invitationId") Long invitationId );

    Long queryInvitationUserCount(String type);

    int  updateInvitationUrl(@Param("invitationId") Long invitationId ,@Param("url") String url );
}