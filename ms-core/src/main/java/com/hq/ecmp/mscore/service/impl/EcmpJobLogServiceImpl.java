package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpJobLog;
import com.hq.ecmp.mscore.mapper.EcmpJobLogMapper;
import com.hq.ecmp.mscore.service.IEcmpJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 定时任务调度日志Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpJobLogServiceImpl implements IEcmpJobLogService
{
    @Autowired
    private EcmpJobLogMapper ecmpJobLogMapper;

    /**
     * 查询定时任务调度日志
     *
     * @param jobLogId 定时任务调度日志ID
     * @return 定时任务调度日志
     */
    @Override
    public EcmpJobLog selectEcmpJobLogById(Long jobLogId)
    {
        return ecmpJobLogMapper.selectEcmpJobLogById(jobLogId);
    }

    /**
     * 查询定时任务调度日志列表
     *
     * @param ecmpJobLog 定时任务调度日志
     * @return 定时任务调度日志
     */
    @Override
    public List<EcmpJobLog> selectEcmpJobLogList(EcmpJobLog ecmpJobLog)
    {
        return ecmpJobLogMapper.selectEcmpJobLogList(ecmpJobLog);
    }

    /**
     * 新增定时任务调度日志
     *
     * @param ecmpJobLog 定时任务调度日志
     * @return 结果
     */
    @Override
    public int insertEcmpJobLog(EcmpJobLog ecmpJobLog)
    {
        ecmpJobLog.setCreateTime(DateUtils.getNowDate());
        return ecmpJobLogMapper.insertEcmpJobLog(ecmpJobLog);
    }

    /**
     * 修改定时任务调度日志
     *
     * @param ecmpJobLog 定时任务调度日志
     * @return 结果
     */
    @Override
    public int updateEcmpJobLog(EcmpJobLog ecmpJobLog)
    {
        return ecmpJobLogMapper.updateEcmpJobLog(ecmpJobLog);
    }

    /**
     * 批量删除定时任务调度日志
     *
     * @param jobLogIds 需要删除的定时任务调度日志ID
     * @return 结果
     */
    @Override
    public int deleteEcmpJobLogByIds(Long[] jobLogIds)
    {
        return ecmpJobLogMapper.deleteEcmpJobLogByIds(jobLogIds);
    }

    /**
     * 删除定时任务调度日志信息
     *
     * @param jobLogId 定时任务调度日志ID
     * @return 结果
     */
    @Override
    public int deleteEcmpJobLogById(Long jobLogId)
    {
        return ecmpJobLogMapper.deleteEcmpJobLogById(jobLogId);
    }
}
