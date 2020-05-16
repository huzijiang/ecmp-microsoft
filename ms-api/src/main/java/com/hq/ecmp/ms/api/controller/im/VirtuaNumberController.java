package com.hq.ecmp.ms.api.controller.im;

import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.mscore.domain.EcmpConfig;
import com.hq.ecmp.mscore.domain.ImVirtuaPhone;
import com.hq.ecmp.mscore.service.IEcmpConfigService;
import com.hq.ecmp.mscore.service.ImVirtuaPhoneService;
import com.hq.ecmp.util.MyOkHttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author crk
 */
@RestController
@RequestMapping("/virtual")
@Api(description = "虚拟小号")
public class VirtuaNumberController {

    @Autowired
    private ImVirtuaPhoneService virtuaPhoneService;
    @Autowired
    private IEcmpConfigService configService;
    @Autowired
    private MyOkHttpUtil okHttpUtil;

    /**
     * 绑定虚拟小号
     *
     * @param
     * @return
     */
    @ApiOperation(value = "绑定虚拟小号", notes = "绑定虚拟小号", httpMethod = "POST")
    @PostMapping("/bind")
    public ApiResponse bind(String customPhone, String driverPhone, String cityCode, String companyId) throws Exception {
        if (!checkCompanyVirtace(companyId)) {
            return ApiResponse.error("该企业暂不支持虚拟小号");
        }
        Map<String, Object> param = new HashMap<>(8);
        //乘客手机号
        param.put("customPhone", customPhone);
        //司机手机号
        param.put("driverPhone", driverPhone);
        //城市信息
        param.put("city", cityCode);
        String result = okHttpUtil.thirdInterface("/service/v2/virtual/bind", param);

        JSONObject data = JSONObject.parseObject(result).getJSONObject("data");
        ImVirtuaPhone imVirtuaPhone = ImVirtuaPhone.builder()
                .customVirtuaPhone(data.getString("hideCustomPhone"))
                .customPhone(customPhone)
                .driverVirtuaPhone(data.getString("hideDriverPhone"))
                .driverPhone(driverPhone)
                .virtuaBindId(data.getString("bindId"))
                .citycode(cityCode)
                .createTime(new Date())
                .build();
        imVirtuaPhone = virtuaPhoneService.insert(imVirtuaPhone);
        return ApiResponse.success(ImVirtuaPhone.builder().driverVirtuaPhone(imVirtuaPhone.getDriverVirtuaPhone()).customVirtuaPhone(imVirtuaPhone.getCustomVirtuaPhone()).build());
    }

    /**
     * 校验企业配置是否开启公司小号
     *
     * @param companyId
     * @return
     */
    private boolean checkCompanyVirtace(String companyId) {
        EcmpConfig ecmpConfig = EcmpConfig.builder().companyId(companyId).configKey("virtualPhoneInfo").build();
        List<EcmpConfig> list = configService.selectEcmpConfigList(ecmpConfig);
        boolean flag = false;
        if (list.size() > 0) {
            ecmpConfig = list.get(0);
            JSONObject configValueJson = JSONObject.parseObject(ecmpConfig.getConfigValue());
            String status = configValueJson.getString("status");
            if (CommonConstant.YES.equals(status)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 解绑虚拟小号
     *
     * @param
     * @return
     */
    @ApiOperation(value = "解绑虚拟小号", notes = "解绑虚拟小号", httpMethod = "POST")
    @PostMapping("/unbind")
    public ApiResponse unbind(String customPhone, String driverPhone) throws Exception {
        ImVirtuaPhone imVirtuaPhone = ImVirtuaPhone.builder()
                .customPhone(customPhone)
                .driverPhone(driverPhone)
                .build();
        imVirtuaPhone = virtuaPhoneService.queryByPhones(imVirtuaPhone);
        Map<String, Object> param = new HashMap<>(4);
        //绑定ID
        param.put("bindId", imVirtuaPhone.getVirtuaBindId());
        //虚拟号
        param.put("hidePhone", imVirtuaPhone.getCustomVirtuaPhone());
        String result = okHttpUtil.thirdInterface("/service/v2/virtual/unbind", param);
        return ApiResponse.success(JSONObject.parseObject(result).getString("msg"));
    }


}
