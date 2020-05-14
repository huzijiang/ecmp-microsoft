package com.hq.ecmp.mscore.service;

import com.hq.ecmp.mscore.domain.UserEmergencyContactInfo;
import java.util.List;

/**
 * 用户紧急联系人信息表(UserEmergencyContactInfo)表服务接口
 *
 * @author makejava
 * @since 2020-03-09 23:58:22
 */
public interface UserEmergencyContactInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    UserEmergencyContactInfo queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<UserEmergencyContactInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param userEmergencyContactInfo 实例对象
     * @return 实例对象
     */
    UserEmergencyContactInfo insert(UserEmergencyContactInfo userEmergencyContactInfo);

    /**
     * 修改数据
     *
     * @param userEmergencyContactInfo 实例对象
     * @return 实例对象
     */
    UserEmergencyContactInfo update(UserEmergencyContactInfo userEmergencyContactInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);


    /**
     * 通过userId查询列表
     *
     * @param userId
     * @return 实例对象
     */
    List<UserEmergencyContactInfo> queryByUserId(Long userId);


    /**8
     * 根据用户id喝订单id查询紧急联系人，获取乘车信息并发编辑短信内容
     * @param userId
     * @param orderId
     * @throws Exception
     */
    String callThepolice(Long userId,String phonenumber,Long orderId,String address)throws Exception;

}