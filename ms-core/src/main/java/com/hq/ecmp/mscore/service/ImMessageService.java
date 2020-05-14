package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.ImMessage;
import com.hq.ecmp.mscore.dto.IMMsgStatusDto;
import com.hq.ecmp.mscore.dto.IMQueryMsgDto;

import java.util.List;
import java.util.Map;

/**
 * (ImMessage)表服务接口
 *
 * @author makejava
 * @since 2020-05-07 10:11:00
 */
public interface ImMessageService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ImMessage queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ImMessage> queryAllByLimit(int offset, int limit);

    /**
     * 按条件查询多条数据
     *
     * @return 对象列表
     */
    List<ImMessage> queryMsgInfo(IMQueryMsgDto imMessage);

    /**
     * 新增数据
     *
     * @param imMessage 实例对象
     * @return 实例对象
     */
    ImMessage insert(ImMessage imMessage);

    /**
     * 修改数据
     *
     * @param imMessage 实例对象
     * @return 实例对象
     */
    ImMessage update(ImMessage imMessage);
    /**
     * 修改所有消息状态
     *
     * @param imMessage 实例对象
     * @return 实例对象
     */
    void updateMsgFail(IMMsgStatusDto imMessage);


    /**
     *
     *
     * @return 实例对象
     */
    int queryMsgConutBy(Map map);
    int queryAllMsgConutBy(Map map);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}