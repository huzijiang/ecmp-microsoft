package com.hq.ecmp.constant;


/**
 * 收款管理状态
 */
public enum CollectionQuittanceEnum {


    COLLECTION_WAIT("S000", "收款单待确认"),

    COLLECTION_WAIT_MAKE("S100", "付款单位确认后"),

    COLLECTION_WAIT_OVER("S150", "付款单位已付款，收款单位未确认"),

    COLLECTION_OVER("S200", "确认已收款(收款方确认)"),

    COLLECTION_DISCARD("S444", "已废弃");











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

    CollectionQuittanceEnum(String key, String desc) {
        this.key=key;
        this.desc=desc;
    }


}
