package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ImWindow;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (ImWindow)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-10 09:40:40
 */
public interface ImWindowMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ImWindow queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ImWindow> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param imWindow 实例对象
     * @return 对象列表
     */
    List<ImWindow> queryAll(ImWindow imWindow);

    /**
     * 新增数据
     *
     * @param imWindow 实例对象
     * @return 影响行数
     */
    int insert(ImWindow imWindow);

    /**
     * 修改数据
     *
     * @param imWindow 实例对象
     * @return 影响行数
     */
    int update(ImWindow imWindow);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);


    List<ImWindow> queryWindowsBySend(ImWindow imwindow);
}