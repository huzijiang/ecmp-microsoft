package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.DriverServiceAppraiseeInfo;
import com.hq.ecmp.mscore.mapper.DriverServiceAppraiseeInfoMapper;
import com.hq.ecmp.mscore.service.DriverServiceAppraiseeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 驾驶员-服务评价表

针对自使用自有车辆的订单，可以记录评价详细内容
针对 网约车 订单，记录其星值分数
(DriverServiceAppraiseeInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-03-07 15:56:25
 */
@Service("driverServiceAppraiseeInfoService")
public class DriverServiceAppraiseeInfoServiceImpl implements DriverServiceAppraiseeInfoService {
    @Autowired
    private DriverServiceAppraiseeInfoMapper driverServiceAppraiseeInfoMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param appraiseId 主键
     * @return 实例对象
     */
    @Override
    public DriverServiceAppraiseeInfo queryById(Long appraiseId) {
        return this.driverServiceAppraiseeInfoMapper.queryById(appraiseId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<DriverServiceAppraiseeInfo> queryAllByLimit(int offset, int limit) {
        return this.driverServiceAppraiseeInfoMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param driverServiceAppraiseeInfo 实例对象
     * @return 实例对象
     */
    @Override
    public DriverServiceAppraiseeInfo insert(DriverServiceAppraiseeInfo driverServiceAppraiseeInfo) {
        driverServiceAppraiseeInfo.setCreateTime(DateUtils.getNowDate());
        this.driverServiceAppraiseeInfoMapper.insert(driverServiceAppraiseeInfo);
        return driverServiceAppraiseeInfo;
    }

    /**
     * 修改数据
     *
     * @param driverServiceAppraiseeInfo 实例对象
     * @return 实例对象
     */
    @Override
    public DriverServiceAppraiseeInfo update(DriverServiceAppraiseeInfo driverServiceAppraiseeInfo) {
        this.driverServiceAppraiseeInfoMapper.update(driverServiceAppraiseeInfo);
        return this.queryById(driverServiceAppraiseeInfo.getAppraiseId());
    }

    /**
     * 通过主键删除数据
     *
     * @param appraiseId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long appraiseId) {
        return this.driverServiceAppraiseeInfoMapper.deleteById(appraiseId) > 0;
    }
}