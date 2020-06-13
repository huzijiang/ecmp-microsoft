package com.hq.ecmp.mscore.mapper;

import com.hq.ecmp.mscore.domain.ReckoningInfo;
import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.dto.lease.LeaseSettlementDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionQuittanceInfoMapper {

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


    /** 通过收款唯一标识查询数据*/
    LeaseSettlementDto getCollectionNumber(@Param("collectionNumber") Long collectionNumber);

    /***
     * 更新结算单状态
     * add by liuzb
     * 确认费用
     * @param data
     * @return
     */
    int ordinaryUserConfirmCost(LeaseSettlementDto data);

    Long findByCollectionNumber(@Param("collectionNumber") Long collectionNumber);

    int updateByCollectionId(ReckoningInfo param);

}
