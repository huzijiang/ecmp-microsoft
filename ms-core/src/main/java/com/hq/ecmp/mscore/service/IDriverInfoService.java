package com.hq.ecmp.mscore.service;

import java.util.List;
import java.util.Date;
import java.util.Map;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.mscore.domain.CarGroupDriverInfo;
import com.hq.ecmp.mscore.domain.DriverCreateInfo;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.DriverQuery;
import com.hq.ecmp.mscore.domain.DriverQueryResult;
import com.hq.ecmp.mscore.domain.DriverUserJobNumber;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.vo.PageResult;

/**
 * 【请填写功能名称】Service接口
 *
 * @author hqer
 * @date 2020-01-02
 */
public interface IDriverInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param driverId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    public DriverInfo selectDriverInfoById(Long driverId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param driverInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<DriverInfo> selectDriverInfoList(DriverInfo driverInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param driverInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertDriverInfo(DriverInfo driverInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param driverInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateDriverInfo(DriverInfo driverInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param driverIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    public int deleteDriverInfoByIds(Long[] driverIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param driverId 【请填写功能名称】ID
     * @return 结果
     */

    public int deleteDriverInfoById(Long driverId);

    public boolean createDriver(DriverCreateInfo driverCreateInfo);
    public boolean updateDriver(DriverCreateInfo driverCreateInfo);

    public List<DriverQueryResult> queryDriverList(DriverQuery query);

    public Integer queryDriverListCount(DriverQuery query);

    public DriverQueryResult  queryDriverDetail(Long driverId);
    /**
     *驾驶员总数
     */
    public int queryCompanyDriverCount();
    /**
     *
     * @param driverRegisterDTO
     * @return
     */
    public int driverItisExist(DriverRegisterDTO driverRegisterDTO);

    public int updateDriverStatus(Long driverId,String state);
    /**
     *驾驶员可用车辆列表
     * @param
     */
    public PageResult<DriverCanUseCarsDTO> getDriverCanCar(Integer pageNum, Integer pageSize,Long driverId,String state,String search);
    /**
     *驾驶员失效列表,离职列表
     * @param
     */
    public PageResult<DriverLoseDTO> getDriverLoseList(Integer pageNum, Integer pageSize,Long carGroupId,String search);
    /**
     * 驾驶员已离职数量
     */
    public Long getDriverLoseCount(Long carGroupId);
    /**
     * 已失效驾驶员进行删除
     */
    public ApiResponse deleteDriver(Long driverId) throws Exception;


    /**
     * 修改驾驶员手机号
     * @param mobile
     * @return
     */
    public int updateDriverMobile(String mobile,Long driverId);

    /**
     * 设置驾驶员离职日期
     * @param dimTime
     * @return
     */
    public int updateDriverDimTime(Date dimTime,Long driverId);
    /**
     * 驾驶员绑定车辆
     * @param driverCarDTO
     * @return
     */
    public void bindDriverCars(DriverCarDTO driverCarDTO, Long userId) throws Exception;

    /**
     * 查询指定车队下的可用驾驶员
     * @param map
     * @return
     */
    public  CarGroupDriverInfo queryCarGroupDriverList(Map map);

    /**
     * 校验驾驶员手机号是否已经存在
     * @param mobile
     * @return  true - 已经存在
     */
    public boolean checkMobile(String mobile);

	/**
	 * 新增驾驶员时输入工号 校验正确性
	 * 1、输入工号之后，根据姓名和手机号去员工表查询工号：
	 *
	 * a、若输入工号等于员工工号，输入正确
	 *
	 * b、若输入工号不等于员工工号，弹窗提示：员工张三：15989890980对应的工号是D2345，请核实后重新输入！
	 *
	 * c、若员工表查询不到工号，则直接保存此驾驶员录入的工号，并且做去重校验，若员工表已存在此工号，弹窗提示：工号已被占用，请重新录入！
	 *
	 * @param driverUserJobNumber
	 * @return
	 * @throws Exception
	 */
	public void checkjobNumber(DriverUserJobNumber driverUserJobNumber) throws Exception;

    /**
     * 驾驶员调度看板
     * @param pageRequest
     * @return
     */
    PageResult driverWorkOrderList(PageRequest pageRequest);
}
