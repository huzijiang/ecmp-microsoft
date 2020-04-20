package com.hq.ecmp.mscore.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.hq.common.utils.DateUtils;
import com.hq.common.utils.StringUtils;
import com.hq.ecmp.constant.CarModeEnum;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.constant.ConfigTypeEnum;
import com.hq.ecmp.mscore.domain.EcmpConfig;
import com.hq.ecmp.mscore.dto.config.*;
import com.hq.ecmp.mscore.mapper.EcmpConfigMapper;
import com.hq.ecmp.mscore.service.IEcmpConfigService;
import com.hq.ecmp.mscore.service.ZimgService;
import com.hq.ecmp.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.hq.ecmp.constant.CommonConstant.*;

/**
 * 参数配置Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
@Slf4j
public class EcmpConfigServiceImpl implements IEcmpConfigService {

    @Autowired
    private EcmpConfigMapper ecmpConfigMapper;

    @Autowired
    private ZimgService zimgService;

    @Value("${app.background}")
    private String defaultBackGround;

    @Value("${app.welcome}")
    private String defaultWelcome;

    /**
     * 查询参数配置
     *
     * @param configId 参数配置ID
     * @return 参数配置
     */
    @Override
    public EcmpConfig selectEcmpConfigById(Integer configId) {
        return ecmpConfigMapper.selectEcmpConfigById(configId);
    }

    /**
     * 查询参数配置列表
     *
     * @param ecmpConfig 参数配置
     * @return 参数配置
     */
    @Override
    public List<EcmpConfig> selectEcmpConfigList(EcmpConfig ecmpConfig) {
        return ecmpConfigMapper.selectEcmpConfigList(ecmpConfig);
    }

    /**
     * 新增参数配置
     *
     * @param ecmpConfig 参数配置
     * @return 结果
     */
    @Override
    public int insertEcmpConfig(EcmpConfig ecmpConfig) {
        ecmpConfig.setCreateTime(DateUtils.getNowDate());
        return ecmpConfigMapper.insertEcmpConfig(ecmpConfig);
    }

    /**
     * 修改参数配置
     *
     * @param ecmpConfig 参数配置
     * @return 结果
     */
    @Override
    public int updateEcmpConfig(EcmpConfig ecmpConfig) {
        ecmpConfig.setUpdateTime(DateUtils.getNowDate());
        return ecmpConfigMapper.updateEcmpConfig(ecmpConfig);
    }

    /**
     * 批量删除参数配置
     *
     * @param configIds 需要删除的参数配置ID
     * @return 结果
     */
    @Override
    public int deleteEcmpConfigByIds(Integer[] configIds) {
        return ecmpConfigMapper.deleteEcmpConfigByIds(configIds);
    }

    /**
     * 删除参数配置信息
     *
     * @param configId 参数配置ID
     * @return 结果
     */
    @Override
    public int deleteEcmpConfigById(Integer configId) {
        return ecmpConfigMapper.deleteEcmpConfigById(configId);
    }

    @Override
    public ConfigInfoDTO selectConfigInfo() {
        try {
            //查询基本信息
            EcmpConfig c = EcmpConfig.builder().configKey(ConfigTypeEnum.BASE_INFO.getConfigKey()).build();
            EcmpConfig baseInfo = ecmpConfigMapper.selectConfigByKey(c);
            EcmpConfig messageInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.MESSAGE_INFO.getConfigKey()).build());
            EcmpConfig backgroundInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.BACKGROUND_IMAGE_INFO.getConfigKey()).build());
            EcmpConfig welcomeInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.WELCOME_IMAGE_INFO.getConfigKey()).build());
            EcmpConfig smsInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.SMS_INFO.getConfigKey()).build());
            EcmpConfig virtualPhoneInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.VIRTUAL_PHONE_INFO.getConfigKey()).build());
            EcmpConfig orderConfirmInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.ORDER_CONFIRM_INFO.getConfigKey()).build());
            EcmpConfig dispatchInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.DISPATCH_INFO.getConfigKey()).build());
            EcmpConfig waitInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.WAIT_MAX_MINUTE.getConfigKey()).build());

            ConfigInfoDTO configInfoDTO = new ConfigInfoDTO();
            if (baseInfo != null) {
                configInfoDTO.setBaseInfo(GsonUtils.jsonToBean(baseInfo.getConfigValue(), EnterPriseBaseInfoDTO.class));
            }
            if (backgroundInfo != null) {
                configInfoDTO.setBackgroundImageInfo(GsonUtils.jsonToBean(backgroundInfo.getConfigValue(), ConfigValueDTO.class));
            }
            if (messageInfo != null) {
                configInfoDTO.setMessageInfo(GsonUtils.jsonToBean(messageInfo.getConfigValue(), ConfigValueDTO.class));
            }
            if (welcomeInfo != null) {
                configInfoDTO.setWelcomeImageInfo(GsonUtils.jsonToBean(welcomeInfo.getConfigValue(), ConfigValueDTO.class));
            }
            if (smsInfo != null) {
                configInfoDTO.setSmsInfo(GsonUtils.jsonToBean(smsInfo.getConfigValue(), ConfigValueDTO.class));
            }
            if (virtualPhoneInfo != null) {
                configInfoDTO.setVirtualPhoneInfo(GsonUtils.jsonToBean(virtualPhoneInfo.getConfigValue(), ConfigValueDTO.class));
            }
            if (orderConfirmInfo != null) {
                configInfoDTO.setOrderConfirmInfo(GsonUtils.jsonToBean(orderConfirmInfo.getConfigValue(), OrderConfirmSetting.class));
            }
            if (dispatchInfo != null) {
                configInfoDTO.setDispatchInfo(GsonUtils.jsonToBean(dispatchInfo.getConfigValue(), ConfigAutoDispatchDTO.class));
            }
            if (waitInfo != null) {
                configInfoDTO.setWaitMaxMinute(GsonUtils.jsonToBean(waitInfo.getConfigValue(), ConfigValueDTO.class));
            }
            return configInfoDTO;
        } catch (Exception e) {
            log.error("查询企业信息失败 {}", e);
            return new ConfigInfoDTO();
        }
    }

    @Override
    public void setUpBaseInfo(EnterPriseBaseInfoDTO enterPriseBaseInfoDTO) {
        try {
            //查询基本信息
            EcmpConfig baseInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.BASE_INFO.getConfigKey()).build());
            //判断是否设置过，存在则更新设置
            if (baseInfo == null) {
                EcmpConfig baseConfig = new EcmpConfig();
                baseConfig.setConfigName("企业基本信息");
                baseConfig.setConfigKey(ConfigTypeEnum.BASE_INFO.getConfigKey());
                baseConfig.setConfigType(ConfigTypeEnum.BASE_INFO.getConfigType());
                baseConfig.setConfigValue(JSON.toJSONString(enterPriseBaseInfoDTO));
                baseConfig.setCreateTime(new Date());
                ecmpConfigMapper.insertEcmpConfig(baseConfig);
            } else {
                EcmpConfig baseConfig = new EcmpConfig();
                baseConfig.setConfigKey(ConfigTypeEnum.BASE_INFO.getConfigKey());
                baseConfig.setConfigValue(JSON.toJSONString(enterPriseBaseInfoDTO));
                baseConfig.setUpdateTime(new Date());
                ecmpConfigMapper.updateConfigByKey(baseConfig);
            }
        } catch (Exception e) {
            log.error("查询企业信息失败 {}", e);
        }
    }

    @Override
    public void setUpWelComeImage(String status, String value, MultipartFile file) {
        try {
            EcmpConfig welcomeInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.WELCOME_IMAGE_INFO.getConfigKey()).build());
            //判断是否设置过，存在则更新设置
            EcmpConfig baseConfig = new EcmpConfig();
            baseConfig.setConfigKey(ConfigTypeEnum.WELCOME_IMAGE_INFO.getConfigKey());
            if (SWITCH_OFF.equals(status)) {
                value = defaultWelcome;
            } else {
                value = zimgService.uploadImage(file);
            }
            ConfigValueDTO configValueDTO = ConfigValueDTO.builder().status(status).value(value).build();
            baseConfig.setConfigValue(JSON.toJSONString(configValueDTO));
            if (welcomeInfo == null) {
                baseConfig.setConfigName("企业开屏图片信息");
                baseConfig.setConfigType(ConfigTypeEnum.WELCOME_IMAGE_INFO.getConfigType());
                baseConfig.setCreateTime(new Date());
                ecmpConfigMapper.insertEcmpConfig(baseConfig);
            } else {
                baseConfig.setUpdateTime(new Date());
                ecmpConfigMapper.updateConfigByKey(baseConfig);
            }
        } catch (Exception e) {
            log.error("企业开屏图片信息 {}", e);
        }
    }

    @Override
    public void setUpBackGroundImage(String status, String value, MultipartFile file) {
        try {
            EcmpConfig backgroundInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.BACKGROUND_IMAGE_INFO.getConfigKey()).build());
            //判断是否设置过，存在则更新设置
            EcmpConfig baseConfig = new EcmpConfig();
            if (SWITCH_OFF.equals(status)) {
                value = defaultBackGround;
            } else {
                value = zimgService.uploadImage(file);
            }
            baseConfig.setConfigKey(ConfigTypeEnum.BACKGROUND_IMAGE_INFO.getConfigKey());
            ConfigValueDTO configValueDTO = ConfigValueDTO.builder().status(status).value(value).build();
            baseConfig.setConfigValue(JSON.toJSONString(configValueDTO));
            if (backgroundInfo == null) {
                baseConfig.setConfigName("背景图片信息");
                baseConfig.setConfigType(ConfigTypeEnum.BACKGROUND_IMAGE_INFO.getConfigType());
                baseConfig.setCreateTime(new Date());
                ecmpConfigMapper.insertEcmpConfig(baseConfig);
            } else {
                baseConfig.setUpdateTime(new Date());
                ecmpConfigMapper.updateConfigByKey(baseConfig);
            }
        } catch (Exception e) {
            log.error("背景图片信息 {}", e);
        }
    }

    @Override
    public void setUpMessageConfig(String status) {
        try {
            EcmpConfig messageInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.MESSAGE_INFO.getConfigKey()).build());
            //判断是否设置过，存在则更新设置
            EcmpConfig baseConfig = new EcmpConfig();
            baseConfig.setConfigKey(ConfigTypeEnum.MESSAGE_INFO.getConfigKey());
            ConfigValueDTO configValueDTO = ConfigValueDTO.builder().status(status).build();
            baseConfig.setConfigValue(JSON.toJSONString(configValueDTO));
            if (messageInfo == null) {
                baseConfig.setConfigName("企业公告设置");
                baseConfig.setConfigType(ConfigTypeEnum.MESSAGE_INFO.getConfigType());
                baseConfig.setCreateTime(new Date());
                ecmpConfigMapper.insertEcmpConfig(baseConfig);
            } else {
                baseConfig.setUpdateTime(new Date());
                ecmpConfigMapper.updateConfigByKey(baseConfig);
            }
        } catch (Exception e) {
            log.error("企业公告设置 {}", e);
        }
    }

    @Override
    public void setUpSms(String status) {
        try {
            EcmpConfig smsInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.SMS_INFO.getConfigKey()).build());
            //判断是否设置过，存在则更新设置
            EcmpConfig baseConfig = new EcmpConfig();
            baseConfig.setConfigKey(ConfigTypeEnum.SMS_INFO.getConfigKey());
            ConfigValueDTO configValueDTO = ConfigValueDTO.builder().status(status).build();
            baseConfig.setConfigValue(JSON.toJSONString(configValueDTO));
            if (smsInfo == null) {
                baseConfig.setConfigName("短信开关");
                baseConfig.setConfigType(ConfigTypeEnum.SMS_INFO.getConfigType());
                baseConfig.setCreateTime(new Date());
                ecmpConfigMapper.insertEcmpConfig(baseConfig);
            } else {
                baseConfig.setUpdateTime(new Date());
                ecmpConfigMapper.updateConfigByKey(baseConfig);
            }
        } catch (Exception e) {
            log.error("短信开关 {}", e);
        }
    }

    @Override
    public void setUpVirtualPhone(String status) {
        try {
            EcmpConfig virtualInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.VIRTUAL_PHONE_INFO.getConfigKey()).build());
            //判断是否设置过，存在则更新设置
            EcmpConfig baseConfig = new EcmpConfig();
            baseConfig.setConfigKey(ConfigTypeEnum.VIRTUAL_PHONE_INFO.getConfigKey());
            ConfigValueDTO configValueDTO = ConfigValueDTO.builder().status(status).build();
            baseConfig.setConfigValue(JSON.toJSONString(configValueDTO));
            if (virtualInfo == null) {
                baseConfig.setConfigName("虚拟小号开关");
                baseConfig.setConfigType(ConfigTypeEnum.VIRTUAL_PHONE_INFO.getConfigType());
                baseConfig.setCreateTime(new Date());
                ecmpConfigMapper.insertEcmpConfig(baseConfig);
            } else {
                baseConfig.setUpdateTime(new Date());
                ecmpConfigMapper.updateConfigByKey(baseConfig);
            }
        } catch (Exception e) {
            log.error("虚拟小号开关 {}", e);
        }
    }

    @Override
    public void setUpOrderConfirm(String status, String value, String owenType, String rideHailing) {
        try {
            EcmpConfig backgroundInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.ORDER_CONFIRM_INFO.getConfigKey()).build());
            //判断是否设置过，存在则更新设置
            EcmpConfig baseConfig = new EcmpConfig();
            baseConfig.setConfigKey(ConfigTypeEnum.ORDER_CONFIRM_INFO.getConfigKey());
            OrderConfirmSetting orderConfirmSetting = new OrderConfirmSetting();
            if (SWITCH_ON_CUSTOM.equals(status)) {
                ConfigOrderConfirmDTO configOrderConfirmDTO = new ConfigOrderConfirmDTO();
                configOrderConfirmDTO.setOwenType(owenType);
                configOrderConfirmDTO.setRideHailing(rideHailing);
                orderConfirmSetting.setValue(configOrderConfirmDTO);
            } else {
                orderConfirmSetting.setValue(null);
            }
            orderConfirmSetting.setStatus(status);
            baseConfig.setConfigValue(JSON.toJSONString(orderConfirmSetting));
            if (backgroundInfo == null) {
                baseConfig.setConfigName("确认订单");
                baseConfig.setConfigType(ConfigTypeEnum.ORDER_CONFIRM_INFO.getConfigType());
                baseConfig.setCreateTime(new Date());
                ecmpConfigMapper.insertEcmpConfig(baseConfig);
            } else {
                baseConfig.setUpdateTime(new Date());
                ecmpConfigMapper.updateConfigByKey(baseConfig);
            }
        } catch (Exception e) {
            log.error("确认订单 {}", e);
        }
    }

    @Override
    public void setUpDispatchInfo(String status, String value) {
        try {
            EcmpConfig dispatchInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.DISPATCH_INFO.getConfigKey()).build());
            //判断是否设置过，存在则更新设置
            EcmpConfig baseConfig = new EcmpConfig();
            baseConfig.setConfigKey(ConfigTypeEnum.DISPATCH_INFO.getConfigKey());
            ConfigAutoDispatchDTO autoDispatchDTO = new ConfigAutoDispatchDTO();
            autoDispatchDTO.setStatus(status);

            if (SWITCH_ON_CUSTOM.equals(status)) {
                Type type = new TypeToken<List<AutoDispatchSetting>>() {
                }.getType();
                List<AutoDispatchSetting> autoDispatchSetting = GsonUtils.jsonToBean(value, type);
                autoDispatchDTO.setValue(autoDispatchSetting);
            } else {
                autoDispatchDTO.setValue(null);
            }
            baseConfig.setConfigValue(JSON.toJSONString(autoDispatchDTO));
            if (dispatchInfo == null) {
                baseConfig.setConfigName("自动派单方式");
                baseConfig.setConfigType(ConfigTypeEnum.DISPATCH_INFO.getConfigType());
                baseConfig.setCreateTime(new Date());
                ecmpConfigMapper.insertEcmpConfig(baseConfig);
            } else {
                baseConfig.setUpdateTime(new Date());
                ecmpConfigMapper.updateConfigByKey(baseConfig);
            }
        } catch (Exception e) {
            log.error("自动派单方式 {}", e);
        }
    }

    @Override
    public void setUpWaitMaxMinute(String status, String value) {
        try {
            EcmpConfig waitInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.WAIT_MAX_MINUTE.getConfigKey()).build());
            //判断是否设置过，存在则更新设置
            EcmpConfig baseConfig = new EcmpConfig();
            baseConfig.setConfigKey(ConfigTypeEnum.WAIT_MAX_MINUTE.getConfigKey());
            ConfigValueDTO configValueDTO = ConfigValueDTO.builder().status(status).value(value).build();
            baseConfig.setConfigValue(JSON.toJSONString(configValueDTO));
            if (waitInfo == null) {
                baseConfig.setConfigName("往返等待时长");
                baseConfig.setConfigType(ConfigTypeEnum.WAIT_MAX_MINUTE.getConfigType());
                baseConfig.setCreateTime(new Date());
                ecmpConfigMapper.insertEcmpConfig(baseConfig);
            } else {
                baseConfig.setUpdateTime(new Date());
                ecmpConfigMapper.updateConfigByKey(baseConfig);
            }
        } catch (Exception e) {
            log.error("往返等待时长 {}", e);
        }
    }

    /**
     * 获取是否配置自动行程确认/异议
     * @param key
     * @return 1手动确认(需展示),0自动确认(无需展示)
     */
    @Override
    public int getOrderConfirmStatus(String key,String useCarMode) {
        if (key.contains(SYS_CONFIG_PREFIX)||ConfigTypeEnum.BASE_INFO.getConfigKey().equals(key)){
            return ZERO;
        }
        EcmpConfig ecmpConfig = ecmpConfigMapper.selectConfigByKey(new EcmpConfig(key));
        if (ecmpConfig!=null&& StringUtils.isNotEmpty(ecmpConfig.getConfigValue())){
            JSONObject jsonObject = JSONObject.parseObject(ecmpConfig.getConfigValue());
            String status = jsonObject.getString("status");
            if (SWITCH_OFF.equals(status)){//开
                return ONE;
            }else if(SWITCH_ON_CUSTOM.equals(status)){//自定义
                String valueJson = jsonObject.getString("value");
                JSONObject value = JSONObject.parseObject(valueJson);
                if (CarModeEnum.ORDER_MODE_HAVE.getKey().equals(useCarMode)){//自有车
                    if (SWITCH_OFF.equals(value.getString("owenType"))){
                        return ONE;
                    }
                }else{//网约车
                    if (SWITCH_OFF.equals(value.getString("rideHailing"))){
                        return ONE;
                    }
                }
            }

        }
        return ZERO;
    }

	@Override
	public boolean checkAutoDispatch() {
		EcmpConfig ecmpConfig = ecmpConfigMapper.selectConfigByKey(new EcmpConfig(ConfigTypeEnum.DISPATCH_INFO.getConfigKey()));
		if(null !=ecmpConfig && StringUtils.isNotEmpty(ecmpConfig.getConfigValue())){
		    JSONObject jsonObject = JSONObject.parseObject(ecmpConfig.getConfigValue());
            String status = jsonObject.getString("status");
            if(SWITCH_ON.equals(status)){
            	return true;
            }
		}
		return false;
	}

	@Override
	public boolean checkUpWaitMaxMinute(Long waitMin) {
		EcmpConfig waitInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.WAIT_MAX_MINUTE.getConfigKey()).build());
		if(null==waitInfo){
			return false;
		}
		String configValue = waitInfo.getConfigValue();
		if(StringUtils.isEmpty(configValue)){
			return false;
		}
		JSONObject jsonObject = JSONObject.parseObject(configValue);
		 String status = jsonObject.getString("status");
		  if (SWITCH_ON.equals(status)){//开
			  String value = jsonObject.getString("value");
			  if(StringUtils.isEmpty(value)){
				  return false;
			  }
              return waitMin.compareTo(Long.valueOf(value))>0;
          }else {
        	  return false;
          }
	}

    /**
     * 获取启动页开屏图（无token）
     * @return
     */
    /*@Override
    public ConfigInfoDTO getStartupChart() {
        EcmpConfig welcomeInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.WELCOME_IMAGE_INFO.getConfigKey()).build());
        ConfigInfoDTO configInfoDTO = new ConfigInfoDTO();
        if (welcomeInfo != null) {
            configInfoDTO.setWelcomeImageInfo(GsonUtils.jsonToBean(welcomeInfo.getConfigValue(), ConfigValueDTO.class));
        }
        return configInfoDTO;
    }*/

    /**
     * 获取首页背景图
     * @return
     */
    /*@Override
    public ConfigInfoDTO getHomeChart() {
        EcmpConfig backgroundInfo = ecmpConfigMapper.selectConfigByKey(EcmpConfig.builder().configKey(ConfigTypeEnum.BACKGROUND_IMAGE_INFO.getConfigKey()).build());        ConfigInfoDTO configInfoDTO = new ConfigInfoDTO();
        ConfigInfoDTO configInfo = new ConfigInfoDTO();
        if (backgroundInfo != null) {
            configInfo.setBackgroundImageInfo(GsonUtils.jsonToBean(backgroundInfo.getConfigValue(), ConfigValueDTO.class));
        }
        return configInfo;
    }*/
}
