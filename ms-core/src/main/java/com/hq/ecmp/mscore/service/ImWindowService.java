package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.ImWindow;
import java.util.List;

/**
 * (ImWindow)表服务接口
 *
 * @author makejava
 * @since 2020-05-10 09:40:40
 */
public interface ImWindowService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ImWindow queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ImWindow> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param imWindow 实例对象
     * @return 实例对象
     */
    ImWindow insert(ImWindow imWindow);

    /**
     * 修改数据
     *
     * @param imWindow 实例对象
     * @return 实例对象
     */
    ImWindow update(ImWindow imWindow);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    List<ImWindow> queryWindowsBySend(ImWindow imwindow);
}