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
@TableName("car_info")
public class CarInfo extends BaseEntity<CarInfo> {

    private static final long serialVersionUID=1L;

    @TableId(value = "car_id", type = IdType.AUTO)
    private Long carId;

    private Long carGroupId;

    private Long deptId;

    private Long carTypeId;

    private String carLicense;

    private String state;

    private String carType;

    private Integer seatNum;

    private String carColor;

    private String carLicenseColor;

    private String powerType;

    private String carType01;

    private String engineNumber;

    private String engineCc;

    private String enginePower;

    private String carNumber;

    private String carImgaeUrl;

    private String carDrivingLicenseImagesUrl;

    private String carLicenseImageUrl;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime buyDate;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime rentEndDate;

    private String ownerOrgId;


    public static final String CAR_ID = "car_id";

    public static final String CAR_GROUP_ID = "car_group_id";

    public static final String DEPT_ID = "dept_id";

    public static final String CAR_TYPE_ID = "car_type_id";

    public static final String CAR_LICENSE = "car_license";

    public static final String STATE = "state";

    public static final String CAR_TYPE = "car_type";

    public static final String SEAT_NUM = "seat_num";

    public static final String CAR_COLOR = "car_color";

    public static final String CAR_LICENSE_COLOR = "car_license_color";

    public static final String POWER_TYPE = "power_type";

    public static final String CAR_TYPE_01 = "car_type_01";

    public static final String ENGINE_NUMBER = "engine_number";

    public static final String ENGINE_CC = "engine_cc";

    public static final String ENGINE_POWER = "engine_power";

    public static final String CAR_NUMBER = "car_number";

    public static final String CAR_IMGAE_URL = "car_imgae_url";

    public static final String CAR_DRIVING_LICENSE_IMAGES_URL = "car_driving_license_images_url";

    public static final String CAR_LICENSE_IMAGE_URL = "car_license_image_url";

    public static final String BUY_DATE = "buy_date";

    public static final String RENT_END_DATE = "rent_end_date";

    public static final String OWNER_ORG_ID = "owner_org_id";

    @Override
    protected Serializable pkVal() {
        return this.carId;
    }

}
