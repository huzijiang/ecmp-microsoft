package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.DriverWorkInfo;
import com.hq.ecmp.mscore.vo.DriverDutyPlanVO;
import com.hq.ecmp.mscore.vo.DriverDutySummaryVO;
import com.hq.ecmp.mscore.vo.DriverDutyWorkVO;
import com.hq.ecmp.mscore.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IDriverWorkInfoService
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
     * 批量删除【请填写功能名称】
     *
     * @param workIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverWorkInfoByIds(Long[] workIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param workId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverWorkInfoById(Long workId);

    /**
     * 查询司机当月排班/出勤信息
     * @param scheduleDate
     * @return
     */
    List<DriverDutyPlanVO> selectDriverWorkInfoByMonth(String scheduleDate, Long userId);

    /**
     * 加载司机应该出勤/已出勤天数
     * @param scheduleDate
     * @return
     */
    DriverDutySummaryVO selectDriverDutySummary(String scheduleDate, Long driverId);

    /**
     * 按月查询司机排班信息
     * @param scheduleDate
     * @param
     * @return
     */
    DriverDutyPlanVO selectDriverScheduleByMonth(String scheduleDate, Long driverId);

    public DriverDutyWorkVO selectDriverSchedule(String scheduleDate, Long driverId);

    /**
     * 按月获取司机的排班详情
     * @param driverId
     * @param month
     * @return
     */
    List<DriverWorkInfoMonthVo> getDriverWorkInfoMonthList(Long driverId,String month);

    /**
     * 按月更新司机的排班信息
     * @param driverWorkInfoDetailVo
     * @param userId  更新人
     */
    void updateDriverWorkDetailMonth(DriverWorkInfoDetailVo driverWorkInfoDetailVo,Long userId);

    //public DriverDutyWorkVO selectSchedule(String scheduleDate);
    /**
     * 按月获取全部司机的排班详情
     * @param month
     * @return
     */
    List<WorkInfoMonthVo> getWorkInfoMonthList(String month,Long companyId);

    /**
     * 按月更新全部司机的排班信息
     * @param workInfoDetailVo
     *
     */
    void updateWorkDetailMonth(WorkInfoDetailVo workInfoDetailVo,Long userId);

    /**
     * //从云端获取一年的节假日修改本地数据的cloud_work_date_info表
     */
    void SchedulingTimingTask();
}
