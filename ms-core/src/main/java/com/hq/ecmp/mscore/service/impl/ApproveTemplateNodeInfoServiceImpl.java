package com.hq.ecmp.mscore.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.hq.api.system.domain.SysUser;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.StringUtils;
import com.hq.ecmp.constant.ApplyTypeEnum;
import com.hq.ecmp.constant.ApproveTypeEnum;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.AddFolwDTO;
import com.hq.ecmp.mscore.dto.FolwInfoDTO;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IApproveTemplateNodeInfoService;
import com.hq.ecmp.mscore.vo.ApprovalListVO;
import com.hq.ecmp.mscore.vo.ApprovalUserVO;
import com.hq.ecmp.mscore.vo.UserVO;
import com.hq.ecmp.util.SortListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hq.ecmp.constant.CommonConstant.ZERO;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
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
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;

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
    @Transactional
    public void addFlowTemplate(AddFolwDTO addFolwDTO,Long userId) throws Exception {
        ApproveTemplateInfo approveTemplateInfo = new ApproveTemplateInfo();
        approveTemplateInfo.setName(addFolwDTO.getName());
        approveTemplateInfo.setCreateBy(String.valueOf(userId));
        approveTemplateInfo.setCreateTime(new Date());
        int count = approveTemplateInfoMapper.insertApproveTemplateInfo(approveTemplateInfo);
        List<FolwInfoDTO> flowList = addFolwDTO.getFlowList();
        addTemeplateNode(flowList,approveTemplateInfo.getApproveTemplateId(),userId);
    }

    @Override
    @Transactional
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
        if (CollectionUtils.isNotEmpty(approveTemplateNodeInfos))
            approveTemplateNodeInfoMapper.deleteByTemplateId(addFolwDTO.getApproveTemplateId());
            log.info(addFolwDTO.getApproveTemplateId()+"审批模板节点删除成功!");

        List<FolwInfoDTO> flowList = addFolwDTO.getFlowList();
        //TODO 修改逻辑有缺失
        this.addTemeplateNode(flowList,addFolwDTO.getApproveTemplateId(),userId);
        log.info(addFolwDTO.getApproveTemplateId()+"审批模板及节点修改成功!");
    }

    @Override
    public List<ApprovalUserVO> getApprovalList(String regimeId, String projectId, SysUser user) throws Exception{
        RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(Long.parseLong(regimeId));
        if (regimeInfo==null){
            throw new Exception("用车制度:"+regimeId+"不存在");
        }
        List<ApproveTemplateNodeInfo> nodeInfos = approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoList(new ApproveTemplateNodeInfo(regimeInfo.getApproveTemplateId()));
        String userIds="";
        if (CollectionUtils.isNotEmpty(nodeInfos)){
            SortListUtil.sort(nodeInfos,"approveNodeId",SortListUtil.ASC);
            ApproveTemplateNodeInfo info=nodeInfos.get(0);
                if (ApproveTypeEnum.APPROVE_T001.getKey().equals(info.getApproverType())){//部门主管
                    UserVO deptLeader = getDeptLeader(user.getDeptId());
                    userIds=String.valueOf(deptLeader.getUserId());
                }else if (ApproveTypeEnum.APPROVE_T004.getKey().equals(info.getApproverType())){//项目负责人
                    if (StringUtils.isEmpty(projectId)){
                        return null;
                    }
                    UserVO projectLeader = getProjectLeader(Long.parseLong(projectId));
                    userIds=String.valueOf(projectLeader.getUserId());
                }else{
                    userIds=info.getUserId();
                }
            }
        return approveTemplateNodeInfoMapper.getApproveUsers(userIds);
    }

    private UserVO getDeptLeader(Long deptId)throws Exception{
        UserVO deptUser=ecmpUserMapper.findDeptLeader(deptId);
        if (deptUser!=null){
            return deptUser;
        }
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(deptId);
        if (ecmpOrg==null){
            throw new Exception("该部门/公司不存在");
        }
        String ancestors = ecmpOrg.getAncestors();
        if (StringUtils.isNotEmpty(ancestors)){
            String[] split = ancestors.split(",");
            for (int i=split.length-2;i>=0;i--){
                deptUser=ecmpUserMapper.findDeptLeader(Long.parseLong(split[i]));
                if (deptUser!=null){
                    return deptUser;
                }
            }
        }
        if (StringUtils.isEmpty(ecmpOrg.getLeader())){
            throw new Exception("该公司未设置领导层");
        }
        return deptUser;

    }

    private UserVO getProjectLeader(Long projectId) throws Exception{
        UserVO userVO=projectInfoMapper.findLeader(projectId);
        if (userVO!=null){
            return userVO;
        }
        ProjectInfo projectInfo = projectInfoMapper.selectProjectInfoById(projectId);
        if (projectInfo.getFatherProjectId()!=ZERO){
            userVO=projectInfoMapper.findLeader(projectInfo.getFatherProjectId());
        }
        if (userVO==null){
            EcmpUser user1 = ecmpUserMapper.selectEcmpUserById(Long.parseLong(projectInfo.getCreateBy()));
            userVO = getDeptLeader(user1.getDeptId());
        }
        return userVO;
    }

    private Boolean addTemeplateNode(List<FolwInfoDTO> flowList,Long approveTemplateId,Long userId)throws Exception{
        if (CollectionUtils.isEmpty(flowList)){
            throw new Exception("审批节点为空");
        }
        SortListUtil.sort(flowList, "number", SortListUtil.ASC);
        Long approveNodeId = 0l;
        for (int i = 0; i < flowList.size(); i++) {
            ApproveTemplateNodeInfo nodeInfo = new ApproveTemplateNodeInfo();
            nodeInfo.setApproverType(flowList.get(i).getType());
            nodeInfo.setApproveTemplateId(approveTemplateId);
            nodeInfo.setRoleId(flowList.get(i).getRoleIds());
            nodeInfo.setNextNodeId(String.valueOf(ZERO));
            nodeInfo.setCreateBy(String.valueOf(userId));
            nodeInfo.setCreateTime(new Date());
            if (ApproveTypeEnum.APPROVE_T002.getKey().equals(flowList.get(i).getType())) {
                String userIds = userRoleMapper.findUserIds(flowList.get(i).getRoleIds());
                nodeInfo.setUserId(userIds);
            } else {
                nodeInfo.setUserId(flowList.get(i).getUserIds());
            }
            approveTemplateNodeInfoMapper.insertApproveTemplateNodeInfo(nodeInfo);
            if (i == 0) {
                approveNodeId = nodeInfo.getApproveNodeId();
            } else if (i > 0) {
                approveTemplateNodeInfoMapper.updateApproveTemplateNodeInfo(new ApproveTemplateNodeInfo(approveNodeId, String.valueOf(nodeInfo.getApproveNodeId())));
                approveNodeId = nodeInfo.getApproveNodeId();
            }
        }
        return true;
    }
}
