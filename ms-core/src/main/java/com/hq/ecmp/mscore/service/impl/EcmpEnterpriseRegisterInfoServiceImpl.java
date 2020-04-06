package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseRegisterInfo;
import com.hq.ecmp.mscore.dto.InvitationDto;
import com.hq.ecmp.mscore.dto.RegisterDTO;
import com.hq.ecmp.mscore.dto.UserRegisterDTO;
import com.hq.ecmp.mscore.mapper.EcmpEnterpriseRegisterInfoMapper;
import com.hq.ecmp.mscore.service.EcmpEnterpriseRegisterInfoService;
import com.hq.ecmp.mscore.vo.RegisterDriverVO;
import com.hq.ecmp.mscore.vo.RegisterUserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (EcmpEnterpriseRegisterInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-03-16 19:00:09
 */
@Service("ecmpEnterpriseRegisterInfoService")
public class EcmpEnterpriseRegisterInfoServiceImpl implements EcmpEnterpriseRegisterInfoService {
    @Resource
    private EcmpEnterpriseRegisterInfoMapper ecmpEnterpriseRegisterInfoMapper;

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
    public int waitAmount(RegisterDTO registerDTO){


        return ecmpEnterpriseRegisterInfoMapper.waitAmountCount(registerDTO.getType(),registerDTO.getState());
    }

    /**
     * 待审批列表-员工
     * @param
     */
    @Override
    public List<RegisterUserVO> queryRegisterUserWait(RegisterDTO registerDTO) {
        return ecmpEnterpriseRegisterInfoMapper.queryRegisterUserWait(registerDTO.getType());
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


}