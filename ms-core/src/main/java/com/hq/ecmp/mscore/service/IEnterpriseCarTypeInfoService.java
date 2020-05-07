package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.EnterpriseCarTypeInfo;
import com.hq.ecmp.mscore.dto.CarTypeDTO;
import com.hq.ecmp.mscore.vo.CarTypeVO;
import com.hq.ecmp.mscore.vo.PageResult;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IEnterpriseCarTypeInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param carTypeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public EnterpriseCarTypeInfo selectEnterpriseCarTypeInfoById(Long carTypeId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<EnterpriseCarTypeInfo> selectEnterpriseCarTypeInfoList(EnterpriseCarTypeInfo enterpriseCarTypeInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertEnterpriseCarTypeInfo(EnterpriseCarTypeInfo enterpriseCarTypeInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateEnterpriseCarTypeInfo(EnterpriseCarTypeInfo enterpriseCarTypeInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carTypeIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEnterpriseCarTypeInfoByIds(Long[] carTypeIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carTypeId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEnterpriseCarTypeInfoById(Long carTypeId) throws Exception;


    /**
     * 查询用户企业有效车型 豪华型 公务型
     * @param userId
     * @return
     */
    List<EnterpriseCarTypeInfo> selectEffectiveCarTypes(Long userId);

    /**
     * 新增车型
     * @param carDto
     * @param userId
     */
    void saveCarType(CarTypeDTO carDto, Long userId) throws Exception;

    /**
     * 修改车型
     * @param carDto
     * @param userId
     */
    void updateCarType(CarTypeDTO carDto, Long userId) throws Exception;

    /**
     * 查询企业车型列表
     * @param companyId
     * @return
     */
    List<CarTypeVO> getCarTypeList(Long companyId);

    /**
     * 车型排序（交换位置）
     * @param mainCarTypeId
     * @param targetCarTypeId
     */
    void sortCarType(Long mainCarTypeId, Long targetCarTypeId,Long userId) throws Exception;
}
