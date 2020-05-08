package com.hq.ecmp.ms.api.controller.car;

import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.annotation.Log;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.ms.api.dto.order.OrderDto;
import com.hq.ecmp.mscore.domain.CarGroupInfo;
import com.hq.ecmp.mscore.domain.CarInfo;
import com.hq.ecmp.mscore.dto.*;
import com.hq.ecmp.mscore.service.ICarGroupInfoService;
import com.hq.ecmp.mscore.service.IEcmpOrgService;
import com.hq.ecmp.mscore.vo.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: chao.zhang
 * @Date: 2020/3/12 10:24
 */
@RestController
@RequestMapping("/cargroup")
@Slf4j
public class CarGroupController {

    @Autowired
    private ICarGroupInfoService carGroupInfoService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IEcmpOrgService ecmpOrgService;

    /**
     * 新增车队
     * @param  carGroupDTO 车队信息
     * @return
     */
    @Log(title = "车队管理", content = "新增车队",businessType = BusinessType.INSERT)
    @ApiOperation(value = "saveCarGroup",notes = "新增车队",httpMethod ="POST")
    @PostMapping("/saveCarGroup")
    public ApiResponse saveCarGroup(@RequestBody CarGroupDTO carGroupDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
          carGroupInfoService.saveCarGroupAndDispatcher(carGroupDTO,userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("保存车队信息失败");
        }
        return ApiResponse.success("保存成功");
    }

