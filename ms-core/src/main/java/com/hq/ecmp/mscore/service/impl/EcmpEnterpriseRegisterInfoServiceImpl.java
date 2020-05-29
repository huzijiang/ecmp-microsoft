package com.hq.ecmp.mscore.service.impl;
import com.hq.core.aspectj.lang.annotation.Log;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.InvitionStateEnum;
import com.hq.ecmp.constant.InvitionTypeEnum;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.EcmpEnterpriseRegisterInfoService;
import com.hq.ecmp.mscore.service.ICarGroupDriverRelationService;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (EcmpEnterpriseRegisterInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-03-16 19:00:09
 */
@Service("ecmpEnterpriseRegisterInfoService")
@Slf4j
public class EcmpEnterpriseRegisterInfoServiceImpl implements EcmpEnterpriseRegisterInfoService {
    @Resource
    private EcmpEnterpriseRegisterInfoMapper ecmpEnterpriseRegisterInfoMapper;
    @Resource
    private EcmpEnterpriseInvitationInfoMapper invitationInfoMapper;
    @Autowired
    private ICarGroupDriverRelationService carGroupDriverRelationService;
    @Resource
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private DriverWorkInfoMapper driverWorkInfoMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IDriverInfoService driverInfoService;
    @Autowired
    private DriverNatureInfoMapper driverNatureInfoMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param registerId 主键
     * @return 实例对象
     */
    @Override
    public EcmpEnterpriseRegisterInfo queryById(Long registerId) {
        return this.ecmpEnterpriseRegisterInfoMapper.queryById(registerId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<EcmpEnterpriseRegisterInfo> queryAllByLimit(int offset, int limit) {
        return this.ecmpEnterpriseRegisterInfoMapper.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param ecmpEnterpriseRegisterInfo 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpEnterpriseRegisterInfo insert(EcmpEnterpriseRegisterInfo ecmpEnterpriseRegisterInfo) {
        this.ecmpEnterpriseRegisterInfoMapper.insert(ecmpEnterpriseRegisterInfo);
        return ecmpEnterpriseRegisterInfo;
    }

    /**
     * 修改数据
     *
     * @param ecmpEnterpriseRegisterInfo 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpEnterpriseRegisterInfo update(EcmpEnterpriseRegisterInfo ecmpEnterpriseRegisterInfo) {
        this.ecmpEnterpriseRegisterInfoMapper.update(ecmpEnterpriseRegisterInfo);
        return this.queryById(ecmpEnterpriseRegisterInfo.getRegisterId());
    }

    /**
     * 通过主键删除数据
     *
     * @param registerId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long registerId) {
        return this.ecmpEnterpriseRegisterInfoMapper.deleteById(registerId) > 0;
    }
    /**
     * 手机号是否已经申请注册
     */
    @Override
    public  int itIsRegistration(String phoneNum,String state){
        return ecmpEnterpriseRegisterInfoMapper.itIsRegistration(phoneNum,state);
    }

    /**
     * 待审批数量 员工
     * @param
     */
    @Override
    public int waitAmount(Long deptId,String type){
        return ecmpEnterpriseRegisterInfoMapper.waitAmountCount(deptId,type,null);
    }

    /**
     * 待审批列表-员工
     * @param
     */
    @Override
    public PageResult<RegisterUserVO> queryRegisterUserWait(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<RegisterUserVO> registerUserVOS = ecmpEnterpriseRegisterInfoMapper.queryRegisterUserWait(pageRequest.getDeptId(),pageRequest.getType());
        Long count= ecmpEnterpriseRegisterInfoMapper.queryRegisterUserWaitCount(pageRequest.getDeptId(),pageRequest.getType());
        return new PageResult<>(count,registerUserVOS);
    }
    /**
     * 待审批列表-驾驶员
     * @param
     */
    @Override
    public PageResult<RegisterDriverVO> queryRegisterDriverWait(Integer pageNum, Integer pageSize,Long carGroupId,String type,String search){

        PageHelper.startPage(pageNum,pageSize);
        List<RegisterDriverVO> registerDriverVOS= ecmpEnterpriseRegisterInfoMapper.queryRegisterDriverWait(carGroupId,type,search);
        PageInfo<RegisterDriverVO> info = new PageInfo<>(registerDriverVOS);
        return new PageResult<>(info.getTotal(),info.getPages(),registerDriverVOS);

    }

    /**
     * 注册申请：拒绝/通过
     */
    @Override
    public int updateRegisterState(RegisterDTO registerDTO){
        return ecmpEnterpriseRegisterInfoMapper.updateRegisterState(registerDTO);
    }
    /**
     * 员工注册
     * @param userRegisterDTO
     * @return
     */
    @Override
    public int insertUserRegister(UserRegisterDTO userRegisterDTO){
        userRegisterDTO.setCreateTime(DateUtils.getNowDate());
        userRegisterDTO.setType("T001");
        userRegisterDTO.setState("S000");
        int m = ecmpEnterpriseRegisterInfoMapper.insertUserRegister(userRegisterDTO);
        return m;
    }

    @Override
    public int updateRegisterApprove(Long registerId,Long userId,String reason,String state) throws Exception {
        EcmpEnterpriseRegisterInfo registerInfo = ecmpEnterpriseRegisterInfoMapper.queryById(registerId);
        if (registerInfo==null){
            throw new Exception("该注册员工id:"+registerId+"不存在");
        }
        EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo = invitationInfoMapper.queryById(registerInfo.getInvitationId());
        if (ecmpEnterpriseInvitationInfo==null){
            throw new Exception("该注册员工来源不明");
        }
        if (!InvitionTypeEnum.USER.getKey().equals(registerInfo.getType())){//不是员工
            throw new Exception("不可审核驾驶员");
        }
        registerInfo.setAuditTime(new Date());
        registerInfo.setAuditor(userId);
        registerInfo.setUpdateBy(String.valueOf(userId));
        registerInfo.setUpdateTime(new Date());
        registerInfo.setState(state);
        if (InvitionStateEnum.APPROVEREJECT.getKey().equals(state)){
            registerInfo.setRejectReason(reason);
        }
        int count = ecmpEnterpriseRegisterInfoMapper.update(registerInfo);
        log.info(registerId+"邀请注册被员工"+userId+"审核通过");
        if (InvitionStateEnum.APPROVEREJECT.getKey().equals(state)){
            return count;
        }
        if (count>0){
            EcmpUser ecmpUser = new EcmpUser();
            ecmpUser.setPhonenumber(registerInfo.getMobile());
            ecmpUser.setUserName(registerInfo.getMobile());
            ecmpUser.setDeptId(ecmpEnterpriseInvitationInfo.getDepartmentId());
            ecmpUser.setNickName(registerInfo.getName());
            ecmpUser.setStatus(CommonConstant.SWITCH_ON);
            ecmpUser.setEmail(registerInfo.getEmail());
            ecmpUser.setItIsDispatcher(CommonConstant.SWITCH_ON);
            ecmpUser.setCreateBy(String.valueOf(userId));
            ecmpUser.setCreateTime(new Date());
            ecmpUser.setSex(registerInfo.getGender());
            ecmpUser.setUserType(CommonConstant.IS_PEER);
            ecmpUser.setJobNumber(registerInfo.getJobNumber());
            ecmpUser.setDelFlag(CommonConstant.SWITCH_ON);
            ecmpUser.setItIsDriver(CommonConstant.SWITCH_ON);
            ecmpUser.setRemark("邀请注册");
            ecmpUserMapper.insertEcmpUser(ecmpUser);
            log.info("员工"+registerInfo.getMobile()+"审核添加成功");
        }
        return count;
    }

    @Override
    public void updateRegisterRefuse(Long registerId, String reason) throws Exception {
        EcmpEnterpriseRegisterInfo registerInfo = ecmpEnterpriseRegisterInfoMapper.queryById(registerId);
        if (registerInfo==null){
            throw new Exception("该注册员工id:"+registerId+"不存在");
        }
        EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo = invitationInfoMapper.queryById(registerInfo.getInvitationId());
        if (ecmpEnterpriseInvitationInfo==null){
            throw new Exception("该注册员工来源不明");
        }
    }
    /**
     * 驾驶员注册
     * @param driverRegisterDTO
     * @return
     */
    @Override
    public int insertDriverRegister(DriverRegisterDTO driverRegisterDTO){
        DriverRegisterInsertDTO driver = new DriverRegisterInsertDTO();
        driver.setCarGroupId(driverRegisterDTO.getCarGroupId());
        driver.setGender(driverRegisterDTO.getGender());
        driver.setIdCard(driverRegisterDTO.getIdCard());
        driver.setJobNumber(driverRegisterDTO.getJobNumber());
        //驾驶证过期日期
        Date expire = driverRegisterDTO.getLicenseExpireDate();
        driver.setLicenseExpireDate(expire);
        //驾驶证有效开始日期
        Date issue = driverRegisterDTO.getLicenseIssueDate();
        driver.setLicenseIssueDate(issue);
        //初次领证日期
        Date initIssue = driverRegisterDTO.getLicenseInitIssueDate();
        driver.setLicenseInitIssueDate(initIssue);
        driver.setInvitationId(driverRegisterDTO.getInvitationId());
        driver.setLicenseImages(driverRegisterDTO.getLicenseImages());
        driver.setName(driverRegisterDTO.getName());
        driver.setMobile(driverRegisterDTO.getMobile());
        //T001:  注册员工   T002： 注册驾驶员
        driver.setType("T002");
        //审核状态  S000 申请中  S001 申请通过  S002 申请拒绝
        driver.setState("S000");
        driver.setReason(driverRegisterDTO.getReason());
        driver.setCreateTime(DateUtils.getNowDate());
        //驾驶证号码
        driver.setLicenseNumber(driverRegisterDTO.getLicenseNumber());
        driver.setLicenseType(driverRegisterDTO.getLicenseType());
        //新增驾驶员注册信息
        int i = ecmpEnterpriseRegisterInfoMapper.insertDriverRegister(driver);
        return i;
    }
    @Override
    public int updateRegisterDriverApprove(Long companyId,Long registerId,Long userId,String reason,String state) throws Exception {
        //1.根据注册id查询用户注册信息
        EcmpEnterpriseRegisterInfo registerInfo = ecmpEnterpriseRegisterInfoMapper.queryById(registerId);

        if (registerInfo==null){
            throw new Exception("该注册驾驶员id:"+registerId+"不存在");
        }
        //2.根据邀请id查询邀请信息
        EcmpEnterpriseInvitationInfo ecmpEnterpriseInvitationInfo = invitationInfoMapper.queryById(registerInfo.getInvitationId());
        if (ecmpEnterpriseInvitationInfo==null){
            throw new Exception("该注册驾驶员来源不明");
        }
        if (!InvitionTypeEnum.DRIVER.getKey().equals(registerInfo.getType())){//不是驾驶员
            throw new Exception("不可审核员工");
        }
        registerInfo.setAuditTime(new Date());
        registerInfo.setAuditor(userId);
        registerInfo.setUpdateBy(String.valueOf(userId));
        registerInfo.setUpdateTime(new Date());
        registerInfo.setState(state);
        registerInfo.setRejectReason(reason);
        //3.修改注册信息状态为通过或者拒绝
        int count = ecmpEnterpriseRegisterInfoMapper.update(registerInfo);
        log.info(registerId+"邀请注册被员工"+userId+"审核通过");
        if (InvitionStateEnum.APPROVEREJECT.getKey().equals(state)){
            //如果是拒绝通过，则直接返回
            return count;
        }
        //4.查询驾驶员性质
        if (count>0){
            DriverNatureInfo driverNatureInfo = driverNatureInfoMapper.selectDriverNatureInfoByIncitationId(ecmpEnterpriseInvitationInfo.getInvitationId());
            DriverCreateInfo driverCreate = new DriverCreateInfo();
            if(driverNatureInfo != null){
               driverCreate.setDriverNature(driverNatureInfo.getDriverNature());
               driverCreate.setHireBeginTime(driverNatureInfo.getHireBeginTime());
               driverCreate.setHireEndTime(driverNatureInfo.getHireEndTime());
               driverCreate.setBorrowBeginTime(driverCreate.getHireBeginTime());
               driverCreate.setBorrowEndTime(driverCreate.getHireEndTime());
               driverCreate.setDriverNature(driverNatureInfo.getDriverNature());
            }
            driverCreate.setMobile(registerInfo.getMobile());
            driverCreate.setDriverName(registerInfo.getName());
           // driverCreate.setCarGroupId(registerInfo.getCarGroupId());
            driverCreate.setIdCard(registerInfo.getIdCard());
            //初始化驾驶员状态
            driverInfoService.initDriverState(driverCreate);
            driverCreate.setLicenseType(registerInfo.getLicenseType());
            driverCreate.setLicenseNumber(registerInfo.getLicenseNumber());
            driverCreate.setLicensePhoto(registerInfo.getLicenseImages());
            driverCreate.setLicenseInitIssueDate(registerInfo.getLicenseInitIssueDate());
            driverCreate.setLicenseIssueDate(registerInfo.getLicenseIssueDate());
            driverCreate.setLicenseExpireDate(registerInfo.getLicenseExpireDate());
            driverCreate.setJobNumber(registerInfo.getJobNumber());
            driverCreate.setLockState("0000");
            driverCreate.setGender(registerInfo.getGender());
            driverCreate.setCreateTime(new Date());
            driverCreate.setCreateBy(userId);
            driverCreate.setInvitationId(ecmpEnterpriseInvitationInfo.getInvitationId());
            driverCreate.setRegimenIds(ecmpEnterpriseInvitationInfo.getRegimeIds());
            driverCreate.setCompanyId(companyId);
            driverCreate.setOptUserId(userId);
            driverCreate.setCarGroupId(registerInfo.getCarGroupId());
            //5.审核通过驾驶员注册后，新增驾驶员表数据  此处应该走 DriverInfoServiceImpl.createDriver()创建驾驶员逻辑
           driverInfoService.createDriver(driverCreate);
            /*int i= driverInfoMapper.createDriver(driverCreate);
            if(i!=0){
                Long driverId = driverCreate.getDriverId();
                log.info("新增驾驶员ID:"+driverId);
                //生成驾驶员-车队关系记录
                CarGroupDriverRelation carGroupDriverRelation = new CarGroupDriverRelation();
                carGroupDriverRelation.setCarGroupId(registerInfo.getCarGroupId());
                carGroupDriverRelation.setDriverId(driverId);
                carGroupDriverRelation.setCreateBy(userId.toString());
                carGroupDriverRelation.setCreateTime(new Date());
                int j = carGroupDriverRelationService.insertCarGroupDriverRelation(carGroupDriverRelation);
                if(j!=0){
                    log.info("驾驶员"+registerInfo.getMobile()+"车队驾驶员关联表新增成功");
                    String date=DateUtils.getDate();
                    log.info("获取当前工作日期:"+date+"驾驶员ID:"+driverId);
                    if(setDriverWorkInfo(driverId)) {
                        log.info("驾驶员"+registerInfo.getMobile()+"排班初始化成功");
                    }

                }

            }*/

        }
        return count;
    }
    @Override
    public RegisterDriverDetailVO queryDriverRegDetail(Long registerId){
        return ecmpEnterpriseRegisterInfoMapper.queryDriverRegDetail(registerId);
    }

    /**
         * 排班初始化
         * @param driverId
         * @return
         */
        public boolean setDriverWorkInfo(Long driverId) {

            List<CloudWorkIDateVo> workDateList = driverWorkInfoMapper.getCloudWorkDateList();

            //获取调用接口的用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getUser().getUserId();
            List<DriverWorkInfoVo> list = new ArrayList<>();
            if (workDateList != null && workDateList.size() > 0) {
                for (int i = 0; i < workDateList.size(); i++) {
                    DriverWorkInfoVo driverWorkInfoVo = new DriverWorkInfoVo();
                    driverWorkInfoVo.setDriverId(driverId);
                    driverWorkInfoVo.setCalendarDate(workDateList.get(i).getCalendarDate());
                    driverWorkInfoVo.setOnDutyRegisteTime(workDateList.get(i).getWorkStart());
                    driverWorkInfoVo.setOffDutyRegisteTime(workDateList.get(i).getWorkEnd());
                    driverWorkInfoVo.setTodayItIsOnDuty("1111");
                    String itIsDuty=workDateList.get(i).getItIsWork();
                    if("0000".equals(itIsDuty)){
                        driverWorkInfoVo.setLeaveStatus("X999");
                    }else if("1111".equals(itIsDuty)){
                        driverWorkInfoVo.setLeaveStatus("X003");
                    }
                    driverWorkInfoVo.setCreatBy(userId);
                    driverWorkInfoVo.setCreatTime(DateUtils.getNowDate());
                    list.add(driverWorkInfoVo);
                }
                int m = driverWorkInfoMapper.insertDriverWorkInfo(list);
                if (m > 0) {
                    int n = driverWorkInfoMapper.updateDriverWork(driverId);
                    return true;
                }
            }
        return false;
    }



}