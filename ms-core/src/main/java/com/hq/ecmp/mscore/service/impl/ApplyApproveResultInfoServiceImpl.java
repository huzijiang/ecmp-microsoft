package com.hq.ecmp.mscore.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.ApproveStateEnum;
import com.hq.ecmp.mscore.domain.ApplyApproveResultInfo;
import com.hq.ecmp.mscore.domain.ApproveTemplateNodeInfo;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IApplyApproveResultInfoService;
import com.hq.ecmp.mscore.vo.ApprovalInfoVO;
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
public class ApplyApproveResultInfoServiceImpl implements IApplyApproveResultInfoService
{
    @Autowired
    private ApplyApproveResultInfoMapper applyApproveResultInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;
    @Autowired
    private ApproveTemplateInfoMapper approveTemplateInfoMapper;
    @Autowired
    private ApproveTemplateNodeInfoMapper approveTemplateNodeInfoMapper;
    @Autowired
    private EcmpUserMapper ecmpUserMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param approveResultId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ApplyApproveResultInfo selectApplyApproveResultInfoById(Long approveResultId)
    {
        return applyApproveResultInfoMapper.selectApplyApproveResultInfoById(approveResultId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ApplyApproveResultInfo> selectApplyApproveResultInfoList(ApplyApproveResultInfo applyApproveResultInfo)
    {
        return applyApproveResultInfoMapper.selectApplyApproveResultInfoList(applyApproveResultInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertApplyApproveResultInfo(ApplyApproveResultInfo applyApproveResultInfo)
    {
        applyApproveResultInfo.setCreateTime(DateUtils.getNowDate());
        return applyApproveResultInfoMapper.insertApplyApproveResultInfo(applyApproveResultInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param applyApproveResultInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateApplyApproveResultInfo(ApplyApproveResultInfo applyApproveResultInfo)
    {
        applyApproveResultInfo.setUpdateTime(DateUtils.getNowDate());
        return applyApproveResultInfoMapper.updateApplyApproveResultInfo(applyApproveResultInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveResultIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApplyApproveResultInfoByIds(Long[] approveResultIds)
    {
        return applyApproveResultInfoMapper.deleteApplyApproveResultInfoByIds(approveResultIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param approveResultId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApplyApproveResultInfoById(Long approveResultId)
    {
        return applyApproveResultInfoMapper.deleteApplyApproveResultInfoById(approveResultId);
    }

    @Override
    public MessageDto getApproveMessage(Long userId) {
        return applyApproveResultInfoMapper.getApproveMessage(userId);
    }

    @Override
    public void initApproveResultInfo(Long applyId,Long regimenId,Long userId) {
        //查询审批模板
        RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(regimenId);
        if (regimeInfo!=null){
            List<ApproveTemplateNodeInfo> approveTemplateNodeInfos = approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoList(new ApproveTemplateNodeInfo(regimeInfo.getApproveTemplateId()));
            Collections.sort(approveTemplateNodeInfos, new Comparator<ApproveTemplateNodeInfo>() {
                @Override
                public int compare(ApproveTemplateNodeInfo o1, ApproveTemplateNodeInfo o2) {
                    int i = o1.getApproveNodeId().intValue() - o2.getApproveNodeId().intValue();
                    if(i == 0){
                        return o1.getApproveNodeId().intValue() - o2.getApproveNodeId().intValue();
                    }
                    return i;
                }
            });
            if (CollectionUtils.isNotEmpty(approveTemplateNodeInfos)){
                for (int i=0;i<approveTemplateNodeInfos.size();i++ ){
                    ApproveTemplateNodeInfo info = approveTemplateNodeInfos.get(i);
                    EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(info.getUserId());
                    ApplyApproveResultInfo resultInfo=new ApplyApproveResultInfo(applyId,regimeInfo.getApproveTemplateId(),info.getApproveNodeId(),ecmpUser.getUserName(),ecmpUser.getPhonenumber());
                    String state= ApproveStateEnum.NOT_ARRIVED_STATE.getKey();
                    if (i==0){
                        state=ApproveStateEnum.WAIT_APPROVE_STATE.getKey();
                    }
                    resultInfo.setState(state);
                    resultInfo.setCreateBy(String.valueOf(userId));
                    resultInfo.setCreateTime(new Date());
                    applyApproveResultInfoMapper.insertApplyApproveResultInfo(resultInfo);
                }
            }
        }
    }

    @Override
    public List<ApprovalInfoVO> getApproveResultList(ApplyApproveResultInfo applyApproveResultInfo) {
        return applyApproveResultInfoMapper.getApproveResultList(applyApproveResultInfo.getApplyId(),applyApproveResultInfo.getApproveTemplateId());
    }

    @Override
    public List<ApplyApproveResultInfo> selectApproveResultByNodeids(String nextNodeId,String state) {
        return applyApproveResultInfoMapper.selectApproveResultByNodeids(nextNodeId,state);
    }
}
