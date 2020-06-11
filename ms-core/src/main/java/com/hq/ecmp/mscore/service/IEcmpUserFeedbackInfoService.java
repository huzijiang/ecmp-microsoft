package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfo;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackInfoVo;
import com.hq.ecmp.mscore.domain.EcmpUserFeedbackVo;
import com.hq.ecmp.mscore.dto.OrderEvaluationDto;
import com.hq.ecmp.mscore.dto.OrderInfoDTO;
import com.hq.ecmp.mscore.vo.PageResult;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpUserFeedbackInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param feedbackId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public EcmpUserFeedbackInfo selectEcmpUserFeedbackInfoById(Long feedbackId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EcmpUserFeedbackInfo> selectEcmpUserFeedbackInfoList(EcmpUserFeedbackInfo ecmpUserFeedbackInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertEcmpUserFeedbackInfo(EcmpUserFeedbackInfo ecmpUserFeedbackInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param ecmpUserFeedbackInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateEcmpUserFeedbackInfo(EcmpUserFeedbackInfo ecmpUserFeedbackInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param feedbackIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpUserFeedbackInfoByIds(Long[] feedbackIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param feedbackId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEcmpUserFeedbackInfoById(Long feedbackId);

    Long saveOrderEvaluation(OrderEvaluationDto evaluationDto, Long userId) throws Exception;

    /**
     * 异议订单
     * @param ecmpUserFeedbackInfo
     * @return
     */
    PageResult<EcmpUserFeedbackInfoVo> getObjectionOrderList(EcmpUserFeedbackInfoVo ecmpUserFeedbackInfo);

    /**
     * 回复异议订单
     * @param ecmpUserFeedbackInfo
     */
    int replyObjectionOrder(EcmpUserFeedbackInfoVo ecmpUserFeedbackInfo,Long userId);

    /**
     * 订单管理补单提交功能
     * @param orderInfoDTO
     * @return
     */
    ApiResponse supplementSubmit(OrderInfoDTO orderInfoDTO);

    int updateFeedback(EcmpUserFeedbackInfoVo feedBackDto);

    PageResult<EcmpUserFeedbackVo> findFeedback(EcmpUserFeedbackInfo ecmpUserFeedbackInfo);


}
