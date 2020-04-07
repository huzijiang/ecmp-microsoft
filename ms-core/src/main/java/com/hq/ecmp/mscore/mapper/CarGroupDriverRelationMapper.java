package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CarGroupDriverRelation;
import com.hq.ecmp.mscore.vo.DriverVO;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface CarGroupDriverRelationMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarGroupDriverRelation selectCarGroupDriverRelationById(Long driverId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carGroupDriverRelation 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarGroupDriverRelation> selectCarGroupDriverRelationList(CarGroupDriverRelation carGroupDriverRelation);

    /**
     * 新增【请填写功能名称】
     *
     * @param carGroupDriverRelation 【请填写功能名称】
     * @return 结果
     */
    public int insertCarGroupDriverRelation(CarGroupDriverRelation carGroupDriverRelation);

    /**
     * 修改【请填写功能名称】
     *
     * @param carGroupDriverRelation 【请填写功能名称】
     * @return 结果
     */
    public int updateCarGroupDriverRelation(CarGroupDriverRelation carGroupDriverRelation);

    /**
     * 删除【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupDriverRelationById(Long driverId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param driverIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCarGroupDriverRelationByIds(Long[] driverIds);

    /**
     * 查询车队人数
     * @return
     */
    int selectCountDriver(Long carGroupId);


    List<DriverVO> selectGroupEffectiveDrives(Long carGroupId);
}
