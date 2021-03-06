package com.hq.ecmp.mscore.service;

import com.hq.api.system.domain.SysUser;
import com.hq.core.security.LoginUser;
import com.hq.ecmp.constant.MsgConstant;
import com.hq.ecmp.constant.MsgTypeConstant;
import com.hq.ecmp.constant.MsgUserConstant;
import com.hq.ecmp.mscore.domain.ApplyInfo;
import com.hq.ecmp.mscore.domain.EcmpMessage;
import com.hq.ecmp.mscore.dto.MessageDto;

import java.util.List;

/**
 * (EcmpMessage)表服务接口
 *
 * @author makejava
 * @since 2020-03-13 15:25:47
 */
public interface EcmpMessageService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    EcmpMessage queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EcmpMessage> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param ecmpMessage 实例对象
     * @return 实例对象
     */
    EcmpMessage insert(EcmpMessage ecmpMessage);

    /**
     * 修改数据
     *
     * @param ecmpMessage 实例对象
     * @return 实例对象
     */
    EcmpMessage update(EcmpMessage ecmpMessage);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    /**
     * 插入一条消息
     */
    void insertMessage(MsgConstant msgConstant, MsgTypeConstant type, MsgUserConstant userConstant,String content, Long ecmpId, String url);

    /**
     * 插入一条消息
     */
    void insertMessage(MsgConstant msgConstant, MsgTypeConstant type, MsgUserConstant userConstant, String content,Long ecmpId);


    List<EcmpMessage> selectMessageList(String identity);


    int getMessagesCount(String identity);

    List<MessageDto> getMessagesForPassenger(SysUser user)throws Exception ;

    List<MessageDto> getRunMessageForDrive(LoginUser user)throws Exception ;

    void saveApplyMessagePass(ApplyInfo applyInfo, Long userId, Long orderId, Long powerId, int isDispatch) throws Exception;
    void applyUserPassMessage(Long applyId,Long ecmpId,Long userId,Long orderId,Long powerId,int isDispatch) throws Exception;
    void saveApplyMessageReject(Long applyId,Long ecmpId,Long userId,String reson) throws Exception;

    void sendNextApproveUsers(String approveUserId,Long applyId,Long userId);
    /**阅读消息**/
    void readMessage(MessageDto messageDto, LoginUser user);
    /**消息通知插入**/
    void saveMessageUnite(LoginUser loginUser,Long orderId, MsgConstant msgConstant) throws Exception;
    /**专发调度员通知*/
    void sendDispatcherMessage(Long orderId,Long dispatchId,Long userId,MsgConstant msgConstant)throws Exception;
}