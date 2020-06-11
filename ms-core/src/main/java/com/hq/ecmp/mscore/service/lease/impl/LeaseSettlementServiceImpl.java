package com.hq.ecmp.mscore.service.lease.impl;

import com.github.pagehelper.PageInfo;
import com.hq.api.system.domain.SysRole;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.mscore.dto.OrderInfoFSDto;
import com.hq.ecmp.mscore.dto.ReckoningDto;
import com.hq.ecmp.mscore.dto.lease.LeaseSettlementDto;
import com.hq.ecmp.mscore.mapper.CarGroupInfoMapper;
import com.hq.ecmp.mscore.mapper.CollectionQuittanceInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderAccountInfoMapper;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.service.CollectionQuittanceInfoService;
import com.hq.ecmp.mscore.service.lease.LeaseSettlementService;
import com.hq.ecmp.mscore.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;


@Service
@Slf4j
public class LeaseSettlementServiceImpl implements LeaseSettlementService {


    @Autowired
    private CollectionQuittanceInfoMapper collectionQuittanceInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private OrderAccountInfoMapper orderAccountInfoMapper;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;
    @Autowired
    private CollectionQuittanceInfoService collectionQuittanceInfoService;
    /***
     *结算单列表（调度员，外部员工，区分角色查询）
     * add by liuzb
     * @param data
     * @return
     * @throws Exception
     */
    @Override
    public PageResult<LeaseSettlementDto> getOrdinaryUserList(LeaseSettlementDto data, LoginUser user) throws Exception {
        String roleKey = isRole(user);
        if(!("admin".equals(roleKey) || "C000".equals(roleKey))){
            data.setCompanyId(user.getUser().getOwnerCompany());
        }
        List<LeaseSettlementDto> list = collectionQuittanceInfoMapper.getOrdinaryUserList(data);
        PageInfo<LeaseSettlementDto> info = new PageInfo<>(list);
        return new PageResult<>(info.getTotal(),info.getPages(),list);
    }

    /***
     * 当前登陆人的角色
     * add by liuzb
     * @param user
     * @return
     */
    private String isRole(LoginUser user){
        if(isAdmin(user)){
            return "admin";
        }
        List<SysRole> roleList = user.getUser().getRoles();
        for(SysRole data : roleList ){
            /**调度员（区分内部调度，外部调度）*/
            if("dispatcher".equals(data.getRoleKey())){
                List<String> list = carGroupInfoMapper.selectIsDispatcher(user.getUser().getUserId());
                for(String str :list){
                    if("C000".equals(str)){
                        return "C000";
                    }
                    if("C111".equals(str)){
                        return "C111";
                    }
                }
            }
        }
        return null;
    }

    /***
     * 当前角色有超管
     * add by liuzb
     * @param user
     * @return
     */
    private boolean  isAdmin(LoginUser user){
        List<SysRole> roleList = user.getUser().getRoles();
        for(SysRole data : roleList ) {
            if("admin".equals(data.getRoleKey()) || "sub_admin".equals(data.getRoleKey())){
                return true;
            }
        }
        return false;
    }

    /***
     *单个结算单下的所有订单，车辆信息详情
     * add by liuzb
     * @param collectionId
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> getOrdinaryUserDetail(Long collectionId) throws Exception {
        LeaseSettlementDto data = collectionQuittanceInfoMapper.getOrdinaryUserById(collectionId);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ReckoningDto param = new ReckoningDto();
        param.setCompanyId(data.getCompanyId());
        param.setStartDate(formatter.format(data.getBeginDate()));
        param.setEndDate(formatter.format(data.getEndDate()));
        return  collectionQuittanceInfoService.reckoningDetail(param);
    }

    /***
     *确认结算单
     * @param collectionId
     * @return
     * @throws Exception
     */
    public int ordinaryUserConfirmCost(Long collectionId,Long userId) throws Exception {
        orderAccountInfoMapper.updateStatementsState(getSettlementOrder(collectionId,userId));
        if(updateSettlementState(collectionId,userId)>0){
           return 1;
        }
        return 0;
    }

    /***
     * 确认结算单，通过结算单日期获取所有订单
     * add by liuzb
     * @param collectionId
     * @return
     * @throws Exception
     */
    private List<OrderInfoFSDto> getSettlementOrder(Long collectionId,Long userId)throws Exception{
        LeaseSettlementDto data = collectionQuittanceInfoMapper.getOrdinaryUserById(collectionId);
        if(null!=data){
            List<String> list = orderInfoMapper.getStatementsList(data);
            if(null!=list && list.size()>0){
                  List<OrderInfoFSDto> updateList = new ArrayList<>();
                  for(String orderId : list ){
                      OrderInfoFSDto updateData = new OrderInfoFSDto();
                      updateData.setUpdateBy(userId);
                      updateData.setOrderId(Long.valueOf(orderId));
                      updateList.add(updateData);
                  }
                  return updateList;
            }

        }
        return null;
    }

    /***
     * 更新结算单状态
     * add by liuzb
     * @param collectionId
     * @param userId
     * @return
     */
    private int updateSettlementState(Long collectionId,Long userId){
        if(null!=userId && !"".equals(userId) && null!=collectionId && !"".equals(collectionId)){
            LeaseSettlementDto data = new LeaseSettlementDto();
            data.setCollectionId(collectionId);
            data.setVerifier(userId);
            data.setCreateTime(new Date());
            data.setState("S100");
            return  collectionQuittanceInfoMapper.ordinaryUserConfirmCost(data);
        }
        return 0;
    }

    /***
     *单个结算下的所有订单，车辆信息详情下载
     * add by liuzb
     * @param collectionId
     * @throws Exception
     */
    @Override
    public void downloadOrdinaryUserDetail(Long collectionId) throws Exception {

    }


    /**
     * 租赁结算用车单位列表
     * add by liuzb
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public List<String> getUseCarUnit(LoginUser user) throws Exception {
        return carGroupInfoMapper.getCarGroupAllName();
    }


    /***
     * 调度员更新结算单状态
     * add by liuzb
     * @param
     * @param user
     * @return
     */
    @Override
    public int updateLeaseSettlementState(Long collectionId,String state, LoginUser user) {
        LeaseSettlementDto data = new LeaseSettlementDto();
        data.setCollectionId(collectionId);
        data.setUpdateBy(user.getUser().getUserId());
        data.setUpdateTime(new Date());
        data.setState(state);
        return  collectionQuittanceInfoMapper.ordinaryUserConfirmCost(data);
    }
}
