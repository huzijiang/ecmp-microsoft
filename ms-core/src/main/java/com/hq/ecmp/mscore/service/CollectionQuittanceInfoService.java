package com.hq.ecmp.mscore.service;


import com.hq.ecmp.mscore.domain.ReckoningInfo;
import com.hq.ecmp.mscore.dto.ReckoningDto;

public interface CollectionQuittanceInfoService {


    void addReckoning(ReckoningInfo param);

    void findReckoning(ReckoningDto param);
}
