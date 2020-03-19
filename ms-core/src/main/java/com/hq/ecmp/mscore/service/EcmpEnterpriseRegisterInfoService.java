package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpEnterpriseRegisterInfo;
import com.hq.ecmp.mscore.dto.RegisterDTO;
import com.hq.ecmp.mscore.vo.registerDriverVO;
import com.hq.ecmp.mscore.vo.registerUserVO;

import java.util.List;

/**
 * (EcmpEnterpriseRegisterInfo)表服务接口
 *
 * @author makejava
 * @since 2020-03-16 19:00:09
 */
public interface EcmpEnterpriseRegisterInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param registerId 主键
     * @return 实例对象
     */
    EcmpEnterpriseRegisterInfo queryById(Long registerId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EcmpEnterpriseRegisterInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param ecmpEnterpriseRegisterInfo 实例对象
     * @return 实例对象
     */
    EcmpEnterpriseRegisterInfo insert(EcmpEnterpriseRegisterInfo ecmpEnterpriseRegisterInfo);

    /**
     * 修改数据
     *
     * @param ecmpEnterpriseRegisterInfo 实例对象
     * @return 实例对象
     */
    EcmpEnterpriseRegisterInfo update(EcmpEnterpriseRegisterInfo ecmpEnterpriseRegisterInfo);

    /**
     * 通过主键删除数据
     *
     * @param registerId 主键
     * @return 是否成功
     */
    boolean deleteById(Long registerId);

    /**
     * 手机号是否已经申请注册
     */
    public  int itIsRegistration(String phoneNum, String state);

    /**
     * 待审批数量 员工/驾驶员
     * @param
     */
    int waitAmount(RegisterDTO registerDTO);
    /**
     * 待审批列表-员工
     * @param
     */
    List<registerUserVO> queryRegisterUserWait(RegisterDTO registerDTO);
    /**
     * 待审批列表-驾驶员
     * @param
     */
    List<registerDriverVO> queryRegisterDriverWait(RegisterDTO registerDTO);



}