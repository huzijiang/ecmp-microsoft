package com.hq.ecmp.constant;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/13 18:17
 */
public enum  ApproveTypeEnum {

    APPROVE_T001("T001","部门负责人"),
    APPROVE_T002("T002","指定角色"),
    APPROVE_T003("T003","指定员工"),
    APPROVE_T004("T004","项目负责人"),
            ;

    private String key;
    private String desc;//描述

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    ApproveTypeEnum(String key, String desc) {
        this.key=key;
        this.desc=desc;
    }
    public static Map<String, ApproveTypeEnum> getParam(){
        Map<String, ApproveTypeEnum> map = Maps.newHashMap();
        ApproveTypeEnum[] hintEnums = ApproveTypeEnum.values();
        for (ApproveTypeEnum hintEnum : hintEnums) {
            map.put(hintEnum.getKey(), hintEnum);
        }
        return map;
    }
    public static ApproveTypeEnum format(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        Map<String, ApproveTypeEnum> param = ApproveTypeEnum.getParam();
        return param.get(key);
    }
    public static String formatKey(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }
        Map<String, ApproveTypeEnum> param = ApproveTypeEnum.getParam();
        return param.get(key).getDesc();
    }

}
