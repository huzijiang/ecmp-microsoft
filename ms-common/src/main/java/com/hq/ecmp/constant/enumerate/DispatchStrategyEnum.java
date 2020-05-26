package com.hq.ecmp.constant.enumerate;

/**
 * @ClassName
 * @Description TODO
 * @Author yj
 * @Date 2020/5/26 12:18
 * @Version 1.0
 */
public enum  DispatchStrategyEnum {

    InCarAndDriverStrategy("内部车和内部司机","inCarAndDriverStrategy"),
    InCarAndOutDriverStrategy("内部车和外部司机","inCarAndOutDriverStrategy"),
    InCarAndSelfDriverStrategy("内部车和自驾","inCarAndSelfDriverStrategy"),
    InDriverAndOutCarStrategy("内部司机和外部车","inDriverAndOutCarStrategy"),
    OutCarAndOutDriverStrategy("外部车和外部司机","outCarAndOutDriverStrategy"),
    OutCarAndSelfDriverStrategy("外部车和自驾","outCarAndSelfDriverStrategy"),
    ;


    private String strategyName;
    private String strategyServiceName;

    DispatchStrategyEnum(String strategyName,String strategyServiceName){
        this.strategyName = strategyName;
        this.strategyServiceName = strategyServiceName;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public String getStrategyServiceName() {
        return strategyServiceName;
    }
}
