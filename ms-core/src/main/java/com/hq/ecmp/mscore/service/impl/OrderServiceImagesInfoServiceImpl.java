package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.OrderServiceImagesInfo;
import com.hq.ecmp.mscore.mapper.OrderServiceImagesInfoMapper;
import com.hq.ecmp.mscore.service.OrderServiceImagesInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (OrderServiceImagesInfo)表服务实现类
 *
 * @author crk
 * @since 2020-05-25 09:21:17
 */
@Service("orderServiceImagesInfoService")
public class OrderServiceImagesInfoServiceImpl implements OrderServiceImagesInfoService {
    @Resource
    private OrderServiceImagesInfoMapper orderServiceImagesInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param imageId 主键
     * @return 实例对象
     */
    @Override
    public OrderServiceImagesInfo queryById(Long imageId) {
        return this.orderServiceImagesInfoDao.queryById(imageId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<OrderServiceImagesInfo> queryAllByLimit(int offset, int limit) {
        return this.orderServiceImagesInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param orderServiceImagesInfo 实例对象
     * @return 实例对象
     */
    @Override
    public OrderServiceImagesInfo insert(OrderServiceImagesInfo orderServiceImagesInfo) {
        this.orderServiceImagesInfoDao.insert(orderServiceImagesInfo);
        return orderServiceImagesInfo;
    }

    /**
     * 修改数据
     *
     * @param orderServiceImagesInfo 实例对象
     * @return 实例对象
     */
    @Override
    public OrderServiceImagesInfo update(OrderServiceImagesInfo orderServiceImagesInfo) {
        this.orderServiceImagesInfoDao.update(orderServiceImagesInfo);
        return this.queryById(orderServiceImagesInfo.getImageId());
    }

    /**
     * 通过主键删除数据
     *
     * @param imageId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long imageId) {
        return this.orderServiceImagesInfoDao.deleteById(imageId) > 0;
    }
}