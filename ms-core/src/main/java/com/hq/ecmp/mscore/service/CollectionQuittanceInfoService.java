package com.hq.ecmp.mscore.service;


import com.hq.ecmp.mscore.domain.ReckoningInfo;
import com.hq.ecmp.mscore.dto.ReckoningDto;

import java.util.Map;

public interface CollectionQuittanceInfoService {


    void addReckoning(ReckoningInfo param);

    void findReckoning(ReckoningDto param);

    void updateReckoningStatus(ReckoningDto param);

    Map reckoningDetail(ReckoningDto param);
}
