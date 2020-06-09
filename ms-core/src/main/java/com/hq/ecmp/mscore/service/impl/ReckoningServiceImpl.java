package com.hq.ecmp.mscore.service.impl;


import com.hq.ecmp.mscore.domain.ReckoningInfo;
import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.mapper.CollectionQuittanceInfoMapper;
import com.hq.ecmp.mscore.service.CollectionQuittanceInfoService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
@Slf4j
public class ReckoningServiceImpl implements CollectionQuittanceInfoService {

    @Autowired
    private CollectionQuittanceInfoMapper collectionService;

    /**
     * 添加收款信息
     * @param param
     */
    @Override
    public void addReckoning(ReckoningInfo param) {
        collectionService.add(param);
    }

    /**
     * 查询收款信息
     * @param param
     */
    @Override
    public void findReckoning(ReckoningDto param) {

    }
}
