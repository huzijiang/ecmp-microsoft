package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.dto.ReckoningDto;
import org.springframework.stereotype.Repository;

@Repository
public interface ReckoningMapper {

    void add(ReckoningDto param);
}
