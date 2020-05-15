package com.hq.ecmp.mscore.service.impl;

import com.hq.core.sms.service.ISmsTemplateInfoService;
import com.hq.ecmp.mscore.domain.DispatchOrderInfo;
import com.hq.ecmp.mscore.domain.OrderInfoMessage;
import com.hq.ecmp.mscore.domain.UserEmergencyContactInfo;
import com.hq.ecmp.mscore.mapper.OrderInfoMapper;
import com.hq.ecmp.mscore.mapper.UserEmergencyContactInfoMapper;
import com.hq.ecmp.mscore.service.UserEmergencyContactInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户紧急联系人信息表(UserEmergencyContactInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-03-09 23:58:22
 */
@Service("userEmergencyContactInfoService")
public class UserEmergencyContactInfoServiceImpl implements UserEmergencyContactInfoService {

    private static final Logger logger = LoggerFactory.getLogger(UserEmergencyContactInfoServiceImpl.class);


    /***
     * 一键报警发给自己的短信code码
     */
    public static final String PERSONAL_ALARM_MESSAGE = "personal_alarm_message";
    /***
     * 一键报警发给当前联系人的短信code码
     */
    public static final String CONTACT_PERSON_ALARM_MESSAGE = "contact_person_alarm_message";
    @Resource
    private UserEmergencyContactInfoMapper userEmergencyContactInfoDao;

    @Autowired
    private ISmsTemplateInfoService smsTemplateInfoService;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public UserEmergencyContactInfo queryById(Long id) {
        return this.userEmergencyContactInfoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<UserEmergencyContactInfo> queryAllByLimit(int offset, int limit) {
        return this.userEmergencyContactInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param userEmergencyContactInfo 实例对象
     * @return 实例对象
     */
    @Override
    public UserEmergencyContactInfo insert(UserEmergencyContactInfo userEmergencyContactInfo) {
        this.userEmergencyContactInfoDao.insert(userEmergencyContactInfo);
        return userEmergencyContactInfo;
    }

    /**
     * 修改数据
     *
     * @param userEmergencyContactInfo 实例对象
     * @return 实例对象
     */
    @Override
    public UserEmergencyContactInfo update(UserEmergencyContactInfo userEmergencyContactInfo) {
        this.userEmergencyContactInfoDao.update(userEmergencyContactInfo);
        return this.queryById(userEmergencyContactInfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.userEmergencyContactInfoDao.deleteById(id) > 0;
    }

    /**
     * 通过userId查询数据
     *
     * @param userId
     * @return 是否成功
     */
    @Override
    public List<UserEmergencyContactInfo> queryByUserId(Long userId) {
        return this.userEmergencyContactInfoDao.queryByUserId(userId);
    }

    /***
     *  当前订单信息发送安全短信（所有联系人）
     * @param userId
     * @param orderId
     * @throws Exception
     */
    @Override
    public String callThepolice(Long userId,String phonenumber, Long orderId,String address) throws Exception {
        if(null!=userId && null!=orderId){
            OrderInfoMessage orderInfoMessage = orderInfoMapper.getCarMessage(orderId);//乘车信息
            List<UserEmergencyContactInfo>  list =  userEmergencyContactInfoDao.queryByUserId(userId);//紧急联系人
            /**发短信*/
            sendMyMessage(orderInfoMessage,address,phonenumber);
            sendContactPersonMessage(orderInfoMessage,list,address,phonenumber);
            return "发送短信成功";
        }
        return null;
    }

    /***
     * 意见报警给自己发送短信
     * @param orderInfoMessage（乘车信息）
     * @param address（当前订单位置地址）
     * @param phonenumber（当前订单乘客电话）
     */
    private void sendMyMessage(OrderInfoMessage orderInfoMessage, String address,String phonenumber){
        /***
         * 这里抓住异常，即使给自己发送短信失败，也要保证给紧急联系人发送
         */
        try{
            Map<String,String> map = new HashMap<>();
            map.put("address",address);
            map.put("carLicense",orderInfoMessage.getCarLicense());
            map.put("carColor",orderInfoMessage.getCarColor());
            smsTemplateInfoService.sendSms(PERSONAL_ALARM_MESSAGE,map,phonenumber);
        }catch(Exception e){
            logger.error("sendMyMessage error",e);
        }
    }


    /***
     * 一键报警给紧急联系人发送短信
     * @param orderInfoMessage（乘车信息）
     * @param list（联系人列表）
     * @param address（当前订单位置地址）
     * @param phonenumber（当前订单乘客电话）
     */
    private void sendContactPersonMessage(OrderInfoMessage orderInfoMessage,List<UserEmergencyContactInfo>  list, String address,String phonenumber){
        if(null!=list && list.size()>0){
            for(UserEmergencyContactInfo data : list ){
                /***
                 * 这里为多条联系人，不能当前一个联系人发送短信失败程序终止，即使程序异常，也要把所有联系人短信发送完成
                 */
                try{
                    Map<String,String> map = new HashMap<>();
                    map.put("address",address);map.put("phonenumber",phonenumber);
                    map.put("carLicense",orderInfoMessage.getCarLicense());
                    map.put("carColor",orderInfoMessage.getCarColor());
                    smsTemplateInfoService.sendSms(CONTACT_PERSON_ALARM_MESSAGE,map,data.getMobile());
                }catch(Exception e){
                    logger.error("contactPerson "+data.getMobile()+"sendContactPersonMessage error",e);
                }
            }
        }
    }

}