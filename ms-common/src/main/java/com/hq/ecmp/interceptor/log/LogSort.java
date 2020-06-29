package com.hq.ecmp.interceptor.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.concurrent.atomic.LongAdder;

/**
 * 自定义logback自增数字
 *
 * @author yangnan
 * @date 17/8/30
 */
public class LogSort extends ClassicConverter {
    private static LongAdder adder = new LongAdder();

    @Override
    public String convert(ILoggingEvent loggingEvent) {
        adder.increment();
        return adder.longValue() + "";
    }

}
