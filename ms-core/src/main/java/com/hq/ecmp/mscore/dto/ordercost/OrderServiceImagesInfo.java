package com.hq.ecmp.mscore.dto.ordercost;

import lombok.Data;

import java.util.Date;

@Data
public class OrderServiceImagesInfo {

    Long imageId;

    Long recordId;

    String imageurl;

    Long createBy;

    Date createTime;

    Long updateBy;

    Date updateTime;
}
