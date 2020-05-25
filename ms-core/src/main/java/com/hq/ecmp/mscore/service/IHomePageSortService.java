package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.UserConsoleHomePageSortInfo;

import java.util.List;

public interface IHomePageSortService {
    /**
     * 获取后台首页顺序
     * @return
     */
    List<UserConsoleHomePageSortInfo> getHomeSort();


    /**
     * 修改后台首页顺序
     * @param userConsoleHomePageSortInfo
     */
    void updateHomeSort(List<UserConsoleHomePageSortInfo> userConsoleHomePageSortInfo);

    /**
     * 修改后台首页顺序
     * @param userConsoleHomePageSortInfo
     */
    void updateHomeSorts(List<UserConsoleHomePageSortInfo> userConsoleHomePageSortInfo);
}
