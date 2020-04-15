package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.DriverServiceAppraiseeInfo;

import java.util.List;

/**
 * 驾驶员-服务评价表

针对自使用自有车辆的订单，可以记录评价详细内容
针对 网约车 订单，记录其星值分数
(DriverServiceAppraiseeInfo)表服务接口
 *
 * @author makejava
 * @since 2020-03-07 15:56:25
 */
public interface DriverServiceAppraiseeInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param appraiseId 主键
     * @return 实例对象
     */
    DriverServiceAppraiseeInfo queryById(Long appraiseId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<DriverServiceAppraiseeInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param driverServiceAppraiseeInfo 实例对象
     * @return 实例对象
     */
    DriverServiceAppraiseeInfo insert(DriverServiceAppraiseeInfo driverServiceAppraiseeInfo);

    /**
     * 修改数据
     *
     * @param driverServiceAppraiseeInfo 实例对象
     * @return 实例对象
     */
    DriverServiceAppraiseeInfo update(DriverServiceAppraiseeInfo driverServiceAppraiseeInfo);

    /**
     * 通过主键删除数据
     *
     * @param appraiseId 主键
     * @return 是否成功
     */
    boolean deleteById(Long appraiseId);

}