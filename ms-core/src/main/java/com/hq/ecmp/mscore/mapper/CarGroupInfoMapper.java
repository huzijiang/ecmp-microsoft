package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.dto.dispatch.DispatchCarGroupDto;
import com.hq.ecmp.mscore.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    List<CarGroupListVO> selectAllByPage(@Param("search") String search,@Param("state")String state,@Param("deptId")Long deptId,@Param("carGroupId")Long carGroupId,@Param("companyId") Long companyId,@Param("userId") Long userId);

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
    List<CarGroupTreeVO> selectFirstLevelCarGroupList(Long deptId,Long userId);

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
    List<String> selectAllCarGroupCode(Long companyId);

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

    List<CarGroupListVO> getCarGroupList(@Param("userId")Long userId,@Param("flag")int flag,@Param("companyId")Long companyId,@Param("cityCode") String cityCode);

    List<CarGroupInfo> selectCarGroupInfoByDeptId(@Param("orgComcany") Long orgComcany,@Param("deptId") Long deptId);

    List<CarLevelVO> findCarTypeByGroupIds(@Param("groupIds")String groupIds);

    List<Map> getCarGroupTreeByDeptIds(List list);

    List<Map> getCarGroupTreeByCarIds(List list);
    /**
     * 查询内部公司在当前城市点的可服务于申请人所在部门的车队id列表
     * @param deptId 部门id
     * @param cityCode 城市code
     * @param companyId 公司id
     * @return
     */
    List<Long> queryCarGroupIdInnerCompany(@Param("deptId") Long deptId,@Param("cityCode") String cityCode,@Param("companyId") Long companyId);

    /**
     * 查询外部公司在当前城市可服务于申请人所在公司的车队id列表
     * @param cityCode 城市code
     * @param companyId 公司id
     * @return
     */
    List<Long> queryCarGroupIdOuterCompany(@Param("cityCode") String cityCode,@Param("companyId") Long companyId);

    /**
     * 通过公司id查询公司的所有调度员
     * @param companyId
     * @return
     */
    List<Long> queryAllDispatchersByCompanyId(Long companyId);

    /**
     * 获取调度员可调度的外部车队列表
     * @param deptId
     * @return
     */
     List<CarGroupInfo> dispatcherCarGroupList(@Param("depeId") Long deptId);

    /***
     *
     * @param userId
     * @return
     */
     List<String> selectIsDispatcher(@Param("userId")Long userId);

    /***
     *
     * @param userId
     * @param orderId
     * @return
     */
     List<String> getTakeBack(@Param("userId")Long userId,@Param("orderId")Long orderId);

    /**
     * 外部车队列表
     * @param companyId
     * @return
     */
    List<CarGroupInfo> applySingleCarGroupList(@Param("state") String state,@Param("itIsInner")String itIsInner,@Param("deptId") Long deptId);

    /**
     * 用户服务部门车队列表(包含内外部车队）
     * @param state
     * @param deptId
     * @return
     */
    List<CarGroupInfo> userDeptCarGroupList(@Param("state") String state,@Param("deptId") Long deptId);

    /**
     * 获取调度员的车队信息
     * @param userId
     * @return
     */
    List<DispatchCarGroupDto> getDisCarGroupInfoByUserId(@Param("userId") Long userId,@Param("companyId") Long companyId);

    List<String> reckoningDetail(ReckoningDto param);

    /**
     *
     * @return
     */
    List<String> getCarGroupAllName();
}
