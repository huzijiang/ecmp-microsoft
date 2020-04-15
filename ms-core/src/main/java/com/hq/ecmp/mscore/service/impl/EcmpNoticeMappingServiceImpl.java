package com.hq.ecmp.mscore.service.impl;


import com.hq.ecmp.mscore.domain.EcmpNoticeMapping;
import com.hq.ecmp.mscore.mapper.EcmpNoticeMappingMapper;
import com.hq.ecmp.mscore.service.EcmpNoticeMappingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (EcmpNoticeMapping)表服务实现类
 *
 * @author makejava
 * @since 2020-03-11 18:42:33
 */
@Service("ecmpNoticeMappingService")
public class EcmpNoticeMappingServiceImpl implements EcmpNoticeMappingService {
    @Resource
    private EcmpNoticeMappingMapper ecmpNoticeMappingDao;

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    @Override
    public EcmpNoticeMapping queryById( Long id) {
        return this.ecmpNoticeMappingDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<EcmpNoticeMapping> queryAllByLimit(int offset, int limit) {
        return this.ecmpNoticeMappingDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param ecmpNoticeMapping 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpNoticeMapping insert(EcmpNoticeMapping ecmpNoticeMapping) {
        this.ecmpNoticeMappingDao.insert(ecmpNoticeMapping);
        return ecmpNoticeMapping;
    }

    /**
     * 修改数据
     *
     * @param ecmpNoticeMapping 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpNoticeMapping update(EcmpNoticeMapping ecmpNoticeMapping) {
        this.ecmpNoticeMappingDao.update(ecmpNoticeMapping);
        return this.queryById(ecmpNoticeMapping.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.ecmpNoticeMappingDao.deleteById(id) > 0;
    }

    /**
     * 通过公告id修改
     * @param mapping
     */
    @Override
    public void updateEcmpNoticeMapping(EcmpNoticeMapping mapping) {
        ecmpNoticeMappingDao.updateEcmpNoticeMapping(mapping);
    }
    /**
     * 通过公告id删除
     * @param noticeId
     */
    @Override
    public void deleteByNoticeId(Integer noticeId) {
        ecmpNoticeMappingDao.deleteByNoticeId(noticeId);
    }
}