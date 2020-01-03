package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpNotice;

import java.util.List;

/**
 * 通知公告Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpNoticeService
{
    /**
     * 查询通知公告
     *
     * @param noticeId 通知公告ID
     * @return 通知公告
     */
    public EcmpNotice selectEcmpNoticeById(Integer noticeId);

    /**
     * 查询通知公告列表
     *
     * @param ecmpNotice 通知公告
     * @return 通知公告集合
     */
    public List<EcmpNotice> selectEcmpNoticeList(EcmpNotice ecmpNotice);

    /**
     * 新增通知公告
     *
     * @param ecmpNotice 通知公告
     * @return 结果
     */
    public int insertEcmpNotice(EcmpNotice ecmpNotice);

    /**
     * 修改通知公告
     *
     * @param ecmpNotice 通知公告
     * @return 结果
     */
    public int updateEcmpNotice(EcmpNotice ecmpNotice);

    /**
     * 批量删除通知公告
     *
     * @param noticeIds 需要删除的通知公告ID
     * @return 结果
     */
    public int deleteEcmpNoticeByIds(Integer[] noticeIds);

    /**
     * 删除通知公告信息
     *
     * @param noticeId 通知公告ID
     * @return 结果
     */
    public int deleteEcmpNoticeById(Integer noticeId);
}
