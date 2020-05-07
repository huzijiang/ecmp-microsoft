package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.dto.CarGroupDTO;
import com.hq.ecmp.mscore.vo.*;

import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface ICarGroupInfoService
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
     * 批量删除【请填写功能名称】
     *
     * @param carGroupIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupInfoByIds(Long[] carGroupIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param carGroupId 【请填写功能名称】ID
     * @return 结果
     */
    public int deleteCarGroupInfoById(Long carGroupId);

    /**
     * 新增车队和调度员信息
     * @param carGroupDTO
     */
    void saveCarGroupAndDispatcher(CarGroupDTO carGroupDTO,Long userId) throws Exception;

    /**
     * 车队详情
     * @param carGroupId
     * @return
     */
    CarGroupDetailVO getCarGroupDetail(Long carGroupId);

    /**
     * 修改车队
     * @param carGroupDTO
     */
    void updateCarGroup(CarGroupDTO carGroupDTO,Long userId) throws Exception;

    /**
     * 禁用车队
     * @param carGroupId
     * @param userId
     */
    void disableCarGroup(Long carGroupId, Long userId) throws Exception;

    /**
     * 启用车队
     * @param carGroupId
     * @param userId
     */
    void startUpCarGroup(Long carGroupId, Long userId) throws Exception;

    /**
     * 分页查询车队信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult<CarGroupListVO> selectCarGroupInfoByPage(Integer pageNum, Integer pageSize,String search,String state,Long deptId,Long carGroupId);

    /**
     * 删除车队
     * @param carGroupId
     * @param userId
     */
    String deleteCarGroup(Long carGroupId,Long userId) throws Exception;

    //查询下级车队列表
    List<CarGroupListVO> selectSubCarGroupInfoList(Long deptId);

    /**
     * 查询指定城市所有车队调度员及车队座机
     * @param
     * @return
     */
    List<CarGroupPhoneVO> getCarGroupPhone(String cityCode);

    /**
     * 查询调度员电话及调度员所在车队座机
     * @param
     * @return
     */
    DispatcherAndFixedLineVO getDispatcherAndFixedLine(Long orderId);

    /*根据分子公司id查询车队树*/
    List<CarGroupTreeVO> selectCarGroupTree(Long deptId);

    /*查询所有车队编号*/
    List<String> selectAllCarGroupCode();

    /*判断车队编号是否存在*/
    boolean judgeCarGroupCode(String carGroupCode);

    /*回显车队信息*/
    CarGroupDTO getCarGroupInfoFeedBack(Long carGroupId);

    /*查询司机所属车队座机及调度员电话*/
    CarGroupPhoneVO getOwnerCarGroupPhone();

    /**
     * 车队名字校验 同一公司下，车队名不能重复
     * @param carGroupName
     * @param owneCompany
     * @return
     */
    Boolean judgeCarGroupName(String carGroupName, Long owneCompany);

    List<CarGroupListVO> getCarGroupList(Long userId);

    /**
     * 联系车队（通用）
     * @param orderId
     * @return
     */
    List<ContactCarGroupVO> cantactCarGroup(Long orderId);
}
