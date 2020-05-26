package com.hq.ecmp.mscore.service.dispatchstrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName
 * @Description TODO 调度策略工厂
 * @Author yj
 * @Date 2020/5/25 17:40
 * @Version 1.0
 */
@Service
public class DispatchStrategyEngineFactory {

    @Autowired
    Map<String,DispatchStrategy> dispatchStrategyMaps = new ConcurrentHashMap<>(6);

    public DispatchStrategy getDispatchStrategy(String serviceName) throws Exception {
        DispatchStrategy dispatchStrategy = dispatchStrategyMaps.get(serviceName);
        if(dispatchStrategy == null){
            throw new Exception("no strategy found");
        }
        return dispatchStrategy;
    }
}
