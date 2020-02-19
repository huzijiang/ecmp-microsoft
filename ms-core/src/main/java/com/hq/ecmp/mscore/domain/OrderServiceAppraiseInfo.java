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
@TableName("order_service_appraise_info")
public class OrderServiceAppraiseInfo extends BaseEntity<OrderServiceAppraiseInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "appraise_id", type = IdType.AUTO)
    private Long appraiseId;

    private Long orderId;

    private Long journeyId;

    private String content;


    public static final String APPRAISE_ID = "appraise_id";

    public static final String ORDER_ID = "order_id";

    public static final String JOURNEY_ID = "journey_id";

    public static final String CONTENT = "content";

    @Override
    protected Serializable pkVal() {
        return this.appraiseId;
    }

}
