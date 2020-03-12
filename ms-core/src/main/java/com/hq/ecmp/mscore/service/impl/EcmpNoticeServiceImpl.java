package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import java.util.Map;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpNotice;
import com.hq.ecmp.mscore.mapper.EcmpNoticeMapper;
import com.hq.ecmp.mscore.service.IEcmpNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 通知公告Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpNoticeServiceImpl implements IEcmpNoticeService
{
    @Autowired
    private EcmpNoticeMapper ecmpNoticeMapper;

    /**
     * 查询通知公告
     *
     * @param noticeId 通知公告ID
     * @return 通知公告
     */
    @Override
    public EcmpNotice selectEcmpNoticeById(Integer noticeId)
    {
        return ecmpNoticeMapper.selectEcmpNoticeById(noticeId);
    }

    /**
     * 查询通知公告列表
     *
     * @param ecmpNotice 通知公告
     * @return 通知公告
     */
    @Override
    public List<EcmpNotice> selectEcmpNoticeList(EcmpNotice ecmpNotice)
    {
        return ecmpNoticeMapper.selectEcmpNoticeList(ecmpNotice);
    }

    /**
     * 根据条件查询通知公告列表
     *
     * @param ecmpNotice 通知公告
     * @return 通知公告
     */
    @Override
    public List<EcmpNotice> selectEcmpNoticeListByOtherId(Map map)
    {
        return ecmpNoticeMapper.selectEcmpNoticeListByOtherId(map);
    }

    /**
     * 新增通知公告
     *
     * @param ecmpNotice 通知公告
     * @return 结果
     */
    @Override
    public int insertEcmpNotice(EcmpNotice ecmpNotice)
    {
        ecmpNotice.setCreateTime(DateUtils.getNowDate());
        return ecmpNoticeMapper.insertEcmpNotice(ecmpNotice);
    }

    /**
     * 修改通知公告
     *
     * @param ecmpNotice 通知公告
     * @return 结果
     */
    @Override
    public int updateEcmpNotice(EcmpNotice ecmpNotice)
    {
        ecmpNotice.setUpdateTime(DateUtils.getNowDate());
        return ecmpNoticeMapper.updateEcmpNotice(ecmpNotice);
    }

    /**
     * 批量删除通知公告
     *
     * @param noticeIds 需要删除的通知公告ID
     * @return 结果
     */
    @Override
    public int deleteEcmpNoticeByIds(Integer[] noticeIds)
    {
        return ecmpNoticeMapper.deleteEcmpNoticeByIds(noticeIds);
    }

    /**
     * 删除通知公告信息
     *
     * @param noticeId 通知公告ID
     * @return 结果
     */
    @Override
    public int deleteEcmpNoticeById(Integer noticeId)
    {
        return ecmpNoticeMapper.deleteEcmpNoticeById(noticeId);
    }

    @Override
    public List<EcmpNotice> selectAll() {
        return null;
    }

    @Override
    public EcmpNotice selectNoticeDetailByUserId(Long userId) {
        return null;
    }

    @Override
    public EcmpNotice selectExpirationDateNewNotice(Long userId) {
        return ecmpNoticeMapper.selectNewEcmpNotice();

    }

}
