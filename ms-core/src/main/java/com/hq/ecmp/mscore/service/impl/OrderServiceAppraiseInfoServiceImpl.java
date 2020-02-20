package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.OrderServiceAppraiseInfo;
import com.hq.ecmp.mscore.mapper.OrderServiceAppraiseInfoMapper;
import com.hq.ecmp.mscore.service.IOrderServiceAppraiseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderServiceAppraiseInfoServiceImpl implements IOrderServiceAppraiseInfoService
{
    @Autowired
    private OrderServiceAppraiseInfoMapper orderServiceAppraiseInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param appraiseId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderServiceAppraiseInfo selectOrderServiceAppraiseInfoById(Long appraiseId)
    {
        return orderServiceAppraiseInfoMapper.selectOrderServiceAppraiseInfoById(appraiseId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderServiceAppraiseInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderServiceAppraiseInfo> selectOrderServiceAppraiseInfoList(OrderServiceAppraiseInfo orderServiceAppraiseInfo)
    {
        return orderServiceAppraiseInfoMapper.selectOrderServiceAppraiseInfoList(orderServiceAppraiseInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderServiceAppraiseInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderServiceAppraiseInfo(OrderServiceAppraiseInfo orderServiceAppraiseInfo)
    {
        orderServiceAppraiseInfo.setCreateTime(DateUtils.getNowDate());
        return orderServiceAppraiseInfoMapper.insertOrderServiceAppraiseInfo(orderServiceAppraiseInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderServiceAppraiseInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderServiceAppraiseInfo(OrderServiceAppraiseInfo orderServiceAppraiseInfo)
    {
        orderServiceAppraiseInfo.setUpdateTime(DateUtils.getNowDate());
        return orderServiceAppraiseInfoMapper.updateOrderServiceAppraiseInfo(orderServiceAppraiseInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param appraiseIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderServiceAppraiseInfoByIds(Long[] appraiseIds)
    {
        return orderServiceAppraiseInfoMapper.deleteOrderServiceAppraiseInfoByIds(appraiseIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param appraiseId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderServiceAppraiseInfoById(Long appraiseId)
    {
        return orderServiceAppraiseInfoMapper.deleteOrderServiceAppraiseInfoById(appraiseId);
    }
}
