package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDate;
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
@TableName("driver_work_info")
public class DriverWorkInfo extends BaseEntity<DriverWorkInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "work_id", type = IdType.AUTO)
    private Long workId;

    private Long driverId;

    private String leaveStatus;

    private String leaveConfirmStatus;

    private LocalDate caledarDate;


    public static final String WORK_ID = "work_id";

    public static final String DRIVER_ID = "driver_id";

    public static final String LEAVE_STATUS = "leave_status";

    public static final String LEAVE_CONFIRM_STATUS = "leave_confirm_status";

    public static final String CALEDAR_DATE = "caledar_date";

    @Override
    protected Serializable pkVal() {
        return this.workId;
    }

}
