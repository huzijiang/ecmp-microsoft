package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ReckoningInfo;
import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.dto.lease.LeaseSettlementDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Map;

@Repository
public interface CollectionQuittanceInfoMapper {

    Map getPayeeInfo(Long companyId);

    void add(ReckoningInfo param);

    void updateReckoningStatus(ReckoningDto param);


    /***
     * 普通用户的结算单列表
     * add by liuzb
     * @param data
     * @return
     */
    List<LeaseSettlementDto> getOrdinaryUserList(LeaseSettlementDto data);


    /***
     * 根据主键id获取结算单
     * add by liuzb
     * @param collectionId
     * @return
     */
    LeaseSettlementDto getOrdinaryUserById(@Param("collectionId") Long collectionId);

    /***
     * 更新结算单状态
     * add by liuzb
     * 确认费用
     * @param data
     * @return
     */
    int ordinaryUserConfirmCost(LeaseSettlementDto data);
}
