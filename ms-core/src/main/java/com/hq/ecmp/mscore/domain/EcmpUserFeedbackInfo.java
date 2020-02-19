package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hq.ecmp.mscore.domain.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author crk
 * @since 2020-02-20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ecmp_user_feedback_info")
public class EcmpUserFeedbackInfo extends BaseEntity<EcmpUserFeedbackInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "feedback_id", type = IdType.AUTO)
    private Long feedbackId;

    private Long userId;

    private Long orderId;

    private String type;

    private String content;


    public static final String FEEDBACK_ID = "feedback_id";

    public static final String USER_ID = "user_id";

    public static final String ORDER_ID = "order_id";

    public static final String TYPE = "type";

    public static final String CONTENT = "content";

    @Override
    protected Serializable pkVal() {
        return this.feedbackId;
    }

}
