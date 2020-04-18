package com.hq.ecmp.mscore.domain;

import com.hq.ecmp.mscore.dto.Page;

import lombok.Data;

/**
 * 制度列表查询类
 * @author cm
 *
 */
@Data
public class RegimeQueryPo extends Page {
           Long sceneId;//场景编号
           String  state;// Y000-启用中    E000-停用中
           String regimeName;//制度名称
}
