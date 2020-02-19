package com.hq.ecmp.mscore.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    protected final static Logger logger = LoggerFactory.getLogger(MetaObjectHandler.class);

    /**
     * 新增时填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
//        System.out.println("insertFill......");
//        // createTime 对应实体属性名, 第二个参数需要填充的值
//        // 先判断是否存在该字段
//        boolean createTime = metaObject.hasSetter("createTime");
//        if (createTime) {
//            System.out.println("insertFill......");
//            setInsertFieldValByName("createTime", LocalDateTime.now(), metaObject);
//        }
    }

    /**
     * 修改时填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
//        System.out.println("updateFill......");
        // updateTime 对应实体属性名, 第二个参数需要填充的值
//        setUpdateFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

}

