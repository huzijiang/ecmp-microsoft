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
@TableName("ecmp_user_feedback_image")
public class EcmpUserFeedbackImage extends MicBaseEntity<EcmpUserFeedbackImage> {

    private static final long serialVersionUID=1L;

    @TableId(value = "image_id", type = IdType.AUTO)
    private Long imageId;

    private Long feedbackId;

    private Integer userId;

    private String imageUrl;


    public static final String IMAGE_ID = "image_id";

    public static final String FEEDBACK_ID = "feedback_id";

    public static final String USER_ID = "user_id";

    public static final String IMAGE_URL = "image_url";

    @Override
    protected Serializable pkVal() {
        return this.imageId;
    }

}
