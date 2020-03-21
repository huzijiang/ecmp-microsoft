package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.ApproveTemplateInfo;
import com.hq.ecmp.mscore.domain.ApproveTemplateNodeInfo;
import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.mapper.ApproveTemplateInfoMapper;
import com.hq.ecmp.mscore.mapper.ApproveTemplateNodeInfoMapper;
import com.hq.ecmp.mscore.mapper.RegimeInfoMapper;
import com.hq.ecmp.mscore.service.IApproveTemplateInfoService;
import com.hq.ecmp.mscore.vo.ApprovaTemplateNodeVO;
import com.hq.ecmp.mscore.vo.ApprovaTemplateVO;
import com.hq.ecmp.util.SortListUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.rmi.log.LogInputStream;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class ApproveTemplateInfoServiceImpl implements IApproveTemplateInfoService
{
    @Autowired
    private ApproveTemplateInfoMapper approveTemplateInfoMapper;
    @Autowired
    private ApproveTemplateNodeInfoMapper approveTemplateNodeInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;

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
    public List<ApprovaTemplateVO> getTemplateList() {
        List<ApprovaTemplateVO> templateList = approveTemplateInfoMapper.getTemplateList();
        if (CollectionUtils.isNotEmpty(templateList)){
            for (ApprovaTemplateVO vo:templateList){
                List<ApprovaTemplateNodeVO> nodeIds = vo.getNodeIds();
                if (CollectionUtils.isNotEmpty(nodeIds)){
                    List<ApprovaTemplateNodeVO> vos=new ArrayList<>();
                    SortListUtil.sort(nodeIds,"approveNodeId",SortListUtil.DESC);
                    for (int i=0;i<nodeIds.size();i++){
                        ApprovaTemplateNodeVO nodeVO = nodeIds.get(i);
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
        List<ApproveTemplateNodeInfo> approveTemplateInfos = approveTemplateNodeInfoMapper.selectApproveTemplateNodeInfoList(new ApproveTemplateNodeInfo(templateId));
        SortListUtil.sort(approveTemplateInfos,"approveNodeId",SortListUtil.DESC);
        List<ApprovaTemplateNodeVO> list=new ArrayList();
        if(CollectionUtils.isNotEmpty(approveTemplateInfos)){
            for (int i=0;i<approveTemplateInfos.size();i++){
                ApprovaTemplateNodeVO nodeVO=new ApprovaTemplateNodeVO();
                BeanUtils.copyProperties(approveTemplateInfos.get(0),nodeVO);
                nodeVO.setType(approveTemplateInfos.get(0).getApproverType());
                nodeVO.setNumber(i);
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
            approveTemplateNodeInfoMapper.deleteByTemplateId(approveTemplateId);
        }
    }
}
