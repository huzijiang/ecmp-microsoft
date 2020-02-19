package com.hq.ecmp.mscore.domain;
/**update2**/

import java.math.BigDecimal;
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
@TableName("order_settling_info")
public class OrderSettlingInfo extends BaseEntity<OrderSettlingInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "bill_id", type = IdType.AUTO)
    private Long billId;

    private Long orderId;

    private BigDecimal amount;

    private String amountDetail;

    private BigDecimal outPrice;


    public static final String BILL_ID = "bill_id";

    public static final String ORDER_ID = "order_id";

    public static final String AMOUNT = "amount";

    public static final String AMOUNT_DETAIL = "amount_detail";

    public static final String OUT_PRICE = "out_price";

    @Override
    protected Serializable pkVal() {
        return this.billId;
    }

}
