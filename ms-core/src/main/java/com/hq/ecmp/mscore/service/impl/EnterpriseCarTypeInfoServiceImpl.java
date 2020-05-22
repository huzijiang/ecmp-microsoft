package com.hq.ecmp.mscore.service.impl;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.domain.EcmpEnterpriseInfo;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.EnterpriseCarTypeInfo;
import com.hq.ecmp.mscore.dto.CarTypeDTO;
import com.hq.ecmp.mscore.mapper.CarInfoMapper;
import com.hq.ecmp.mscore.mapper.EcmpEnterpriseInfoMapper;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.mapper.EnterpriseCarTypeInfoMapper;
import com.hq.ecmp.mscore.service.IEnterpriseCarTypeInfoService;
import com.hq.ecmp.mscore.vo.CarTypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class EnterpriseCarTypeInfoServiceImpl implements IEnterpriseCarTypeInfoService
{
    @Autowired
    private EnterpriseCarTypeInfoMapper enterpriseCarTypeInfoMapper;
    @Autowired
    private EcmpUserMapper ecmpUserMapper;
    @Autowired
    private EcmpEnterpriseInfoMapper ecmpEnterpriseInfoMapper;
    @Autowired
    private CarInfoMapper carInfoMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param carTypeId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public EnterpriseCarTypeInfo selectEnterpriseCarTypeInfoById(Long carTypeId)
    {
        return enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(carTypeId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<EnterpriseCarTypeInfo> selectEnterpriseCarTypeInfoList(EnterpriseCarTypeInfo enterpriseCarTypeInfo)
    {
        return enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertEnterpriseCarTypeInfo(EnterpriseCarTypeInfo enterpriseCarTypeInfo)
    {
        enterpriseCarTypeInfo.setCreateTime(DateUtils.getNowDate());
        return enterpriseCarTypeInfoMapper.insertEnterpriseCarTypeInfo(enterpriseCarTypeInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param enterpriseCarTypeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateEnterpriseCarTypeInfo(EnterpriseCarTypeInfo enterpriseCarTypeInfo)
    {
        enterpriseCarTypeInfo.setUpdateTime(DateUtils.getNowDate());
        return enterpriseCarTypeInfoMapper.updateEnterpriseCarTypeInfo(enterpriseCarTypeInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carTypeIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEnterpriseCarTypeInfoByIds(Long[] carTypeIds)
    {
        return enterpriseCarTypeInfoMapper.deleteEnterpriseCarTypeInfoByIds(carTypeIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carTypeId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteEnterpriseCarTypeInfoById(Long carTypeId) throws Exception {
        //判断该车型下是否绑定车辆 如有绑定则无法删除
        CarInfo carInfo = CarInfo.builder().carTypeId(carTypeId).build();
        List<CarInfo> carInfos = carInfoMapper.selectCarInfoList(carInfo);
        if(carInfos.size() != 0){
            throw new Exception("请先移除类型下绑定的车辆");
        }
        int i = enterpriseCarTypeInfoMapper.deleteEnterpriseCarTypeInfoById(carTypeId);
        if(i != 1){
            throw new Exception("删除失败");
        }
        return i;
    }

    /**
     * 查询用户企业有效车型 豪华型 公务型
     * @param userId
     * @return
     */
    @Override
    public List<EnterpriseCarTypeInfo> selectEffectiveCarTypes(Long userId) {
        //查询用户所在公司  用户 -- 部门 -- 公司
        //查询用户所在部门
        EcmpUser ecmpUser = ecmpUserMapper.selectEcmpUserById(userId);
        //查询用户所在公司
        Long deptId = ecmpUser.getDeptId();
        EcmpEnterpriseInfo ecmpEnterpriseInfo = new EcmpEnterpriseInfo();
        ecmpEnterpriseInfo.setDeptId(deptId);
        //根据部门id查公司
        List<EcmpEnterpriseInfo> ecmpEnterpriseInfos = ecmpEnterpriseInfoMapper.selectEcmpEnterpriseInfoList(ecmpEnterpriseInfo);
        //公司id
        Long enterpriseId = ecmpEnterpriseInfos.get(0).getEnterpriseId();
        //查询公司有效车型 状态 S000   生效中  S444   失效中
        EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
        enterpriseCarTypeInfo.setCompanyId(enterpriseId);
        enterpriseCarTypeInfo.setStatus("S000");
        //查询公司车型
        List<EnterpriseCarTypeInfo> enterpriseCarTypeInfos = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
        return enterpriseCarTypeInfos;
    }

    /**
     * 新增车型
     * @param carDto
     * @param userId
     */
    @Override
    public void saveCarType(CarTypeDTO carDto, Long userId) throws Exception {
        EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
        enterpriseCarTypeInfo.setCompanyId(carDto.getCompanyId());
        enterpriseCarTypeInfo.setName(carDto.getName());
        //增加車型級別
        String Level = enterpriseCarTypeInfoMapper.getCarTypeDTOById(carDto.getCompanyId());
        String carLevelType= "P001";
        if(Level!=null && Level !=""){
            String carLevel = Level.substring(1);
            String newEquipmentNo = String.format("P" + "%03d", Integer.parseInt(carLevel.substring(1))+1);
            enterpriseCarTypeInfo.setLevel(newEquipmentNo);
        }else{
            enterpriseCarTypeInfo.setLevel(carLevelType);
        }

        // 初始化状态为生效  状态   S000   生效中    S444   失效中
        enterpriseCarTypeInfo.setStatus(CarConstant.START_CAR_TYPE);
        enterpriseCarTypeInfo.setCreateBy(String.valueOf(userId));
        enterpriseCarTypeInfo.setImageUrl(carDto.getImageUrl());
        enterpriseCarTypeInfo.setCreateTime(new Date());
        int i = enterpriseCarTypeInfoMapper.insertEnterpriseCarTypeInfo(enterpriseCarTypeInfo);
        if(i != 1){
            throw new Exception("新增车型失败");
        }
    }

    /**
     * 修改车型
     * @param carDto
     * @param userId
     */
    @Override
    public void updateCarType(CarTypeDTO carDto, Long userId) throws Exception {
        EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
        enterpriseCarTypeInfo.setCarTypeId(carDto.getCarTypeId());
        enterpriseCarTypeInfo.setName(carDto.getName());
        enterpriseCarTypeInfo.setLevel(carDto.getLevel());
        enterpriseCarTypeInfo.setImageUrl(carDto.getImageUrl());
        enterpriseCarTypeInfo.setUpdateBy(String.valueOf(userId));
        enterpriseCarTypeInfo.setUpdateTime(new Date());
        int i = enterpriseCarTypeInfoMapper.updateEnterpriseCarTypeInfo(enterpriseCarTypeInfo);
        if(i != 1){
            throw new Exception();
        }
    }

    /**
     * 查询企业车型列表
     * @param companyId
     * @return
     */
    @Override
    public List<CarTypeVO> getCarTypeList(Long companyId) {
        EnterpriseCarTypeInfo enterpriseCarTypeInfo = new EnterpriseCarTypeInfo();
        enterpriseCarTypeInfo.setCompanyId(companyId);
        List<EnterpriseCarTypeInfo> enterpriseCarTypeInfos = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoList(enterpriseCarTypeInfo);
        CarTypeVO carTypeVO = null;
        List<CarTypeVO> list = Lists.newArrayList();
        for (EnterpriseCarTypeInfo carTypeInfo : enterpriseCarTypeInfos) {
            carTypeVO = CarTypeVO.builder().carTypeId(carTypeInfo.getCarTypeId())
                    .countryCarTypeId(carTypeInfo.getCountryCarTypeId())
                    .level(carTypeInfo.getLevel())
                    .name(carTypeInfo.getName())
                    .status(carTypeInfo.getStatus())
                    .enterpriseId(carTypeInfo.getCompanyId())
                    .carType(carTypeInfo.getCarType())
                    .carNum(carTypeInfo.getCarNum())
                    .imageUrl(carTypeInfo.getImageUrl())
                    .build();
            list.add(carTypeVO);
        }
        return list;
    }

    /**
     * 车型排序（交换位置）
     * @param mainCarTypeId
     * @param targetCarTypeId
     */
    @Override
    public void sortCarType(Long mainCarTypeId, Long targetCarTypeId,Long userId) throws Exception {
        EnterpriseCarTypeInfo mainCarTypeInfo = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(mainCarTypeId);
        String min=mainCarTypeInfo.getLevel();
        EnterpriseCarTypeInfo targetCarTypeInfo = enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeInfoById(targetCarTypeId);
        String target = targetCarTypeInfo.getLevel();
        mainCarTypeInfo.setLevel(target);
        mainCarTypeInfo.setImageUrl(targetCarTypeInfo.getImageUrl());
        mainCarTypeInfo.setName(targetCarTypeInfo.getName());
        mainCarTypeInfo.setUpdateTime(new Date());
        mainCarTypeInfo.setUpdateBy(String.valueOf(userId));
        targetCarTypeInfo.setLevel(min);
        targetCarTypeInfo.setImageUrl(mainCarTypeInfo.getImageUrl());
        targetCarTypeInfo.setName(mainCarTypeInfo.getName());
        targetCarTypeInfo.setUpdateBy(String.valueOf(userId));
        targetCarTypeInfo.setUpdateTime(new Date());
        int i = enterpriseCarTypeInfoMapper.updateEnterpriseCarTypeInfo(mainCarTypeInfo);
        if(i != 1){
            throw new Exception();
        }
        int j = enterpriseCarTypeInfoMapper.updateEnterpriseCarTypeInfo(targetCarTypeInfo);
        if(j != 1){
            throw new Exception();
        }
    }

    /**
     * 车型图标中已经用过的图标
     * @param companyId
     * @return
     */
    @Override
    public List<CarTypeDTO> selectEnterpriseCarTypeList(Long companyId) {
        return enterpriseCarTypeInfoMapper.selectEnterpriseCarTypeList(Long.valueOf(companyId));
    }

    /**
     * 根据CarTypeIdd查询对应的车型id集合
     * @param carTypeDTO
     * @return
     */
    @Override
    public List<CarTypeDTO> selectCarTypeById(CarTypeDTO carTypeDTO) {
        return enterpriseCarTypeInfoMapper.selectCarTypeById(carTypeDTO);
    }
}
