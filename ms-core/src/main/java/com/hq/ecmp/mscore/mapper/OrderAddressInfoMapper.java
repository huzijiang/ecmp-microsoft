package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderAddressInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-03-16
 */
public interface OrderAddressInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param orderAddressId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderAddressInfo selectOrderAddressInfoById(Long orderAddressId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param orderAddressInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderAddressInfo> selectOrderAddressInfoList(OrderAddressInfo orderAddressInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param orderAddressInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderAddressInfo(OrderAddressInfo orderAddressInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param orderAddressInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderAddressInfo(OrderAddressInfo orderAddressInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param orderAddressId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderAddressInfoById(Long orderAddressId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param orderAddressIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderAddressInfoByIds(Long[] orderAddressIds);
}
