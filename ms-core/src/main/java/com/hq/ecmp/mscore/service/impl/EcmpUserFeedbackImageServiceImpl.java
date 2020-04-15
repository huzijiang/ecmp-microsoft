package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackImage;
import com.hq.ecmp.mscore.mapper.EcmpUserFeedbackImageMapper;
import com.hq.ecmp.mscore.service.IEcmpUserFeedbackImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpUserFeedbackImageServiceImpl implements IEcmpUserFeedbackImageService
{
    @Autowired
    private EcmpUserFeedbackImageMapper ecmpUserFeedbackImageMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param imageId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public EcmpUserFeedbackImage selectEcmpUserFeedbackImageById(Long imageId)
    {
        return ecmpUserFeedbackImageMapper.selectEcmpUserFeedbackImageById(imageId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpUserFeedbackImage 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<EcmpUserFeedbackImage> selectEcmpUserFeedbackImageList(EcmpUserFeedbackImage ecmpUserFeedbackImage)
    {
        return ecmpUserFeedbackImageMapper.selectEcmpUserFeedbackImageList(ecmpUserFeedbackImage);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpUserFeedbackImage 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertEcmpUserFeedbackImage(EcmpUserFeedbackImage ecmpUserFeedbackImage)
    {
        ecmpUserFeedbackImage.setCreateTime(DateUtils.getNowDate());
        return ecmpUserFeedbackImageMapper.insertEcmpUserFeedbackImage(ecmpUserFeedbackImage);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpUserFeedbackImage 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateEcmpUserFeedbackImage(EcmpUserFeedbackImage ecmpUserFeedbackImage)
    {
        ecmpUserFeedbackImage.setUpdateTime(DateUtils.getNowDate());
        return ecmpUserFeedbackImageMapper.updateEcmpUserFeedbackImage(ecmpUserFeedbackImage);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param imageIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserFeedbackImageByIds(Long[] imageIds)
    {
        return ecmpUserFeedbackImageMapper.deleteEcmpUserFeedbackImageByIds(imageIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param imageId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEcmpUserFeedbackImageById(Long imageId)
    {
        return ecmpUserFeedbackImageMapper.deleteEcmpUserFeedbackImageById(imageId);
    }
}
