package com.hq.ecmp.mscore.service.impl;


import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.UserAcceptOrderAccountEmailInfo;
import com.hq.ecmp.mscore.mapper.UserAcceptOrderAccountEmailInfoMapper;
import com.hq.ecmp.mscore.service.UserAcceptOrderAccountEmailInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (UserAcceptOrderAccountEmailInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-03-12 12:09:12
 */
@Service("userAcceptOrderAccountEmailInfoService")
public class UserAcceptOrderAccountEmailInfoServiceImpl implements UserAcceptOrderAccountEmailInfoService {

    @Autowired
    private UserAcceptOrderAccountEmailInfoMapper userAcceptOrderAccountEmailInfoMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param Id 主键
     * @return 实例对象
     */
    @Override
    public  UserAcceptOrderAccountEmailInfo queryById(Integer Id) {
        return userAcceptOrderAccountEmailInfoMapper.queryById(Id);
    }

    /**
     * 查询多条数据
     *
     * @param userId 查询起始位置
     * @return 对象列表
     */
    @Override
    public List<UserAcceptOrderAccountEmailInfo> queryAllByUserId(Long userId) {
        return userAcceptOrderAccountEmailInfoMapper.queryAllByLimit(userId);
    }

    /**
     * 新增数据
     *
     * @param userAcceptOrderAccountEmailInfo 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(UserAcceptOrderAccountEmailInfo userAcceptOrderAccountEmailInfo) {

        userAcceptOrderAccountEmailInfo.setCreateTime(DateUtils.getNowDate());
        return userAcceptOrderAccountEmailInfoMapper.insert(userAcceptOrderAccountEmailInfo);
    }

    /**
     * 修改数据
     *
     * @param userAcceptOrderAccountEmailInfo 实例对象
     * @return 实例对象
     */
    @Override
    public int update(UserAcceptOrderAccountEmailInfo userAcceptOrderAccountEmailInfo) {
        userAcceptOrderAccountEmailInfo.setCreateTime(DateUtils.getNowDate());

        return userAcceptOrderAccountEmailInfoMapper.update(userAcceptOrderAccountEmailInfo);

    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public int deleteById(Integer id) {

        return userAcceptOrderAccountEmailInfoMapper.deleteById(id);
    }
}