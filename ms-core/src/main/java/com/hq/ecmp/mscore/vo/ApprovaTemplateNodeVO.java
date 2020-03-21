package com.hq.ecmp.mscore.vo;

import lombok.Data;

/**
 *
 * 审批模板对象
 * @date: 2020/2/28 16:40
 * @author:guojin
 */

@Data
public class ApprovaTemplateNodeVO {


    private Long approveNodeId;
    private String name;
    private String type;
    private String userId;
    private String roleId;
    private String nextNodeId;
    private int number;
}
