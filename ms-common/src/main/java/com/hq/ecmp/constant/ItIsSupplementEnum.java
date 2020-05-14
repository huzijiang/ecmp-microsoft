package com.hq.ecmp.constant;

//补单状态与直接调度区分
public enum ItIsSupplementEnum {

    ORDER_NORMAL_STATUS("正常单子","N000"),
    ORDER_REPLENISHMENT_STATUS("补单","Y000"),
    ORDER_Replenishment_status("直接调度","D000");

    private String key;
    private String value;

    ItIsSupplementEnum(String key,String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
