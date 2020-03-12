package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpNoticeMapping;

import java.util.List;

/**
 * (EcmpNoticeMapping)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-11 18:42:33
 */
public interface EcmpNoticeMappingMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    EcmpNoticeMapping queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EcmpNoticeMapping> queryAllByLimit( int offset,int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param ecmpNoticeMapping 实例对象
     * @return 对象列表
     */
    List<EcmpNoticeMapping> queryAll(EcmpNoticeMapping ecmpNoticeMapping);

    /**
     * 新增数据
     *
     * @param ecmpNoticeMapping 实例对象
     * @return 影响行数
     */
    int insert(EcmpNoticeMapping ecmpNoticeMapping);

    /**
     * 修改数据
     *
     * @param ecmpNoticeMapping 实例对象
     * @return 影响行数
     */
    int update(EcmpNoticeMapping ecmpNoticeMapping);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 影响行数
     */
    int deleteById( );

}