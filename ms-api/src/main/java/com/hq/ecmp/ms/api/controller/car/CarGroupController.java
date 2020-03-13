package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.dto.CarGroupDTO;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/12 10:24
 */
@RestController
@RequestMapping("/cargroup")
public class CarGroupController {

    @Autowired
    private ICarGroupInfoService carGroupInfoService;
    @Autowired
    private TokenService tokenService;

    /**
     * 新增车队
     * @param  carGroupDTO 车队信息
     * @return
     */
    @ApiOperation(value = "saveCarGroup",notes = "新增车队",httpMethod ="POST")
    @PostMapping("/saveCarGroup")
    public ApiResponse saveCarGroup(@RequestBody CarGroupDTO carGroupDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        CarGroupInfo carGroupInfo = new CarGroupInfo();
        try {
            //车队编码
            carGroupInfo.setCarGroupCode(carGroupDTO.getCarGroupCode());
            //所属城市编码
            carGroupInfo.setCity(carGroupDTO.getCity());
            //所属城市名字
            carGroupInfo.setCityName(carGroupDTO.getCityName());
            //车队名称
            carGroupInfo.setCarGroupName(carGroupDTO.getCarGroupName());
            //车队详细地址
            carGroupInfo.setFullAddress(carGroupDTO.getFullAddress());
            //车队短地址
            carGroupInfo.setShortAddress(carGroupDTO.getShortAddress());
            //所属组织
            carGroupInfo.setOwnerOrg(carGroupDTO.getOwnerOrg());
            //车队负责人
            carGroupInfo.setLeader(carGroupDTO.getLeader());
            //创建人
            carGroupInfo.setCreateBy(String.valueOf(userId));
            int i = carGroupInfoService.insertCarGroupInfo(carGroupInfo);
            if(i != 1){
                return ApiResponse.error("保存车队信息失敗");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("保存车队信息失败");
        }
        return ApiResponse.success("保存成功");
    }
}
