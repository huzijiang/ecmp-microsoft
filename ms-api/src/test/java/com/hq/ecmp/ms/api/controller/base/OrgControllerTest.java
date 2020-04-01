package com.hq.ecmp.ms.api.controller.base;

import com.alibaba.fastjson.JSON;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.domain.ProjectUserRelationInfo;
import com.hq.ecmp.mscore.dto.AddFolwDTO;
import com.hq.ecmp.mscore.dto.FolwInfoDTO;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.mapper.ProjectInfoMapper;
import com.hq.ecmp.mscore.mapper.ProjectUserRelationInfoMapper;
import com.hq.ecmp.mscore.service.IApproveTemplateInfoService;
import com.hq.ecmp.mscore.service.IApproveTemplateNodeInfoService;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.mscore.vo.ApprovaTemplateVO;
import com.hq.ecmp.mscore.vo.OrgTreeVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/2/25 10:25
 */

@SpringBootTest
class OrgControllerTest {

    @Autowired
    private IApproveTemplateInfoService templateInfoService;
    @Autowired
    private IApproveTemplateNodeInfoService nodeInfoService;
    @Autowired
    private ProjectInfoMapper projectInfoMapper;
    @Autowired
    private ProjectUserRelationInfoMapper projectUserRelationInfoMapper;
    @Autowired
    private EcmpUserMapper userMapper;
    @Autowired
    private IProjectInfoService iProjectInfoService;
    @Autowired
    private IEcmpOrgService orgService;

    @Test
    void getUserOwnCompanyDept() {
        //加载部门（成本中心）
        System.out.println("dfsfad");
    }

    @Test
    void getTemplateList() {//获取审批列表
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNum(1);
        pageRequest.setPageSize(20);
        List<ApprovaTemplateVO> templateList = templateInfoService.getTemplateList(pageRequest);
        System.out.println(templateList.toString());
    }

    @Test
    void add() {//新增审批流模板
        AddFolwDTO dto = new AddFolwDTO();
        dto.setName("测试增加部门/项目模板");
        List<FolwInfoDTO> list=new ArrayList<>();
        list.add(new FolwInfoDTO(0,"T001","","","255"));
        list.add(new FolwInfoDTO(1,"T003","","200091"));
        list.add(new FolwInfoDTO(2,"T004","","","6"));
        dto.setFlowList(list);
        try {
            nodeInfoService.addFlowTemplate(dto,101L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void edit() {//编辑审批流模板
        AddFolwDTO dto = new AddFolwDTO();
        dto.setName("编辑审批流模板");
        dto.setApproveTemplateId(17l);
        List<FolwInfoDTO> list=new ArrayList<>();
        list.add(new FolwInfoDTO(0,"T001","",""));
        list.add(new FolwInfoDTO(1,"T002","4,5",""));
        list.add(new FolwInfoDTO(2,"T003","","101,105,106"));
        dto.setFlowList(list);
        try {
            nodeInfoService.editFlowTemplate(dto,101L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getListByUserId(){
        try {
            List<ProjectInfo> listByUserId = iProjectInfoService.getListByUserId(101l, null);
            System.out.println(listByUserId.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deptTree(){
        try {
            List<OrgTreeVo> orgTreeVos = orgService.selectDeptTree(null, null);
            System.out.println(JSON.toJSONString(orgTreeVos));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void deptUserTree(){
        try {
            List<OrgTreeVo> orgTreeVos = orgService.selectDeptUserTree(null, null);
            System.out.println(orgTreeVos.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void addProjectUser(){//添加项目与新用户绑定
        Long startuserId=0l;
        Long endUserId=0l;
        List<Long> userIds=userMapper.findUserIds(startuserId,endUserId);
        List<ProjectInfo> projectInfos = projectInfoMapper.selectProjectInfoList(null);
        List<ProjectUserRelationInfo> list=new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userIds)){
            for (Long userId:userIds){
                if (CollectionUtils.isNotEmpty(projectInfos)){
                    for (ProjectInfo projectInfo:projectInfos){
                        list.add(new ProjectUserRelationInfo(projectInfo.getProjectId(),userId));
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(list)){
            projectUserRelationInfoMapper.insertProjectList(list);
        }
    }
}