package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.EcmpJob;
import com.hq.ecmp.mscore.mapper.EcmpJobMapper;
import com.hq.ecmp.mscore.service.IEcmpJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务调度表 服务实现类
 * </p>
 *
 * @author crk
 * @since 2020-02-20
 */
@Service
public class EcmpJobServiceImpl extends ServiceImpl<EcmpJobMapper, EcmpJob> implements IEcmpJobService {

}
