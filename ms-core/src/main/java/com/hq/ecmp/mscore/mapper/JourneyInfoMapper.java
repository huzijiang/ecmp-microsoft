package com.hq.ecmp.mscore.mapper;

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
    int getJourneyListCount(@Param("userId")Long userId);
}
