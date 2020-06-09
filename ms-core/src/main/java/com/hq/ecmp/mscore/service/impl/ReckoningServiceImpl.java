package com.hq.ecmp.mscore.service.impl;


import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.mapper.ReckoningMapper;
import com.hq.ecmp.mscore.service.ReckoningService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
@Slf4j
public class ReckoningServiceImpl implements ReckoningService {

    @Autowired
    private ReckoningMapper reckoningMapper;

    /**
     * 添加收款信息
     * @param param
     */
    @Override
    public void addReckoning(ReckoningDto param) {
        reckoningMapper.add(param);
    }

    /**
     * 查询收款信息
     * @param param
     */
    @Override
    public void findReckoning(ReckoningDto param) {

    }
}
