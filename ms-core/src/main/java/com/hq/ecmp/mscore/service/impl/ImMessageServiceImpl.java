package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.ImMessage;
import com.hq.ecmp.mscore.dto.IMMsgStatusDto;
import com.hq.ecmp.mscore.dto.IMQueryMsgDto;
import com.hq.ecmp.mscore.mapper.ImMessageMapper;
import com.hq.ecmp.mscore.service.ImMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * (ImMessage)表服务实现类
 *
 * @author makejava
 * @since 2020-05-07 10:11:00
 */
@Service("imMessageService")
public class ImMessageServiceImpl implements ImMessageService {
    @Resource
    private ImMessageMapper imMessageDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ImMessage queryById(Long id) {
        return this.imMessageDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ImMessage> queryAllByLimit(int offset, int limit) {
        return this.imMessageDao.queryAllByLimit(offset, limit);
    }

    /**
     * 按条件查询多条数据
     *
     * @return 对象列表
     */
    @Override
    public List<ImMessage> queryMsgInfo(IMQueryMsgDto imMessage) {
        return this.imMessageDao.queryMsgInfo(imMessage);
    }

    /**
     * 新增数据
     *
     * @param imMessage 实例对象
     * @return 实例对象
     */
    @Override
    public ImMessage insert(ImMessage imMessage) {
        this.imMessageDao.insert(imMessage);
        return imMessage;
    }

    /**
     * 修改数据
     *
     * @param imMessage 实例对象
     * @return 实例对象
     */
    @Override
    public ImMessage update(ImMessage imMessage) {
        this.imMessageDao.update(imMessage);
        return this.queryById(imMessage.getId());
    }

    @Override
    public void updateMsgFail(IMMsgStatusDto imMessage) {
        this.imMessageDao.updateMsgFail(imMessage);
    }

    /**
     * @return 实例对象
     */
    @Override
    public int queryMsgConutBy(Map map) {
        return imMessageDao.queryMsgConutBy(map);
    }

    @Override
    public int queryAllMsgConutBy(Map map) {
        return  imMessageDao.queryAllMsgCount(map);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.imMessageDao.deleteById(id) > 0;
    }
}