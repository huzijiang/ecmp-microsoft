package com.hq.ecmp.mscore.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class OrderServiceImagesInfo {

    Long imageId;

    Long recordId;

    String imageurl;

    Long createBy;

    Date createTime;

    Long updateBy;

    Date updateTime;
}
