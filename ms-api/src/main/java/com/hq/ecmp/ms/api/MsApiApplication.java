package com.hq.ecmp.ms.api;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.hq.core.config.DruidConfig;
import com.hq.core.config.properties.DruidProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwaggerBootstrapUI
@EnableSwagger2
@Import({DruidConfig.class,DruidProperties.class})
@SpringBootApplication()
public class MsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsApiApplication.class, args);
    }

}
