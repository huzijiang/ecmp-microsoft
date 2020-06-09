package com.hq.ecmp.mscore.service;


import com.hq.ecmp.mscore.dto.ReckoningDto;

public interface ReckoningService {


    void addReckoning(ReckoningDto param);

    void findReckoning(ReckoningDto param);
}
