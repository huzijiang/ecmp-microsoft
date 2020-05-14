package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.bo.InvoiceAbleItineraryData;
import com.hq.ecmp.mscore.domain.JourneyInfo;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.vo.JourneyVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface JourneyInfoMapper
{
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
     * 新增行程信息
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
     * 删除【请填写功能名称】
     *
     * @param journeyId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyInfoById(Long journeyId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param journeyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteJourneyInfoByIds(Long[] journeyIds);

    MessageDto getJourneyMessage(Long userId);

    /**
     * 获取正在进行中的行程
     * @param userId
     * @return list
     */
    public List<JourneyVO> getJourneyList(Long userId);

    /**
     * 获取正在进行中的行程个数
     * @param userId
     * @return 个数
     */
    int getJourneyListCount(Long userId);

    /**
     * 查询用户审核通过的用车行程
     * @param userId
     * @return
     */
    List<JourneyInfo> queryPassJourneyList(Long userId);

    String selectTitleById(Long journeyId);
    /**
     * 判断是否有正在进行中的行程
     *
     * @param userId
     * @return 个数
     */
    public int getWhetherJourney(Long userId);


    /***
     * add by liuz 可以开发票的行程数据，或已开发票的行程数据
     * @param userId
     * @return
     * @throws Exception
     */
    List<InvoiceAbleItineraryData> getInvoiceAbleItinerary(Long userId)throws Exception;


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

    /***
     *
     * @param accountId
     * @return
     */
    List<InvoiceAbleItineraryData> getInvoiceAbleItineraryHistoryKey(Long accountId)throws Exception;
}
