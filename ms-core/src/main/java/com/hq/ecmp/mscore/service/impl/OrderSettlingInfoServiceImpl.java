package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.OrderSettlingInfo;
import com.hq.ecmp.mscore.mapper.OrderSettlingInfoMapper;
import com.hq.ecmp.mscore.service.IOrderSettlingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderSettlingInfoServiceImpl implements IOrderSettlingInfoService
{
    @Autowired
    private OrderSettlingInfoMapper orderSettlingInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param billId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderSettlingInfo selectOrderSettlingInfoById(Long billId)
    {
        return orderSettlingInfoMapper.selectOrderSettlingInfoById(billId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderSettlingInfo> selectOrderSettlingInfoList(OrderSettlingInfo orderSettlingInfo)
    {
        return orderSettlingInfoMapper.selectOrderSettlingInfoList(orderSettlingInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderSettlingInfo(OrderSettlingInfo orderSettlingInfo)
    {
        orderSettlingInfo.setCreateTime(DateUtils.getNowDate());
        return orderSettlingInfoMapper.insertOrderSettlingInfo(orderSettlingInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderSettlingInfo(OrderSettlingInfo orderSettlingInfo)
    {
        orderSettlingInfo.setUpdateTime(DateUtils.getNowDate());
        return orderSettlingInfoMapper.updateOrderSettlingInfo(orderSettlingInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param billIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderSettlingInfoByIds(Long[] billIds)
    {
        return orderSettlingInfoMapper.deleteOrderSettlingInfoByIds(billIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param billId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderSettlingInfoById(Long billId)
    {
        return orderSettlingInfoMapper.deleteOrderSettlingInfoById(billId);
    }
}
