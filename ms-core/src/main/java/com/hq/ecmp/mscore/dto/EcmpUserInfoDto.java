package com.hq.ecmp.mscore.dto;

import com.hq.ecmp.mscore.domain.EcmpOrg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName 用户的末级部门信息和末级公司信息
 * @Description TODO
 * @Author yj
 * @Date 2020/5/8 11:46
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EcmpUserInfoDto {
    /**
     * 用户的末级公司信息
     */
    private EcmpOrg userCompanyInfo;

    /**
     * 用户的末级部门信息
     */
    private EcmpOrg userDeptInfo;

}
