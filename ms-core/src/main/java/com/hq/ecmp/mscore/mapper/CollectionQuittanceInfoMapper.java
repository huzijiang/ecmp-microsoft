package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ReckoningInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionQuittanceInfoMapper {

    void add(ReckoningInfo param);
}
