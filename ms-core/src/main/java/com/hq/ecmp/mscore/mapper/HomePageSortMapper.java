package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.UserConsoleHomePageSortInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomePageSortMapper {

    /**
     * 获取后台首页排序
     * @return
     */
    List<UserConsoleHomePageSortInfo> getHomeSort();

    /**
     * 修改后台首页顺序
     * @param userConsoleHomePageSortInfo
     */
    void updateHomeSort(UserConsoleHomePageSortInfo userConsoleHomePageSortInfo);
}
