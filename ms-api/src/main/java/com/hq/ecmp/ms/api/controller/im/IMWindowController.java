package com.hq.ecmp.ms.api.controller.im;

import com.hq.common.core.api.ApiResponse;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.ms.api.dto.im.IMBaseDto;
import com.hq.ecmp.mscore.domain.DriverInfo;
import com.hq.ecmp.mscore.domain.EcmpUser;
import com.hq.ecmp.mscore.domain.ImWindow;
import com.hq.ecmp.mscore.service.IDriverInfoService;
import com.hq.ecmp.mscore.service.IEcmpUserService;
import com.hq.ecmp.mscore.service.ImMessageService;
import com.hq.ecmp.mscore.service.ImWindowService;
import com.hq.ecmp.mscore.vo.IMWindowVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/imWindow")
@Api(tags = "即时通讯窗口")
public class IMWindowController {

    @Autowired
    private ImWindowService imWindowService;
    @Autowired
    private IDriverInfoService driverService;
    @Autowired
    private IEcmpUserService userService;
    @Autowired
    private ImMessageService imMessageService;
    /**
     * 新增对话框
     * @param  imWindowDto
     * @return
     */
    @ApiOperation(value = "saveWindow",notes = "新建窗口",httpMethod ="POST")
    @PostMapping("/saveWindow")
    public ApiResponse saveMsg(@RequestBody IMBaseDto imWindowDto){
        ImWindow imwindow = ImWindow.builder()
                .createTime(new Date())
                .sendRoleType(imWindowDto.getSendRoleType())
                .sendId(imWindowDto.getSendId())
                .receiveRoleType(imWindowDto.getReceiveRoleType())
                .receiveId(imWindowDto.getReceiveId())
                .status(CommonConstant.ZERO)
                .build();
        imwindow = imWindowService.insert(imwindow);
        return ApiResponse.success(imwindow.getId());
    }

    /**
     * 获取窗口列表
     * @return
     */
    @ApiOperation(value = "getWindow",notes = "获取窗口列表",httpMethod ="POST")
    @PostMapping("/getWindow")
    public ApiResponse getWindow(int sendRoleType,long sendId){
        ImWindow imwindow = ImWindow.builder()
                .sendRoleType(sendRoleType)
                .sendId(sendId)
                .status(CommonConstant.ZERO)
                .build();
        List<ImWindow> windows = imWindowService.queryWindowsBySend(imwindow);

        List<IMWindowVO> list = new ArrayList<>();
        for (ImWindow window : windows) {
            IMWindowVO imWindowVO = new IMWindowVO();
            BeanUtils.copyProperties(window,imWindowVO);
            int receiveRoleType = window.getReceiveRoleType();
            if(receiveRoleType == 1){
                EcmpUser user =  userService.selectEcmpUserById(window.getReceiveId());
                imWindowVO.setReceiveName(user.getUserName());
            }else{
                DriverInfo driverInfo = driverService.selectDriverInfoById(window.getReceiveId());
                imWindowVO.setReceiveName(driverInfo.getDriverName());
            }
            Map map = new HashMap();
            map.put("sendRoleType",sendRoleType);
            map.put("sendId",sendId);
            map.put("receiveRoleType",receiveRoleType);
            map.put("receiveId",window.getReceiveId());
            map.put("status",CommonConstant.ZERO);
            int count = imMessageService.queryMsgConutBy(map);
            imWindowVO.setUnReadCount(count);
            list.add(imWindowVO);
        }
        return ApiResponse.success(list);
    }

    /**
     * 移除对话框
     * @return
     */
    @ApiOperation(value = "removeWindow",notes = "移除对话框",httpMethod ="POST")
    @PostMapping("/removeWindow")
    public ApiResponse getWindow(long id) throws Exception {
        imWindowService.deleteById(id);
        return ApiResponse.success();
    }
}
