package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.domain.UndoSMSTemplate;
import com.hq.ecmp.mscore.dto.ApplyInfoDTO;
import com.hq.ecmp.mscore.dto.JourneyAddressInfoDto;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface ApplyInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param applyId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public ApplyInfo selectApplyInfoById(Long applyId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param applyInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ApplyInfo> selectApplyInfoList(ApplyInfo applyInfo);

    /**
     * 新增申请信息
     *
     * @param applyInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertApplyInfo(ApplyInfo applyInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param applyInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateApplyInfo(ApplyInfo applyInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param applyId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteApplyInfoById(Long applyId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param applyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteApplyInfoByIds(Long[] applyIds);

    /**
     * 分页查询申请列表
     * @param userId
     * @return
     */
    List<ApplyInfoDTO> selectApplyInfoListByPage(Long userId);

    /**暂时不用*/
    List<MessageDto> getOrderCount(Long userId);

    //获取当前申请通知列表
    MessageDto getApplyMessage(@Param("userId") Long userId,@Param("stateList") String[] stateList);

    int getApplyApproveCount(@Param("userId")Long userId,@Param("state")String state,@Param("roleIds")List<Long> roleIds);

    List<ApplyInfo> checkApplyExpiredList(@Param("state") String state);
    
    Integer queryApplyNumByRegimeId(Long regimeId);

    /**
     * 通过申请id查询订单id
     * @param applyId
     * @return
     */
    Long selectOrderInfoById(@Param("applyId") Long applyId);

    /**
     * 撤销数据列表
     * @param applyId
     * @return
     */
    UndoSMSTemplate queryApplyUndoList(@Param("applyId") Long applyId);

    /**
     * 通过订单id查询申请单申请时填写的车型id
     * @param orderId
     * @return
     */
    Long getApplyCarTypeIdWithOrderId(Long orderId);

    /**
     * 增加多个下车地点
     * @param journeyAddressInfoDto
     */
    int insertJourneyAddressInfo(JourneyAddressInfoDto journeyAddressInfoDto);

    /**
     * 通过行程id查询申请相关信息
     * @return
     */
    DispatchVo getDispatchApplyInfoByJourneyId(@Param("journeyId") Long journeyId);

    /**
     * 查询所需要的id
     * @param applyId
     * @return
     */
    ApplySingleIdVO getApplySingleIdVO(@Param("applyId") Long applyId);

    /**
     * 删除多个下车地点
     * @param journeyId
     */
    void deleteJourneyAddressInfo(@Param("journeyId")Long journeyId);

    List<Map<String, String>> getApplyStateCount(@Param("companyId") Long companyId,@Param("deptId") Long deptId);

    List<UserApplySingleVo> getApplyListPage(@Param("deptId") Long deptId,@Param("applyId") Long applyId);

    /**
     * 申请单详情
     * @param applyId
     * @return
     */
    ApplySingleVO getApplyInfoDetail(@Param("applyId") Long applyId);

    /**
     * 通过行程id查询多个下车地址
     * @param journeyId
     * @return
     */
    List<AddressVO> getJourneyAddressInfoByJourneyId(@Param("journeyId")Long journeyId);

    /**
     * 查询申请单调度员所需要的短信内容
     * @param applyId
     * @return
     */
    UndoSMSTemplate getUndoSMSTemplate(@Param("applyId") Long applyId);
}
