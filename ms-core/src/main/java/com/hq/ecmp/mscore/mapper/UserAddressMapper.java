package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.UserAddress;
import org.springframework.stereotype.Repository;

/**
 * 用户地址Mapper接口
 */
@Repository
public interface UserAddressMapper
{
    /**
     * 查询用户地址信息
     */
     UserAddress getByUserId(Long userId);

    /**
     * 新增用户信息

     */
    int insertUserAddress(UserAddress userAddress);

    /**
     * 修改用户信息
     */
    int updateUserAddress(UserAddress userAddress);

}

