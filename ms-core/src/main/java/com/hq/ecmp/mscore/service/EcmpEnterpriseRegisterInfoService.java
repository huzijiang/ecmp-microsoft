package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpEnterpriseRegisterInfo;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.dto.RegisterDTO;
import com.hq.ecmp.mscore.dto.UserRegisterDTO;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.mscore.vo.RegisterDriverVO;
import com.hq.ecmp.mscore.vo.RegisterUserVO;

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
    int waitAmount(Long deptId,String type);
    /**
     * 待审批列表-员工
     * @param
     */
    PageResult<RegisterUserVO> queryRegisterUserWait(PageRequest pageRequest);
    /**
     * 待审批列表-驾驶员
     * @param
     */
    List<RegisterDriverVO> queryRegisterDriverWait(RegisterDTO registerDTO);
    /**
     * 注册申请：拒绝/通过
     */
    public int updateRegisterState(RegisterDTO registerDTO);

    /**
     * 员工注册
     * @param userRegisterDTO
     * @return
     */
    UserRegisterDTO insertUserRegister(UserRegisterDTO userRegisterDTO);


    int updateRegisterApprove(Long registerId,Long userId,String reason,String state) throws Exception;

    void updateRegisterRefuse(Long registerId, String reason)throws Exception;;
}