package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.DriverServiceAppraiseeInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 驾驶员-服务评价表

针对自使用自有车辆的订单，可以记录评价详细内容
针对 网约车 订单，记录其星值分数
(DriverServiceAppraiseeInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-07 15:56:25
 */
public interface DriverServiceAppraiseeInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param appraiseId 主键
     * @return 实例对象
     */
    DriverServiceAppraiseeInfo queryById(Long appraiseId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<DriverServiceAppraiseeInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param driverServiceAppraiseeInfo 实例对象
     * @return 对象列表
     */
    List<DriverServiceAppraiseeInfo> queryAll(DriverServiceAppraiseeInfo driverServiceAppraiseeInfo);

    /**
     * 新增数据
     *
     * @param driverServiceAppraiseeInfo 实例对象
     * @return 影响行数
     */
    int insert(DriverServiceAppraiseeInfo driverServiceAppraiseeInfo);

    /**
     * 修改数据
     *
     * @param driverServiceAppraiseeInfo 实例对象
     * @return 影响行数
     */
    int update(DriverServiceAppraiseeInfo driverServiceAppraiseeInfo);

    /**
     * 通过主键删除数据
     *
     * @param appraiseId 主键
     * @return 影响行数
     */
    int deleteById(Long appraiseId);

}