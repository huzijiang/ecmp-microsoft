package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.vo.CarGroupFixedPhoneVO;
import com.hq.ecmp.mscore.vo.CarGroupListVO;
import com.hq.ecmp.mscore.vo.CarGroupPhoneVO;
import com.hq.ecmp.mscore.vo.CarGroupTreeVO;
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
public interface CarGroupInfoMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param carGroupId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public CarGroupInfo selectCarGroupInfoById(Long carGroupId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CarGroupInfo> selectCarGroupInfoList(CarGroupInfo carGroupInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertCarGroupInfo(CarGroupInfo carGroupInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param carGroupInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateCarGroupInfo(CarGroupInfo carGroupInfo);

    /**
     * 删除【请填写功能名称】
     *
     * @param carGroupId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupInfoById(Long carGroupId);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param carGroupIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteCarGroupInfoByIds(Long[] carGroupIds);

    /**
     * 查找车队的集合
     * @param search
     * @param state
     * @param deptId
     * @param carGroupId
     * @return
     */
    List<CarGroupListVO> selectAllByPage(@Param("search") String search,@Param("state")String state,@Param("deptId")Long deptId,@Param("carGroupId")Long carGroupId,@Param("companyId") Long companyId);

    /**
     * 查询该组织下的车队信息
     *
     * @param deptId 组织ID
     * @return 结果
     */
    public int selectCountByOrgdeptId(Long deptId);

    /**
     * 查询下级车队列表
     * @param deptId
     * @return
     */
    List<CarGroupListVO> selectSubCarGroupInfoList(Long deptId);

    /**
     * 查询城市内所有车队
     * @param cityCode
     * @return
     */
    List<CarGroupInfo> selectValidCarGroupListByCity(String cityCode);

    /**
     * 查询车队电话及车队名字
     * @param groupIds
     * @return
     */
    List<CarGroupFixedPhoneVO> selectCarGroupPhones(List<Long> groupIds);


    /**
     * 查询指定司机的
     *车队信息
     * @param driverId
     * @return List<CarGroupInfo>
     */
    List<CarGroupInfo> selectCarGroupsByDriverId(Long driverId);

    /**
     * 根据分子公司id查询一级车队
     * @param deptId
     * @return
     */
    List<CarGroupTreeVO> selectFirstLevelCarGroupList(Long deptId);

    /**
     * 根据车队id查询车队树
     * @param carGroupId
     * @return
     */
    List<CarGroupTreeVO> getCarGroupTree(Long carGroupId);

    /**
     * 查询下级车队数量
     * @param
     * @return
     */
    int selectCountByParentId(Long parentId);

    /**
     * 查询所有车队编号
     * @return
     */
    List<String> selectAllCarGroupCode();

    /**
     * 查询公司所有车队名字
     * @param owneCompany
     * @return
     */
    List<String> selectAllCarGroupNameByCompany(Long owneCompany);

    /**
     * 查询启用的公司所有车队
     * @param carGroupInfo
     * @return
     */
    List<CarGroupInfo> selectEnableCarGroupInfoList(CarGroupInfo carGroupInfo);

    List<CarGroupListVO> getCarGroupList(Long userId);

    List<CarGroupInfo> selectCarGroupInfoByDeptId(@Param("orgComcany") Long orgComcany,@Param("deptId") Long deptId);
}
