package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ImVirtuaPhone;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (ImVirtuaPhone)表数据库访问层
 *
 * @author makejava
 * @since 2020-05-12 20:27:20
 */
public interface ImVirtuaPhoneMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ImVirtuaPhone queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ImVirtuaPhone> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param imVirtuaPhone 实例对象
     * @return 对象列表
     */
    List<ImVirtuaPhone> queryAll(ImVirtuaPhone imVirtuaPhone);

    /**
     * 新增数据
     *
     * @param imVirtuaPhone 实例对象
     * @return 影响行数
     */
    int insert(ImVirtuaPhone imVirtuaPhone);

    /**
     * 修改数据
     *
     * @param imVirtuaPhone 实例对象
     * @return 影响行数
     */
    int update(ImVirtuaPhone imVirtuaPhone);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    ImVirtuaPhone queryByPhones(ImVirtuaPhone imVirtuaPhone);
}