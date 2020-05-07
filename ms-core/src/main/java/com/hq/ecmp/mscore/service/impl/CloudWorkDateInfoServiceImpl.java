package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.CloudWorkDateInfo;
import com.hq.ecmp.mscore.mapper.CloudWorkDateInfoMapper;
import com.hq.ecmp.mscore.service.ICloudWorkDateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author hqer
 * @date 2020-05-07
 */
@Service
public class CloudWorkDateInfoServiceImpl implements ICloudWorkDateInfoService
{
    @Resource
    private CloudWorkDateInfoMapper cloudWorkDateInfoMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public CloudWorkDateInfo selectCloudWorkDateInfoById(Long id)
    {
        return cloudWorkDateInfoMapper.selectCloudWorkDateInfoById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param cloudWorkDateInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CloudWorkDateInfo> selectCloudWorkDateInfoList(CloudWorkDateInfo cloudWorkDateInfo)
    {
        return cloudWorkDateInfoMapper.selectCloudWorkDateInfoList(cloudWorkDateInfo);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param cloudWorkDateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCloudWorkDateInfo(CloudWorkDateInfo cloudWorkDateInfo)
    {
        cloudWorkDateInfo.setCreateTime(DateUtils.getNowDate());
        return cloudWorkDateInfoMapper.insertCloudWorkDateInfo(cloudWorkDateInfo);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param cloudWorkDateInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCloudWorkDateInfo(CloudWorkDateInfo cloudWorkDateInfo)
    {
        cloudWorkDateInfo.setUpdateTime(DateUtils.getNowDate());
        return cloudWorkDateInfoMapper.updateCloudWorkDateInfo(cloudWorkDateInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCloudWorkDateInfoByIds(Long[] ids)
    {
        return cloudWorkDateInfoMapper.deleteCloudWorkDateInfoByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param id 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteCloudWorkDateInfoById(Long id)
    {
        return cloudWorkDateInfoMapper.deleteCloudWorkDateInfoById(id);
    }
}