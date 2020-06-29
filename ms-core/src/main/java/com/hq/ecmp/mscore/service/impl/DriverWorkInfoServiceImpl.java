package com.hq.ecmp.mscore.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.MacTools;
import com.hq.ecmp.mscore.domain.DriverWorkInfo;
import com.hq.ecmp.mscore.mapper.DriverInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverServiceStateInfoMapper;
import com.hq.ecmp.mscore.mapper.DriverWorkInfoMapper;
import com.hq.ecmp.mscore.service.IDriverWorkInfoService;
import com.hq.ecmp.mscore.vo.DriverDutyPlanVO;
import com.hq.ecmp.mscore.vo.DriverDutySummaryVO;
import com.hq.ecmp.mscore.vo.DriverDutyWorkVO;
import com.hq.ecmp.mscore.vo.*;
import com.hq.ecmp.util.OkHttpUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class DriverWorkInfoServiceImpl implements IDriverWorkInfoService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DriverWorkInfoMapper driverWorkInfoMapper;
    @Autowired
    private DriverServiceStateInfoMapper driverServiceStateInfoMapper;
    @Autowired
    private DriverInfoMapper driverInfoMapper;

    @Value("${thirdService.enterpriseId}") // 企业编号
    private String enterpriseId;
    @Value("${thirdService.licenseContent}") // 企业证书信息
    private String licenseContent;
    @Value("${thirdService.apiUrl}") // 三方平台的接口前地址
    private String apiUrl;

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
        //查询司机已出勤天数  已出勤天数为 当月开始到 当天的应该出勤并且出勤的天使
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
        //查询司机休假时间  只查询今天以前的假
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

    /**
     * 从云端获取一年的节假日修改本地数据的cloud_work_date_info表
     */
    @Override
    public void SchedulingTimingTask() {
        try {
            // MAC地址
            List<String> macList = MacTools.getMacList();
            String macAdd = macList.get(0);
            // 调用云端接口
            Map<String, Object> querySchedulingMap = new HashMap<>();
            //企业编号
            querySchedulingMap.put("enterpriseId", enterpriseId);
            //企业证书信息
            querySchedulingMap.put("licenseContent", licenseContent);
            //物理地址
            querySchedulingMap.put("mac", macAdd);
            //year
            querySchedulingMap.put("year","");
            //去云端的路径
            String resultQuery = OkHttpUtil.postForm(apiUrl + "/basic/holidays", querySchedulingMap);
            JSONObject jsonObjectQuery = JSONObject.parseObject(resultQuery);
            JSONObject json = jsonObjectQuery.getJSONObject("data");
            JSONArray jsonArrayHolidays = json.getJSONArray("holidays");
            JSONArray jsonArrayDutyDays = json.getJSONArray("dutyDays");
            jsonArrayHolidays.addAll(jsonArrayDutyDays);

            List<WorkInfoMonthVo> cloudWorkDateInfo = new ArrayList<WorkInfoMonthVo>();
            WorkInfoMonthVo workInfoMonthVo =new WorkInfoMonthVo();
            //查询cloud_work_date_info表中的数据
            List<DriverWorkInfo> list = driverWorkInfoMapper.selectDriverWorkInfoList(new DriverWorkInfo());
            for(int i= 0; i<jsonArrayHolidays.size(); i++){
                //System.out.println(jsonArrayHolidays.get(i));
                for(int j =0; j<list.size(); j++ ){
                    if(jsonArrayHolidays.get(i).equals(list.get(j).getCaledarDate())){
                        workInfoMonthVo.setCalendarDate(list.get(j).getCaledarDate());
                        //这里缺一个枚举类 定义状态
                        workInfoMonthVo.setWorkState("1111");
                        cloudWorkDateInfo.add(workInfoMonthVo);
                    }
                }
            }
            //定时任务没有操作用户所有默认为1
            Long userId = 1L;
        } catch (Exception e) {
            logger.error("业务处理异常", e);
        }
    }

    /**
     * 排班初始化
     * @param driverId
     * @return
     */
/*    public boolean setDriverWorkInfo(Long driverId){

        String date=DateUtils.getNowDate().toString();
        List<CloudWorkIDateVo> workDateVos = driverWorkInfoMapper.getCloudWorkDateList(date);
        DriverWorkInfoVo driverWorkInfoVo=null;
        driverWorkInfoVo.setDriverId(driverId);
        for(CloudWorkIDateVo work : workDateVos){
            driverWorkInfoVo.setCalendarDate(work.getCalendarDate());
            driverWorkInfoVo.setOnDutyRegisteTime(work.getWorkStart());
            driverWorkInfoVo.setOffDutyRegisteTime(work.getWorkEnd());
            driverWorkInfoVo.setTodayItIsOnDuty(work.getItIsWork());
            int i = driverWorkInfoMapper.insertDriverWorkInfo(driverWorkInfoVo);
            if(i > 0){
                return true;
            }
        }
        return false;
    }*/


}
