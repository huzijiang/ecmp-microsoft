package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderServiceCostDetailRecordInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * (OrderServiceCostDetailRecordInfo)表数据库访问层
 *
 * @author crk
 * @since 2020-05-24 18:23:06
 */
public interface OrderServiceCostDetailRecordInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param recordId 主键
     * @return 实例对象
     */
    OrderServiceCostDetailRecordInfo queryById(Long recordId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<OrderServiceCostDetailRecordInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /***
     *
     * @param data
     * @return
     */
    List<OrderServiceCostDetailRecordInfo> getList(OrderServiceCostDetailRecordInfo data);


    /***
     *
     * @param data
     * @return
     */
    int updateData(OrderServiceCostDetailRecordInfo data);



    /**
     * 通过实体作为筛选条件查询
     *
     * @param orderServiceCostDetailRecordInfo 实例对象
     * @return 对象列表
     */
    List<OrderServiceCostDetailRecordInfo> queryAll(OrderServiceCostDetailRecordInfo orderServiceCostDetailRecordInfo);

    /**
     * 新增数据
     *
     * @param orderServiceCostDetailRecordInfo 实例对象
     * @return 影响行数
     */
    int insert(OrderServiceCostDetailRecordInfo orderServiceCostDetailRecordInfo);

    /**
     * 修改数据
     *
     * @param orderServiceCostDetailRecordInfo 实例对象
     * @return 影响行数
     */
    int update(OrderServiceCostDetailRecordInfo orderServiceCostDetailRecordInfo);

    /**
     * 通过主键删除数据
     *
     * @param recordId 主键
     * @return 影响行数
     */
    int deleteById(Long recordId);

}