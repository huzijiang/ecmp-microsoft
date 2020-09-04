package com.hq.ecmp.mscore.service.impl;

import com.hq.common.utils.poi.ExcelUtil;
import com.hq.core.web.domain.AjaxResult;
import com.hq.ecmp.mscore.mapper.UseCarSumMapper;
import com.hq.ecmp.mscore.service.UseCarSumService;
import com.hq.ecmp.vo.UseCarSumExportVo;
import com.hq.ecmp.vo.UseCarSumVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UseCarSumServiceImpl：
 *
 * @author: ll
 * @date: 2020/9/4 16:11
 */
@Service
public class UseCarSumServiceImpl implements UseCarSumService {
    @Autowired
    private UseCarSumMapper useCarSumMapper;

    @Override
    public AjaxResult export(UseCarSumVo useCarSumVo) {
        List<UseCarSumExportVo> list = useCarSumMapper.getUseCarSumExportVoList(useCarSumVo);
        ExcelUtil<UseCarSumExportVo> util = new ExcelUtil<>(UseCarSumExportVo.class);
        return util.exportExcel(list, "导出用车数据");
    }
}
