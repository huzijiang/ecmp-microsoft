package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpNotice;
import com.hq.ecmp.mscore.dto.config.Scheduling;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
     * 根据条件查询通知公告列表
     *
     * @param ecmpNotice 通知公告
     * @return 通知公告集合
     */
    public List<EcmpNotice> selectEcmpNoticeListByOtherId(Map map);
    /**
     * 获取最新公告
     *
     * @param userId 通知公告
     * @return 通知公告集合
     */
    public EcmpNotice selectNewEcmpNotice(Map map);

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
    public int deleteEcmpNoticeById(@Param("noticeId") Integer noticeId);

    /**
     * 批量删除通知公告
     *
     * @param noticeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpNoticeByIds(Integer[] noticeIds);

    /**
     * 分页查询总车队列表
     * @param search
     * @return
     */
    List<EcmpNotice> selectNoticeSearchList(@Param("companyId") Long companyId);

    /**
     * 查询公告列表详情（后台管理系统）
     * @param //carGroupId
     * @return
     */
    EcmpNotice getNoticeDetails(@Param("noticeId") Integer noticeId);

    /**
     * 定时任务：修改公告管理公告状态
     */
    List<EcmpNotice> selectNoticeByStatus();

    void addObtainScheduling(Scheduling scheduling);

    /**
     * 首页公告展示列表(最新5条并且发布中)
     * @param companyId
     * @return
     */
    List<EcmpNotice> getNoticeFiveList(Long companyId);
}
