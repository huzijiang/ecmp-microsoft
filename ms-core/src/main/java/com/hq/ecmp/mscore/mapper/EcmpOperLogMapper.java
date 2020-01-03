package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpOperLog;

import java.util.List;

/**
 * 操作日志记录Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface EcmpOperLogMapper
{
    /**
     * 查询操作日志记录
     *
     * @param operId 操作日志记录ID
     * @return 操作日志记录
     */
    public EcmpOperLog selectEcmpOperLogById(Long operId);

    /**
     * 查询操作日志记录列表
     *
     * @param ecmpOperLog 操作日志记录
     * @return 操作日志记录集合
     */
    public List<EcmpOperLog> selectEcmpOperLogList(EcmpOperLog ecmpOperLog);

    /**
     * 新增操作日志记录
     *
     * @param ecmpOperLog 操作日志记录
     * @return 结果
     */
    public int insertEcmpOperLog(EcmpOperLog ecmpOperLog);

    /**
     * 修改操作日志记录
     *
     * @param ecmpOperLog 操作日志记录
     * @return 结果
     */
    public int updateEcmpOperLog(EcmpOperLog ecmpOperLog);

    /**
     * 删除操作日志记录
     *
     * @param operId 操作日志记录ID
     * @return 结果
     */
    public int deleteEcmpOperLogById(Long operId);

    /**
     * 批量删除操作日志记录
     *
     * @param operIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpOperLogByIds(Long[] operIds);
}
