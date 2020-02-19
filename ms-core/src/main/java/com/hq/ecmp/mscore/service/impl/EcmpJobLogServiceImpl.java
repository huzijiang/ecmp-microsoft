package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.EcmpJobLog;
import com.hq.ecmp.mscore.mapper.EcmpJobLogMapper;
import com.hq.ecmp.mscore.service.IEcmpJobLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务调度日志表 服务实现类
 * </p>
 *
 * @author crk
 * @since 2020-02-20
 */
@Service
public class EcmpJobLogServiceImpl extends ServiceImpl<EcmpJobLogMapper, EcmpJobLog> implements IEcmpJobLogService {

}
