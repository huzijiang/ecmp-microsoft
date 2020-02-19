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
@TableName("order_account_info")
public class OrderAccountInfo extends BaseEntity<OrderAccountInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "account_id", type = IdType.AUTO)
    private Long accountId;

    private Long billId;

    private String orderId;

    private Integer costCenter;

    private Integer projectId;

    private String state;


    public static final String ACCOUNT_ID = "account_id";

    public static final String BILL_ID = "bill_id";

    public static final String ORDER_ID = "order_id";

    public static final String COST_CENTER = "cost_center";

    public static final String PROJECT_ID = "project_id";

    public static final String STATE = "state";

    @Override
    protected Serializable pkVal() {
        return this.accountId;
    }

}
