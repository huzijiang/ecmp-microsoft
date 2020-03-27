package com.hq.ecmp.mscore.service.impl;

import java.util.*;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.ApproveStateEnum;
import com.hq.ecmp.constant.ApproveTypeEnum;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IApplyApproveResultInfoService;
import com.hq.ecmp.mscore.vo.ApprovalInfoVO;
import com.hq.ecmp.mscore.vo.UserVO;
import com.hq.ecmp.util.SortListUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private ApplyInfoMapper applyInfoMapper;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;
    @Autowired
    private EcmpUserRoleMapper userRoleMapper;

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

//    @Override
//    public void initApproveResultInfo(Long applyId,Long regimenId,Long userId) {
//        //查询审批模板
//        RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(regimenId);
//        if (regimeInfo!=null){
//            List<ApproveTemplateNodeInfo> approveTemplateNodeInfos = approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoList(new ApproveTemplateNodeInfo(regimeInfo.getApproveTemplateId()));
//            Collections.sort(approveTemplateNodeInfos, new Comparator<ApproveTemplateNodeInfo>() {
//                @Override
//                public int compare(ApproveTemplateNodeInfo o1, ApproveTemplateNodeInfo o2) {
//                    int i = o1.getApproveNodeId().intValue() - o2.getApproveNodeId().intValue();
//                    if(i == 0){
//                        return o1.getApproveNodeId().intValue() - o2.getApproveNodeId().intValue();
//                    }
//                    return i;
//                }
//            });
//            if (CollectionUtils.isNotEmpty(approveTemplateNodeInfos)){
//                for (int i=0;i<approveTemplateNodeInfos.size();i++ ){
//                    ApproveTemplateNodeInfo info = approveTemplateNodeInfos.get(i);
//                    EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(info.getUserId());
//                    ApplyApproveResultInfo resultInfo=new ApplyApproveResultInfo(applyId,regimeInfo.getApproveTemplateId(),info.getApproveNodeId(),ecmpUser.getUserName(),ecmpUser.getPhonenumber());
//                    String state= ApproveStateEnum.NOT_ARRIVED_STATE.getKey();
//                    if (i==0){
//                        state=ApproveStateEnum.WAIT_APPROVE_STATE.getKey();
//                    }
//                    resultInfo.setState(state);
//                    resultInfo.setCreateBy(String.valueOf(userId));
//                    resultInfo.setCreateTime(new Date());
//                    applyApproveResultInfoMapper.insertApplyApproveResultInfo(resultInfo);
//                }
//            }
//        }
//    }

    @Override
    public List<ApprovalInfoVO> getApproveResultList(ApplyApproveResultInfo applyApproveResultInfo) {
        return applyApproveResultInfoMapper.getApproveResultList(applyApproveResultInfo.getApplyId(),applyApproveResultInfo.getApproveTemplateId());
    }

    @Override
    public List<ApplyApproveResultInfo> selectApproveResultByNodeids(String nextNodeId,String state) {
        return applyApproveResultInfoMapper.selectApproveResultByNodeids(nextNodeId,state);
    }

    @Override
    public List<ApplyApproveResultInfo> selectByUserId(Long applyId, Long userId,String state) {
        return applyApproveResultInfoMapper.selectByUserId(applyId,userId,state);
    }

    /**
     * 初始化审批流
     * @param applyId 申请id
     * @param regimenId 用车制度id
     * @param userId 登录人id
     */
    @Override
    public void initApproveResultInfo(Long applyId,Long regimenId,Long userId) {
        //查询审批模板
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(regimenId);
        if (regimeInfo!=null){
            List<ApproveTemplateNodeInfo> approveTemplateNodeInfos = approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoList(new ApproveTemplateNodeInfo(regimeInfo.getApproveTemplateId()));
            SortListUtil.sort(approveTemplateNodeInfos,"approveNodeId",SortListUtil.ASC);
            if (CollectionUtils.isNotEmpty(approveTemplateNodeInfos)){
                for (int i=0;i<approveTemplateNodeInfos.size();i++ ){
                    ApproveTemplateNodeInfo info = approveTemplateNodeInfos.get(i);
                    ApplyApproveResultInfo resultInfo=new ApplyApproveResultInfo(applyId,regimeInfo.getApproveTemplateId(),info.getApproveNodeId(),info.getApproverType(),info.getNextNodeId());
                    resultInfo.setCreateTime(new Date());
                    resultInfo.setCreateBy(String.valueOf(userId));
                    String state= ApproveStateEnum.NOT_ARRIVED_STATE.getKey();
                    if (i==0){
                        state=ApproveStateEnum.WAIT_APPROVE_STATE.getKey();
                    }
                    resultInfo.setState(state);
                    switch (ApproveTypeEnum.format(info.getApproverType())) {
                        case  APPROVE_T001://部门负责人
                            UserVO deptUser=ecmpUserMapper.findDeptLeader(Long.parseLong(applyInfo.getCreateBy()));
                            resultInfo.setApproveUserId(String.valueOf(deptUser.getUserId()));
                            break;
                        case  APPROVE_T002://指定角色
                            resultInfo.setApproveRoleId(info.getRoleId());
                            String userIds=userRoleMapper.findUserIds(info.getRoleId());
                            resultInfo.setApproveUserId(userIds);
                            break;
                        case  APPROVE_T003://指定多个员工
                            resultInfo.setApproveUserId(info.getUserId());
                            break;
                        case  APPROVE_T004://项目负责人
                            UserVO userVO=projectInfoMapper.findLeader(applyInfo.getProjectId());
                            resultInfo.setApproveUserId(String.valueOf(userVO.getUserId()));
                            break;
                        }
                    applyApproveResultInfoMapper.insertApplyApproveResultInfo(resultInfo);
                }
            }
        }
    }

}
