package com.hq.ecmp.mscore.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderServiceImagesInfo {

    Long imageId;

    Long recordId;

    String imageurl;

    Long createBy;

    Date createTime;

    Long updateBy;

    Date updateTime;

}
