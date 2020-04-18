package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.pagehelper.PageHelper;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.ApproveTypeEnum;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.IApproveTemplateInfoService;
import com.hq.ecmp.mscore.vo.ApprovaTemplateNodeVO;
import com.hq.ecmp.mscore.vo.ApprovaTemplateVO;
import com.hq.ecmp.util.SortListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.log.LogInputStream;

import javax.annotation.Resource;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class ApproveTemplateInfoServiceImpl implements IApproveTemplateInfoService
{
    @Autowired
    private ApproveTemplateInfoMapper approveTemplateInfoMapper;
    @Autowired
    private ApproveTemplateNodeInfoMapper approveTemplateNodeInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Resource
    private EcmpRoleMapper roleMapper;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param approveTemplateId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public ApproveTemplateInfo selectApproveTemplateInfoById(Long approveTemplateId)
    {
        return approveTemplateInfoMapper.selectApproveTemplateInfoById(approveTemplateId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param approveTemplateInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<ApproveTemplateInfo> selectApproveTemplateInfoList(ApproveTemplateInfo approveTemplateInfo)
    {
        return approveTemplateInfoMapper.selectApproveTemplateInfoList(approveTemplateInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param approveTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertApproveTemplateInfo(ApproveTemplateInfo approveTemplateInfo)
    {
        approveTemplateInfo.setCreateTime(DateUtils.getNowDate());
        return approveTemplateInfoMapper.insertApproveTemplateInfo(approveTemplateInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param approveTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateApproveTemplateInfo(ApproveTemplateInfo approveTemplateInfo)
    {
        approveTemplateInfo.setUpdateTime(DateUtils.getNowDate());
        return approveTemplateInfoMapper.updateApproveTemplateInfo(approveTemplateInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param approveTemplateIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApproveTemplateInfoByIds(Long[] approveTemplateIds)
    {
        return approveTemplateInfoMapper.deleteApproveTemplateInfoByIds(approveTemplateIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param approveTemplateId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteApproveTemplateInfoById(Long approveTemplateId)
    {
        return approveTemplateInfoMapper.deleteApproveTemplateInfoById(approveTemplateId);
    }

    @Override
    public List<ApprovaTemplateVO> getTemplateList(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<ApprovaTemplateVO> templateList = approveTemplateInfoMapper.getTemplateList(pageRequest.getSearch());
        if (CollectionUtils.isNotEmpty(templateList)){
            for (ApprovaTemplateVO vo:templateList){
                List<ApprovaTemplateNodeVO> nodeIds = vo.getNodeIds();
                if (CollectionUtils.isNotEmpty(nodeIds)){
                    List<ApprovaTemplateNodeVO> vos=new ArrayList<>();
                    SortListUtil.sort(nodeIds,"approveNodeId",SortListUtil.ASC);
                    for (int i=0;i<nodeIds.size();i++){
                        ApprovaTemplateNodeVO nodeVO = nodeIds.get(i);
                        if (ApproveTypeEnum.APPROVE_T001.getKey().equals(nodeVO.getType())){
                            String name="部门主管审批";
                            if (StringUtils.isNotBlank(nodeVO.getDeptProjectId())){
                                EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(Long.parseLong(nodeVO.getDeptProjectId()));
                                if (ecmpOrg!=null){
                                    name=ecmpOrg.getDeptName()+"主管审批";
                                }
                            }
                            nodeVO.setName(name);
                        }else if (ApproveTypeEnum.APPROVE_T002.getKey().equals(nodeVO.getType())){
                            String roleName="角色审批";
                            if (StringUtils.isNotBlank(nodeVO.getRoleId())){
                                EcmpRole ecmpRole = roleMapper.selectEcmpRoleById(Long.parseLong(nodeVO.getRoleId()));
                                roleName=ecmpRole.getRoleName()+"角色审批";
                            }
                            nodeVO.setName(roleName);
                        }else if (ApproveTypeEnum.APPROVE_T003.getKey().equals(nodeVO.getType())){
//                            String userName= ecmpUserMapper.findNameByUserIds(nodeVO.getUserId());
                            nodeVO.setName("指定员工审批");
                        }else{
                            String name="项目主管审批";
                            if (StringUtils.isNotBlank(nodeVO.getDeptProjectId())){
                                ProjectInfo projectInfo = projectInfoMapper.selectProjectInfoById(Long.parseLong(nodeVO.getDeptProjectId()));
                                if (projectInfo!=null){
                                    name=projectInfo.getName()+"主管审批";
                                }
                            }
                            nodeVO.setName(name);
                        }
                        nodeVO.setNumber(i);
                        vos.add(nodeVO);
                    }
                    vo.setNodeIds(vos);
                }
            }
        }
        return templateList;
    }

    @Override
    public ApprovaTemplateVO flowTemplateDetail(Long templateId) {
        ApprovaTemplateVO vo=new ApprovaTemplateVO();
        ApproveTemplateInfo approveTemplateInfo = approveTemplateInfoMapper.selectApproveTemplateInfoById(templateId);
        vo.setApproveTemplateId(templateId);
        vo.setName(approveTemplateInfo.getName());
        List<RegimeInfo> regimeInfos = regimeInfoMapper.selectRegimeInfoList(new RegimeInfo(templateId));
        vo.setIsBingRegime(regimeInfos.size());
        List<ApproveTemplateNodeInfo> approveTemplateInfos = approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoList(new ApproveTemplateNodeInfo(templateId));
        SortListUtil.sort(approveTemplateInfos,"approveNodeId",SortListUtil.ASC);
        List<ApprovaTemplateNodeVO> list=new ArrayList();
        if(CollectionUtils.isNotEmpty(approveTemplateInfos)){
            for (int i=0;i<approveTemplateInfos.size();i++){
                ApprovaTemplateNodeVO nodeVO=new ApprovaTemplateNodeVO();
                BeanUtils.copyProperties(approveTemplateInfos.get(i),nodeVO);
                nodeVO.setType(approveTemplateInfos.get(i).getApproverType());
                nodeVO.setNumber(i);
                nodeVO.setName(ApproveTypeEnum.format(approveTemplateInfos.get(i).getApproverType()).getDesc());
                list.add(nodeVO);
            }
        }
        vo.setNodeIds(list);
        return vo;
    }

    @Override
    @Transactional
    public void deleteFlow(Long approveTemplateId) throws Exception {
        //判端用车制度是否有关联
        List<RegimeInfo> regimeInfos = regimeInfoMapper.selectRegimeInfoList(new RegimeInfo(approveTemplateId));
        List<Long> joinName = regimeInfos.stream().map(RegimeInfo::getRegimenId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(regimeInfos)){
            throw new Exception("此审批流与用车制度:"+ StringUtils.join(joinName, ",")+"有关联");
        }
        int i = approveTemplateInfoMapper.deleteApproveTemplateInfoById(approveTemplateId);
        if (i>0){
            int count = approveTemplateNodeInfoMapper.deleteByTemplateId(approveTemplateId);
            if (count>0){
                log.info(approveTemplateId+"审批模板及节点删除成功!");
            }
        }
    }

    @Override
    public Long getTemplateListCount(String search) {
        return approveTemplateInfoMapper.getTemplateListCount(search);
    }
}
