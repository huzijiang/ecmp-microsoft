package com.hq.ecmp.mscore.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (EcmpMessage)实体类
 *
 * @author makejava
 * @since 2020-03-13 15:25:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EcmpMessageVO implements Serializable {
    private static final long serialVersionUID = 480753607230255189L;
    
    private Long id;

    /**
    * 消息状态  分已读 和 未读
        0000-已读
        1111-未读
    */
    private String status;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    private String url;

    private String time;

}