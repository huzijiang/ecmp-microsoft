package com.hq.ecmp.ms.api.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: zj.hu
 * @Date: 2020-01-04 15:32
 */
@Data
@JsonIgnoreProperties({" hibernateLazyInitializer","handler"})
public class OrderEvaluationDto {

    /**
     * 订单编号
     */
    @NotNull
    @ApiParam(required = true)
    private Long orderId;
    private String type;

    /**
     * 意见描述
     */
    private  String content;

    /**图片路径*/
//    private List<MultipartFile> files;
    private String imgUrls;
}
