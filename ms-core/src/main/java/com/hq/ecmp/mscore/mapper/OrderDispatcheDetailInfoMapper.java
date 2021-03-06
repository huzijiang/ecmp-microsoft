package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderDispatcheDetailInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-05-25
 */
@Repository
public interface OrderDispatcheDetailInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param dispatchId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderDispatcheDetailInfo selectOrderDispatcheDetailInfoById(Integer dispatchId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param orderDispatcheDetailInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderDispatcheDetailInfo> selectOrderDispatcheDetailInfoList(OrderDispatcheDetailInfo orderDispatcheDetailInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param orderDispatcheDetailInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderDispatcheDetailInfo(OrderDispatcheDetailInfo orderDispatcheDetailInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param orderDispatcheDetailInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderDispatcheDetailInfo(OrderDispatcheDetailInfo orderDispatcheDetailInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderDispatcheDetailInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderDispatcheDetailInfoByOrderId(OrderDispatcheDetailInfo orderDispatcheDetailInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param dispatchId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderDispatcheDetailInfoById(Integer dispatchId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param dispatchIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderDispatcheDetailInfoByIds(Integer[] dispatchIds);

    /***
     *
     * @param orderId
     * @return
     */
    int deleteOrderId(@Param("orderId")Long orderId);

    /**
     * 还原订单调度表的信息为最初申请时的状态
     * @param orderId
     * @param updateBy
     * @param updateTime
     */
    void revertOrderDispatcheDetailInfoByOrderId(@Param("orderId") Long orderId, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    OrderDispatcheDetailInfo selectDispatcheInfo(@Param("orderId") Long orderId);

    Map<String,String> selectGroupInfo(@Param("orderId") Long orderId);

    /**
     * 查询派车备注
     * @param orderId
     * @return
     */
    String selectDispatchRemark(@Param("orderId") Long orderId);
}