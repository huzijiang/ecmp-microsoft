package com.hq.ecmp.mscore.mybatisplus;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源配置
 */
@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.hq.ecmp.mscore.mapper"})
@Log4j2
public class MybatisPlusConfig {

    
    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     * 输出每条SQL语句及其执行时间，生产环境不建议使用该插件
     */
    @Bean
    @Profile({"dev","test"})// 设置 local dev 环境开启
    public PerformanceInterceptor performanceInterceptor() {
    	PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        //格式化sql语句
        performanceInterceptor.setFormat(false);
        //sql执行时间超过value值就会停止执行，单位是毫秒
        performanceInterceptor.setMaxTime(180000);
        return performanceInterceptor;
    }
    

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
    	PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    	List<ISqlParser> sqlParserList = new ArrayList<>();
        // 攻击 SQL 阻断解析器、加入解析链
        sqlParserList.add(new BlockAttackSqlParser());
        paginationInterceptor.setSqlParserList(sqlParserList);
    	//paginationInterceptor.setLocalPage(true);// 开启 PageHelper 的支持
        return paginationInterceptor;
    }

    /**
     * 注入主键生成器
     */
    @Bean
    public IKeyGenerator keyGenerator() {
        return new H2KeyGenerator();
    }

    
    /**
     * 乐观锁mybatis插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }


    /**
     * 注入sql注入器 逻辑删除需要
     */
    @Bean
    public ISqlInjector sqlInjector() {
    	return new LogicSqlInjector();
    }

    /**
     * 逻辑删除插件
     */
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        dbConfig.setLogicDeleteValue("Y");
        dbConfig.setLogicNotDeleteValue("N");
        globalConfig.setDbConfig(dbConfig);
        globalConfig.setSqlInjector(new LogicSqlInjector());
        return globalConfig;
    }



    /**
     * 如果是对全表的删除或更新操作，就会终止该操作
     * @return
     */
    @Bean
    public SqlExplainInterceptor sqlExplainInterceptor() {
    	SqlExplainInterceptor sqlExplainInterceptor= new SqlExplainInterceptor();
    	return sqlExplainInterceptor;
    }

}
