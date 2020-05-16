package com.hq.ecmp.mscore.service;

import java.util.List;

import com.hq.ecmp.mscore.bo.InvoiceAbleItineraryData;
import com.hq.ecmp.mscore.bo.JourneyBeingEndDate;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.vo.JourneyDetailVO;
import com.hq.ecmp.mscore.vo.JourneyVO;
import com.hq.ecmp.mscore.vo.OrderVO;
import lombok.Data;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */

public interface IJourneyInfoService {
    /**
     * 查询【请填写功能名称】
     *
     * @param journeyId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public JourneyInfo selectJourneyInfoById(Long journeyId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<JourneyInfo> selectJourneyInfoList(JourneyInfo journeyInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertJourneyInfo(JourneyInfo journeyInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateJourneyInfo(JourneyInfo journeyInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param journeyIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyInfoByIds(Long[] journeyIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param journeyId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyInfoById(Long journeyId);

    public List<CarAuthorityInfo> getUserCarAuthorityList(Long userId);

    /**
     * 获取指定差旅行程下所有行程节点生成的用车权限
     *
     * @param // * @param journeyId
     * @return
     */
    public List<UserAuthorityGroupCity> getUserCarAuthority(Long journeyId);

    MessageDto getJourneyMessage(Long userId);

    /**
     * 获取正在进行中的行程
     *
     * @param userId
     * @return list
     */
    public List<JourneyVO> getJourneyList(Long userId);

    /**
     * 获取正在进行中的行程个数
     *
     * @param userId
     * @return 个数
     */
    public int getJourneyListCount(Long userId);

    JourneyDetailVO getItineraryDetail(Long powerId) throws Exception ;
    /**
     * 判断是否有正在进行中的行程
     *
     * @param userId
     * @return 个数
     */
    public int getWhetherJourney(Long userId);

    /**
     * 通过行程节点计算行程的实际可用时间
     * @param journeyNodeInfo
     * @return
     */
    public JourneyBeingEndDate getValidDateByJourneyNodeId(JourneyNodeInfo journeyNodeInfo, RegimeInfo regimeInfo);


    /***
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<InvoiceAbleItineraryData> getInvoiceAbleItinerary(Long userId,int pageNum, int pageSize)throws Exception;


    /***
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<InvoiceAbleItineraryData> getInvoiceAbleItineraryHistory(Long userId)throws Exception;


    /***
     *
     * @param invoiceId
     * @return
     * @throws Exception
     */
    Integer getInvoiceItineraryCount(Long invoiceId)throws Exception;


}