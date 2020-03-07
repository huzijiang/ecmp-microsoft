package com.hq.ecmp.constant;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 提示语模块
 */
public enum HintEnum {
    BESERVED_Hint("S299","您已成功预约%s出发的车辆，出发前30分钟可查看驾驶员位置","服务前提示语"),
    DRIVER_ARRIVE_HINT("S600","驾驶员已到达约定上车地点，等待您上车","驾驶员到达提示语"),
    RUNNING_HINT("S616","已出发，请系好安全带","进行中提示语")
    ;
    private String state;
    private String hint;//提示语
    private String desc;//描述

    HintEnum(String state,String hint, String desc) {
        this.state = state;
        this.hint = hint;
        this.desc = desc;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getHint() {
        return hint;
    }
    public void setHint(String hint) {
        this.hint = hint;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String join(String... args) {
        return String.format(this.getHint(), args);
    }
    public static Map<String, String> getParam(){
        Map<String, String> map = Maps.newHashMap();
        HintEnum[] hintEnums = HintEnum.values();
        for (HintEnum hintEnum : hintEnums) {
            map.put(hintEnum.getState(), hintEnum.getHint());
        }
        return map;
    }
    public static String format(String key){
        Map<String, String> param = HintEnum.getParam();
        return param.get(key);
    }
}
