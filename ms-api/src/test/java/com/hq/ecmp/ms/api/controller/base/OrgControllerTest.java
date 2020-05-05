package com.hq.ecmp.ms.api.controller.base;

import static com.hq.ecmp.constant.CommonConstant.DEPT_TYPE_ORG;
import static com.hq.ecmp.constant.CommonConstant.PROJECT_USER_TREE;

import java.util.ArrayList;
import java.util.List;

import com.hq.api.system.domain.SysDriver;
import com.hq.common.utils.StringUtils;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.velocity.app.event.implement.EscapeXmlReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSON;
import com.hq.api.system.domain.SysUser;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.mapper.ProjectInfoMapper;
import com.hq.ecmp.mscore.mapper.ProjectUserRelationInfoMapper;
import com.hq.ecmp.mscore.service.IApplyInfoService;
import com.hq.ecmp.mscore.service.IApproveTemplateInfoService;
import com.hq.ecmp.mscore.service.IApproveTemplateNodeInfoService;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.service.IJourneyUserCarPowerService;
import com.hq.ecmp.mscore.service.IProjectInfoService;
import com.hq.ecmp.mscore.vo.ApplyDetailVO;
import com.hq.ecmp.mscore.vo.ApprovaTemplateVO;
import com.hq.ecmp.mscore.vo.ApprovalListVO;
import com.hq.ecmp.mscore.vo.ApprovalUserVO;
import com.hq.ecmp.mscore.vo.OrgTreeVo;
import com.hq.ecmp.util.RedisUtil;

/**
 * @Author: chao.zhang
 * @Date: 2020/2/25 10:25
 */

@SpringBootTest
class OrgControllerTest {

    @Autowired
    private IApproveTemplateInfoService templateInfoService;
    @Autowired
    private IApplyApproveResultInfoService resultInfoService;
    @Autowired
    private IOrderAccountInfoService iOrderAccountInfoService;
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
    @Autowired
    private IJourneyUserCarPowerService journeyUserCarPowerService;
    @Autowired
    private IApplyApproveResultInfoService applyApproveResultInfoService;
    @Autowired
    private IEcmpUserService ecmpUserService;
    @Autowired
    private IEcmpOrgService ecmpOrgService;
    @Autowired
    private IOrderInfoService orderInfoService;
    @Autowired
    private IDriverHeartbeatInfoService driverHeartbeatInfoService;
    @Autowired
    private EcmpMessageService ecmpMessageService;



