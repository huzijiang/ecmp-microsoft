package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.SmsConstant;
import com.hq.ecmp.mscore.domain.SmsTemplateInfo;
import com.hq.ecmp.mscore.mapper.SmsTemplateInfoMapper;
import com.hq.ecmp.mscore.service.ISmsTemplateInfoService;
import com.hq.ecmp.mscore.service.ThirdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-03-21
 */
@Service
@Slf4j
public class SmsTemplateInfoServiceImpl implements ISmsTemplateInfoService
{
    @Autowired
    private SmsTemplateInfoMapper smsTemplateInfoMapper;
    @Resource
    private ThirdService thirdService;

    /**
     * 查询【请填写功能名称】
     * 
     * @param templateId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public SmsTemplateInfo selectSmsTemplateInfoById(Long templateId)
    {
        return smsTemplateInfoMapper.selectSmsTemplateInfoById(templateId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param smsTemplateInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<SmsTemplateInfo> selectSmsTemplateInfoList(SmsTemplateInfo smsTemplateInfo)
    {
        return smsTemplateInfoMapper.selectSmsTemplateInfoList(smsTemplateInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param smsTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertSmsTemplateInfo(SmsTemplateInfo smsTemplateInfo)
    {
        smsTemplateInfo.setCreateTime(DateUtils.getNowDate());
        return smsTemplateInfoMapper.insertSmsTemplateInfo(smsTemplateInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param smsTemplateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateSmsTemplateInfo(SmsTemplateInfo smsTemplateInfo)
    {
        smsTemplateInfo.setUpdateTime(DateUtils.getNowDate());
        return smsTemplateInfoMapper.updateSmsTemplateInfo(smsTemplateInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param templateIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSmsTemplateInfoByIds(Long[] templateIds)
    {
        return smsTemplateInfoMapper.deleteSmsTemplateInfoByIds(templateIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param templateId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteSmsTemplateInfoById(Long templateId)
    {
        return smsTemplateInfoMapper.deleteSmsTemplateInfoById(templateId);
    }

    /**
     * 发送短信
     * @param templateCode 模板code
     * @param params 参数数组，和模板顺序一致
     * @param phoneNum  手机号
     * @return
     * @throws Exception
     */
    @Override
    public boolean sendSms(String templateCode,Map<String,String> params,String phoneNum) throws Exception {
        String content=buildContent(templateCode,params);
        return  thirdService.sendSms(phoneNum,content);
    }

    /**
     * 获取短信内容
     * @param templateCode 模板码
     * @param params 替换字符串
     * @return
     * 2016年8月20日
     */
    private String buildContent (String templateCode, Map<String,String> params) throws Exception {
        SmsTemplateInfo smsTemplateInfo = new SmsTemplateInfo();
        smsTemplateInfo.setTemplateCode(templateCode);
        smsTemplateInfo.setState(SmsConstant.SMS_VALID);
        List<SmsTemplateInfo> smsTemplateInfos = smsTemplateInfoMapper.selectSmsTemplateInfoList(smsTemplateInfo);
        SmsTemplateInfo smsTemplateInfoCh = smsTemplateInfos.get(0);
        if (null == smsTemplateInfoCh) throw new Exception("找不到短信模板，模板码：【"+templateCode+"】");
        String content = smsTemplateInfoCh.getContent();
        Set<String> keys = params.keySet();
        if (null != params) {
            if (smsTemplateInfoCh.getParameterAmount() != keys.size()) {
                throw new Exception("短信模板参数不匹配，模板码：【"+templateCode+"】，参数："+ params);
            }
            for (String key:keys) {
                content = content.replace("${"+key+"}", params.get(key));
            }
        } else {
            if(smsTemplateInfoCh.getParameterAmount()!=0) throw new Exception("短信模板参数不匹配，模板码：【"+templateCode+"】，传入参数为空");
        }
        return content;
    }

}
