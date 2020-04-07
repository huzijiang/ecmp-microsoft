package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.DriverWorkInfo;
import com.hq.ecmp.mscore.vo.*;

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
    DriverDutySummaryVO selectDriverDutySummary(String scheduleDate, Long userId);

    /**
     * 按月查询司机排班信息
     * @param scheduleDate
     * @param userId
     * @return
     */
    DriverDutyPlanVO selectDriverScheduleByMonth(String scheduleDate, Long userId);

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
}
