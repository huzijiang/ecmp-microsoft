package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.JourneyUserCarPower;
import com.hq.ecmp.mscore.domain.ServiceTypeCarAuthority;
import com.hq.ecmp.mscore.domain.UserCarAuthority;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface JourneyUserCarPowerMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param powerId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public JourneyUserCarPower selectJourneyUserCarPowerById(Long powerId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<JourneyUserCarPower> selectJourneyUserCarPowerList(JourneyUserCarPower journeyUserCarPower);

    /**
     * 新增【请填写功能名称】
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 结果
     */
    public int insertJourneyUserCarPower(JourneyUserCarPower journeyUserCarPower);

    /**
     * 修改【请填写功能名称】
     *
     * @param journeyUserCarPower 【请填写功能名称】
     * @return 结果
     */
    public int updateJourneyUserCarPower(JourneyUserCarPower journeyUserCarPower);

    /**
     * 删除【请填写功能名称】
     *
     * @param powerId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyUserCarPowerById(Long powerId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param powerIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteJourneyUserCarPowerByIds(Long[] powerIds);
    
    
    public List<UserCarAuthority> queryNoteAllUserAuthority(Long nodeId);
    
	public List<ServiceTypeCarAuthority> queryUserAuthorityFromService(JourneyUserCarPower JourneyUserCarPower);

	public Integer querySurplusNum(JourneyUserCarPower journeyUserCarPower);
	
	public Integer batchInsert(@Param("list")List<JourneyUserCarPower> list);

}
