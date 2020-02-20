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
@TableName("invoice_address")
public class InvoiceAddress extends MicBaseEntity<InvoiceAddress> {

    private static final long serialVersionUID=1L;

    @TableId(value = "address_id", type = IdType.AUTO)
    private Long addressId;

    private String accepter;

    private String mobile;

    private String address;

    private String itIsDefault;


    public static final String ADDRESS_ID = "address_id";

    public static final String ACCEPTER = "accepter";

    public static final String MOBILE = "mobile";

    public static final String ADDRESS = "address";

    public static final String IT_IS_DEFAULT = "it_is_default";

    @Override
    protected Serializable pkVal() {
        return this.addressId;
    }

}
