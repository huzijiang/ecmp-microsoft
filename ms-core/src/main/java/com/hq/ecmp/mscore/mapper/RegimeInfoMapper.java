package com.hq.ecmp.mscore.mapper;

import java.util.List;

import com.hq.ecmp.mscore.vo.DispatchVo;
import com.hq.ecmp.mscore.vo.RegimenVO;
import com.hq.ecmp.mscore.vo.UseCarTypeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hq.ecmp.mscore.domain.RegimeInfo;
import com.hq.ecmp.mscore.domain.RegimeOpt;
import com.hq.ecmp.mscore.domain.RegimePo;
import com.hq.ecmp.mscore.domain.RegimeQueryPo;
import com.hq.ecmp.mscore.domain.RegimeVo;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface RegimeInfoMapper
{
    /**
     * 根据用车制度id查询用车制度详细信息
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public RegimeInfo selectRegimeInfoById(Long regimenId);

    /**
     * 查询所有用车制度信息
     * @return
     */
    public List<RegimeInfo> selectAll();

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
     * 删除【请填写功能名称】
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteRegimeInfoById(Long regimenId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param regimenIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteRegimeInfoByIds(Long[] regimenIds);
    
    public List<RegimeVo> queryRegimeList(RegimeQueryPo regimeQueryPo);
    
    public Integer queryRegimeListCount(RegimeQueryPo regimeQueryPo);
    
    public Integer updateStatus(RegimeOpt regimeOpt);
    
    public RegimeInfo queryRegimeType(Long regimeId);

    public Integer insertRegime(RegimePo po);

    public RegimeVo queryRegimeDetail(Long regimeId);



    public RegimenVO selectRegimenVOById(@Param("regimeId") Long regimeId );


    public RegimeInfo queryUseCarModelByNoteId(Long noteId);


    public String queryUseCarModelByJourneyId(Long journeyId);
    /*
    * 根据userid 查询用车制度名称
    * */
    public  List<RegimeVo> selectByUserId(Long userId);

    /**
     * 查询用户可用网约车车型等级
     * @param regimenId
     * @return
     */
    String getUserOnlineCarLevels(Long regimenId);
    
    
    public RegimeVo queryRegimeInfoByOrderId(Long orderId);

    List<RegimenVO> selectAllRegimenVO();

    /**
     * 修改过期的制度
     * @param regimeInfo
     * @return
     */
    int updateExpiredRegimeInfo(RegimeInfo regimeInfo);

    List<Long> selectEnableRegimenIdByUserId(Long userId);

    /**
     * 申请单提交根据名称搜索城市
     * @param companyId
     * @return
     */
    Long queryRegimeInfoByCompanyId(@Param("companyId") Long companyId);

    /**
     * 查询制度中可用车型
     * @param companyId
     * @return
     */
    String queryRegimeLevelByCompanyId(@Param("companyId") Long companyId);

    /**
     * 获得制度中可用车型
     * @param regimenId
     * @return
     */
    List<UseCarTypeVO> getCanUseCarTypes(@Param("regimenId") Long regimenId,@Param("companyId") Long companyId);

    /**
     * 通过制度id查询场景名称和制度名称
     * @param regimeId
     * @return
     */
    DispatchVo getDispatchReAndSceneInfo(Long regimeId);
}
