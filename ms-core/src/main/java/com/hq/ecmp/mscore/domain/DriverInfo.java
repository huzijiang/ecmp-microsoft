package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hq.ecmp.mscore.domain.base.BaseEntity;
import java.time.LocalDateTime;
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
@TableName("driver_info")
public class DriverInfo extends BaseEntity<DriverInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "driver_id", type = IdType.AUTO)
    private Long driverId;

    private Long userId;

    private String driverName;

    private String country;

    private String nation;

    private String idCard;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime birthday;

    private String gender;

    private String mobile;

    private String address;

    private String emergencyContactA;

    private String emergencyContactB;

    private String emergencyContactC;

    private String itIsFullTime;

    private String licenseType;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime licenseIssueDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime licenseInitIssueDate;

    private String licenseNumber;

    private String licenseArchivesNumber;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime licenseExpireDate;


    public static final String DRIVER_ID = "driver_id";

    public static final String USER_ID = "user_id";

    public static final String DRIVER_NAME = "driver_name";

    public static final String COUNTRY = "country";

    public static final String NATION = "nation";

    public static final String ID_CARD = "id_card";

    public static final String BIRTHDAY = "birthday";

    public static final String GENDER = "gender";

    public static final String MOBILE = "mobile";

    public static final String ADDRESS = "address";

    public static final String EMERGENCY_CONTACT_A = "emergency_contact_a";

    public static final String EMERGENCY_CONTACT_B = "emergency_contact_b";

    public static final String EMERGENCY_CONTACT_C = "emergency_contact_c";

    public static final String IT_IS_FULL_TIME = "it_is_full_time";

    public static final String LICENSE_TYPE = "license_type";

    public static final String LICENSE_ISSUE_DATE = "license_issue_date";

    public static final String LICENSE_INIT_ISSUE_DATE = "license_init_issue_date";

    public static final String LICENSE_NUMBER = "license_number";

    public static final String LICENSE_ARCHIVES_NUMBER = "license_archives_number";

    public static final String LICENSE_EXPIRE_DATE = "license_expire_date";

    @Override
    protected Serializable pkVal() {
        return this.driverId;
    }

}
