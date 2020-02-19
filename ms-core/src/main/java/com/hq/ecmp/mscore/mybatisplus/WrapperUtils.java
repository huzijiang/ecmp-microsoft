package com.hq.ecmp.mscore.mybatisplus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hq.common.exception.BaseException;
import com.hq.ecmp.mscommon.utils.JsonUtils;

import java.util.Map;

public class WrapperUtils {
    public static QueryWrapper getQueryWrapper(Object o) {
        QueryWrapper wrapper = new QueryWrapper();
        String jsonStr;
        try {
            jsonStr = JsonUtils.toUnderlineJSONString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BaseException("对象转换失败");
        }
        JSONObject json = JSON.parseObject(jsonStr);
        for (Map.Entry<String, Object> j : json.entrySet()) {
            String name = j.getKey();
            Object value = j.getValue();
            wrapper.eq(name, value);
        }
        return wrapper;
    }
}
