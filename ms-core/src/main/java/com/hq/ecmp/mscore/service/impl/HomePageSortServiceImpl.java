package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.UserConsoleHomePageSortInfo;
import com.hq.ecmp.mscore.mapper.HomePageSortMapper;
import com.hq.ecmp.mscore.service.IHomePageSortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomePageSortServiceImpl implements IHomePageSortService {

     @Autowired
     private HomePageSortMapper homePageSortMapper;


    /**
     * 获取后台首页排序
     * @return
     */
    @Override
    public List<UserConsoleHomePageSortInfo> getHomeSort() {
        List<UserConsoleHomePageSortInfo> homePageSort= homePageSortMapper.getHomeSort();
        return homePageSort;
    }

    /**
     * 修改后台首页顺序
     * @param userConsoleHomePageSortInfo
     */
    @Override
    public void updateHomeSort(List<UserConsoleHomePageSortInfo> userConsoleHomePageSortInfo) {
        for (UserConsoleHomePageSortInfo homePageSort:userConsoleHomePageSortInfo){
            UserConsoleHomePageSortInfo  home = new UserConsoleHomePageSortInfo();
            home.setSortNum(homePageSort.getSortNum());
            home.setPanelId(homePageSort.getPanelId());
            homePageSortMapper.updateHomeSort(home);
        }
    }
}
