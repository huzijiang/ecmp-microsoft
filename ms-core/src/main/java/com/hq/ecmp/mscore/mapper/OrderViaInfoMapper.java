package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderViaInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-03-12
 */
public interface OrderViaInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param viaId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderViaInfo selectOrderViaInfoById(Long viaId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param orderViaInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderViaInfo> selectOrderViaInfoList(OrderViaInfo orderViaInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param orderViaInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderViaInfo(OrderViaInfo orderViaInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param orderViaInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderViaInfo(OrderViaInfo orderViaInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param viaId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderViaInfoById(Long viaId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param viaIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderViaInfoByIds(Long[] viaIds);

    /**
     * 批量插入
     * @param list
     */
    public void insertOrderViaInfoBatch(List<OrderViaInfo> list);
}
