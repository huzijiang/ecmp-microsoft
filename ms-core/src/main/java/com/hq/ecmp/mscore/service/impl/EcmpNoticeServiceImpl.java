package com.hq.ecmp.mscore.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpNotice;
import com.hq.ecmp.mscore.mapper.EcmpNoticeMapper;
import com.hq.ecmp.mscore.mapper.EcmpNoticeMappingMapper;
import com.hq.ecmp.mscore.service.IEcmpNoticeService;
import com.hq.ecmp.mscore.vo.PageResult;
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

    @Autowired
    private EcmpNoticeMappingMapper ecmpNoticeMappingMapper;

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
    public EcmpNotice selectExpirationDateNewNotice(Map map) {
        return ecmpNoticeMapper.selectNewEcmpNotice(map);

    }

    /**
     * 分页查询总车队列表
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    @Override
    public PageResult<EcmpNotice> selectNoticeSearchList(Integer pageNum, Integer pageSize) {
            PageHelper.startPage(pageNum,pageSize);
            List<EcmpNotice> list =  ecmpNoticeMapper.selectNoticeSearchList();
            for(EcmpNotice ecmpNotice: list){
                List<Long> bucIds = ecmpNoticeMappingMapper.selectNoticeId(ecmpNotice.getNoticeId());
                ecmpNotice.setBucIds(bucIds);
            }
            PageInfo<EcmpNotice> info = new PageInfo<>(list);
            return new PageResult<>(info.getTotal(),info.getPages(),list);
    }

    /**
     * 查询公告列表详情（后台管理系统）
     * @param //carGroupId
     * @return
     */
    @Override
    public EcmpNotice getNoticeDetails(Integer noticeId) {
        EcmpNotice EcmpNotice = ecmpNoticeMapper.getNoticeDetails(noticeId);
        return EcmpNotice;
    }

    /**
     * 定时任务：修改公告管理公告状态
     */
    @Override
    public void announcementTask() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查询公告管理状态为 待发布与发布中的数据
        List<EcmpNotice> list =  ecmpNoticeMapper.selectNoticeByStatus();
        for (EcmpNotice ecmpNotice :list){
                Date publishTime = format.parse(ecmpNotice.getPublishTime());
                Date endTime = format.parse(ecmpNotice.getEndTime());
                //当前时间
                Date time = DateUtils.getNowDate();
                //当前时间小于开始时间并且结束时间大于当前时间 就是待发布
                if(time.before(publishTime) && endTime.after(time)){
                    ecmpNotice.setStatus("0");
                    //当前时间大于开始时间并且当前时间小于结束时间就是发布中
                }else if(time.after(publishTime) && time.before(endTime)){
                    ecmpNotice.setStatus("1");
                    //开始时间大于当前时间并且结束时间大于当前时间就是已结束
                }else  if(time.after(publishTime) && time.after(endTime)){
                    ecmpNotice.setStatus("2");
                }
                //根据id修改公告的状态
                 ecmpNoticeMapper.updateEcmpNotice(ecmpNotice);
            }
        }
}
