package com.hq.ecmp.mscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseInvitationInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.EcmpEnterpriseInvitationInfoMapper;
import com.hq.ecmp.mscore.service.EcmpEnterpriseInvitationInfoService;
import com.hq.ecmp.mscore.vo.InvitationDriverVO;
import com.hq.ecmp.mscore.vo.InvitationUrlVO;
import com.hq.ecmp.mscore.vo.InvitationUserVO;
import com.hq.ecmp.mscore.vo.PageResult;
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
    public PageResult<InvitationUserVO> queryInvitationUser(PageRequest PageRequest){
        PageHelper.startPage(PageRequest.getPageNum(),PageRequest.getPageSize());
        List<InvitationUserVO> invitationUserVOS = ecmpEnterpriseInvitationInfoMapper.queryInvitationUser(PageRequest.getType());
        System.out.println("返回邀请链接实体VO："+invitationUserVOS);
        Long count=ecmpEnterpriseInvitationInfoMapper.queryInvitationUserCount(PageRequest.getType());
        return new PageResult<>(count,invitationUserVOS);
    }
    /**
     * 邀请员工详情
     */
    public InvitationUserVO  queryInvitationUserDetial(Long invitationId){
        return ecmpEnterpriseInvitationInfoMapper.queryInvitationUserDetial(invitationId);
    }
    /**
     * 邀请列表-驾驶员
     */
    public PageResult<InvitationDriverVO> queryInvitationDriver(PageRequest PageRequest){
        PageHelper.startPage(PageRequest.getPageNum(),PageRequest.getPageSize());
        List<InvitationDriverVO> invitationDriverVOS = ecmpEnterpriseInvitationInfoMapper.queryInvitationDriver(PageRequest.getType());
        System.out.println("返回邀请链接实体VO："+invitationDriverVOS);
        Long count=ecmpEnterpriseInvitationInfoMapper.queryInvitationUserCount(PageRequest.getType());
        return new PageResult<>(count,invitationDriverVOS);
    }

    /**
     * 邀请驾驶员详情
     */
    public InvitationDriverVO  queryInvitationDriverDetial(Long invitationId){
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
       // driverInvitationDTO.setUrl("http:Driver.cn");
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
       // uerInvitationDTO.setUrl("http:User.cn");
        return ecmpEnterpriseInvitationInfoMapper.insertUserInvitation(uerInvitationDTO);
    }

    /**
     * 邀请员工链接
     */
    public InvitationUrlVO queryInvitationUserUrl(Long invitationId){
        return ecmpEnterpriseInvitationInfoMapper.queryInvitationUserUrl(invitationId);
    }
    /**
     * 邀请链接
     */
    public int invitationDel(Long invitationId) {
        return ecmpEnterpriseInvitationInfoMapper.invitationDel(invitationId);

    }

    public int updateInvitationUrl(UserInvitationUrlDTO userInvitationUrlDTO) {
        return ecmpEnterpriseInvitationInfoMapper.updateInvitationUrl(userInvitationUrlDTO.getInvitationId(), userInvitationUrlDTO.getUrl());
    }

}