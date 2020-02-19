package com.hq.ecmp.mscore.domain;
/**update2**/

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hq.ecmp.mscore.domain.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * 部门表
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
@TableName("ecmp_org")
public class EcmpOrg extends BaseEntity<EcmpOrg> {

    private static final long serialVersionUID=1L;

    /**
     * 组织id
     */
    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long deptId;

    /**
     * 上级组织id
     */
    private Long parentId;

    /**
     * 公司ID
     */
    private Long companyId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 组织名称
     */
    private String deptName;

    /**
     * 组织类别（1 公司 2 部门 3 车队）
     */
    private String deptType;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门状态（0正常 1停用）
     */
    @TableLogic
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;


    public static final String DEPT_ID = "dept_id";

    public static final String PARENT_ID = "parent_id";

    public static final String COMPANY_ID = "company_id";

    public static final String ANCESTORS = "ancestors";

    public static final String DEPT_NAME = "dept_name";

    public static final String DEPT_TYPE = "dept_type";

    public static final String ORDER_NUM = "order_num";

    public static final String LEADER = "leader";

    public static final String PHONE = "phone";

    public static final String EMAIL = "email";

    public static final String STATUS = "status";

    public static final String DEL_FLAG = "del_flag";

    @Override
    protected Serializable pkVal() {
        return this.deptId;
    }

}
