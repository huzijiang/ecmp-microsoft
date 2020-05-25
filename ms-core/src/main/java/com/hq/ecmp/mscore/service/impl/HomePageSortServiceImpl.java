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

    /**
     * 修改后台首页顺序
     * @param userConsoleHomePageSortInfo
     */
    @Override
    public void updateHomeSorts(List<UserConsoleHomePageSortInfo> userConsoleHomePageSortInfo) {
        for (UserConsoleHomePageSortInfo homePageSort:userConsoleHomePageSortInfo){
            List<UserConsoleHomePageSortInfo> homeSorts = homePageSortMapper.getHomeSortsById(homePageSort.getUserId(), homePageSort.getCompanyId());
            //判断非空，若为空，表示数据库没有该用户的首页排版，需要添加，反之修改
            if(homeSorts.size() != 0){
                UserConsoleHomePageSortInfo  home = new UserConsoleHomePageSortInfo();
                home.setSortNum(homePageSort.getSortNum());
                home.setPanelId(homePageSort.getPanelId());
                home.setUserId(homePageSort.getUserId());
                home.setCompanyId(homePageSort.getCompanyId());
                homePageSortMapper.updateHomeSorts(home);
            }else{
                UserConsoleHomePageSortInfo  home = new UserConsoleHomePageSortInfo();
                home.setSortNum(homePageSort.getSortNum());
                home.setPanelId(homePageSort.getPanelId());
                home.setUserId(homePageSort.getUserId());
                home.setDeptId(homePageSort.getDeptId());
                home.setPanelName(homePageSort.getPanelName());
                home.setCompanyId(homePageSort.getCompanyId());
                homePageSortMapper.addHomeSort(home);
            }
        }
    }

}
