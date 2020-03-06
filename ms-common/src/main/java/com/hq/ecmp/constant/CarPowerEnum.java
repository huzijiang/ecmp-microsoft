package com.hq.ecmp.constant;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 提示语模块
 */
public enum CarPowerEnum {
    gasoline("P001","汽油"),
    Diesel("P002","柴油"),
    ELECTRIC("P003","电动"),
    Hybrid("P004","混动"),
    ;
    private String Key;//提示语

    private String desc;//描述

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    CarPowerEnum(String key, String desc) {
        Key = key;
        this.desc = desc;
    }
    public static Map<String, String> getParam(){
        Map<String, String> map = Maps.newHashMap();
        CarPowerEnum[] carPowerEnums = CarPowerEnum.values();
        for (CarPowerEnum carPowerEnum : carPowerEnums) {
            map.put(carPowerEnum.getKey(), carPowerEnum.getDesc());
        }
        return map;
    }
    public static String format(String key){
        Map<String, String> param = CarPowerEnum.getParam();
        return param.get(key);
    }

}
