package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.vo.CarGroupListVO;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface CarGroupInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param carGroupId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarGroupInfo selectCarGroupInfoById(Long carGroupId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarGroupInfo> selectCarGroupInfoList(CarGroupInfo carGroupInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarGroupInfo(CarGroupInfo carGroupInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarGroupInfo(CarGroupInfo carGroupInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param carGroupId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupInfoById(Long carGroupId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carGroupIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCarGroupInfoByIds(Long[] carGroupIds);

    List<CarGroupListVO> selectAllByPage(String search);


    /**
     * 查询该组织下的车队信息
     *
     * @param deptId 组织ID
     * @return 结果
     */
    public int selectCountByOrgdeptId(Long deptId);

    /**
     * 查询下级车队列表
     * @param deptId
     * @return
     */
    List<CarGroupListVO> selectSubCarGroupInfoList(Long deptId);
}
