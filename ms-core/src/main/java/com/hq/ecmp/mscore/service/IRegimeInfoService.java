package com.hq.ecmp.mscore.service;

import java.util.List;

import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.domain.RegimeOpt;
import com.hq.ecmp.mscore.domain.RegimePo;
import com.hq.ecmp.mscore.domain.RegimeQueryPo;
import com.hq.ecmp.mscore.domain.RegimeVo;
import com.hq.ecmp.mscore.vo.RegimenVO;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IRegimeInfoService
{
    /**
     * 根据用车制度id查询用车值得详细信息
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public RegimeInfo selectRegimeInfoById(Long regimenId);

    /**
     * 查询所有用车制度信息
     * @return
     */
    List<RegimeInfo> selectAll();

    /**
     * 查询【请填写功能名称】列表
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<RegimeInfo> selectRegimeInfoList(RegimeInfo regimeInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertRegimeInfo(RegimeInfo regimeInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateRegimeInfo(RegimeInfo regimeInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param regimenIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRegimeInfoByIds(Long[] regimenIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRegimeInfoById(Long regimenId);

    /**
     * 根据用户id查询用车制度集合(可加场景条件)
     * @param userId
     * @return
     */
    List<RegimenVO> findRegimeInfoListByUserId(Long userId, Long sceneId);
    
    /**
     * 判断制度可用车方式是否含有自有车
     * @param
     * @return
     */
     public boolean findOwnCar(Long regimenId);
     
     
     public boolean createRegime(RegimePo regimePo);
     
     /**
      * 分页查询制度列表
      * @param regimeQueryPo
      * @return
      */
     public List<RegimeVo> queryRegimeList(RegimeQueryPo regimeQueryPo);
     
     
     public Integer queryRegimeListCount(RegimeQueryPo regimeQueryPo);
     
     
     public boolean optRegime(RegimeOpt regimeOpt);
     
     public RegimeInfo  queryRegimeType(Long regimeId);
     
     public RegimeVo queryRegimeDetail(Long regimeId);
     
     /**
      * 查询行程节点对应的用车方式
      * @param noteId
      * @return
      */
     public String queryUseCarModelByNoteId(Long noteId);
     
     public String queryUseCarModelByJourneyId(Long journeyId);

    /**
     * 获取用户可用网约车等级 P001,P002,P003
     * @return
     */
    String getUserOnlineCarLevels(Long regimenId);
}

