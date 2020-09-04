package com.hq.ecmp.mscore.service;

import com.hq.core.web.domain.AjaxResult;
import com.hq.ecmp.vo.UseCarSumExportVo;
import com.hq.ecmp.vo.UseCarSumVo;

import java.util.List;

/**
 * UseCarSumService：
 *
 * @author: ll
 * @date: 2020/9/4 16:10
 */
public interface UseCarSumService {
    AjaxResult export(UseCarSumVo useCarSumVo);
}
