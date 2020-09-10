package com.hq.ecmp.mscore.service.impl;

import com.hq.ecmp.mscore.mapper.UseCarSumMapper;
import com.hq.ecmp.mscore.service.UseCarSumService;
import com.hq.ecmp.vo.UseCarSumExportVo;
import com.hq.ecmp.vo.UseCarSumVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<UseCarSumExportVo> export(UseCarSumVo useCarSumVo) {
        List<UseCarSumExportVo> list = useCarSumMapper.getUseCarSumExportVoList(useCarSumVo);
//        List<UseCarSumExportVo> list = new ArrayList<>();
//        UseCarSumExportVo useCarSumExportVo = new UseCarSumExportVo();
//        useCarSumExportVo.setAmount("9999");
//        useCarSumExportVo.setAmountByIn("88");
//        useCarSumExportVo.setUseTimes("4");
//        useCarSumExportVo.setDeptName("AAA");
//        useCarSumExportVo.setOrders(99);
//        useCarSumExportVo.setUseTimes("999");
//
//        list.add(useCarSumExportVo);
        return list;
    }
}
