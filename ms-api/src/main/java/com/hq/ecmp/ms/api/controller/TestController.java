package com.hq.ecmp.ms.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zj.hu
 * @Date: 2019-12-31 14:10
 */
@Api(tags = "test api ")
@RestController
@RequestMapping("/test")
public class TestController {

    /**
     * 接口必须 明确申明  接口请求方式
     * @param name
     * @return
     */
    @ApiOperation(value = "getHello",notes = "该接口方法需要的注意事项....",httpMethod ="GET")
    @PostMapping("/getHello")
    public String getHello(String name){
        return "您好 ："+name;
    }

    /**
     * 接口必须 明确申明  接口请求方式
     * @param name
     * @return
     */
    @ApiOperation(value = "getSayGoodBye",notes = "该接口方法需要的注意事项---1---2....",httpMethod ="GET")
    @PostMapping("/getSayGoodBye")
    public String getSayGoodBye(String name){
        System.out.println("-------");
        return "您好 ："+name;
    }

}
