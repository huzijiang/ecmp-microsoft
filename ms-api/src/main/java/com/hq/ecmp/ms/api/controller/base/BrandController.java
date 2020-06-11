package com.hq.ecmp.ms.api.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.MacTools;
import com.hq.common.utils.OkHttpUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shuxiao
 * @date 2020/6/10
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Value("${thirdService.enterpriseId}") // 企业编号
    private String enterpriseId;
    @Value("${thirdService.licenseContent}") // 企业证书信息
    private String licenseContent;
    @Value("${thirdService.apiUrl}") // 三方平台的接口前地址
    private String apiUrl;

    /**
     * 从云端获取品牌
     * @return
     */
    @ApiOperation(value = "getAllBrand", notes = "从云端获取品牌", httpMethod = "POST")
    @PostMapping("/getAllBrand")
    public ApiResponse<List<?>> getAllBrand(@RequestParam(value = "keywords") String keywords) {
        try {
            // MAC地址
            List<String> macList = MacTools.getMacList();
            String macAdd = macList.get(0);
            Map<String, Object> map = new HashMap<>();
            //物理地址
            map.put("mac", macAdd);
            //关键字查询
            map.put("keywords", keywords);
            //企业编号
            map.put("enterpriseId", enterpriseId);
            //企业证书信息
            map.put("licenseContent", licenseContent);
            //去云端的路径
            String resultQuery = OkHttpUtil.postForm(apiUrl + "/basic/carModels", map);
            JSONObject jsonObjectQuery = JSONObject.parseObject(resultQuery);
            if (!"0".equals(jsonObjectQuery.getString("code"))) {
                return ApiResponse.error("调用云端接口获取云端平台车辆品牌信息失败");
            }
            Object object = jsonObjectQuery.get("data");
            if (null == object) {
                return ApiResponse.success("调用云端接口获取云端平台车辆品牌信息为空");
            }
            List<?> list = JSONObject.parseArray(object.toString());
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("调用云端接口获取云端平台车辆品牌信息失败");
        }
    }
}
