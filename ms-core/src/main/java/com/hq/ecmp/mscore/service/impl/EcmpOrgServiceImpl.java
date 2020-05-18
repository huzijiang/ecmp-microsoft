package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.MacTools;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.OrgConstant;
import com.hq.ecmp.mscore.domain.EcmpOrg;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.EcmpOrgDto;
import com.hq.ecmp.mscore.dto.EcmpUserDto;
import com.hq.ecmp.mscore.dto.EcmpUserInfoDto;
import com.hq.ecmp.mscore.dto.PageRequest;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.vo.*;

import com.hq.ecmp.util.GsonUtils;
import com.hq.ecmp.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.security.acl.Owner;
import java.util.*;

import static com.hq.common.core.api.ApiResponse.SUCCESS_CODE;
import static com.hq.ecmp.constant.CommonConstant.DEPT_TYPE_ORG;
import static com.hq.ecmp.constant.CommonConstant.SWITCH_ON;

/**
 * 部门Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class EcmpOrgServiceImpl implements IEcmpOrgService {
    @Autowired
    private EcmpOrgMapper ecmpOrgMapper;
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private CarInfoMapper carInfoMapper;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;//车队
    @Autowired
    private EcmpRoleDeptMapper ecmpRoleDeptMapper;
    @Autowired
    private EcmpUserRoleMapper ecmpUserRoleMapper;
    @Autowired
    private CarGroupDriverRelationMapper carGroupDriverRelationMapper;
    @Autowired
    private ICarGroupInfoService carGroupInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EcmpRoleMapper ecmpRoleMapper;
    @Autowired
    private EcmpConfigMapper ecmpConfigMapper;
    @Value("${thirdService.apiUrl}") // 三方平台的接口前地址
    private String apiUrl;
    @Value("${thirdService.enterpriseId}") // 企业编号
    private String enterpriseId;
    @Value("${thirdService.licenseContent}") // 企业证书信息
    private String licenseContent;
    /**
     * 显示公司组织结构
     *
     * @param deptId 部门ID deptType组织类型 1公司 2部门
     * @return deptList 部门列表
     */
    @Override
    public List<EcmpOrgDto> selectCombinationOfCompany(EcmpOrgVo ecmpOrgVo){

        List<EcmpOrgDto> ecmpOrgList = new ArrayList<>();
        Long deptId = ecmpOrgVo.getDeptId();
        Long companyId = ecmpOrgVo.getCompanyId();
        Long deptType =  ecmpOrgVo.getDeptType();
        if(deptId==null){
            //默认查询所有公司列表
            ecmpOrgList = ecmpOrgMapper.selectByEcmpOrgOwnerCompanyId(companyId);
        }else {
            ecmpOrgList = ecmpOrgMapper.selectByEcmpOrgParentId(deptId,deptType);
        }
        if(ecmpOrgList.size()>0){
            for (EcmpOrgDto company:ecmpOrgList) {
                List<EcmpOrgDto> ecmpOrgs = loadEcmpOrg(company.getDeptId(), deptType);
                company.setDeptList(ecmpOrgs);
            }
        }
        return ecmpOrgList;
    }

    /**
     * 显示当前登陆用户所属公司与公司下的部门
     *
     * @param deptId 部门ID deptType组织类型 1公司 2部门
     * @return
     */
    @Override
    public List<EcmpOrgDto> selectDeptComTree(Long deptId,Long deptType,Long ownerCompany){
        EcmpOrgDto subDetail = ecmpOrgMapper.getSubComDept(deptId);
        Long subComDeptId=subDetail.getDeptId();
        List<EcmpOrgDto> ecmpOrgDtoList = ecmpOrgMapper.selectByEcmpOrgParentId(subComDeptId,  deptType);
        return ecmpOrgDtoList;
    }

    public List<EcmpOrgDto> loadEcmpOrg(Long parentId,Long deptType) {
        List<EcmpOrgDto> list = new ArrayList<>();
        List<EcmpOrgDto> deptList = ecmpOrgMapper.selectByEcmpOrgParentId(parentId,deptType);
        if(deptList.size()>0){
            for (EcmpOrgDto ecmpOrg:deptList) {
                list.add(ecmpOrg);
                ecmpOrg.setDeptList(loadEcmpOrg(ecmpOrg.getDeptId(),deptType));
            }
        }
        return list;
    }

    /**
     * 当前机构信息；分/子公司编号；分/子公司主管；分/子公司人数
     *
     * @param deptId 部门ID
     * @return ecmpOrg
     */
    @Override
    public EcmpOrgDto selectCurrentDeptInformation(Long deptId){
        return ecmpOrgMapper.selectCurrentDeptInformation(deptId,deptId.toString());
    }

    /**
     * 部门树
     * @param deptId
     * @return
     */
    @Override
    public OrgTreeVo selectDeptTree(Long deptId,String deptName) {
        OrgTreeVo orgTreeVos = ecmpOrgMapper.selectDeptTree(deptId, deptName);
        return orgTreeVos;
    }

    /**
     * 员工树
     * @param deptId
     * @param deptName
     * @return
     */
    @Override
    public OrgTreeVo selectDeptUserTree(Long deptId, String deptName) {

        OrgTreeVo orgTreeVos = ecmpOrgMapper.selectDeptTree(deptId, deptName);
        List<EcmpUser> userTreeVos = ecmpUserMapper.selectEcmpUserList(null);
//        OrgTreeVo deptUserChild = getDeptUserChild(orgTreeVos, userTreeVos);
        return orgTreeVos;
    }

