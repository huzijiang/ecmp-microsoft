package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpUserFeedbackImage;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpUserFeedbackImageService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param imageId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public EcmpUserFeedbackImage selectEcmpUserFeedbackImageById(Long imageId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpUserFeedbackImage 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EcmpUserFeedbackImage> selectEcmpUserFeedbackImageList(EcmpUserFeedbackImage ecmpUserFeedbackImage);

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpUserFeedbackImage 【请填写功能名称】
     * @return 结果
     */
    public int insertEcmpUserFeedbackImage(EcmpUserFeedbackImage ecmpUserFeedbackImage);

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpUserFeedbackImage 【请填写功能名称】
     * @return 结果
     */
    public int updateEcmpUserFeedbackImage(EcmpUserFeedbackImage ecmpUserFeedbackImage);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param imageIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpUserFeedbackImageByIds(Long[] imageIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param imageId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpUserFeedbackImageById(Long imageId);
}
