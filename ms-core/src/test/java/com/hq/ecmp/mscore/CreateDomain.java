package com.hq.ecmp.mscore;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.jupiter.api.Test;;

import java.io.*;

/**
 * @author: zhu
 * @date: 2018/8/20 11:17
 * mybatis-plus逆向工程示例代码
 */
public class CreateDomain {
    //    static String classPath = "D:\\javaProduct\\books\\src\\main\\java";
    //本地项目地址，记得改
    static String classPath = "D:\\workSpace\\hqzhuanche\\ecmp-microservice\\ms-core\\src\\main\\java";
    static String bassPath = "com.hq.ecmp.mscore";
    static String entityPath = "domain";
    static String cantrollerPath = "controller";
    static String jdbcUrl = "jdbc:mysql://10.117.1.221:3306/ecmp";
    static String name = "root";
    static String pwd = "hqzc@2019";

    @Test
    public void testGenerator() {
        //1、全局配置
        GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(true)//开启AR模式
                .setAuthor("crk")//设置作者
                //生成路径(一般都是生成在此项目的src/main/java下面)
                .setOutputDir(classPath)
                .setFileOverride(true)//第二次生成会把第一次生成的覆盖掉
                .setIdType(IdType.AUTO)//主键策略
//                .setServiceName("%sService")//生成的service接口名字首字母是否为I，这样设置就没有I
                .setBaseResultMap(true)//生成resultMap
                .setBaseColumnList(true);//在xml中生成基础列
        //2、数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)//数据库类型
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUrl(jdbcUrl)
                .setUsername(name)
                .setPassword(pwd);
        //3、策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true)//开启全局大写命名
                .setEntitySerialVersionUID(true)
//                      .setDbColumnUnderline(true)//表名字段名使用下划线
                .setNaming(NamingStrategy.underline_to_camel)//下划线到驼峰的命名方式
                .setTablePrefix("")//表名前缀
                .setEntityLombokModel(true)//使用lombok
                .setEntityColumnConstant(true)//为字段生成常量字段
                .setEntityBuilderModel(true)//是否为构建者模型（默认 false）
                .setRestControllerStyle(true)
                .setControllerMappingHyphenStyle(true)
                .setLogicDeleteFieldName("status")
                .setSuperEntityClass("com.hq.ecmp.mscore.domain.base.BaseEntity")
                .setSuperEntityColumns("create_by","create_time","update_by","update_time","remark","data_scope")
                .setInclude("apply_approve_result_info","apply_info","approve_template_info","approve_template_node_info","car_contract_info","car_group_dispatcher_info","car_group_driver_relation","car_group_info","car_heartbeat_info","car_illegal_info","car_info","car_insurance_info","car_location_info","car_maintenance_info","car_refuel_info","car_yearly_check_info","driver_car_relation_info","driver_heartbeat_info","driver_info","driver_service_state_info","driver_work_info","ecmp_city_traffic_node_info","ecmp_config","ecmp_dict_data","ecmp_dict_type","ecmp_enterprise_info","ecmp_job","ecmp_job_log","ecmp_logininfor","ecmp_menu","ecmp_notice","ecmp_oper_log","ecmp_org","ecmp_post","ecmp_role","ecmp_role_dept","ecmp_role_menu","ecmp_user","ecmp_user_feedback_image","ecmp_user_feedback_info","ecmp_user_post","ecmp_user_role","enterprise_car_type_info","gen_table","gen_table_column","invoice_address","invoice_info","journey_info","journey_node_info","journey_passenger_info","journey_plan_price_info","journey_user_car_power","order_account_info","order_car_trace_info","order_info","order_invoice_info","order_service_appraise_info","order_settling_info","order_state_trace_info","project_info","project_user_relation_info","regime_info","regime_template_info","scene_info","scene_regime_relation","user_app_info","user_call_police_info","user_regime_relation_info");//逆向工程使用的表
//                .setInclude("apply_info");//逆向工程使用的表

        ;
        //4、包名策略配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(bassPath)//设置包名的parent
                .setMapper("mapper")
                .setService("service")
                .setController(cantrollerPath)
                .setEntity(entityPath)
                .setXml("mapper.xml");//设置xml文件的目录
        //5、整合配置
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig);
        //6、执行
        autoGenerator.execute();
        modifyEntity();
//        modifyController();
    }


//    @Test
    public void modifyEntity() {
        //entity的位置
        String entity_path = classPath + "\\" + bassPath.replace(".", "\\") + "\\" + entityPath.replace(".", "\\");
        File files = new File(entity_path);
        for (File file : files.listFiles()) {
            BufferedReader bf = null;
            String str;
            StringBuilder sb = new StringBuilder();
            boolean flag = true;
            try {
                bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8"));
                while ((str = bf.readLine()) != null) {
                    if ("import lombok.Data;".equals(str)) {
                        sb.append(str).append(System.getProperty("line.separator"));
                        sb.append("import lombok.Builder;").append(System.getProperty("line.separator"));
                        sb.append("import lombok.NoArgsConstructor;").append(System.getProperty("line.separator"));
                        sb.append("import lombok.AllArgsConstructor;").append(System.getProperty("line.separator"));
                        sb.append("import com.fasterxml.jackson.annotation.JsonFormat;").append(System.getProperty("line.separator"));
                        sb.append("import com.fasterxml.jackson.databind.annotation.JsonSerialize;").append(System.getProperty("line.separator"));
                        sb.append("import org.springframework.format.annotation.DateTimeFormat;").append(System.getProperty("line.separator"));
                        sb.append("import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;").append(System.getProperty("line.separator"));
                    } else if ("@Data".equals(str)) {
                        sb.append(str).append(System.getProperty("line.separator"));
                        sb.append("@Builder").append(System.getProperty("line.separator"));
                        sb.append("@NoArgsConstructor").append(System.getProperty("line.separator"));
                        sb.append("@AllArgsConstructor").append(System.getProperty("line.separator"));
                    } else if (str.startsWith("package")) {
                        sb.append(str).append(System.getProperty("line.separator"));
                        sb.append("/**update2**/").append(System.getProperty("line.separator"));
                    } else if ("/**update2**/".equals(str)) {
                        flag = false;
                        break;
                    } else if (str.contains("private LocalDateTime")) {
                        sb.append("    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern=\"yyyy-MM-dd HH:mm:ss\")\n" +
                                "    @DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")\n" +
                                "    @JsonSerialize(using = LocalDateTimeSerializer.class)").append(System.getProperty("line.separator"));
                        sb.append(str).append(System.getProperty("line.separator"));
                    } else {
                        sb.append(str).append(System.getProperty("line.separator"));

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bf != null) {
                    try {
                        bf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            if (flag) {
                file.delete();
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf8"));
                    bw.write(sb.toString());
                    System.out.println("复制完毕");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bw != null) {
                        try {
                            bw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }


        }
    }
}

