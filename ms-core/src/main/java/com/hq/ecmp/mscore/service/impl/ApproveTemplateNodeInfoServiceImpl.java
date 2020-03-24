package com.hq.ecmp.mscore.service.impl;

import java.util.*;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.ApplyTypeEnum;
import com.hq.ecmp.constant.ApproveTypeEnum;
import com.hq.ecmp.mscore.domain.ApproveTemplateInfo;
import com.hq.ecmp.mscore.domain.ApproveTemplateNodeInfo;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.dto.AddFolwDTO;
import com.hq.ecmp.mscore.dto.FolwInfoDTO;
import com.hq.ecmp.mscore.mapper.ApproveTemplateInfoMapper;
import com.hq.ecmp.mscore.mapper.ApproveTemplateNodeInfoMapper;
import com.hq.ecmp.mscore.mapper.EcmpUserRoleMapper;
import com.hq.ecmp.mscore.mapper.RegimeInfoMapper;
import com.hq.ecmp.mscore.service.IApproveTemplateNodeInfoService;
import com.hq.ecmp.mscore.vo.ApprovalListVO;
import com.hq.ecmp.mscore.vo.ApprovalUserVO;
import com.hq.ecmp.util.SortListUtil;
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
public class ApproveTemplateNodeInfoServiceImpl implements IApproveTemplateNodeInfoService
{
    @Autowired
    private ApproveTemplateNodeInfoMapper approveTemplateNodeInfoMapper;
    @Autowired
    private ApproveTemplateInfoMapper approveTemplateInfoMapper;
    @Autowired
    private EcmpUserRoleMapper userRoleMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param approveNodeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ApproveTemplateNodeInfo selectApproveTemplateNodeInfoById(Long approveNodeId)
    {
        return approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoById(approveNodeId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param approveTemplateNodeInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ApproveTemplateNodeInfo> selectApproveTemplateNodeInfoList(ApproveTemplateNodeInfo approveTemplateNodeInfo)
    {
        return approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoList(approveTemplateNodeInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param approveTemplateNodeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertApproveTemplateNodeInfo(ApproveTemplateNodeInfo approveTemplateNodeInfo)
    {
        approveTemplateNodeInfo.setCreateTime(DateUtils.getNowDate());
        return approveTemplateNodeInfoMapper.insertApproveTemplateNodeInfo(approveTemplateNodeInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param approveTemplateNodeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateApproveTemplateNodeInfo(ApproveTemplateNodeInfo approveTemplateNodeInfo)
    {
        approveTemplateNodeInfo.setUpdateTime(DateUtils.getNowDate());
        return approveTemplateNodeInfoMapper.updateApproveTemplateNodeInfo(approveTemplateNodeInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveNodeIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApproveTemplateNodeInfoByIds(Long[] approveNodeIds)
    {
        return approveTemplateNodeInfoMapper.deleteApproveTemplateNodeInfoByIds(approveNodeIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param approveNodeId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApproveTemplateNodeInfoById(Long approveNodeId)
    {
        return approveTemplateNodeInfoMapper.deleteApproveTemplateNodeInfoById(approveNodeId);
    }

    @Override
    public String getListByNodeIds(List<Long> nodeIds) {

        return approveTemplateNodeInfoMapper.getListByNodeIds(nodeIds);
    }

    @Override
    public void addFlowTemplate(AddFolwDTO addFolwDTO,Long userId) throws Exception {
        ApproveTemplateInfo approveTemplateInfo = new ApproveTemplateInfo();
        approveTemplateInfo.setName(addFolwDTO.getName());
        approveTemplateInfo.setCreateBy(String.valueOf(userId));
        approveTemplateInfo.setCreateTime(new Date());
        int count = approveTemplateInfoMapper.insertApproveTemplateInfo(approveTemplateInfo);
        List<FolwInfoDTO> flowList = addFolwDTO.getFlowList();
        if (CollectionUtils.isNotEmpty(flowList)){
            SortListUtil.sort(flowList, "number", SortListUtil.DESC);
            String nextNodeId="0";
            for (int i=0;i<flowList.size();i++){
                ApproveTemplateNodeInfo nodeInfo=new ApproveTemplateNodeInfo();
                nodeInfo.setApproverType(flowList.get(i).getType());
                nodeInfo.setApproveTemplateId(approveTemplateInfo.getApproveTemplateId());
                nodeInfo.setRoleId(flowList.get(i).getRoleIds());
                nodeInfo.setNextNodeId(nextNodeId);
                nodeInfo.setCreateBy(String.valueOf(userId));
                nodeInfo.setCreateTime(new Date());
                if (ApproveTypeEnum.APPROVE_T002.getKey().equals(flowList.get(i).getType())){
                    String userIds=userRoleMapper.findUserIds(flowList.get(i).getRoleIds());
                    nodeInfo.setUserId(userIds);
                }else{
                    nodeInfo.setUserId(flowList.get(i).getUserIds());
                }
                approveTemplateNodeInfoMapper.insertApproveTemplateNodeInfo(nodeInfo);
                nextNodeId=String.valueOf(nodeInfo.getApproveNodeId());
            }
        }
    }

    @Override
    public void editFlowTemplate(AddFolwDTO addFolwDTO,Long userId) throws Exception {
        ApproveTemplateInfo approveTemplateInfo = approveTemplateInfoMapper.selectApproveTemplateInfoById(addFolwDTO.getApproveTemplateId());
        if (approveTemplateInfo==null){
            throw new Exception("无此模板审批流");
        }
        if (!approveTemplateInfo.getName().equals(addFolwDTO.getName())){
            approveTemplateInfo.setName(addFolwDTO.getName());
            approveTemplateInfo.setUpdateBy(String.valueOf(userId));
            approveTemplateInfo.setUpdateTime(new Date());
            approveTemplateInfoMapper.updateApproveTemplateInfo(approveTemplateInfo);
        }
        List<ApproveTemplateNodeInfo> approveTemplateNodeInfos = approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoList(new ApproveTemplateNodeInfo(addFolwDTO.getApproveTemplateId()));

        List<FolwInfoDTO> flowList = addFolwDTO.getFlowList();
        if (CollectionUtils.isNotEmpty(flowList)){
            SortListUtil.sort(flowList, "number", SortListUtil.DESC);

        }

    }

    @Override
    public List<ApprovalUserVO> getApprovalList(Long regimeId) {
        RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(regimeId);
        if (regimeInfo==null){
            return null;
        }
        String allApproveUserId = approveTemplateNodeInfoMapper.getAllApproveUserId(regimeInfo.getApproveTemplateId());
        String trim = allApproveUserId.trim();
        return approveTemplateNodeInfoMapper.getApproveUsers(trim);
    }
}
