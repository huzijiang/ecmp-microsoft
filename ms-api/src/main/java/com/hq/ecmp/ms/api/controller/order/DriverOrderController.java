package com.hq.ecmp.ms.api.controller.order;

import com.alibaba.fastjson.JSONObject;
import com.hq.common.core.api.ApiResponse;
import com.hq.common.utils.ServletUtils;
import com.hq.core.aspectj.lang.enums.BusinessType;
import com.hq.core.aspectj.lang.enums.OperatorType;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.CommonConstant;
import com.hq.ecmp.interceptor.log.Log;
import com.hq.ecmp.mscore.domain.JourneyPassengerInfo;
import com.hq.ecmp.mscore.domain.OrderSettlingInfoVo;
import com.hq.ecmp.mscore.domain.ReassignInfo;
import com.hq.ecmp.mscore.dto.ContactorDto;
import com.hq.ecmp.mscore.dto.IsContinueReDto;
import com.hq.ecmp.mscore.dto.OrderViaInfoDto;
import com.hq.ecmp.mscore.service.IDriverOrderService;
import com.hq.ecmp.mscore.service.IOrderSettlingInfoService;
import com.hq.ecmp.mscore.service.OrderInfoTwoService;
import com.hq.ecmp.mscore.vo.OrderReassignVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName DriverOrderController
 * @Description TODO
 * @Author yj
 * @Date 2020/3/10 14:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/driverOrder")
public class DriverOrderController {

    @Autowired
    @Lazy
    IDriverOrderService iDriverOrderService;
    @Resource
    IOrderSettlingInfoService iOrderSettlingInfoService;
    @Resource
    OrderInfoTwoService orderInfoTwoService;



    @Autowired
    TokenService tokenService;