    /**
     * 车队详情
     * @param
     * @return
     */
    @Log(title = "车队管理",content = "车队详情", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCarGroupDetail",notes = "车队详情",httpMethod ="POST")
    @PostMapping("/getCarGroupDetail")
    public ApiResponse<CarGroupDetailVO> getCarGroupDetail(@RequestBody CarGroupDTO carGroupDTO){
        try {
            CarGroupDetailVO carGroupDetailVO = carGroupInfoService.getCarGroupDetail(carGroupDTO.getCarGroupId());
            return ApiResponse.success(carGroupDetailVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询车队详情失败");
        }
    }

    /**
     * 修改车队
     * @param
     * @return
     */
    @Log(title = "车队管理",content = "修改车队", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "updateCarGroup",notes = "修改车队",httpMethod ="POST")
    @PostMapping("/updateCarGroup")
    public ApiResponse updateCarGroup(@RequestBody CarGroupDTO carGroupDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carGroupInfoService.updateCarGroup(carGroupDTO,userId);
            return ApiResponse.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("修改车队失败");
        }
    }

    /**
     * 删除车队
     * @param
     * @return
     */
    @Log(title = "车队管理",content = "删除车队", businessType = BusinessType.DELETE)
    @ApiOperation(value = "deleteCarGroup",notes = "删除车队",httpMethod ="POST")
    @PostMapping("/deleteCarGroup")
    public ApiResponse deleteCarGroup(@RequestBody CarGroupDTO carGroupDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            String rtnMsg = carGroupInfoService.deleteCarGroup(carGroupDTO.getCarGroupId(),userId);
            return ApiResponse.success(rtnMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 禁用车队
     * @param
     * @return
     */
    @Log(title = "车队管理",content = "禁用车队", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "disableCarGroup",notes = "禁用车队",httpMethod ="POST")
    @PostMapping("/disableCarGroup")
    public ApiResponse disableCarGroup(@RequestBody Long carGroupId){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carGroupInfoService.disableCarGroup(carGroupId,userId);
            return ApiResponse.success("禁用车队成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 启用车队
     * @param
     * @return
     */
    @Log(title = "车队管理",content = "启用车队", businessType = BusinessType.OTHER)
    @ApiOperation(value = "startUpCarGroup",notes = "启用车队",httpMethod ="POST")
    @PostMapping("/startUpCarGroup")
    public ApiResponse startUpCarGroup(@RequestBody Long carGroupId){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getUser().getUserId();
        try {
            carGroupInfoService.startUpCarGroup(carGroupId,userId);
            return ApiResponse.success("启用车队成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("启用车队失败");
        }
    }

    /**
     * 分页全部查询车队列表（带搜索功能）
     * @param
     * @return
     */
    @Log(title = "车队管理",content = "车队列表", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCarGroupList",notes = "分页全部查询车队列表",httpMethod ="POST")
    @PostMapping("/getCarGroupList")
    public ApiResponse<PageResult<CarGroupListVO>> getCarGroupList(@RequestBody PageRequest pageRequest){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId = loginUser.getUser().getOwnerCompany();
        try {
            PageResult<CarGroupListVO> list = carGroupInfoService.selectCarGroupInfoByPage(pageRequest.getPageNum(),
                    pageRequest.getPageSize(),pageRequest.getSearch(),
                    pageRequest.getState(),pageRequest.getDeptId(),pageRequest.getCarGroupId(),companyId);
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询车队列表失败");
        }
    }

    /**
     * 查询下级车队列表
     * @param
     * @return
     */
   /* @ApiOperation(value = "getSubCarGroupList",notes = "启用车队",httpMethod ="POST")
    @PostMapping("/getSubCarGroupList")
    public ApiResponse<List<CarGroupListVO>> getSubCarGroupList(@RequestBody SubGroupListDTO subGroupListDTO){
        try {
            List<CarGroupListVO> list = carGroupInfoService.selectSubCarGroupInfoList(subGroupListDTO.getDeptId());
            return ApiResponse.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询下级车队列表失败");
        }
    }*/

    /**
     * 查询指定城市所有车队调度员及车队座机
     * 不传城市code 则会根据用户角色（司机/用户都行）查询所在（分子）公司所有车队调度员及车队座机
     * @param
     * @return
     */
   /* @Log(title = "车队管理",content = "获取城市车队联系电话", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCarGroupPhone",notes = "查询登录用户（司机/用户均可）所在公司所有车队联系电话(可传城市代码特定查某城市车队)",httpMethod ="POST")
    @PostMapping("/getCarGroupPhone")
    public ApiResponse<List<CarGroupPhoneVO>> getCarGroupPhone(@RequestBody(required = false) CarGroupPhoneDTO carGroupPhoneDTO){
        List<CarGroupPhoneVO>  list = null;
        try {
            if (carGroupPhoneDTO == null){
                carGroupPhoneDTO = new CarGroupPhoneDTO();
            }
            list = carGroupInfoService.getCarGroupPhone(carGroupPhoneDTO.getCityCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(list);
    }*/

    /**
     * 查询司机所属 车队调度员及车队座机
     * @param
     * @return
     */
   /* @Log(title = "车队管理",content = "查询司机所属车队联系电话", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getOwnerCarGroupPhone",notes = "查询车队联系电话",httpMethod ="POST")
    @PostMapping("/getOwnerCarGroupPhone")
    public ApiResponse<CarGroupPhoneVO> getOwnerCarGroupPhone(){
        CarGroupPhoneVO  carGroupPhoneVO = null;
        try {
            carGroupPhoneVO = carGroupInfoService.getOwnerCarGroupPhone();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
        return ApiResponse.success(carGroupPhoneVO);
    }*/

    /**
     * 查询调度员电话机和调度员所在车队座机
     * @param
     * @return
     */
    /*@Log(title = "车队管理",content = "查询订单调度员所在车队电话", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getDispatcherAndFixedLine",notes = "查询调度员电话机和调度员所在车队座机",httpMethod ="POST")
    @PostMapping("/getDispatcherAndFixedLine")
    public ApiResponse<DispatcherAndFixedLineVO> getDispatcherAndFixedLine(@RequestBody DispatcherAndFixedLineDTO
                                                                           dispatcherAndFixedLineDTO){
        DispatcherAndFixedLineVO  vo = null;
        try {
             vo = carGroupInfoService.getDispatcherAndFixedLine(dispatcherAndFixedLineDTO.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(vo);
    }*/

    /**
     * 联系车队 （乘客（有无订单、司机（有无订单） 4种情况通用）
     * @param
     * @return
     */
    @Log(title = "车队管理",content = "联系车队(通用)", businessType = BusinessType.OTHER)
    @ApiOperation(value = "cantactCarGroup",notes = "联系车队",httpMethod ="POST")
    @PostMapping("/cantactCarGroup")
    public ApiResponse<List<ContactCarGroupVO>> cantactCarGroup(@RequestBody(required = false) CarGroupPhoneDTO carGroupPhoneDTO){
        List<ContactCarGroupVO>  list = null;
        try {
            if (carGroupPhoneDTO == null){
                carGroupPhoneDTO = new CarGroupPhoneDTO();
            }
            list = carGroupInfoService.cantactCarGroup(carGroupPhoneDTO.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(list);
    }



   /**
     * 查询公司车队树
     * @param
     * @return
     */
   /* @Log(title = "车队管理", content = "查询公司车队树",businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCompanyCarGroupTree",notes = "公司车队树",httpMethod ="POST")
    @PostMapping("/getCompanyCarGroupTree")
    public ApiResponse<List<CompanyCarGroupTreeVO>> getCompanyCarGroupTree(
            @RequestBody EcmpOrgDto ecmpOrgDto){
        //根据公司id查询车队列表
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long companyId = loginUser.getUser().getOwnerCompany();
        List<CompanyCarGroupTreeVO>  list = null;
        try {
            list = ecmpOrgService.selectCompanyCarGroupTree(companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(list);
    }*/


    /**
     * 查询公司车队树升级版  满足前端
     * @param
     * @return
     */
    @Log(title = "车队管理",content = "查询升级版公司车队树", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getNewCompanyCarGroupTree",notes = "查询公司车队树升级版  满足前端",httpMethod ="POST")
    @PostMapping("/getNewCompanyCarGroupTree")
    public ApiResponse<List<CarGroupTreeVO>> getNewCompanyCarGroupTree(
            @RequestBody EcmpOrgDto ecmpOrgDto){
        //根据公司id查询车队列表
        List<CarGroupTreeVO>  list = null;
        try {
            list = ecmpOrgService.selectNewCompanyCarGroupTree(ecmpOrgDto.getDeptId(),null);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("车队树查询失败,公司id:{}",ecmpOrgDto.getDeptId());
            return ApiResponse.error("车队树查询失败");
        }
        return ApiResponse.success(list);
    }

    /**
     * 查询  公司树
     * @param
     * @return
     */
   /* @Log(title = "车队管理",content = "查询公司树", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCompanyTree",notes = "公司树",httpMethod ="POST")
    @PostMapping("/getCompanyTree")
    public ApiResponse<List<CompanyTreeVO>> getCompanyTree(
            @RequestBody EcmpOrgDto ecmpOrgDto){
        //根据公司id查询公司树
        List<CompanyTreeVO>  list = null;
        try {
            list = ecmpOrgService.getCompanyTree(ecmpOrgDto.getDeptId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(list);
    }*/

    /**
     * 根据分/子公司id查询  车队树
     * @param
     * @return
     */
    /*@Log(title = "车队管理",content = "查询车队树", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCarGroupTree",notes = "公司树",httpMethod ="POST")
    @PostMapping("/getCarGroupTree")
    public ApiResponse<List<CarGroupTreeVO>> getCarGroupTree(
            @RequestBody EcmpOrgDto ecmpOrgDto){
        //根据公司id查询公司树
        List<CarGroupTreeVO>  list = null;
        try {
            list = carGroupInfoService.selectCarGroupTree(ecmpOrgDto.getDeptId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(list);
    }*/


    /**
     * （分、子）公司车队人数统计
     * @param
     * @return
     */
    @Log(title = "车队管理",content = "公司车队总人数统计", businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCarGroupCount",notes = "车队人数统计",httpMethod ="POST")
    @PostMapping("/getCarGroupCount")
    public ApiResponse<CarGroupCountVO> getCarGroupCount(
            @RequestBody CarGroupCountDto carGroupCountDto){
        //根据公司id查询车队人数
        CarGroupCountVO carGroupCountVO = null;
        try {
            carGroupCountVO = ecmpOrgService.selectCarGroupCount(carGroupCountDto.getDeptId(),carGroupCountDto.getCarGroupId());
            return ApiResponse.success(carGroupCountVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("查询失败");
        }
    }

    /**
     * 判断车队编号是否存在
     * @param
     * @return
     */
    @Log(title = "车队管理",content = "判断车队编号存在与否", businessType = BusinessType.OTHER)
    @ApiOperation(value = "judgeCarGroupCode",notes = "判断车队编号是否存在",httpMethod ="POST")
    @PostMapping("/judgeCarGroupCode")
    public ApiResponse<Boolean> judgeCarGroupCode(
            @RequestBody CarGroupDTO carGroupDTO){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long ownerCompany = loginUser.getUser().getOwnerCompany();
        try {
           boolean exist = carGroupInfoService.judgeCarGroupCode(carGroupDTO.getCarGroupCode(),ownerCompany);
           return ApiResponse.success(exist);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("判断失败");
        }
    }

    /**
     * 车队信息回显
     * @param
     * @return
     */
    @Log(title = "车队管理", content = "车队信息回显",businessType = BusinessType.OTHER)
    @ApiOperation(value = "getCarGroupInfoFeedBack",notes = "车队信息回显",httpMethod ="POST")
    @PostMapping("/getCarGroupInfoFeedBack")
    public ApiResponse<CarGroupDTO> getCarGroupInfoFeedBack(
            @RequestBody CarGroupDTO carGroupDTO){
        try {
            CarGroupDTO result = carGroupInfoService.getCarGroupInfoFeedBack(carGroupDTO.getCarGroupId());
            return ApiResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("回显失败");
        }
    }

    /**
     * 车队名字校验 同一公司下，车队名不能重复
     * @param
     * @return
     */
    @Log(title = "车队管理",content = "车队名字校验", businessType = BusinessType.OTHER)
    @ApiOperation(value = "judgeCarGroupName",notes = "车队名字校验",httpMethod ="POST")
    @PostMapping("/judgeCarGroupName")
    public ApiResponse<Boolean> judgeCarGroupName(
            @RequestBody CarGroupDTO carGroupDTO){
        try {
            //如果已经存在 返回true 不存在则返回false
            Boolean exist = carGroupInfoService.judgeCarGroupName(carGroupDTO.getCarGroupName(),carGroupDTO.getCompanyId());
            return ApiResponse.success(exist);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("车队名校验失败");
        }
    }

}
