package com.hq.ecmp.mscore.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/5 11:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplyInfoDetailVO {

    private UserApplySingleVo applyInfoDetail;
    private List<ApprovalDispatcherVO> approveList;

}
