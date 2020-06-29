package com.hq.ecmp.ms.api;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.hq.core.config.DruidConfig;
import com.hq.core.config.properties.DruidProperties;
import com.hq.ecmp.constant.InterceptorConstant;
import com.hq.ecmp.interceptor.HeaderInterceptor;
import com.hq.ecmp.interceptor.RequestInterceptor;
import com.hq.ecmp.interceptor.TraceLogInterceptor;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: zj.hu
 * @Date: 2020-01-04 15:26
 */
@SwaggerDefinition
@EnableSwaggerBootstrapUI
@EnableScheduling
@Import({DruidConfig.class,DruidProperties.class})
@SpringBootApplication(scanBasePackages ={"com.hq.*"},exclude={DataSourceAutoConfiguration.class})
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
public class MsApiApplication<author> implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(MsApiApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TraceLogInterceptor()).addPathPatterns("/**").excludePathPatterns(InterceptorConstant.EXCLUDE_HEADER);
        registry.addInterceptor(new HeaderInterceptor()).addPathPatterns("/**").excludePathPatterns(InterceptorConstant.EXCLUDE_HEADER);
        registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**").excludePathPatterns(InterceptorConstant.EXCLUDE_HEADER);
    }

}
