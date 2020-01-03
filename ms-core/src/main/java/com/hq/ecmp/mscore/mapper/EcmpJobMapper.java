package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpJob;

import java.util.List;

/**
 * 定时任务调度Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface EcmpJobMapper
{
    /**
     * 查询定时任务调度
     *
     * @param jobId 定时任务调度ID
     * @return 定时任务调度
     */
    public EcmpJob selectEcmpJobById(Long jobId);

    /**
     * 查询定时任务调度列表
     *
     * @param ecmpJob 定时任务调度
     * @return 定时任务调度集合
     */
    public List<EcmpJob> selectEcmpJobList(EcmpJob ecmpJob);

    /**
     * 新增定时任务调度
     *
     * @param ecmpJob 定时任务调度
     * @return 结果
     */
    public int insertEcmpJob(EcmpJob ecmpJob);

    /**
     * 修改定时任务调度
     *
     * @param ecmpJob 定时任务调度
     * @return 结果
     */
    public int updateEcmpJob(EcmpJob ecmpJob);

    /**
     * 删除定时任务调度
     *
     * @param jobId 定时任务调度ID
     * @return 结果
     */
    public int deleteEcmpJobById(Long jobId);

    /**
     * 批量删除定时任务调度
     *
     * @param jobIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpJobByIds(Long[] jobIds);
}
