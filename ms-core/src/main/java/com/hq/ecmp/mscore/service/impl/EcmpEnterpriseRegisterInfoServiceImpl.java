package com.hq.ecmp.mscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.InvitionStateEnum;
import com.hq.ecmp.constant.InvitionTypeEnum;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseInvitationInfo;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseRegisterInfo;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.dto.InvitationDto;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.RegisterDTO;
import com.hq.ecmp.mscore.dto.UserRegisterDTO;
import com.hq.ecmp.mscore.mapper.EcmpEnterpriseInvitationInfoMapper;
import com.hq.ecmp.mscore.mapper.EcmpEnterpriseRegisterInfoMapper;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.service.EcmpEnterpriseRegisterInfoService;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.RegisterDriverVO;
import com.hq.ecmp.mscore.vo.RegisterUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * (EcmpEnterpriseRegisterInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-03-16 19:00:09
 */
@Service("ecmpEnterpriseRegisterInfoService")
@Slf4j
public class EcmpEnterpriseRegisterInfoServiceImpl implements EcmpEnterpriseRegisterInfoService {
    @Resource
    private EcmpEnterpriseRegisterInfoMapper ecmpEnterpriseRegisterInfoMapper;
    @Resource
    private EcmpEnterpriseInvitationInfoMapper invitationInfoMapper;
    @Resource
    private EcmpUserMapper ecmpUserMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param registerId 主键
     * @return 实例对象
     */
    @Override
    public EcmpEnterpriseRegisterInfo queryById(Long registerId) {
        return this.ecmpEnterpriseRegisterInfoMapper.queryById(registerId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<EcmpEnterpriseRegisterInfo> queryAllByLimit(int offset, int limit) {
        return this.ecmpEnterpriseRegisterInfoMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param ecmpEnterpriseRegisterInfo 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpEnterpriseRegisterInfo insert(EcmpEnterpriseRegisterInfo ecmpEnterpriseRegisterInfo) {
        this.ecmpEnterpriseRegisterInfoMapper.insert(ecmpEnterpriseRegisterInfo);
        return ecmpEnterpriseRegisterInfo;
    }

    /**
     * 修改数据
     *
     * @param ecmpEnterpriseRegisterInfo 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpEnterpriseRegisterInfo update(EcmpEnterpriseRegisterInfo ecmpEnterpriseRegisterInfo) {
        this.ecmpEnterpriseRegisterInfoMapper.update(ecmpEnterpriseRegisterInfo);
        return this.queryById(ecmpEnterpriseRegisterInfo.getRegisterId());
    }

    /**
     * 通过主键删除数据
     *
     * @param registerId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long registerId) {
        return this.ecmpEnterpriseRegisterInfoMapper.deleteById(registerId) > 0;
    }
    /**
     * 手机号是否已经申请注册
     */
    public  int itIsRegistration(String phoneNum,String state){
        return ecmpEnterpriseRegisterInfoMapper.itIsRegistration(phoneNum,state);
    }

    /**
     * 待审批数量 员工/驾驶员
     * @param
     */
    public int waitAmount(Long deptId,String type){
        return ecmpEnterpriseRegisterInfoMapper.waitAmountCount(deptId,type);
    }

    /**
     * 待审批列表-员工
     * @param
     */
    @Override
    public PageResult<RegisterUserVO> queryRegisterUserWait(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<RegisterUserVO> registerUserVOS = ecmpEnterpriseRegisterInfoMapper.queryRegisterUserWait(pageRequest.getDeptId(),pageRequest.getType());
        Long count= ecmpEnterpriseRegisterInfoMapper.queryRegisterUserWaitCount(pageRequest.getDeptId(),pageRequest.getType());
        return new PageResult<>(count,registerUserVOS);
    }
    /**
     * 待审批列表-驾驶员
     * @param
     */
    @Override
    public List<RegisterDriverVO> queryRegisterDriverWait(RegisterDTO registerDTO){
        return ecmpEnterpriseRegisterInfoMapper.queryRegisterDriverWait(registerDTO.getType());
    }
    /**
     * 注册申请：拒绝/通过
     */
    @Override
    public int updateRegisterState(RegisterDTO registerDTO){
        return ecmpEnterpriseRegisterInfoMapper.updateRegisterState(registerDTO);
    }
    /**
     * 员工注册
     * @param userRegisterDTO
     * @return
     */
    public UserRegisterDTO insertUserRegister(UserRegisterDTO userRegisterDTO){
        userRegisterDTO.setCreateTime(DateUtils.getNowDate());
        return ecmpEnterpriseRegisterInfoMapper.insertUserRegister(userRegisterDTO);
    }

    @Override
    public int updateRegisterApprove(Long registerId,Long userId,String reason,String state) throws Exception {
        EcmpEnterpriseRegisterInfo registerInfo = ecmpEnterpriseRegisterInfoMapper.queryById(registerId);
        if (registerInfo==null){
            throw new Exception("该注册员工id:"+registerId+"不存在");
        }
        EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo = invitationInfoMapper.queryById(registerInfo.getInvitationId());
        if (ecmpEnterpriseInvitationInfo==null){
            throw new Exception("该注册员工来源不明");
        }
        if (!InvitionTypeEnum.USER.getKey().equals(registerInfo.getType())){//不是员工
            throw new Exception("不可审核驾驶员");
        }
        registerInfo.setAuditTime(new Date());
        registerInfo.setAuditor(userId);
        registerInfo.setUpdateBy(String.valueOf(userId));
        registerInfo.setUpdateTime(new Date());
        registerInfo.setState(state);
        if (InvitionStateEnum.APPROVEREJECT.getKey().equals(state)){
            registerInfo.setRejectReason(reason);
        }
        int count = ecmpEnterpriseRegisterInfoMapper.update(registerInfo);
        log.info(registerId+"邀请注册被员工"+userId+"审核通过");
        if (InvitionStateEnum.APPROVEREJECT.getKey().equals(state)){
            return count;
        }
        if (count>0){
            EcmpUser ecmpUser = new EcmpUser();
            ecmpUser.setPhonenumber(registerInfo.getMobile());
            ecmpUser.setUserName(registerInfo.getMobile());
            ecmpUser.setDeptId(ecmpEnterpriseInvitationInfo.getDepartmentId());
            ecmpUser.setNickName(registerInfo.getName());
            ecmpUser.setStatus(CommonConstant.SWITCH_ON);
            ecmpUser.setEmail(registerInfo.getEmail());
            ecmpUser.setItIsDispatcher(CommonConstant.SWITCH_ON);
            ecmpUser.setCreateBy(String.valueOf(userId));
            ecmpUser.setCreateTime(new Date());
            ecmpUser.setSex(registerInfo.getGender());
            ecmpUser.setUserType(CommonConstant.IS_PEER);
            ecmpUser.setJobNumber(registerInfo.getJobNumber());
            ecmpUser.setDelFlag(CommonConstant.SWITCH_ON);
            ecmpUser.setItIsDriver(CommonConstant.SWITCH_ON);
            ecmpUser.setRemark("邀请注册");
            ecmpUserMapper.insertEcmpUser(ecmpUser);
            log.info("员工"+registerInfo.getMobile()+"审核添加成功");
        }
        return count;
    }

    @Override
    public void updateRegisterRefuse(Long registerId, String reason) throws Exception {
        EcmpEnterpriseRegisterInfo registerInfo = ecmpEnterpriseRegisterInfoMapper.queryById(registerId);
        if (registerInfo==null){
            throw new Exception("该注册员工id:"+registerId+"不存在");
        }
        EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo = invitationInfoMapper.queryById(registerInfo.getInvitationId());
        if (ecmpEnterpriseInvitationInfo==null){
            throw new Exception("该注册员工来源不明");
        }
    }


}