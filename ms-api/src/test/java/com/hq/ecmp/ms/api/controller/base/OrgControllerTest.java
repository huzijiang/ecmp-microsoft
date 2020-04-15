package com.hq.ecmp.ms.api.controller.base;

import static com.hq.ecmp.constant.CommonConstant.PROJECT_USER_TREE;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;
import com.hq.api.system.domain.SysUser;
import com.hq.ecmp.mscore.domain.ProjectInfo;
import com.hq.ecmp.mscore.domain.ProjectUserRelationInfo;
import com.hq.ecmp.mscore.dto.AddFolwDTO;
import com.hq.ecmp.mscore.dto.FolwInfoDTO;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.mapper.ProjectInfoMapper;
import com.hq.ecmp.mscore.mapper.ProjectUserRelationInfoMapper;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IApproveTemplateInfoService;
import com.hq.ecmp.mscore.service.IApproveTemplateNodeInfoService;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.mscore.vo.ApplyDetailVO;
import com.hq.ecmp.mscore.vo.ApprovaTemplateVO;
import com.hq.ecmp.mscore.vo.ApprovalListVO;
import com.hq.ecmp.mscore.vo.ApprovalUserVO;
import com.hq.ecmp.mscore.vo.OrgTreeVo;
import com.hq.ecmp.util.RedisUtil;

import QrCodeService.QrCodeService;

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
    @Autowired
    private IApplyInfoService applyInfoService;
    @Autowired
    private RedisUtil redisUtil;
 

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
            OrgTreeVo orgTreeVos = orgService.selectDeptTree(null, null);
            System.out.println(JSON.toJSONString(orgTreeVos));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void deptUserTree(){
        try {
            OrgTreeVo orgTreeVos = orgService.selectDeptUserTree(null, null);
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

    @Test
    public void objEquls(){//测试两个对象是否相等
        try {
            ProjectUserRelationInfo info1=new ProjectUserRelationInfo(1l,1l);
            ProjectUserRelationInfo info2=new ProjectUserRelationInfo(1l,1l);
            System.out.println(info1.equals(info2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void selectApplyDetail(){//获取审批流信息
        try {
            Long applyId=891l;
            ApplyDetailVO applyDetailVO = applyInfoService.selectApplyDetail(applyId);
            //查询审批流信息
            //如果申请单无需审批则不展示审批流相关信息
            List<ApprovalListVO> approveList = applyInfoService.getApproveList(applyDetailVO.getApplyUser(), applyDetailVO.getApplyMobile(),applyId,applyDetailVO.getTime());
            System.out.println(approveList);
            System.out.println(applyDetailVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getApprovalList(){//根据制度获取审批人
        try {
            SysUser sysUser = new SysUser();
            sysUser.setUserId(106l);
            sysUser.setDeptId(252L);
            List<ApprovalUserVO> approvalList = nodeInfoService.getApprovalList("1", "7", sysUser);
            System.out.println(approvalList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getOrgTree(){//根据制度获取审批人
        try {
            OrgTreeVo orgTreeVos = iProjectInfoService.selectProjectUserTree( 3l );
            System.out.println(JSON.toJSONString(orgTreeVos));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void delRedis(){//根据制度获取审批人
        try {
            redisUtil.delKey(String.format(PROJECT_USER_TREE,1));
            redisUtil.delKey(String.format(PROJECT_USER_TREE,2));
            redisUtil.delKey(String.format(PROJECT_USER_TREE,3));
            redisUtil.delKey(String.format(PROJECT_USER_TREE,4));
            redisUtil.delKey(String.format(PROJECT_USER_TREE,5));
            redisUtil.delKey(String.format(PROJECT_USER_TREE,6));
            redisUtil.delKey(String.format(PROJECT_USER_TREE,7));
            System.out.println("chengg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   

}