package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CloudWorkDateInfo;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author hqer
 * @date 2020-05-07
 */
public interface CloudWorkDateInfoMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CloudWorkDateInfo selectCloudWorkDateInfoById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param cloudWorkDateInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CloudWorkDateInfo> selectCloudWorkDateInfoList(CloudWorkDateInfo cloudWorkDateInfo);

    /**
     * 新增【请填写功能名称】
     * 
     * @param cloudWorkDateInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCloudWorkDateInfo(CloudWorkDateInfo cloudWorkDateInfo);

    /**
     * 修改【请填写功能名称】
     * 
     * @param cloudWorkDateInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCloudWorkDateInfo(CloudWorkDateInfo cloudWorkDateInfo);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCloudWorkDateInfoById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCloudWorkDateInfoByIds(Long[] ids);
}