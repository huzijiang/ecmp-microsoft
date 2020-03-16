package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户信息Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpUserServiceImpl implements IEcmpUserService {
    @Autowired
    private EcmpUserMapper ecmpUserMapper;


    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户信息ID
     * @return 用户信息
     */
    @Override
    public EcmpUser selectEcmpUserById(Long userId) {
        return ecmpUserMapper.selectEcmpUserById(userId);
    }

    /**
     * 查询用户信息列表
     *
     * @param ecmpUser 用户信息
     * @return 用户信息
     */
    @Override
    public List<EcmpUser> selectEcmpUserList(EcmpUser ecmpUser) {
        return ecmpUserMapper.selectEcmpUserList(ecmpUser);
    }

    /**
     * 新增用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    @Override
    public int insertEcmpUser(EcmpUser ecmpUser) {
        ecmpUser.setCreateTime(DateUtils.getNowDate());
        return ecmpUserMapper.insertEcmpUser(ecmpUser);
    }

    /**
     * 修改用户信息
     *
     * @param ecmpUser 用户信息
     * @return 结果
     */
    @Override
    public int updateEcmpUser(EcmpUser ecmpUser) {
        ecmpUser.setUpdateTime(DateUtils.getNowDate());
        return ecmpUserMapper.updateEcmpUser(ecmpUser);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户信息ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserByIds(Long[] userIds) {
        return ecmpUserMapper.deleteEcmpUserByIds(userIds);
    }

    /**
     * 删除用户信息信息
     *
     * @param userId 用户信息ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserById(Long userId) {
        return ecmpUserMapper.deleteEcmpUserById(userId);
    }

	@Override
	public boolean isDispatcher(Long userId) {
		Integer count = ecmpUserMapper.queryDispatcher(userId);
		return count>0;
	}
    /**
     * 可管理员工
     * @return
     */
    @Override
    public int  queryCompanyEmpCunt(){
        return ecmpUserMapper.queryCompanyEmp();
    }
}
