package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.OrderSettlingInfo;
import com.hq.ecmp.mscore.domain.OrderSettlingInfoVo;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IOrderSettlingInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param billId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public OrderSettlingInfo selectOrderSettlingInfoById(Long billId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<OrderSettlingInfo> selectOrderSettlingInfoList(OrderSettlingInfo orderSettlingInfo);

    /**
     * 新增【自有车结算调用接口】
     *
     * @param orderSettlingInfo 【记录实际里程和时长】
     * @return 结果
     */
    public int insertOrderSettlingInfo(OrderSettlingInfo orderSettlingInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param orderSettlingInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateOrderSettlingInfo(OrderSettlingInfo orderSettlingInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param billIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderSettlingInfoByIds(Long[] billIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param billId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteOrderSettlingInfoById(Long billId);

    /**
     * 司机端费用上报提交
     * @param orderSettlingInfoVo
     * @param userId
     * @param companyId
     */
    int addExpenseReport(OrderSettlingInfoVo orderSettlingInfoVo, Long userId,Long companyId) throws ParseException;

    /**
     * 格式化自有车费用
     * @param orderSettlingInfoVo
     * @param personalCancellationFee
     * @param enterpriseCancellationFee
     * @return
     */
    String formatCostFee(OrderSettlingInfoVo orderSettlingInfoVo, BigDecimal personalCancellationFee, BigDecimal enterpriseCancellationFee);

    BigDecimal getAllFeeAmount(OrderSettlingInfoVo orderSettlingInfoVo, Long userId, Long companyId) throws ParseException;
}
