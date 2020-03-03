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

    /**
     * 查询所有的公公告信息
     * @return
     */
    List<EcmpNotice> selectAll();

    /**
     * 根据用户Id查询公告详细信息
     * @param userId
     * @return
     */
    EcmpNotice selectNoticeDetailByUserId(Long userId);

    /**
     * 根据用户Id获取有效期内的最新公告
     * @param userId
     * @return
     */
    EcmpNotice selectExpirationDateNewNotice(Long userId);
}
