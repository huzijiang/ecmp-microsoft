package com.hq.ecmp.mscore.bo;

import lombok.Data;

/**
 * @Author: chao.zhang
 * @Date: 2020/5/13 17:05
 */
@Data
public class JourneyNodeBo {
    //返程节点id
    private Long nodeIdIsReturn;
    //没有往返求情况下， 取得第一个节点id （根据预估价格表需要，也可取最后一个节点id）
    private Long nodeIdNoReturn;
}
