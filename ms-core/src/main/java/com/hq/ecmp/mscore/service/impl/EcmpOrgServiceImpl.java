package com.hq.ecmp.mscore.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
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
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.vo.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    /**
     * 显示公司组织结构
     *
     * @param deptId 部门ID deptType组织类型 1公司 2部门
     * @return deptList 部门列表
     */
    @Override
    public List<EcmpOrgDto> selectCombinationOfCompany(Long deptId,String deptType){

       /* int bl = 0;
        if(deptType==null){
            deptType = "1";
            bl = 1;
        }*/
        List<EcmpOrgDto> ecmpOrgList = new ArrayList<>();
        if(deptId==null){
            Long parentId = 0L;
            //默认查询所有公司列表
            ecmpOrgList = ecmpOrgMapper.selectByEcmpOrgParentId(deptId,parentId,null);
        }else {
            ecmpOrgList = ecmpOrgMapper.selectByEcmpOrgParentId(deptId,null,null);
        }
        if(ecmpOrgList.size()>0){
            for (EcmpOrgDto company:ecmpOrgList) {
                List<EcmpOrgDto> ecmpOrgs = loadEcmpOrg(null,company.getDeptId(), deptType);
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
    public List<EcmpOrgDto> selectDeptComTree(Long deptId,String deptType){
        EcmpOrgDto subDetail = ecmpOrgMapper.getSubComDept(deptId);
        Long subComDeptId=subDetail.getDeptId();
        List<EcmpOrgDto> ecmpOrgDtoList = ecmpOrgMapper.selectByEcmpOrgParentId(subComDeptId, null, OrgConstant.DEPT_TYPE_1);
        return ecmpOrgDtoList;
    }

    public List<EcmpOrgDto> loadEcmpOrg(Long deptId,Long parentId,String deptType) {
        List<EcmpOrgDto> list = new ArrayList<>();
        List<EcmpOrgDto> deptList = ecmpOrgMapper.selectByEcmpOrgParentId(null,parentId,deptType);
        if(deptList.size()>0){
            for (EcmpOrgDto ecmpOrg:deptList) {
                list.add(ecmpOrg);
                ecmpOrg.setDeptList(loadEcmpOrg(null,ecmpOrg.getDeptId(),deptType));
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





    //公司车队树
    @Override
    public List<CompanyCarGroupTreeVO> selectCompanyCarGroupTree(Long deptId) {
       /* if(deptId == null){
            EcmpOrg ecmpOrg = new EcmpOrg();
            ecmpOrg.setParentId(0L);
            List<EcmpOrg> ecmpOrgs = ecmpOrgMapper.selectEcmpOrgList(ecmpOrg);
            deptId = ecmpOrgs.get(0).getDeptId();
        }*/
        /*EcmpOrg ecmpOrg = new EcmpOrg();
        ecmpOrg.setParentId(deptId);*/
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
                    tree.get(i).setCarGroupTreeVO(carGroupInfoService.selectCarGroupTree(tree.get(i).getDeptId()));
                    tree.get(i).setChildrenList(this.selectCompanyCarGroupTree(tree.get(i).getDeptId()));
                }
            }
        }
        return tree;
    }



    /*根据公司id查询公司车队总人数*/
    @Override
    public CarGroupCountVO selectCarGroupCount(Long deptId) {
        EcmpOrg ecmpOrg = ecmpOrgMapper.selectEcmpOrgById(deptId);
        CarGroupCountVO carGroupCountVO = new CarGroupCountVO();
        //公司编号
        if(ObjectUtils.isNotEmpty(ecmpOrgMapper)){
            carGroupCountVO.setDeptCode(ecmpOrg.getDeptCode());
        }
        String leader = ecmpOrg.getLeader();
        if( leader != null) {
            EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(Long.valueOf(leader));
            //公司负责人
            if(ecmpUser != null){
                carGroupCountVO.setLeaderName(ecmpUser.getUserName());
            }
        }
        CarGroupInfo carGroupInfo = new CarGroupInfo();
        carGroupInfo.setOwnerCompany(deptId);
        List<CarGroupInfo> carGroupInfos = carGroupInfoMapper.selectCarGroupInfoList(carGroupInfo);
        int size = carGroupInfos.size();
        int num = 0;
        if(size > 0){
            for (CarGroupInfo groupInfo : carGroupInfos) {
                if (carGroupInfo != null) {
                    Long carGroupId = groupInfo.getCarGroupId();
                    int i = carGroupDriverRelationMapper.selectCountDriver(carGroupId);
                    num += i;
                }
            }
        }
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
        return ecmpOrgMapper.queryCompanyListCount(ecmpOrg.getDeptId(),ecmpOrg.getDeptType());
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
        if (CommonConstant.START.equals(ecmpOrgVo.getDeptType())){//公司
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
        int iz = ecmpOrgMapper.addDept(ecmpOrgVo);
        if (CommonConstant.START.equals(ecmpOrgVo.getDeptType())){//公司
            if (!flag){//公司主管不存在
                //新建公司主管
                EcmpUser companyUser=new EcmpUser();
                companyUser.setUserName(ecmpOrgVo.getPhone());
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
        //根据用户的部门id查询用户部门对象
        EcmpOrg userEcmpOrg = ecmpOrgMapper.selectEcmpOrgById(ecmpUser.getDeptId());
        //根据公司id（以及部门名称模糊）查询部门对象列表
        EcmpOrg ecmpOrg = new EcmpOrg();
        ecmpOrg.setDeptName(name);
        //1
       List<EcmpOrg> ecmpOrgs = ecmpOrgMapper.selectEcmpOrgList(ecmpOrg);
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

        //查询该组织下的驾驶员信息 如果该公司没有员工，就没有驾驶员
        int driverNum =driverInfoMapper.selectDriverCountByDeptId(deptId);

        //查询该组织下的车辆信息  如果carNum>0不可删除
        int carNum=carInfoMapper.selectCarCountByDeptId(deptId);

        //查询该组织下的车队信息  如果该组织没有车也就没有车队
        int carGroupNum = carGroupInfoMapper.selectCountByOrgdeptId(deptId);

        //如果满足上述条件，执行删除操作
        if(ecmpOrgNum==0&&ecmpUserNum==0&&carNum==0){
            int delFlag = ecmpOrgMapper.updateDelFlagById(deptId,deptType);
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
    public List<EcmpOrgDto> selectCompanyByDeptNameOrCode(String deptNameOrCode){
        List<EcmpOrgDto> ecmpOrgDtoList=new ArrayList<>();
        List<Long> deptIds = ecmpOrgMapper.selectDeptIdsByDeptNameOrCode(deptNameOrCode, deptNameOrCode,OrgConstant.DEPT_TYPE_1);
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
        return ecmpOrgDtoList;
    }

    /**
     * 按照部门名称或编号模糊查询匹配的列表
     * @param deptNameOrCode
     * @return 结果
     */
    @Override
    public List<EcmpOrgDto> selectDeptByDeptNameOrCode(String deptNameOrCode){
        List<EcmpOrgDto> ecmpOrgDtoList=new ArrayList<>();
        List<Long> deptIds = ecmpOrgMapper.selectDeptIdsByDeptNameOrCode(deptNameOrCode, deptNameOrCode,OrgConstant.DEPT_TYPE_2);
        if(deptIds.size()>0){
            for (int i = 0; i < deptIds.size(); i++) {
                EcmpOrgDto ecmpOrgDto = ecmpOrgMapper.selectDeptByDeptNameOrCode(deptNameOrCode, deptNameOrCode, deptIds.get(i));
                String leader=ecmpOrgDto.getLeader();
                leader = changeUserIdToNickNames(leader);
                ecmpOrgDto.setLeader(leader);
                ecmpOrgDtoList.add(ecmpOrgDto);
            }
        }
        return ecmpOrgDtoList;
    }

    @Override
    public List<EcmpOrgDto> selectDeptByCompany(Long deptId) {
        return null;
    }

	@Override
	public List<Long> queryDeptIdOfCompany(Long deptId) {
		List<Long> result=new ArrayList<Long>();
		List<EcmpOrgDto> selectCombinationOfCompany = selectCombinationOfCompany(deptId, "1");
		recursion(selectCombinationOfCompany, result);
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

    /**
     *查询分/子公司下的部门名称和deptId
     * @return
     */
   // @Override
    /*public List<EcmpOrgDto> selectDeptByCompany(Long deptId) {
        return ecmpOrgMapper.selectDeptByCompany(deptId);
    }*/

}
