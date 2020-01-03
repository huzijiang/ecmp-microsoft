package com.hq.ecmp.mscore.service.impl;

import java.util.List;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.mscore.domain.EcmpConfig;
import com.hq.ecmp.mscore.mapper.EcmpConfigMapper;
import com.hq.ecmp.mscore.service.IEcmpConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 参数配置Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EcmpConfigServiceImpl implements IEcmpConfigService
{
    @Autowired
    private EcmpConfigMapper ecmpConfigMapper;

    /**
     * 查询参数配置
     *
     * @param configId 参数配置ID
     * @return 参数配置
     */
    @Override
    public EcmpConfig selectEcmpConfigById(Integer configId)
    {
        return ecmpConfigMapper.selectEcmpConfigById(configId);
    }

    /**
     * 查询参数配置列表
     *
     * @param ecmpConfig 参数配置
     * @return 参数配置
     */
    @Override
    public List<EcmpConfig> selectEcmpConfigList(EcmpConfig ecmpConfig)
    {
        return ecmpConfigMapper.selectEcmpConfigList(ecmpConfig);
    }

    /**
     * 新增参数配置
     *
     * @param ecmpConfig 参数配置
     * @return 结果
     */
    @Override
    public int insertEcmpConfig(EcmpConfig ecmpConfig)
    {
        ecmpConfig.setCreateTime(DateUtils.getNowDate());
        return ecmpConfigMapper.insertEcmpConfig(ecmpConfig);
    }

    /**
     * 修改参数配置
     *
     * @param ecmpConfig 参数配置
     * @return 结果
     */
    @Override
    public int updateEcmpConfig(EcmpConfig ecmpConfig)
    {
        ecmpConfig.setUpdateTime(DateUtils.getNowDate());
        return ecmpConfigMapper.updateEcmpConfig(ecmpConfig);
    }

    /**
     * 批量删除参数配置
     *
     * @param configIds 需要删除的参数配置ID
     * @return 结果
     */
    @Override
    public int deleteEcmpConfigByIds(Integer[] configIds)
    {
        return ecmpConfigMapper.deleteEcmpConfigByIds(configIds);
    }

    /**
     * 删除参数配置信息
     *
     * @param configId 参数配置ID
     * @return 结果
     */
    @Override
    public int deleteEcmpConfigById(Integer configId)
    {
        return ecmpConfigMapper.deleteEcmpConfigById(configId);
    }
}
