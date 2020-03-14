package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.dto.CarGroupDTO;
import com.hq.ecmp.mscore.vo.CarGroupDetailVO;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ICarGroupInfoService
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
     * 批量删除【请填写功能名称】
     *
     * @param carGroupIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupInfoByIds(Long[] carGroupIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carGroupId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupInfoById(Long carGroupId);

    /**
     * 新增车队和调度员信息
     * @param carGroupDTO
     */
    void saveCarGroupAndDispatcher(CarGroupDTO carGroupDTO,Long userId);

    /**
     * 车队详情
     * @param carGroupId
     * @return
     */
    CarGroupDetailVO getCarGroupDetail(Long carGroupId);

    /**
     * 修改车队
     * @param carGroupDTO
     */
    void updateCarGroup(CarGroupDTO carGroupDTO,Long userId);

    /**
     * 禁用车队
     * @param carGroupId
     * @param userId
     */
    void disableCarGroup(Long carGroupId, Long userId);
}
