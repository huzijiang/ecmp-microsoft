package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.domain.EcmpOperLog;
import com.hq.ecmp.mscore.mapper.EcmpOperLogMapper;
import com.hq.ecmp.mscore.service.IEcmpOperLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志记录 服务实现类
 * </p>
 *
 * @author crk
 * @since 2020-02-20
 */
@Service
public class EcmpOperLogServiceImpl extends ServiceImpl<EcmpOperLogMapper, EcmpOperLog> implements IEcmpOperLogService {

}
