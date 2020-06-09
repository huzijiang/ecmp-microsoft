package com.hq.ecmp.mscore.service.lease;

import com.hq.ecmp.mscore.dto.lease.LeaseSettlementDto;
import com.hq.ecmp.mscore.vo.PageResult;

import java.util.Map;

public interface LeaseSettlementService {

    /***
     *
     * @param data
     * @return
     * @throws Exception
     */
    PageResult<LeaseSettlementDto> getOrdinaryUserList(LeaseSettlementDto data)throws Exception;


    /***
     *
     * @param collectionId
     * @return
     * @throws Exception
     */
    Map<String,Object> getOrdinaryUserDetail(Long collectionId)throws Exception;


    /***
     *
     * @param collectionId
     * @return
     * @throws Exception
     */
    int ordinaryUserConfirmCost(Long collectionId)throws Exception;


}
