package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.ImVirtuaPhone;
import java.util.List;

/**
 * (ImVirtuaPhone)表服务接口
 *
 * @author makejava
 * @since 2020-05-12 20:27:20
 */
public interface ImVirtuaPhoneService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ImVirtuaPhone queryById(Integer id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ImVirtuaPhone> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param imVirtuaPhone 实例对象
     * @return 实例对象
     */
    ImVirtuaPhone insert(ImVirtuaPhone imVirtuaPhone);

    /**
     * 修改数据
     *
     * @param imVirtuaPhone 实例对象
     * @return 实例对象
     */
    ImVirtuaPhone update(ImVirtuaPhone imVirtuaPhone);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    ImVirtuaPhone queryByPhones(ImVirtuaPhone imVirtuaPhone);
}