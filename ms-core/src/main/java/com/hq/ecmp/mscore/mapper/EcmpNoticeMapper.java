package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpNotice;

import java.util.List;

/**
 * 通知公告Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface EcmpNoticeMapper
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
     * 删除通知公告
     *
     * @param noticeId 通知公告ID
     * @return 结果
     */
    public int deleteEcmpNoticeById(Integer noticeId);

    /**
     * 批量删除通知公告
     *
     * @param noticeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpNoticeByIds(Integer[] noticeIds);
}
