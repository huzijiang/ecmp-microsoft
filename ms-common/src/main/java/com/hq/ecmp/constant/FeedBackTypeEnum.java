package com.hq.ecmp.constant;

import lombok.Data;



public enum FeedBackTypeEnum {



    COMPLAIN_TYPE("T001"," 投诉类型");

    private String type;
    private String typeName;

    FeedBackTypeEnum(String type, String typeName) {
        this.type = type;
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


}
