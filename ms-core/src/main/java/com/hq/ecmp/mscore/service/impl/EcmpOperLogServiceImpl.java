package com.hq.ecmp.mscore.service.impl;

import java.util.List;

import com.hq.ecmp.mscore.domain.EcmpOperLog;
import com.hq.ecmp.mscore.mapper.EcmpOperLogMapper;
import com.hq.ecmp.mscore.service.IEcmpOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 操作日志记录Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpOperLogServiceImpl implements IEcmpOperLogService
{
    @Autowired
    private EcmpOperLogMapper ecmpOperLogMapper;

    /**
     * 查询操作日志记录
     *
     * @param operId 操作日志记录ID
     * @return 操作日志记录
     */
    @Override
    public EcmpOperLog selectEcmpOperLogById(Long operId)
    {
        return ecmpOperLogMapper.selectEcmpOperLogById(operId);
    }

    /**
     * 查询操作日志记录列表
     *
     * @param ecmpOperLog 操作日志记录
     * @return 操作日志记录
     */
    @Override
    public List<EcmpOperLog> selectEcmpOperLogList(EcmpOperLog ecmpOperLog)
    {
        return ecmpOperLogMapper.selectEcmpOperLogList(ecmpOperLog);
    }

    /**
     * 新增操作日志记录
     *
     * @param ecmpOperLog 操作日志记录
     * @return 结果
     */
    @Override
    public int insertEcmpOperLog(EcmpOperLog ecmpOperLog)
    {
        return ecmpOperLogMapper.insertEcmpOperLog(ecmpOperLog);
    }

    /**
     * 修改操作日志记录
     *
     * @param ecmpOperLog 操作日志记录
     * @return 结果
     */
    @Override
    public int updateEcmpOperLog(EcmpOperLog ecmpOperLog)
    {
        return ecmpOperLogMapper.updateEcmpOperLog(ecmpOperLog);
    }

    /**
     * 批量删除操作日志记录
     *
     * @param operIds 需要删除的操作日志记录ID
     * @return 结果
     */
    @Override
    public int deleteEcmpOperLogByIds(Long[] operIds)
    {
        return ecmpOperLogMapper.deleteEcmpOperLogByIds(operIds);
    }

    /**
     * 删除操作日志记录信息
     *
     * @param operId 操作日志记录ID
     * @return 结果
     */
    @Override
    public int deleteEcmpOperLogById(Long operId)
    {
        return ecmpOperLogMapper.deleteEcmpOperLogById(operId);
    }
}
