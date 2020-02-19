package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.mapper.EcmpUserMapper;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author crk
 * @since 2020-02-20
 */
@Service
public class EcmpUserServiceImpl extends ServiceImpl<EcmpUserMapper, EcmpUser> implements IEcmpUserService {

}
