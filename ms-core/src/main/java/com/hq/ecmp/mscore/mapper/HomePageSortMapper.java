package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpUserRole;
import com.hq.ecmp.mscore.domain.UserConsoleHomePageSortInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface HomePageSortMapper {

    /**
     * 获取后台首页排序
     * @return
     */
    List<UserConsoleHomePageSortInfo> getPanelByRoleId(String roleIds);

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

    void updateHomeSorts(UserConsoleHomePageSortInfo userConsoleHomePageSortInfo);

    void addHomeSort(UserConsoleHomePageSortInfo userConsoleHomePageSortInfo);

    List<UserConsoleHomePageSortInfo> getHomeSortsById(@Param("userId")Long userId, @Param("companyId")Long companyId,@Param("panelId")Long panelId );

    String getRoleIds(Long userId);

    List<UserConsoleHomePageSortInfo> getHomeSortsByPanelId(@Param("panelId")Long panelId);

    List<UserConsoleHomePageSortInfo> getHomePageSort(@Param("userId")Long userId);

    int deleteHomeSorts(UserConsoleHomePageSortInfo userConsoleHomePageSortInfo);
}
