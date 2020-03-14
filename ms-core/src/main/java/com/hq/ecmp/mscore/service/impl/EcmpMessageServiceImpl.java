package com.hq.ecmp.mscore.service.impl;
import java.util.*;

import com.hq.common.exception.BaseException;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.MsgConstant;
import com.hq.ecmp.constant.MsgStatusConstant;
import com.hq.ecmp.constant.MsgTypeConstant;
import com.hq.ecmp.constant.MsgUserConstant;
import com.hq.ecmp.mscore.domain.EcmpMessage;
import com.hq.ecmp.mscore.mapper.EcmpMessageMapper;
import com.hq.ecmp.mscore.service.EcmpMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * (EcmpMessage)表服务实现类
 *
 * @author makejava
 * @since 2020-03-13 15:25:47
 */
@Service("ecmpMessageService")
public class EcmpMessageServiceImpl implements EcmpMessageService {
    @Resource
    private EcmpMessageMapper ecmpMessageDao;

    @Autowired
    private TokenService tokenService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EcmpMessage queryById(Long id) {
        return this.ecmpMessageDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<EcmpMessage> queryAllByLimit(int offset, int limit) {
        return this.ecmpMessageDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param ecmpMessage 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpMessage insert(EcmpMessage ecmpMessage) {
        this.ecmpMessageDao.insert(ecmpMessage);
        return ecmpMessage;
    }

    /**
     * 修改数据
     *
     * @param ecmpMessage 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpMessage update(EcmpMessage ecmpMessage) {
        this.ecmpMessageDao.update(ecmpMessage);
        return this.queryById(ecmpMessage.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.ecmpMessageDao.deleteById(id) > 0;
    }

    /**
     * 插入一条消息
     * @param msgConstant
     * @param userConstant
     * @param ecmpId
     * @param url
     */
    @Override
    public void insertMessage(MsgConstant msgConstant, MsgTypeConstant type, MsgUserConstant userConstant, String content,Long ecmpId, String url) {
        EcmpMessage ecmpMessage = EcmpMessage.builder()
                .configType(userConstant.getType())
                .ecmpId(ecmpId)
                .status(MsgStatusConstant.MESSAGE_STATUS_T002.getType())
                .type(type.getType())
                .category(msgConstant.getDesp())
                .createTime(new Date())
                .content(content)
                .createBy(ecmpId)
                .url(url)
                .build();
        ecmpMessageDao.insert(ecmpMessage);
    }

    @Override
    public void insertMessage(MsgConstant msgConstant, MsgTypeConstant type, MsgUserConstant userConstant,String content, Long ecmpId) {
        insertMessage(msgConstant,type,userConstant,content,ecmpId,"");
    }

    @Override
    public List<EcmpMessage> selectMessageList(String identity) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long ecmpId;
        List<Integer> configTypeList = new ArrayList();
        switch (identity){
            //乘客
            case "1":
                ecmpId = loginUser.getUser().getUserId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_APPROVAL.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_USER.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DISPATCHER.getType());
                break;
            //司机
            case "2":
                ecmpId = loginUser.getDriver().getDriverId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DRIVER.getType());
                break;
            default:
                throw new BaseException("错误信息");
        }
        Map map = new HashMap();
        map.put("configTypeList",configTypeList);
        map.put("ecmpId",ecmpId);
        return ecmpMessageDao.queryMessageList(map);
    }


    @Override
    public int getMessagesCount(String identity) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long ecmpId;
        List<Integer> configTypeList = new ArrayList();
        switch (identity){
            //乘客
            case "1":
                ecmpId = loginUser.getUser().getUserId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_APPROVAL.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_USER.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DISPATCHER.getType());
                break;
            //司机
            case "2":
                ecmpId = loginUser.getDriver().getDriverId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DRIVER.getType());
                break;
            default:
                throw new BaseException("错误信息");
        }
        Map map = new HashMap();
        map.put("configTypeList",configTypeList);
        map.put("ecmpId",ecmpId);
        return ecmpMessageDao.queryMessageCount(map);
    }
}