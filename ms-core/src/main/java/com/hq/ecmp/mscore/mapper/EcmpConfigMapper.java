package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EcmpConfig;

import java.util.List;

/**
 * 参数配置Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface EcmpConfigMapper
{
    /**
     * 查询参数配置
     *
     * @param configId 参数配置ID
     * @return 参数配置
     */
    public EcmpConfig selectEcmpConfigById(Integer configId);

    /**
     * 根据KEY查询相关配置值
     * @param config
     * @return
     */
    public EcmpConfig selectConfigByKey(EcmpConfig config);

    /**
     * 查询参数配置列表
     *
     * @param ecmpConfig 参数配置
     * @return 参数配置集合
     */
    public List<EcmpConfig> selectEcmpConfigList(EcmpConfig ecmpConfig);

    /**
     * 新增参数配置
     *
     * @param ecmpConfig 参数配置
     * @return 结果
     */
    public int insertEcmpConfig(EcmpConfig ecmpConfig);

    /**
     * 修改参数配置
     *
     * @param ecmpConfig 参数配置
     * @return 结果
     */
    public int updateEcmpConfig(EcmpConfig ecmpConfig);

    /**
     * 根据Key更新value
     * @param ecmpConfig
     * @return
     */
    public int updateConfigByKey(EcmpConfig ecmpConfig);

    /**
     * 删除参数配置
     *
     * @param configId 参数配置ID
     * @return 结果
     */
    public int deleteEcmpConfigById(Integer configId);

    /**
     * 批量删除参数配置
     *
     * @param configIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEcmpConfigByIds(Integer[] configIds);
}
