package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.vo.DisWorkBenchInfo;

/**
 * <p>调度工作台服务层</p>
 *
 * @author yj
 * @version 1.0
 * @date 2020/6/9 15:15
 */
public interface DispatchWorkBenchService {

    /**
     * 获取调度工作台列表信息
     * @param pageNum
     * @param pageSize
     * @param state
     */
    DisWorkBenchInfo getDispatchInfoListWorkBench(String state,Integer pageNum,Integer pageSize);
}
