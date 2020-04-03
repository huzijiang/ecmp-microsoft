package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseInvitationInfo;
import com.hq.ecmp.mscore.dto.DriverInvitationDTO;
import com.hq.ecmp.mscore.dto.InvitationDto;
import com.hq.ecmp.mscore.dto.InvitationInfoDTO;
import com.hq.ecmp.mscore.dto.UserInvitationDTO;
import com.hq.ecmp.mscore.mapper.EcmpEnterpriseInvitationInfoMapper;
import com.hq.ecmp.mscore.service.EcmpEnterpriseInvitationInfoService;
import com.hq.ecmp.mscore.vo.InvitationDriverVO;
import com.hq.ecmp.mscore.vo.InvitationUserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (EcmpEnterpriseInvitationInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-03-16 18:33:22
 */
@Service("ecmpEnterpriseInvitationInfoService")
public class EcmpEnterpriseInvitationInfoServiceImpl implements EcmpEnterpriseInvitationInfoService {
    @Resource
    private EcmpEnterpriseInvitationInfoMapper ecmpEnterpriseInvitationInfoMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param invitationId 主键
     * @return 实例对象
     */
    @Override
    public EcmpEnterpriseInvitationInfo queryById(Long invitationId) {
        return this.ecmpEnterpriseInvitationInfoMapper.queryById(invitationId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<EcmpEnterpriseInvitationInfo> queryAllByLimit(int offset, int limit) {
        return this.ecmpEnterpriseInvitationInfoMapper.queryAllByLimit(offset, limit);
    }


    /**
     * 新增数据
     *
     * @param ecmpEnterpriseInvitationInfo 实例对象
     * @return 实例对象
     */

    public int insert(EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo) {

        return ecmpEnterpriseInvitationInfoMapper.insert(ecmpEnterpriseInvitationInfo);
    }

    /**
     * 修改数据
     *
     * @param ecmpEnterpriseInvitationInfo 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpEnterpriseInvitationInfo update(EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo) {
        this.ecmpEnterpriseInvitationInfoMapper.update(ecmpEnterpriseInvitationInfo);
        return this.queryById(ecmpEnterpriseInvitationInfo.getInvitationId());
    }

    /**
     * 通过主键删除数据
     *
     * @param invitationId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long invitationId) {
        return this.ecmpEnterpriseInvitationInfoMapper.deleteById(invitationId) > 0;
    }


    /**
     * 邀请：停用/启用
     */
    @Override
    public int updateInvitationState(InvitationDto invitationDto){

        return ecmpEnterpriseInvitationInfoMapper.updateInvitationState(invitationDto);
    }
    /**
     * 邀请列表-员工
     */
    public List<InvitationUserVO> queryInvitationUser(InvitationInfoDTO invitationInfoDTO){
        invitationInfoDTO.setType("T001");
        return ecmpEnterpriseInvitationInfoMapper.queryInvitationUser(invitationInfoDTO.getType());
    }
    /**
     * 邀请员工详情
     */
    public InvitationUserVO  queryInvitationUserDetial(String invitationId){
        return ecmpEnterpriseInvitationInfoMapper.queryInvitationUserDetial(invitationId);
    }
    /**
     * 邀请列表-驾驶员
     */
    public List<InvitationDriverVO> queryInvitationDriver(InvitationInfoDTO invitationInfoDTO){
        return ecmpEnterpriseInvitationInfoMapper.queryInvitationDriver(invitationInfoDTO.getType());
    }
    /**
     * 邀请驾驶员详情
     */
    public InvitationDriverVO  queryInvitationDriverDetial(String invitationId){
        return ecmpEnterpriseInvitationInfoMapper.queryInvitationDriverDetial(invitationId);
    }
    /**
     *
     * @param driverInvitationDTO
     * @return
     */
    public int insertDriverInvitation(DriverInvitationDTO driverInvitationDTO){
        driverInvitationDTO.setCreateTime(DateUtils.getNowDate());
        driverInvitationDTO.setState("Y000");
        driverInvitationDTO.setType("T002");
        return ecmpEnterpriseInvitationInfoMapper.insertDriverInvitation(driverInvitationDTO);
    }
    /**
     *
     * @param uerInvitationDTO
     * @return
     */
    public int insertUserInvitation(UserInvitationDTO uerInvitationDTO){
        uerInvitationDTO.setCreateTime(DateUtils.getNowDate());
        uerInvitationDTO.setState("Y000");
        uerInvitationDTO.setType("T001");
        return ecmpEnterpriseInvitationInfoMapper.insertUserInvitation(uerInvitationDTO);
    }

}