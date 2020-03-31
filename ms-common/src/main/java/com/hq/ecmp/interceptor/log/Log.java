package com.hq.ecmp.interceptor.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 日志输出注解
 * <p>
 * 输出示例如下:
 * =======================>
 * 操作: 查询订单信息
 * 执行: com.hq.ecmp.cotroller.orderController.queryPage()
 * 入参: pageSize: 10, pageNum: 1, orderId: 1000
 * 耗时: 6ms
 * <=======================
 * </p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    /**
     * 传入当前操作内容，eg: 新增一个订单
     */
    String value() default "";

    /**
     * 排除不打印的参数，比如当参数传入HttpServletRequest、HttpServletResponse等无需打印参数时，需手动加入排除
     */
    Class[] exclude() default {};
}
