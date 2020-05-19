package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpNotice;
import com.hq.ecmp.mscore.vo.PageResult;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

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

    List<EcmpNotice> selectEcmpNoticeListByOtherId(Map map);

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
    EcmpNotice selectExpirationDateNewNotice(Map map);

    /**
     * 分页全部查询公告列表（带搜索功能）
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    PageResult<EcmpNotice> selectNoticeSearchList(Integer pageNum, Integer pageSize,Long companyId);

    /**
     * 查询公告列表详情（后台管理系统）
     * @param //carGroupId
     * @return
     */
    EcmpNotice getNoticeDetails(Integer noticeId);

    /**
     * 定时任务修改公告状态
     */
    void announcementTask() throws ParseException;

    /**
     *
     */
    void addObtainScheduling();

    /**
     * 首页公告展示列表(最新5条并且发布中)
     * @param pageNum
     * @param pageSize
     * @param companyId
     * @return
     */
    PageResult<EcmpNotice> getNoticeFiveList(Integer pageNum, Integer pageSize, Long companyId);
}
