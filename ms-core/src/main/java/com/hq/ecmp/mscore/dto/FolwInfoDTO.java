package com.hq.ecmp.mscore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
public class FolwInfoDTO {

   private int number;
   private String type;
   private String roleIds;
   private String userIds;
   private Long nodeId;

   public FolwInfoDTO(int number, String type, String roleIds, String userIds) {
      this.number = number;
      this.type = type;
      this.roleIds = roleIds;
      this.userIds = userIds;
   }
}