package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.dto.ApplyInfoDTO;
import com.hq.ecmp.mscore.dto.MessageDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
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

    /**
     * 获取用户正在进行的流程统计
     * @param userId
     * @return
     */
    List<MessageDto> getOrderCount(Long userId);

    //获取当前申请通知列表
    MessageDto getApplyMessage(@Param("userId") Long userId,@Param("stateList") String[] stateList);
}
