package com.hq.ecmp.mscore.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *
 * 审批模板对象
 * @date: 2020/2/28 16:40
 * @author:guojin
 */

@Data
public class ApprovaTemplateVO {


    /**
     * 审批人Id
     */
    private Long approveTemplateId;
    private String name;
    private Integer isBingRegime;
    private List<ApprovaTemplateNodeVO> nodeIds;
    public ApprovaTemplateVO() {
    }

    public ApprovaTemplateVO(Long approveTemplateId, String name, List<ApprovaTemplateNodeVO> nodeIds) {
        this.approveTemplateId = approveTemplateId;
        this.name = name;
        this.nodeIds = nodeIds;
    }
}
