package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.DriverCarRelationInfo;
import com.hq.ecmp.mscore.domain.DriverWorkInfo;
import com.hq.ecmp.mscore.mapper.DriverInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverServiceStateInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverWorkInfoMapper;
import com.hq.ecmp.mscore.service.IDriverWorkInfoService;
import com.hq.ecmp.mscore.vo.DriverDutyPlanVO;
import com.hq.ecmp.mscore.vo.DriverDutySummaryVO;
import com.hq.ecmp.mscore.vo.DriverDutyWorkVO;
import com.hq.ecmp.mscore.vo.*;
import org.apache.commons.collections.CollectionUtils;
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
        return list;
    }

    /**
     * 加载司机应该出勤/已出勤天数
     * @param scheduleDate
     * @return
     */
    @Override
    public DriverDutySummaryVO selectDriverDutySummary(String scheduleDate, Long driverId) {
        //查询司机driverId
       // Long driverId = driverInfoMapper.selectDriverIdByUserId(userId);
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
     * @param
     * @return
     */
    @Override
    public DriverDutyPlanVO selectDriverScheduleByMonth(String scheduleDate, Long driverId) {
        DriverDutyPlanVO driverDutyPlanVO = new DriverDutyPlanVO();
        //查询司机driverId
       // Long driverId = driverInfoMapper.selectDriverIdByUserId(userId);
        //查询司机上班时间
        List<String> dutyDate = driverWorkInfoMapper.selectDutyDateByMonth(scheduleDate,driverId);
        //查询司机休假时间
        List<String> holidays = driverWorkInfoMapper.selectHolidaysByMonth(scheduleDate,driverId);
        driverDutyPlanVO.setHolidays(holidays);
        driverDutyPlanVO.setDutyDate(dutyDate);
        return driverDutyPlanVO;
    }

    /**
     * 按查询司机排班详情信息
     * @param scheduleDate
     * @param driverId
     * @return
     */
    @Override
    public DriverDutyWorkVO selectDriverSchedule(String scheduleDate, Long driverId) {
        DriverDutyWorkVO driverDutyWorkVO = new DriverDutyWorkVO();
        //查询司机driverId
       // Long driverId = driverInfoMapper.selectDriverIdByUserId(userId);
        //查询司机上班时间
         // List<Date> dutyDate = driverWorkInfoMapper.selectDutyDateByMonth(scheduleDate,driverId);
        //查询司机休假时间
        List<String> holidays = driverWorkInfoMapper.selectHolidaysByMonth(scheduleDate,driverId);
        driverDutyWorkVO.setHolidays(holidays);
      //  driverDutyWorkVO.setDutyDate(dutyDate);
        return driverDutyWorkVO;
    }

    /**
     * 按月获取司机的排班详情
     * @param driverId
     * @param month
     * @return
     */
    @Override
    public List<DriverWorkInfoMonthVo> getDriverWorkInfoMonthList(Long driverId, String month){
        return driverWorkInfoMapper.getDriverWorkInfoMonthList(driverId, month);
    }

    /**
     * 按月更新司机的排班信息
     * @param driverWorkInfoDetailVo
     */

    @Override
    public void updateDriverWorkDetailMonth(DriverWorkInfoDetailVo driverWorkInfoDetailVo,Long userId) {
        if(CollectionUtils.isNotEmpty(driverWorkInfoDetailVo.getDriverWorkInfoMonthVos())){
            driverWorkInfoMapper.updateDriverWorkDetailMonth(driverWorkInfoDetailVo.getDriverWorkInfoMonthVos(),userId,DateUtils.getNowDate());
        }
    }

    /**
     * 按月获取的排班详情-全部司机
     * @param month
     * @return
     */
    @Override
    public List<WorkInfoMonthVo> getWorkInfoMonthList(String month,Long companyId){
        return driverWorkInfoMapper.getWorkInfoMonthList(month,companyId);
    }

    /**
     * 按月更新的排班信息-全部司机
     * @param workInfoDetailVo
     *
     */
    @Override
    public void updateWorkDetailMonth(WorkInfoDetailVo workInfoDetailVo,Long userId) {
        if(CollectionUtils.isNotEmpty(workInfoDetailVo.getWorkInfoMonthVos())){
            workInfoDetailVo.getWorkInfoMonthVos();
            driverWorkInfoMapper.updateWorkDetailMonth(workInfoDetailVo.getWorkInfoMonthVos(),userId,DateUtils.getNowDate());
            driverWorkInfoMapper.updateWorkDetailMonthByDriverInfo(workInfoDetailVo.getWorkInfoMonthVos(),userId,DateUtils.getNowDate());
        }

    }



}
