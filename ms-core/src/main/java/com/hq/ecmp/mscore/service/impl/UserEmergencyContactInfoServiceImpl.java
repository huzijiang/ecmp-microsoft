package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.UserEmergencyContactInfo;
import com.hq.ecmp.mscore.mapper.UserEmergencyContactInfoMapper;
import com.hq.ecmp.mscore.service.UserEmergencyContactInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户紧急联系人信息表(UserEmergencyContactInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-03-09 23:58:22
 */
@Service("userEmergencyContactInfoService")
public class UserEmergencyContactInfoServiceImpl implements UserEmergencyContactInfoService {
    @Resource
    private UserEmergencyContactInfoMapper userEmergencyContactInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserEmergencyContactInfo queryById(Long id) {
        return this.userEmergencyContactInfoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<UserEmergencyContactInfo> queryAllByLimit(int offset, int limit) {
        return this.userEmergencyContactInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param userEmergencyContactInfo 实例对象
     * @return 实例对象
     */
    @Override
    public UserEmergencyContactInfo insert(UserEmergencyContactInfo userEmergencyContactInfo) {
        this.userEmergencyContactInfoDao.insert(userEmergencyContactInfo);
        return userEmergencyContactInfo;
    }

    /**
     * 修改数据
     *
     * @param userEmergencyContactInfo 实例对象
     * @return 实例对象
     */
    @Override
    public UserEmergencyContactInfo update(UserEmergencyContactInfo userEmergencyContactInfo) {
        this.userEmergencyContactInfoDao.update(userEmergencyContactInfo);
        return this.queryById(userEmergencyContactInfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.userEmergencyContactInfoDao.deleteById(id) > 0;
    }

    /**
     * 通过userId查询数据
     *
     * @param userId
     * @return 是否成功
     */
    @Override
    public List<UserEmergencyContactInfo> queryByUserId(Long userId) {
        return this.userEmergencyContactInfoDao.queryByUserId(userId);
    }
}