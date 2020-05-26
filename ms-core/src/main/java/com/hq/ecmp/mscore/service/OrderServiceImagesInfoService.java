package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.OrderServiceImagesInfo;
import java.util.List;

/**
 * (OrderServiceImagesInfo)表服务接口
 *
 * @author crk
 * @since 2020-05-25 09:21:17
 */
public interface OrderServiceImagesInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param imageId 主键
     * @return 实例对象
     */
    OrderServiceImagesInfo queryById(Long imageId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<OrderServiceImagesInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param orderServiceImagesInfo 实例对象
     * @return 实例对象
     */
    OrderServiceImagesInfo insert(OrderServiceImagesInfo orderServiceImagesInfo);

    /**
     * 修改数据
     *
     * @param orderServiceImagesInfo 实例对象
     * @return 实例对象
     */
    OrderServiceImagesInfo update(OrderServiceImagesInfo orderServiceImagesInfo);

    /**
     * 通过主键删除数据
     *
     * @param imageId 主键
     * @return 是否成功
     */
    boolean deleteById(Long imageId);

}