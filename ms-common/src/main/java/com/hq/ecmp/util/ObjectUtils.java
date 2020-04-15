package com.hq.ecmp.util;

import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;
import java.util.Set;

/**
 * Created by yangnan on 17/10/23.
 *
 * @author yangnan
 *         对象操作类
 */
public class ObjectUtils {

    /**
     * 对象转成map
     *
     * @param obj
     * @return 如果obj==null 则返回null
     */
    public static Map<String, Object> objectToMap(Object obj) {

        if (obj == null) {
            return null;
        }

        BeanMap beanMap = BeanMap.create(obj);
        if (beanMap == null) {
            return null;
        }

        Map<String, Object> map = Maps.newHashMap();
        Set keys = beanMap.keySet();
        for (Object key : keys) {
            if (key instanceof String) {
                map.put((String) key, beanMap.get(key));
            }
        }
        return map;
    }

    /**
     *
     * @param obj
     * @return
     */
    public static Map<String, String> objectToMap2(Object obj) {
        if (obj == null) {
            return null;
        }
        BeanMap beanMap = BeanMap.create(obj);
        if (beanMap == null) {
            return null;
        }
        Map<String, String> map = Maps.newHashMap();
        Set keys = beanMap.keySet();
        for (Object key : keys) {
            map.put((String) key, (String) beanMap.get(key));
        }
        return map;
    }

    /**
     * 对象转成map
     * null值不放入map
     * @param obj
     * @return 如果obj==null 则返回null
     */
    public static Map<String, Object> objectToMapIgnoreNull(Object obj) {

        if (obj == null) {
            return null;
        }

        BeanMap beanMap = BeanMap.create(obj);
        if (beanMap == null) {
            return null;
        }

        Map<String, Object> map = Maps.newHashMap();
        Set keys = beanMap.keySet();
        for (Object key : keys) {
            if (key instanceof String && beanMap.get(key) != null) {
                map.put((String) key, beanMap.get(key));
            }
        }
        return map;
    }
}
