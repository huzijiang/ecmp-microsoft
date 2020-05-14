package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.ImWindow;
import com.hq.ecmp.mscore.mapper.ImWindowMapper;
import com.hq.ecmp.mscore.service.ImWindowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ImWindow)表服务实现类
 *
 * @author makejava
 * @since 2020-05-10 09:40:40
 */
@Service("imWindowService")
public class ImWindowServiceImpl implements ImWindowService {
    @Resource
    private ImWindowMapper imWindowDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */

    @Override
    public ImWindow queryById(Long id) {
        return this.imWindowDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ImWindow> queryAllByLimit(int offset, int limit) {
        return this.imWindowDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param imWindow 实例对象
     * @return 实例对象
     */
    @Override
    public ImWindow insert(ImWindow imWindow) {
        this.imWindowDao.insert(imWindow);
        return imWindow;
    }

    /**
     * 修改数据
     *
     * @param imWindow 实例对象
     * @return 实例对象
     */
    @Override
    public ImWindow update(ImWindow imWindow) {
        this.imWindowDao.update(imWindow);
        return this.queryById(imWindow.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.imWindowDao.deleteById(id) > 0;
    }

    @Override
    public List<ImWindow> queryWindowsBySend(ImWindow imwindow) {
        return this.imWindowDao.queryWindowsBySend(imwindow);
    }
}