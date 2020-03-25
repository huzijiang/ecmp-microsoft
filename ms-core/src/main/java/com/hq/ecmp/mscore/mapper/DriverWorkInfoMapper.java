package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.DriverWorkInfo;
import com.hq.ecmp.mscore.vo.DriverDutyPlanVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
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
     * 新增【请填写功能名称】
     *
     * @param driverWorkInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertDriverWorkInfo(DriverWorkInfo driverWorkInfo);

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
    List<Date> selectDutyDateByMonth(@Param("scheduleDate") String scheduleDate, @Param("driverId") Long driverId);

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
}
