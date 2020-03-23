package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.DriverWorkInfo;
import com.hq.ecmp.mscore.mapper.DriverInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverServiceStateInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverWorkInfoMapper;
import com.hq.ecmp.mscore.service.IDriverWorkInfoService;
import com.hq.ecmp.mscore.vo.DriverDutyPlanVO;
import com.hq.ecmp.mscore.vo.DriverDutySummaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class DriverWorkInfoServiceImpl implements IDriverWorkInfoService
{
    @Autowired
    private DriverWorkInfoMapper driverWorkInfoMapper;
    @Autowired
    private DriverServiceStateInfoMapper driverServiceStateInfoMapper;
    @Autowired
    private DriverInfoMapper driverInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param workId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public DriverWorkInfo selectDriverWorkInfoById(Long workId)
    {
        return driverWorkInfoMapper.selectDriverWorkInfoById(workId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverWorkInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<DriverWorkInfo> selectDriverWorkInfoList(DriverWorkInfo driverWorkInfo)
    {
        return driverWorkInfoMapper.selectDriverWorkInfoList(driverWorkInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param driverWorkInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertDriverWorkInfo(DriverWorkInfo driverWorkInfo)
    {
        driverWorkInfo.setCreateTime(DateUtils.getNowDate());
        return driverWorkInfoMapper.insertDriverWorkInfo(driverWorkInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param driverWorkInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateDriverWorkInfo(DriverWorkInfo driverWorkInfo)
    {
        driverWorkInfo.setUpdateTime(DateUtils.getNowDate());
        return driverWorkInfoMapper.updateDriverWorkInfo(driverWorkInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param workIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverWorkInfoByIds(Long[] workIds)
    {
        return driverWorkInfoMapper.deleteDriverWorkInfoByIds(workIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param workId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteDriverWorkInfoById(Long workId)
    {
        return driverWorkInfoMapper.deleteDriverWorkInfoById(workId);
    }

    /**
     * 查询司机当月排班/出勤信息
     * @param scheduleDate
     * @return
     */
    @Override
    public List<DriverDutyPlanVO> selectDriverWorkInfoByMonth(String scheduleDate, Long userId) {
        List<DriverDutyPlanVO> list = driverWorkInfoMapper.selectDriverWorkInfoByMonth(scheduleDate,userId);
        /*List<DriverDutyPlanVO> list = new ArrayList<>();
        DriverDutyPlanVO v1 = DriverDutyPlanVO.builder().dutyDate("2020-03-12").status("S002").build();
        DriverDutyPlanVO v2 = DriverDutyPlanVO.builder().dutyDate("2020-03-15").status("S001").build();
        DriverDutyPlanVO v3 = DriverDutyPlanVO.builder().dutyDate("2020-03-16").status("S002").build();
        list.add(v1);
        list.add(v2);
        list.add(v3);*/
        return list;
    }

    /**
     * 加载司机应该出勤/已出勤天数
     * @param scheduleDate
     * @return
     */
    @Override
    public DriverDutySummaryVO selectDriverDutySummary(String scheduleDate, Long userId) {
        //查询司机driverId
        Long driverId = driverInfoMapper.selectDriverIdByUserId(userId);
        //查询司机应出勤天数
        int shouldDutyDays = driverWorkInfoMapper.selectDriverShouldDutyDays(scheduleDate,driverId);
        //查询司机已出勤天数
        int alreadyDutyDays = driverWorkInfoMapper.selectDriverAlreadyDutyDays(scheduleDate,driverId);
        DriverDutySummaryVO build = DriverDutySummaryVO.builder().alreadyDuty(alreadyDutyDays)
                .shouldDuty(shouldDutyDays).build();
        return build;
    }

    /**
     * 按月查询司机排班信息
     * @param scheduleDate
     * @param userId
     * @return
     */
    @Override
    public DriverDutyPlanVO selectDriverScheduleByMonth(String scheduleDate, Long userId) {
        DriverDutyPlanVO driverDutyPlanVO = new DriverDutyPlanVO();
        //查询司机driverId
        Long driverId = driverInfoMapper.selectDriverIdByUserId(userId);
        //查询司机上班时间
      //  List<Date> dutyDate = driverWorkInfoMapper.selectDutyDateByMonth(scheduleDate,driverId);
        //查询司机休假时间
        List<String> holidays = driverWorkInfoMapper.selectHolidaysByMonth(scheduleDate,driverId);
        driverDutyPlanVO.setHolidays(holidays);
        return driverDutyPlanVO;
    }

}
