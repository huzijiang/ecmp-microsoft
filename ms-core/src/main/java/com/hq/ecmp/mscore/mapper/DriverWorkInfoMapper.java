package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.DriverWorkInfo;
import com.hq.ecmp.mscore.vo.*;
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
public interface DriverWorkInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param workId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public DriverWorkInfo selectDriverWorkInfoById(Long workId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverWorkInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DriverWorkInfo> selectDriverWorkInfoList(DriverWorkInfo driverWorkInfo);
    /**
     * 修改【请填写功能名称】
     *
     * @param driverWorkInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateDriverWorkInfo(DriverWorkInfo driverWorkInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param workId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverWorkInfoById(Long workId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param workIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteDriverWorkInfoByIds(Long[] workIds);

    /**
     * 查询司机应出勤天数
     * @param scheduleDate
     * @param driverId
     * @return
     */
    int selectDriverShouldDutyDays(@Param("scheduleDate") String scheduleDate, @Param("driverId") Long driverId);

    /**
     * 查询司机当月排班日期对应的出勤情况
     * @param scheduleDate
     * @param driverId
     * @return
     */
    List<DriverDutyPlanVO> selectDriverWorkInfoByMonth(@Param("scheduleDate") String scheduleDate, @Param("driverId") Long driverId);

    /**
     * 按月查询司机上班时间安排
     * @param scheduleDate
     * @param
     * @return
     */
    List<String> selectDutyDateByMonth(@Param("scheduleDate") String scheduleDate, @Param("driverId") Long driverId);

    /**
     * 查询司机休假时间
     * @param scheduleDate
     * @param driverId
     * @return
     */
    List<String> selectHolidaysByMonth(@Param("scheduleDate")String scheduleDate, @Param("driverId") Long driverId);

    /**
     * 查询司机某月已出勤天数
     * @param scheduleDate
     * @param driverId
     * @return
     */
    int selectDriverAlreadyDutyDays(@Param("scheduleDate") String scheduleDate,@Param("driverId") Long driverId);

    /**
     * 按月获取司机的排班详情
     * @param driverId
     * @param month
     * @return
     */
    List<DriverWorkInfoMonthVo> getDriverWorkInfoMonthList(@Param("driverId") Long driverId,@Param("month") String month);

    /**
     * 按月更新司机的排班信息
     * @param driverWorkInfoMonthVos
     */
    void updateDriverWorkDetailMonth(@Param("list") List<DriverWorkInfoMonthVo> driverWorkInfoMonthVos,@Param("userId") Long userId,@Param("updateTime") Date updateTime);

    /**
     * 按月获取司机的排班详情
     * @param month
     * @param month
     * @return
     */
    List<WorkInfoMonthVo> getWorkInfoMonthList(@Param("month") String month,@Param("companyId") Long companyId);

    /**
     * 按月更新司机的排班信息
     * @param list
     */
    void updateWorkDetailMonth(@Param("list") List<WorkInfoMonthVo> list,@Param("userId") Long userId,@Param("updateTime") Date updateTime);
    void updateWorkDetailMonthByDriverInfo(@Param("list") List<WorkInfoMonthVo> list,@Param("userId") Long userId,@Param("updateTime") Date updateTime);
    /**
     * 按月更新司机的排班信息
     * @param list
     * @return
     */
    int insertDriverWorkInfo(List<DriverWorkInfoVo> list);
    int updateDriverWork(@Param("driverId") Long driverId);
    /**
     * 获取初始化排班数据
     */
    List<CloudWorkIDateVo> getCloudWorkDateList();

}
