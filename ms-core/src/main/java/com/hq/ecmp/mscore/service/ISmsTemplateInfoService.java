package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.SmsTemplateInfo;

import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author hqer
 * @date 2020-03-21
 */
public interface ISmsTemplateInfoService 
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
     * 批量删除【请填写功能名称】
     * 
     * @param templateIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSmsTemplateInfoByIds(Long[] templateIds);

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param templateId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteSmsTemplateInfoById(Long templateId);

    /**
     * 短信发送功能
     * @param templateCode
     * @param params 参数数组，和模板顺序一致
     * @param phoneNum
     * @return
     */
    public boolean sendSms(String templateCode, Map<String,String> params, String phoneNum) throws Exception;
}
