package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpJobLog;

import java.util.List;

/**
 * 定时任务调度日志Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpJobLogService
{
    /**
     * 查询定时任务调度日志
     *
     * @param jobLogId 定时任务调度日志ID
     * @return 定时任务调度日志
     */
    public EcmpJobLog selectEcmpJobLogById(Long jobLogId);

    /**
     * 查询定时任务调度日志列表
     *
     * @param ecmpJobLog 定时任务调度日志
     * @return 定时任务调度日志集合
     */
    public List<EcmpJobLog> selectEcmpJobLogList(EcmpJobLog ecmpJobLog);

    /**
     * 新增定时任务调度日志
     *
     * @param ecmpJobLog 定时任务调度日志
     * @return 结果
     */
    public int insertEcmpJobLog(EcmpJobLog ecmpJobLog);

    /**
     * 修改定时任务调度日志
     *
     * @param ecmpJobLog 定时任务调度日志
     * @return 结果
     */
    public int updateEcmpJobLog(EcmpJobLog ecmpJobLog);

    /**
     * 批量删除定时任务调度日志
     *
     * @param jobLogIds 需要删除的定时任务调度日志ID
     * @return 结果
     */
    public int deleteEcmpJobLogByIds(Long[] jobLogIds);

    /**
     * 删除定时任务调度日志信息
     *
     * @param jobLogId 定时任务调度日志ID
     * @return 结果
     */
    public int deleteEcmpJobLogById(Long jobLogId);
}
