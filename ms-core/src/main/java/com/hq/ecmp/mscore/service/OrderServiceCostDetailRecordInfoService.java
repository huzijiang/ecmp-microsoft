package com.hq.ecmp.mscore.service;


import com.hq.ecmp.mscore.domain.OrderServiceCostDetailRecordInfo;

import java.util.List;

/**
 * (OrderServiceCostDetailRecordInfo)表服务接口
 *
 * @author crk
 * @since 2020-05-24 18:23:08
 */
public interface OrderServiceCostDetailRecordInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param recordId 主键
     * @return 实例对象
     */
    OrderServiceCostDetailRecordInfo queryById(Long recordId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<OrderServiceCostDetailRecordInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param orderServiceCostDetailRecordInfo 实例对象
     * @return 实例对象
     */
    OrderServiceCostDetailRecordInfo insert(OrderServiceCostDetailRecordInfo orderServiceCostDetailRecordInfo);

    /**
     * 修改数据
     *
     * @param orderServiceCostDetailRecordInfo 实例对象
     * @return 实例对象
     */
    OrderServiceCostDetailRecordInfo update(OrderServiceCostDetailRecordInfo orderServiceCostDetailRecordInfo);

    /**
     * 通过主键删除数据
     *
     * @param recordId 主键
     * @return 是否成功
     */
    boolean deleteById(Long recordId);

}