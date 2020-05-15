package com.hq.ecmp.util;

import com.hq.common.utils.OkHttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author crk
 */
@Component
public class MyOkHttpUtil {
    /**
     * 企业编号
     */
    @Value("${thirdService.enterpriseId}")
    private String enterpriseId;
    /**
     * 企业证书信息
     */
    @Value("${thirdService.licenseContent}")
    private String licenseContent;
    /**
     * 三方平台的接口前地址
     */
    @Value("${thirdService.apiUrl}")
    private String apiUrl;

    public String thirdInterface(String url, Map param) throws Exception {
        List<String> macList = MacTools.getMacList();
        String macAdd = macList.get(0);
        Map<String, Object> map = new HashMap<>();
        map.put("enterpriseId", enterpriseId);
        map.put("licenseContent", licenseContent);
        map.put("mac", macAdd);
        map.putAll(param);
        return OkHttpUtil.postForm(apiUrl + url, map);
    }
}
