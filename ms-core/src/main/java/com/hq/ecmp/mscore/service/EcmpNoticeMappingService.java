package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EcmpNoticeMapping;
import java.util.List;

/**
 * (EcmpNoticeMapping)表服务接口
 *
 * @author makejava
 * @since 2020-03-11 18:42:33
 */
public interface EcmpNoticeMappingService {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    EcmpNoticeMapping queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EcmpNoticeMapping> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param ecmpNoticeMapping 实例对象
     * @return 实例对象
     */
    EcmpNoticeMapping insert(EcmpNoticeMapping ecmpNoticeMapping);

    /**
     * 修改数据
     *
     * @param ecmpNoticeMapping 实例对象
     * @return 实例对象
     */
    EcmpNoticeMapping update(EcmpNoticeMapping ecmpNoticeMapping);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 通过公告id修改
     * @param mapping
     */
    void updateEcmpNoticeMapping(EcmpNoticeMapping mapping);

    /**
     * 通过公告id删除
     * @param noticeId
     */
    void deleteByNoticeId(Integer noticeId);
}