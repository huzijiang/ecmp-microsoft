package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.OrderServiceCostDetailRecordInfo;
import com.hq.ecmp.mscore.service.OrderServiceCostDetailRecordInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (OrderServiceCostDetailRecordInfo)表服务实现类
 *
 * @author crk
 * @since 2020-05-24 18:23:10
 */
@Service("orderServiceCostDetailRecordInfoService")
public class OrderServiceCostDetailRecordInfoServiceImpl implements OrderServiceCostDetailRecordInfoService {
    @Resource
    private OrderServiceCostDetailRecordInfoService orderServiceCostDetailRecordInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param recordId 主键
     * @return 实例对象
     */
    @Override
    public OrderServiceCostDetailRecordInfo queryById(Long recordId) {
        return this.orderServiceCostDetailRecordInfoDao.queryById(recordId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<OrderServiceCostDetailRecordInfo> queryAllByLimit(int offset, int limit) {
        return this.orderServiceCostDetailRecordInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param orderServiceCostDetailRecordInfo 实例对象
     * @return 实例对象
     */
    @Override
    public OrderServiceCostDetailRecordInfo insert(OrderServiceCostDetailRecordInfo orderServiceCostDetailRecordInfo) {
        this.orderServiceCostDetailRecordInfoDao.insert(orderServiceCostDetailRecordInfo);
        return orderServiceCostDetailRecordInfo;
    }

    /**
     * 修改数据
     *
     * @param orderServiceCostDetailRecordInfo 实例对象
     * @return 实例对象
     */
    @Override
    public OrderServiceCostDetailRecordInfo update(OrderServiceCostDetailRecordInfo orderServiceCostDetailRecordInfo) {
        this.orderServiceCostDetailRecordInfoDao.update(orderServiceCostDetailRecordInfo);
        return this.queryById(orderServiceCostDetailRecordInfo.getRecordId());
    }

    /**
     * 通过主键删除数据
     *
     * @param recordId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long recordId) {
        return this.orderServiceCostDetailRecordInfoDao.deleteById(recordId);
    }
}