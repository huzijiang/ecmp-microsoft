package com.hq.ecmp.constant;

/**
 * @ClassName DriverBehavior
 * @Description TODO 司机行为枚举类
 * @Author yj
 * @Date 2020/3/10 14:42
 * @Version 1.0
 */
public enum  DriverBehavior {
    PICKUP_PASSENGER("1","出发去接乘客"),
    ARRIVE("2","司机到达"),
    START_SERVICE("3","开始服务"),
    SERVICE_COMPLETION("4","服务完成"),
    ;

    private String type;
    private String typeName;

    DriverBehavior(String type, String typeName) {
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
