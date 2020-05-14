package com.hq.ecmp.ms.api.controller.im;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.IMConStant;
import com.hq.ecmp.ms.api.dto.im.IMMsgDto;
import com.hq.ecmp.mscore.domain.ImMessage;
import com.hq.ecmp.mscore.dto.IMMsgStatusDto;
import com.hq.ecmp.mscore.dto.IMQueryMsgDto;
import com.hq.ecmp.mscore.service.ImMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author crk
 */
@RestController
@RequestMapping("/im")
@Api(description = "即时通讯信息")
public class IMMsgController {

    @Autowired
    private ImMessageService imMessageService;

    /**
     * 保存信息
     *
     * @param imMsgDto
     * @return
     */
    @ApiOperation(value = "saveMsg", notes = "保存信息", httpMethod = "POST")
    @PostMapping("/saveMsg")
    public ApiResponse saveMsg(IMMsgDto imMsgDto) {
        ImMessage imMessage = ImMessage.builder()
                .createTime(new Date())
                .centext(imMsgDto.getContent())
                .sendRoleType(imMsgDto.getSendRoleType())
                .sendId(imMsgDto.getSendId())
                .receiveRoleType(imMsgDto.getReceiveRoleType())
                .receiveId(imMsgDto.getReceiveId())
                .status(IMConStant.MSG_UNREAD_STATUS.getStatus())
                .build();
        imMessage = imMessageService.insert(imMessage);
        return ApiResponse.success(imMessage.getId());
    }

    /**
     * 获取消息记录
     *
     * @param
     * @return
     */
    @ApiOperation(value = "getMsgs", notes = "获取消息记录", httpMethod = "POST")
    @PostMapping("/getMsgs")
    public ApiResponse getMsgs(@RequestBody IMQueryMsgDto queryMsgDto) {

        List<Map<String, List<ImMessage>>> result = new ArrayList<>();
        List<ImMessage> sendMsgs = imMessageService.queryMsgInfo(queryMsgDto);
        Date nowTime = new Date();
        Date laseMsgTime = null;
        for (ImMessage sendMsg : sendMsgs) {
            if (sendMsg.getSendId().equals(queryMsgDto.getSendId()) && sendMsg.getSendRoleType().equals(queryMsgDto.getSendRoleType())) {
                sendMsg.setIdentity(IMConStant.MSG_IDENTITY_MY.getStatus());
            } else {
                sendMsg.setIdentity(IMConStant.MSG_IDENTITY_YOUR.getStatus());
            }
            Date msgCreateTime = sendMsg.getCreateTime();
            if (laseMsgTime == null) {
                laseMsgTime = msgCreateTime;
                Map<String, List<ImMessage>> map = new HashMap<>(8);
                List<ImMessage> list = new ArrayList<>();
                list.add(sendMsg);
                map.put(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, laseMsgTime), list);
                result.add(map);
            } else {
                int betweenTime = (int) ((msgCreateTime.getTime() - laseMsgTime.getTime()) / (1000 * 60 * 60));
                if (betweenTime <= 1) {
                    Map<String, List<ImMessage>> map = result.get(result.size() - 1);
                    List<ImMessage> list = map.get(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, laseMsgTime));
                    list.add(sendMsg);
                } else {
                    laseMsgTime = msgCreateTime;
                    Map<String, List<ImMessage>> map = new HashMap<>(8);
                    List<ImMessage> list = new ArrayList<>();
                    list.add(sendMsg);
                    map.put(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, laseMsgTime), list);
                    result.add(map);
                }
            }
        }
        return ApiResponse.success(result);
    }

    /**
     * 更改单个信息为以读
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "updateMsgStatus", notes = "更改单个信息为以读", httpMethod = "POST")
    @PostMapping("/updateMsgStatus")
    public ApiResponse updateMsgStatus(Long id) {
        ImMessage imMessage = ImMessage.builder().id(id).status(1).build();
        imMessage = imMessageService.update(imMessage);
        return ApiResponse.success(imMessage.getId());
    }

    /**
     * 将信息设置成已失效
     *
     * @param msgStatusDto
     * @return
     */
    @ApiOperation(value = "updateMsgStatus", notes = "将信息设置成已失效", httpMethod = "POST")
    @PostMapping("/updateMsgFail")
    public ApiResponse updateMsgStatus(@RequestBody IMMsgStatusDto msgStatusDto) {
        msgStatusDto.setStatus(IMConStant.MSG_FAIL_READ_STATUS.getStatus());
        imMessageService.updateMsgFail(msgStatusDto);
        return ApiResponse.success();
    }
    /**
     * 查询单个信息状态
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "queryMsgStatus", notes = "查询单个信息状态", httpMethod = "POST")
    @PostMapping("/queryMsgStatus")
    public ApiResponse queryMsgStatus(Long id) {
        ImMessage imMessage = imMessageService.queryById(id);
        return ApiResponse.success(imMessage);
    }

    /**
     * 查询所有未读信息数量
     *
     * @return
     */
    @ApiOperation(value = "queryAllMsgs", notes = "查询所有未读信息数量", httpMethod = "POST")
    @PostMapping("/queryAllUnReadCount")
    public ApiResponse queryAllUnReadCount(int sendRoleType, int sendId) {
        Map map = new HashMap(8);
        map.put("sendRoleType", sendRoleType);
        map.put("sendId", sendId);
        map.put("status", IMConStant.MSG_UNREAD_STATUS.getStatus());
        int count = imMessageService.queryAllMsgConutBy(map);
        return ApiResponse.success(count);
    }

    /**
     * 查询单个对话框未读信息
     *
     * @return
     */
    @ApiOperation(value = "querySingleUnReadMsgs", notes = "查询单个某个对话框未读信息数量", httpMethod = "POST")
    @PostMapping("/querySingleUnReadCount")
    public ApiResponse querySingleUnReadCount(int sendRoleType, int sendId, int receiveRoleType, int receiveId) {
        Map map = new HashMap(8);
        map.put("sendRoleType", sendRoleType);
        map.put("sendId", sendId);
        map.put("receiveRoleType", receiveRoleType);
        map.put("receiveId", receiveId);
        map.put("status", 0);
        int count = imMessageService.queryMsgConutBy(map);
        return ApiResponse.success(count);
    }

}
