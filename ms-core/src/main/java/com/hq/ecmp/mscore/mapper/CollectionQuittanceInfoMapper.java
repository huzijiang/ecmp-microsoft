package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ReckoningInfo;
import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.dto.lease.LeaseSettlementDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionQuittanceInfoMapper {

    void add(ReckoningInfo param);

    void updateReckoningStatus(ReckoningDto param);


    /***
     * 普通用户的租赁列表
     * @param data
     * @return
     */
    List<LeaseSettlementDto> getOrdinaryUserList(LeaseSettlementDto data);

    /***
     * add by liuzb
     * 确认费用
     * @param data
     * @return
     */
    int ordinaryUserConfirmCost(LeaseSettlementDto data);
}
