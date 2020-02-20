package com.hq.ecmp.mscore.domain;
/**update2**/

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.hq.ecmp.mscore.domain.base.MicBaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@TableName("invoice_info")
public class InvoiceInfo extends MicBaseEntity<InvoiceInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "invoice_id", type = IdType.AUTO)
    private Long invoiceId;

    private BigDecimal amount;

    private String header;

    private String tin;

    private String bankName;

    private String bankCardNo;

    private String registedAddress;

    private String telephone;

    private String acceptAddress;

    private Integer applyer;

    private String content;

    private String type;

    @TableLogic
    private String status;


    public static final String INVOICE_ID = "invoice_id";

    public static final String AMOUNT = "amount";

    public static final String HEADER = "header";

    public static final String TIN = "tin";

    public static final String BANK_NAME = "bank_name";

    public static final String BANK_CARD_NO = "bank_card_no";

    public static final String REGISTED_ADDRESS = "registed_address";

    public static final String TELEPHONE = "telephone";

    public static final String ACCEPT_ADDRESS = "accept_address";

    public static final String APPLYER = "applyer";

    public static final String CONTENT = "content";

    public static final String TYPE = "type";

    public static final String STATUS = "status";

    @Override
    protected Serializable pkVal() {
        return this.invoiceId;
    }

}
