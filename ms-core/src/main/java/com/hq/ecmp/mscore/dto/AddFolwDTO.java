package com.hq.ecmp.mscore.dto;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddFolwDTO {

    /**
     * 申请ID
     */
    @NotNull
    private String name;
    private Long approveTemplateId;
    private List<FolwInfoDTO> flowList;

}