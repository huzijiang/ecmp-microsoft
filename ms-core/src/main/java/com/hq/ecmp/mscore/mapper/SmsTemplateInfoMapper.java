package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.SmsTemplateInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-03-21
 */
public interface SmsTemplateInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param templateId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public SmsTemplateInfo selectSmsTemplateInfoById(Long templateId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param smsTemplateInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<SmsTemplateInfo> selectSmsTemplateInfoList(SmsTemplateInfo smsTemplateInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param smsTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertSmsTemplateInfo(SmsTemplateInfo smsTemplateInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param smsTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateSmsTemplateInfo(SmsTemplateInfo smsTemplateInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param templateId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSmsTemplateInfoById(Long templateId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param templateIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSmsTemplateInfoByIds(Long[] templateIds);
}