    @Test
    void getMessagesForPassenger() {
        try {
            SysUser sysUser = new SysUser();
            sysUser.setUserId(200347l);
            List<MessageDto> messagesForPassenger = ecmpMessageService.getMessagesForPassenger(sysUser);
            System.out.println(messagesForPassenger.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void getOrgByDeptId() {
        //加载部门（成本中心）
        EcmpOrg ecmpOrg = ecmpOrgService.selectEcmpOrgById(102l);
        if (DEPT_TYPE_ORG.equals(ecmpOrg.getDeptType())){//是公司
            System.out.println(ecmpOrg.toString());
        }else{
            String ancestors = ecmpOrg.getAncestors();
            if (StringUtils.isNotEmpty(ancestors)){
                String[] split = ancestors.split(",");
                for (int i=split.length-1;i>=0;i--){
                    EcmpOrg org= ecmpOrgService.selectEcmpOrgById(Long.parseLong(split[i]));
                    if (DEPT_TYPE_ORG.equals(org.getDeptType())){//是公司
                        System.out.println(org.toString());
                    }
                }
            }
        }
    }

    @Test
    void getUserOwnCompanyDept() {
        //加载部门（成本中心）
        Long a=5l;
        Long b=5l;
        System.out.println(a.equals(b));
        System.out.println("dfsfad");
    }

    @Test
    void applyPass() {
        //加载部门（成本中心）
        ApplyDTO applyDTO = new ApplyDTO();
        applyDTO.setApplyId(2181l);
        try{
            resultInfoService.applyPass(applyDTO,200051l,new ArrayList<>());
            System.out.println("审批成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("审批是吧");
        }

    }

    @Test
    void deleteFlow() {//项目员工树
        try{
            templateInfoService.deleteFlow(75l);
            System.out.println("删除成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("删除失败");
        }
    }
    @Test
    void selectProjectUserTree() {//项目员工树
        OrgTreeVo deptList = iProjectInfoService.selectProjectUserTree(91l);
        System.out.println(deptList.toString());
    }
    @Test
    void flowTemplateDetail() {//项目员工树
        PageRequest pageRequest=new PageRequest();
        pageRequest.setPageNum(5);
        pageRequest.setPageSize(10);
        List<ApprovaTemplateVO> templateList = templateInfoService.getTemplateList(pageRequest);
        System.out.println(templateList.toString());
    }
    @Test
    void getDriverOrderList() {//项目员工树
        try{
            LoginUser loginUser=new LoginUser();
            SysDriver sysDriver=new SysDriver();
            sysDriver.setDriverId(3005l);
            loginUser.setDriver(sysDriver);
            List<OrderDriverListInfo>  driverOrderList = orderInfoService.getDriverOrderList(loginUser,1, 20);
            System.out.println(driverOrderList.toString());
        }catch (Exception e){

        }
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
        dto.setName("测试增加部门审批流bug");
        List<FolwInfoDTO> list=new ArrayList<>();
        list.add(new FolwInfoDTO(0,"T001","","",""));
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
            List<ProjectInfo> listByUserId = iProjectInfoService.getListByUserId(101l, null,null);
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
            OrgTreeVo orgTreeVos = iProjectInfoService.selectProjectUserTree( 19l );
            System.out.println(JSON.toJSONString(orgTreeVos));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void invoiceViewList(){//根据制度获取审批人
        try {
            PageResult<OrderAccountViewVO> invoiceViewList = iOrderAccountInfoService.getAccountViewList(new PageRequest());
//            System.out.println(JSON.toJSONString(orgTreeVos));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void invoiceViewList1(){//根据制度获取审批人
        try {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPageSize(10);
            pageRequest.setPageNum(1);
            PageResult<EcmpUserDto> ecmpUserList = ecmpUserService.getEcmpUserPage(pageRequest);
//            System.out.println(JSON.toJSONString(orgTreeVos));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delRedis(){//根据制度获取审批人
        try {
            redisUtil.delKey(String.format(PROJECT_USER_TREE,19));
            redisUtil.delKey(String.format(PROJECT_USER_TREE,11));
            redisUtil.delKey(String.format(PROJECT_USER_TREE,13));
            redisUtil.delKey(String.format(PROJECT_USER_TREE,17));
            redisUtil.delKey(String.format(PROJECT_USER_TREE,15));
//            redisUtil.delKey(String.format(PROJECT_USER_TREE,6));
//            redisUtil.delKey(String.format(PROJECT_USER_TREE,7));
            System.out.println("chengg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getOrderState(){
        Long orderNo=935l;
        try {
            OrderStateVO  orderVO = orderInfoService.getOrderState(orderNo);
            //TODO 记得生产放开
            if (CarConstant.USR_CARD_MODE_HAVE.equals(orderVO.getUseCarMode())){//自有车
                DriverHeartbeatInfo driverHeartbeatInfo = driverHeartbeatInfoService.findNowLocation(orderVO.getDriverId(), orderNo);
                if(driverHeartbeatInfo!=null){
                    String latitude=driverHeartbeatInfo.getLatitude().stripTrailingZeros().toPlainString();
                    String longitude=driverHeartbeatInfo.getLongitude().stripTrailingZeros().toPlainString();
                    orderVO.setDriverLongitude(longitude);
                    orderVO.setDriverLatitude(latitude);
                }
            }else {
                orderVO = orderInfoService.getTaxiState(orderVO, orderNo);
            }
            System.out.println(JSON.toJSONString(orderVO));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getOrderDetail(){
        Long orderNo=935l;
        try {
            OrderVO  orderVO = orderInfoService.orderBeServiceDetail(orderNo);
            System.out.println(JSON.toJSONString(orderVO));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getSubDetail(){
        Long orderNo=935l;
        try {
            EcmpOrgDto ecmpOrg = orgService.getSubDetail(100l);
            System.out.println(JSON.toJSONString(ecmpOrg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void selectCompanyList(){
        Long orderNo=935l;
        try {
            PageResult<EcmpOrgDto> ecmpOrg = orgService.selectCompanyList(100l,"1",1,10);
            System.out.println(JSON.toJSONString(ecmpOrg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void initApproveResultInfo(){
        Long orderNo=935l;
        try {
           applyApproveResultInfoService.initApproveResultInfo(1l,423l,105l);
            System.out.println("生成审批流");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}