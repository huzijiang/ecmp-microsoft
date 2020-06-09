package com.hq.ecmp.mscore.service.lease.impl;

import com.hq.ecmp.mscore.dto.lease.LeaseSettlementDto;
import com.hq.ecmp.mscore.service.lease.LeaseSettlementService;
import com.hq.ecmp.mscore.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@Slf4j
public class LeaseSettlementServiceImpl implements LeaseSettlementService {


    /***
     *
     * @param data
     * @return
     * @throws Exception
     */
    @Override
    public PageResult<LeaseSettlementDto> getOrdinaryUserList(LeaseSettlementDto data) throws Exception {
        return null;
    }


    /***
     *
     * @param collectionId
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> getOrdinaryUserDetail(Long collectionId) throws Exception {
        return null;
    }

    /***
     *
     * @param collectionId
     * @return
     * @throws Exception
     */
    @Override
    public int ordinaryUserConfirmCost(Long collectionId) throws Exception {
        return 0;
    }
}
