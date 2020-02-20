package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.hq.ecmp.mscore.domain.base.MicBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
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
public class OrderAccountInfo extends MicBaseEntity<OrderAccountInfo> {

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
