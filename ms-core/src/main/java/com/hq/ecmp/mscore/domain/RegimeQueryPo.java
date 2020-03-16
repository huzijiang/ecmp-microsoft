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
           Integer  status;// 0-启用    1-禁用
           String regimeName;//制度名称
}
