package com.hq.ecmp.mscore.service.lease.impl;

import com.github.pagehelper.PageInfo;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.dto.OrderInfoFSDto;
import com.hq.ecmp.mscore.dto.lease.LeaseSettlementDto;
import com.hq.ecmp.mscore.mapper.CollectionQuittanceInfoMapper;
import com.hq.ecmp.mscore.service.lease.LeaseSettlementService;
import com.hq.ecmp.mscore.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class LeaseSettlementServiceImpl implements LeaseSettlementService {


    @Autowired
    private CollectionQuittanceInfoMapper collectionQuittanceInfoMapper;

    /***
     *
     * @param data
     * @return
     * @throws Exception
     */
    @Override
    public PageResult<LeaseSettlementDto> getOrdinaryUserList(LeaseSettlementDto data, LoginUser user) throws Exception {
        data.setCompanyId(user.getUser().getOwnerCompany());
        List<LeaseSettlementDto> list = collectionQuittanceInfoMapper.getOrdinaryUserList(data);
        PageInfo<LeaseSettlementDto> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),info.getPages(),list);
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
    public int ordinaryUserConfirmCost(Long collectionId,Long userId) throws Exception {
        if(null!=userId && !"".equals(userId) && null!=collectionId && !"".equals(collectionId)){
            LeaseSettlementDto data = new LeaseSettlementDto();
            data.setCollectionId(collectionId);
            data.setCreateBy(userId);
            data.setCreateTim(new Date());
            data.setState("S100");
            return  collectionQuittanceInfoMapper.ordinaryUserConfirmCost(data);
        }
        return 0;
    }


    /***
     *
     * @param collectionId
     * @throws Exception
     */
    @Override
    public void downloadOrdinaryUserDetail(Long collectionId) throws Exception {

    }
}
