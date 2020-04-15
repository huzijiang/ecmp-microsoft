package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.OrderAccountInfo;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.mapper.OrderAccountInfoMapper;
import com.hq.ecmp.mscore.service.IOrderAccountInfoService;
import com.hq.ecmp.mscore.vo.OrderAccountVO;
import com.hq.ecmp.mscore.vo.OrderAccountViewVO;
import com.hq.ecmp.mscore.vo.PageResult;
import com.hq.ecmp.util.DateFormatUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class OrderAccountInfoServiceImpl implements IOrderAccountInfoService
{
    @Autowired
    private OrderAccountInfoMapper orderAccountInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param accountId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public OrderAccountInfo selectOrderAccountInfoById(Long accountId)
    {
        return orderAccountInfoMapper.selectOrderAccountInfoById(accountId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param orderAccountInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<OrderAccountInfo> selectOrderAccountInfoList(OrderAccountInfo orderAccountInfo)
    {
        return orderAccountInfoMapper.selectOrderAccountInfoList(orderAccountInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param orderAccountInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertOrderAccountInfo(OrderAccountInfo orderAccountInfo)
    {
        orderAccountInfo.setCreateTime(DateUtils.getNowDate());
        return orderAccountInfoMapper.insertOrderAccountInfo(orderAccountInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param orderAccountInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateOrderAccountInfo(OrderAccountInfo orderAccountInfo)
    {
        orderAccountInfo.setUpdateTime(DateUtils.getNowDate());
        return orderAccountInfoMapper.updateOrderAccountInfo(orderAccountInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param accountIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderAccountInfoByIds(Long[] accountIds)
    {
        return orderAccountInfoMapper.deleteOrderAccountInfoByIds(accountIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param accountId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteOrderAccountInfoById(Long accountId)
    {
        return orderAccountInfoMapper.deleteOrderAccountInfoById(accountId);
    }

    @Override
    public List<OrderAccountVO> getAccountList() {
        List<OrderAccountVO> accountList = orderAccountInfoMapper.getAccountList();
        if (CollectionUtils.isNotEmpty(accountList)){
            for (OrderAccountVO vo:accountList){
             //   String beginMonth=vo.getAccountDate()+"-01";
             //   String endMonth= DateFormatUtils.getLastDayOfMonth(DateFormatUtils.parseDate(DateFormatUtils.DATE_FORMAT,beginMonth));
                String beginMonth=vo.getBeginDate();
                String endMonth=vo.getEndDate();
                String desc=beginMonth+"至"+endMonth+" "+vo.getTotal()+ "元 (未开票)";
                vo.setDesc(desc);
            }
        }
        return accountList;
    }
    @Override
    public PageResult<OrderAccountViewVO> getAccountViewList( PageRequest pageRequest){
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<OrderAccountViewVO> accountViewList = orderAccountInfoMapper.getAccountViewList();
        if(CollectionUtils.isNotEmpty(accountViewList)){
            for (OrderAccountViewVO vo:accountViewList){
                String beginMonth= vo.getAccountDate()+"-01";
                String endMonth= DateFormatUtils.getLastDayOfMonth(DateFormatUtils.parseDate(DateFormatUtils.DATE_FORMAT,beginMonth));
                String desc=beginMonth+"至"+endMonth;
                vo.setDesc(desc);
            }
        }
        Long count=orderAccountInfoMapper.getAccountViewListCount();
        return new PageResult<>(count,accountViewList);
    }
}
