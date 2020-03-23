package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.OrderAccountInfo;
import com.hq.ecmp.mscore.vo.OrderAccountVO;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface OrderAccountInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param accountId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderAccountInfo selectOrderAccountInfoById(Long accountId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderAccountInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderAccountInfo> selectOrderAccountInfoList(OrderAccountInfo orderAccountInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param orderAccountInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertOrderAccountInfo(OrderAccountInfo orderAccountInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderAccountInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderAccountInfo(OrderAccountInfo orderAccountInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param accountId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderAccountInfoById(Long accountId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param accountIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteOrderAccountInfoByIds(Long[] accountIds);

    List<OrderAccountVO> getAccountList(String state);
}
