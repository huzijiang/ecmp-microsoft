package com.hq.ecmp.mscore.service;

import java.util.List;
import java.util.Map;

import com.hq.ecmp.mscore.domain.CarAuthorityInfo;
import com.hq.ecmp.mscore.domain.JourneyUserCarPower;
import com.hq.ecmp.mscore.domain.ServiceTypeCarAuthority;
import com.hq.ecmp.mscore.domain.UserCarAuthority;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IJourneyUserCarPowerService
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
     * 批量删除【请填写功能名称】
     *
     * @param powerIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyUserCarPowerByIds(Long[] powerIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param powerId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteJourneyUserCarPowerById(Long powerId);
    
    /**
     * 查询指定行程的接机  送机   市内用车对应的剩余次数
     * @param journeyId
     * @return
     */
    public Map<String,Integer> selectStatusCount(Long journeyId);
    
    
    /**
     * 获取行程节点下的用户用车权限
     * @param nodeId
     * 		  cityCode 用车城市
     * @return
     */
    public List<UserCarAuthority>  queryNoteAllUserAuthority(Long nodeId,String cityCode);
    
    public List<CarAuthorityInfo> queryJourneyAllUserAuthority(Long journeyId);
    
    /**
     * 查询行程下指定服务类型的用车权限
     * @param type  C001 -接机    C009  -送机     C222  -市内用车
     * @param journeyId
     * @return
     */
    public List<ServiceTypeCarAuthority> queryUserAuthorityFromService(String type,Long journeyId);
    
    /**
     * 对用车申请单生成用车权限
     * @param applyId  申请单
     * @param auditUserId   申请单审核通过人
     * @return
     */
    public boolean createUseCarAuthority(Long applyId,Long auditUserId);
    
    
    /**
     * 构建用车权限对应的前端状态
     * @param flag 权限对应的用车制度中  true-只有网约车    false-包含自有车
     *        powerId   权限ID
     * @return
     */
    public String buildUserAuthorityPowerStatus(boolean flag,Long powerId);
    
    /**
     * 查询公务权限的用车城市
     * @param powerId
     * @return
     */
    public String queryOfficialPowerUseCity(Long powerId);
    
    
    /**
     * 查询公务用车下单需要的权限信息
     * @return
     */
    public List<CarAuthorityInfo> queryOfficialOrderNeedPower(Long journeyId);
    
    /**
     * 更新权限剩余可用次数
     * @param powerId   权限ID
     * @param optType   1-申请用车    2-取消订单
     * @return
     */
    public boolean updatePowerSurplus(Long powerId,Integer optType);
    
    /**
     * 校验用车权限是否过期
     * @param powerId
     * @return true-已过期   false-未过期
     */
    public boolean checkPowerOverTime(Long powerId);
    
    
    
}