    @com.hq.core.aspectj.lang.annotation.Log(title = "司机状态变更接口",businessType = BusinessType.UPDATE,operatorType = OperatorType.MOBILE)
    @Log("司机状态变更接口")
    @ApiOperation(value = "司机状态变更接口", notes = "司机状态变更接口 eg 司机出发、司机到达、开始服务,服务完成")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "操作类型,1:司机出发 2 司机到达 3 开始服务，4 服务完成", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "currentPoint", value = "司机当前位置坐标,经纬度通过逗号分隔", required = true, paramType = "query", dataType = "String",example = "精度,维度"),
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/handleStatus", method = RequestMethod.POST)
    public ApiResponse handleStatus(@RequestParam("type") String type,
                                    @RequestParam("currentPoint") String currentPoint,
                                    @RequestParam("orderNo") String orderNo,
                                    @RequestParam("mileage") String mileage,
                                    @RequestParam("travelTime") String travelTime) {
        //需要处理4种情况 | 司机出发、司机到达、开始服务、服务完成
        //记录订单的状态跟踪表
        try {
            //获取调用接口的用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getDriver().getDriverId();
            iDriverOrderService.handleDriverOrderStatus(type,currentPoint,orderNo,userId,mileage,travelTime);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success();
    }

    @com.hq.core.aspectj.lang.annotation.Log(title = "司机完成订单接口",businessType = BusinessType.UPDATE,operatorType = OperatorType.MOBILE)
    @Log("司机完成订单接口")
    @ApiOperation(value = "司机完成订单接口", notes = "司机完成订单接口,返回还车还是继续用车的相关信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/completed", method = RequestMethod.POST)
    public ApiResponse<IsContinueReDto> completed(
                                 @RequestParam("orderNo") String orderNo) {
        //需要计算服务时长（从开始服务到服务完成的时长）、记录里程
        //判断此订单的司机和车辆的下一个订单是否是同一个订单，如果是则返回下一个任务信息、如果不是则返回提示语：司机下一个任务的时间
        IsContinueReDto aContinue = null;
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getDriver().getDriverId();
            aContinue = iDriverOrderService.isContinue(orderNo,String.valueOf(userId));
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
        return ApiResponse.success(aContinue);
    }

    @com.hq.core.aspectj.lang.annotation.Log(title = "司机开始或者结束等待接口",businessType = BusinessType.INSERT,operatorType = OperatorType.MOBILE)
    @Log("司机开始或者结束等待接口")
    @ApiOperation(value = "司机开始或者结束等待接口", notes = "司机开始或者结束等待接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isFinish", value = "开始或者结束等的状态 eg 1开始 2结束", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "currentPoint", value = "当前位置，（精度,维度）,经纬度以逗号分隔", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "waitingId", value = "订单轨迹标识 开启等待会传回一个等待轨迹标识，如果是关闭等待操作，则需要传此标识到后台", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/waitingOrder", method = RequestMethod.POST)
    public ApiResponse<JSONObject> waitingOrder(@RequestParam("isFinish") String isFinish,
                                    @RequestParam("currentPoint") String currentPoint,
                                    @RequestParam("orderNo") String orderNo,
                                    @RequestParam("waitingId")String waitingId) {
        //记录司机在点击开始或者结束等待的时间点
        Long traceId = null;
        try {
            //获取调用接口的用户信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getDriver().getDriverId();
            traceId = iDriverOrderService.waitingOrder(orderNo,isFinish,currentPoint, String.valueOf(userId), waitingId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("waitingId",traceId);
        return ApiResponse.success(jsonObject);
    }

    /**
    *   @author yj
    *   @Description  //TODO 只显示乘车人的电话，页面直接点击呼叫即可联系乘车人
    *   @Date 11:13 2020/3/12
    *   @Param  [token, orderNo]
    *   @return ApiResponse
    **/
    @com.hq.core.aspectj.lang.annotation.Log(title = "司机联系乘客",businessType = BusinessType.OTHER,operatorType = OperatorType.MOBILE)
    @Log("司机联系乘客")
    @ApiOperation(value = "联系乘客")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value ="/contactPassenger",method = RequestMethod.POST)
    public ApiResponse<ContactorDto> contactPassenger(@RequestParam("orderNo") String orderNo){
        ContactorDto contactorDto = null;
        try {
            List<JourneyPassengerInfo> infoWithPassengers = iDriverOrderService.getInfoWithPassenger(orderNo);
            contactorDto = new ContactorDto();
            if(infoWithPassengers.size()>0){
                JourneyPassengerInfo journeyPassengerInfo = infoWithPassengers.get(0);
                contactorDto.setName(journeyPassengerInfo.getName());
                contactorDto.setPhone(journeyPassengerInfo.getMobile());
            }
            contactorDto.setRoleName(CommonConstant.PASSENGER_ROLE);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
        return ApiResponse.success(contactorDto);
    }

    @com.hq.core.aspectj.lang.annotation.Log(title = "司机联系车队",businessType = BusinessType.OTHER,operatorType = OperatorType.MOBILE)
    @Log("联系车队")
    @ApiOperation(value = "联系车队")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/contactCarGroup",method = RequestMethod.POST)
    public ApiResponse<List<ContactorDto>> contactCarGroup(@RequestParam("orderNo") String orderNo){
        List<ContactorDto> infoWithCarGroup = null;
        try {
            infoWithCarGroup = iDriverOrderService.getInfoWithCarGroup(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
        return ApiResponse.success(infoWithCarGroup);
    }

    @com.hq.core.aspectj.lang.annotation.Log(title = "获取订单途径地信息(非包车)",businessType = BusinessType.OTHER,operatorType = OperatorType.MOBILE)
    @Log("获取订单途径地信息，非包车的公务自有车")
    @ApiOperation(value = "获取订单途径地信息，非包车的公务自有车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderNo", value = "订单号", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/getOrderViaInfos", method = RequestMethod.POST)
    public ApiResponse<List<OrderViaInfoDto>> getOrderViaInfos(@RequestParam("orderNo") String orderNo){
        List<OrderViaInfoDto> orderViaInfos = null;
        try {
            orderViaInfos = iDriverOrderService.getOrderViaInfos(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error();
        }
        return ApiResponse.success(orderViaInfos);
    }
    /**
     * 司机端费用上报提交
     */
    @ApiOperation(value = "addExpenseReport", notes = "司机端费用上报提交 ", httpMethod = "POST")
    @PostMapping("/addExpenseReport")
    public ApiResponse addExpenseReport(@RequestBody OrderSettlingInfoVo orderSettlingInfoVo){
        try {
            //获取登陆用户的信息
            HttpServletRequest request = ServletUtils.getRequest();
            LoginUser loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getDriver().getDriverId();
            Long companyId = loginUser.getUser().getDept().getCompanyId();
           int  i = iOrderSettlingInfoService.addExpenseReport(orderSettlingInfoVo,userId,companyId);
           if(i<0){
               return ApiResponse.error("司机端费用上报提交失败");
           }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success();
    }

    /**
     *查询改派记录
     */
    @ApiOperation(value = "reassignDetail",notes = "查询改派记录",httpMethod ="POST")
    @PostMapping("/reassignDetail")
    public ApiResponse<OrderReassignVO> reassignDetail(@RequestParam("orderNo") Long orderNo){
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
//        Long driverId = loginUser.getDriver().getDriverId();
        return ApiResponse.success(orderInfoTwoService.reassignDetail(orderNo,null));
    }
}
