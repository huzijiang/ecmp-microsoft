package com.hq.ecmp.ms.api;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.hq.core.config.DruidConfig;
import com.hq.core.config.properties.DruidProperties;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @Author: zj.hu
 * @Date: 2020-01-04 15:26
 */
@SwaggerDefinition
@EnableSwaggerBootstrapUI
@Import({DruidConfig.class,DruidProperties.class})
@SpringBootApplication(scanBasePackages ={"com.hq.*"},exclude={DataSourceAutoConfiguration.class})
public class MsApiApplication<author> {

    public static void main(String[] args) {

        SpringApplication.run(MsApiApplication.class, args);
    }

}
