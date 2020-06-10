package com.hq.ecmp.mscore.mapper;


import com.hq.ecmp.mscore.domain.CarOptLogInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-06-09
 */
public interface CarOptLogInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param logId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarOptLogInfo selectCarOptLogInfoById(Long logId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param carOptLogInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarOptLogInfo> selectCarOptLogInfoList(CarOptLogInfo carOptLogInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param carOptLogInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarOptLogInfo(CarOptLogInfo carOptLogInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param carOptLogInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarOptLogInfo(CarOptLogInfo carOptLogInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param logId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarOptLogInfoById(Long logId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCarOptLogInfoByIds(Long[] logIds);

    CarOptLogInfo selectCarOptLogInfoByCarIdAndLogType(@Param("carId") Long carId,@Param("optTpe") String logType);
}