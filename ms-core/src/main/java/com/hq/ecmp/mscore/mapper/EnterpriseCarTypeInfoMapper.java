package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.EnterpriseCarTypeInfo;
import com.hq.ecmp.mscore.dto.CarTypeDTO;
import com.hq.ecmp.mscore.vo.CarTypeVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author hqer
 * @date 2020-01-02
 */
@Repository
public interface EnterpriseCarTypeInfoMapper
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
     * 删除【请填写功能名称】
     *
     * @param carTypeId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteEnterpriseCarTypeInfoById(Long carTypeId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carTypeIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteEnterpriseCarTypeInfoByIds(Long[] carTypeIds);

    /**
     * 根据车型查询id
     * @param onlineCarLevel
     * @return
     */
    Long selectCarTypeId(String onlineCarLevel);

    /**
     * 查詢最大的車型級別
     * @return
     */
    String getCarTypeDTOById(@Param("enterpriseId") Long enterpriseId);
    
    /**
     * 查询车辆的车型名称
     * @param carId
     * @return
     */
    String queryCarTypeNameByCarId(Long carId);

    /**
     * 车型图标中已经用过的图标
     * @param enterpriseId
     * @return
     */
    List<CarTypeDTO> selectEnterpriseCarTypeList(@Param("enterpriseId") Long enterpriseId);

    /**
     * 根据CarTypeIdd查询对应的车型id集合
     * @param carTypeDTO
     * @return
     */
    List<CarTypeDTO> selectCarTypeById(CarTypeDTO carTypeDTO);

    String selectCarTypesByTypeIds(@Param("ownerCompany") Long ownerCompany, @Param("levels") String levels);

    List<EnterpriseCarTypeInfo> selectEnterpriseCarTypeIds(@Param("list") List<String> carTypeId);

    /**
     * 查询制度中可用车型
     * @param useCarModeOwnerLevel
     * @param companyId
     * @return
     */
    List<CarTypeVO> queryCarTypeList(@Param("useCarModeOwnerLevel") String useCarModeOwnerLevel,@Param("companyId") Long companyId);

    /**
     * 获取车型中最大的排序 序号
     * @param companyId
     * @return
     */
    String getCarTypeDTOSortById(Long companyId);
}
