package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.hq.ecmp.mscore.domain.base.MicBaseEntity;

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
@TableName("order_invoice_info")
public class OrderInvoiceInfo extends MicBaseEntity<OrderInvoiceInfo> {

    private static final long serialVersionUID=1L;

    private Long invoiceId;

    private Long accountId;


    public static final String INVOICE_ID = "invoice_id";

    public static final String ACCOUNT_ID = "account_id";

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
