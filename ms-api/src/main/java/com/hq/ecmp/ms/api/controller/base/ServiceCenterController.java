package com.hq.ecmp.ms.api.controller.base;

import com.hq.common.core.api.ApiResponse;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务中心
 * @date: 2020/3/6 13:02
 * @author:guojin
 */

@RestController
@RequestMapping("/ServiceCenter")
public class ServiceCenterController {

    /**
     * 约车问题获取接口
     * @param
     * @return
     */
    @ApiOperation(value = "CallProblemObtain",notes = "约车问题获取接口",httpMethod ="POST")
    @PostMapping("/CallProblemObtain")
    public ApiResponse<List<String>> CallProblemObtain(){
        List<String> list = new ArrayList<>();
        list.add("怎样才能快速约到车?");
        list.add("约到车之后应该怎么做?");
        list.add("约车应该注意哪些问题");
        if (CollectionUtils.isNotEmpty(list)){
            return ApiResponse.success(list);
        }else {
            return ApiResponse.error("资费问题获取失败");
        }
    }

    /**
     * 资费问题获取接口
     * @param
     * @return
     */
    @ApiOperation(value = "TariffProblemObtain",notes = "资费问题获取接口",httpMethod ="POST")
    @PostMapping("/TariffProblemObtain")
    public ApiResponse<List<String>> TariffProblemObtain(){
        List<String> list = new ArrayList<>();
        list.add("资费怎么算?");
        list.add("取消订单我是否有损失?");
        list.add("");
        if (CollectionUtils.isNotEmpty(list)){
            return ApiResponse.success(list);
        }else {
            return ApiResponse.error("资费问题获取失败");
        }
    }

    /**
     * 账户问题获取接口
     * @param
     * @return
     */
    @ApiOperation(value = "AccountProblemObtain",notes = "账户问题获取接口",httpMethod ="POST")
    @PostMapping("/AccountProblemObtain")
    public ApiResponse<List<String>> AccountProblemObtain(){
        List<String> list = new ArrayList<>();
        list.add("账户支付需要怎么操作?");
        list.add("账户是否安全合法?");
        list.add("账户是否可用?");
        if (CollectionUtils.isNotEmpty(list)){
            return ApiResponse.success(list);
        }else {
            return ApiResponse.error("账户问题获取失败");
        }
    }

    /**
     * 问题单个条目详情获取接口
     * @param
     * @return
     */
    @ApiOperation(value = "AccountProblemObtain",notes = "账户问题获取接口",httpMethod ="POST")
    @PostMapping("/QuestionSingleEntryDetails")
    public ApiResponse<String> QuestionSingleEntryDetails(){
        String str = "约车问题我需要怎么做,第一步查看手机上是否安装旗妙出行，如有安装，点击软件打开首页面，" +
                "第二步输入手机号，点击发送验证码,获取验证码进行输入，登录自己的账号";
        return ApiResponse.success(str);
    }
}
