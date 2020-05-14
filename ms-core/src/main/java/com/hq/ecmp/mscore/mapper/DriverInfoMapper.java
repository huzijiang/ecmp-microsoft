package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.bo.SelectCarConditionBo;
import com.hq.ecmp.mscore.bo.SelectDriverConditionBo;
import com.hq.ecmp.mscore.bo.WaitSelectedCarBo;
import com.hq.ecmp.mscore.bo.WaitSelectedDriverBo;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.DriverCanUseCarsDTO;
import com.hq.ecmp.mscore.dto.DriverDTO;
import com.hq.ecmp.mscore.dto.DriverLoseDTO;

import com.hq.ecmp.mscore.vo.DriverOrderVo;
import com.hq.ecmp.mscore.vo.DriverVO;
import com.hq.ecmp.mscore.vo.PageResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface DriverInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public DriverInfo selectDriverInfoById(Long driverId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DriverInfo> selectDriverInfoList(DriverInfo driverInfo);

    /**
     * 根据车队查询集查询 司机信息
     * @param list
     * @return
     */
    public List<DriverInfo> selectDriverInfoListByIds(List list);

    /**
     * 新增【请填写功能名称】
     *
     * @param driverInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertDriverInfo(DriverInfo driverInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param driverInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateDriverInfo(DriverInfo driverInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverInfoById(Long driverId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param driverIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDriverInfoByIds(Long[] driverIds);
    public Integer createDriver(DriverCreateInfo driverCreateInfo);

    public List<DriverQueryResult> queryDriverList(DriverQuery query);

    public Integer queryDriverListCount(DriverQuery query);

    public DriverQueryResult queryDriverDetail(Long driverId);

    /**
     *驾驶员总数
     */
    public int queryCompanyDriver(@Param("companyId") Long companyId);


    DriverInfo selectDriverInfoByUserId(Long userId);

    /**
     * Updated upstream
     * 根据deptId查询归属驾驶员数量
     *
     * @param deptId 组织id
     * @return 结果
     */
    public int selectDriverCountByDeptId(Long deptId);

    /**
     * 禁用/启用  驾驶员--关联公司/部门/员工
     *
     * @param deptId 部门ID
     * @param state 状态 W001 待审   V000 生效中   NV00 失效
     * @return 结果
     */
    public int updateUseStatus(@Param("deptId") Long deptId, @Param("state") String state);

    /**
     * 禁用/启用  驾驶员
     *
     * @param driverId 驾驶员ID
     * @param state 状态 W001 待审   V000 生效中   NV00 失效
     * @return 结果
     */
    public int updateDriverStatus(@Param("driverId") Long driverId, @Param("state") String state);

    /**
     * 禁用/启用  驾驶员--关联员工
     *
     * @param userId 部门ID
     * @param state 状态 W001 待审   V000 生效中   NV00 失效
     * @return 结果
     */
    public int updateDriverUseStatus(@Param("userId") Long userId, @Param("state") String state);

    /**
     * 查询车辆可用驾驶员
     * @param driverId
     * @return
     */
    DriverInfo selectEffectiveDriverInfoById(Long driverId);

    /**
     *
     * @param phoneNumber
     * @return
     */
    public int driverItisExist( @Param("phoneNumber") String phoneNumber);

    /**
     * 禁用驾驶员
     * @param driverId
     * @return
     */
    int disableDriver(Long driverId);


    /**
     *驾驶员可用车辆列表
     * @param
     */
    public List<DriverCanUseCarsDTO> getDriverCanCar(@Param("driverId") Long driverId,@Param("state") String state,@Param("search") String search);
    /**
     *驾驶员失效列表,离职列表
     * @param
     */
    public List<DriverLoseDTO> getDriverLoseList(@Param("carGroupId") Long carGroupId,@Param("search") String search);
    /**
     * 驾驶员已离职数量
     */
    public Long getDriverLoseCount(@Param("carGroupId") Long carGroupId);
    /**
     * 已失效驾驶员进行删除
     */
    public int deleteDriver(Long driverId);

    /**
     * 修改驾驶员
     * @param driverCreateInfo
     * @return
     */
    public Integer updateDriver(DriverCreateInfo driverCreateInfo);

    /**
     * 修改驾驶员手机号
     * @param mobile
     * @return
     */
    public int updateDriverMobile(@Param("mobile") String mobile,@Param("driverId") Long driverId);

    /**
     * 设置驾驶员离职日期
     * @param dimTime
     * @return
     */
    public int updateDriverDimTime(@Param("dimTime")Date dimTime,@Param("driverId")Long driverId);



    /**
     * 查询driverId
     * @param userId
     * @return
     */
    Long selectDriverIdByUserId(Long userId);


    /**
     * 查询司机信息列表
     * @param selectDriverConditionBo
     * @return
     */
    public List<DriverInfo> dispatcherSelectDriver(SelectDriverConditionBo selectDriverConditionBo);

    /**
     *  通过 司机姓名 或者手机号 查询 司机信息列表
     * @param selectDriverConditionBo
     * @return
     */
    public List<DriverInfo> dispatcherSelectDriverUseDriverNameOrPhone(SelectDriverConditionBo selectDriverConditionBo);

    /**
     * 锁定司机
     * @param driverId
     * @return
     */
    public int lockDriver(@Param("driverId") long driverId);

    /**
     * 解除锁定司机
     * @param carGroupId
     * @return
     */

    public List<DriverQueryResult>  queryDriverInfoList(@Param("carGroupId")Long carGroupId,@Param("carId")Long carId,@Param("search")String search);

    public int unlockDriver(@Param("driverId") long driverId);


    public Integer queryDriverNumOfStateAndCarGroup(DriverQuery query);

    /**
     * 更新驾驶员的状态
     * @param list
     * @param state W001 待审核 V000 生效中 NV00 失效/离职
     * @return
     */
    public int updateDriverState(@Param("list") List<CarGroupDriverRelation> list,
                                 @Param("updateBy") String updateBy, @Param("state") String state);

    public List<DriverVO> selectDriverInfoByCarId(@Param("workState") String workState, @Param("itIsFullTime") String itIsFullTime, @Param("businessFlag") String businessFlag,@Param("carId") Long carId);

    List<DriverOrderVo> driverWorkOrderList(@Param("carGroupId")Long carGroupId,@Param("date")String date,@Param("search")String search);

    Long driverWorkOrderListCount(@Param("carGroupId")Long carGroupId,@Param("date")String date,@Param("search")String search);

    /**
     * 补单获取驾驶员列表
     * @param driverInfo
     * @return
     */
    List<DriverInfo> supplementObtainDriver(DriverInfo driverInfo);
}
