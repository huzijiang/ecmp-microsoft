package com.hq.ecmp.mscore.service;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.EcmpConfig;
import com.hq.ecmp.mscore.dto.config.ConfigInfoDTO;
import com.hq.ecmp.mscore.dto.config.EnterPriseBaseInfoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * 参数配置Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEcmpConfigService
{
    /**
     * 查询参数配置
     *
     * @param configId 参数配置ID
     * @return 参数配置
     */
    public EcmpConfig selectEcmpConfigById(Integer configId);

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
     * 批量删除参数配置
     *
     * @param configIds 需要删除的参数配置ID
     * @return 结果
     */
    public int deleteEcmpConfigByIds(Integer[] configIds);

    /**
     * 删除参数配置信息
     *
     * @param configId 参数配置ID
     * @return 结果
     */
    public int deleteEcmpConfigById(Integer configId);

    /**
     * 查询企业配置信息
     * @return
     */
    ConfigInfoDTO selectConfigInfo(Long companyId);

    /**
     * 设置企业基本信息
     * @param enterPriseBaseInfoDTO
     */
    void setUpBaseInfo(EnterPriseBaseInfoDTO enterPriseBaseInfoDTO);

    /**
     * 设置开屏图
     * @param status
     * @param value
     * @param file
     */
    void setUpWelComeImage(String status, String value, MultipartFile file,Long companyId);

    /**
     * 设置背景图
     * @param status
     * @param value
     * @param file
     */
    ApiResponse setUpBackGroundImage(String status, String value, MultipartFile file,Long companyId);

    /**
     * 设置企业公告
     * @param status
     */
    void setUpMessageConfig(String status,Long companyId);

    /**
     * 设置短信开关
     * @param status
     */
    void setUpSms(String status,Long companyId);

    /**
     * 设置虚拟小号开关
     * @param status
     */
    void setUpVirtualPhone(String status,Long companyId);

    /**
     * 设置订单确认开关
     *
     * @param status
     * @param value
     * @param owenType
     * @param rideHailing
     */
    void setUpOrderConfirm(String status, String value, String owenType, String rideHailing,Long companyId);

    /**
     * 设置自动派单方式
     * @param status
     * @param value
     */
    void setUpDispatchInfo(String status, String value,Long companyId);

    /**
     * 设置用车往返等时长（单位分钟）
     * @param status
     * @param value
     */
    void setUpWaitMaxMinute(String status, String value,Long companyId);

    /**
     * 获取行程确认/异议 开关状态(0:自动,1手动)
     */
    int getOrderConfirmStatus(String key,String useCarMode,Long companyId);


    public boolean checkAutoDispatch(Long companyId);

    /**
     * 判断设置的等待时长是否超过预计等待时长
     * @param waitMin   等待时长 分钟
     * @return
     */
    public boolean checkUpWaitMaxMinute(Long waitMin);

    /**
     * 获取启动页开屏图（无token）
     * @return
     */
    //ConfigInfoDTO  getStartupChart();

    /**
     * 获取首页背景图的接口
     * @return
     */
    //ConfigInfoDTO getHomeChart();
}
