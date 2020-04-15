package com.hq.ecmp.mscore.service;

import java.util.List;

import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.domain.RegimeOpt;
import com.hq.ecmp.mscore.domain.RegimePo;
import com.hq.ecmp.mscore.domain.RegimeQueryPo;
import com.hq.ecmp.mscore.domain.RegimeVo;
import com.hq.ecmp.mscore.vo.CarLevelAndPriceReVo;
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
     
     
     public boolean updateRegime(RegimePo regimePo);
     
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
      * 查询行程节点对应的用车制度
      * @param noteId
      * @return
      */
     public RegimeInfo queryUseCarModelByNoteId(Long noteId);
     
     public String queryUseCarModelByJourneyId(Long journeyId);

    /**
     * 获取用户可用网约车等级 P001,P002,P003
     * @return
     */
    String getUserOnlineCarLevels(Long regimenId,String type);

    /**
     * 判断是否不调度
     * 仅有网约车时  不调度  走网约
     * 自有车+网约车时，但上车地点“不在”车队的用车城市范围内   不走调度  走网约
     * @param regimeId
     * @param cityCode
     * @return
     */
    public boolean judgeNotDispatch(Long regimeId,String cityCode);

    /**
     * app端查询用车制度详情
     * @param regimenId
     * @return
     */
    RegimeVo selectRegimeDetailById(Long regimenId);
    
    /**
     * 
     * @param orderId 订单ID
     * @param useCarMode  非必传   用车方式  W100-自有车    W200-网约车    
     * @return  车型    P001   P002    P003   多个用,拼接
     */
    String queryCarModeLevel(Long orderId,String useCarMode);

    /**
     * 通过订单，用车方式获取车型以及预估价格等相关信息
     * @param orderId
     * @param useCarMode,不传默认为网约车 W100-自有车 	W200-网约车
     */
    List<CarLevelAndPriceReVo> getCarlevelAndPriceByOrderId(Long orderId, String useCarMode) throws Exception;

    /*根据场景id查询制度集合*/
    List<RegimenVO> selectRegimesBySceneId(Long sceneId);

    /**
     * 查询所有制度RegimenVO
     * @return
     */
    List<RegimenVO> selectAllRegimenVO();

}

