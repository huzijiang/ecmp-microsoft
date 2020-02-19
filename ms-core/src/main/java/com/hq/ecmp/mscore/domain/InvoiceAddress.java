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
@TableName("invoice_address")
public class InvoiceAddress extends BaseEntity<InvoiceAddress> {

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
