package com.hq.ecmp.mscore.dto.config;

import com.hq.ecmp.mscore.dto.FolwInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 申请单VO
 * @author xueyong
 * @date 2020/1/3
 * ecmp-proxy.
 */
@Data
public class ApproveTemplateIDTO {

    @NotNull
    private Long approveTemplateId;

}