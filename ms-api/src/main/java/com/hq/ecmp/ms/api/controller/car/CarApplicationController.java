package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.ms.api.dto.car.ApprovalVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 根据用车制度Id,查询审批人列表
 * @date: 2020/3/2 16:26
 * @author:guojin
 */

@RestController
@RequestMapping(value = "/CarApplication")
public class CarApplicationController {

    @ApiOperation(value = "list", notes = "根据用车制度Id,查询审批人列表", httpMethod = "POST")
    @PostMapping("/getApprovalList")
    public ApiResponse<List<ApprovalVO>> getApprovalList() {
        List<ApprovalVO> list = new ArrayList<>();
        list.add(new ApprovalVO(1, "张三", "15100000000"));
        list.add(new ApprovalVO(2, "李四", "15200000000"));
        list.add(new ApprovalVO(3, "王五", "15300000000"));
        list.add(new ApprovalVO(4, "赵六", "15400000000"));
        list.add(new ApprovalVO(5, "陈七", "15500000000"));
        return ApiResponse.success(list);
    }
}
