package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpJob;
import com.hq.ecmp.mscore.mapper.EcmpJobMapper;
import com.hq.ecmp.mscore.service.IEcmpJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 定时任务调度Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpJobServiceImpl implements IEcmpJobService
{
    @Autowired
    private EcmpJobMapper ecmpJobMapper;

    /**
     * 查询定时任务调度
     *
     * @param jobId 定时任务调度ID
     * @return 定时任务调度
     */
    @Override
    public EcmpJob selectEcmpJobById(Long jobId)
    {
        return ecmpJobMapper.selectEcmpJobById(jobId);
    }

    /**
     * 查询定时任务调度列表
     *
     * @param ecmpJob 定时任务调度
     * @return 定时任务调度
     */
    @Override
    public List<EcmpJob> selectEcmpJobList(EcmpJob ecmpJob)
    {
        return ecmpJobMapper.selectEcmpJobList(ecmpJob);
    }

    /**
     * 新增定时任务调度
     *
     * @param ecmpJob 定时任务调度
     * @return 结果
     */
    @Override
    public int insertEcmpJob(EcmpJob ecmpJob)
    {
        ecmpJob.setCreateTime(DateUtils.getNowDate());
        return ecmpJobMapper.insertEcmpJob(ecmpJob);
    }

    /**
     * 修改定时任务调度
     *
     * @param ecmpJob 定时任务调度
     * @return 结果
     */
    @Override
    public int updateEcmpJob(EcmpJob ecmpJob)
    {
        ecmpJob.setUpdateTime(DateUtils.getNowDate());
        return ecmpJobMapper.updateEcmpJob(ecmpJob);
    }

    /**
     * 批量删除定时任务调度
     *
     * @param jobIds 需要删除的定时任务调度ID
     * @return 结果
     */
    @Override
    public int deleteEcmpJobByIds(Long[] jobIds)
    {
        return ecmpJobMapper.deleteEcmpJobByIds(jobIds);
    }

    /**
     * 删除定时任务调度信息
     *
     * @param jobId 定时任务调度ID
     * @return 结果
     */
    @Override
    public int deleteEcmpJobById(Long jobId)
    {
        return ecmpJobMapper.deleteEcmpJobById(jobId);
    }
}
