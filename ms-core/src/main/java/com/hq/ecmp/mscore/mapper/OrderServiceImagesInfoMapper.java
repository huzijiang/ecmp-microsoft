package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderServiceImagesInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (OrderServiceImagesInfo)表数据库访问层
 *
 * @author crk
 * @since 2020-05-25 09:21:16
 */
public interface OrderServiceImagesInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param imageId 主键
     * @return 实例对象
     */
    OrderServiceImagesInfo queryById(Long imageId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<OrderServiceImagesInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param orderServiceImagesInfo 实例对象
     * @return 对象列表
     */
    List<OrderServiceImagesInfo> queryAll(OrderServiceImagesInfo orderServiceImagesInfo);

    /**
     * 新增数据
     *
     * @param orderServiceImagesInfo 实例对象
     * @return 影响行数
     */
    int insert(OrderServiceImagesInfo orderServiceImagesInfo);

    /**
     * 修改数据
     *
     * @param orderServiceImagesInfo 实例对象
     * @return 影响行数
     */
    int update(OrderServiceImagesInfo orderServiceImagesInfo);

    /**
     * 通过主键删除数据
     *
     * @param imageId 主键
     * @return 影响行数
     */
    int deleteById(Long imageId);

    List<OrderServiceImagesInfo> getList(OrderServiceImagesInfo orderServiceImagesInfo);


}