//    private OrgTreeVo getDeptUserChild(OrgTreeVo orgTreeVos,List<EcmpUser> userTreeVos){
//        if (CollectionUtils.isEmpty(userTreeVos)) {
//            return orgTreeVos;
//        }
//        List<UserTreeVo> users = new ArrayList<>();
//        for (EcmpUser ecmpUser : userTreeVos) {
//            UserTreeVo userTreeVo = new UserTreeVo();
//            BeanUtils.copyProperties(ecmpUser, userTreeVo);
//            if (ecmpUser.getDeptId() == orgTreeVos.getDeptId()) {
//                users.add(userTreeVo);
//            }
//        }
//        orgTreeVos.setUsers(users);
//        if (CollectionUtils.isEmpty(orgTreeVos.getChildren())) {
//            return orgTreeVos;
//        } else {
//            for (OrgTreeVo deptAndUser : orgTreeVos.getChildren()) {
//                getDeptUserChild(deptAndUser, userTreeVos);
//            }
//        }
//        return orgTreeVos;
//    }


    /**
     * 公司车队树
     * @param deptId
     * @return
     */
    @Override
    public List<CompanyCarGroupTreeVO> selectCompanyCarGroupTree(Long deptId) {

        List<CompanyCarGroupTreeVO> tree = ecmpOrgMapper.selectCompanyCarGroupTree(deptId);
        int size = tree.size();
        if ( size > 0){
            for (int i = 0; i < size; i++) {
                if (tree.get(i) != null) {
                    String leader = tree.get(i).getLeader();
                    if(leader != null) {
                        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(Long.valueOf(leader));
                        if(ecmpUser != null ){
                            tree.get(i).setLeaderName(ecmpUser.getUserName());
                        }
                    }
                    //查询公司下的车队树
                    tree.get(i).setCarGroupTreeVO(carGroupInfoService.selectCarGroupTree(tree.get(i).getDeptId()));
                    //递归查询子公司
                    //tree.get(i).setChildrenList(this.selectCompanyCarGroupTree(tree.get(i).getDeptId()));
                }
            }
        }
        return tree;
    }

    @Override
    public List<CarGroupTreeVO> selectNewCompanyCarGroupTree(Long deptId, Long parentId) {
       /* if(deptId == null && parentId == null){
            parentId = 0L;
        }
        List<CarGroupTreeVO> tree = ecmpOrgMapper.selectNewCompanyCarGroupTree(deptId,parentId);
        int size = tree.size();
        if ( size > 0){
            for (int i = 0; i < size; i++) {
                if (tree.get(i) != null) {
                    //查询公司下的车队树
                    tree.get(i).setCarGroupTreeVO(carGroupInfoService.selectCarGroupTree(tree.get(i).getDeptId())) ;
                    List<CarGroupTreeVO> carGroupTreeVO = tree.get(i).getCarGroupTreeVO();
                    carGroupTreeVO.addAll(this.selectNewCompanyCarGroupTree(null, tree.get(i).getDeptId()));
                }
            }
        }*/
        if(deptId == null){
            throw new RuntimeException("公司id不能为空");
        }
        //查询公司
        List<CarGroupTreeVO> tree = ecmpOrgMapper.selectNewCompanyCarGroupTree(deptId,parentId);
        //递归查询公司的车队树
        tree.get(0).setCarGroupTreeVO(carGroupInfoService.selectCarGroupTree(deptId));
        return tree;
    }


    /**
     * 1.如果传的是车队id则查询该车队人数返回  (司机数量)
     * 2.如果传的公司id，则查询公司车队总人数  （司机数量）
     * @param deptId
     * @param carGroupId
     * @return
     */
    @Override
    public CarGroupCountVO selectCarGroupCount(Long deptId,Long carGroupId) {
        if(deptId == null && carGroupId == null){
            throw new RuntimeException("车队id和公司id为空");
        }
        CarGroupCountVO carGroupCountVO = new CarGroupCountVO();
        //1.如果传的是车队id则查询该车队人数返回
        if(carGroupId != null){
            CarGroupInfo carGroupInfo = carGroupInfoMapper.selectCarGroupInfoById(carGroupId);
            //车队编号
            carGroupCountVO.setDeptCode(carGroupInfo.getCarGroupCode());
            List<String> dispatcherNames = carGroupInfoService.getDispatcherNames(carGroupId);
            StringBuilder leaderName = new StringBuilder();
            dispatcherNames.forEach(a->leaderName.append(a+" "));
            String names = leaderName.toString().trim();
            //调度员名字
            carGroupCountVO.setLeaderName(names);
            int n = carGroupDriverRelationMapper.selectCountDriver(carGroupId);
            //该车队人数
            carGroupCountVO.setTotalMember(n);
            return carGroupCountVO;
        }
        //2.如果传的公司id，则查询公司车队总人数
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(deptId);
        //公司编号
        if(ObjectUtils.isNotEmpty(ecmpOrgMapper)){
            carGroupCountVO.setDeptCode(ecmpOrg.getDeptCode());
        }
        String leader = ecmpOrg.getLeader();
        if( leader != null) {
            EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(Long.valueOf(leader));
            //公司负责人
            if(ecmpUser != null){
                carGroupCountVO.setLeaderName(ecmpUser.getNickName());
            }
        }
        CarGroupInfo carGroupInfo = new CarGroupInfo();
        carGroupInfo.setCompanyId(deptId);
        //查询所属公司下的车队
        List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.selectCarGroupInfoList(carGroupInfo);
        int size = carGroupInfos.size();
        int num = 0;
        if(size > 0){
            for (CarGroupInfo groupInfo : carGroupInfos) {
                if (carGroupInfo != null) {
                    Long groupId = groupInfo.getCarGroupId();
                    int i = carGroupDriverRelationMapper.selectCountDriver(groupId);
                    num += i;
                }
            }
        }
        //公司车队总司机数
        carGroupCountVO.setTotalMember(num);
       /* List<CarGroupTreeVO> list = selectCarGroupTree(deptId);
        int num = 0;
        for (CarGroupTreeVO carGroupTreeVO : list) {
            num += carGroupCountVO.getTotalMember();
            List<CarGroupTreeVO> childrenList = carGroupTreeVO.getChildrenList();
            if(CollectionUtils.isNotEmpty(childrenList)){
                for (CarGroupTreeVO groupTreeVO : childrenList) {
                    num += groupTreeVO.getCount();
                }
            }
        }
        carGroupCountVO.setTotalMember(num);*/
        return carGroupCountVO;
    }


    //递归公司  (公司树)
    @Override
    public List<CompanyTreeVO> getCompanyTree(Long deptId){
        if (deptId == null){
            EcmpOrg ecmpOrg = new EcmpOrg();
            ecmpOrg.setParentId(0L);
            List<EcmpOrg> ecmpOrgs = ecmpOrgMapper.selectEcmpOrgList(ecmpOrg);
            deptId = ecmpOrgs.get(0).getDeptId();
        }
        //根据deptId查询下级公司
        List<CompanyTreeVO> list = ecmpOrgMapper.selectCompanyTree(deptId);
        int size = list.size();
        if(size > 0){
            for (int i = 0; i < size; i++) {
                list.get(i).setChildrenList(this.getCompanyTree(list.get(i).getDeptCompanyId()));
            }
        }
        return list;
    }

    private List<UserTreeVo> getUserList(Long deptId){
        return ecmpUserMapper.selectListByDeptId(deptId);
    }

    /**
     * 显示公司列表
     *
     * @param deptId 部门ID
     * @return ecmpOrg
     */
    @Override
    public PageResult<EcmpOrgDto> selectCompanyList(Long deptId,String deptType,int pageSize,int pageNum){
        /*列表：（分/子公司名称、编号、分/子公司主管、分/子公司人数、上级公司、下属公司数、状态*/
        PageHelper.startPage(pageNum,pageSize);
        List<EcmpOrgDto> companyList = new ArrayList<>();
        List<Long> deptIdList = new ArrayList<>();
        if(deptId!=null){
            deptIdList = ecmpOrgMapper.selectCompanyByParentId(deptId, OrgConstant.DEPT_TYPE_1);
            EcmpOrgDto supDto=ecmpOrgMapper.getSubDetail(deptId);
            String supComName=supDto.getDeptName();
            if(deptIdList.size()>0){
                for (Long deptId1:deptIdList) {
                    EcmpOrgDto ecmpOrgDto=ecmpOrgMapper.selectCompanyList(deptId1,OrgConstant.DEPT_TYPE_1);
                    ecmpOrgDto.setSupComName(supComName);
                    companyList.add(ecmpOrgDto);
                }
            }
        }
        PageInfo<Long> info = new PageInfo<>(deptIdList);
        return new PageResult<>(info.getTotal(),info.getPages(),companyList);
    }

    /**
     * 显示查询总条数
     * @param ecmpOrg
     * @return
     */
    @Override
    public Integer queryCompanyListCount(EcmpOrgVo ecmpOrg){
        return ecmpOrgMapper.queryCompanyListCount(ecmpOrg.getDeptId(),ecmpOrg.getDeptType().toString());
    }

    /**
     * 显示部门列表
     *
     * @param deptId 部门ID
     * @return ecmpOrg
     */
    @Override
    public PageResult<EcmpOrgDto> selectDeptList(Long deptId,String deptType,int pageNum,int pageSize){
        /*列表：部门名称、编号、部门主管、部门人数、所属组织、下属部门数、状态*/
        PageHelper.startPage(pageNum,pageSize);
        List<EcmpOrgDto> companyList = new ArrayList<>();
        List<Long> deptIdList = new ArrayList<>();
        if(deptId!=null){
            deptIdList = ecmpOrgMapper.selectCompanyByParentId(deptId, OrgConstant.DEPT_TYPE_2);
            EcmpOrgDto supDto=ecmpOrgMapper.getSubDetail(deptId);
            String supComName=supDto.getDeptName();
            if(deptIdList.size()>0){
                for (Long deptId1:deptIdList) {
                    EcmpOrgDto ecmpOrgDto=ecmpOrgMapper.selectDeptList(deptId1,OrgConstant.DEPT_TYPE_2);
                    ecmpOrgDto.setSupComName(supComName);
                    List<UserVO> leaders=ecmpOrgMapper.selectUserByLeader(ecmpOrgDto.getLeader());
                    ecmpOrgDto.setLeaderUsers(leaders);
                    companyList.add(ecmpOrgDto);
                }
            }
        }
        PageInfo<Long> info = new PageInfo<>(deptIdList);
        return new PageResult<>(info.getTotal(),info.getPages(),companyList);
    }

    /**
     * 查询部门详情
     *
     * @param deptId 部门ID
     * @return ecmpOrg
     */
    @Override
    public EcmpOrgDto getDeptDetails(Long deptId){
        return ecmpOrgMapper.selectByDeptId(deptId);
    }

    /**
     * 查询分/子公司、部门编号是否已存在
     *
     * @param deptCode 分/子公司、部门编号
     * @return ecmpOrg
     */
    @Override
    public int selectDeptCodeExist(String deptCode){
            return ecmpOrgMapper.selectDeptCodeExist(deptCode);
    }


    /*
     * 添加部门
     *  @param  ecmpOrg
     * @return int
     * */
    @Override
    @Transactional
    public int addDept(EcmpOrgVo ecmpOrgVo ,Long userId) throws Exception{
        boolean flag=false;

        this.checkOrgVo(ecmpOrgVo,1);//1新增,0编辑校验
        if (CommonConstant.DEPT_TYPE_ORG.equals(ecmpOrgVo.getDeptType())){//公司
            EcmpUser userByPhone = ecmpUserMapper.getUserByPhone(ecmpOrgVo.getPhone());
            if (userByPhone!=null){
                log.info("新增公司:公司主管手机号:"+ecmpOrgVo.getPhone()+"信息"+userByPhone.getUserId()+userByPhone.getUserName());
                ecmpOrgVo.setLeader(String.valueOf(userByPhone.getUserId()));
                flag=true;
            }
        }
        ecmpOrgVo.setCreateTime(DateUtils.getNowDate());
        EcmpOrg parentOrg = ecmpOrgMapper.selectEcmpOrgById(ecmpOrgVo.getParentId());
        String ancestors=parentOrg.getAncestors()+","+ecmpOrgVo.getParentId();
        ecmpOrgVo.setAncestors(ancestors);
        if("Y000".equals(ecmpOrgVo.getItIsIndependent())){
            ecmpOrgVo.setStatus(EcmpOrgVo.statusEnum.CLOUD.getCode());
        }else{
            ecmpOrgVo.setStatus(EcmpOrgVo.statusEnum.OK.getCode());
        }
        int iz = ecmpOrgMapper.addDept(ecmpOrgVo);
        if("Y000".equals(ecmpOrgVo.getItIsIndependent())){
            //云端独立核算
            Map map = new HashMap();
            // MAC地址
            List<String> macList = MacTools.getMacList();
            String macAdd = macList.get(0);
            map.put("companyName",ecmpOrgVo.getDeptName());
            map.put("enterpriseId",enterpriseId);
            map.put("itIsIndependent","");
            map.put("licenseContent",licenseContent);
            map.put("mac",macAdd);
            map.put("orgId",ecmpOrgVo.getDeptId());
            uploadIndependentCompanyApply(map);
        }
        if (CommonConstant.DEPT_TYPE_ORG.equals(ecmpOrgVo.getDeptType())){//公司
            if (!flag){//公司主管不存在
                //新建公司主管
                EcmpUser companyUser=new EcmpUser();
                companyUser.setUserName(ecmpOrgVo.getPhone());
                companyUser.setOwnerCompany(ecmpOrgVo.getDeptId());
                companyUser.setItIsDriver(CommonConstant.SWITCH_ON);
                companyUser.setItIsDispatcher(CommonConstant.SWITCH_ON);
                companyUser.setRemark("新建公司操作添加的主管");
                companyUser.setDelFlag(CommonConstant.SWITCH_ON);
                companyUser.setEmail(ecmpOrgVo.getEmail());
                companyUser.setNickName(ecmpOrgVo.getLeader());
                companyUser.setPhonenumber(ecmpOrgVo.getPhone());
                companyUser.setStatus(CommonConstant.SWITCH_ON);
                companyUser.setDeptId(ecmpOrgVo.getDeptId());
                int i = ecmpUserMapper.insertEcmpUser(companyUser);
                if (ecmpOrgVo.getDeptId()!=null &&companyUser.getUserId()!=null){
                    ecmpOrgMapper.updateEcmpOrg(new EcmpOrgVo(ecmpOrgVo.getDeptId(),String.valueOf(companyUser.getUserId())));
                }
                log.info("新建公司对应手机号"+ecmpOrgVo.getPhone()+"的主管新增成功");

                //获取系统默认角色ID
                String [] roleKeys = {CommonConstant.ROLE_SUB_ADMIN,CommonConstant.ROLE_EMPLOYEE,CommonConstant.ROLE_DEPT_MANAGER,CommonConstant.ROLE_PROJECT_MANAGER,CommonConstant.ROLE_DRIVER,CommonConstant.ROLE_DISPATCHER};
                Long [] roleIds =  new Long[roleKeys.length];
                for(int k = 0;k<roleKeys.length;k++){
                    roleIds[k] = ecmpRoleMapper.selectEcmpRoleByRoleKey(roleKeys[k]).getRoleId();
                    System.out.println(roleIds[k]);
                }
                //给分子公司主管授权
                EcmpUserRole userRole = new EcmpUserRole();
                userRole.setUserId(companyUser.getUserId());
                for(int m = 0;m<2;m++){
                    System.out.println(roleIds[m]);
                    userRole.setRoleId(roleIds[m]);
                    ecmpUserRoleMapper.insertEcmpUserRole(userRole);
                }
                //默认角色与分子公司关联

                EcmpRoleDept roleDept = new EcmpRoleDept();
                roleDept.setDeptId(ecmpOrgVo.getDeptId());
                for(int s =0;s<roleIds.length;s++){
                    roleDept.setRoleId(roleIds[s]);
                    ecmpRoleDeptMapper.insertEcmpRoleDept(roleDept);
                }

                //默认将当前公司的企业配置信息给分子公司
                String ownerCompanyId = ecmpUserMapper.selectEcmpUserById(userId).getOwnerCompany().toString();
                EcmpConfig ecmpConfig = new EcmpConfig();
                ecmpConfig.setCompanyId(ownerCompanyId);
                List<EcmpConfig> ecmpConfigs = ecmpConfigMapper.selectEcmpConfigList(ecmpConfig);
                String newCompanyId = ecmpOrgVo.getDeptId().toString();
                for(EcmpConfig config:ecmpConfigs){
                    EcmpConfig newConfig = new EcmpConfig();
                    newConfig.setCompanyId(newCompanyId);
                    newConfig.setConfigName(config.getConfigName());
                    newConfig.setConfigKey(config.getConfigKey());
                    newConfig.setConfigType(config.getConfigType());
                    newConfig.setConfigValue(config.getConfigValue());
                    newConfig.setCreateTime(new Date());
                    newConfig.setUpdateTime(new Date());
                    ecmpConfigMapper.insertEcmpConfig(newConfig);
                }
            }
        }
        return iz;
    }

    /**
     * 部门公司校验
     * @param ecmpOrgVo
     * @param flag 1新增,0编辑
     * @throws Exception
     */
    private void checkOrgVo(EcmpOrgVo ecmpOrgVo,int flag)throws Exception{
        if (StringUtils.isBlank(ecmpOrgVo.getDeptName())) {
            throw new Exception("部门/公司名称不可为空");
        }
        int isRepart = ecmpOrgMapper.isRepart(ecmpOrgVo.getDeptName(),flag,ecmpOrgVo.getDeptId(),ecmpOrgVo.getParentId());
        if(isRepart>0){
            throw new Exception("部门名称，不可重复录入！");
        }
        if (flag == 1) {
            if (StringUtils.isNotBlank(ecmpOrgVo.getDeptCode())) {
                int j = ecmpOrgMapper.selectDeptCodeExist(ecmpOrgVo.getDeptCode().trim());
                if (j > 0) {
                    throw new Exception("该编号已存在，不可重复录入！");
                }
            }
        }
        if (ecmpOrgVo.getParentId() == null || ecmpOrgVo.getParentId().intValue() == 0) {
            throw new Exception("上级部门/公司不可为空");
        }
        if (CommonConstant.START.equals(ecmpOrgVo.getDeptType())) {//公司
            if (StringUtils.isBlank(ecmpOrgVo.getEmail())) {
                throw new Exception("主管邮箱为空!");
            }
            if (StringUtils.isBlank(ecmpOrgVo.getLeader())) {
                throw new Exception("主管为空!");
            }
            if (StringUtils.isBlank(ecmpOrgVo.getPhone())) {
                throw new Exception("主管手机号为空!");
            }
            if (flag == 1) {
                int count = ecmpUserMapper.selectEmailExist(ecmpOrgVo.getEmail().trim());
                if (count > 0) {
                    throw new Exception("该邮箱已存在，不可重复录入！");
                }
            }
        }

    }

    /*
     * 查询上级部门下的所有员工
     *  @param  ecmpOrg
     * @return int
     * */
    public List<EcmpUserDto> selectUserByDeptId(EcmpOrgVo ecmpOrg){
        List<EcmpUserDto> ecmpUserList = ecmpUserMapper.selectUserByDeptId(ecmpOrg.getDeptId());
        if(ecmpUserList.size()>0){
            return ecmpUserList;
        }
        return null;
    }

    /*
     * 修改部门
     *  @param  ecmpOrg
     * @return int
     * */
    @Transactional
    @Override
    public int updateDept(EcmpOrgVo ecmpOrg,Long userId)throws Exception{
        this.checkOrgVo(ecmpOrg,0);
        ecmpOrg.setUpdateTime(DateUtils.getNowDate());
        ecmpOrg.setUpdateBy(String.valueOf(userId));
        int ix = ecmpOrgMapper.updateDept(ecmpOrg);
        return ix;
    }

    /**
     * 查询部门
     *
     * @param deptId 部门ID
     * @rrn 部门
     */
    @Override
    public EcmpOrg selectEcmpOrgById(Long deptId) {
        return ecmpOrgMapper.selectEcmpOrgById(deptId);
    }

    /**
     * 根据公司id查询部门对象列表
     *
     * @param deptId
     * @return
     */
    @Override
    public List<EcmpOrg> selectEcmpOrgsByDeptId(Long deptId) {
        return null;
    }

    /**
     * 查询部门列表
     *
     * @param ecmpOrg 部门
     * @return 部门
     */
    @Override
    public List<EcmpOrg> selectEcmpOrgList(EcmpOrg ecmpOrg) {
        return ecmpOrgMapper.selectEcmpOrgList(ecmpOrg);
    }


    /**
     * 修改公司
     *
     * @param ecmpOrg 公司
     * @return 结果
     */
    @Override
    @Transactional
    public int updateEcmpOrg(EcmpOrgVo ecmpOrg,Long userId)throws Exception {
        boolean flag=false;
        this.checkOrgVo(ecmpOrg,0);
        EcmpUser userByPhone = ecmpUserMapper.getUserByPhone(ecmpOrg.getPhone());
        if (userByPhone!=null){
            log.info("编辑公司"+ecmpOrg.getDeptId()+":公司主管手机号:"+ecmpOrg.getPhone()+"信息"+userByPhone.getUserId()+userByPhone.getUserName());
            ecmpOrg.setLeader(String.valueOf(userByPhone.getUserId()));
            flag=true;
        }
        ecmpOrg.setUpdateBy(String.valueOf(userId));
        ecmpOrg.setUpdateTime(DateUtils.getNowDate());
        int i1= ecmpOrgMapper.updateEcmpOrg(ecmpOrg);
        if (CommonConstant.START.equals(ecmpOrg.getDeptType())){//公司
            if (!flag){//公司主管不存在
                //新建公司主管
                EcmpUser companyUser=new EcmpUser();
                companyUser.setUserName(ecmpOrg.getPhone());
                companyUser.setItIsDriver(CommonConstant.SWITCH_ON);
                companyUser.setItIsDispatcher(CommonConstant.SWITCH_ON);
                companyUser.setRemark("新建公司操作添加的主管");
                companyUser.setDelFlag(CommonConstant.SWITCH_ON);
                companyUser.setEmail(ecmpOrg.getEmail());
                companyUser.setNickName(ecmpOrg.getLeader());
                companyUser.setPhonenumber(ecmpOrg.getPhone());
                companyUser.setStatus(CommonConstant.SWITCH_ON);
                companyUser.setDeptId(ecmpOrg.getDeptId());
                int i = ecmpUserMapper.insertEcmpUser(companyUser);
                if (ecmpOrg.getDeptId()!=null &&companyUser.getUserId()!=null){
                    ecmpOrgMapper.updateEcmpOrg(new EcmpOrgVo(ecmpOrg.getDeptId(),String.valueOf(companyUser.getUserId())));
                }
                log.info("编辑公司对应手机号"+ecmpOrg.getPhone()+"的主管新增成功");
            }
        }
        return i1;
    }

    /**
     * 批量删除部门
     *
     * @param deptIds 需要删除的部门ID
     * @return 结果
     */
    @Override
    public int deleteEcmpOrgByIds(Long[] deptIds) {
        return ecmpOrgMapper.deleteEcmpOrgByIds(deptIds);
    }

    /**
     * 删除部门信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Override
    public int deleteEcmpOrgById(Long deptId) {
        return ecmpOrgMapper.deleteEcmpOrgById(deptId);
    }

    /**
     * (根据部门名称模糊)查询用户 所在（子）公司的 部门列表
     * @param userId
     * @param name
     * @return
     */
    @Override
    public List<EcmpOrg> selectUserOwnCompanyDept(Long userId, String name) {
        ///根据userId查询用户信息
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(userId);
        //用户所属公司
        Long ownerCompany = ecmpUser.getOwnerCompany();
        //用户所属部门
        Long deptId = ecmpUser.getDeptId();
        //查询公司信息
        //EcmpOrg companyInfo = ecmpOrgMapper.selectEcmpOrgById(ownerCompany);
        //查询公司下的所有部门
        EcmpOrg ecmpOrg = new EcmpOrg();
        ecmpOrg.setCompanyId(ownerCompany);
        ecmpOrg.setDeptName(name);
        List<EcmpOrg> ecmpOrgs = ecmpOrgMapper.selectEcmpOrgList(ecmpOrg);
       // ecmpOrgs.add(companyInfo);
        for (EcmpOrg org : ecmpOrgs) {
            if(org.getDeptId().equals(deptId)){
                //如果是本部门，状态 为 1
                org.setStatus("1");
            }else {
                //不是本部门，状态为 0
                org.setStatus("0");
            }
        }

        return ecmpOrgs;
    }

    /**
     *查询子公司列表
     * @return
     */
    @Override
    public String[] selectSubCompany(Long deptId) {
        String[] subCompany = ecmpOrgMapper.selectSubCompany(deptId);
        return subCompany;
    }

    /**
     *查询分/子公司详情
     * @return
     */
    @Override
    public EcmpOrgDto getSubDetail(Long deptId) {
        return ecmpOrgMapper.getSubDetail(deptId);
    }

    /**
     * 逻辑删除分子公司/部门信息
     *
     * @param deptType, deptId
     * @return 结果
     */
    @Transactional
    @Override
    public String updateDelFlagById(String deptType,Long deptId) {

        //根据deptId查询组织下级是否有数据信息 ecmpOrgNum>0不可删除
        int ecmpOrgNum = ecmpOrgMapper.selectByAncestorsLikeDeptId(deptId);

        //查询该组织下的员工信息 如果ecmpUserNum>0不可删除
        int ecmpUserNum = ecmpUserMapper.selectEcmpUserByDeptId(deptId);

        //排查当前分/子公司关联数据是否只有分/子公司主管
        if(OrgConstant.DEPT_TYPE_1.equals(deptType)){
            int count=ecmpOrgMapper.selectCountByParentId(deptId.intValue());
            if(ecmpUserNum==1||count==1){
                int delFlag = ecmpOrgMapper.updateDelFlagById(deptId,deptType.toString());
                //上面已判断，部门下存在人员不可删除
                /*List<Long> UserIds = ecmpUserMapper.getEcmpUserIdsByDeptId(deptId);
                ecmpUserMapper.updateDelFlagById(UserIds.get(0));*/
                if(delFlag==1){
                    return "删除分/子公司数据成功！";
                }else {
                    return "删除分/子公司数据失败！";
                }
            }
        }

        //查询该组织下的驾驶员信息 如果该公司没有员工，就没有驾驶员
        int driverNum =driverInfoMapper.selectDriverCountByDeptId(deptId);

        //查询该组织下的车辆信息  如果carNum>0不可删除
        int carNum=carInfoMapper.selectCarCountByDeptId(deptId);

        //查询该组织下的车队信息  如果该组织没有车也就没有车队
        int carGroupNum = carGroupInfoMapper.selectCountByOrgdeptId(deptId);

        //如果满足上述条件，执行删除操作
        if(ecmpOrgNum==0&&ecmpUserNum==0&&carNum==0){
            int delFlag = ecmpOrgMapper.updateDelFlagById(deptId,deptType.toString());
            if(deptType.equals("1")){
                if(delFlag==1){
                    return "删除分/子公司数据成功！";
                }else {
                    return "删除分/子公司数据失败！";
                }
            }else{
                if(delFlag==1){
                    return "删除部门数据成功！";
                }else {
                    return "删除部门数据失败！";
                }
            }
        }
        return "不可删除！";
    }

    /**
     * 逻辑批量删除部门信息
     *
     * @param deptIds 部门ID
     * @return 结果
     */
    @Transactional
    @Override
    public int updateDelFlagByIds(Long[] deptIds) {
        return ecmpOrgMapper.updateDelFlagByIds(deptIds);
    }

    /**
     * 禁用/启用  分/子公司或部门
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @Transactional
    @Override
    public String updateUseStatus(String status,Long deptId){
        //禁用/启用  分/子公司
        int i = ecmpOrgMapper.updateUseStatus(status,deptId.toString(),deptId);
        //禁用/启用  员工
        int i1 = ecmpUserMapper.updateRelationUseStatus(deptId, status);
        //禁用/启用  驾驶员  state 状态 W001 待审   V000 生效中   NV00 失效
        int i2 = driverInfoMapper.updateUseStatus(deptId, "0".equals(status)?"V000":"NV00");
        if("0".equals(status)){
            return "启用成功！";
        }
        return "禁用成功！";
    }

    /**
     * 按照分子公司名称或编号模糊查询匹配的列表
     * @param deptNameOrCode
     * @return 结果
     */
    @Override
    public PageResult<EcmpOrgDto> selectCompanyByDeptNameOrCode(PageRequest pageRequest,String deptNameOrCode){
        List<EcmpOrgDto> ecmpOrgDtoList=new ArrayList<>();
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Long> deptIds = ecmpOrgMapper.selectDeptIdsByDeptNameOrCode(deptNameOrCode, deptNameOrCode,OrgConstant.DEPT_TYPE_1);
        PageInfo info = new PageInfo<>(deptIds);
        if(deptIds.size()>0){
            for (int i = 0; i < deptIds.size(); i++) {
                EcmpOrgDto ecmpOrgDto = ecmpOrgMapper.selectCompanyByDeptNameOrCode(deptNameOrCode, deptNameOrCode, deptIds.get(i));
                LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
                String leader=ecmpOrgDto.getLeader();
                leader = changeUserIdToNickNames(leader);
                ecmpOrgDto.setLeader(leader);
                ecmpOrgDtoList.add(ecmpOrgDto);
            }
        }
        return new PageResult<>(info.getTotal(),info.getPages(),ecmpOrgDtoList);
    }

    /**
     * 按照部门名称或编号模糊查询匹配的列表
     * @param deptNameOrCode
     * @return 结果
     */
    @Override
    public PageResult<EcmpOrgDto> selectDeptByDeptNameOrCode(PageRequest pageRequest, String deptNameOrCode){
        List<EcmpOrgDto> ecmpOrgDtoList=new ArrayList<>();
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Long> deptIds = ecmpOrgMapper.selectDeptIdsByDeptNameOrCode(deptNameOrCode, deptNameOrCode,OrgConstant.DEPT_TYPE_2);
        PageInfo info = new PageInfo<>(deptIds);
        if(deptIds.size()>0){
            for (int i = 0; i < deptIds.size(); i++) {
                EcmpOrgDto ecmpOrgDto = ecmpOrgMapper.selectDeptByDeptNameOrCode(deptNameOrCode, deptNameOrCode, deptIds.get(i));
                String leader=ecmpOrgDto.getLeader();
                leader = changeUserIdToNickNames(leader);
                ecmpOrgDto.setLeader(leader);
                ecmpOrgDtoList.add(ecmpOrgDto);
            }
        }
        return new PageResult<>(info.getTotal(),info.getPages(),ecmpOrgDtoList);
    }

    @Override
    public List<EcmpOrgDto> selectDeptByCompany(Long deptId) {
        return null;
    }

	@Override
	public List<Long> queryDeptIdOfCompany(Long deptId) {
		List<Long> result =new ArrayList<Long>();
		result.add(deptId);
		//查询所有部门
		EcmpOrg ecmpOrg = new EcmpOrg();
		List<EcmpOrg> selectEcmpOrgList = ecmpOrgMapper.selectEcmpOrgList(ecmpOrg);
		if(null !=selectEcmpOrgList && selectEcmpOrgList.size()>0){
			for (EcmpOrg e : selectEcmpOrgList) {
				//上级所有部门ID  0,100,101
				String ancestors = e.getAncestors();
				if(StringUtils.isNotEmpty(ancestors)){
					String[] split = ancestors.split(",");
					List<String> parentIdList = Arrays.asList(split);
					//上级部门中包含了 deptId 这个部门   则 当前部门是deptId的下级部门
					if(parentIdList.contains(deptId.toString())){
						result.add(e.getDeptId());
					}
				}
			}
		}

		return result;
	}



    private void recursion(List<EcmpOrgDto> selectCombinationOfCompany, List<Long> result) {
		if (null != selectCombinationOfCompany && selectCombinationOfCompany.size() > 0) {
			for (EcmpOrgDto ecmpOrgDto : selectCombinationOfCompany) {
				Long deptId = ecmpOrgDto.getDeptId();
				result.add(deptId);
				List<EcmpOrgDto> nextDeptList = ecmpOrgDto.getDeptList();
				if (null != nextDeptList && nextDeptList.size() > 0) {
					recursion(nextDeptList, result);
				}
			}
		}
	}

    private String changeUserIdToNickNames(String leader) {
        if(leader==null||"".equals(leader)||"0".equals(leader)){
            leader="无";
            return leader;
        }
        if(leader.contains(",")){
            String[] split = leader.split(",");
            /*userIds = Arrays.asList(split).stream().mapToInt(Integer::parseInt).toArray();*/
            Long[]  userIds = (Long[]) ConvertUtils.convert(split,Long.class);
            List<String> nickNames = ecmpUserMapper.selectNickNamesByUserIds(userIds);
            if(!nickNames.isEmpty()){
                leader = StringUtils.join(nickNames.toArray(), ",");
            }
        }else{
           String nickNames = ecmpUserMapper.selectNickNamesByUserId(Long.parseLong(leader));
           leader=nickNames;
        }

        return leader;
    }

    @Override
    public EcmpOrg getOrgByDeptId(Long deptId){
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(deptId);
        if (DEPT_TYPE_ORG.equals(ecmpOrg.getDeptType())){//是公司
            return ecmpOrg;
        }else{
            String ancestors = ecmpOrg.getAncestors();
            if (com.hq.common.utils.StringUtils.isNotEmpty(ancestors)){
                String[] split = ancestors.split(",");
                for (int i=split.length-1;i>=0;i--){
                    if (SWITCH_ON.equals(split[i])){
                        continue;
                    }
                    EcmpOrg org= ecmpOrgMapper.selectEcmpOrgById(Long.parseLong(split[i]));
                    if (DEPT_TYPE_ORG.equals(org.getDeptType())){//是公司
                        return org;
                    }
                }
            }
            return null;
        }
    }

    /**
     *查询分/子公司下的部门名称和deptId
     * @return
     */
   // @Override
    /*public List<EcmpOrgDto> selectDeptByCompany(Long deptId) {
        return ecmpOrgMapper.selectDeptByCompany(deptId);
    }*/

    /**
     * 通过用户id获取末级部门信息和末级公司信息
     * @param userId 用户id
     * @return
     */
    @Override
    public EcmpUserInfoDto getUserLatestDeptInfoByUserId(Long userId) {
        EcmpUserInfoDto ecmpUserInfoDto = new EcmpUserInfoDto();
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(userId);
        EcmpOrg ecmpDept = ecmpOrgMapper.selectEcmpOrgById(ecmpUser.getDeptId());
        if(ecmpDept!=null){
            EcmpOrg ecmpCompany = ecmpOrgMapper.selectEcmpOrgById(ecmpDept.getCompanyId());
            ecmpUserInfoDto.setUserDeptInfo(ecmpDept);
            if(ecmpCompany!=null){
                ecmpUserInfoDto.setUserCompanyInfo(ecmpCompany);
            }
        }
        return ecmpUserInfoDto;
    }


    //上传企业审核信息
    private void uploadIndependentCompanyApply(Map map){
        try {
            String resultJSON =  OkHttpUtil.postForm(apiUrl+"/independentCompany/uploadIndependentCompanyApply",map);
            Type type = new TypeToken<ApiResponse>() {
            }.getType();
            GsonUtils.jsonToBean(resultJSON, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //定时任务接口，查询云端审核状态，更新状态
    @Override
    public void selectIndependentCompanyApplyState() throws Exception {
        EcmpOrg ecmpOrg = new EcmpOrg();
        ecmpOrg.setStatus(EcmpOrgVo.statusEnum.CLOUD.getCode());
        // MAC地址
        List<String> macList = MacTools.getMacList();
        String macAdd = macList.get(0);
        ecmpOrgMapper.selectEcmpOrgList(ecmpOrg).stream().forEach(x->{
            Map map = new HashMap();
            map.put("enterpriseId",enterpriseId);
            map.put("licenseContent",licenseContent);
            map.put("mac",macAdd);
            map.put("orgId",x.getDeptId());
            try {
                String resultJSON = OkHttpUtil.postForm(apiUrl+"/independentCompany/selectIndependentCompanyApplyState",map);
                Type type = new TypeToken<ApiResponse>() {
                }.getType();
                ApiResponse apiResponse = GsonUtils.jsonToBean(resultJSON, type);
                if(apiResponse.getMsg().equals("success")) {
                    LinkedTreeMap linkedTreeMap = ((LinkedTreeMap) apiResponse.getData());
                    if("Y000".equals(linkedTreeMap.get("state"))){
                        EcmpOrgVo ecmpOrgVo = new EcmpOrgVo();
                        ecmpOrgVo.setDeptId(x.getDeptId());
                        ecmpOrgVo.setStatus(EcmpOrgVo.statusEnum.OK.getCode());
                        ecmpOrgMapper.updateEcmpOrg(ecmpOrgVo);
                    }
                    if("N111".equals(linkedTreeMap.get("state"))){
                        EcmpOrgVo ecmpOrgVo = new EcmpOrgVo();
                        ecmpOrgVo.setDeptId(x.getDeptId());
                        ecmpOrgVo.setStatus(EcmpOrgVo.statusEnum.Fail.getCode());
                        ecmpOrgMapper.updateEcmpOrg(ecmpOrgVo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
