package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.ImVirtuaPhone;
import com.hq.ecmp.mscore.mapper.ImVirtuaPhoneMapper;
import com.hq.ecmp.mscore.service.ImVirtuaPhoneService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (ImVirtuaPhone)表服务实现类
 *
 * @author makejava
 * @since 2020-05-12 20:27:20
 */
@Service("imVirtuaPhoneService")
public class ImVirtuaPhoneServiceImpl implements ImVirtuaPhoneService {
    @Resource
    private ImVirtuaPhoneMapper imVirtuaPhoneDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public ImVirtuaPhone queryById(Integer id) {
        return this.imVirtuaPhoneDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<ImVirtuaPhone> queryAllByLimit(int offset, int limit) {
        return this.imVirtuaPhoneDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param imVirtuaPhone 实例对象
     * @return 实例对象
     */
    @Override
    public ImVirtuaPhone insert(ImVirtuaPhone imVirtuaPhone) {
        this.imVirtuaPhoneDao.insert(imVirtuaPhone);
        return imVirtuaPhone;
    }

    /**
     * 修改数据
     *
     * @param imVirtuaPhone 实例对象
     * @return 实例对象
     */
    @Override
    public ImVirtuaPhone update(ImVirtuaPhone imVirtuaPhone) {
        this.imVirtuaPhoneDao.update(imVirtuaPhone);
        return this.queryById(imVirtuaPhone.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.imVirtuaPhoneDao.deleteById(id) > 0;
    }

    @Override
    public ImVirtuaPhone queryByPhones(ImVirtuaPhone imVirtuaPhone) {
        return this.imVirtuaPhoneDao.queryByPhones(imVirtuaPhone);
    }
}